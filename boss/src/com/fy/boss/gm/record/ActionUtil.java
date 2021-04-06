package com.fy.boss.gm.record;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fy.boss.gm.record.Action;
import com.fy.boss.gm.record.SrcFile;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;

public class ActionUtil {
	public static List<Action> loadActionPage(String xmlname){
		//可以获取gm操作日志
	    List<Action> acs =new ArrayList<Action>();	
		try {
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration filesConf[] = rootConf.getChildren();
			for(int i = 0 ; i < filesConf.length ; i++){
				Action ac = new Action();
				ac.setGmname(filesConf[i].getAttribute("gmname"));
			    ac.setAction(filesConf[i].getAttribute("action"));
			    acs.add(ac);
			}
			return acs;
		} catch (Exception e) {
		return new ArrayList<Action>();
		}
	}
	public static void  saveActionPage(List<Action> acs,String saveFile) throws Exception{
		//保存或更新gm操作日志
		Configuration rootConf = new DefaultConfiguration("actions", "-");
		for(Action ac : acs){
		    Configuration acConf =new DefaultConfiguration("action", "-");
			rootConf.addChild(acConf);
			acConf.setAttribute("gmname", ac.getGmname());
			acConf.setAttribute("action", ac.getAction());
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf);
    }
	public static List<SrcFile> loadFilePage(String xmlname){
		//可以获取一个保存聊天记录文件的一组文件地址
	    List<SrcFile> files =new ArrayList<SrcFile>();	
		try {
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration filesConf[] = rootConf.getChildren();
			for(int i = 0 ; i < filesConf.length ; i++){
				SrcFile sf = new SrcFile();
				sf.setDate(filesConf[i].getAttribute("createdate"));
			    sf.setSrcname(filesConf[i].getAttribute("src"));
			    files.add(sf);
			}
			return files;
		} catch (Exception e) {
			e.printStackTrace();
		return files;
		}
	}
	public static void  saveFilePage(List<SrcFile> files,String saveFile) throws Exception{
		//保存或更新包含聊天记录文件地址
		Configuration rootConf = new DefaultConfiguration("recordfiles", "-");
		for(SrcFile file : files){
		    Configuration fileConf =new DefaultConfiguration("recordfile", "-");
			rootConf.addChild(fileConf);
			fileConf.setAttribute("createdate", file.getDate());
			fileConf.setAttribute("src", file.getSrcname());
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf);
    }
}
