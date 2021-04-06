<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,
				com.fy.engineserver.sprite.*,
				com.fy.engineserver.cache.*,
				com.fy.engineserver.cache.service.*,
				com.fy.engineserver.protobuf.*,
				com.xuanzhi.tools.cache.*"%>
<%
CreateTask task1 = new CreateTask(1004,200000);
task1.start();
CreateTask task2 = new CreateTask(201004,200000);
task2.start();
CreateTask task3 = new CreateTask(401004,200000);
task3.start();
CreateTask task4 = new CreateTask(601004,200000);
task4.start();
CreateTask task5 = new CreateTask(801004,200000);
task5.start();
%>
