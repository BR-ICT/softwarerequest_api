package com.br.engine;

/**
 * One status of a request's flow as FlowQuery.getStatusFlowApprove reads it (SR_FLOWAPPROVE + VI_PROCESS):
 * its approver, the next and previous status with their approvers, and the conditions of those two
 * (SR_PROCESS PMCFLD / PMCOPR / PMCVAL).
 */
class FlowStep {

	static final String VACANT = "VACANT";

	final String status;
	final String approver;
	final String next;
	final String nextApprover;
	final String nextGroup;
	final String previous;
	final String previousApprover;
	final String interfaceM3;
	final Condition nextCondition;
	final Condition previousCondition;

	/** Variable (valueName of a form question), operator and value; an empty variable = no condition. */
	static class Condition {
		final String field;
		final String operator;
		final String value;

		Condition(String field, String operator, String value) {
			this.field = field;
			this.operator = operator;
			this.value = value;
		}

		@Override
		public String toString() {
			return field.isEmpty() ? "-" : field + " " + operator + " " + value;
		}
	}

	// getStatusFlowApprove answers "approver ; status ; next ; next approver ; next group ; previous ;
	// previous approver ; interface M3 ; next condition (3) ; previous condition (3)"
	private FlowStep(String status, String[] fields) {
		this.status = status;
		this.approver = field(fields, 0);
		this.next = field(fields, 2);
		this.nextApprover = field(fields, 3);
		this.nextGroup = field(fields, 4);
		this.previous = field(fields, 5);
		this.previousApprover = field(fields, 6);
		this.interfaceM3 = field(fields, 7);
		this.nextCondition = new Condition(field(fields, 8), field(fields, 9), field(fields, 10));
		this.previousCondition = new Condition(field(fields, 11), field(fields, 12), field(fields, 13));
	}

	static FlowStep read(String cono, String divi, String servicename, String serviceno, String requesttype,
			String status) throws Exception {
		return new FlowStep(status, FlowQuery
				.getStatusFlowApprove(cono, divi, servicename, serviceno, requesttype, status).split(";"));
	}

	/** End of the flow: Next Status points to itself. */
	boolean isLast() {
		return next.equals(status);
	}

	/** Start of the flow: Previous Status points to itself (a reject here cancels the request). */
	boolean isFirst() {
		return previous.equals(status);
	}

	private static String field(String[] fields, int index) {
		return index < fields.length ? fields[index].trim() : "";
	}

	@Override
	public String toString() {
		return "status " + status + " (" + approver + ", interface M3 " + interfaceM3 + "), next " + next + " ("
				+ nextApprover + " / " + nextGroup + ", if " + nextCondition + "), previous " + previous + " ("
				+ previousApprover + ", if " + previousCondition + ")";
	}
}
