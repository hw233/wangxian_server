package com.fy.engineserver.util.datacheck;

import java.util.Arrays;

public class SendHtmlToMail {

	private String[] titles;
	private String[] values;

	public SendHtmlToMail(String[] titles, String[] values) {
		this.titles = titles;
		this.values = values;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "SendHtmlToMail [titles=" + Arrays.toString(titles) + ", values=" + Arrays.toString(values) + "]";
	}

}
