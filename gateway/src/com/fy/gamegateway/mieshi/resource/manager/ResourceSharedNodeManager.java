package com.fy.gamegateway.mieshi.resource.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.gamegateway.mieshi.server.MieshiGatewayServer;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.*;

public class ResourceSharedNodeManager implements Runnable{

	static Logger logger = Logger.getLogger(MieshiGatewayServer.class);
	
	static ResourceSharedNodeManager self;
	
	public static ResourceSharedNodeManager getInstance(){
		return self;
	}
	
	String configFile;
	
	ArrayList<ResourceSharedNode> nodeList = new ArrayList<ResourceSharedNode>();
	
	HashMap<String,ResourceSharedNode> channle2NodeMap = new HashMap<String,ResourceSharedNode>();
	
	
	ArrayList<PackageSharedNode> channle2PackageNodes = new ArrayList<PackageSharedNode>();
//	HashMap<String,PackageSharedNode> channle2PackageNodeMap = new HashMap<String,PackageSharedNode>();

	Thread thread;
	
	public ArrayList<ResourceSharedNode> getNodeList(){
		return nodeList;
	}
	
	public void addPackageNode(String channel,String url, String gpuType){
		PackageSharedNode nn = getPackageSharedNodeByChannel(channel, gpuType);
		if(nn == null){
			nn = new PackageSharedNode(channel,url);
			nn.gupType = gpuType;
			this.channle2PackageNodes.add(nn);
		}
	}
	
	public void removePackageNode(int index){
		this.channle2PackageNodes.remove(index);
	}
	
	public PackageSharedNode[] getAllPackageSharedNodes(){
		return channle2PackageNodes.toArray(new PackageSharedNode[0]);
	}
	
	public void addNode(String name,boolean enable){
		ResourceSharedNode nn = getResourceSharedNode(name);
		if(nn == null){
			nn = new ResourceSharedNode();
			nn.name = name;
			nn.enable = enable;
			nodeList.add(nn);
		}
	}
	
	public void removeNode(String name){
		ResourceSharedNode nn = getResourceSharedNode(name);
		if(nn != null){
			nodeList.remove(nn);
			
			Iterator<String> it = channle2NodeMap.keySet().iterator();
			while(it.hasNext()){
				String channel = it.next();
				ResourceSharedNode node = channle2NodeMap.get(channel);
				if(node == nn){
					it.remove();
				}
			}
		}
	}
	
	public HashMap<String,ResourceSharedNode> getChannle2NodeMap(){
		return channle2NodeMap;
	}
	
	public ResourceSharedNode getResourceSharedNodeByChannel(String channel){
		return channle2NodeMap.get(channel);
	}
	
	public PackageSharedNode getPackageSharedNodeByChannel(String channel, String gupType){
		for (PackageSharedNode node : channle2PackageNodes) {
			if (node.channel.equals(channel) && node.gupType.equals(gupType)) {
				return node;
			}
		}
		return null;
	}
	
	public void addChannel2NodeMap(String channel,String node){
		ResourceSharedNode nn = getResourceSharedNode(node);
		if(node != null){
			channle2NodeMap.put(channel, nn);
		}
	}
	
	public void removeChannel(String channel){
		channle2NodeMap.remove(channel);
	}
	
	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public void init() throws Exception{
		File f = new File(configFile);
		if(f.exists()){
			load(configFile);
		}
		
		thread = new Thread(this,"第三方共享资源检查");
		thread.start();
		
		self = this;
	}
	
	public void run(){
		while(true){
			try{
				for(ResourceSharedNode n : nodeList){
					Iterator<ResourceSharedNode.ResourcePackage> it = n.rps.iterator();
					while(it.hasNext()){
						ResourceSharedNode.ResourcePackage rr = it.next();
						String url = rr.url;
						try{
							long startTime = System.currentTimeMillis();
							HashMap headers = new HashMap();
							HttpUtils.webHead(new URL(url), headers, 1000, 1000);
							Integer integer = (Integer)headers.get(HttpUtils.Response_Code);
							if(integer == 200){
								rr.checkEnable = true;
								rr.lastCheckTime = System.currentTimeMillis();
							}else{
								rr.checkEnable = false;
								rr.lastCheckTime = System.currentTimeMillis();
							}
							
							if(logger.isInfoEnabled()){
								logger.error("[共享资源检查] ["+n.getName()+"] ["+rr.url+"] ["+(rr.checkEnable?"正常":"异常")+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
							}
						}catch(Exception e){
							logger.error("[共享资源检查] ["+n.getName()+"] ["+rr.url+"] [出现异常]",e);
							rr.checkEnable = false;
						}
						
					}
				}
				PackageSharedNode[] nodes = channle2PackageNodes.toArray(new PackageSharedNode[0]);
				for (PackageSharedNode node : nodes) {
					String url = node.url;
					long startTime = System.currentTimeMillis();
					if (node.isCheckEnable() && (startTime - node.lastCheckTime) < 60*1000L*60) {
						continue;
					}
					try{
						HashMap headers = new HashMap();
						HttpUtils.webHead(new URL(url), headers, 1000, 1000);
						Integer integer = (Integer)headers.get(HttpUtils.Response_Code);
						if(integer == 200){
							node.checkEnable = true;
							node.lastCheckTime = System.currentTimeMillis();
							
							try{
								String s = (String)headers.get("Content-Length");
								if(s != null){
									node.fileSize = Long.parseLong(s);
								}
							}catch(Exception e){
								e.printStackTrace();
							}
							
						}else{
							node.checkEnable = false;
							node.lastCheckTime = System.currentTimeMillis();
						}
						
						if(logger.isInfoEnabled()){
							logger.error("[共享程序包检查] ["+node.getChannel()+"] ["+node.getUrl()+"] ["+(node.checkEnable?"正常":"异常")+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
						}
					}catch(Exception e){
						if(logger.isInfoEnabled()){
							logger.error("[共享程序包检查] ["+node.getChannel()+"] ["+node.getUrl()+"] ["+(node.checkEnable?"正常":"异常")+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]",e);
						}
						node.checkEnable = false;
					}
					
				}
				
				
				Thread.sleep(30000);
				
			}catch(Throwable e){
				logger.error("第三方共享资源检查线程，出现未知异常",e);
			}
		}
	}
	
	public void load(String file) throws Exception{
		Document dom = XmlUtil.load(file);
		Element root = dom.getDocumentElement();
		
		Element eles[] = XmlUtil.getChildrenByName(root, "resource-shared-node");
		for(int i = 0 ; i < eles.length ; i++){
			Element e = eles[i];
			
			String name = XmlUtil.getAttributeAsString(e, "name", null);
			boolean enable = XmlUtil.getAttributeAsBoolean(e, "enabled", false);
			
			ResourceSharedNode node = new ResourceSharedNode();
			node.name = name;
			node.enable = enable;
			
			Element subEles[] = XmlUtil.getChildrenByName(e, "url");
			for(int j = 0 ; j < subEles.length ; j++){
				String s = XmlUtil.getValueAsString(subEles[j], "",null);
				if(s.trim().length() > 0){
					node.packageURLs.add(s.trim());
				}
			}
			node.init();
			nodeList.add(node);
		}
		
		eles = XmlUtil.getChildrenByName(root, "channle2node");
		for(int i = 0 ; i < eles.length ; i++){
			Element e = eles[i];
			String channel = XmlUtil.getAttributeAsString(e, "channel", null, null);
			String node = XmlUtil.getAttributeAsString(e, "node", null, null);
			ResourceSharedNode nn = getResourceSharedNode(node);
			if(node != null){
				channle2NodeMap.put(channel, nn);
			}
		}
		
		eles = XmlUtil.getChildrenByName(root, "channle2package");
		for(int i = 0 ; i < eles.length ; i++){
			Element e = eles[i];
			String channel = XmlUtil.getAttributeAsString(e, "channel", null, null);
			String url = XmlUtil.getAttributeAsString(e, "url", null, null);
			boolean b = XmlUtil.getAttributeAsBoolean(e, "enable",false);
			String apk = XmlUtil.getAttributeAsString(e, "apk", null, null);
			String version = XmlUtil.getAttributeAsString(e, "ver", null, null);
			String gpu = XmlUtil.getAttributeAsString(e, "gpu", null, null);
			if (gpu==null || gpu.length() == 0) {
				gpu = "auto";
			}
			PackageSharedNode nn = new PackageSharedNode(channel,url);
			nn.gupType = gpu;
			if (version != null && apk != null && nn.getPackageVersion() == null) {
				nn.setFilename(apk);
				nn.setPackageVersion(version);
			}
			nn.setEnable(b);
			this.channle2PackageNodes.add(nn);
		}
	}
	
	public ResourceSharedNode getResourceSharedNode(String name){
		for(ResourceSharedNode n : nodeList){
			if(n.getName().equals(name)){
				return n;
			}
		}
		return null;
	}
	
	public void saveTo(String file) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='utf-8' ?>\n");
		sb.append("<resource-shared-nodes>\n");
		
		for(ResourceSharedNode n : nodeList){
			sb.append("<resource-shared-node name='"+n.name+"' enabled='"+n.enable+"'>\n");
			for(String s : n.packageURLs){
				sb.append("<url>"+StringUtil.escapeForXML(s)+"</url>\n");
			}
			sb.append("</resource-shared-node>");
		}
		
		Iterator<String> it = channle2NodeMap.keySet().iterator();
		while(it.hasNext()){
			String channel = it.next();
			ResourceSharedNode node = channle2NodeMap.get(channel);
			sb.append("<channle2node channel='"+channel+"' node='"+node.getName()+"' />\n");
		}
		
		PackageSharedNode[] nodes = channle2PackageNodes.toArray(new PackageSharedNode[0]);
		for (PackageSharedNode node : nodes){
			sb.append("<channle2package channel='"+node.channel+"' url='"+node.url+"' enable='"+node.enable+"' ver='"+node.packageVersion+"' apk='"+node.filename+"' gpu='"+node.gupType+"'/>\n");
		}
		
		sb.append("</resource-shared-nodes>");
		
		File f = new File(file);
		FileOutputStream output = new FileOutputStream(f);
		output.write(sb.toString().getBytes());
		output.close();
	}
	
	public ArrayList<PackageSharedNode> getChannle2PackageNodes() {
		return channle2PackageNodes;
	}

	public void setChannle2PackageNodes(
			ArrayList<PackageSharedNode> channle2PackageNodes) {
		this.channle2PackageNodes = channle2PackageNodes;
	}
}
