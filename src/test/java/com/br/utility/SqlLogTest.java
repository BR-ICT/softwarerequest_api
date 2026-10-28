package com.br.utility;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/** The SQL SqlLog writes to the debug log: each ? replaced by its value. */
public class SqlLogTest {

	private static Map<Integer, Object> values(Object... list) {
		Map<Integer, Object> values = new HashMap<>();
		for (int i = 0; i < list.length; i++) {
			values.put(i + 1, list[i]);
		}
		return values;
	}

	@Test
	public void format_replacesEachMarkerInOrder() {
		assertEquals("SELECT * FROM T WHERE A = '10' AND B = 5 AND C = NULL",
				SqlLog.format("SELECT * FROM T WHERE A = ? AND B = ? AND C = ?", values("10", 5, null)));
	}

	@Test
	public void format_keepsQuestionMarkInsideLiteral() {
		assertEquals("SELECT 'why?' FROM T WHERE A = 'x'", SqlLog.format("SELECT 'why?' FROM T WHERE A = ?", values("x")));
	}

	@Test
	public void format_doublesQuotesInText() {
		assertEquals("UPDATE T SET R = 'it''s ok'", SqlLog.format("UPDATE T SET R = ?", values("it's ok")));
	}

	@Test
	public void format_leavesUnsetMarker() {
		assertEquals("WHERE A = 'a' AND B = ?", SqlLog.format("WHERE A = ? AND B = ?", values("a")));
	}
}
