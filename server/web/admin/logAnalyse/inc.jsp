<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%!/**
	 *fileFullName:日志文件全路径名
	 *commandFile:要执行的命令文件
	 *args 命令参数列表
	 */
	CompoundReturn getLogResult(String fileFullName, String commandFile, String... args) {
		String[] returnValues = {};
		CompoundReturn cr = CompoundReturn.createCompoundReturn();
		File file = new File(fileFullName);
		if (!file.exists()) {
			return cr.setBooleanValue(false).setStringValue("日志文件" + fileFullName + "不存在").setStringValues(returnValues);
		}
		file = new File(commandFile);
		if (!file.exists()) {
			return cr.setBooleanValue(false).setStringValue("命令文件" + commandFile + "不存在").setStringValues(returnValues);
		}
		String encoding = System.getProperty("file.encoding");
		InputStream is = null;
		InputStream error = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("/bin/sh /bin/chmod 777 " + commandFile);//设置执权限
			int setValue = process.waitFor();
			System.out.println("setValue:" + setValue);
			StringBuffer sbf = new StringBuffer("/bin/sh ");

			sbf.append(commandFile).append(" ");
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					sbf.append(" ").append(new String(args[i].getBytes(), encoding)).append(" ");
				}
			}

			System.out.println("命令:" + sbf.toString());

			process = Runtime.getRuntime().exec(sbf.toString());
			is = process.getInputStream();
			error = process.getErrorStream();
			if (error.available() > 0) {
				String errorString = "";
				String line = null;
				final BufferedReader br = new BufferedReader(new InputStreamReader(error));
				while ((line = br.readLine()) != null) {
					errorString += line;
				}
				return cr.setBooleanValue(false).setStringValue("执行异常:" + new String(errorString.getBytes(), encoding)).setStringValues(returnValues);
			}

			final BufferedReader br = new BufferedReader(new InputStreamReader(is));
			List<String> resList = new ArrayList<String>();
			String line = null;
			while ((line = br.readLine()) != null) {
				resList.add(line);
			}
			int runValue = process.waitFor();
			System.out.println("runValue:" + runValue);

			return cr.setBooleanValue(true).setStringValue("查询成功,记录总数:" + resList.size()).setStringValues(resList.toArray(returnValues));
		} catch (Exception e) {
			e.printStackTrace();
			return cr.setBooleanValue(false).setStringValue("异常[" + e.toString() + "]").setStringValues(returnValues);
		} finally {
			if (error != null) {
				try {
					error.close();
				} catch (Exception e) {

				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {

				}
			}
			if (process != null) {
				process.destroy();
			}
		}

	}

	boolean isValid(String value) {
		return value != null && !"".equals(value.trim());
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	String today() {
		return sdf.format(new Date());
	}%>