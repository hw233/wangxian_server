package com.fy.engineserver.unite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

import static com.fy.engineserver.datasource.language.Translate.*;

import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
    @SimpleIndex(members={"uniteName"}),
    @SimpleIndex(members={"memberName"})
})
public class Unite implements Cloneable,Cacheable, CacheListener{
	
	private static UniteComparator comparator= new Unite.UniteComparator();
	public static String[] NAMES = {Translate.大,二,三,四,五,六,七,八,九,十,十一,十二,十三,十四,十五,十六,十七,十八,十九,二十};
	@SimpleId
	private long uniteId;
	@SimpleVersion
	private int version;
	
	@SimpleColumn(length=20)
	private String uniteName;
	@SimpleColumn(length=20)
	private String memberName;
	
	public Unite(){
		
	}
		
	private List<Long> memberIds = new ArrayList<Long>();
	
	private long lastUpdateTime = 0;
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	private boolean frontOrBack; // true 大x，false  x大 
	
	public String getLogString() {
		return "{id:"+uniteId+"}{uniteTitle:"+uniteName+"}";
	}

	public Unite(long id){
		this.uniteId = id;
	}
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public void remove(int arg0) {
		
		try {
			UniteManager.em.save(this);
		} catch (Exception e) {
			UniteManager.logger.error("[从缓存删除异常]",e);
		}
	}

	public long getUniteId() {
		return uniteId;
	}


	public void setUniteId(long uniteId) {
		this.uniteId = uniteId;
	}


	public String getUniteName() {
		return uniteName;
	}


	public void setUniteName(String uniteName) {
		this.uniteName = uniteName;
	}

	
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
		UniteManager.em.notifyFieldChange(this, "memberName");
	}

	public List<Long> getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(List<Long> memberIds) {
		this.memberIds = memberIds;
		UniteManager.em.notifyFieldChange(this, "memberIds");
	}

	public boolean isFrontOrBack() {
		return frontOrBack;
	}


	public void setFrontOrBack(boolean frontOrBack) {
		this.frontOrBack = frontOrBack;
		UniteManager.em.notifyFieldChange(this, "frontOrBack");
	}
	 
	public void addMember(Player player){
		this.memberIds.add(player.getId());
		setMemberIds(memberIds);
		this.calculateName();
	}
	/**
	 * 计算称号
	 * @return
	 */
	public void calculateName(){
		
		List<Player> list = new ArrayList<Player>();
		Player p = null;
		for(long id: memberIds){
			try {
				p = PlayerManager.getInstance().getPlayer(id);
				if(p != null){
					list.add(p);
				}
			} catch (Exception e) {
				UniteManager.logger.error("[结义计算称号] ["+this.getLogString()+"]",e);
			}
		}
		Collections.sort(list, comparator);
		
		int i= 0;
		int key = PlayerTitlesManager.getInstance().getKeyByType(UniteManager.结义称号类型);
		int color = PlayerTitlesManager.getInstance().getColorByType(UniteManager.结义称号类型);
		if(key < 0 || color < 0){
			UniteManager.logger.error("[结义计算称号错误] [没有指定称号] ["+key+"]");
			return ;
		}
		for(Player p1 : list){
//			p1.setUniteTitle(uniteName);
			if(frontOrBack){
				p1.addTitle(key, this.uniteName+"*"+NAMES[i] + memberName,color,"",0, this.uniteName+"*"+NAMES[i] + memberName,"","", -1L);
			}else{
				p1.addTitle(key, this.uniteName+"*"+memberName +NAMES[i],color,"",0, this.uniteName+"*"+memberName +NAMES[i],"","", -1L);
			}
			i++;
			AchievementManager.getInstance().record(p1, RecordAction.结义人数,list.size());
			if(AchievementManager.logger.isWarnEnabled()){
				AchievementManager.logger.warn("[成就统计结义人数] ["+p1.getLogString()+"] [num:"+list.size()+"]");
			}
			
		}
		
		
		
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}




	static class UniteComparator implements Comparator<Player> {

		@Override
		public int compare(Player o1, Player o2) {
			int lv1 = o1.getLevel();
			int lv2 = o2.getLevel();
			if(lv1 < lv2){
				return 1;
			}else{
				long exp1 = o1.getExp();
				long exp2 = o2.getExp();
				if(exp1 <= exp2){
					return 1;
				}else{
					return 0;

				}
			}
		}
		
	}
}
