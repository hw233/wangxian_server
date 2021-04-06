package com.fy.engineserver.closebetatest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.XmlUtil;

public class FCAccountInfoManager {

	private static FCAccountInfoManager self;
	
	private FCAccountInfoManager(){
	}
	public File accountNumberTXTFile;
	public File sequenceTXTFile;
	public File fcAccountXML;
	/**
	 * 账号密码文件的当前发放到的位置，数组下标
	 */
	public static int accountNum = 0;
	/**
	 * 礼品序列号文件的当前发放到的位置，数组下标
	 */
	public static int sequenceNum = 0;

	/**
	 * 要发放的所有公测30元神奇卡号密码
	 */
	public static String[] accountNumbers = new String[0];
	/**
	 * 要发放的所有礼品序列号
	 */
	public static String[] sequences = new String[0];
	public static final int MAX_ACCOUNT_NO = 1;
	private ArrayList<FCAccountInfo> fcs = new ArrayList<FCAccountInfo>();

	public FCAccountInfo[] getFCArray(){
		if(fcs != null){
			return fcs.toArray(new FCAccountInfo[0]);
		}else{
			return new FCAccountInfo[0];
		}
	}
	public ArrayList<FCAccountInfo> getFcs() {
		return fcs;
	}
	public void setFcs(ArrayList<FCAccountInfo> fcs) {
		this.fcs = fcs;
	}
	public static FCAccountInfoManager getInstance(){
		if(self == null){
			self = new FCAccountInfoManager();
		}
		return self;
	}
	public File getAccountNumberTXTFile() {
		return accountNumberTXTFile;
	}
	public void setAccountNumberTXTFile(File accountNumberTXTFile) {
		this.accountNumberTXTFile = accountNumberTXTFile;
	}
	public File getSequenceTXTFile() {
		return sequenceTXTFile;
	}
	public void setSequenceTXTFile(File sequenceTXTFile) {
		this.sequenceTXTFile = sequenceTXTFile;
	}

	public File getFcAccountXML() {
		return fcAccountXML;
	}
	public void setFcAccountXML(File fcAccountXML) {
		this.fcAccountXML = fcAccountXML;
	}
	public void init() throws Exception{
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try{
			if(accountNumberTXTFile != null && accountNumberTXTFile.isFile() && accountNumberTXTFile.exists())
				readAccountNumberTXT(accountNumberTXTFile.getAbsolutePath());
//			if(sequenceTXTFile != null && sequenceTXTFile.isFile() && sequenceTXTFile.exists())
//				readSequenceTXT(sequenceTXTFile.getAbsolutePath());
			if(fcAccountXML != null && fcAccountXML.isFile() && fcAccountXML.exists())
				readFCAccountXML(fcAccountXML);
			self = this;
			System.out.println("[系统初始化] [自动发放账号卡号管理器] [初始化成功] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		}catch(Exception e){
			System.out.println("[系统初始化] [自动发放账号卡号管理器] [初始化失败] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		}
	}
	
	public String getFCAccount(String username) {
		FCAccountInfo[] fcas = getFCArray();
		String account = null;
		if(fcas != null){
			boolean exist = false;
			for(FCAccountInfo fc : fcas){
				if(fc != null && username.equals(fc.getFcAccount())){
					exist = true;
					if(fc.getCount() >= MAX_ACCOUNT_NO){
						break;
					}else{
						if(accountNumbers.length > accountNum){
							account = accountNumbers[accountNum];
							fc.setCount(fc.getCount()+1);
							accountNum++;
						}else{
							break;
						}
					}
				}
			}
			if(!exist){
				if(accountNumbers.length > accountNum){
					account = accountNumbers[accountNum];
					FCAccountInfo fc = new FCAccountInfo();
					fc.setFcAccount(username);
					fc.setCount(1);
					if(fcs == null){
						fcs = new ArrayList<FCAccountInfo>();
					}
					fcs.add(fc);
					accountNum++;
				}
			}
		}
		return account;
	}

	public String getPresentSequence() {
		String account = null;
		if(sequences.length > sequenceNum){
			account = sequences[sequenceNum];
			sequenceNum++;
		}
		return account;
	}

	/**
	 * 
	 * @param file
	 */
	private void readAccountNumberTXT(String file){
		String str = FileUtils.readFile(file);
		if(str != null){
			if(str.indexOf("\n") >= 0){
				FCAccountInfoManager.accountNumbers = str.split("\n");
			}else{
				FCAccountInfoManager.accountNumbers = new String[]{str};
			}
		}
	}
	/**
	 * 
	 * @param file
	 */
	private void readSequenceTXT(String file){
		String str = FileUtils.readFile(file);
		if(str != null){
			if(str.indexOf("\n") >= 0){
				sequences = str.split("\n");
			}else{
				sequences = new String[]{str};
			}
		}
	}
	/**
	 * 
	 * @param filePath
	 */
	public void writeFCAccountXML(){
		if(fcs != null && !fcs.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version='1.0' encoding='gbk'?>\n");
			sb.append("<fcList accountNum='"+FCAccountInfoManager.accountNum+"'>\n");
			for(FCAccountInfo fc : fcs){
				if(fc != null){
					sb.append("\t<fc value='"+fc.getCount()+"'>\n");
					sb.append("\t\t<![CDATA["+fc.getFcAccount()+"]]>");
					sb.append("\t</fc>\n");
				}
			}
			sb.append("</fcList>");
			String filePath = null;
			if(fcAccountXML != null){
				filePath = fcAccountXML.getAbsolutePath();
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
			}else{
				System.out.println(this.getClass().getName()+"文件存储错误，没有配置存储位置");
			}
			
		}
	}
	/**
	 * @param file
	 * @throws Exception
	 */
	private void readFCAccountXML(File file) throws Exception{

		if(file != null && file.isFile() && file.exists()){
			Document dom = XmlUtil.load(file.getAbsolutePath());
			Element root = dom.getDocumentElement();
			Element eles[] = XmlUtil.getChildrenByName(root, "fc");
			if(eles == null){
				return;
			}
			FCAccountInfoManager.accountNum = XmlUtil.getAttributeAsInteger(root, "accountNum",0);
			fcs = new ArrayList<FCAccountInfo>();
			for(int i = 0 ; i < eles.length ; i++){
				Element e = eles[i];
				String key = null;
				try{
				    key = XmlUtil.getValueAsString(e, null).trim();
				}catch(Exception ee){
					ee.printStackTrace();
				}
				int value = XmlUtil.getAttributeAsInteger(e, "value",0);
				if(key != null){
					FCAccountInfo fc = new FCAccountInfo();
					fc.setFcAccount(key);
					fc.setCount(value);
					fcs.add(fc);
				}
			}
		}
	}
}
