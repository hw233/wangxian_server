package com.fy.engineserver.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.tool.message.DefaultMessageFactory;
import com.fy.engineserver.tool.message.VERIFY_USER_REQ;
import com.fy.engineserver.tool.message.VERIFY_USER_RES;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class VerifyServer implements ConnectionConnectedHandler, MessageHandler {	
//	protected static Logger logger = Logger.getLogger(VerifyServer.class);
public	static Logger logger = LoggerFactory.getLogger(VerifyServer.class);
	
	private static VerifyServer instance;
	private DefaultConnectionSelector selector ;
	private int port = 7777;
	
	private File userFile;
	
	private Map<String, HashMap<String , String>> users = new HashMap<String, HashMap<String,String>>();
	
	private final static String UNIVERSIAL_USER_NAME = "all";
	private VerifyServer(){
		
	}
	
	public String 默认打开编辑器的服务器 [] = {"国内本地测试","仙尊降世","ST","开发服"};
	
	public synchronized static VerifyServer getInstance(){
		if(instance == null){
			instance = new VerifyServer();
		}
		return instance;
	}
	
	public File getUserFile(){
		return userFile;
	}
	
	public void setUserFile(File userFile){
		this.userFile = userFile;
	}
	
	public int getPort(){
		return port;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	/**
	 * 是否可以打开编辑器
	 * @return
	 */
	public boolean isOpenBJQ(){
		try{
			String servername = GameConstants.getInstance().getServerName();
			for(String name:默认打开编辑器的服务器){
				if(name.equals(servername)){
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public void init() throws Exception{
		
		if(isOpenBJQ()){
			selector = new DefaultConnectionSelector();
			selector.setClientModel(false);
			selector.setPort(port);
			selector.setConnectionConnectedHandler(this);
			selector.init();
			
			loadUserFile();
		}
		ServiceStartRecord.startLog(this);
	}
	
	public void loadUserFile() throws Exception{
		if(userFile == null){
			throw new NullPointerException("用户配置文件为空，请配置");
		}
		
		if(userFile.isDirectory()){
			throw new IllegalArgumentException("用户配置文件为目录[" + userFile.getAbsolutePath() + "]，请配置");
		}
		
		if(!userFile.exists()){
			throw new IllegalArgumentException("用户配置文件[" + userFile.getAbsolutePath() + "]不存在，请检查");
		}
		
		users.clear();
		
		Document document = XmlUtil.load(new FileInputStream(userFile));
		Element element = document.getDocumentElement();
		Element[] ees = XmlUtil.getChildrenByName(element, "editor");
		for (int i = 0; i < ees.length; i++) {
			Element ee = ees[i];
			String editorName = XmlUtil.getAttributeAsString(ee, "name", null, TransferLanguage.getMap());
			if(editorName == null){
				System.out.println("无法载入索引为[" + i + "]的编辑器名称，跳过 ...");
				continue;
			}
			
			HashMap<String, String> userItems = new HashMap<String, String>();
			users.put(editorName, userItems);
			
			Element[] ues = XmlUtil.getChildrenByName(ee, "user");
			for (int j = 0; j < ues.length; j++) {
				Element ue = ues[j];
				String name = XmlUtil.getAttributeAsString(ue, "name", null, TransferLanguage.getMap());
				if(name == null){
					System.out.println("无法获得编辑器[" + editorName + "] 的索引为[" + j + "] 的用户名，该用户被禁用，请检查");
					continue;
				}
				
				String password = null;
				if(name.equals(UNIVERSIAL_USER_NAME)){
					password = "";
				}else{
					password = XmlUtil.getAttributeAsString(ue, "password", null, TransferLanguage.getMap());					
				}
				if(password == null){
					System.out.println("无法获得编辑器[" + editorName + "] 用户[" + name + "] 的密码，该用户被禁用，请检查");
					continue;
				}
				
				userItems.put(name, password);
			}
		}
	}
	
	public void connected(Connection conn) throws IOException {
		// TODO Auto-generated method stub
		conn.setMessageFactory(DefaultMessageFactory.getInstance());
		conn.setMessageHandler(this);
	}

	public void discardRequestMessage(Connection conn, RequestMessage req)
			throws ConnectionException {
		// TODO Auto-generated method stub

	}

	public ResponseMessage receiveRequestMessage(Connection conn,
			RequestMessage message) throws ConnectionException {
		// TODO Auto-generated method stub
		if(message instanceof VERIFY_USER_REQ){
			VERIFY_USER_REQ req = (VERIFY_USER_REQ)message;
			String userName = req.getUsername();
			String password = req.getPassword();
			String product = req.getProduct();
			
			byte result =1;
			String info = "";
			//verify
			if(!users.containsKey(product) || users.get(product) == null){
				result  = 1;
				info =Translate.text_6067 + new Date() + Translate.text_6068 + product + Translate.text_6069; 				
			}else{
				HashMap<String, String> userItems = users.get(product);
				if(userItems.containsKey(UNIVERSIAL_USER_NAME)){
					result = 0;
					info  = Translate.text_6070 + new Date() + Translate.text_6071 + userName + Translate.text_6068 + product + "]";
				}else{
					if(!userItems.containsKey(userName) || userItems.get(userName) == null){
						result = 1;
						info = Translate.text_6067 + new Date() + Translate.text_6072 + userName + Translate.text_6073;
					}else if(userItems.get(userName).equals(password)){
						result = 0;
						info  = Translate.text_6070 + new Date() + Translate.text_6071 + userName + Translate.text_6068 + product + "]";
					}else{
						result = 1;
						info  = Translate.text_6067 + new Date() + Translate.text_6074 + product + Translate.text_6071 + userName + "]";
					}
				}
			}
//			logger.info("[userName:"+userName+"][pass:"+password+"][result:"+result+"]["+info+"]");
			if(logger.isInfoEnabled())
				logger.info("[userName:{}][pass:{}][result:{}][{}]", new Object[]{userName,password,result,info});
			return new VERIFY_USER_RES(((VERIFY_USER_REQ) message).getSequnceNum() , result , info);
		}
		
		return new VERIFY_USER_RES(((VERIFY_USER_REQ) message).getSequnceNum() , (byte)1 , Translate.text_6067 + new Date() + Translate.text_6075);
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req,
			ResponseMessage res) throws ConnectionException {
		// TODO Auto-generated method stub

	}

	public RequestMessage waitingTimeout(Connection conn, long timeout)
			throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) throws Exception{
		int port = 7777;
		VerifyServer server = new VerifyServer();
		DefaultConnectionSelector selector = new DefaultConnectionSelector();
		selector.setClientModel(false);
		selector.setPort(port);
		selector.setConnectionConnectedHandler(server);
		selector.init();
	}
	
	public String[] getEditors(){
		return users.keySet().toArray(new String[users.size()]);
	}
	
	public String[] getUsers4Editor(String editorName){
		if(users.containsKey(editorName)){
			Map<String, String> userItems = users.get(editorName);
			return userItems.keySet().toArray(new String[userItems.size()]);
		}

		return new String[0];
	}
}
