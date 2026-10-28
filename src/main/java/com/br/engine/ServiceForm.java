package com.br.engine;

import java.util.Objects;

import org.codehaus.jettison.json.JSONObject;

/**
 * The keys every form sends with a submit / approve / reject (vApproval, vRequester, vDepthead, vApproveBy,
 * vRemark, vVersion). The rest of the form stays in json.
 */
class ServiceForm {

	final JSONObject json;
	final boolean approval;
	final String requester;
	final String depthead;
	final String approveBy;
	final String remark;
	final String version;

	private ServiceForm(JSONObject json) throws Exception {
		this.json = json;
		this.approval = Boolean.valueOf(json.getString("vApproval"));
		this.requester = json.getString("vRequester");
		this.depthead = json.getString("vDepthead");
		this.approveBy = json.getString("vApproveBy");
		this.remark = json.getString("vRemark");
		this.version = json.getString("vVersion");
	}

	static ServiceForm read(String data) throws Exception {
		return new ServiceForm(new JSONObject(data));
	}

	// panelConstant of the forms: requestDateConstant; forms not renamed yet still send requestDateMain
	String requestDate() throws Exception {
		return json.has("requestDateConstant") ? json.getString("requestDateConstant")
				: json.getString("requestDateMain");
	}

	// vTitle: forms without reasonMain send their own title (e.g. SWRQ the software name)
	String title() throws Exception {
		String title = json.optString("vTitle", "").trim();
		return title.isEmpty() ? json.getString("reasonMain") : title;
	}

	/**
	 * null when vVersion matches APPCTL3 'SRQ', else the message for the user: an old frontend in the browser
	 * cache must reload (F5) before it can send.
	 */
	String outdatedMessage(String refused) throws Exception {
		String current = FlowQuery.checkVersion("SRQ");
		if (version != null && !version.isEmpty() && Objects.equals(current, version)) {
			return null;
		}
		return refused + ", Please update your version to " + current + " :  " + version + " (Click F5 button).";
	}

	@Override
	public String toString() {
		return "approval " + approval + ", requester " + requester + ", depthead " + depthead + ", approveBy "
				+ approveBy + ", remark " + remark + ", version " + version;
	}
}
