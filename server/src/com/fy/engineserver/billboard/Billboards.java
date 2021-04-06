package com.fy.engineserver.billboard;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Billboards {
	public Billboard[] billboard;
	
	private String name="";
	
	private byte type;
	
//	public static Logger log=Logger.getLogger("Billboard");
public	static Logger log = LoggerFactory.getLogger("Billboard");
	
	public String[] submenu;
	
	
	public Billboards(String name,byte type,String[] submenu){
		this.name=name;
		this.type=type;
		this.submenu=submenu;
	}
	
//	public void init(String[] submenu){
//		this.billboard=new Billboard[submenu.length];
//		for(int i=0;i<this.billboard.length;i++){
//			this.billboard[i]=new Billboard(submenu[i]);
//		}
//	}
	
	public Billboard getBillboard(String name){
		if(this.billboard!=null){
			for(int i=0;i<this.billboard.length;i++){
				if(this.billboard[i]!=null){
					if(this.billboard[i].getName().equals(name)){
						if(log.isInfoEnabled()){
//							log.info("[获取具体排行榜] [成功] [名称："+name+"]");
							if(log.isInfoEnabled())
								log.info("[获取具体排行榜] [成功] [名称：{}]", new Object[]{name});
						}
						return this.billboard[i];
					}
				}
			}
		}
//		log.warn("[获取具体排行榜] [失败] [没有这个排行榜] [名称："+name+"]");
		if(log.isWarnEnabled())
			log.warn("[获取具体排行榜] [失败] [没有这个排行榜] [名称：{}]", new Object[]{name});
		return null;
	}
	
	protected void setData(String name,String[] rankObject,long[] value,String[] discription,long[] id){
		Billboard bb=this.getBillboard(name);
		if(bb!=null){
			bb.setData(rankObject, value, discription, id);
		}
	}
	
	protected abstract void update(Connection con);

	public String getName() {
		return name;
	}

	public byte getType() {
		return type;
	}
	
	public String[] getSubmenu(){
		return this.submenu;
	}
	
//	protected abstract void resetRankObject();
	
}
