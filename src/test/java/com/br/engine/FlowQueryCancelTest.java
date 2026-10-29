package com.br.engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** FlowQuery.mayCancel: who may cancel a request (menus Approve / Monitoring). */
public class FlowQueryCancelTest {

	@Test
	public void requester_mayCancel_anyLetterCase() {
		assertTrue(FlowQuery.mayCancel("2", "somchai ", "MGR01", "SOMCHAI"));
	}

	@Test
	public void approverOfTheCurrentStatus_mayCancel() {
		assertTrue(FlowQuery.mayCancel("2", "SOMCHAI", "USER1, MGR01 ,USER3", "mgr01"));
	}

	@Test
	public void waitingForResubmit_mayCancel() {
		assertTrue(FlowQuery.mayCancel("1", "SOMCHAI", "SOMCHAI", "SOMCHAI"));
	}

	@Test
	public void partOfAName_refused() {
		// old LIKE '%user%': SOM could cancel the request of SOMCHAI, MGR the one waiting for MGR01
		assertFalse(FlowQuery.mayCancel("2", "SOMCHAI", "MGR01,USER3", "SOM"));
		assertFalse(FlowQuery.mayCancel("2", "SOMCHAI", "MGR01,USER3", "MGR"));
	}

	@Test
	public void otherUser_refused() {
		assertFalse(FlowQuery.mayCancel("2", "SOMCHAI", "MGR01", "USER9"));
	}

	@Test
	public void completeOrCancelled_refused() {
		assertFalse(FlowQuery.mayCancel("3", "SOMCHAI", "MGR01", "SOMCHAI"));
		assertFalse(FlowQuery.mayCancel("9", "SOMCHAI", "MGR01", "MGR01"));
	}

	@Test
	public void emptyUser_refused() {
		assertFalse(FlowQuery.mayCancel("2", "", "", ""));
		assertFalse(FlowQuery.mayCancel("2", "SOMCHAI", "MGR01", null));
	}

	@Test
	public void noFlowApproveRow_onlyRequester() {
		assertTrue(FlowQuery.mayCancel("2", "SOMCHAI", "", "SOMCHAI"));
		assertFalse(FlowQuery.mayCancel("2", "SOMCHAI", "", "MGR01"));
	}
}
