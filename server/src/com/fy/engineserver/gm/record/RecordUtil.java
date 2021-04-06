package com.fy.engineserver.gm.record;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;

public class RecordUtil {
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
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf,"UTF-8");
    }
	public static List<RecordMessage> loadRecordPage(String xmlname){
		//加载一个文件读取某一天的聊天记录
		List<RecordMessage> rms = new ArrayList<RecordMessage>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration rmsConf[] = rootConf.getChildren();
			for(int i=0;i<rmsConf.length;i++){
				RecordMessage rm = new RecordMessage();
				rm.setFname(rmsConf[i].getAttribute("fname"));
				rm.setMessage(rmsConf[i].getAttribute("message"));
				rm.setToname(rmsConf[i].getAttribute("toname"));
				rm.setSendDate(rmsConf[i].getAttribute("senddate"));
				rms.add(rm);
			}
			return rms;
		} catch (Exception e) {
			e.printStackTrace();
			return rms;
		}
	}
	public  static void  saveRecordPage(List<RecordMessage> records,String saveFile) throws Exception{
		//保存或者更新某天一天的聊天记录
		Configuration rootConf = new DefaultConfiguration("daymessages", "-");
		for(RecordMessage rm : records){
		    Configuration rmConf =new DefaultConfiguration("daymessage", "-");
			rootConf.addChild(rmConf);
			rmConf.setAttribute("fname", rm.getFname());
			rmConf.setAttribute("message",rm.getMessage());
			rmConf.setAttribute("toname", rm.getToname());
			rmConf.setAttribute("senddate", rm.getSendDate());
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf,"UTF-8");
    }
	
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
			e.printStackTrace();
		return acs;
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
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf,"UTF-8");
    }
	public static List<BroadCastRecord> loadBroadCastPage(String xmlname){
		//可以获取gm操作日志
	    List<BroadCastRecord> bcs =new ArrayList<BroadCastRecord>();	
		try {
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration bcssConf[] = rootConf.getChildren();
			for(int i = 0 ; i < bcssConf.length ; i++){
				BroadCastRecord bc = new BroadCastRecord();
				bc.setMessage(bcssConf[i].getAttribute("message"));
			    bc.setCycletime(bcssConf[i].getAttribute("cycletime"));
			    bc.setEndDate(bcssConf[i].getAttribute("endDate"));
			    bc.setStartDate(bcssConf[i].getAttribute("startDate"));
			    bcs.add(bc);
			}
			return bcs;
		} catch (Exception e) {
			e.printStackTrace();
		return bcs;
		}
	}
	public static void  saveBroadCastPage(List<BroadCastRecord> bcs,String saveFile) throws Exception{
		//保存或更新gm操作日志
		Configuration rootConf = new DefaultConfiguration("broadcasts", "-");
		for(BroadCastRecord bc : bcs){
		    Configuration bcConf =new DefaultConfiguration("broadcast", "-");
			rootConf.addChild(bcConf);
			bcConf.setAttribute("message", bc.getMessage());
			bcConf.setAttribute("cycletime", bc.getCycletime());
			bcConf.setAttribute("endDate", bc.getEndDate());
			bcConf.setAttribute("startDate", bc.getStartDate());
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf,"UTF-8");
    }
	public static void  saveMailRecordPage(List<MailRecord> mrs,String saveFile) throws Exception{
		//保存或更新gm操作日志
		Configuration rootConf = new DefaultConfiguration("mails", "-");
		for(MailRecord mr : mrs){
		    Configuration acConf =new DefaultConfiguration("mail", "-");
			rootConf.addChild(acConf);
			acConf.setAttribute("gmname",mr.getGmname() );
			acConf.setAttribute("mid", mr.getMid());
			acConf.setAttribute("title",mr.getTitle());
			acConf.setAttribute("qcontent",mr.getQcontent() );
			acConf.setAttribute("rcontent",mr.getRescontent());
			acConf.setAttribute("items",mr.getItems() );
			acConf.setAttribute("rdate",mr.getResdate());
			acConf.setAttribute("cdate", mr.getCdate());
			acConf.setAttribute("playerid", mr.getPlayerid()+"");
			acConf.setAttribute("username", mr.getUsername());
			acConf.setAttribute("playername", mr.getPlayername());
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(new File(saveFile), rootConf,"UTF-8");
    }
	public static List<MailRecord> loadMailRecordPage(String xmlname){
		//可以获取gm操作日志
	    List<MailRecord> mrs =new ArrayList<MailRecord>();	
		try {
			Configuration rootConf = new DefaultConfigurationBuilder().buildFromFile(xmlname);
			Configuration filesConf[] = rootConf.getChildren();
			for(int i = 0 ; i < filesConf.length ; i++){
				MailRecord mr = new MailRecord();
				mr.setGmname(filesConf[i].getAttribute("gmname",""));
				mr.setMid(filesConf[i].getAttribute("mid",""));
				mr.setTitle(filesConf[i].getAttribute("title",""));
				mr.setQcontent(filesConf[i].getAttribute("qcontent",""));
				mr.setRescontent(filesConf[i].getAttribute("rcontent",""));
				mr.setItems(filesConf[i].getAttribute("items"," "));
				mr.setResdate(filesConf[i].getAttribute("rdate",""));
				mr.setUsername(filesConf[i].getAttribute("username",""));
				mr.setPlayerid(filesConf[i].getAttributeAsLong("playerid",-1)); 
				mr.setPlayername(filesConf[i].getAttribute("playername",""));
				mr.setCdate(filesConf[i].getAttribute("cdate",""));
				mrs.add(mr);
				
			}
			return mrs;
		} catch (Exception e) {
			e.printStackTrace();
		return mrs;
		}
	}
}
