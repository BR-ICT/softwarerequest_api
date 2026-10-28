package com.br.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.Test;

/** The token of the approve link from email: base64 of "serviceno|status|approver|AhaToken". */
public class ApproveLinkTokenTest {

	private static String encode(String text) {
		return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void decodesTheLinkOfTheEmail() {
		// token of http://.../user/approve/create?...&serviceno=26000011&status=10&approver=WATTAN_SAE
		ApproveLinkToken link = ApproveLinkToken.decode("MjYwMDAwMTF8MTB8V0FUVEFOX1NBRXxBaGFUb2tlbg==");
		assertEquals("26000011", link.serviceno);
		assertEquals("10", link.status);
		assertEquals("WATTAN_SAE", link.approver);
	}

	@Test
	public void isForItsRequestAndStatusOnly() {
		ApproveLinkToken link = ApproveLinkToken.decode(encode("26000011|10|WATTAN_SAE|AhaToken"));
		assertTrue(link.isFor("26000011", "10"));
		assertFalse(link.isFor("26000012", "10"));
		assertFalse(link.isFor("26000011", "20"));
		assertFalse(link.isFor(null, null));
	}

	@Test
	public void refusesWhatIsNotALinkToken() {
		assertNull(ApproveLinkToken.decode(null));
		assertNull(ApproveLinkToken.decode(""));
		assertNull(ApproveLinkToken.decode("not base64 !"));
		assertNull(ApproveLinkToken.decode(encode("26000011|10|WATTAN_SAE|Other")));
		assertNull(ApproveLinkToken.decode(encode("26000011|10|WATTAN_SAE")));
		assertNull(ApproveLinkToken.decode(encode("26000011|10||AhaToken")));
		// a login JWT is not a link token
		assertNull(ApproveLinkToken.decode("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJYIn0.sig"));
	}
}
