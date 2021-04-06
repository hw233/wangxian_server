<%@ page contentType="text/html;charset=utf-8"%>
<%@page import="com.xuanzhi.tools.transport.DefaultConnectionSelector"%>
<%@page import="com.fy.boss.client.BossClientService"%>
<%
	
	BossClientService bossClientService = BossClientService.getInstance();
/**
	    <bean id="boss_system_client" class="com.xuanzhi.tools.transport.DefaultConnectionSelector"
	            init-method="init" scope="singleton">
	            <property name="enableHeapForTimeout" value="true"></property>
	            <property name="maxConnectionNum" value="256"></property>
	            <property name="threadPoolMaxPoolSize" value="256"></property>
	            <property name="name" value="BossSystemClient"></property>
	            <property name="clientModel" value="true"></property>
	            <property name="host" value="116.213.142.183"></property>
	           <!--  <property name="port" value="10001"></property>--> 
	           <property name="port" value="10010"></property>
            	<property name="connectionCreatedHandler" ref="bossClientService"></property>
	    </bean>
*/
Thread.sleep(50000l);
out.println("ok");
	
%>
