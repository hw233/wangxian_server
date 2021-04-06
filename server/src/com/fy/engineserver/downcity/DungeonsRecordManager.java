/**
 * 
 */
package com.fy.engineserver.downcity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.billboard.BillboardManager;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;

/**
 * @author Administrator
 *
 */
public class DungeonsRecordManager implements ConfigFileChangedListener {
	
	Hashtable<String, ArrayList<DungeonRecord>> dungeonRecords;
	
	File dungeonNamesFile;
	
	File dungeonRecordsDataFile;
	
	static DungeonsRecordManager self;
	
//	static Logger logger = Logger.getLogger(DownCityManager.class);
public	static Logger logger = LoggerFactory.getLogger(DownCityManager.class);

	/**
	 * 
	 */
	public DungeonsRecordManager() {
		// TODO Auto-generated constructor stub
		DungeonsRecordManager.self=this;
	}
	
	public void init(){
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ConfigFileChangedAdapter.getInstance().addListener(this.dungeonNamesFile, this);
		this.dungeonRecords=new Hashtable<String, ArrayList<DungeonRecord>>();
		this.loadConfigFile(this.dungeonNamesFile);
		this.loadDungeonsDataFile(this.dungeonRecordsDataFile);
		System.out.println("[系统初始化] [副本通关记录管理器] [初始化完成] ["+this.getClass()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
	}

	/* (non-Javadoc)
	 * @see com.xuanzhi.tools.watchdog.ConfigFileChangedListener#fileChanged(java.io.File)
	 */
	public void fileChanged(File file) {
		// TODO Auto-generated method stub
		if(logger.isInfoEnabled()){
			logger.info("[更新副本目录] [开始更新]");
		}
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.loadConfigFile(file);
		this.loadDungeonsDataFile(this.dungeonRecordsDataFile);
		this.saveDungeonsRecordData();
		if(logger.isInfoEnabled()){
//			logger.info("[更新副本目录] [成功] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
			if(logger.isInfoEnabled())
				logger.info("[更新副本目录] [成功] [耗时：{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
		}
	}
	
	public static DungeonsRecordManager getInstance(){
		return self;
	}

	public File getDungeonNamesFile() {
		return dungeonNamesFile;
	}

	public void setDungeonNamesFile(File dungeonNamesFile) {
		this.dungeonNamesFile = dungeonNamesFile;
	}

	public File getDungeonRecordsDataFile() {
		return dungeonRecordsDataFile;
	}

	public void setDungeonRecordsDataFile(File dungeonRecordsDataFile) {
		this.dungeonRecordsDataFile = dungeonRecordsDataFile;
	}
	
	protected synchronized void update(DownCity dc,Player[] players, long completeTime){
		DownCityInfo di=dc.getDi();
		try{
			if(this.dungeonRecords.containsKey(di.getName())){
				ArrayList<DungeonRecord> al=this.dungeonRecords.get(di.getName());
				for(DungeonRecord dr:al){
					if(dr.getId().equals(dc.getId())){
						if(logger.isInfoEnabled()){
//							logger.info("[更新副本通关记录] [记录已存在] [副本："+di.getName()+"] [ID："+dc.getId()+"]");
							if(logger.isInfoEnabled())
								logger.info("[更新副本通关记录] [记录已存在] [副本：{}] [ID：{}]", new Object[]{di.getName(),dc.getId()});
						}
						return;
					}
				}
				DungeonRecord dr=new DungeonRecord(dc.getId());
				dr.setCompleteTime(completeTime);
				dr.setDungeonName(di.getName());
				dr.setDungeonMapName(di.getMapName());
				dr.setCreatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				Player[] p=players;
				if(p!=null&&p.length>0){
					String[] names=new String[p.length];
					String teamLeader="";
					byte camp=0;
					for(int i=0;i<names.length;i++){
						names[i]=p[i].getName();
						if(p[i].getTeamMark()==Player.TEAM_MARK_CAPTAIN){
							teamLeader=p[i].getName();
							camp=p[i].getCountry();
							dr.setTeamLeaderId(p[i].getId());
						}
					}
					if(teamLeader.length()<=0){
						teamLeader=p[0].getName();
						camp=p[0].getCountry();
						dr.setTeamLeaderId(p[0].getId());
//						logger.warn("[更新副本通关记录] [未找到队长，将找到的第一个玩家设为队长] [副本："+di.getName()+"] [ID："+dc.getId()+"] [玩家："+teamLeader+"]");
						if(logger.isWarnEnabled())
							logger.warn("[更新副本通关记录] [未找到队长，将找到的第一个玩家设为队长] [副本：{}] [ID：{}] [玩家：{}]", new Object[]{di.getName(),dc.getId(),teamLeader});
					}
					dr.setLeaderPoliticalCamp(camp);
					dr.setTeamMembers(names);
					dr.setTeamLeader(teamLeader);
				}else{
//					logger.warn("[更新副本通关记录] [副本中没有玩家] [副本："+di.getName()+"] [ID："+dc.getId()+"] [时间："+(completeTime/1000/60)+"分钟]");
					if(logger.isWarnEnabled())
						logger.warn("[更新副本通关记录] [副本中没有玩家] [副本：{}] [ID：{}] [时间：{}分钟]", new Object[]{di.getName(),dc.getId(),(completeTime/1000/60)});
					return;
				}
				boolean isNeedSort=false;
				if(al.size()<BillboardManager.REQ_LINES){
					al.add(dr);
					isNeedSort=true;
				}else{
					if(completeTime<al.get(al.size()-1).getCompleteTime()){
						al.remove(al.size()-1);
						al.add(dr);
						isNeedSort=true;
					}
				}
				
				if(isNeedSort){
					Collections.sort(al, new Comparator<DungeonRecord>(){
						public int compare(DungeonRecord o1, DungeonRecord o2) {
							// TODO Auto-generated method stub
							if(o1.getCompleteTime()<o2.getCompleteTime()){
								return -1;
							}else if(o1.getCompleteTime()>o2.getCompleteTime()){
								return 1;
							}else if(o1.getCompleteTime()==o2.getCompleteTime()){
								if(o1.getCreatTime()<o2.getCreatTime()){
									return -1;
								}else if(o1.getCreatTime()>o2.getCreatTime()){
									return 1;
								}
							}
							return 0;
						}
						
					});
					if(logger.isInfoEnabled()){
//						logger.info("[更新副本通关记录] [添加记录] [副本："+di.getName()+"] [ID："+dc.getId()+"] [时间："+(completeTime/1000/60)+"分钟] [人数："+players.length+"]");
						if(logger.isInfoEnabled())
							logger.info("[更新副本通关记录] [添加记录] [副本：{}] [ID：{}] [时间：{}分钟] [人数：{}]", new Object[]{di.getName(),dc.getId(),(completeTime/1000/60),players.length});
						for(int i=0;i<dr.getTeamMembers().length;i++){
//							logger.info("[更新副本通关记录] [参与人员:"+dr.getTeamMembers()[i]+"]");
							if(logger.isInfoEnabled())
								logger.info("[更新副本通关记录] [参与人员:{}]", new Object[]{dr.getTeamMembers()[i]});
						}
					}
					this.saveDungeonsRecordData();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[更新副本通关记录] [发生错误] [副本："+di.getName()+"] [ID："+dc.getId()+"] [错误："+e+"]",e);
		}
	}
	
	private void loadConfigFile(File file){
		FileInputStream fis=null;
		try{
			fis=new FileInputStream(file);
			Document doc=XmlUtil.load(fis);
			Element root=doc.getDocumentElement();
			Element[] dungeons=XmlUtil.getChildrenByName(root, "dungeon");
			for(int i=0;i<dungeons.length;i++){
				String name=XmlUtil.getAttributeAsString(dungeons[i], "name", TransferLanguage.getMap());
				this.dungeonRecords.put(name, new ArrayList<DungeonRecord>());
			}
			if(logger.isInfoEnabled()){
//				logger.info("[读取副本通关配置] [数量："+this.dungeonRecords.size()+"]");
				if(logger.isInfoEnabled())
					logger.info("[读取副本通关配置] [数量：{}]", new Object[]{this.dungeonRecords.size()});
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[读取副本通关配置] [发生错误："+e+"]",e);
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
	
	private void loadDungeonsDataFile(File file){
		FileInputStream fis=null;
		try{
			fis=new FileInputStream(file);
			Document doc=XmlUtil.load(fis,"UTF-8");
			Element root=doc.getDocumentElement();
			Element[] dungeons=XmlUtil.getChildrenByName(root, "dungeon");
			for(int i=0;i<dungeons.length;i++){
				String name=XmlUtil.getAttributeAsString(dungeons[i], "name", null);
//				Element en=XmlUtil.getChildByName(dungeons[i], "name");
//				String name=XmlUtil.getValueAsString(en);
				if(this.dungeonRecords.containsKey(name)){
					ArrayList<DungeonRecord> al=this.dungeonRecords.get(name);
					Element[] records=XmlUtil.getChildrenByName(dungeons[i], "record");
					for(int j=0;j<records.length;j++){
						String id=XmlUtil.getAttributeAsString(records[j], "dungeonId", null);
						String dungeonName=XmlUtil.getAttributeAsString(records[j], "dungeonName", null);
						String mapName=XmlUtil.getAttributeAsString(records[j], "mapName", null);
						long completeTime=XmlUtil.getAttributeAsLong(records[j], "completeTime");
						long creatTime=XmlUtil.getAttributeAsLong(records[j], "creatTime");
						int teamLeaderId=XmlUtil.getAttributeAsInteger(records[j], "teamLeaderId");
						Element teamLeader=XmlUtil.getChildByName(records[j], "teamLeader");
						Element members=XmlUtil.getChildByName(records[j], "members");
						Element[] memberNames=XmlUtil.getChildrenByName(members, "name");
						String[] names=new String[memberNames.length];
						for(int k=0;k<names.length;k++){
							names[k]=XmlUtil.getValueAsString(memberNames[k], null);
						}
						DungeonRecord dr=new DungeonRecord(id);
						dr.setDungeonName(dungeonName);
						dr.setDungeonMapName(mapName);
						dr.setCompleteTime(completeTime);
						dr.setCreatTime(creatTime);
						dr.setTeamLeader(XmlUtil.getValueAsString(teamLeader, null));
						dr.setTeamMembers(names);
						dr.setTeamLeaderId(teamLeaderId);
						al.add(dr);
					}
				}
			}
			if(logger.isInfoEnabled()){
				logger.info("[读取副本通关数据] [成功]");
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[读取副本通关数据] [发生错误] [错误："+e+"]",e);
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
	
	private void saveDungeonsRecordData(){
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		FileOutputStream fos=null;
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
			sb.append("<dungeonRecords time='"+new Date()+"'>\n");
			Enumeration<String> en=this.dungeonRecords.keys();
			while(en.hasMoreElements()){
				String key=en.nextElement();
				ArrayList<DungeonRecord> al=this.dungeonRecords.get(key);
				sb.append("<dungeon name='"+key+"'>\n");
				for(DungeonRecord dr:al){
					sb.append("<record dungeonId='"+dr.getId()+"' dungeonName='"+dr.getDungeonName()+"' mapName='"+dr.getDungeonMapName()+"' completeTime='"+dr.getCompleteTime()+"' creatTime='"+dr.getCreatTime()+"' teamLeaderId='"+dr.getTeamLeaderId()+"'>\n");
					sb.append("<teamLeader>");
					sb.append("<![CDATA["+dr.getTeamLeader()+"]]>");
					sb.append("</teamLeader>\n");
					sb.append("<members>\n");
					String[] members=dr.getTeamMembers();
					if(members!=null){
						for(int i=0;i<members.length;i++){
							sb.append("<name>");
							sb.append("<![CDATA["+members[i]+"]]>");
							sb.append("</name>\n");
						}
					}
					sb.append("</members>\n");
					sb.append("</record>\n");
				}
				sb.append("</dungeon>\n");
			}
			sb.append("</dungeonRecords>");
			
			fos=new FileOutputStream(this.dungeonRecordsDataFile);
			fos.write(sb.toString().getBytes("UTF-8"));
			fos.flush();
			if(logger.isInfoEnabled()){
//				logger.info("[存储副本通关数据] [成功] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
				if(logger.isInfoEnabled())
					logger.info("[存储副本通关数据] [成功] [耗时：{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[存储副本通关数据] [发生错误] [错误："+e+"]",e);
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public Hashtable<String, ArrayList<DungeonRecord>> getDungeonRecords() {
		return dungeonRecords;
	}
	
	public static void main(String[] args) {
		DungeonsRecordManager drm=new DungeonsRecordManager();
		drm.dungeonRecords=new Hashtable<String, ArrayList<DungeonRecord>>();
		drm.loadConfigFile(new File("dungeonNamesConfig.xml"));
		drm.loadDungeonsDataFile(new File("dungeonRecordsData.xml"));
		
	}

}
