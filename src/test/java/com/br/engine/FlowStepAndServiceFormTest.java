package com.br.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class FlowStepAndServiceFormTest {

	private MockedStatic<FlowQuery> selectData;

	@Before
	public void setUp() {
		selectData = Mockito.mockStatic(FlowQuery.class);
	}

	@After
	public void tearDown() {
		selectData.close();
	}

	private FlowStep step(String status, String answer) throws Exception {
		selectData.when(() -> FlowQuery.getStatusFlowApprove(any(), any(), any(), any(), any(), any()))
				.thenReturn(answer);
		return FlowStep.read("10", "101", "SPRQ", "26000001", "create", status);
	}

	@Test
	public void flowStep_readsEveryField() throws Exception {
		FlowStep s = step("20", "dept01 ; 20 ; 30 ; appr01 ; APPROVER ; 10 ; VACANT ; 1 ; amount ; >= ; 500 ; type ; = ; A");
		assertEquals("dept01", s.approver);
		assertEquals("30", s.next);
		assertEquals("appr01", s.nextApprover);
		assertEquals("APPROVER", s.nextGroup);
		assertEquals("10", s.previous);
		assertEquals("VACANT", s.previousApprover);
		assertEquals("1", s.interfaceM3);
		assertEquals("amount", s.nextCondition.field);
		assertEquals(">=", s.nextCondition.operator);
		assertEquals("500", s.nextCondition.value);
		assertEquals("type", s.previousCondition.field);
		assertFalse(s.isLast());
		assertFalse(s.isFirst());
	}

	@Test
	public void flowStep_shortAnswer_noConditions() throws Exception {
		FlowStep s = step("60", "it01 ; 60 ; 60 ; it01 ; IT ; 50 ; mgr01");
		assertEquals("", s.interfaceM3);
		assertEquals("", s.nextCondition.field);
		assertTrue(s.isLast());
	}

	@Test
	public void flowStep_startOfFlow_isFirst() throws Exception {
		assertTrue(step("00", "user01 ; 00 ; 10 ; mgr01 ; MANAGER ; 00 ; user01").isFirst());
	}

	private static String form(String extra) {
		return "{\"vApproval\":\"true\",\"vRequester\":\"u1\",\"vDepthead\":\"d1\",\"vApproveBy\":\"a1\""
				+ ",\"vRemark\":\"\",\"vVersion\":\"1.0.0\"" + extra + "}";
	}

	@Test
	public void serviceForm_titleAndRequestDate() throws Exception {
		ServiceForm f = ServiceForm.read(form(",\"reasonMain\":\"Fix\",\"vTitle\":\" Photoshop \""
				+ ",\"requestDateMain\":\"2026-01-01\",\"requestDateConstant\":\"2026-02-02\""));
		assertTrue(f.approval);
		assertEquals("Photoshop", f.title());
		assertEquals("2026-02-02", f.requestDate());
		assertEquals("Fix", ServiceForm.read(form(",\"reasonMain\":\"Fix\",\"vTitle\":\"\"")).title());
		assertEquals("2026-01-01", ServiceForm.read(form(",\"requestDateMain\":\"2026-01-01\"")).requestDate());
	}

	@Test
	public void serviceForm_outdatedMessage() throws Exception {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn("1.0.0");
		assertNull(ServiceForm.read(form("")).outdatedMessage("Can't Submit service number"));

		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn("1.0.1");
		assertEquals("Can't Submit service number, Please update your version to 1.0.1 :  1.0.0 (Click F5 button).",
				ServiceForm.read(form("")).outdatedMessage("Can't Submit service number"));
	}
}
