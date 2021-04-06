package com.fy.boss.gm.gmuser.server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.fy.boss.gm.gmuser.BanPassport;
import com.fy.boss.gm.gmuser.server.BanPassportManager;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.ConfigurationException;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.FileUtils;

public class BanPassportManager {
	Logger logger = Logger.getLogger(BanPassportManager.class.getName());
	private static BanPassportManager self;
	private List<BanPassport> bps = new ArrayList<BanPassport>();
	private List<String> bpusernames = new ArrayList<String>();
	private String bpfile;

	public void initialize(){
		long old = System.currentTimeMillis();
		try {
			bps = loadPage(bpfile);
			if(bps!=null&&bps.size()>0){
			for(BanPassport bp:bps)
				bpusernames.add(bp.getUsername());
			}
			self = this;
			System.out.println("[load][banpassportfile] [success]["
					+ (System.currentTimeMillis() - old) + "]");
			logger.info("[load][banpassportfile] [success]["
					+ (System.currentTimeMillis() - old) + "]");
		} catch (Exception e) {
			logger.warn("[load][banpassportfile] [fail]");
		}
	}

	public static BanPassportManager getInstance() {
		return self;
	}

	public void addBanPassport(BanPassport bp) {
        try {
        	if(!bpusernames.contains(bp.getUsername())){
			bpusernames.add(bp.getUsername());
			bps.add(bp);
			}
        	else{
        		bps.set(bpusernames.indexOf(bp.getUsername()), bp);
        	}
			savePage(bps, bpfile); 
			logger.info("[ban][passport] [success]");
		} catch (Exception e) {
			logger.info("[ban][passport] [fail]");
		}
	}
    public void deletePassport(String username) {
    	try {
        	if(!bpusernames.contains(username)){
			return;
			}
        	else{
        		bps.remove(bpusernames.indexOf(username));
        		bpusernames.remove(username);
        	}
			savePage(bps, bpfile); 
			logger.info("[delete][banpassport] [success]");
		} catch (Exception e) {
			logger.info("[delete][banpassport] [fail]");
		}
    	
    }
	public List<BanPassport> loadPage(String xmlname) {
		List<BanPassport> bpss = new ArrayList<BanPassport>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder()
					.buildFromFile(xmlname);
			Configuration rolesConf[] = rootConf.getChildren();
			for (int i = 0; i < rolesConf.length; i++) {
				BanPassport bp = new BanPassport();
				bp.setUsername(rolesConf[i].getAttribute("username", ""));
				bp.setGmname(rolesConf[i].getAttribute("gmname", ""));
				bp.setResult(rolesConf[i].getAttribute("result", ""));
				bp.setBanhour(rolesConf[i].getAttribute("banhour", ""));
				bp.setDate(rolesConf[i].getAttribute("date", ""));
				bpss.add(bp);
			}
			return bpss;
		} catch (Exception e) {
			return new ArrayList<BanPassport>();
		}
	}

	public void savePage(List<BanPassport> bpss, String saveFile)
			throws IllegalArgumentException, IllegalAccessException,
			SAXException, IOException, ConfigurationException,
			InvocationTargetException {
		Configuration rootConf = new DefaultConfiguration("banpassports", "-");
		for (BanPassport bp : bpss) {
			Configuration bpConf = new DefaultConfiguration("banpassport", "-");
			rootConf.addChild(bpConf);
			bpConf.setAttribute("username", bp.getUsername());
			bpConf.setAttribute("gmname", bp.getGmname());
			bpConf.setAttribute("result", bp.getResult());
			bpConf.setAttribute("banhour", bp.getBanhour());
			bpConf.setAttribute("date", bp.getDate());
		}
		FileUtils.chkFolder(saveFile);
		new DefaultConfigurationSerializer().serializeToFile(
				new File(saveFile), rootConf);
	}

	public List<BanPassport> getBps() {
		return bps;
	}

	public void setBps(List<BanPassport> bps) {
		this.bps = bps;
	}



	public List<String> getBpusernames() {
		return bpusernames;
	}

	public void setBpusernames(List<String> bpusernames) {
		this.bpusernames = bpusernames;
	}

	public String getBpfile() {
		return bpfile;
	}

	public void setBpfile(String bpfile) {
		this.bpfile = bpfile;
	}

}
