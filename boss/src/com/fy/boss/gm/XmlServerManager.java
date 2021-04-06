package com.fy.boss.gm;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.gm.XmlServer;
import com.fy.boss.gm.XmlServerManager;
import com.fy.boss.gm.XmlServerUtil;

public class XmlServerManager {
	 protected static Logger logger = Logger.getLogger("com.xuanzhi.boss.gmxml.XmlServerManager");
	  protected String serverConfFile;
	  private List<XmlServer> servers=new ArrayList<XmlServer>();
	  private static XmlServerManager self;
	  private List<String>  serverids = new ArrayList<String>();
	  public static XmlServerManager getInstance(){
		  return self;
	  }
	  public void initialize()  {
			//......
			long now = System.currentTimeMillis();
			try {
				servers = XmlServerUtil.loadPage(serverConfFile);
				for(XmlServer s:servers){
					serverids.add(s.getId());
					logger.warn("服务器："+s.getDescription()+"\n");
				}
					
				self = this;
				System.out.println(this.getClass().getName() + " initialize successfully ["+(System.currentTimeMillis()-now)+"] ");
				logger.info(this.getClass().getName() + " initialize successfully ["+(System.currentTimeMillis()-now)+"][saveFile:"+serverConfFile+"]");
			} catch (Exception e) {
				logger.info(this.getClass().getName() + " initialize fail [saveFile:"+serverConfFile+"]");
			}
		}
	  public XmlServer getServer(String id){
		  XmlServer server = new XmlServer();
		  for(XmlServer se :servers){
			  if(id.equals(se.getId())){
				  server = se;
				  break;
			  }
		  }
		  return server;
	  }
	  public void save(){
		  try {
			  XmlServerUtil.savePage(servers, serverConfFile);
			logger.info(this.getClass().getName() + " save success [saveFile:"+serverConfFile+"]");
		} catch (Exception e) {
			logger.info(this.getClass().getName() + " save fail [saveFile:"+serverConfFile+"]");
		}
	  }
	  public int getIndex(String id){
		   int index =0;
		   for(int i=0;i<servers.size();i++){
			   XmlServer s = servers.get(i); 
			   if(id.equals(s.getId())){
				   index = i;
				   break;
			   }
			   
		   }
		return index;
	  }
	  public void insert(XmlServer server,String sid){
		   try {    
			    server.setId(IdGenerator());
			    if(sid.equals("-1"))
			    	servers.add(server);
			    else{
				    int index = getIndex(sid);
					servers.add(index, server);
			    }
				serverids.add(server.getId());
				   save();
				   logger.info("server :"+server.getId()+"add success at "+new java.util.Date());
			} catch (Exception e) {
				logger.info("server :"+server.getId()+"add fail at "+new java.util.Date());
			}  
	  }
	  public void update(XmlServer server,String id){
		   try {
			int index = getIndex(id);
			servers.set(index, server);
			   save();
			   logger.info("server :"+server.getId()+"update success at "+new java.util.Date());
		} catch (Exception e) {
			logger.info("server :"+server.getId()+"update fail at "+new java.util.Date());
		}
	  }
	  public void delete(String id){
		  try {
			for(int i=0;i<servers.size();i++){
				  XmlServer server = servers.get(i);
				  if(id.equals(server.getId())){
					  servers.remove(server);
					  serverids.remove(id);
					  save();
				  }
			  }
			logger.info("server :["+id+"] delete success at "+ new java.util.Date());
		} catch (Exception e) {
			logger.info("server :["+id +"] delete fail at "+new java.util.Date());
		}
	  }
	  public String IdGenerator(){
		  long id =1000l;
		  for(XmlServer s :servers){
			  long s1 = Long.parseLong(s.getId());
			  if(s1>=id)
				 id=s1+1;
		  }
		  return id+"";		  
	  }
	public String getServerConfFile() {
		return serverConfFile;
	}
	public void setServerConfFile(String serverConfFile) {
		this.serverConfFile = serverConfFile;
	}
	public List<XmlServer> getServers() {
		return servers;
	}
	public void setServers(List<XmlServer> servers) {
		this.servers = servers;
	}
	public List<String> getServerids() {
		return serverids;
	}
	public void setServerids(List<String> serverids) {
		this.serverids = serverids;
	}
	public XmlServer getXmlServerByName(String serverName) {
		for(XmlServer s : servers) {
			if(s.getDescription().trim().equals(serverName.trim())) {
				return s;
			}
		}
		return null;
	}
}
