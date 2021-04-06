package com.fy.engineserver.datasource.buff;

import java.util.ArrayList;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.ConvoyNPC;
import com.fy.engineserver.sprite.npc.GuardNPC;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 驱散不利的buff
 *
 */
@SimpleEmbeddable
public class Buff_QuSan extends Buff{

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			Buff buffs[] = p.getActiveBuffs();
			for(int i = 0 ; i < buffs.length ; i++ ){
				if(buffs[i] instanceof Buff_DingSheng || buffs[i] instanceof Buff_XuanYun || buffs[i] instanceof Buff_ZhongDu || buffs[i] instanceof Buff_ZhongDuWuGong || buffs[i] instanceof Buff_ZhongDuFaGong || buffs[i] instanceof Buff_JianShu || buffs[i] instanceof Buff_XuLuo || buffs[i] instanceof Buff_JianBaoJi || buffs[i] instanceof Buff_JianGong || buffs[i] instanceof Buff_JianLiLiang || buffs[i] instanceof Buff_JianMingJie || buffs[i] instanceof Buff_JianMingZhong || buffs[i] instanceof Buff_JianShanBi || buffs[i] instanceof Buff_JianTiZhi || buffs[i] instanceof Buff_JianZhiLi || buffs[i] instanceof Buff_ChenMo || buffs[i] instanceof Buff_JianXue || buffs[i] instanceof Buff_JiangDiZhengTiShuChuBiLi || buffs[i] instanceof Buff_JianLanWuGong || buffs[i] instanceof Buff_JianLanFaGong || buffs[i] instanceof Buff_ZhongDuZuiHouFaZuoWuGong || buffs[i] instanceof Buff_ZhongDuZuiHouFaZuoFaGong || buffs[i] instanceof Buff_JianLanBuff){
					buffs[i].setInvalidTime(0);
				}
			}
		}else if(owner instanceof Monster){
			Monster s = (Monster)owner;
			ArrayList<Buff> buffs = s.getBuffs();
			for(int i = 0 ; i < buffs.size() ; i++ ){
				Buff buff = buffs.get(i);
				if(buff instanceof Buff_DingSheng || buff instanceof Buff_XuanYun || buff instanceof Buff_ZhongDu || buff instanceof Buff_ZhongDuWuGong || buff instanceof Buff_ZhongDuFaGong || buff instanceof Buff_JianShu || buff instanceof Buff_XuLuo || buff instanceof Buff_JianBaoJi || buff instanceof Buff_JianGong || buff instanceof Buff_JianLiLiang || buff instanceof Buff_JianMingJie || buff instanceof Buff_JianMingZhong || buff instanceof Buff_JianShanBi || buff instanceof Buff_JianTiZhi || buff instanceof Buff_JianZhiLi || buff instanceof Buff_ChenMo || buff instanceof Buff_JianXue || buff instanceof Buff_JiangDiZhengTiShuChuBiLi || buff instanceof Buff_JianLanWuGong || buff instanceof Buff_JianLanFaGong || buff instanceof Buff_ZhongDuZuiHouFaZuoWuGong || buff instanceof Buff_ZhongDuZuiHouFaZuoFaGong || buff instanceof Buff_JianLanBuff){
					buff.setInvalidTime(0);
				}
			}
		}else if(owner instanceof GuardNPC){
			GuardNPC s = (GuardNPC)owner;
			ArrayList<Buff> buffs = s.getBuffs();
			for(int i = 0 ; i < buffs.size() ; i++ ){
				Buff buff = buffs.get(i);
				if(buff instanceof Buff_DingSheng || buff instanceof Buff_XuanYun || buff instanceof Buff_ZhongDu || buff instanceof Buff_ZhongDuWuGong || buff instanceof Buff_ZhongDuFaGong || buff instanceof Buff_JianShu || buff instanceof Buff_XuLuo || buff instanceof Buff_JianBaoJi || buff instanceof Buff_JianGong || buff instanceof Buff_JianLiLiang || buff instanceof Buff_JianMingJie || buff instanceof Buff_JianMingZhong || buff instanceof Buff_JianShanBi || buff instanceof Buff_JianTiZhi || buff instanceof Buff_JianZhiLi || buff instanceof Buff_ChenMo || buff instanceof Buff_JianXue || buff instanceof Buff_JiangDiZhengTiShuChuBiLi || buff instanceof Buff_JianLanWuGong || buff instanceof Buff_JianLanFaGong || buff instanceof Buff_ZhongDuZuiHouFaZuoWuGong || buff instanceof Buff_ZhongDuZuiHouFaZuoFaGong || buff instanceof Buff_JianLanBuff){
					buff.setInvalidTime(0);
				}
			}
		}else if(owner instanceof ConvoyNPC){
			ConvoyNPC s = (ConvoyNPC)owner;
			ArrayList<Buff> buffs = s.getBuffs();
			for(int i = 0 ; i < buffs.size() ; i++ ){
				Buff buff = buffs.get(i);
				if(buff instanceof Buff_DingSheng || buff instanceof Buff_XuanYun || buff instanceof Buff_ZhongDu || buff instanceof Buff_ZhongDuWuGong || buff instanceof Buff_ZhongDuFaGong || buff instanceof Buff_JianShu || buff instanceof Buff_XuLuo || buff instanceof Buff_JianBaoJi || buff instanceof Buff_JianGong || buff instanceof Buff_JianLiLiang || buff instanceof Buff_JianMingJie || buff instanceof Buff_JianMingZhong || buff instanceof Buff_JianShanBi || buff instanceof Buff_JianTiZhi || buff instanceof Buff_JianZhiLi || buff instanceof Buff_ChenMo || buff instanceof Buff_JianXue || buff instanceof Buff_JiangDiZhengTiShuChuBiLi || buff instanceof Buff_JianLanWuGong || buff instanceof Buff_JianLanFaGong || buff instanceof Buff_ZhongDuZuiHouFaZuoWuGong || buff instanceof Buff_ZhongDuZuiHouFaZuoFaGong || buff instanceof Buff_JianLanBuff){
					buff.setInvalidTime(0);
				}
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
	
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
