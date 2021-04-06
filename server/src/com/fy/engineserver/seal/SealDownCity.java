package com.fy.engineserver.seal;

import java.util.List;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;
import com.fy.engineserver.seal.SealManager.BossDeadInfo;
import com.fy.engineserver.seal.data.SealTaskInfo;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.petdao.PetDaoManager;

public class SealDownCity {

	private Game game;
	/**
	 * 1：玩家离开副本
	 * 2: BOSS死亡
	 * 3:玩家死亡
	 * 8:回城成功，心跳停止
	 * 4:破封任务开启时间结束
	 */
	public int stat;	
	public int times = 10000;	//倒计时
	public long runTime;
	private int buffnums;
	private int bossnums;	//对于不能立马刷出boss的心跳计数
	private long lasttimes;
	
	public void heartbeat(){
		if(game!=null){
			game.heartbeat();
			String resultMess = "正在挑战";
			LivingObject livingObjectArray[] = game.getLivingObjects();
			Monster m = null;
			Player player = null;
			for(LivingObject lo : livingObjectArray){
				if(lo!=null){
					if(lo instanceof Player){
						player = (Player)lo;
					}else if(lo instanceof Monster){
						m = (Monster)lo;
					}
				}
			}
			
			if(player==null){
				stat = 1;
				resultMess = "玩家离开副本";
			}
			
			if(stat==0&&player!=null){
				if(player.isFlying()){
					player.downFromHorse();
				}
				if(player.isDeath() || player.isAlive()==false){
					stat = 3;
					resultMess = "玩家死亡,挑战失败";
				}
				if(SealManager.getInstance().isCloseCurrSeal()){
					stat = 4;
					resultMess = "破封任务开启时间结束";
					player.sendError(Translate.破封任务结束);
				}
			}
			
			
			
			if(m!=null && stat==0 && (m.isAlive()==false || m.isDeath())){
				//增加封印阶段
				//缩短封印时间
				stat = 2;
				long bossid = m.getSpriteCategoryId();
				int currSealStep = SealManager.getInstance().getSeal().getSealStep();
				int selectSealStep = player.optionSealStep;
				List<SealCityBossInfo> infos = SealManager.getInstance().infos;
				SealCityBossInfo sealinfo = null;
				long newSealTime = System.currentTimeMillis();
				long oldSealTime = SealManager.getInstance().getSeal().getSealCanOpenTime();
				
				for(SealCityBossInfo info : infos){
					if(selectSealStep==info.sealLayer && info.sealLevel==SealManager.getInstance().getSealLevel()){
						sealinfo = info;
						break;
					}
				}
				
				String key = SealManager.getInstance().getSealLevel()+selectSealStep+SealManager.getInstance().KEY_BOSS_DEAD_NUM+player.getId();
				BossDeadInfo bossinfo = (BossDeadInfo) ActivityManagers.getInstance().getDdc().get(key);
				if(bossinfo==null){
					bossinfo = new BossDeadInfo();
					bossinfo.setDeadNums(0);
					bossinfo.setFirstDeadTime(System.currentTimeMillis());
					ActivityManagers.getInstance().getDdc().put(key, bossinfo);
					bossinfo = (BossDeadInfo)ActivityManagers.getInstance().getDdc().get(key);
				}
				int newNums = bossinfo.getDeadNums()+1;
				if(ActivityManagers.isSameDay(System.currentTimeMillis(), bossinfo.getFirstDeadTime())==false){
					bossinfo.setDeadNums(1);
				}else{
					bossinfo.setDeadNums(newNums);
				}
				bossinfo.setFirstDeadTime(System.currentTimeMillis());
			
				ActivityManagers.getInstance().getDdc().put(key, bossinfo);
				
				if(sealinfo==null){
					resultMess = "出错，请检查当前封印阶段的boss配置";
				}else{
					if(currSealStep==3){
						resultMess = "已达最大阶段，封印阶段不变";
					}else if(selectSealStep==currSealStep){
						resultMess = "已经击杀过，封印阶段不变";
					}else if(selectSealStep>currSealStep){
						if(newSealTime + sealinfo.cutSealTime < oldSealTime){
							newSealTime = oldSealTime - sealinfo.cutSealTime;
						}
						try {
							SealTaskInfo taskinfo = SealManager.getInstance().getTakInfo(SealManager.getInstance().getSealLevel(), currSealStep,0);
							if(taskinfo==null){
								taskinfo = new SealTaskInfo();
								SealManager.getInstance().saveInfo(taskinfo);
							}
							int newstep = selectSealStep;
							if(newstep<0){
								newstep = 0;
							}
							if(newstep>=3){
								newstep = 2;
							}
							
							if(taskinfo.getBossid()==0){
								taskinfo.setSealLevel(SealManager.getInstance().getSealLevel());
								taskinfo.setBossid(sealinfo.bossId);
								resultMess = "击杀boss成功，首次杀过";
								SealManager.getInstance().getSeal().setSealCanOpenTime(newSealTime,"首次击杀boss");
								SealManager.getInstance().getSeal().setSealStep(newstep);
								
								SealManager.getInstance().changeSealTime();
								String days = "";
								if(newSealTime>System.currentTimeMillis()){
									days = (newSealTime - System.currentTimeMillis())/(24*60*60*1000)+"";
								}else{
									days = 0+"";
								}
								SealManager.getInstance().saveSeal();
								String mess = Translate.translateString(Translate.封印首次击杀boss, new String[][]{{ Translate.STRING_1,CountryManager.得到国家名(player.getCountry())+"●"+player.getName() },{ Translate.STRING_2,m.getName() }, { Translate.STRING_3, SealManager.getInstance().getSealLevel()+""}, { Translate.STRING_4,(sealinfo.cutSealTime/(SealManager.一天毫秒数))+""}, { Translate.STRING_5, days}});
								ChatMessageService cms = ChatMessageService.getInstance();
								ChatMessage msg = new ChatMessage();
								msg.setMessageText(mess);
								cms.sendColorMessageToWorld(msg,true);
								
								{
									SealTaskInfo taskinfo_next = SealManager.getInstance().getTakInfo(SealManager.getInstance().getSealLevel(), newstep,0);
									if(taskinfo_next!=null){
										taskinfo_next.setSealStep(newstep);
										SealManager.getInstance().saveInfo(taskinfo_next);
									}
								}
								
							}else{
								resultMess = "击杀boss成功，已经击杀过";
								player.sendError(Translate.boss挑战成功);
							}
							int bufflevel = taskinfo.getBuffLevel()+1;
							if(bufflevel>5){
								bufflevel = 5;
							}
							taskinfo.setBuffLevel(bufflevel);
							SealManager.getInstance().saveInfo(taskinfo);
						} catch (Exception e) {
							e.printStackTrace();
							Game.logger.warn("[封印副本线程] [异常:{}] [resultMess:{}]",new Object[]{e,resultMess});
							stat = 2;
						}
					}
					
					if(Game.logger.isWarnEnabled()){
						Game.logger.warn("[封印副本线程] [击杀boss成功] [{}] [resultMess:{}] [bossid:{}] [seallevel:{}] [缩短的封印时间:{}] [封印时间:{}] [当前阶段，选择阶段:{}]",new Object[]{newNums,resultMess,bossid,SealManager.getInstance().getSealLevel(),(sealinfo.cutSealTime),(oldSealTime+"-->"+newSealTime),(currSealStep+"-->"+selectSealStep)});
					}
				}
			}
			
			
			
			if(stat!=0){
				if(player!=null){
					if(runTime==0){
						NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
						resp.setCountdownType((byte)50);
						resp.setLeftTime(times/1000);
						resp.setDescription(Translate.离开副本倒计时);
						player.addMessageToRightBag(resp);
						runTime = System.currentTimeMillis();
					}
					if(System.currentTimeMillis()-runTime>=times){
						stat = 8;
						回城(player);
					}
					Long lasttime = SealManager.playersOfDownCitys.get(new Long(player.getId()));
					lasttimes = lasttime.longValue();
					if(lasttime!=null && lasttimes>0){
						SealManager.playersOfDownCitys.put(new Long(player.getId()), new Long(0));
						lasttimes = lasttime.longValue();
					}
				}else{
					stat = 8;
//					回城(player);
				}	
				Game.logger.warn("[封印副本线程] [离开副本进入倒计时1] [lasttimes:{}] [倒计时:{}秒] [stat:{}] [玩家:{}] [runTime:{}]",new Object[]{lasttimes,(System.currentTimeMillis()-runTime)/1000,stat,(player==null?"null":player.getName()),runTime});
			}

			if(m==null){
				bossnums++;
			}
			
			SealCityBossInfo bossinfo = null;
			if(m!=null && m.getHp()<m.getMaxHP() && buffnums==0){
				buffnums = 1;
				for(SealCityBossInfo info : SealManager.getInstance().infos){
					if(info.sealLevel==SealManager.getInstance().getSealLevel() && info.sealLayer==player.optionSealStep){
						bossinfo = info;
					}
				}
				if(bossinfo!=null && bossinfo.buffid>0){
					SealTaskInfo taskInfo = SealManager.getInstance().getTakInfo(SealManager.getInstance().getSealLevel(), SealManager.getInstance().getSeal().getSealStep(),0);
					if(taskInfo!=null){
						int buffLevel = taskInfo.getBuffLevel();
						if(buffLevel>5){
							buffLevel = 5;
						}
						int buffId = bossinfo.buffid;
						BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(buffId);
						if(bt!=null){
							Buff buff = bt.createBuff(buffLevel);
							buff.setStartTime(SystemTime.currentTimeMillis());
							buff.setInvalidTime(buff.getStartTime() + 100*24*60*60*1000);
							if(buff!=null){
								m.placeBuff(buff);
								if(Game.logger.isWarnEnabled()){
									Game.logger.warn("[封印副本线程] [刷怪物] [种植buff成功] [buff:{}] [boss:{}] [玩家:{}]",new Object[]{buff.getTemplateName()+"--"+buff.getLevel(),m.getName(), player.getName()});
								}
							}
						} 
					}else{
						if(Game.logger.isWarnEnabled()){
							Game.logger.warn("[封印副本线程] [taskInfo==null]");
						}
					}
				}
			}
			
			if(Game.logger.isWarnEnabled()){
				Game.logger.warn("[封印副本线程] [{}] [bossinfo:{}] [runTime:{}] [stat:{}] [血量:{}] [最大血量:{}]  [buff数:{}] [boss:{}] [islive:{}] [isdead:{}] [id:{}] [玩家：{}]",new Object[]{resultMess,(bossinfo==null?"null":bossinfo.buffid),runTime,stat,(m!=null?m.getHp():"0"),(m!=null?m.getMaxHP():"0"),buffnums,(m!=null?m.getName():""), (m!=null?m.isAlive():""),(m!=null?m.isDeath():""),(m!=null?m.getSpriteCategoryId():"null"),(player==null?"null":player.getName()) });
			}
			
			if(bossnums>=5){
				if(player!=null && m==null){
					if(runTime==0){
						NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
						resp.setCountdownType((byte)50);
						resp.setLeftTime(5);
						resp.setDescription(Translate.离开副本倒计时);
						player.addMessageToRightBag(resp);
						runTime = System.currentTimeMillis();
					}
					if(System.currentTimeMillis()-runTime>=times){
						回城(player);
					}
					Game.logger.warn("[封印副本线程] [离开副本进入倒计时2] [倒计时:{}秒] [stat:{}] [玩家:{}] [runTime:{}]",new Object[]{(System.currentTimeMillis()-runTime)/1000,stat,(player==null?"null":player.getName()),runTime});
				}
			}
		}
	}
	
	private void 回城(Player p){
		try{
			game.transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()));
			SealManager.playersOfDownCitys.put(new Long(p.getId()), new Long(System.currentTimeMillis()));
			stat = 8;
			game=null;
		}catch(Exception e){
			PetDaoManager.log.warn("[封印副本线程] [回城] [异常]"+e);
		}
	}

	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
}
