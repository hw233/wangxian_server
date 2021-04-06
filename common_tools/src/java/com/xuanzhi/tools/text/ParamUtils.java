package com.xuanzhi.tools.text;

import javax.servlet.http.HttpServletRequest;

/**
 * This class assists skin writers in getting parameters.
 */
public class ParamUtils {
	/**
	 * Gets a parameter as a string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            The name of the parameter you want to get
	 * @return The value of the parameter or null if the parameter was not found
	 *         or if the parameter is a zero-length string.
	 */
	public static String getParameter(HttpServletRequest request, String paramName) {
		return getParameter(request, paramName, false);
	}

	/**
	 * Gets a parameter as a string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            The name of the parameter you want to get
	 * @param emptyStringsOK
	 *            Return the parameter values even if it is an empty string.
	 * @return The value of the parameter or null if the parameter was not
	 *         found.
	 */
	public static String getParameter(HttpServletRequest request, String paramName, boolean emptyStringsOK) {
		String temp = request.getParameter(paramName);
		if (temp != null) {
			if (temp.equals("") && !emptyStringsOK) {
				return null;
			} else {
				return temp;
			}
		} else {
			return null;
		}
	}

	/**
	 * Gets a parameter as a string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            The name of the parameter you want to get
	 * @param emptyStringsOK
	 *            Return the parameter values even if it is an empty string.
	 * @return The value of the parameter or null if the parameter was not
	 *         found.
	 */
	public static String getParameter(HttpServletRequest request, String paramName, String defaultValue) {
		String temp = request.getParameter(paramName);
		if (temp != null) {
			if (temp.equals("")) {
				return defaultValue;
			} else {
				return temp;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * Gets a parameter as a boolean.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            The name of the parameter you want to get
	 * @return True if the value of the parameter was "true", false otherwise.
	 */
	public static boolean getBooleanParameter(HttpServletRequest request, String paramName) {
		String temp = request.getParameter(paramName);
		if (temp != null && temp.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets a parameter as a int.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            The name of the parameter you want to get
	 * @return The int value of the parameter specified or the default value if
	 *         the parameter is not found.
	 */
	public static int getIntParameter(HttpServletRequest request, String paramName, int defaultNum) {
		String temp = request.getParameter(paramName);
		if (temp != null && !temp.equals("")) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	public static long getLongParameter(HttpServletRequest request, String paramName, long defaultNum) {
		String temp = request.getParameter(paramName);
		if (temp != null && !temp.equals("")) {
			long num = defaultNum;
			try {
				num = Long.valueOf(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	public static float getFloatParameter(HttpServletRequest request, String paramName, float defaultNum) {
		String temp = request.getParameter(paramName);
		if (temp != null && !temp.equals("")) {
			float num = defaultNum;
			try {
				num = Float.valueOf(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	/**
	 * Gets a checkbox parameter value as a boolean.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            The name of the parameter you want to get
	 * @return True if the value of the checkbox is "on", false otherwise.
	 */
	public static boolean getCheckboxParameter(HttpServletRequest request, String paramName) {
		String temp = request.getParameter(paramName);
		if (temp != null && temp.equals("on")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets a parameter as a string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param attribName
	 *            The name of the parameter you want to get
	 * @return The value of the parameter or null if the parameter was not found
	 *         or if the parameter is a zero-length string.
	 */
	public static String getAttribute(HttpServletRequest request, String attribName) {
		return getAttribute(request, attribName, false);
	}

	/**
	 * Gets a parameter as a string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param attribName
	 *            The name of the parameter you want to get
	 * @param emptyStringsOK
	 *            Return the parameter values even if it is an empty string.
	 * @return The value of the parameter or null if the parameter was not
	 *         found.
	 */
	public static String getAttribute(HttpServletRequest request, String attribName, boolean emptyStringsOK) {
		String temp = (String) request.getAttribute(attribName);
		if (temp != null) {
			if (temp.equals("") && !emptyStringsOK) {
				return null;
			} else {
				return temp;
			}
		} else {
			return null;
		}
	}

	/**
	 * Gets an attribute as a boolean.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param attribName
	 *            The name of the attribute you want to get
	 * @return True if the value of the attribute is "true", false otherwise.
	 */
	public static boolean getBooleanAttribute(HttpServletRequest request, String attribName) {
		String temp = (String) request.getAttribute(attribName);
		if (temp != null && temp.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets an attribute as a int.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param attribName
	 *            The name of the attribute you want to get
	 * @return The int value of the attribute or the default value if the
	 *         attribute is not found or is a zero length string.
	 */
	public static int getIntAttribute(HttpServletRequest request, String attribName, int defaultNum) {
		String temp = (String) request.getAttribute(attribName);
		if (temp != null && !temp.equals("")) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	/**
	 * Gets a group of parameters with the same name as an int array.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param strParamName
	 *            The name of the parameter you want to get
	 * @return The values in the request as an int array, or 0 length array if
	 *         no values found.
	 */
	public static int[] getIntArrParameter(HttpServletRequest request, String strParamName) {
		String[] strTempArr = request.getParameterValues(strParamName);

		if (strTempArr == null) {
			return new int[0];
		}

		int[] iRetArr = new int[strTempArr.length];

		for (int i = 0; i < strTempArr.length; i++) {
			try {
				iRetArr[i] = Integer.parseInt(strTempArr[i]);
			} catch (Exception e) {
				iRetArr[i] = -1;
			}
		}

		return iRetArr;
	}
}
