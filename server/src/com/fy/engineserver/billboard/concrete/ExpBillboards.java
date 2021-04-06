package com.fy.engineserver.billboard.concrete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fy.engineserver.billboard.Billboard;
import com.fy.engineserver.billboard.BillboardData;
import com.fy.engineserver.billboard.BillboardManager;
import com.fy.engineserver.billboard.Billboards;
import com.fy.engineserver.datasource.language.Translate;

public class ExpBillboards extends Billboards {
	final String[][] TITLES={{Translate.text_2320,Translate.text_394,Translate.text_395},{Translate.text_2320,Translate.text_394,Translate.text_395},{Translate.text_2320,Translate.text_394,Translate.text_395},{Translate.text_2320,Translate.text_394,Translate.text_395},{Translate.text_2320,Translate.text_394,Translate.text_395},{Translate.text_2320,Translate.text_394,Translate.text_395}};
	
	public ExpBillboards(String name,byte type){
		super(name,type,new String[]{Translate.text_2343,Translate.text_2344,Translate.text_2345,Translate.text_2346,Translate.text_2347,Translate.text_2348});
	}

	@Override
	protected void update(Connection con) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			this.billboard = new Billboard[this.submenu.length];
			for (int i = 0; i < this.billboard.length; i++) {
				this.billboard[i] = new Billboard(this.submenu[i],this.TITLES[i]);
				this.billboard[i].data = new BillboardData[BillboardManager.MAX_LINES];
				String sql = "";
				if (i == 0) {
					sql = "select NAME,PLAYERLEVEL,ID,POLCAMP from (select NAME,PLAYERLEVEL,ID,POLCAMP from PLAYER where (polcamp=1 or polcamp=2) and removed<>1 order by PLAYERLEVEL desc,LASTLEVELUPTIME asc) where rownum<='"
							+ BillboardManager.MAX_LINES + "'";
				} else {
					sql = "select NAME,PLAYERLEVEL,ID,POLCAMP from (select NAME,PLAYERLEVEL,ID,POLCAMP from PLAYER where (polcamp=1 or polcamp=2) and removed<>1 and CAREER='"
							+ (i - 1)
							+ "' order by PLAYERLEVEL desc,LASTLEVELUPTIME asc) where rownum<='"
							+ BillboardManager.MAX_LINES + "'";
				}
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				int index = 0;
				while (rs.next()) {
					if(rs.getString(1).indexOf("GM")!=0){
						this.billboard[i].data[index] = new BillboardData(index + 1);
						this.billboard[i].data[index].setRankingObject(rs
								.getString(1));
						this.billboard[i].data[index].setValue(rs.getInt(2));
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
			log.error("[更新排行榜] [失败] [发生错误] ["+this.getName()+"] [错误："+e+"]",e);
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
