<%@page import="com.fy.boss.authorize.model.Passport"%>
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
DefaultConnectionSelector selector = new DefaultConnectionSelector();
selector.init();
selector.setEnableHeapForTimeout(true);
selector.setMaxConnectionNum(256);
selector.setThreadPoolMaxPoolSize(256);
selector.setName("BossSystemClient");
selector.setClientModel(true);
selector.setHost("116.213.192.216");
selector.setPort(10010);
selector.setConnectionCreatedHandler(bossClientService);
bossClientService.setConnectionSelector(selector);

try
	{
	//String orderId = bossClientService.savingForChannelUser("trytry", 1, "纵游一卡通", "0001", "04040", 100, "巍巍昆仑", "feiliu_MIESHI", "android");
	//String orderId = bossClientService.savingForAppChinaUser("trytry", 1, "纵游一卡通", "0001", "04040", 100, "巍巍昆仑", "appchina_MIESHI", "android");
	Passport p = bossClientService.getPassportByUserName("atryarewty");
	out.println(p);
	}
	catch(Exception e)
	{
		out.println(new String(e.getMessage().getBytes(),"utf-8"));
	}
	
	
%>
