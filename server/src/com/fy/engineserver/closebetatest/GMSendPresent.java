package com.fy.engineserver.closebetatest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.XmlUtil;

public class GMSendPresent{
	private static GMSendPresent self;
	public static GMSendPresent getInstance(){
		return self;
	}
//	protected static final Log logger = LogFactory.getLog(AutoSendMail.class.getName());
public	static Logger logger = LoggerFactory.getLogger(AutoSendMail.class.getName());
	/**
	 * 人物名与账号密码对应
	 */
	public static List<Hashtable<String,String>> accountNumberList = new ArrayList<Hashtable<String,String>>();
	/**
	 * 人物名与礼品对应
	 */
	public static List<Hashtable<String,String>> presentList = new ArrayList<Hashtable<String,String>>();

	public File accountNumberListFile;
	public File presentListFile;

	public File getAccountNumberListFile() {
		return accountNumberListFile;
	}

	public void setAccountNumberListFile(File accountNumberListFile) {
		this.accountNumberListFile = accountNumberListFile;
	}

	public File getPresentListFile() {
		return presentListFile;
	}

	public void setPresentListFile(File presentListFile) {
		this.presentListFile = presentListFile;
	}

	public void init() throws Exception{
		try{
			if(accountNumberListFile != null && accountNumberListFile.isFile() && accountNumberListFile.exists())
				readAccountNumberListXML(accountNumberListFile);
			if(presentListFile != null && presentListFile.isFile() && presentListFile.exists())
				readPresentListXML(presentListFile);
			self = this;
			System.out.println(this.getClass().getName()+"资源成功加载");
		}catch(Exception e){
			System.out.println(this.getClass().getName()+"资源加载错误");
		}
	}

	public void sendAccount(int id){}
	
	public void sendPresent(String name){}
	
	public void sendMailPresent(int id,String comment,String[] articles){}
	public void writeAccountNumberListXML(String filePath){
		if(accountNumberList != null && !accountNumberList.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version='1.0' encoding='gbk'?>\n");
			sb.append("<accountNumberList>\n");
			for(Hashtable<String,String> hm : accountNumberList){
				if(hm != null && hm.keySet() != null){
					for(String playerName : hm.keySet()){
						if(playerName != null && hm.get(playerName) != null){
							sb.append("\t<player value='"+hm.get(playerName)+"'>\n");
							sb.append("\t\t<![CDATA["+playerName+"]]>");
							sb.append("\t</player>\n");
						}
					}
				}
			}
			sb.append("</accountNumberList>");

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
	public void writePresentListXML(String filePath){
		if(presentList != null && !presentList.isEmpty()){
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version='1.0' encoding='gbk'?>\n");
			sb.append("<presentList>\n");
			for(Hashtable<String,String> hm : presentList){
				if(hm != null && hm.keySet() != null){
					for(String playerName : hm.keySet()){
						if(playerName != null && hm.get(playerName) != null){
							sb.append("\t<player value='"+hm.get(playerName)+"'>\n");
							sb.append("\t\t<![CDATA["+playerName+"]]>");
							sb.append("\t</player>\n");
						}
					}
				}
			}
			sb.append("</presentList>");

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
	public void readAccountNumberListXML(File file) throws Exception{

		if(file != null && file.isFile() && file.exists()){
			Document dom = XmlUtil.load(file.getAbsolutePath());
			Element root = dom.getDocumentElement();
			Element eles[] = XmlUtil.getChildrenByName(root, "player");
			if(eles == null){
				return;
			}
			accountNumberList = new ArrayList<Hashtable<String,String>>();
			for(int i = 0 ; i < eles.length ; i++){
				Element e = eles[i];
				String key = null;
				try{
				    key = XmlUtil.getValueAsString(e, null).trim();
				}catch(Exception ee){
					ee.printStackTrace();
				}
				String value = XmlUtil.getAttributeAsString(e, "value",null, null);
				if(key != null && value != null){
					Hashtable<String,String> hm = new Hashtable<String,String>();
					hm.put(key, value);
					accountNumberList.add(hm);
				}
			}
		}
	}
	public void readPresentListXML(File file) throws Exception{

		if(file != null && file.isFile() && file.exists()){
			Document dom = XmlUtil.load(file.getAbsolutePath());
			Element root = dom.getDocumentElement();
			Element eles[] = XmlUtil.getChildrenByName(root, "player");
			if(eles == null){
				return;
			}
			presentList = new ArrayList<Hashtable<String,String>>();
			for(int i = 0 ; i < eles.length ; i++){
				Element e = eles[i];
				String key = null;
				try{
					key = XmlUtil.getValueAsString(e, null).trim();
				}catch(Exception ee){
					ee.printStackTrace();
				}
				String value = XmlUtil.getAttributeAsString(e, "value",null, null);
				if(key != null && value != null){
					Hashtable<String,String> hm = new Hashtable<String,String>();
					hm.put(key, value);
					presentList.add(hm);
				}
			}
		}
	}
}
