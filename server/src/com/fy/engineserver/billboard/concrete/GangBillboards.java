package com.fy.engineserver.billboard.concrete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fy.engineserver.billboard.Billboard;
import com.fy.engineserver.billboard.BillboardData;
import com.fy.engineserver.billboard.BillboardManager;
import com.fy.engineserver.billboard.Billboards;
import com.fy.engineserver.datasource.language.Translate;


public class GangBillboards extends Billboards {

	final String[][] TITLES={{Translate.text_2320,Translate.text_2349,Translate.text_395},{Translate.text_2320,Translate.text_2349,Translate.text_2350},{Translate.text_2320,Translate.text_2349,Translate.text_2321},{Translate.text_2320,Translate.text_2349,Translate.text_2351}};
	
	
	public GangBillboards(String name, byte type) {
		super(name, type,new String[]{Translate.text_408,Translate.text_2352,Translate.text_2353,Translate.text_2354});
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void update(Connection con) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			this.billboard = new Billboard[this.submenu.length];
			for (int i = 0; i < this.billboard.length; i++) {
				this.billboard[i] = new Billboard(this.submenu[i],this.TITLES[i]);
				this.billboard[i].data = new BillboardData[BillboardManager.MAX_LINES];
				String sql = "";
				switch(i){
				case 0:
//					sql="select t1.NAME,t1.GANGLEVEL,t2.name,t1.id from gang t1,player t2 where t1.GANGLEVEL is not null and t1.bangzhu=t2.id and rownum<='"+ BillboardManager.MAX_LINES + "' order by t1.GANGLEVEL desc,t1.GANGEXP desc";
					sql="select GANG_NAME,GANG_LEVEL,PLAYER_NAME,GANG_ID from (select t1.NAME as GANG_NAME,t1.GANGLEVEL as GANG_LEVEL,t2.name as PLAYER_NAME,t1.id as GANG_ID from gang t1,player t2 where t1.GANGLEVEL is not null and t1.bangzhu=t2.id order by t1.GANGLEVEL desc,t1.GANGEXP desc) where rownum<='"+ BillboardManager.MAX_LINES + "'";
					break;
					
				case 1:
//					sql="select t1.NAME,t1.FUNDS,t2.name,t1.id from gang t1,player t2 where t1.FUNDS is not null and t1.bangzhu=t2.id and rownum<='"+ BillboardManager.MAX_LINES + "' order by t1.FUNDS desc";
					sql="select GANG_NAME,GANG_FUNDS,PLAYER_NAME,PLAYER_ID from (select t1.NAME as GANG_NAME,t1.FUNDS AS GANG_FUNDS,t2.name AS PLAYER_NAME,t1.id AS PLAYER_ID from gang t1,player t2 where t1.FUNDS is not null and t1.bangzhu=t2.id order by t1.FUNDS desc) where rownum<='"+ BillboardManager.MAX_LINES + "'";
					break;
					
				case 2:
//					sql="select t1.name,t2.num,t3.name,t1.id from gang t1,(select gangid,count(gangid) as num from gangmember group by gangid) t2,player t3 where t1.id=t2.gangid and t1.bangzhu=t3.id and rownum<='"+ BillboardManager.MAX_LINES + "' order by t2.num desc";
					sql="select GANE_NAME,MEMBER_NUM,PLAYER_NAME,PLAYER_ID from (select t1.name AS GANE_NAME,t2.num AS MEMBER_NUM,t3.name AS PLAYER_NAME,t1.id AS PLAYER_ID from gang t1,(select gangid,count(gangid) as num from gangmember group by gangid) t2,player t3 where t1.id=t2.gangid and t1.bangzhu=t3.id order by t2.num desc) where rownum<='"+ BillboardManager.MAX_LINES + "'";
					break;
					
				case 3:
					sql="select GANG_NAME,GANG_POINT,PLAYER_NAME,PLAYER_ID from (select t1.NAME as GANG_NAME,t1.gangPoint AS GANG_POINT,t2.name AS PLAYER_NAME,t1.id AS PLAYER_ID from gang t1,player t2 where t1.gangPoint is not null and t1.bangzhu=t2.id order by t1.gangPoint desc) where rownum<='"+ BillboardManager.MAX_LINES + "'";
					break;
				}
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				int index = 0;
				while (rs.next()) {
					this.billboard[i].data[index] = new BillboardData(index + 1);
					this.billboard[i].data[index].setRankingObject(rs.getString(1));
					this.billboard[i].data[index].setValue(rs.getLong(2));
					this.billboard[i].data[index].setDescription(rs.getString(3));
					this.billboard[i].data[index].setId(rs.getInt(4));
					index++;
				}
			}
//			this.resetRankObject();
			
			if(log.isInfoEnabled()){
//				log.info("[更新排行榜] [成功] ["+this.getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
				if(log.isInfoEnabled())
					log.info("[更新排行榜] [成功] [{}] [耗时：{}]", new Object[]{this.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[更新排行榜] [失败] [发生错误] ["+this.getName()+"] [错误："+e+"]",e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//	@Override
//	protected void resetRankObject() {
//		// TODO Auto-generated method stub
//		for (int i = 0; i < this.billboard.length; i++) {
//			for(int j=0;j<this.billboard[i].data.length;j++){
//				if(this.billboard[i].data[j]!=null){
//					GangManager gm=BillboardManager.getInstance().getGm();
//					Gang gang=null;;
//					try{
//						gang=gm.getGang(this.billboard[i].data[j].getId());
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//					if(gang!=null){
//						if(!BillboardManager.getInstance().gangNames.containsKey(""+this.billboard[i].data[j].getId())){
//							BillboardManager.getInstance().gangNames.put(""+this.billboard[i].data[j].getId(), gang.getName());
//						}
//					}else{
//						if(!BillboardManager.getInstance().gangNames.containsKey(""+this.billboard[i].data[j].getId())){
//							BillboardManager.getInstance().gangNames.put(""+this.billboard[i].data[j].getId(), "");
//						}
//						log.warn("[更新排行榜] ["+this.getName()+"] [帮会未找到] [ID:"+this.billboard[i].data[j].getId()+"]");
//					}
//				}
//			}
//		}
//		if(log.isInfoEnabled()){
//			log.info("[更新排行榜] ["+this.getName()+"] [resetRankObject]");
//		}
//	}
	
}
