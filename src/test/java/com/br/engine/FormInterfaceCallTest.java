package com.br.engine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FormInterfaceCallTest {

	private static final String POSTED = "{\"idcardMain\":\"123\"}";

	private static FormInterface.Request request(String mode) {
		return new FormInterface.Request("10", "101", "SPRQ", "26000001", "create", "00", "user01", mode, null,
				POSTED, "{}", "token", "");
	}

	@Test
	public void modeOf_flags() {
		assertEquals("interface", FormInterfaceCall.modeOf("1"));
		assertEquals("check", FormInterfaceCall.modeOf("2 "));
		assertEquals("", FormInterfaceCall.modeOf("0"));
		assertEquals("", FormInterfaceCall.modeOf(null));
	}

	@Test
	public void answerToSave_check_keepsPostedDataAndNoReference() {
		FormInterface.Answer saved = FormInterfaceCall.answerToSave(request("check"),
				new FormInterface.Answer("{\"x\":1}", "S001"));
		assertEquals(POSTED, saved.data);
		assertEquals("", saved.reference);
	}

	@Test
	public void answerToSave_interface_takesDataAndReference() {
		FormInterface.Answer saved = FormInterfaceCall.answerToSave(request("interface"),
				new FormInterface.Answer("{\"x\":1}", " S001 "));
		assertEquals("{\"x\":1}", saved.data);
		assertEquals("S001", saved.reference);
	}

	@Test
	public void answerToSave_interface_noData_keepsPosted() {
		FormInterface.Answer saved = FormInterfaceCall.answerToSave(request("interface"),
				new FormInterface.Answer(null, null));
		assertEquals(POSTED, saved.data);
		assertEquals("", saved.reference);
	}

	@Test
	public void answerToSave_interface_noAnswer_keepsPosted() {
		FormInterface.Answer saved = FormInterfaceCall.answerToSave(request("interface"), null);
		assertEquals(POSTED, saved.data);
		assertEquals("", saved.reference);
	}

	@Test
	public void request_nullsBecomeEmpty() {
		FormInterface.Request request = new FormInterface.Request("10", "101", "SPRQ", "", "create", "00", "u",
				"check", null, POSTED, "{}", null, null);
		assertEquals(0, request.ownedFlags.size());
		assertEquals("", request.token);
		assertEquals("", request.linkToken);
	}
}
