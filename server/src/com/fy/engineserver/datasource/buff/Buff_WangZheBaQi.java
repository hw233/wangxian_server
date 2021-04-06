package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 此buff只能国王使用，因为这个buff会产生另一个buff，用于阻止国王使用另一个技能(王者霸气和御卫术不能同时使用，由于技能方面没有设计共用cd，所以用这种方法达到效果)
 * 
 *
 */
@SimpleEmbeddable
public class Buff_WangZheBaQi extends Buff {
	int hpPercent = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_WangZheBaQi bt = (BuffTemplate_WangZheBaQi)this.getTemplate();
			if(bt.hpPercent != null && bt.hpPercent.length > getLevel()){
				hpPercent = bt.hpPercent[getLevel()];
			}
			//减少人物当前血量
			p.setHp(p.getHp()*(100-hpPercent)/100);
			
			//给释放者上buff，让施法者不能使用霸体和御卫技能
			BuffTemplateManager bm = BuffTemplateManager.getInstance();
			BuffTemplate template = bm.getBuffTemplateByName(Translate.禁用国王专用技能);
			if(template != null){
				Buff bf = template.createBuff(1);
				if(bf != null){
					bf.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					bf.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + CountryManager.禁用霸体和御卫buff时长);
					bf.setCauser(this.getCauser());
					this.getCauser().placeBuff(bf);
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
