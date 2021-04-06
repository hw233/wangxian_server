package com.fy.engineserver.closebetatest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.XmlUtil;

public class AutoSendMail implements Runnable {
	
	protected static final Logger logger = LoggerFactory.getLogger(AutoSendMail.class.getName());
	private final int RESULT_COUNT = 100;
	private static AutoSendMail self;
	public static AutoSendMail getInstance(){
		return self;
	}
	private boolean running = false;
	
	private Thread localThread;
	/**
	 * 发送神卡所需等级
	 */
	private final int LEVEL = 30;
	/**
	 * 人物名与公测30元神奇卡号密码对应
	 */
	public static Hashtable<String,String> accountNumberMap = new Hashtable<String,String>();

	public File accountNumberMapFile;

	public File getAccountNumberMapFile() {
		return accountNumberMapFile;
	}
	public void setAccountNumberMapFile(File accountNumberMapFile) {
		this.accountNumberMapFile = accountNumberMapFile;
	}
	public void init() throws Exception{
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		System.out.println("[系统初始化] [自动发公测30元神奇卡号管理器] [初始化成功] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		if(logger.isInfoEnabled())
			logger.info("{} initialize successfully [{}]", new Object[]{this.getClass().getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-startTime)});
	}
	public void start() {
		if(!running) {
			running = true;
			localThread = new Thread(this);
			localThread.start();
		}
	}
	
	public void stop() {
		running = false;
		if(localThread != null){
			localThread.interrupt();
		}
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(running){
			try {
				Thread.sleep(300000);
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				if(running) {
					sendLogic();
//					logger.info("[执行活动任务] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)+"ms]");
					if(logger.isInfoEnabled())
						logger.info("[执行活动任务] [{}ms]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)});
				}
			} catch (Exception e) {
				e.printStackTrace();
//				logger.info("[执行活动任务异常] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis())+"ms]");
				if(logger.isInfoEnabled())
					logger.info("[执行活动任务异常] [{}ms]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis())});
			}
		}
	}

	public void sendLogic(){}
	private void writeAccountNumberMapXML(String filePath){
		if(accountNumberMap != null && !accountNumberMap.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version='1.0' encoding='gbk'?>\n");
			sb.append("<accountNumberMap>\n");
			if(accountNumberMap.keySet() != null){
				for(String playerName : accountNumberMap.keySet()){
					if(playerName != null && accountNumberMap.get(playerName) != null){
						sb.append("\t<player value='"+accountNumberMap.get(playerName)+"'>\n");
						sb.append("\t\t<![CDATA["+playerName+"]]>");
						sb.append("\t</player>\n");
					}
				}
			}
			sb.append("</accountNumberMap>");

			File file = new File(filePath);
			File bakfile1 = new File(filePath+"bak1");
			File bakfile2 = new File(filePath+"bak2");
			if(bakfile2.exists()){
				bakfile2.delete();
			}
			if(bakfile1.exists()){
				bakfile1.renameTo(bakfile2);
			}
			if(file.exists()){
				file.renameTo(bakfile1);
			}
			try {
				OutputStream os = new FileOutputStream(file);
				os.write(sb.toString().getBytes());
				os.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void readAccountNumberMapXML(File file) throws Exception{

		if(file != null && file.isFile() && file.exists()){
			Document dom = XmlUtil.load(file.getAbsolutePath());
			Element root = dom.getDocumentElement();
			Element eles[] = XmlUtil.getChildrenByName(root, "player");
			if(eles == null){
				return;
			}
			accountNumberMap = new Hashtable<String,String>();
			for(int i = 0 ; i < eles.length ; i++){
				Element e = eles[i];
				String key = null;
				try{
				    key = XmlUtil.getValueAsString(e, null).trim();
				}catch(Exception ee){
					ee.printStackTrace();
				}
				String value = XmlUtil.getAttributeAsString(e, "value",null, null);
				if(key != null && value != null)
					accountNumberMap.put(key, value);
			}
		}
	}
}
