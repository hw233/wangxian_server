package com.fy.gamebase.monitor;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public interface JspProc {
	public void proc(Writer out, Map<String, String[]> request) throws IOException;
}
