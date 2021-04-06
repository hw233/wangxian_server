package com.fy.engineserver.activity.godDown;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.STRING_2;
import static com.fy.engineserver.datasource.language.Translate.STRING_3;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;


public class GodDownFlushEntity {

	
	public GodDownFlushEntity(int num){
		
		npcNum = 0;
		dayNum = num;
		
		addTime = SystemTime.currentTimeMillis()+GodDwonManager.十分钟;
		removeTime = SystemTime.currentTimeMillis()+GodDwonManager.半小时;
		int[] ints = GodDwonManager.指定结束时间[num];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, ints[0]);
		cal.set(Calendar.MINUTE, ints[1]);
		本次刷新实体的结束时间 = cal.getTimeInMillis()+1000;
	}
	
	
	public void init(){
		int[] endints = GodDwonManager.指定结束时间[dayNum];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, endints[0]);
		cal.set(Calendar.MINUTE, endints[1]);
		activityEndTime = cal.getTimeInMillis();
		
		
		List<Integer> npcLists = GodDwonManager.getInstance().npcLists;
		List<Integer> countryLists = GodDwonManager.getInstance().countryLists;
		Collections.shuffle(npcLists);
		Collections.shuffle(countryLists);
		int npcSize = npcLists.size();
		int countrySize = countryLists.size();
		npcSequence = new int[npcSize];
		countrySequence = new int[countrySize];
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<npcSize;i++){
			npcSequence[i] = npcLists.get(i);
			sb.append("npcId:"+npcLists.get(i));
		}
		sb.append("|||||");
		for(int j = 0;j<countrySize;j++){
			countrySequence[j] = countryLists.get(j);
			sb.append("countryNum:"+countryLists.get(j));
		}
		GodDwonManager.logger.warn("[天神下凡初始化成功] [第几次"+dayNum+"] [结束时间:"+activityEndTime+"] [顺序:"+sb.toString()+"]");
	}
	
	public long 本次刷新实体的结束时间;
	
	public GodDownTemplate template;
	
	//一天只能三次，今天第几次
	public int dayNum = 0;
	
	//本次活动结束时间 指定其中的三个时间点
	public long activityEndTime;
	
	//四个npc顺序
	public int[] npcSequence;
	// 0,1,2,3 0为中立
	public int[] countrySequence;
	
	
	//第几个npc  0 到3
	public int npcNum;
	
	//生成npc时生成
	public NPC nowNpc;
	public Game nowGame;
	
	//下个npc产生时间
	public long addTime;
	//这个npc消失时间
	public long removeTime;
	
	//true就是通知过了不能在通知
	public boolean createNotice = false;
	public boolean destoryNotice = false;
	public boolean createNpc = false;
	
	public void heartBeat(){
		try{
			long now = SystemTime.currentTimeMillis();
			if(now > removeTime){
				nowGame.removeSprite(nowNpc);
				
				addTime = now + GodDwonManager.十分钟;;
				removeTime = now + GodDwonManager.半小时;
				++npcNum;
				if(nowGame != null && nowNpc != null){
					nowGame.removeSprite(nowNpc);
				}
				destoryNotice = false;
				createNotice = false;
				createNpc = false;
				GodDwonManager.logger.warn("[npc消失] ["+nowNpc.getNPCCategoryId()+"] ["+nowGame.getGameInfo().displayName+"] [第几个npc:"+npcNum+"]");
			}
			if(now >= (addTime-GodDwonManager.五分钟) && !createNotice){
				if(npcNum >= countrySequence.length){
					return;
				}
				template = GodDwonManager.getInstance().flushTemplate(countrySequence[npcNum]);
				nowNpc = MemoryNPCManager.getNPCManager().createNPC(npcSequence[npcNum]);
				nowNpc.setX(template.x);
				nowNpc.setY(template.y);
				Point bornPoint = new Point(template.x, template.y);
				nowNpc.setBornPoint(bornPoint);
				nowGame = GameManager.getInstance().getGameByName(template.map, countrySequence[npcNum]);
				//广播将产生
				产生npc通知();
				createNotice = true;
			} 
			
			if(now >= addTime && !createNpc){
				createNpc = true;
				nowGame.addSprite(nowNpc);
				//产生
				GodDwonManager.logger.warn("[真正产生npc] ["+nowNpc.getName()+"] ["+nowGame.getGameInfo().getName()+"] [x:"+nowNpc.getX()+"] [y:"+nowNpc.getY()+"]");
			}
			
			if(now >= (removeTime - GodDwonManager.五分钟) && !destoryNotice){
				//广播将消失
				删除npc通知();
				destoryNotice = true;
			}
		}catch(Exception e){
			GodDwonManager.logger.error("[天神下凡刷新实体心跳异常]",e);
		}
	}
	
	
	//天神“NPC名字”出现在“国家名”的“世界地图区域名称”，快去找他领取福利！
	public void 产生npc通知(){
		String map = CountryManager.getInstance().得到国家名(countrySequence[npcNum]);
		String des = Translate.translateString(Translate.天神XX出现在XX的区域, new String[][]{{STRING_1,nowNpc.getName()},{STRING_2,map},{STRING_3,template.region}});
		GodDwonManager.noticeTelevision(des);
	}
	
	//天神“NPC名字”出现在“国家名”的“地图名”，5分钟以后消失。快去领取福利吧！
	public void 删除npc通知(){
		String map = CountryManager.getInstance().得到国家名(countrySequence[npcNum]);
		String des = Translate.translateString(Translate.天神XX出现在XX的地图, new String[][]{{STRING_1,nowNpc.getName()},{STRING_2,map},{STRING_3,template.chinaMap}});
		GodDwonManager.noticeTelevision(des);
	}
	
}
