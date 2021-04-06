package com.fy.boss.gm.record;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.fy.boss.gm.record.TelRecord;
import com.fy.boss.gm.record.TelRecordManager;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.ConfigurationException;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class TelRecordManager {
	public Logger logger = Logger.getLogger(TelRecordManager.class);
	private String telrecordfile;
	private static TelRecordManager self;
	private List<String> telrecordids = new ArrayList<String>();
	private List<TelRecord> telrecords = new ArrayList<TelRecord>();

	public static TelRecordManager getInstance(){
		return self;
	}
    public void initialize(){
    	long old = System.currentTimeMillis();
    	try {
			telrecords = loadPage(telrecordfile);
			telrecordids = new ArrayList<String>();
			for(TelRecord tr:telrecords){
				telrecordids.add(tr.getId());
			}
			self = this;
			System.out.println("["+TelRecordManager.class.getName()+"][init][success]["+(System.currentTimeMillis()-old)+"]");
			logger.info("["+TelRecordManager.class.getName()+"][init][success]["+(System.currentTimeMillis()-old)+"]");
		} catch (Exception e) {
            logger.warn("["+TelRecordManager.class.getName()+"][init][fail]\n"+StringUtil.getStackTrace(e));
		}
    }
	
    public String getTelrecordfile() {
		return telrecordfile;
	}
	public void setTelrecordfile(String telrecordfile) {
		this.telrecordfile = telrecordfile;
	}
	public TelRecord addone(TelRecord tr){
    	tr.setId(idGenerator());
    	telrecords.add(tr);
    	telrecordids.add(tr.getId());
    	try {
			savePage(telrecords, telrecordfile);
			logger.info("[addone][success]["+tr.getId()+"]");
			return tr;
		} catch (Exception e) {
			logger.warn("[addone][fail]["+tr.getId()+"]");
			return null;
		}
    }
	public TelRecord findById(String id){
		if(telrecordids.contains(id))
		 return telrecords.get(telrecordids.indexOf(id));
		else 
		 return null;
	}
	public boolean update(TelRecord tr){
		if(!telrecordids.contains(tr.getId()))
		 return false;
		else{
		 try {
			telrecords.set(telrecordids.indexOf(tr.getId()), tr);
			  savePage(telrecords, telrecordfile);
			 logger.info("[update][success]["+tr.getId()+"]");
			return true;
		} catch (Exception e) {
			logger.warn("[update][fail]["+tr.getId()+"]");
		    return false;
		}	
		}
	}
	public boolean delete(String tr){
		if(!telrecordids.contains(tr))
			 return false;
			else{
			 try {
				telrecords.remove(telrecordids.indexOf(tr));
				telrecordids.remove(tr);
				  savePage(telrecords, telrecordfile);
				  logger.info("[delete][success]["+tr+"]");
				return true;
			} catch (Exception e) {
				  logger.warn("[delete][fail]["+tr+"]");
			    return false;
			}	
			}
	}
	public static List<TelRecord> loadPage(String xmlname) {
		List<TelRecord> trs = new ArrayList<TelRecord>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder()
					.buildFromFile(xmlname);
			Configuration rolesConf[] = rootConf.getChildren();
			for (int i = 0; i < rolesConf.length; i++) {
				TelRecord tr = new TelRecord();
				tr.setId(rolesConf[i].getAttribute("id",""));
				tr.setTel(rolesConf[i].getAttribute("tel",""));
				tr.setUname(rolesConf[i].getAttribute("uname",""));
				tr.setName(rolesConf[i].getAttribute("name",""));
				tr.setCreatedate(rolesConf[i].getAttribute("createdate",""));
				tr.setServername(rolesConf[i].getAttribute("servername",""));
				tr.setPlayername(rolesConf[i].getAttribute("playername",""));
				tr.setQuestion(rolesConf[i].getAttribute("question",""));
				tr.setAnswer(rolesConf[i].getAttribute("answer",""));
				tr.setType(rolesConf[i].getAttribute("type",""));
				tr.setRecorder(rolesConf[i].getAttribute("recorder",""));
				tr.setRecorder1(rolesConf[i].getAttribute("recorder1",""));
				tr.setRecorder2(rolesConf[i].getAttribute("recorder2",""));
				tr.setMemo1(rolesConf[i].getAttribute("memo1",""));
				tr.setMemo2(rolesConf[i].getAttribute("memo2",""));
				tr.setRecorder3(rolesConf[i].getAttribute("recorder3",""));
				tr.setRecorder4(rolesConf[i].getAttribute("recorder4",""));
				tr.setMemo3(rolesConf[i].getAttribute("memo3",""));
				tr.setMemo4(rolesConf[i].getAttribute("memo4",""));
				tr.setMemo4(rolesConf[i].getAttribute("questtype",""));
				trs.add(tr);
			}
			return trs;
		} catch (Exception e) {
			return new ArrayList<TelRecord>();
		}
	}  

	public static void savePage(List<TelRecord> trs, String saveFile)
			throws IllegalArgumentException, IllegalAccessException,
			SAXException, IOException, ConfigurationException,
			InvocationTargetException {
		Configuration rootConf = new DefaultConfiguration("telrecords", "-");
		for (TelRecord tr : trs) {
			Configuration trConf = new DefaultConfiguration("telrecord", "-");
			rootConf.addChild(trConf);
			trConf.setAttribute("id", tr.getId());
			trConf.setAttribute("uname", tr.getUname());
			trConf.setAttribute("name", tr.getName());
			trConf.setAttribute("servername", tr.getServername());
			trConf.setAttribute("createdate", tr.getCreatedate());
			trConf.setAttribute("playername", tr.getPlayername());
			trConf.setAttribute("question", tr.getQuestion());
			trConf.setAttribute("answer", tr.getAnswer());
			trConf.setAttribute("type", tr.getType());
			trConf.setAttribute("answeertype", tr.getType());
			trConf.setAttribute("recorder", tr.getRecorder());
			trConf.setAttribute("recorder1", tr.getRecorder1());
			trConf.setAttribute("recorder2", tr.getRecorder2());
			trConf.setAttribute("memo1", tr.getMemo1());
			trConf.setAttribute("memo2", tr.getMemo2());
			trConf.setAttribute("recorder3", tr.getRecorder3());
			trConf.setAttribute("recorder4", tr.getRecorder4());
			trConf.setAttribute("memo3", tr.getMemo3());
			trConf.setAttribute("memo4", tr.getMemo4());
			trConf.setAttribute("questtype", tr.getQuesttype());
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(
				new File(saveFile), rootConf,"UTF-8");
	}
    private String idGenerator(){
    	long id = (long)(Math.random()*Integer.MAX_VALUE);
    	while(telrecordids.contains(id+""))
    		id = (long)(Math.random()*Integer.MAX_VALUE);
    	return id+"";
    }
	public List<String> getTelrecordids() {
		return telrecordids;
	}

	public void setTelrecordids(List<String> telrecordids) {
		this.telrecordids = telrecordids;
	}

	public List<TelRecord> getTelrecords() {
		return telrecords;
	}

	public void setTelrecords(List<TelRecord> telrecords) {
		this.telrecords = telrecords;
	}
}
