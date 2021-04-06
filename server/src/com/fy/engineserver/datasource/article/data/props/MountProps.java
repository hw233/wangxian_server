package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 特殊道具：坐骑
 *
 */
public class MountProps extends Props{

	/**
	 * 是否为飞行坐骑，true能飞，false不能飞
	 */
	private boolean canFly;

	/**
	 * 速度（百分比）
	 */
	private short speed;

	/**
	 * 坐骑avata
	 */
	private byte avata;
	
	/**
	 * 御剑还是莲花，0御剑 1莲花2葫芦3苍鹰4祥云
	 */
	private byte yujianOrLianhua;

	public boolean isCanFly() {
		return canFly;
	}

	public void setCanFly(boolean canFly) {
		this.canFly = canFly;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public byte getAvata() {
		return avata;
	}

	public void setAvata(byte avata) {
		this.avata = avata;
	}

	public byte getYujianOrLianhua() {
		return yujianOrLianhua;
	}

	public void setYujianOrLianhua(byte yujianOrLianhua) {
		this.yujianOrLianhua = yujianOrLianhua;
	}

	/**
	 * 骑乘坐骑(重写父类方法)
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae){
//		if(haveValidDays && ae.inValidTime != 0 && ae.inValidTime < com.fy.engineserver.gametime.SystemTime.currentTimeMillis()){
//			if(player.getHorseName() == null || !player.getHorseName().equals(name)){
//				HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, "["+this.name+Translate.translate.text_3752);
//				player.addMessageToRightBag(error);
//				return false;
//			}
//		}
//		if(haveValidDays && maxValidDays != 0 && ae.inValidTime == 0 && activationOption == Article.ACTIVATION_OPTION_USE){
//			long lastingTime = maxValidDays*60000;
//			ae.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+lastingTime);
//		}
//		//0表示没有坐骑，1表示骑马，2以后表示飞行
//		if(player.getHorse() == 0){
//			player.setHorseName(name);
//			player.setFlying(canFly);
//			byte[] bs = player.getScheme();
//
//			if(bs != null){
//				//7为坐骑在人物身上的scheme数组下标 9为男人飞行坐骑的下标，8为女人飞行坐骑的下标
//
//					if(player.getSex() == 0){
//						if(bs.length > 13){
//							if(canFly){
//								if(yujianOrLianhua == 0){
//									bs[7] = 0;
//									bs[9] = avata;
//									bs[10] = 0;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(2));
//								}else if(yujianOrLianhua == 1){
//									bs[7] = 0;
//									bs[9] = 0;
//									bs[10] = avata;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(3));
//								}else if(yujianOrLianhua == 2){
//									bs[7] = 0;
//									bs[9] = 0;
//									bs[10] = 0;
//									bs[11] = avata;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(4));
//								}else if(yujianOrLianhua == 3){
//									bs[7] = 0;
//									bs[9] = 0;
//									bs[10] = 0;
//									bs[11] = 0;
//									bs[12] = avata;
//									bs[13] = 0;
//									player.setHorse((byte)(5));
//								}else if(yujianOrLianhua == 4){
//									bs[7] = 0;
//									bs[9] = 0;
//									bs[10] = 0;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = avata;
//									player.setHorse((byte)(6));
//								}
//							}else{
//								bs[7] = avata;
//								bs[9] = 0;
//								bs[10] = 0;
//								bs[11] = 0;
//								bs[12] = 0;
//								bs[13] = 0;
//								player.setHorse((byte)(1));
//							}
//						}
//						
//					}else{
//						if(bs.length > 13){
//							if(canFly){
//								if(yujianOrLianhua == 0){
//									bs[7] = 0;
//									bs[8] = avata;
//									bs[9] = 0;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(2));
//								}else if(yujianOrLianhua == 1){
//									bs[7] = 0;
//									bs[8] = 0;
//									bs[9] = avata;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(3));
//								}else if(yujianOrLianhua == 2){
//									bs[7] = 0;
//									bs[8] = 0;
//									bs[9] = 0;
//									bs[11] = avata;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(4));
//								}else if(yujianOrLianhua == 3){
//									bs[7] = 0;
//									bs[8] = 0;
//									bs[9] = 0;
//									bs[11] = 0;
//									bs[12] = avata;
//									bs[13] = 0;
//									player.setHorse((byte)(5));
//								}else if(yujianOrLianhua == 4){
//									bs[7] = 0;
//									bs[8] = 0;
//									bs[9] = 0;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = avata;
//									player.setHorse((byte)(6));
//								}
//							}else{
//								bs[7] = avata;
//								bs[8] = 0;
//								bs[9] = 0;
//								bs[11] = 0;
//								bs[12] = 0;
//								bs[13] = 0;
//								player.setHorse((byte)(1));
//							}
//						}
//					}
//					player.setScheme(player.getScheme());
//			}
//			short oldSpeedE = player.getSpeedE();
//			short newSpeedE = (short) (oldSpeedE + speed);
//			player.setSpeedE(newSpeedE);
//			if(ArticleManager.logger.isDebugEnabled()){
//				ArticleManager.logger.debug("[使用道具] [坐骑] [成功] [playerId:"+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"] [直接改变人物属性] [yujianOrLianhua:"+yujianOrLianhua+"] [avatar:"+this.avata+"]");
//			}
//		}else{
//			if(player.getHorseName() != null && player.getHorseName().equals(name)){
//				player.setHorse((byte)0);
//				player.setHorseName(null);
//				player.setFlying(false);
//				byte[] bs = player.getScheme();
//				if(bs != null){
//					//7为坐骑在人物身上的scheme数组下标 9为男人飞行坐骑的下标，8为女人飞行坐骑的下标
//					if(player.getSex() == 0){
//						if(bs.length > 13){
//							bs[7] = 0;
//							bs[9] = 0;
//							bs[10] = 0;
//							bs[11] = 0;
//							bs[12] = 0;
//							bs[13] = 0;
//						}
//						
//					}else{
//						if(bs.length > 13){
//							bs[7] = 0;
//							bs[8] = 0;
//							bs[9] = 0;
//							bs[11] = 0;
//							bs[12] = 0;
//							bs[13] = 0;
//						}
//					}
//					player.setScheme(player.getScheme());
//				}
//				short oldSpeedE = player.getSpeedE();
//				short newSpeedE = (short) (oldSpeedE - speed);
//				player.setSpeedE(newSpeedE);
//				if(ArticleManager.logger.isDebugEnabled()){
//					ArticleManager.logger.debug("[收回坐骑][成功] [playerId:"+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] [直接改变人物属性]");
//				}
//			}else if(player.getHorseName() != null){
//				ArticleManager am = ArticleManager.getInstance();
//				Article article = am.getArticle(player.getHorseName());
//				if(article instanceof MountProps){
//					MountProps oldMp = (MountProps)article;
//					String oldHorseName = player.getHorseName();
//					player.setHorse((byte)0);
//					player.setHorseName(null);
//					byte[] bs = player.getScheme();
//					if(bs != null){
//						//7为坐骑在人物身上的scheme数组下标 9为男人飞行坐骑的下标，8为女人飞行坐骑的下标
//						if(player.getSex() == 0){
//							if(bs.length > 13){
//								bs[7] = 0;
//								bs[9] = 0;
//								bs[10] = 0;
//								bs[11] = 0;
//								bs[12] = 0;
//								bs[13] = 0;
//							}
//							
//						}else{
//							if(bs.length > 13){
//								bs[7] = 0;
//								bs[8] = 0;
//								bs[9] = 0;
//								bs[11] = 0;
//								bs[12] = 0;
//								bs[13] = 0;
//							}
//						}
//						player.setScheme(player.getScheme());
//					}
//					short oldSpeedE = player.getSpeedE();
//					short newSpeedE = (short) (oldSpeedE - oldMp.getSpeed());
//					player.setSpeedE(newSpeedE);
//					if(ArticleManager.logger.isDebugEnabled()){
//						ArticleManager.logger.debug("[收回坐骑][成功] [playerId:"+player.getId()+"]["+player.getName()+"] [收回:"+oldHorseName+"] [直接改变人物属性]");
//					}
//				}else{
//					if(ArticleManager.logger.isDebugEnabled()){
//						ArticleManager.logger.debug("[收回坐骑][不成功] [playerId:"+player.getId()+"]["+player.getName()+"][坐骑："+player.getHorseName()+"]不存在或不是坐骑类");
//						return false;
//					}
//				}
//				
//				player.setHorseName(name);
//				player.setFlying(canFly);
//				byte[] bs = player.getScheme();
//				if(bs != null){
//					//7为坐骑在人物身上的scheme数组下标 9为男人飞行坐骑的下标，8为女人飞行坐骑的下标
//
//					if(player.getSex() == 0){
//						if(bs.length > 13){
//							if(canFly){
//								if(yujianOrLianhua == 0){
//									bs[7] = 0;
//									bs[9] = avata;
//									bs[10] = 0;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(2));
//								}else if(yujianOrLianhua == 1){
//									bs[7] = 0;
//									bs[9] = 0;
//									bs[10] = avata;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(3));
//								}else if(yujianOrLianhua == 2){
//									bs[7] = 0;
//									bs[9] = 0;
//									bs[10] = 0;
//									bs[11] = avata;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(4));
//								}else if(yujianOrLianhua == 3){
//									bs[7] = 0;
//									bs[9] = 0;
//									bs[10] = 0;
//									bs[11] = 0;
//									bs[12] = avata;
//									bs[13] = 0;
//									player.setHorse((byte)(5));
//								}else if(yujianOrLianhua == 4){
//									bs[7] = 0;
//									bs[9] = 0;
//									bs[10] = 0;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = avata;
//									player.setHorse((byte)(6));
//								}
//							}else{
//								bs[7] = avata;
//								bs[9] = 0;
//								bs[10] = 0;
//								bs[11] = 0;
//								bs[12] = 0;
//								bs[13] = 0;
//								player.setHorse((byte)(1));
//							}
//						}
//						
//					}else{
//						if(bs.length > 13){
//							if(canFly){
//								if(yujianOrLianhua == 0){
//									bs[7] = 0;
//									bs[8] = avata;
//									bs[9] = 0;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(2));
//								}else if(yujianOrLianhua == 1){
//									bs[7] = 0;
//									bs[8] = 0;
//									bs[9] = avata;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(3));
//								}else if(yujianOrLianhua == 2){
//									bs[7] = 0;
//									bs[8] = 0;
//									bs[9] = 0;
//									bs[11] = avata;
//									bs[12] = 0;
//									bs[13] = 0;
//									player.setHorse((byte)(4));
//								}else if(yujianOrLianhua == 3){
//									bs[7] = 0;
//									bs[8] = 0;
//									bs[9] = 0;
//									bs[11] = 0;
//									bs[12] = avata;
//									bs[13] = 0;
//									player.setHorse((byte)(5));
//								}else if(yujianOrLianhua == 4){
//									bs[7] = 0;
//									bs[8] = 0;
//									bs[9] = 0;
//									bs[11] = 0;
//									bs[12] = 0;
//									bs[13] = avata;
//									player.setHorse((byte)(6));
//								}
//							}else{
//								bs[7] = avata;
//								bs[8] = 0;
//								bs[9] = 0;
//								bs[11] = 0;
//								bs[12] = 0;
//								bs[13] = 0;
//								player.setHorse((byte)(1));
//							}
//						}
//					}
//					player.setScheme(player.getScheme());
//				}
//				short oldSpeedE = player.getSpeedE();
//				short newSpeedE = (short) (oldSpeedE + speed);
//				player.setSpeedE(newSpeedE);
//				if(ArticleManager.logger.isDebugEnabled()){
//					ArticleManager.logger.debug("[使用道具] [坐骑] [成功] ["+this.getName()+"] [playerId:"+player.getId()+"]["+player.getName()+"] ["+getComment()+"] [直接改变人物属性]");
//				}
//			}
//		}
		return true;

	}
@Override
public String canUse(Player p) {
	// TODO Auto-generated method stub
	if (getCanUseType() == 0 && p.isInBattleField()) {
		return Translate.text_3753+this.name+"]";
	}
	if (getCanUseType() == 1 && !p.isInBattleField()) {
		return Translate.text_3754+this.name+"]";
	}
//	if (isFightStateLimit() && p.isFighting()) {
//		if(p.getHorseName() == null || !p.getHorseName().equals(name)){
//			return Translate.translate.text_3755+this.name+"]";
//		}
//	}
	if (getLevelLimit() > p.getLevel())
		return Translate.text_3570+this.name+Translate.text_3571+getLevelLimit()+Translate.text_99;
	if (isGameMapLimitFlag() && getGml() != null) {
		if (p.getGame() != null
				&& p.getGame().equals(getGml().getGameMapName())
				&& (p.getX() - getGml().getX()) * (p.getX() - getGml().getX()) + (p.getY() - getGml().getY())
						* (p.getY() - getGml().getY()) < getGml().getRedius() * getGml().getRedius()) {
			return null;
		} else {
			return Translate.text_3756+this.name+Translate.text_3757+getGml().getGameMapName()+Translate.text_3758+getGml().getX()+","+getGml().getY()+Translate.text_3759;
		}
	}
	if( !canFly && p.getCurrentGame() != null &&  //马 				
			p.getCurrentGame().gi.isLimitMOUNT() ){//禁止骑马
		return Translate.text_3760;
	}
	if( canFly && p.getCurrentGame() != null && //飞行道具			
			p.getCurrentGame().gi.isLimitFLY() ){//禁止飞行
		return Translate.text_3761;
	}
	return null;
}
	
	public String getComment(){
		if(speed >= 0){
			return Translate.text_3210+speed+"%";
		}else{
			return Translate.text_3211+(-speed)+"%";
		}
	}
}
