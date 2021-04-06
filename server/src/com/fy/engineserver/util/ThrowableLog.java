package com.fy.engineserver.util;

/**
 * 
 * 
 * 
 */
public class ThrowableLog {
	/** 监视关键字 */
	private static final String monitorKeyword = "com.fy";
	/** 一共显示多少行本项目的错误 */
	private static final int maxLine = 5;

	public static String logToStderr(Throwable throwable) {
		StackTraceElement[] elements = throwable.getStackTrace();
		int collectioned = 0;
		boolean foundKeyword = false;
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < elements.length; i++) {
			String lineMsg = elements[i].toString();
			if (foundKeyword) {
				buffer.append(lineMsg);
				collectioned++;
			} else {
				buffer.append(lineMsg);
				boolean fitcheck = lineMsg.contains(monitorKeyword);
				if (fitcheck) {
					foundKeyword = true;
					collectioned++;
				}
			}
			if (collectioned >= maxLine) {
				break;
			}

		}
		System.err.println(buffer.toString());
		return buffer.toString();
	}
}
