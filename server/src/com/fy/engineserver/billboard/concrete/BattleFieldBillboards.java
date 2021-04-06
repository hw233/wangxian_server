package com.fy.engineserver.billboard.concrete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fy.engineserver.billboard.Billboard;
import com.fy.engineserver.billboard.BillboardData;
import com.fy.engineserver.billboard.BillboardManager;
import com.fy.engineserver.billboard.Billboards;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.stat.StatData;

public class BattleFieldBillboards extends Billboards {
	
	final String[][] TITLES={{Translate.text_2320,Translate.text_394,Translate.text_2321},{Translate.text_2320,Translate.text_394,Translate.text_2321},{Translate.text_2320,Translate.text_394,Translate.text_2322},{Translate.text_2320,Translate.text_394,Translate.text_2322},{Translate.text_2320,Translate.text_394,Translate.text_2322}};

	public BattleFieldBillboards(String name, byte type) {
		super(name, type,new String[]{Translate.text_2323,Translate.text_2324,Translate.text_2325,Translate.text_2326,Translate.text_2327});
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void update(Connection con) {
		// TODO Auto-generated method stub
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			this.billboard = new Billboard[this.submenu.length];
			int[] ids={StatData.STAT_BATTLE_FIELD_KILLING_NUM,StatData.STAT_BATTLE_FIELD_KILLING_NUM_WEEKLY,StatData.STAT_ARATHI_WIN_TIMES,StatData.STAT_COMBAT_5V5_WIN_TIMES,StatData.STAT_COMBAT_10V10_WIN_TIMES};
			for (int i = 0; i < this.billboard.length; i++) {
				this.billboard[i] = new Billboard(this.submenu[i],this.TITLES[i]);
				this.billboard[i].data = new BillboardData[BillboardManager.MAX_LINES];
				String sql = "";
//				sql="select t2.NAME,t1.value,t1.playerid,t2.polcamp from player_props t1,player t2 where (t2.polcamp=1 or t2.polcamp=2) and t2.removed<>1 and t1.playerid=t2.id and t1.statid='"+ids[i]+"' and rownum<='"+ BillboardManager.MAX_LINES + "' order by t1.value desc";
				sql="select PLAYER_NAME,KILLING_NUM,PLAYER_ID,POLCAMP from (select distinct t2.NAME as PLAYER_NAME, t1.value as KILLING_NUM,t1.playerid as PLAYER_ID,t2.polcamp as POLCAMP from player_props t1,player t2 where (t2.polcamp=1 or t2.polcamp=2) and t2.removed='0' and t1.playerid=t2.id and t1.statid='" + ids[i] + "' order by t1.value desc) where rownum<='" + BillboardManager.MAX_LINES + "'";
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				int index = 0;
				while (rs.next()) {
					if(rs.getString(1).indexOf("GM")!=0){
						this.billboard[i].data[index] = new BillboardData(index + 1);
						this.billboard[i].data[index].setRankingObject(rs
								.getString(1));
						this.billboard[i].data[index].setValue(rs.getLong(2));
						this.billboard[i].data[index].setId(rs.getInt(3));
						this.billboard[i].data[index].setAdditionalInfo(rs.getInt(4));
						index++;
					}else{
						if(log.isInfoEnabled()){
//							log.info("[更新排行榜] [剔除GM数据] ["+this.getName()+"] ["+rs.getString(1)+"]");
							if(log.isInfoEnabled())
								log.info("[更新排行榜] [剔除GM数据] [{}] [{}]", new Object[]{this.getName(),rs.getString(1)});
						}
					}
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
