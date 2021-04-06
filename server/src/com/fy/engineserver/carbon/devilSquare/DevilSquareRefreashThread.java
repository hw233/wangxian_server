package com.fy.engineserver.carbon.devilSquare;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.carbon.devilSquare.instance.DevilSquare;
import com.fy.engineserver.carbon.devilSquare.model.BaseModel;
import com.fy.engineserver.carbon.devilSquare.model.RefreashModel;
import com.fy.engineserver.carbon.devilSquare.model.RefreashRule;
import com.fy.engineserver.datasource.language.Translate;

/**
 * 刷怪线程
 * @author Administrator
 *
 */
public class DevilSquareRefreashThread extends Thread{
	
	public static Logger logger = DevilSquareManager.logger;
	
	public static boolean isStart = true;
	public static int heartbeatTime = 1000;
	
	@Override
	public void run() {
		long lastNotifyTime = 0;
		int index = 0;
		while(isStart) {
			DevilSquareManager dsm = DevilSquareManager.instance;
			Map<Integer, DevilSquare> dsInst = dsm.dsInst;
			dsm.dsInst = dsInst;
			if(dsInst.size() > 0) {
				Iterator<Integer> ite = dsInst.keySet().iterator();
				DevilSquare ds = null;
				RefreashModel rm = null;
				BaseModel bm = null;
				long currentTime = System.currentTimeMillis();
				int key = -1;
				while(ite.hasNext()) {	
					try{
						key = ite.next();
						if(key == 150) {
							index = 1;
						} else if(key == 190) {
							index = 2;
						} else if(key == 220){
							index = 3 ;
						} else if(key == 110) {
							index = 0;
						}
						ds = dsInst.get(key);
						if(ds != null && ds.getStatus() == DevilSquareConstant.START) {				//刷怪
							bm = dsm.baseModelMap.get(key);
							try{
								boolean isBoxRefreash = false;
//								List<Integer> boxList = bm.getIntever4RefreashBoxs();
								List<Integer[]> tempBoxPoints = new ArrayList<Integer[]>();
								if(ds.intever4Box != null && ds.isBoxAct && ds.boxRefreashTimes >= 1 && ds.intever4Box.size() >= ds.boxRefreashTimes) {
									if(currentTime >= (ds.openTime + ds.intever4Box.get(ds.boxRefreashTimes-1) * 1000)) {
										isBoxRefreash = true;
									}
								}
								if(isBoxRefreash) {
									List<Integer[]> tbo = bm.getBoxPoints();
									while(tempBoxPoints.size() < bm.getBoxNum()) {
										Integer[] ii = tbo.get(ds.random.nextInt(tbo.size()));
										if(!tempBoxPoints.contains(ii)) {
											tempBoxPoints.add(ii);
										}
									}
									ds.notifyRefreashBox(DevilSquareConstant.boxCateId, DevilSquareConstant.boxarticlename[index], DevilSquareConstant.boxlasttime, tempBoxPoints);
								}
							} catch(Exception e) {
								logger.error("[恶魔广场] [刷宝箱出错=" + key + "]", e);
							}
							if(currentTime < (ds.lastStatusChangeT + bm.getPrepareTime() * 1000)) {
								continue;
							}
							if(ds.refreashTimes < bm.getMaxRefreashTimes()) {		//刷小怪
								if(currentTime > (lastNotifyTime + 1000)) {	//怪物死亡复活。。一秒通知一次
									ds.notifyReLiveOldMonster();
								}
								int[] tempKey = dsm.getRefreashModelKey(key, ds.refreashTimes);
								rm = dsm.refreashModelMap.get(tempKey[0]);
								if(tempKey[1] < rm.getRefreashTimes()) {			
									boolean isRefreash = false;
									RefreashRule rfr = rm.getRefreashRules().get(tempKey[1]);
									if(ds.lastRefreashTime == 0) {								//副本刚开还没有刷过怪
										if(currentTime >= (ds.lastStatusChangeT + bm.getPrepareTime() * 1000 + rfr.getInterval() * 1000)) {
											isRefreash = true;
										} else if(!ds.firstNoticeFlag){
											if(rfr.getInterval() > 30) {		//30s=！！！
												ds.countMap.put(DevilSquareConstant.count_refreashMonster, (currentTime + (rfr.getInterval() - 30) * 1000));
												if(logger.isDebugEnabled()) {
													logger.debug("[恶魔广场刷怪倒计时] [间隔n秒后执行:" + (rfr.getInterval() - 30) + "]");
												}
											} else {
												ds.noticeAllPlayerCountTime(DevilSquareConstant.count_refreashMonster, rfr.getInterval(), Translate.下波怪物倒计时);
											}
											ds.firstNoticeFlag = true;
										}
									} else if(currentTime >= (ds.lastRefreashTime + rfr.getInterval() * 1000)) {
										isRefreash = true;
									}
									if(isRefreash) {
										try{
											if((ds.refreashTimes + 1) >= bm.getMaxRefreashTimes()) {
												ds.noticeAllPlayerCountTime(DevilSquareConstant.count_cleanMonster, bm.getIntervarl4Clean(), Translate.清除怪物倒计时);
											} else {
												int[] tempKey1 = dsm.getRefreashModelKey(key, ds.refreashTimes + 1);
												RefreashModel rm2 = dsm.refreashModelMap.get(tempKey1[0]);
												RefreashRule rfr2 = rm2.getRefreashRules().get(tempKey1[1]);
												if(rfr2.getInterval() > 3) {			//超过3s的才通知
													if(rfr2.getInterval() > 30) {
														ds.countMap.put(DevilSquareConstant.count_refreashMonster, (currentTime + (rfr2.getInterval() - 30) * 1000));
														if(logger.isDebugEnabled()) {
															logger.debug("[恶魔广场刷怪倒计时] [间隔n秒后执行:" + (rfr2.getInterval() - 30) + "]");
														}
													} else {
														ds.noticeAllPlayerCountTime(DevilSquareConstant.count_refreashMonster, rfr2.getInterval(), Translate.下波怪物倒计时);
													}
												}
											}
										}catch (Exception e) {
											logger.error("[刷怪通知异常]", e);
										}
										ds.notifyRefreashMonster(rfr.getMonsterIds().get(0), rm.getRefreashPoints(), rfr.getMaxNumbers(), rfr.getIntervalRevive());
										if(logger.isInfoEnabled()) {
											logger.info("[**********恶魔广场刷小怪---]"+ key +"[**********monsterid:" + rfr.getMonsterIds().get(0) + "]");
										}
									}
								}
							} else if(ds.refreashTimes == bm.getMaxRefreashTimes()) {		//小怪已经刷完，需要判断是否达到清屏时间，刷新boss
								boolean isRefreash = false;
								if(currentTime >= (ds.lastRefreashTime + bm.getIntervarl4Clean() * 1000) && !ds.isBossTime) {
									ds.notifyRemoveAllMonster();
									ds.noticeAllPlayerCountTime(DevilSquareConstant.count_refreashBoss, bm.getInterverl4Boss2(), Translate.boss刷新倒计时);
								}
								if(currentTime >= (ds.lastRefreashTime + bm.getInterverl4Boss2() * 1000 +  bm.getIntervarl4Clean() * 1000)) {
									isRefreash = true;
								}
								if(isRefreash) {
									List<Integer[]> tempList = new ArrayList<Integer[]>();
									tempList.add(bm.getBossPoint());
									ds.notifyRemoveAllMonster();
									ds.notifyRefreashMonster(bm.getBossId(), tempList, 1, 0);
									if(logger.isInfoEnabled()) {
										logger.info("[**********恶魔广场刷boss---]"+ key +"[***********bossid:" + bm.getBossId() + "]");
									}
								}
							}
						}
					}catch(Exception e) {
						logger.error("[恶魔广场刷怪出错] [副本等级：" + key + "]", e);
					}
				}
			}
			try {
				Thread.sleep(heartbeatTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				DevilSquareManager.logger.error("[恶魔广场刷怪线程出错]", e);
			}
		}
	}

}
