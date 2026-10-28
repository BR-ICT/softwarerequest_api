package com.br.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** ApproveGuard.decide: who may approve / reject a request at a status. */
public class ApproveGuardTest {

	private static final String[] OPEN_AT_20 = { "20", "2" };

	private static String decide(String[] head, String approvedBy, String approvers, String caller,
			String approveBy) {
		return ApproveGuard.decide(head, approvedBy, approvers, "26000001", "20", true, caller, approveBy);
	}

	@Test
	public void approverOfTheStatus_mayApprove() {
		assertNull(decide(OPEN_AT_20, "", "mgr01", "mgr01", "mgr01"));
	}

	@Test
	public void oneOfSeveralApprovers_anyLetterCase_mayApprove() {
		assertNull(decide(OPEN_AT_20, "", "USER1, MGR01 ,USER3", "mgr01", "Mgr01"));
	}

	@Test
	public void callerIsNotVApproveBy_refused() {
		// vApproveBy is in the form: anyone could send another user's name
		String refusal = decide(OPEN_AT_20, "", "mgr01", "user99", "mgr01");
		assertTrue(refusal, refusal.contains("you are user99 but the form was sent as mgr01"));
	}

	@Test
	public void noCaller_refused() {
		assertTrue(decide(OPEN_AT_20, "", "mgr01", null, "mgr01").startsWith("Can't approve, you are "));
	}

	@Test
	public void notAnApproverOfTheStatus_refused() {
		String refusal = decide(OPEN_AT_20, "", "mgr01,mgr02", "user99", "user99");
		assertTrue(refusal, refusal.contains("user99 is not an approver of status 20 (mgr01,mgr02)"));
	}

	@Test
	public void vacantStatus_refused() {
		assertTrue(decide(OPEN_AT_20, "", "VACANT", "mgr01", "mgr01").contains("is not an approver"));
	}

	@Test
	public void alreadyApproved_refused() {
		// the second click of a double click: the first one is written already
		assertEquals("Can't approve, Service approve already by mgr01",
				decide(OPEN_AT_20, "mgr01", "mgr01", "mgr01", "mgr01"));
	}

	@Test
	public void requestMovedOn_refused() {
		// approved at 20 and rejected back to 10 later: FAAPBY of 20 is empty again, the old page must not approve 20
		String refusal = decide(new String[] { "10", "1" }, "", "mgr01", "mgr01", "mgr01");
		assertTrue(refusal, refusal.contains("is at status 10 now (not 20)"));
	}

	@Test
	public void completeOrCancelled_refused() {
		assertTrue(decide(new String[] { "20", "3" }, "", "mgr01", "mgr01", "mgr01").contains("already complete"));
		assertTrue(decide(new String[] { "20", "9" }, "", "mgr01", "mgr01", "mgr01").contains("already cancelled"));
	}

	@Test
	public void unknownRequest_refused() {
		assertTrue(decide(null, "", "mgr01", "mgr01", "mgr01").contains("does not exist"));
	}

	@Test
	public void reject_saysReject() {
		assertTrue(ApproveGuard.decide(OPEN_AT_20, "", "mgr01", "26000001", "20", false, "x", "mgr01")
				.startsWith("Can't reject, "));
	}
}
