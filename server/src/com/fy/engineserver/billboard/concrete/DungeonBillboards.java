/**
 * 
 */
package com.fy.engineserver.billboard.concrete;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.fy.engineserver.billboard.Billboard;
import com.fy.engineserver.billboard.BillboardData;
import com.fy.engineserver.billboard.BillboardManager;
import com.fy.engineserver.billboard.Billboards;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DungeonRecord;
import com.fy.engineserver.downcity.DungeonsRecordManager;

/**
 * @author Administrator
 *
 */
public class DungeonBillboards extends Billboards {

	/**
	 * @param name
	 * @param type
	 * @param submenu
	 */
	public DungeonBillboards(String name, byte type) {
		super(name, type, null);
		// TODO Auto-generated constructor stub
		DungeonsRecordManager drm=BillboardManager.getInstance().getDrm();
		String[] ss= new String[drm.getDungeonRecords().size()];
		Enumeration<String> en=drm.getDungeonRecords().keys();
		int i=0;
		while(en.hasMoreElements()){
			String s=en.nextElement();
			ss[i]=s;
			i++;
		}
		this.submenu=ss;
	}

	/* (non-Javadoc)
	 * @see com.fy.engineserver.billboard.Billboards#resetRankObject()
	 */
//	@Override
//	protected void resetRankObject() {
//		// TODO Auto-generated method stub
//
//	}

	/* (non-Javadoc)
	 * @see com.fy.engineserver.billboard.Billboards#update(java.sql.Connection)
	 */
	@Override
	protected void update(Connection con) {
		// TODO Auto-generated method stub
		try {
			long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			DungeonsRecordManager drm=BillboardManager.getInstance().getDrm();
			Hashtable<String, ArrayList<DungeonRecord>> hash=drm.getDungeonRecords();
			this.billboard = new Billboard[this.submenu.length];
			for (int i = 0; i < this.billboard.length; i++) {
				this.billboard[i]=new Billboard(this.submenu[i],new String[]{Translate.text_2320,Translate.text_690,Translate.text_2328});
				ArrayList<DungeonRecord> al=hash.get(this.submenu[i]);
				if(al!=null){
					this.billboard[i].data = new BillboardData[al.size()];
				}else{
					this.billboard[i].data = new BillboardData[0];
				}
				for(int j=0;j<this.billboard[i].data.length;j++){
					DungeonRecord dr=al.get(j);
					this.billboard[i].data[j]=new BillboardData(j+1);
					this.billboard[i].data[j].setRankingObject(dr.getTeamLeader());
					this.billboard[i].data[j].setValue(dr.getCompleteTime()/1000/60);
					this.billboard[i].data[j].setAdditionalInfo(dr.getLeaderPoliticalCamp());
					String[] members=dr.getTeamMembers();
					String s=Translate.text_2329;
					if(members!=null){
						for(int k=0;k<members.length;k++){
							if(k<members.length-1){
								s+=(members[k]+'\n');
							}else{
								s+=members[k];
							}
						}
					}
					this.billboard[i].data[j].setDescription(s);
					this.billboard[i].data[j].setId(dr.getTeamLeaderId());
				}
			}
			if(log.isInfoEnabled()){
//				log.info("[更新排行榜] [成功] ["+this.getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
				if(log.isInfoEnabled())
					log.info("[更新排行榜] [成功] [{}] [耗时：{}]", new Object[]{this.getName(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("[更新排行榜] [失败] [发生错误] ["+this.getName()+"] [错误："+e+"]",e);
		}
	}

}
