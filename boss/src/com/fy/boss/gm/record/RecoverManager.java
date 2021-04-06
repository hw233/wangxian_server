package com.fy.boss.gm.record;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.fy.boss.gm.record.Recover;
import com.fy.boss.gm.record.RecoverManager;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.ConfigurationException;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;
import com.xuanzhi.tools.text.StringUtil;

public class RecoverManager {
	public Logger logger = Logger.getLogger(RecoverManager.class);
	private String recoverfile;
	private static RecoverManager self;
	private List<String> recoverrecordids = new ArrayList<String>();
	private List<Recover> recoverrecords = new ArrayList<Recover>();

	public static RecoverManager getInstance() {
		return self;
	}

	public void initialize() {
		long old = System.currentTimeMillis();
		try {
			recoverrecords = loadPage(recoverfile);
			recoverrecordids = new ArrayList<String>();
			for (Recover rr : recoverrecords) {
				recoverrecordids.add(rr.getId());
			}
			self = this;
			System.out.println("[" + RecoverManager.class.getName()
					+ "][init][success][" + (System.currentTimeMillis() - old)
					+ "]");
			logger.info("[" + RecoverManager.class.getName()
					+ "][init][success][" + (System.currentTimeMillis() - old)
					+ "]");
		} catch (Exception e) {
			logger.warn("[" + RecoverManager.class.getName()
					+ "][init][fail]\n" + StringUtil.getStackTrace(e));
		}
	}

	public Recover addone(Recover ar) {
		ar.setId(idGenerator());
		try {
			recoverrecords.add(ar);
			recoverrecordids.add(ar.getId());
			savePage(recoverrecords, recoverfile);
			logger.info("[addone][success][" + ar.getId() + "]");
			return ar;
		} catch (Exception e) {
			logger.warn("[addone][fail][" + ar.getId() + "]");
			return null;
		}
	}

	public Recover findById(String id) {
		if (recoverrecordids.contains(id))
			return recoverrecords.get(recoverrecordids.indexOf(id));
		else
			return null;
	}

	public boolean update(Recover rr) {
		if (!recoverrecordids.contains(rr.getId()))
			return false;
		else {
			try {
				recoverrecords.set(recoverrecordids.indexOf(rr.getId()), rr);
				savePage(recoverrecords, recoverfile);
				logger.info("[update][success][" + rr.getId() + "]");
				return true;
			} catch (Exception e) {
				logger.warn("[update][fail][" + rr.getId() + "]");
				return false;
			}
		}
	}

	public boolean delete(String rr) {
		if (!recoverrecordids.contains(rr))
			return false;
		else {
			try {
				recoverrecords.remove(recoverrecordids.indexOf(rr));
				recoverrecordids.remove(rr);
				savePage(recoverrecords, recoverfile);
				logger.info("[delete][success][" + rr + "]");
				return true;
			} catch (Exception e) {
				logger.warn("[delete][fail][" + rr + "]");
				return false;
			}
		}
	}

	public static List<Recover> loadPage(String xmlname) {
		List<Recover> rrs = new ArrayList<Recover>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder()
					.buildFromFile(xmlname);
			Configuration rolesConf[] = rootConf.getChildren();
			for (int i = 0; i < rolesConf.length; i++) {
				Recover rr = new Recover();
				rr.setId(rolesConf[i].getAttribute("id", ""));
				rr.setPhone(rolesConf[i].getAttribute("phone", ""));
				rr.setName(rolesConf[i].getAttribute("name", ""));
				rr.setUsername(rolesConf[i].getAttribute("username", ""));
				rr.setServer(rolesConf[i].getAttribute("server", ""));
				rr.setPlayer(rolesConf[i].getAttribute("player", ""));
				rr.setCreatetime(rolesConf[i].getAttribute("createtime", ""));
				rr.setStoptime(rolesConf[i].getAttribute("stoptime", ""));
				rr.setRecoder(rolesConf[i].getAttribute("recoder", ""));
				rr.setRecoder1(rolesConf[i].getAttribute("recoder1", ""));
				rr.setRecoder2(rolesConf[i].getAttribute("recorde2", ""));
				rr.setMemo1(rolesConf[i].getAttribute("memo1", ""));
				rr.setMemo2(rolesConf[i].getAttribute("memo2", ""));
				rr.setRecoder3(rolesConf[i].getAttribute("recoder3", ""));
				rr.setRecoder4(rolesConf[i].getAttribute("recoder4", ""));
				rr.setMemo3(rolesConf[i].getAttribute("memo3", ""));
				rr.setMemo4(rolesConf[i].getAttribute("memo4", ""));
				rr.setResult(rolesConf[i].getAttribute("result", ""));
				rr.setGangname(rolesConf[i].getAttribute("gangname", ""));
				rr.setPwd1(rolesConf[i].getAttribute("pwd1", ""));
				rr.setPwd2(rolesConf[i].getAttribute("pwd2", ""));
				rr.setPhoneusetime1(rolesConf[i].getAttribute("phoneusetime1", ""));
				rr.setPhoneusetime2(rolesConf[i].getAttribute("phoneusetime2", ""));
				rr.setPhoneusetype1(rolesConf[i].getAttribute("phoneusetype1", ""));
				rr.setPhoneusetype2(rolesConf[i].getAttribute("phoneusetype2", ""));
				rr.setLastlogintime(rolesConf[i].getAttribute("lastlogintime", ""));
				rr.setChargemessage(rolesConf[i].getAttribute("chargemessage",""));
				rr.setClientid(rolesConf[i].getAttribute("clientid", ""));
				rr.setType(rolesConf[i].getAttribute("type", ""));
				rr.setWaihumemo(rolesConf[i].getAttribute("waihumemo", ""));
				rr.setWaihuname(rolesConf[i].getAttribute("waihuname", ""));
				rrs.add(rr);
			}
			return rrs;
		} catch (Exception e) {
			return new ArrayList<Recover>();
		}
	}

	public static void savePage(List<Recover> rrs, String saveFile)
			throws IllegalArgumentException, IllegalAccessException,
			SAXException, IOException, ConfigurationException,
			InvocationTargetException {
		Configuration rootConf = new DefaultConfiguration("recovers", "-");
		for (Recover rr : rrs) {
			Configuration rrConf = new DefaultConfiguration("recover",
					"-");
			rootConf.addChild(rrConf);
			rrConf.setAttribute("id", rr.getId());
			rrConf.setAttribute("phone", rr.getPhone());
			rrConf.setAttribute("name", rr.getName());
			rrConf.setAttribute("username", rr.getUsername());
			rrConf.setAttribute("server", rr.getServer());
			rrConf.setAttribute("player", rr.getPlayer());
			rrConf.setAttribute("createtime", rr.getCreatetime());
			rrConf.setAttribute("stoptime", rr.getStoptime());
			rrConf.setAttribute("recoder", rr.getRecoder());
			rrConf.setAttribute("gangname", rr.getGangname());
			rrConf.setAttribute("pwd1", rr.getPwd1());
			rrConf.setAttribute("pwd2",rr.getPwd2());
			rrConf.setAttribute("phoneusetime1", rr.getPhoneusetime1());
			rrConf.setAttribute("phoneusetime2", rr.getPhoneusetime2());
			rrConf.setAttribute("phoneusetype1", rr.getPhoneusetype1());
			rrConf.setAttribute("phoneusetype2", rr.getPhoneusetype2());
			rrConf.setAttribute("lastlogintime", rr.getLastlogintime());
			rrConf.setAttribute("clientid", rr.getClientid());
			rrConf.setAttribute("chargemessage", rr.getChargemessage());
			rrConf.setAttribute("type", rr.getType());
			rrConf.setAttribute("result", rr.getResult());
			rrConf.setAttribute("memo1", rr.getMemo1());
			rrConf.setAttribute("recoder1", rr.getRecoder1());
			rrConf.setAttribute("memo2", rr.getMemo2());
			rrConf.setAttribute("recoder2",rr.getRecoder2());
			rrConf.setAttribute("memo3", rr.getMemo3());
			rrConf.setAttribute("waihuname", rr.getWaihuname());
			rrConf.setAttribute("waihumemo", rr.getWaihumemo());
			rrConf.setAttribute("recoder3", rr.getRecoder3());
			rrConf.setAttribute("memo4",rr.getMemo4());
			rrConf.setAttribute("recoder4", rr.getRecoder4());
			
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(
				new File(saveFile), rootConf,"UTF-8");
	}

	private String idGenerator() {
		long id = (long)(Math.random() * Integer.MAX_VALUE);
		while (recoverrecordids.contains(id + ""))
			id = (long)(Math.random() * Integer.MAX_VALUE);
		return id + "";
	}

	public String getRecoverfile() {
		return recoverfile;
	}

	public void setRecoverfile(String recoverfile) {
		this.recoverfile = recoverfile;
	}

	public List<String> getRecoverrecordids() {
		return recoverrecordids;
	}

	public void setRecoverrecordids(List<String> recoverrecordids) {
		this.recoverrecordids = recoverrecordids;
	}

	public List<Recover> getRecoverrecords() {
		return recoverrecords;
	}

	public void setRecoverrecords(List<Recover> recoverrecords) {
		this.recoverrecords = recoverrecords;
	}

	

}
