package com.fy.engineserver.billboard.concrete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fy.engineserver.billboard.Billboard;
import com.fy.engineserver.billboard.BillboardData;
import com.fy.engineserver.billboard.BillboardManager;
import com.fy.engineserver.billboard.Billboards;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.stat.StatData;

public class OnlineTimeBillboards extends Billboards {
	
	final String[][] TITLES={{Translate.text_2320,Translate.text_394,Translate.text_2363}};

	public OnlineTimeBillboards(String name, byte type) {
		super(name, type,new String[]{Translate.text_2315});
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void update(Connection con) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			this.billboard = new Billboard[1];
			this.billboard[0] = new Billboard(
					BillboardManager.BILLBOARDS_NAMES[6],this.TITLES[0]);
			this.billboard[0].data = new BillboardData[BillboardManager.MAX_LINES];
//			String sql = "select t3.name,t2.value as day,t1.value as time,t1.playerid,t3.polcamp from player_props t1,player_props t2,player t3 where (t3.polcamp=1 or t3.polcamp=2) and t3.removed<>1 and t1.statid='"
//					+ StatData.STAT_ONLINE_TIME
//					+ "' and t2.statid='"
//					+ StatData.STAT_ONLINE_DAYS
//					+ "' and t1.playerid=t2.playerid and t1.playerid=t3.id and rownum<='"
//					+ BillboardManager.MAX_LINES
//					+ "' order by day desc,time desc";
			
			String sql="select * from (select distinct t3.name AS PLAYER_NAME,t2.value as day,t1.value as time,t1.playerid AS PLAYER_ID,t3.polcamp AS POLCAMP from player_props t1,player_props t2,player t3 where (t3.polcamp=1 or t3.polcamp=2) and t3.removed<>1 and t1.statid='"
					+ StatData.STAT_ONLINE_TIME
					+ "' and t2.statid='"
					+ StatData.STAT_ONLINE_DAYS
					+ "' and t1.playerid=t2.playerid and t1.playerid=t3.id order by day desc,time desc) where rownum<='"+ BillboardManager.MAX_LINES+ "'";
			
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			int index = 0;
			while (rs.next()) {
				if (rs.getString(1).indexOf("GM") != 0) {
					this.billboard[0].data[index] = new BillboardData(index + 1);
					this.billboard[0].data[index].setRankingObject(rs
							.getString(1));
					this.billboard[0].data[index].setValue(rs.getInt(2));
					this.billboard[0].data[index].setId(rs.getInt(4));
					this.billboard[0].data[index].setAdditionalInfo(rs
							.getInt(5));
					
					PlayerManager pm=BillboardManager.getInstance().getPm();
					Player player=null;
					try{
						player=pm.getPlayer(rs.getInt(3));
					}catch(Exception e){
						
					}
//					if(player!=null){
//						if(!BillboardManager.getInstance().playerNames.containsKey(""+rs.getInt(4))){
//							BillboardManager.getInstance().playerNames.put(""+rs.getInt(4), player.getName());
//						}
//					}else{
//						if(!BillboardManager.getInstance().playerNames.containsKey(""+rs.getInt(4))){
//							BillboardManager.getInstance().playerNames.put(""+rs.getInt(4), "");
//						}
//						log.warn("[更新排行榜] ["+this.getName()+"] [玩家未找到] [ID:"+rs.getInt(4)+"]");
//					}
					index++;
				}else{
					if(log.isInfoEnabled()){
//						log.info("[更新排行榜] [剔除GM数据] ["+this.getName()+"] ["+rs.getString(1)+"]");
						if(log.isInfoEnabled())
							log.info("[更新排行榜] [剔除GM数据] [{}] [{}]", new Object[]{this.getName(),rs.getString(1)});
					}
				}
			}
//			this.resetRankObject();
			
			if (log.isInfoEnabled()) {
//				log.info("[更新排行榜] [成功] [" + this.getName() + "] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
				if(log.isInfoEnabled())
					log.info("[更新排行榜] [成功] [{}] [耗时：{}]", new Object[]{this.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
			}
		} catch (Exception e) {
			e.printStackTrace();
//log.warn("[更新排行榜] [失败] [发生错误] [" + this.getName() + "] [错误：" + e
//+ "]", e);
if(log.isWarnEnabled())
	log.warn("[更新排行榜] [失败] [发生错误] [" + this.getName() + "] [错误：" + e+ "]", e);
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
//		for(int i = 0; i < this.billboard.length; i++){
//			for(int j=0;j<this.billboard[i].data.length;j++){
//				if(this.billboard[i].data[j]!=null){
//					PlayerManager pm=BillboardManager.getInstance().getPm();
//					Player player=null;
//					try{
//						player=pm.getPlayer((int)this.billboard[i].data[j].getId());
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//					if(player!=null){
//						if(!BillboardManager.getInstance().playerNames.containsKey(""+this.billboard[i].data[j].getId())){
//							BillboardManager.getInstance().playerNames.put(""+this.billboard[i].data[j].getId(), player.getName());
//						}
//					}else{
//						if(!BillboardManager.getInstance().playerNames.containsKey(""+this.billboard[i].data[j].getId())){
//							BillboardManager.getInstance().playerNames.put(""+this.billboard[i].data[j].getId(), "");
//						}
//						log.warn("[更新排行榜] ["+this.getName()+"] [玩家未找到] [ID:"+this.billboard[i].data[j].getId()+"]");
//					}
//				}
//			}
//		}
//		if(log.isInfoEnabled()){
//			log.info("[更新排行榜] ["+this.getName()+"] [resetRankObject]");
//		}
//	}
	
}
