package com.fy.engineserver.gm.custom;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;

public class CustomManager {
//	 protected static Logger logger = Logger.getLogger(CustomManager.class.getName());
public	static Logger logger = LoggerFactory.getLogger(CustomManager.class.getName());
	  protected String customConfFile;
	  
	  private List<String> customs = new ArrayList<String>();
	  private static CustomManager self;
	  public static CustomManager getInstance(){
		  return self;
	  }
	  public void initialize()  {
			//......
			long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			try {
				customs = loadPage(customConfFile);
				self = this;
				System.out.println(this.getClass().getName() + " initialize successfully ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)+"]");
				if(logger.isInfoEnabled())
					logger.info("{} initialize successfully [{}][saveFile:{}]", new Object[]{this.getClass().getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now),customConfFile});
			} catch (Exception e) {
				if(logger.isInfoEnabled())
					logger.info("{} initialize fail [saveFile:{}]", new Object[]{this.getClass().getName(),customConfFile});
			}
		}
	
	  
	  public void insertCustom(String value,int index){
		    try {//测试是否是值改变了
		    customs.add(index, value);
			savePage(customs ,customConfFile);
//			logger.info("value: ["+value+"to "+customConfFile+"] insert success at"); 
			if(logger.isInfoEnabled())
				logger.info("value: [{}to {}] insert success at", new Object[]{value,customConfFile}); 
		} catch (Exception e) {
//			logger.info("value: ["+value+"to "+customConfFile+"] insert fail at"); 
			if(logger.isInfoEnabled())
				logger.info("value: [{}to {}] insert fail at", new Object[]{value,customConfFile}); 
		}
	  }
	  public void updateCustom(String value,int index){
		   try {
			customs.set(index, value);
			savePage(customs ,customConfFile);
//			   logger.info("value :["+value+"] at "+index+" update success  ");
			   if(logger.isInfoEnabled())
				   logger.info("value :[{}] at {} update success  ", new Object[]{value,index});
		} catch (Exception e) {
//			   logger.info("value :["+value+"] at "+index+" update fail  ");
			   if(logger.isInfoEnabled())
				   logger.info("value :[{}] at {} update fail  ", new Object[]{value,index});
		}
		
	  }
	 
	  public void  deleteCustom(int index){
		 try{
			 customs.remove(index);
			 savePage(customs ,customConfFile);
//			logger.info("index :["+index+"] delete success  ");
			if(logger.isInfoEnabled())
				logger.info("index :[{}] delete success  ", new Object[]{index});
		} catch (Exception e) {
//			logger.info("index :["+index+"] delete faile  ");
			if(logger.isInfoEnabled())
				logger.info("index :[{}] delete faile  ", new Object[]{index});
		}
	  }
	
	public  List<String> loadPage(String xmlname){
	    try {
			List<String> customs =new ArrayList<String>();	
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration customsConf[] = rootConf.getChildren();
			for(int i = 0 ; i < customsConf.length ; i++){
			  customs.add(customsConf[i].getAttribute("value"));		   
			}
			return customs;
		} catch (Exception e) {
			if(logger.isInfoEnabled())
				logger.info(" load page faile  ");
			return new ArrayList<String>();
		}
	}
	public  void  savePage(List<String> customs,String saveFile) throws Exception{
		Configuration rootConf = new DefaultConfiguration("customs", "-");
		for(String custom : customs){
		    Configuration customConf =new DefaultConfiguration("custom", "-");
			rootConf.addChild(customConf);
			customConf.setAttribute("value", custom);	
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf,"UTF-8");
    }
	public String getCustomConfFile() {
		return customConfFile;
	}
	public void setCustomConfFile(String customConfFile) {
		this.customConfFile = customConfFile;
	}
	public List<String> getCustoms() {
		return customs;
	}
	public void setCustoms(List<String> customs) {
		this.customs = customs;
	}
	
}
