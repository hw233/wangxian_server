<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%!String tempShellDir = "tempShell";//临时命令文件夹名字

	Comparator<QueryCondition> order = new Comparator<QueryCondition>() {
		public int compare(QueryCondition o1, QueryCondition o2) {
			return o2.PRI - o1.PRI;
		}
	};
	/**
	 *contextPath:应用的根路径
	 *fileFullName:日志文件全路径名
	 *conditions:查询条件
	 */
	String encoding = System.getProperty("file.encoding");

	CompoundReturn getLogResult(String contextPath, String logFileFullName, List<QueryCondition> conditions) {
		String[] returnValues = {};
		CompoundReturn cr = CompoundReturn.createCompoundReturn();
		File file = new File(logFileFullName);
		if (!file.exists()) {
			return cr.setBooleanValue(false).setStringValue("日志文件" + logFileFullName + "不存在").setStringValues(returnValues);
		}
		cr = createCommandFile(contextPath, logFileFullName, conditions);
		if (!cr.getBooleanValue()) {
			return cr;
		}

		File commandFile = (File) cr.getObjValue();

		String commandFileName = commandFile.getAbsolutePath();
		InputStream is = null;
		InputStream error = null;
		Process process = null;
		try {
			System.out.println("命令commandFileName:" + commandFileName);
			process = Runtime.getRuntime().exec("/bin/sh /bin/chmod 777 " + commandFileName);//设置执权限
			int setValue = process.waitFor();
			System.out.println("设置文件执行权限返回:" + setValue);

			process = Runtime.getRuntime().exec("/bin/sh " + commandFileName);
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
			if (commandFile != null) {
				System.out.println("即将删除命令文件:" + commandFile.getAbsolutePath());
				commandFile.delete();
			}
		}

	}

	Random random = new Random();

	CompoundReturn createCommandFile(String contextPath, String logFileFullName, List<QueryCondition> conditions) {
		CompoundReturn cr = CompoundReturn.createCompoundReturn();
		FileOutputStream fos = null;
		try {
			if (conditions == null) {
				return cr.setBooleanValue(false).setStringValue("条件NUll");
			}
			if (conditions.size() == 0) {
				return cr.setBooleanValue(false).setStringValue("条件长度=0");
			}
			Collections.sort(conditions, order);

			String[] paths = logFileFullName.split("/");
			String fileName = contextPath + "/tempShellDir/" + paths[paths.length - 1] + SystemTime.currentTimeMillis() + "-" + random.nextInt(10000) + ".sh";
			File f = new File(fileName);
			if (f.exists()) {
				return cr.setBooleanValue(false).setStringValue("文件名居然存在:" + fileName).setObjValue(f);
			}

			System.out.println("[准备创建文件:" + f.getAbsolutePath() + "]");
			String fileContent = "#!/bin/sh \n /bin/cat " + logFileFullName + " ";
			for (QueryCondition queryCondition : conditions) {
				fileContent += queryCondition.getPickledCommand();
			}
			if (!f.getParentFile().exists()) {
				System.out.println("[准备创建文件:" + f.getAbsolutePath() + "] [父目录不存在]");
				if (f.getParentFile().mkdirs()) {
					System.out.println("[准备创建文件:" + f.getAbsolutePath() + "] [父目录不存在] [创建成功]");
				}
			}
			f.createNewFile();
			fos = new FileOutputStream(f);
			fos.write(fileContent.getBytes());
			fos.flush();
			System.out.println("[创建文件:" + f.getAbsolutePath() + "] [内容:" + fileContent + "]");
			return cr.setBooleanValue(true).setStringValue("创建脚本文件成功:" + fileContent).setObjValue(f);
		} catch (Exception e) {
			e.printStackTrace();
			return cr.setBooleanValue(false).setStringValue("创建脚本文件异常:" + e.toString());
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("致命错误:命令文件流关闭异常:" + e.toString());
				}
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

<%!enum ConditionType {
		且查找,
		且排除,
		或查找,
		或排除, ;
	}

	//查询条件
	class QueryCondition {
		ConditionType conditionType;//条件类型
		String[] conditions;
		int PRI;//优先级

		public QueryCondition(ConditionType conditionType, String[] conditions, int PRI) {
			this.conditions = conditions;
			this.conditionType = conditionType;
			this.PRI = PRI;
		}

		public QueryCondition(ConditionType conditionType, String condition, int PRI) {
			String[] tempArr = condition.split(System.getProperty("line.separator"));
			for (int i = 0; i < tempArr.length; i++) {
				tempArr[i] = tempArr[i].trim();
			}
			this.conditions = tempArr;
			this.conditionType = conditionType;
			this.PRI = PRI;
		}

		public void setConditionType(ConditionType conditionType) {
			this.conditionType = conditionType;
		}

		public ConditionType getConditionType() {
			return this.conditionType;
		}

		public void setCommand(String[] conditions) {
			this.conditions = conditions;
		}

		public String[] getConditions() {
			return this.conditions;
		}

		public String getPickledCommand() {
			StringBuffer sbf = new StringBuffer(" ");
			String parm = " ";
			String split = "&";
			switch (conditionType) {
			case 且查找:
				parm = " ";
				split = "&";
				break;
			case 且排除:
				parm = " -v ";
				split = "&";
				break;
			case 或查找:
				parm = " ";
				split = "|";
				break;
			case 或排除:
				parm = " -v ";
				split = "|";
				break;
			}
			sbf.append("|/bin/egrep ").append(parm).append(" \"");
			for (int i = 0; i < conditions.length; i++) {
				String condition = conditions[i];
				sbf.append(condition);
				if (i != conditions.length - 1) {
					sbf.append(split);
				}
			}
			sbf.append("\"");
			System.out.println("查询条件:[conditionType:" + conditionType + "] [conditions:" + Arrays.toString(conditions) + "] [结果] [" + sbf.toString() + "]");
			return sbf.toString();
		}
	}%>