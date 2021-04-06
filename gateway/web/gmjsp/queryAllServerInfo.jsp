<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%><%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%><%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%><%@page import="net.sf.json.JSONObject"%>
<%
        MieshiServerInfo[]  serverList = MieshiServerInfoManager.getInstance().getServerInfoList();
        Map<String, Object> result = new HashMap<String, Object>();
        for(MieshiServerInfo info : serverList){
                if(info != null && info.getRealname() != null && info.getServerUrl() != null){
                        result.put(info.getRealname(),info.getServerUrl());
                }
        }
        out.write(JSONObject.fromObject(result).toString());
%>
