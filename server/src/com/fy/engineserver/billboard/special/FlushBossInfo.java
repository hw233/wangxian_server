package com.fy.engineserver.billboard.special;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import com.fy.engineserver.billboard.special.SpecialEquipmentManager.SpecialEquipmentAppearMap;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.worldmap.WorldMapManager;

import static com.fy.engineserver.datasource.language.Translate.*;

public class FlushBossInfo implements Serializable {

	
//	2小时电视：XXX国的XXXX地区，天生异象，灵气异动，星辰曜青天！疑似有异宝就要现世！
//	1小时电视：XXX国的XXXX地图名，长虹贯日，灵气混乱，海上生明月！疑似有异宝将要现世！
//	10分钟电视：XXX国的XXXX地图名，遮天蔽日，灵气暴乱，混沌临九天！10分钟后有异宝现世！
//
//	广播：XXX国的XXXX地图名，灵气已经平稳，XXXX怪物名字出现
	private static final long serialVersionUID = 1933904503942846478L;
	
	
	private boolean firstNotice = true;
	private boolean secondNotice = true;
	private boolean thirdNotice = true;
	
	private boolean effect;
	
	//只有boss死之后才能产生新的boss默认为false，没死
	public boolean bossDie = false;
	public long bossDieTime;
	
	//多长时间后生成boss
	private long flushTime;
	//国家
	public byte country;
	//刷新地图
	public SpecialEquipmentAppearMap appearMap;

	//生成boss的真实时间
	private long appearMonster;
	//时间到 提示玩家  生成boss
	public void heartBeat(){
		
		if(effect){
			flushTime -= 1000;
			
			if(firstNotice){
				firstNotice = false;
				第一次通知();
			}
			if(secondNotice){
				if(flushTime <= SpecialEquipmentManager.第二次通知生成boss信息的时间){
					secondNotice = false;
					第二次通知();
				}
				
			}
			if(thirdNotice){
				if(flushTime <= SpecialEquipmentManager.第三次通知生成boss信息的时间){
					thirdNotice = false;
					第三次通知();
				}
			}
			
			if(flushTime <= 0){
				setEffect(false);
				setAppearMonster(SystemTime.currentTimeMillis());
				
				//生成boss
				Game game = null;
				String mapName = this.appearMap.getMapName();
				boolean peace = true;
				game = GameManager.getInstance().getGameByName(mapName, country);
				if(game != null){
					peace = false;
				}else{
					game = GameManager.getInstance().getGameByName(mapName, 0);
				}
				if(game != null){
					
					MemoryMonsterManager mm = (MemoryMonsterManager) MemoryMonsterManager.getMonsterManager();
					Monster monster = mm.createMonster(this.monsterId);
					if(monster != null){
						
						String displayName = game.getGameInfo().displayName;
						if(peace){
//							SpecialEquipmentManager.noticeTelevision(displayName+"灵气已经平稳，"+monster.getName()+"出现");
							SpecialEquipmentManager.noticeTelevision(Translate.translateString(Translate.xx灵气已经平稳xx出现, new String[][]{{STRING_1,displayName},{STRING_2,monster.getName()}}));
						}else{
//							SpecialEquipmentManager.noticeTelevision(CountryManager.getInstance().得到国家名(country)+"国"+displayName+"灵气已经平稳，"+monster.getName()+"出现");
							SpecialEquipmentManager.noticeTelevision(translateString(XX国XX灵气已经平稳XX出现, new String[][]{{STRING_1,CountryManager.getInstance().得到国家名(country)},{STRING_2,displayName},{STRING_3,monster.getName()}}));
						}
						Point bornPoint = new Point(this.appearMap.x, this.appearMap.y);
						monster.setBornPoint(bornPoint);
						
						monster.setX(this.appearMap.x);
						monster.setY(this.appearMap.y);
						game.addSprite(monster);
						SpecialEquipmentManager.logger.warn("[生成boss成功] [map:"+appearMap.getMapName()+"] ["+country+"] [monster:"+monsterId+"]");
						
						SpecialEquipmentManager.getInstance().getDisk().put("bossInfo", this);
						SpecialEquipmentManager.logger.warn("[生成boss成功] [修改bossinfo] [map:"+appearMap.getMapName()+"] ["+country+"] [monster:"+monsterId+"]");
						
					}else{
						SpecialEquipmentManager.logger.error("[生成boss错误] [map:"+appearMap.getMapName()+"] ["+country+"] [monster null:"+monsterId+"]");
					}
					
				}else{
					SpecialEquipmentManager.logger.error("[生成boss错误] [game为null] [map:"+appearMap.getMapName()+"] ["+country+"]");
				}
			}
			
		}
	}
	
	
	public void 第一次通知(){
		
		Game game = null;
		String mapName = this.appearMap.getMapName();
		boolean peace = true;
		game = GameManager.getInstance().getGameByName(mapName, country);
		if(game != null){
			peace = false;
		}else{
			game = GameManager.getInstance().getGameByName(mapName, 0);
		}
		if(game != null){
			
			Map<String, String[]> map = WorldMapManager.getInstance().mapENGLISHNamesInArea;
			if(map != null){
				boolean find = false;
				String china = "";
				for(Entry<String, String[]> en:map.entrySet()){
					if(en.getValue() != null){
						for(String st : en.getValue()){
							if(st.equals(game.getGameInfo().getName())){
								find = true;
								china = en.getKey();
								break;
							}
						}
					}
					if(find){
						break;
					}
				}
				if(find){
					if(peace){
//						SpecialEquipmentManager.noticeTelevision(china+地区天生异象灵气异动星辰曜青天);
						SpecialEquipmentManager.noticeTelevision(translateString(xx地区天生异象灵气异动星辰曜青天, new String[][]{{STRING_1,china}}));
					}else{
//						SpecialEquipmentManager.noticeTelevision(CountryManager.getInstance().得到国家名(country)+"国"+china+"地区天生异象，灵气异动，星辰曜青天！疑似有异宝就要现世");
						SpecialEquipmentManager.noticeTelevision(translateString(xx国xx地区天生异象灵气异动星辰曜青天, new String[][]{{STRING_1,CountryManager.getInstance().得到国家名(country)},{STRING_2,china}}));
					}
				}else{
					String displayName = game.getGameInfo().displayName;
					if(peace){
//						SpecialEquipmentManager.noticeTelevision(displayName+地区天生异象灵气异动星辰曜青天);
						SpecialEquipmentManager.noticeTelevision(translateString(xx地区天生异象灵气异动星辰曜青天, new String[][]{{STRING_1,displayName}}));
					}else{
//						SpecialEquipmentManager.noticeTelevision(CountryManager.getInstance().得到国家名(country)+"国"+displayName+"地区天生异象，灵气异动，星辰曜青天！疑似有异宝就要现世");
						SpecialEquipmentManager.noticeTelevision(translateString(xx国xx地区天生异象灵气异动星辰曜青天, new String[][]{{STRING_1,CountryManager.getInstance().得到国家名(country)},{STRING_2,displayName}}));
					}
				}
				SpecialEquipmentManager.logger.warn("[第一次通知] [map:"+appearMap.getMapName()+"] ["+country+"] [monster:"+monsterId+"]");
			}
		}else{
			SpecialEquipmentManager.logger.error("[第一次广播错误] [game为null] [map:"+appearMap.getMapName()+"] ["+country+"]");
		}
		
	}
	
	public void 第二次通知(){
		
		Game game = null;
		String mapName = this.appearMap.getMapName();
		boolean peace = true;
		game = GameManager.getInstance().getGameByName(mapName, country);
		if(game != null){
			peace = false;
		}else{
			game = GameManager.getInstance().getGameByName(mapName, 0);
		}
		if(game != null){
			
			String displayName = game.getGameInfo().displayName;
			if(peace){
//				SpecialEquipmentManager.noticeTelevision(displayName+"长虹贯日，灵气混乱，海上生明月！疑似有异宝将要现世");
//				SpecialEquipmentManager.noticeTelevision(displayName+长虹贯日灵气混乱);
				SpecialEquipmentManager.noticeTelevision(translateString(xx长虹贯日灵气混乱, new String[][]{{STRING_1,displayName}}));
			}else{
//				SpecialEquipmentManager.noticeTelevision(CountryManager.getInstance().得到国家名(country)+"国"+displayName+"长虹贯日，灵气混乱，海上生明月！疑似有异宝将要现世");
				SpecialEquipmentManager.noticeTelevision(translateString(xx国xx长虹贯日灵气混乱海上生明月, new String[][]{{STRING_1,CountryManager.getInstance().得到国家名(country)},{STRING_2,displayName}}));
			}
			SpecialEquipmentManager.logger.warn("[第二次通知] [map:"+appearMap.getMapName()+"] ["+country+"] [monster:"+monsterId+"]");
		}else{
			SpecialEquipmentManager.logger.error("[第二次广播错误] [game为null] [map:"+appearMap.getMapName()+"] ["+country+"]");
		}
		
	}
	
	public void 第三次通知(){
		Game game = null;
		String mapName = this.appearMap.getMapName();
		boolean peace = true;
		game = GameManager.getInstance().getGameByName(mapName, country);
		if(game != null){
			peace = false;
		}else{
			game = GameManager.getInstance().getGameByName(mapName, 0);
		}
		if(game != null){
			
			String displayName = game.getGameInfo().displayName;
			if(peace){
//				SpecialEquipmentManager.noticeTelevision(displayName+"遮天蔽日，灵气暴乱，混沌临九天！10分钟后有异宝现世");
//				SpecialEquipmentManager.noticeTelevision(displayName+遮天蔽日灵气暴乱);
				SpecialEquipmentManager.noticeTelevision(translateString(XX遮天蔽日灵气暴乱, new String[][]{{STRING_1,displayName}}));
			}else{
//				SpecialEquipmentManager.noticeTelevision(CountryManager.getInstance().得到国家名(country)+"国"+displayName+"遮天蔽日，灵气暴乱，混沌临九天！10分钟后有异宝现世");
				SpecialEquipmentManager.noticeTelevision(translateString(xx国xx遮天蔽日灵气暴乱, new String[][]{{STRING_1,CountryManager.getInstance().得到国家名(country)},{STRING_2,displayName}}));
			}
			SpecialEquipmentManager.logger.warn("[第三次通知] [map:"+appearMap.getMapName()+"] ["+country+"] [monster:"+monsterId+"]");
		}else{
			SpecialEquipmentManager.logger.error("[第三次广播错误] [game为null] [map:"+appearMap.getMapName()+"] ["+country+"]");
		}
	}
	
	public FlushBossInfo() {
		super();
	}
	
	public FlushBossInfo(String equipmentName,long flushTime, byte country,int monsterId,
			SpecialEquipmentAppearMap appearMap) {
		super();
		this.equipmentName = equipmentName;
		this.flushTime = flushTime;
		this.country = country;
		this.appearMap = appearMap;
		this.monsterId = monsterId;
		effect = true;
	}

	public String equipmentName;
	public int monsterId;
	
	public long getFlushTime() {
		return flushTime;
	}

	public void setFlushTime(long flushTime) {
		this.flushTime = flushTime;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public SpecialEquipmentAppearMap getAppearMap() {
		return appearMap;
	}

	public void setAppearMap(SpecialEquipmentAppearMap appearMap) {
		this.appearMap = appearMap;
	}

	public boolean isEffect() {
		return effect;
	}

	public void setEffect(boolean effect) {
		this.effect = effect;
	}
	public long getAppearMonster() {
		return appearMonster;
	}

	public void setAppearMonster(long appearMonster) {
		this.appearMonster = appearMonster;
	}
	public int getMonsterId() {
		return monsterId;
	}
	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	
}
