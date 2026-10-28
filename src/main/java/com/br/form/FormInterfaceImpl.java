package com.br.form;

import com.br.engine.FormInterface;

/** Interface M3 of SWRQ: none, the form is saved as posted (no status of SWRQ is flagged PMM3IF). */
public class FormInterfaceImpl implements FormInterface {

	@Override
	public Answer call(Request request) throws Exception {
		return Answer.unchanged(request);
	}
}
