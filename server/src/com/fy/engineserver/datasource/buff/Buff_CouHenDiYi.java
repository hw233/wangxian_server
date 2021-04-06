package com.fy.engineserver.datasource.buff;

import java.util.ArrayList;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.Monster.DamageRecord;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 将目标者的仇恨列表第一位设置为种植buff的人
 * 并持续一段时间
 * 
 *
 */
@SimpleEmbeddable
public class Buff_CouHenDiYi extends Buff{
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			
		}else if(owner instanceof Monster){
			Monster s = (Monster)owner;
			ArrayList<DamageRecord> al = s.getHatridList();
			DamageRecord dr = null;
			int max = 0;
			for(int i = 0 ; i < al.size() ; i++){
				DamageRecord d = al.get(i);
				if(d.f == this.getCauser()){
					dr = d;
				}
				if(d.hatred > max){
					max = d.hatred;
				}
			}
			if(dr != null){
				dr.hatred = max + 1;
			}else{
				dr = new DamageRecord(this.getCauser(),0,max+1);
				al.add(dr);
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
		
		if(owner instanceof Player){
			
		}else if(owner instanceof Monster){
			Monster s = (Monster)owner;
			ArrayList<DamageRecord> al = s.getHatridList();
			DamageRecord dr = null;
			int max = 0;
			for(int i = 0 ; i < al.size() ; i++){
				DamageRecord d = al.get(i);
				if(d.f == this.getCauser()){
					dr = d;
				}
				if(d.hatred > max){
					max = d.hatred;
				}
			}
			if(dr != null){
				dr.hatred = max + 1;
			}else{
				dr = new DamageRecord(this.getCauser(),0,max+1);
				al.add(dr);
			}
		}
	}

}
