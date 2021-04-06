package com.fy.engineserver.toolitems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.toolitems.message.BUFF_REQ;
import com.fy.engineserver.toolitems.message.BUFF_RES;
import com.fy.engineserver.toolitems.message.ItemEditorMessageFactory;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class ItemsMessageServer implements ConnectionConnectedHandler, MessageHandler{
	private DefaultConnectionSelector selector ;
	private int port = 7670;
//	static Logger logger = Logger.getLogger(ItemsMessageServer.class);
public	static Logger logger = LoggerFactory.getLogger(ItemsMessageServer.class);
	public ItemsMessageServer(){
		
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void init() throws Exception{
		selector = new DefaultConnectionSelector();
		selector.setConnectionSendBufferSize(5 * 1024 * 1024);
		selector.setClientModel(false);
		selector.setPort(port);
		selector.setConnectionConnectedHandler(this);
		selector.init();
	}
	
	public void connected(Connection conn) throws IOException {
		// TODO Auto-generated method stub
		conn.setMessageFactory(ItemEditorMessageFactory.getInstance());
		conn.setMessageHandler(this);
	}

	public void discardRequestMessage(Connection arg0, RequestMessage arg1)
			throws ConnectionException {
		// TODO Auto-generated method stub
//		logger.warn("Item服务器丢弃请求[" + arg1.getTypeDescription() + "]");
		if(logger.isWarnEnabled())
			logger.warn("Item服务器丢弃请求[{}]", new Object[]{arg1.getTypeDescription()});
	}

	public ResponseMessage receiveRequestMessage(Connection arg0,
			RequestMessage request) throws ConnectionException {
		// TODO Auto-generated method stub
		if(request instanceof BUFF_REQ){
			if(logger.isWarnEnabled())
				logger.warn("收到请求BUFF数据请求");
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			HashMap<String, BuffTemplate> hm = btm.getTemplates();
			ArrayList<String> al = new ArrayList<String>();
			if(hm != null && hm.keySet() != null){
				for(String str : hm.keySet()){
					if(str != null){
						al.add(str);
					}
				}
			}
			BUFF_RES res = new BUFF_RES(((BUFF_REQ)request).getSequnceNum(),al.toArray(new String[0]));
			return res;
		}
//		if(request instanceof TASK_REQ){
//			logger.warn("收到请求TASK数据请求");
//			TaskManager tm = TaskManager.getInstance();
//			Task[] tasks = tm.getAllTasks();
//			String[] taskdependencys = tm.getAllDependencys();
//			String[] results = null;
//			if(tasks != null){
//				results = new String[tasks.length];
//				for(int i = 0; i < tasks.length; i++){
//					Task task = tasks[i];
//					if(task != null){
//						results[i] = task.getDependency()+","+task.getName()+","+task.getId();
//					}
//				}
//			}
//			TASK_RES res = new TASK_RES(((TASK_REQ)request).getSequnceNum(),taskdependencys,results);
//			return res;
//		}
		return null;
	}

	public void receiveResponseMessage(Connection arg0, RequestMessage arg1,
			ResponseMessage arg2) throws ConnectionException {
		// TODO Auto-generated method stub
//		logger.warn("Item服务器收到响应[" + arg2.getTypeDescription() + "]");
		if(logger.isWarnEnabled())
			logger.warn("Item服务器收到响应[{}]", new Object[]{arg2.getTypeDescription()});
	}

	public RequestMessage waitingTimeout(Connection arg0, long arg1)
			throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}

}

