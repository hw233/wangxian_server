package com.fy.engineserver.billboard.concrete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fy.engineserver.billboard.Billboard;
import com.fy.engineserver.billboard.BillboardData;
import com.fy.engineserver.billboard.BillboardManager;
import com.fy.engineserver.billboard.Billboards;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.honor.Honor;
import com.fy.engineserver.honor.HonorManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.stat.StatData;

public class IncomeFromAuBillboards extends Billboards {

	final String[][] TITLES={{Translate.text_2320,Translate.text_394,Translate.text_2355},{Translate.text_2320,Translate.text_394,Translate.text_2355}};
	
	public IncomeFromAuBillboards(String name, byte type) {
		super(name, type,new String[]{Translate.text_2356,Translate.text_2357});
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
			int[] ids={StatData.STAT_INCOME_FORM_AU,StatData.STAT_INCOME_FORM_AU_WEEKLY};
			for (int i = 0; i < this.billboard.length; i++) {
				this.billboard[i] = new Billboard(this.submenu[i],this.TITLES[i]);
				this.billboard[i].data = new BillboardData[BillboardManager.MAX_LINES];
				String sql = "";
//				sql="select t2.NAME,t1.value,t1.playerid,t2.polcamp from player_props t1,player t2 where (t2.polcamp=1 or t2.polcamp=2) and t2.removed<>1 and t1.playerid=t2.id and t1.statid='"+ids[i]+"' and rownum<='"+ BillboardManager.MAX_LINES + "' order by t1.value desc";
				sql="select PLAYER_NAME,INCOME,PLAYER_ID,POLCAMP from (select distinct t2.NAME AS PLAYER_NAME,t1.value AS INCOME,t1.playerid AS PLAYER_ID,t2.polcamp AS POLCAMP from player_props t1,player t2 where (t2.polcamp=1 or t2.polcamp=2) and t2.removed<>1 and t1.playerid=t2.id and t1.statid='"+ids[i]+"' order by t1.value desc) where rownum<='"+ BillboardManager.MAX_LINES + "'";
				
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				int index = 0;
				while (rs.next()) {
					if (rs.getString(1).indexOf("GM") != 0) {
						this.billboard[i].data[index] = new BillboardData(
								index + 1);
						this.billboard[i].data[index].setRankingObject(rs
								.getString(1));
						this.billboard[i].data[index].setValue(rs.getLong(2));
						this.billboard[i].data[index].setId(rs.getInt(3));
						this.billboard[i].data[index].setAdditionalInfo(rs
								.getInt(4));
						index++;
						
						//--------------------------------------------------------------------------------
						//称号处理
						if(i==0){
							try{
								if(index>2){
									Player player=null;
									try{
										player=PlayerManager.getInstance().getPlayer(this.billboard[0].data[index-1].getId());
									}catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
									if(player!=null){
										Honor h=HonorManager.getInstance().getHonor(player,Translate.text_2316);
										if(h!=null){
											HonorManager.getInstance().loseHonor(player,h, HonorManager.LOSE_REASON_NOT_MEET_STANDARDS);
										}
										h=HonorManager.getInstance().getHonor(player,Translate.text_2358);
										if(h!=null){
											HonorManager.getInstance().loseHonor(player,h, HonorManager.LOSE_REASON_NOT_MEET_STANDARDS);
										}
										h=HonorManager.getInstance().getHonor(player,Translate.text_2359);
										if(h!=null){
											HonorManager.getInstance().loseHonor(player,h, HonorManager.LOSE_REASON_NOT_MEET_STANDARDS);
										}
									}
								}
							}catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
								HonorManager.logger.error("[称号] [处理收入排行榜时出现错误] [错误："+e+"]",e);
							}
						}
						//--------------------------------------------------------------------------------
					}else{
						if(log.isInfoEnabled()){
//							log.info("[更新排行榜] [剔除GM数据] ["+this.getName()+"] ["+rs.getString(1)+"]");
							if(log.isInfoEnabled())
								log.info("[更新排行榜] [剔除GM数据] [{}] [{}]", new Object[]{this.getName(),rs.getString(1)});
						}
					}
				}
				
				//--------------------------------------------------------------------------------
				//称号处理
				if(i==0){
					if(index>100){//100
						for(int k=0;k<3;k++){
							if(this.billboard[0].data[k].getValue()>200000){
								Player player=null;
								try{
									player=PlayerManager.getInstance().getPlayer(this.billboard[0].data[k].getId());
								}catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								if(player!=null){
									if(k==0){
										HonorManager.getInstance().gainHonor(player, Translate.text_2316);
									}else if(k==1){
										HonorManager.getInstance().gainHonor(player, Translate.text_2358);
									}else{
										HonorManager.getInstance().gainHonor(player, Translate.text_2359);
									}
								}
							}
						}
					}
				}
				//--------------------------------------------------------------------------------
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
