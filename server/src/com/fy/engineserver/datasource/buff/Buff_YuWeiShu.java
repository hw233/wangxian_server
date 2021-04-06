package com.fy.engineserver.datasource.buff;

import java.util.Calendar;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.fairyBuddha.challenge.FairyChallengeManager;
import com.fy.engineserver.activity.furnace.FurnaceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Country_yuweishu_zhaoji;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.tool.GlobalTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 国王拉人技能，每天最多10次，同时获得N秒免疫负面状态buff，国王立即回满血，并且持续回血N秒
 * 此buff只能国王使用，因为这个buff会产生另一个buff，用于阻止国王使用另一个技能(王者霸气和御卫术不能同时使用，由于技能方面没有设计共用cd，所以用这种方法达到效果)
 * 
 *
 */
@SimpleEmbeddable
public class Buff_YuWeiShu extends Buff {
	int hpPercent = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.getHp() > 0){
				p.setHp(p.getMaxHP());
			}
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if(cm != null && pm != null){
				boolean isXianjie = false;
				try {
					String mapname = p.getCurrentGame().gi.name;
					isXianjie = RobberyConstant.没飞升玩家不可进入的地图.contains(mapname);
				} catch (Exception e) {
					
				}
				//
				if(p.getCurrentGame() != null && !p.getCurrentGame().gi.禁止使用召唤玩家道具 && !p.isInBattleField() 
						&& !PetDaoManager.getInstance().isPetDao(p.getCurrentGame().gi.name) && !FairyChallengeManager.getInst().isFairyChallengeMap(p.getCurrentGame().gi.name)
						&& !ChestFightManager.inst.isPlayerInActive(p) && !FurnaceManager.inst.isFurnaceMap(p.getCurrentGame().gi.name)){
					for (String mapName : RobberyConstant.限制使用拉令的仙界地图集) {
						if (mapName.equalsIgnoreCase(p.getCurrentGame().gi.name)) {
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.此地图不能拉人);
							p.addMessageToRightBag(hreq);
							return ;
						}
					}
					for (String mapName : RobberyConstant.限制使用拉令的仙界地图集) {
						if (mapName.contains(p.getCurrentGame().gi.name)) {
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.此地图不能拉人);
							p.addMessageToRightBag(hreq);
							return ;
						}
					}
					Country country = cm.countryMap.get(p.getCountry());
					if(country != null){
						long[] ids = country.getYulinjunIds();
						if(ids != null){
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							int hour = calendar.get(Calendar.HOUR_OF_DAY);
							int minute = calendar.get(Calendar.MINUTE);
							int count = 0;
							if(count < CountryManager.国王使用技能拉人次数){
								count++;
								for(int i = 0; i < ids.length; i++){
									long id = ids[i];
									if(id > 0){
										try{
											Player pp = pm.getPlayer(id);
											if(pp != null && pp.isOnline()){
												String res = GlobalTool.verifyMapTrans(pp.getId());
												if (res != null) {
													continue;
												}
												if (isXianjie && pp.getLevel() <= 220) {
													continue;
												}
												WindowManager mm = WindowManager.getInstance();
												MenuWindow mw = mm.createTempMenuWindow(100);
												mw.setDescriptionInUUB(Translate.translateString(Translate.国王召集你们前往,new String[][]{new String[]{Translate.STRING_1,cm.得到官职名(cm.官职(p.getCountry(), p.getId()))},{Translate.PLAYER_NAME_1,p.getName()},new String[]{Translate.STRING_2,hour+":"+minute}}));
												Option_Country_yuweishu_zhaoji option = new Option_Country_yuweishu_zhaoji();
												option.setMapName(p.getGame());
												option.setX(p.getX());
												option.setY(p.getY());
												option.setText(Translate.确定);
												Option cancelOption = new Option_UseCancel();
												cancelOption.setText(Translate.取消);
												mw.setOptions(new Option[]{option,cancelOption});
												CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),
														mw.getId(),mw.getDescriptionInUUB(),mw.getOptions());
												pp.addMessageToRightBag(req);
											}
										}catch(Exception ex){
											
										}
									}
								}
							}
						}
					}
				}else{
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.此地图不能拉人);
					p.addMessageToRightBag(hreq);
				}
				
			}
			
			//给释放者上buff，让施法者不能使用霸体和御卫技能
			BuffTemplateManager bm = BuffTemplateManager.getInstance();
			BuffTemplate template = bm.getBuffTemplateByName(CountryManager.禁用国王专用技能);
			if(template != null){
				Buff bf = template.createBuff(1);
				if(bf != null){
					bf.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					bf.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + CountryManager.禁用霸体和御卫buff时长);
					bf.setCauser(this.getCauser());
					this.getCauser().placeBuff(bf);
				}
			}

			//给释放者上N秒免疫负面状态buff
			template = bm.getBuffTemplateByName(CountryManager.免疫);
			if(template != null){
				Buff bf = template.createBuff(1);
				if(bf != null){
					bf.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					bf.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + CountryManager.免疫时长);
					bf.setCauser(this.getCauser());
					this.getCauser().placeBuff(bf);
				}
			}

			//给释放者上持续回血N秒buff
			template = bm.getBuffTemplateByName(CountryManager.加血);
			if(template != null){
				Buff bf = template.createBuff(1);
				if(bf != null){
					bf.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					bf.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + CountryManager.加血时长);
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
