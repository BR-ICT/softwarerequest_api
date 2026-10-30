package com.br.engine;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.br.connection.ConnectDB2;
import com.sun.jersey.multipart.FormDataBodyPart;

/**
 * The request flow of every form (SR_FLOWHEAD / SR_FLOWDETAIL / SR_FLOWAPPROVE, Process Flow VI_PROCESS):
 * first submit (prepareAddServiceForm), approve / reject (prepareUpdateServiceForm), cancel.
 *
 * Same file in every form API (servicerequest_api, itemreq_api, supplier_request_api, customerrequest_api,
 * userrequest_api): change it in servicerequest_api first, then copy the whole com.br.engine package.
 * What only one form does (M3, duplicate checks) goes in that API's FormInterfaceImpl, never in here.
 */
public class ServiceFormEngine {

	private static final Logger logger = LogManager.getLogger(ServiceFormEngine.class);

	/** Start status of every flow: the REQUESTER submits (and submits again after a reject). */
	public static final String REQUESTER_STATUS = "00";

	private static final String DBNAME = EngineConfig.DBNAME;

	/**
	 * First submit of a new request (status 00, REQUESTER): creates the service number and its flow.
	 * Every check that can refuse the submit runs before the first write.
	 * token = the requester's x-access-token, passed on to the service's API (Interface M3 of the start status);
	 * caller = the user of that token: must be vRequester (nobody submits in another user's name).
	 */
	public static String prepareAddServiceForm(String cono, String divi, String servicename, String requesttype,
			String status, String data, List<FormDataBodyPart> file, List<FormDataBodyPart> filename,
			List<FormDataBodyPart> fieldname, String token, String caller) throws Exception {
		logger.info("prepareAddServiceForm");
		try {
			ServiceForm form = ServiceForm.read(data);
			String requestDate = form.requestDate();
			String title = form.title();
			logger.debug("{}", form);
			String outdated = form.outdatedMessage("Can't Create service number");
			if (outdated != null) {
				return Result.nok(outdated);
			}
			String refusal = ApproveGuard.submitRefusal(caller, form.requester);
			if (refusal != null) {
				logger.warn(refusal);
				return Result.nok(refusal);
			}

			// --- checks, nothing written yet ---
			// start status of the Process Flow: "code ; status ; group ; next status ; next approver ; next group"
			String[] start = FlowQuery.getStatusNewFlowApproveV2(cono, divi, servicename, requesttype, status)
					.split(";");
			String startGroup = start[2].trim();
			String nextStatus = start[3].trim();

			// Interface M3 Check of the start status (e.g. SPRQ refuses a tax ID that is already a supplier)
			FormInterface.Answer answer = FormInterfaceCall.beforeSubmit(cono, divi, servicename,
					requesttype, status, form.requester, token, data);
			data = answer.data;
			JSONObject json = new JSONObject(data);

			// approve mode ALL: a condition that cannot be checked throws here
			List<MultiApproveData.SubGroup> subGroups = MultiApproveData.buildSubGroups(cono, divi, servicename,
					requesttype, json);
			// lanes (sub group conditions of approve mode ANY): a condition that cannot be checked throws here
			List<String[]> subGroupApprovers = subGroupApprovers(cono, divi, servicename, requesttype, json);

			// --- writes ---
			String serviceNumber = createRequest(cono, divi, servicename, requesttype, nextStatus, requestDate, title,
					data, referenceToWrite(requesttype, answer, form, false), form);
			assignApprovers(cono, divi, servicename, serviceNumber, requesttype, json, subGroups, form.requester);
			saveSubGroupApprovers(cono, divi, servicename, serviceNumber, subGroupApprovers, form.requester);
			nextStatus = skipToFirstNeededStatus(cono, divi, servicename, serviceNumber, requesttype, status,
					nextStatus, data, json, form);

			// the requester's own status counts as approved by this submit
			if (startGroup.equals("REQUESTER")) {
				FlowUpdate.updateServiceFlowApproveBy(cono, divi, servicename, serviceNumber, status, "",
						form.requester);
			}
			// the request is written already: a file that cannot be saved is reported, not a reason to fail
			String fileWarning = "";
			if (file != null && !file.isEmpty()) {
				try {
					ServiceFile.prepareWriteToFileServer(cono, divi, servicename, serviceNumber, file, fieldname,
							form.requester);
				} catch (Exception e) {
					logger.error("Files of {} not saved: {}", serviceNumber, e.getMessage());
					fileWarning = " (attachment not saved: " + e.getMessage() + ", please attach it again)";
				}
			}

			Workflow.notify(cono, divi, servicename, serviceNumber, requesttype, nextStatus);
			return Result.ok("Service number " + serviceNumber + fileWarning);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Result.nok(e.getMessage());
		}
	}

	/**
	 * Head (new service number, title SERVICE-NUMBER-REQUESTTYPE-title, reference = referenceToWrite), detail (the
	 * form data) and one flow approve row per status of the Process Flow. Answers the new number.
	 */
	private static String createRequest(String cono, String divi, String servicename, String requesttype,
			String nextStatus, String requestDate, String title, String data, String reference, ServiceForm form)
			throws Exception {
		String serviceNumber = FlowInsert.addServiceHead(cono, divi, servicename, requestDate, nextStatus, form.requester,
				form.depthead);
		FlowInsert.addServiceDetail(cono, divi, servicename, serviceNumber, requesttype, data, nextStatus, form.requester,
				form.depthead);

		String fullTitle = servicename + "-" + serviceNumber + "-" + requesttype.toUpperCase() + "-" + title;
		FlowUpdate.updateServiceHeadTexts(cono, divi, servicename, serviceNumber, fullTitle, reference,
				form.requester);

		FlowInsert.addServiceFlowApprove(cono, divi, servicename, serviceNumber, requesttype);
		return serviceNumber;
	}

	/**
	 * The flow approve rows start as VACANT: the names of the System / Selector groups come from the form keys
	 * (vRequester, vDepthead, ...), approve mode ALL statuses get one row per sub group.
	 */
	private static void assignApprovers(String cono, String divi, String servicename, String serviceNumber,
			String requesttype, JSONObject json, List<MultiApproveData.SubGroup> subGroups, String requester)
			throws Exception {
		ServiceFormEngine.syncApproverNames(cono, divi, servicename, serviceNumber, requesttype, "", json, requester);
		if (!subGroups.isEmpty()) {
			MultiApproveData.saveSubGroups(cono, divi, servicename, serviceNumber, subGroups, requester);
		}
	}

	/**
	 * Skips like an approve does (e.g. SPRQ update has no selector at 10): head and detail move to the first
	 * status that needs an approver. Answers that status.
	 */
	private static String skipToFirstNeededStatus(String cono, String divi, String servicename,
			String serviceNumber, String requesttype, String status, String nextStatus, String data, JSONObject json,
			ServiceForm form) throws Exception {
		String needed = ServiceFormEngine.nextNeededStatus(cono, divi, servicename, serviceNumber, requesttype, status,
				json);
		if (!needed.equals(nextStatus)) {
			logger.info("Submit skips to status {} (instead of {})", needed, nextStatus);
			FlowUpdate.updateServiceHead(cono, divi, servicename, serviceNumber, nextStatus, needed, form.requester,
					form.depthead);
			FlowUpdate.updateServiceDetail(cono, divi, servicename, serviceNumber, nextStatus, needed, data,
					form.requester);
		}
		return needed;
	}

	// SR_FLOWHEAD.FHHSTA
	private static final String HEAD_WAIT_SUBMIT = "1";
	private static final String HEAD_WAIT_APPROVE = "2";
	private static final String HEAD_APPROVE_COMPLETE = "3";

	// SR_REQUESTTYPE.RQNAME of a new code (FHDES2 only once it is made, referenceToWrite)
	private static final String REQUEST_CREATE = "create";
	private static final String HEAD_CANCEL_COMPLETE = "9";

	/**
	 * Approve (vApproval = true) or reject (false) the request at its current status. Approving status 00 is
	 * the requester submitting again after a reject.
	 *
	 * One call at a time per request (one Tomcat): two approvers of a request at the same moment would
	 * each read and write the form data and the flow; the tables are not journaled, no transaction.
	 */
	/**
	 * token = the approver's x-access-token, linkToken = the token of the approve link from email
	 * (x-approve-token, approver not logged in); both passed on to the service's API (Interface M3).
	 * caller = the user of that token / link (api_data.approveCaller): must be vApproveBy (ApproveGuard).
	 */
	public static String prepareUpdateServiceForm(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status,
			String data, List<FormDataBodyPart> file, List<FormDataBodyPart> filename, List<FormDataBodyPart> fieldname,
			String token, String linkToken, String caller) throws Exception {
		Object lock = REQUEST_LOCKS.computeIfAbsent(cono + "/" + divi + "/" + servicename + "/" + serviceno,
				key -> new Object());
		synchronized (lock) {
			return updateServiceForm(cono, divi, servicename, serviceno, requesttype, status, data, file,
					fieldname, token, linkToken, caller);
		}
	}

	// one lock object per request, kept for the life of the API (a few bytes per request)
	private static final ConcurrentHashMap<String, Object> REQUEST_LOCKS = new ConcurrentHashMap<>();

	private static String updateServiceForm(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status, String data, List<FormDataBodyPart> file,
			List<FormDataBodyPart> fieldname, String token, String linkToken, String caller) throws Exception {
		logger.info("prepareUpdateServiceForm");
		try {
			ServiceForm form = ServiceForm.read(data);
			logger.debug("{}", form);
			String outdated = form.outdatedMessage("Can't Submit service number");
			if (outdated != null) {
				return Result.nok(outdated);
			}

			FlowStep step = FlowStep.read(cono, divi, servicename, serviceno, requesttype, status);
			logger.info("Flow {}", step);
			String refusal = ApproveGuard.refusal(cono, divi, servicename, serviceno, status, form.approval, caller,
					form.approveBy, step);
			if (refusal != null) {
				logger.warn(refusal);
				return Result.nok(refusal);
			}

			if (form.approval) {
				return approve(cono, divi, servicename, serviceno, requesttype, status, data, form, file, fieldname,
						token, linkToken);
			}
			return reject(cono, divi, servicename, serviceno, requesttype, status, data, form, step);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Result.nok(e.getMessage());
		}
	}

	/**
	 * Approve at status: the Interface M3 of the status first (its refusal writes nothing), then approve mode
	 * ALL (other sub groups may still have to approve), then the request moves to the next status that needs
	 * an approver and the workflow emails that approver.
	 */
	private static String approve(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status, String data, ServiceForm form, List<FormDataBodyPart> file,
			List<FormDataBodyPart> fieldname, String token, String linkToken) throws Exception {
		if (ServiceFormEngine.REQUESTER_STATUS.equals(status)) {
			resyncApproversOnResubmit(cono, divi, servicename, serviceno, requesttype, status, form);
		}

		// --- Interface M3 and approve mode ALL ---
		FormInterface.Answer answer = FormInterfaceCall.beforeApprove(cono, divi, servicename, serviceno,
				requesttype, status, form.approveBy, token, linkToken, data);
		data = answer.data;
		MultiApproveData.Outcome multiApprove = MultiApproveData.approve(cono, divi, servicename, serviceno, status,
				form.approveBy, form.remark);
		if (multiApprove.kind == MultiApproveData.Outcome.Kind.REFUSE) {
			return Result.nok("Can't approve, " + multiApprove.message);
		}
		// approve mode ALL: only this user's replies per line are taken (authorizeLines.ict*), the rest comes
		// from the DB, so parallel sub groups keep each other's replies
		String saveData = MultiApproveData.mergeApproverReplies(cono, divi, servicename, serviceno, requesttype,
				multiApprove, data);
		if (multiApprove.kind == MultiApproveData.Outcome.Kind.WAIT) {
			// other sub groups still have to approve: the status stays, no workflow email
			FlowUpdate.updateServiceDetail(cono, divi, servicename, serviceno, status, status, saveData, form.approveBy);
			writeHeadTexts(cono, divi, servicename, serviceno, requesttype, answer, form, false);
			return Result.ok(multiApprove.message
					+ saveFiles(cono, divi, servicename, serviceno, file, fieldname, form.approveBy));
		}

		// --- where the request goes (a condition that cannot be checked throws here, before the writes) ---
		FlowStep step = walkForward(cono, divi, servicename, serviceno, requesttype, status, new JSONObject(data));
		String nextStatus = step.next;

		// --- write the approve (ApproveGuard checked it is still open, one call at a time per request) ---
		FlowUpdate.updateServiceHead(cono, divi, servicename, serviceno, status, nextStatus, form.approveBy, form.depthead);
		FlowUpdate.updateServiceDetail(cono, divi, servicename, serviceno, status, nextStatus, saveData, form.approveBy);
		writeHeadTexts(cono, divi, servicename, serviceno, requesttype, answer, form, step.isLast());
		FlowUpdate.updateServiceFlowApproveBy(cono, divi, servicename, serviceno, status, form.remark, form.approveBy);
		MultiApproveData.finishApprove(cono, divi, servicename, serviceno, status, multiApprove, form.approveBy,
				form.remark);
		String fileWarning = saveFiles(cono, divi, servicename, serviceno, file, fieldname, form.approveBy);
		// the head is at nextStatus now (updateServiceHead above)
		FlowUpdate.updateServiceHeadStatus(cono, divi, servicename, serviceno, nextStatus,
				step.isLast() ? HEAD_APPROVE_COMPLETE : HEAD_WAIT_APPROVE, form.approveBy);

		Workflow.notify(cono, divi, servicename, serviceno, requesttype, nextStatus);
		return Result.ok("Complete" + fileWarning);
	}

	/**
	 * FHDES2: what the Interface of the status answers wins (a code that only exists once the ERP made it),
	 * else the form's vReference (the code the form decided, e.g. the customer code preview).
	 */
	static String referenceOf(FormInterface.Answer answer, ServiceForm form) {
		return answer.reference.isEmpty() ? form.reference() : answer.reference;
	}

	/**
	 * FHDES2 to write now, "" = keep the head's. A create only gets it when the request completes (FHHSTA 3):
	 * before that the code is only a preview (e.g. CMRQ customer code preview), not yet made in the ERP.
	 * Update / enable / disable name an existing code, written from the first submit.
	 */
	static String referenceToWrite(String requesttype, FormInterface.Answer answer, ServiceForm form,
			boolean complete) {
		return REQUEST_CREATE.equals(requesttype) && !complete ? "" : referenceOf(answer, form);
	}

	/**
	 * Title (FHDES1, SERVICE-NUMBER-REQUESTTYPE-title) and reference (FHDES2) again from the form of each
	 * resubmit / approve, so a name or code changed along the flow shows (a create: reference only on the
	 * approve that completes it, referenceToWrite). Written with the approve, after the Interface passed: a
	 * refused approve writes neither. An empty title or reference keeps what the head has.
	 */
	private static void writeHeadTexts(String cono, String divi, String servicename, String serviceno,
			String requesttype, FormInterface.Answer answer, ServiceForm form, boolean complete) throws Exception {
		String title = form.titleOrEmpty();
		String fullTitle = title.isEmpty() ? ""
				: servicename + "-" + serviceno + "-" + requesttype.toUpperCase() + "-" + title;
		FlowUpdate.updateServiceHeadTexts(cono, divi, servicename, serviceno, fullTitle,
				referenceToWrite(requesttype, answer, form, complete), form.approveBy);
	}

	/**
	 * Reject at status: the request goes back to the nearest previous status that needs an approver; a reject
	 * at the start of the flow cancels it. The workflow emails the approver of that status.
	 */
	private static String reject(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status, String data, ServiceForm form, FlowStep step) throws Exception {
		// the rejecter's replies are kept too (ICT Result is required on Reject as well)
		String saveData = MultiApproveData.mergeRejecterReplies(cono, divi, servicename, serviceno, requesttype,
				status, form.approveBy, data);

		// --- where the request goes (a condition that cannot be checked throws here, before the writes) ---
		String previousStatus = walkBackward(cono, divi, servicename, serviceno, requesttype, step, form.json);

		// --- write the reject (ApproveGuard checked it is still open, one call at a time per request) ---
		FlowUpdate.updateServiceHead(cono, divi, servicename, serviceno, status, previousStatus, form.approveBy,
				form.depthead);
		FlowUpdate.updateServiceDetail(cono, divi, servicename, serviceno, status, previousStatus, saveData, form.approveBy);
		// clear the approvals from the previous status up to this one (approve mode ALL: every sub group of
		// these statuses approves again), then this status is rejected by this user
		FlowUpdate.updateServiceFlowReject(cono, divi, servicename, serviceno, previousStatus, status, form.remark,
				form.approveBy);
		MultiApproveData.resetApprovals(cono, divi, servicename, serviceno, previousStatus, status, form.approveBy);
		FlowUpdate.updateServiceFlowRejectBy(cono, divi, servicename, serviceno, status, form.remark, form.approveBy);
		FlowUpdate.updateServiceHeadStatus(cono, divi, servicename, serviceno, previousStatus,
				status.equals(previousStatus) ? HEAD_CANCEL_COMPLETE : HEAD_WAIT_SUBMIT, form.approveBy);

		Workflow.notify(cono, divi, servicename, serviceno, requesttype, previousStatus);
		return Result.ok("Complete");
	}

	/**
	 * Submit again from 00 after a reject. Only the requester picks the approvers of the System / Selector
	 * groups (DEPTHEAD, APPROVER, ...), so their names and the approve mode ALL sub groups (whose flags may
	 * have changed) are written again from this form; later approvals never change them. Runs before the flow
	 * is read: a status whose name is not given stays VACANT and is skipped.
	 */
	private static void resyncApproversOnResubmit(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status, ServiceForm form) throws Exception {
		// built first, so a condition that cannot be checked throws before any write
		List<MultiApproveData.SubGroup> subGroups = MultiApproveData.buildSubGroups(cono, divi, servicename,
				requesttype, form.json);
		List<String[]> subGroupApprovers = subGroupApprovers(cono, divi, servicename, requesttype, form.json);
		syncApproverNames(cono, divi, servicename, serviceno, requesttype, status, form.json, form.approveBy);
		MultiApproveData.saveSubGroups(cono, divi, servicename, serviceno, subGroups, form.approveBy);
		saveSubGroupApprovers(cono, divi, servicename, serviceno, subGroupApprovers, form.approveBy);
	}

	/**
	 * Lanes of approve mode ANY. VI_PROCESS (so the first FAAPLI) holds only the default sub group (PMDES2 '1')
	 * of each status, and the condition of the default row is the condition of the status (false = the status
	 * is skipped, e.g. CMRQ GFD 60 creditLimit3Main >= 500001). The condition of any other sub group of the
	 * status picks its lane: the users of every sub group whose condition holds approve instead of the default
	 * (more than one = all their users, any one approves), e.g. CMRQ COLLECTION 30: LOCAL saleTypeMain <> 2,
	 * EXPORT saleTypeMain = 2. No lane holds, or a sub group without a condition or users = the default.
	 * Answers { status, users } of every status with more than one sub group, so a resubmit whose form now
	 * picks another lane writes them again. Read before any write: a condition that cannot be checked (field
	 * missing in the form) or users that do not fit FAAPLI throw.
	 */
	static List<String[]> subGroupApprovers(String cono, String divi, String servicename, String requesttype,
			JSONObject data) throws Exception {
		return chooseSubGroupApprovers(FlowQuery.getProcessSubGroupUsers(cono, divi, servicename, requesttype),
				data);
	}

	private static class Lane {
		final List<String> users = new ArrayList<>();
		boolean isDefault;
		String field = "", operator = "", value = "";
	}

	// rows = FlowQuery.getProcessSubGroupUsers: { status, sub group, PMDES2, user, PMCFLD, PMCOPR, PMCVAL }
	static List<String[]> chooseSubGroupApprovers(List<String[]> rows, JSONObject data) throws Exception {
		Map<String, Map<String, Lane>> statuses = new LinkedHashMap<>();
		for (String[] row : rows) {
			Lane lane = statuses.computeIfAbsent(row[0], s -> new LinkedHashMap<>()).computeIfAbsent(row[1],
					g -> new Lane());
			if (!row[3].isEmpty()) {
				lane.users.add(row[3]);
			}
			lane.isDefault = "1".equals(row[2]);
			lane.field = row[4];
			lane.operator = row[5];
			lane.value = row[6];
		}

		List<String[]> approvers = new ArrayList<>();
		for (Map.Entry<String, Map<String, Lane>> status : statuses.entrySet()) {
			Map<String, Lane> lanes = status.getValue();
			if (lanes.size() < 2) {
				continue;
			}
			Set<String> users = new LinkedHashSet<>();
			List<String> defaultUsers = new ArrayList<>();
			for (Lane lane : lanes.values()) {
				if (lane.isDefault) {
					defaultUsers = lane.users;
				} else if (!lane.field.isEmpty()
						&& conditionHolds(data, status.getKey(), lane.field, lane.operator, lane.value)) {
					users.addAll(lane.users);
				}
			}
			if (users.isEmpty()) {
				users.addAll(defaultUsers);
			}
			if (users.isEmpty()) {
				continue;
			}
			String list = String.join(",", users);
			if (list.length() > MultiApproveData.APPROVER_LIST_LENGTH) {
				throw new Exception("Status " + status.getKey() + " has too many approvers for one request ("
						+ list.length() + " characters, max " + MultiApproveData.APPROVER_LIST_LENGTH
						+ "). Remove users from its sub groups.");
			}
			approvers.add(new String[] { status.getKey(), list });
		}
		return approvers;
	}

	private static void saveSubGroupApprovers(String cono, String divi, String servicename, String serviceno,
			List<String[]> approvers, String username) throws Exception {
		for (String[] approver : approvers) {
			logger.info("Approver of status {} (sub group): {}", approver[0], approver[1]);
			FlowUpdate.updateServiceUserFlowApprove(cono, divi, servicename, serviceno, approver[0], username,
					approver[1]);
		}
	}

	/**
	 * Runs after the approve is written: a file that cannot be saved is reported in the answer ("" = all saved),
	 * a failed call would make the user approve again, which ApproveGuard refuses.
	 */
	private static String saveFiles(String cono, String divi, String servicename, String serviceno,
			List<FormDataBodyPart> file, List<FormDataBodyPart> fieldname, String username) {
		if (file == null || file.isEmpty()) {
			return "";
		}
		try {
			ServiceFile.prepareUpdateToFileServer(cono, divi, servicename, serviceno, file, fieldname, username);
			return "";
		} catch (Exception e) {
			logger.error("Files of {} not saved: {}", serviceno, e.getMessage());
			return " (attachment not saved: " + e.getMessage() + ", please attach it again)";
		}
	}

	/**
	 * System / Selector groups (SR_GROUP.GMDES2 1 or 2) have no fixed users: SR_FLOWAPPROVE.FAAPLI starts
	 * as VACANT and the name comes from the form, in the key kept in SR_GROUP.GMDES1 of the group:
	 * REQUESTER -> vRequester, DEPTHEAD -> vDepthead, SALES -> vSalesdirector, ...
	 * Writes that name to every such status of the request (afterStatus = only the statuses after it,
	 * empty = all). A name that is missing or empty means no approver: the status is set (back) to
	 * VACANT and the engine skips it - picking an approver is optional, and a requester who removes
	 * one after a reject removes that step. So a form must send every name each time, not only the
	 * changed ones. A group without a key in GMDES1 throws: its approver would be skipped silently.
	 */
	static void syncApproverNames(String cono, String divi, String servicename, String serviceno,
			String requesttype, String afterStatus, JSONObject data, String username) throws Exception {
		List<String[]> statuses = FlowQuery.getFlowApproveNamedGroups(cono, divi, servicename, serviceno,
				requesttype, afterStatus);
		for (String[] row : statuses) {
			String status = row[0], group = row[1], currentName = row[2], formKey = row[3];
			if (formKey.isEmpty()) {
				throw new Exception("Group " + group + " of status " + status
						+ " has no form key (SR_GROUP.GMDES1), the approver's name cannot be read.");
			}
			String name = approverName(data, formKey);
			if (name.isEmpty()) {
				name = "VACANT";
			}
			if (name.equals(currentName)) {
				continue;
			}
			logger.info("Approver of status {} ({}): {} -> {}", status, group, currentName, name);
			FlowUpdate.updateServiceUserFlowApprove(cono, divi, servicename, serviceno, status, username, name);
		}
	}

	// Value of the form key of a group (SR_GROUP.GMDES1, any letter case), e.g. vDepthead; "" when the
	// form has no such key
	static String approverName(JSONObject data, String formKey) {
		for (Iterator<?> keys = data.keys(); keys.hasNext();) {
			String key = String.valueOf(keys.next());
			if (key.equalsIgnoreCase(formKey)) {
				return data.isNull(key) ? "" : data.optString(key, "").trim();
			}
		}
		return "";
	}

	/**
	 * Whether the engine skips the next status of step (approve and submit): its approver is VACANT, or its
	 * condition is not met by the form data. A status in approve mode ALL has its condition per sub group:
	 * skipped only when all are VACANT.
	 */
	static boolean skipsNext(String cono, String divi, String servicename, String serviceno, FlowStep step,
			JSONObject data) throws Exception {
		return step.nextApprover.equals(FlowStep.VACANT)
				|| (!MultiApproveData.hasSubGroups(cono, divi, servicename, serviceno, step.next)
						&& !conditionHolds(data, step.next, step.nextCondition.field, step.nextCondition.operator,
								step.nextCondition.value));
	}

	/**
	 * From status forward, past every next status skipsNext skips: answers the step whose Next Status is where
	 * the request goes (the end of the flow when isLast). Throws on a flow that loops.
	 */
	static FlowStep walkForward(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status, JSONObject data) throws Exception {
		Set<String> visited = new HashSet<>();
		visited.add(status);
		FlowStep step = FlowStep.read(cono, divi, servicename, serviceno, requesttype, status);
		logger.info("Flow {}", step);
		while (!step.isLast() && skipsNext(cono, divi, servicename, serviceno, step, data)) {
			String skipped = step.nextApprover.equals(FlowStep.VACANT) ? "VACANT" : "skipped";
			if (!visited.add(step.next)) {
				throw new Exception("Process Flow " + servicename + " / " + requesttype + " loops through " + skipped
						+ " status " + step.next + ". Check Next Status in Process Flow.");
			}
			logger.info("Skips {} status {}", skipped, step.next);
			step = FlowStep.read(cono, divi, servicename, serviceno, requesttype, step.next);
			logger.info("Flow {}", step);
		}
		return step;
	}

	/**
	 * Submit: the status after fromStatus that needs an approver (walkForward). The approver names of
	 * SR_FLOWAPPROVE must be synced first.
	 */
	static String nextNeededStatus(String cono, String divi, String servicename, String serviceno,
			String requesttype, String fromStatus, JSONObject data) throws Exception {
		return walkForward(cono, divi, servicename, serviceno, requesttype, fromStatus, data).next;
	}

	/**
	 * Reject: the nearest previous status of step that needs an approver, past VACANT statuses and those whose
	 * condition is not met. Going back to step itself (start of the flow) is a cancel, so its own condition is
	 * not looked at. Throws on a flow that loops or has nowhere to go back to.
	 */
	private static String walkBackward(String cono, String divi, String servicename, String serviceno,
			String requesttype, FlowStep step, JSONObject data) throws Exception {
		Set<String> visited = new HashSet<>();
		visited.add(step.status);
		String previous = step.previous;
		String previousApprover = step.previousApprover;
		FlowStep.Condition condition = step.previousCondition;
		boolean skip = previousApprover.equals(FlowStep.VACANT) || (!previous.equals(step.status)
				&& !previousConditionHolds(data, cono, divi, servicename, serviceno, previous, condition));
		while (skip) {
			boolean vacant = previousApprover.equals(FlowStep.VACANT);
			String skipped = vacant ? "VACANT" : "skipped";
			if (!visited.add(previous)) {
				throw new Exception("Process Flow " + servicename + " / " + requesttype + " loops through " + skipped
						+ " status " + previous + ". Check Previous Status in Process Flow.");
			}
			FlowStep before = FlowStep.read(cono, divi, servicename, serviceno, requesttype, previous);
			if (before.isFirst()) {
				throw new Exception("Process Flow " + servicename + " / " + requesttype + " starts with status "
						+ previous + (vacant ? " that has no approver (VACANT)" : " whose condition is not met")
						+ ". Nowhere to send the reject back to.");
			}
			logger.info("Reject skips {} status {} -> {}", skipped, previous, before.previous);
			previous = before.previous;
			previousApprover = before.previousApprover;
			condition = before.previousCondition;
			skip = previousApprover.equals(FlowStep.VACANT)
					|| !previousConditionHolds(data, cono, divi, servicename, serviceno, previous, condition);
		}
		return previous;
	}

	/**
	 * Condition of a status (SR_PROCESS PMCFLD / PMCOPR / PMCVAL) against the form data of this call:
	 * true = the status needs its approver, false = skip it. No variable = no condition = true.
	 * The variable is the valueName of a form question. Both sides are compared as numbers when both are
	 * numbers; text can only be compared with = and <>. A condition that cannot be checked (variable
	 * missing or empty, text with a numeric operator) throws - it never skips or approves silently.
	 */
	static boolean conditionHolds(JSONObject data, String status, String field, String operator, String value)
			throws Exception {
		if (field.isEmpty()) {
			return true;
		}
		String condition = "Condition of status " + status + " (" + field + " " + operator + " " + value + ")";
		String actual = data.isNull(field) ? "" : String.valueOf(data.opt(field)).trim();
		if (actual.isEmpty()) {
			throw new Exception(condition + " cannot be checked: " + field + " is missing or empty in the form.");
		}

		BigDecimal actualNumber = toNumber(actual);
		BigDecimal valueNumber = toNumber(value);
		int compare;
		if (actualNumber != null && valueNumber != null) {
			compare = actualNumber.compareTo(valueNumber);
		} else if (operator.equals("=") || operator.equals("<>")) {
			compare = actual.equals(value) ? 0 : 1;
		} else {
			throw new Exception(condition + " cannot be checked: " + field + " = \"" + actual
					+ "\" is not a number.");
		}

		switch (operator) {
		case ">":
			return compare > 0;
		case ">=":
			return compare >= 0;
		case "<":
			return compare < 0;
		case "<=":
			return compare <= 0;
		case "=":
			return compare == 0;
		case "<>":
			return compare != 0;
		default:
			throw new Exception(condition + " cannot be checked: unknown operator.");
		}
	}

	// Reject: condition of a previous status; one in approve mode ALL is skipped only when all its sub
	// groups are VACANT (FAAPLI), its condition per sub group was applied when the rows were built
	private static boolean previousConditionHolds(JSONObject data, String cono, String divi, String servicename,
			String serviceno, String status, FlowStep.Condition condition) throws Exception {
		if (MultiApproveData.hasSubGroups(cono, divi, servicename, serviceno, status)) {
			return true;
		}
		return conditionHolds(data, status, condition.field, condition.operator, condition.value);
	}

	private static BigDecimal toNumber(String text) {
		try {
			return new BigDecimal(text.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String cancelServiceForm(String cono, String divi, String servicename, String serviceno,
			String username)
			throws Exception {
		logger.info("cancelServiceForm");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			Boolean checkCancelService = FlowQuery.checkCancelService(cono, divi, servicename, serviceno, username);

			if (checkCancelService) {
				String query = "UPDATE " + DBNAME + ".SR_FLOWHEAD \n"
						+ "SET FHHSTA = '9' \n"
						+ ", FHENDA = CURRENT DATE \n"
						+ ", FHENTI = CURRENT TIME \n"
						+ ", FHENUS = '" + username + "' \n"
						+ "WHERE FHCONO = '" + cono + "' \n"
						+ "AND FHDIVI = '" + divi + "' \n"
						+ "AND FHCODE = '" + servicename + "' \n"
						+ "AND FHSRNO = '" + serviceno + "'";
				// System.out.println("updateMARDetail\n" + query);
				logger.debug(query);
				stmt.execute(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Cancel complete.");
				logger.info("Update complete.");

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Cancel not complete.");
				logger.info("Update not complete.");

			}

			return mJsonObj.toString();

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}

		}

	}
}
