/**
 * 
 */
package com.fy.engineserver.operating;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.operating.activities.ActivityItemManager;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * @author Administrator
 *
 */
public class RuntimeString {

	Hashtable<String, String> stringH;
	
	File file;
	
	Logger log=ActivityItemManager.log;
	
	static RuntimeString self;
	/**
	 * 
	 */
	public RuntimeString() {
		// TODO Auto-generated constructor stub
		this.stringH=new Hashtable<String, String>();
		RuntimeString.self=this;
	}
	
	public void init() throws Exception{
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.readFile();
		System.out.println("[系统初始化] [公告文字] [初始化完成] ["+this.getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"毫秒]");
	}
	
	private void readFile(){
		FileInputStream fis=null;
		try{
			fis=new FileInputStream(this.file);
			Document doc=null;
//			if(TransferLanguage.characterTransferormFlag){
				doc=XmlUtil.load(fis,"utf-8");
//			}else{
//				doc=XmlUtil.load(fis);
//			}
			Element root = doc.getDocumentElement();
			Element[] detail=XmlUtil.getChildrenByName(root, "detail");
			for(int i=0;i<detail.length;i++){
				String key=XmlUtil.getAttributeAsString(detail[i], "key", null);
				String value=XmlUtil.getAttributeAsString(detail[i], "value", null);
				this.stringH.put(key, value);
			}
			if(this.log.isInfoEnabled()){
//				this.log.info("[读取公告文字] [成功] [数量："+this.stringH.size()+"]");
				if(this.log.isInfoEnabled())
					this.log.info("[读取公告文字] [成功] [数量：{}]", new Object[]{this.stringH.size()});
			}
		}catch(Exception e){
			e.printStackTrace();
			this.log.error("[读取公告文字] [出现错误] [错误："+e+"]",e);
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void save(){
		try{
			this.outputDataFile();
			if(this.log.isInfoEnabled()){
//				this.log.info("[存储公告文字] [成功] [数量："+this.stringH.size()+"]");
				if(this.log.isInfoEnabled())
					this.log.info("[存储公告文字] [成功] [数量：{}]", new Object[]{this.stringH.size()});
			}
		}catch(Exception e){
			e.printStackTrace();
			this.log.error("[存储公告文字] [出现错误] [错误："+e+"]",e);
		}
	}
	
	private void outputDataFile() throws Exception{
		FileOutputStream output=null;
		try{
			StringBuffer sb=new StringBuffer();
			sb.append("<?xml version='1.0' encoding='gb2312'?>\n");
			sb.append("<runtimeString time='"+new Date()+"'>"+'\n');
			Enumeration<String> keys = this.stringH.keys();
			while(keys.hasMoreElements()){
				String key=keys.nextElement();
				String value=this.stringH.get(key);
				sb.append("<detail key='"+key+"' value='"+value+"'/>"+'\n');
			}
			sb.append("</runtimeString>");
			
			output = new FileOutputStream(this.file);
//			if(TransferLanguage.characterTransferormFlag){
				output.write(sb.toString().getBytes("utf-8"));
//			}else{
//				output.write(sb.toString().getBytes());
//			}
			
			output.flush();
			output.close();
		}finally{
			if(output!=null){
				output.close();
			}
		}
	}
	
	public static RuntimeString getInstance(){
		return RuntimeString.self;
	}
	
	public boolean add(String key,String value){
		if(this.stringH.containsKey(key)){
			return false;
		}else{
			this.stringH.put(key, value);
			if(this.log.isInfoEnabled()){
//				this.log.info("[添加公告信息] [KEY:"+key+"] [VALUE:"+value+"]");
				if(this.log.isInfoEnabled())
					this.log.info("[添加公告信息] [KEY:{}] [VALUE:{}]", new Object[]{key,value});
			}
			this.save();
			return true;
		}
	}
	
	public boolean remove(String key){
		if(this.stringH.containsKey(key)){
			this.stringH.remove(key);
			if(this.log.isInfoEnabled()){
//				this.log.info("[删除公告信息] [KEY:"+key+"]");
				if(this.log.isInfoEnabled())
					this.log.info("[删除公告信息] [KEY:{}]", new Object[]{key});
			}
			this.save();
			return true;
		}else{
			return false;
		}
	}
	
	public boolean edit(String key,String value){
		if(this.stringH.containsKey(key)){
			this.stringH.put(key, value);
			if(this.log.isInfoEnabled()){
//				this.log.info("[编辑公告信息] [KEY:"+key+"] [VALUE:"+value+"]");
				if(this.log.isInfoEnabled())
					this.log.info("[编辑公告信息] [KEY:{}] [VALUE:{}]", new Object[]{key,value});
			}
			this.save();
			return true;
		}else{
			return false;
		}
	}

	public Hashtable<String, String> getStringH() {
		return stringH;
	}
	
	public String getStringByKey(String key){
		if(this.stringH.containsKey(key)){
			return this.stringH.get(key);
		}else{
			return null;
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
