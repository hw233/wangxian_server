package com.fy.engineserver.toolskill;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.toolskill.message.BUFF_REQ;
import com.fy.engineserver.toolskill.message.BUFF_RES;
import com.fy.engineserver.toolskill.message.CareerAndSkillFactory;
import com.fy.engineserver.toolskill.message.ICON_REQ;
import com.fy.engineserver.toolskill.message.ICON_RES;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class ToolSkillServer implements ConnectionConnectedHandler, MessageHandler{
	private DefaultConnectionSelector selector ;
	private int port = 7669;
	
//	static Logger logger = Logger.getLogger(ToolSkillServer.class);
public	static Logger logger = LoggerFactory.getLogger(ToolSkillServer.class);
	public ToolSkillServer(){
		
	}
	
	public void init() throws Exception{
		selector = new DefaultConnectionSelector();
		selector.setConnectionSendBufferSize(5 * 1024 * 1024);
		selector.setClientModel(false);
		selector.setPort(port);
		selector.setConnectionConnectedHandler(this);
		selector.init();
	}
	
	public void discardRequestMessage(Connection arg0, RequestMessage arg1)
			throws ConnectionException {
		// TODO Auto-generated method stub
//		logger.warn("任务服务器丢弃请求[" + arg1.getTypeDescription() + "]");
		if(logger.isWarnEnabled())
			logger.warn("任务服务器丢弃请求[{}]", new Object[]{arg1.getTypeDescription()});
	}

	public ResponseMessage receiveRequestMessage(Connection arg0,
			RequestMessage request) throws ConnectionException {
		// TODO Auto-generated method stub
		if(request instanceof ICON_REQ){
			ICON_REQ req = (ICON_REQ) request;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String[] imageNames =new String[0]; //IconStore.instance.getAllImages();
			
			int total = 0;
			int[] lengths = new int[imageNames.length];
			for (int i = 0; i < imageNames.length; i++) {
				byte[] data = new byte[0];//IconStore.instance.getImageData(imageNames[i]);
				try{
					baos.write(data);
				}catch(IOException e){
					throw new RuntimeException("写入图标[" + imageNames[i] + "]失败",e);
				}
				lengths[i] = data.length;
				total += data.length;
			}
			ICON_RES res = new ICON_RES(req.getSequnceNum() , imageNames , lengths , baos.toByteArray());

			return res;
		}else if(request instanceof BUFF_REQ){
			BUFF_REQ req = (BUFF_REQ)request;
			HashMap<String,BuffTemplate> map = BuffTemplateManager.getInstance().getTemplates();
			
			List<String> bns = new ArrayList<String>();
			BuffTemplate[]bts = map.values().toArray(new BuffTemplate[0]);
			
			Arrays.sort(bts , new Comparator<BuffTemplate>(){

				public int compare(BuffTemplate o1, BuffTemplate o2) {
					// TODO Auto-generated method stub
					return o1.getGroupId() - o2.getGroupId();
				}
				
			});
			
			for (int i = 0; i < bts.length; i++) {
				bns.add(bts[i].getName());
			}
			
			BUFF_RES res = new BUFF_RES(req.getSequnceNum() , bns.toArray(new String[0]));
			return res;
		}
		return null;
	}

	public void receiveResponseMessage(Connection arg0, RequestMessage arg1,
			ResponseMessage arg2) throws ConnectionException {
		// TODO Auto-generated method stub
//		logger.warn("任务服务器收到响应[" + arg2.getTypeDescription() + "]");
		if(logger.isWarnEnabled())
			logger.warn("任务服务器收到响应[{}]", new Object[]{arg2.getTypeDescription()});
	}

	public RequestMessage waitingTimeout(Connection arg0, long arg1)
			throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public void connected(Connection conn) throws IOException {
		// TODO Auto-generated method stub
		conn.setMessageFactory(CareerAndSkillFactory.getInstance());
		conn.setMessageHandler(this);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
