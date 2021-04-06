package com.fy.engineserver.movedata;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.marriage.MarriageInfo;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.uniteserver.UnitedServerManager2;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

public class DataMove2<T> implements Runnable{
	
	Queue<T> input;
	
	Class<T> clazz;
	
	Field versionField;// 要清除version
	
	Field idField = null;
	
	DataCollect2<T> dataCollect;
	
	private byte checkType = -1;	//检测类型   1：直接simpleId就是playerId   2：自定义变量为playerId
	
	public long savedNum = 0;
	
	public boolean running = true;
	
	public static long checkVersionTime = 3 * 60 * 1000;
	
	private long lastCheckTime = 0;
	
	long startTime ;
	
	boolean checkPlayer = false;
	
	Long[] counts = new Long[]{0L,0L, 0L};		//counts[0]=总数  count[1]=有效数据数 count[2]耗时
	
	int receiveTimes = 0;// 接收到数据次数
	int stopTimes = 0;// 暂停次数
	long receiveTotalNum = 0L;// 接收到总记录数
	
	public DataMove2(Queue<T> input, Class<T> clazz, DataCollect2<T> dataCollect, byte checkType) {
		this.input = input;
		this.clazz = clazz;
		this.dataCollect = dataCollect;
		this.checkType = checkType;
		
		versionField = DataMoveManager.getFieldByAnnotation(clazz, SimpleVersion.class);
		versionField.setAccessible(true);
		idField = DataMoveManager.getFieldByAnnotation(clazz, SimpleId.class);
		if (idField == null) {
			throw new IllegalStateException("[未找到ID字段] [" + clazz.toString() + "]");
		}
		idField.setAccessible(true);
	}

	public void start(){
		new Thread(this,clazz.getSimpleName()).start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String log = "[接收数据] [" + clazz.getSimpleName() + "]";
		
		List<T> notifiedList = new ArrayList<T>();
		startTime = System.currentTimeMillis();
		lastCheckTime = startTime;
		
		boolean isJiazuMember = clazz.getClass().isAssignableFrom(JiazuMember.class);
		checkPlayer = clazz.isAssignableFrom(Player.class);
		
		SimpleEntityManager<T> entityManager = DataMoveManager.instance.getFactory().getSimpleEntityManager(clazz);
		
		List<Long> ids = null;
		if (DataMoveManager.isdebug) {
			ids = new ArrayList<Long>();
		}
		
		Field field = null;
		if (checkType == DataMoveManager.checkTypeVar) {
			String varName = DataMoveManager.checkVarClass.get(clazz);
			if (varName == null) {
				DataMoveManager.logger.error("[需要检测变量但是未加入到checkVarClass中] [class:" + clazz.getSimpleName() + "]");
				running = false;
				return;
			}
			try {
				field = clazz.getDeclaredField(varName);
				field.setAccessible(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				DataMoveManager.logger.error("[反射获取变量异常] [class:" + clazz.getSimpleName() + "] [varName:" + varName + "]", e);
				running = false;
				return ;
			} 
		}
		while (dataCollect.running || input.size() > 0) {
			
			Iterator<T> itor = notifiedList.iterator();
			while (itor.hasNext()) {
				T t = itor.next();
				try {
					int version = versionField.getInt(t);
					if (version != 0) {
						long id = idField.getLong(t);
						itor.remove();
						savedNum++;
						if (DataMoveManager.logger.isInfoEnabled()) {
							DataMoveManager.logger.info(log + " [移除已经存储的对象] [id:" + id + "]");
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			try {
				// 防止过多的调用底层的countUnSavedNewObjects。暂停100毫秒
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
//			/**********************************************/
//			DataMoveManager.instance.getFactory().getSimpleEntityManager(clazz).setBatchSaveOrUpdateSize(50);
//			/**********************************************/
			
			long countUnSavedNewObjects = entityManager.countUnSavedNewObjects();
			if (countUnSavedNewObjects >= UnitedServerManager.singleClassUnsaveMax) {
				DataMoveManager.logger.warn(log + " [当前底层未存储量太大,线程暂停2秒] [存量情况:" + countUnSavedNewObjects + "/" + UnitedServerManager.singleClassUnsaveMax + "] [存储数量:" + savedNum + "/" + dataCollect.totalCount + "] [完成比例:" + new DecimalFormat("0.0000%").format(((double) savedNum) / dataCollect.totalCount) + "]");
				try {
					Thread.sleep(2000L);
					stopTimes++;
				} catch (InterruptedException e) {
					DataMoveManager.logger.error(log + " [线程sleep异常]", e);
				}
			} else {
				try {
					List<T> onceNoticeSimpleJPA = new ArrayList<T>();
					synchronized (input) {
						int leftNum = UnitedServerManager.singleClassUnsaveMax;
						while (leftNum > 0 && input.size() > 0) {
							onceNoticeSimpleJPA.add(input.poll());
							leftNum--;
						}
					}
					if (onceNoticeSimpleJPA.size() == 0) {
						continue;
					}
					SimpleEntityManager<T> mm = entityManager;
					mm.releaseReference();
					for (T t : onceNoticeSimpleJPA) {
						try {
							counts[0]++;
							if (DataMoveManager.tiaoshi) {
//								boolean isPlayer = t.getClass().isAssignableFrom(Player.class);
								long id = 0;
								if (checkPlayer) {
									id = idField.getLong(t);
									if (!DataMoveManager.instance.allPlayerIds.contains(id)) {
										DataMoveManager.instance.allPlayerIds.add(id);
									} else {
										DataMoveManager.logger.info("[接受查询出来的角色] [重复] [id:" + id + "]");
									}
								}
								if (checkPlayer && DataMoveManager.logger.isInfoEnabled()) {
									DataMoveManager.logger.info("[接受到查询出来的角色] [isPlayer:"+checkPlayer+"] [getPlayerIds()跳过] [id : " + id + "] [" + this.clazz.getSimpleName() + "]");
								}
							}
							if (checkType == DataMoveManager.checkTypeSimpleId) {		//simpleId就是playerId
								long id = idField.getLong(t);
//								boolean isPlayer = t.getClass().isAssignableFrom(Player.class);
								if (DataMoveManager.instance.getPlayerIds().contains(id)) {
									if (checkPlayer && DataMoveManager.logger.isInfoEnabled()) {
										DataMoveManager.logger.info("[isPlayer:"+checkPlayer+"] [getPlayerIds()跳过] [id : " + id + "] [" + this.clazz.getSimpleName() + "]");
									}
									continue;
								} else if (!checkPlayer && DataMoveManager.instance.getNeedKeepPlayer().contains(id)) {	//除player和背包数据都清除
//									if (DataMoveManager.logger.isInfoEnabled()) {
//										DataMoveManager.logger.info("[isPlayer:"+isPlayer+"] [getNeedKeepPlayer()跳过] [id : " + id + "] [" + this.clazz.getSimpleName() + "]");
//									}
									continue;
								} else if (t.getClass().isAssignableFrom(Relation.class)) {
									Relation relation = (Relation) t;
									if (DataMoveManager.instance.needRemoveMarriageId.contains(relation.getMarriageId())) {
										((Relation) t).setMarriageId(-1);
									}
									List<Long> needRemoveFriIds = new ArrayList<Long>();
									List<Long> needRemoveChouIds = new ArrayList<Long>();
									for (long pId : relation.getFriendlist()) {
										if (DataMoveManager.instance.getPlayerIds().contains(pId)) {
											needRemoveFriIds.add(pId);
										}
									}
									for (long pId : relation.getChourenlist()) {
										if (DataMoveManager.instance.getPlayerIds().contains(pId)) {
											needRemoveChouIds.add(pId);
										}
									}
									for (long pId : needRemoveFriIds) {
										((Relation) t).getFriendlist().remove(pId);
									}
									for (long pId : needRemoveChouIds) {
										((Relation) t).getChourenlist().remove(pId);
									}
									needRemoveChouIds = null;
									needRemoveFriIds = null;
								}
							} else if (field != null) {	//playerId是simpleId外自定义的变量
								long pId = Long.parseLong(field.get(t).toString());
								if (DataMoveManager.instance.getPlayerIds().contains(pId)) {
									if (isJiazuMember) {			//如果是jiazumember，需要将id暂时存储起来，方便在转移结束后改变jiazu成员列表存入新库
										JiazuMember jm = (JiazuMember) t;
										DataMoveManager.instance.notifyNewNeedRemoveMembers(jm.getJiazuID(), jm.getJiazuMemID());
									}
									continue;
								}
								if (t.getClass().isAssignableFrom(Pet.class)) {
									Pet pet = (Pet) t;
									if (pet.isDelete() && pet.getRarity() < 2) {
										continue;
									}
								}
							} else if (checkType == DataMoveManager.checkTypeOther) {		//其他一些特殊的判断
								if (t.getClass().isAssignableFrom(MarriageInfo.class)) {
									MarriageInfo minfo = (MarriageInfo) t;
									//结婚双方有一方数据删除都需要将结婚数据清除掉
									if (DataMoveManager.instance.getPlayerIds().contains(minfo.getHoldA()) || DataMoveManager.instance.getPlayerIds().contains(minfo.getHoldB())) {
										DataMoveManager.instance.needRemoveMarriageId.add(minfo.getId());
										continue;
									}
								}
							}
						} catch (Exception e2) {
							DataMoveManager.logger.error("[异常2]", e2);
						}
						
						long idd = idField.getLong(t);
						try {
							if (DataMoveManager.isdebug) {
								if (!ids.contains(idd)) {
									versionField.setInt(t, 0);
									if (checkPlayer) {
										try {
											entityManager.flush(t);
											counts[1]++;
											if (!DataMoveManager.instance.needKeepPlayerIds.contains(idd)) {
												DataMoveManager.instance.needKeepPlayerIds.add(idd);
											}
										} catch (Exception e) {
											DataMoveManager.logger.error("[flush角色异常] [id:" + idd + "]", e);
										}
									} else {
										entityManager.notifyNewObject(t);
									}
	//								ids.add(idd);
									notifiedList.add(t);
									if (DataMoveManager.tiaoshi) {
	//									boolean isPlayer = t.getClass().isAssignableFrom(Player.class);
										if (checkPlayer) {
											if (!DataMoveManager.instance.hasSave.contains(idd)) {
												DataMoveManager.instance.hasSave.add(idd);
											} else {
												DataMoveManager.logger.info("[存储查询出来的角色] [异常] [出现重复] [id : " + idd + "]");
											}
										}
									}
									if (DataMoveManager.logger.isInfoEnabled()) {
										DataMoveManager.logger.info("[notifyNewObject] [" + clazz.getSimpleName() + "] [id:" + idField.getLong(t) + "]");
									}
								} else {
									DataMoveManager.logger.warn("[主键冲突] [类："+clazz.getSimpleName()+"] [id:" + idd + "]");
								}
							} else {
								versionField.setInt(t, 0);
								entityManager.notifyNewObject(t);
								notifiedList.add(t);
								counts[1]++;
								if (DataMoveManager.logger.isInfoEnabled()) {
									DataMoveManager.logger.info("[notifyNewObject] [" + clazz.getSimpleName() + "] [id:" + idField.getLong(t) + "]");
								}
							}
						}catch (Exception e) {
							DataMoveManager.logger.error("[异常233333] [idd:" + idd + "]", e);
						}
						
					}
					
					receiveTotalNum += onceNoticeSimpleJPA.size();
					receiveTimes++;
					DataMoveManager.logger.warn(log + " [存储数据] [" + onceNoticeSimpleJPA.size() + "个] [接收数据:" + receiveTimes + "次] [累计:" + receiveTotalNum + "条]  [总记录数:" + dataCollect.totalCount + "] [接收比例:" + new DecimalFormat("0.0000%").format(((double) receiveTotalNum) / dataCollect.totalCount) + "] [总耗时:" + (System.currentTimeMillis() - startTime) + "]");
				} catch (Exception e) {
					DataMoveManager.logger.error("[异常]", e);
				}
			}
		}
		
		// 处理那些其他都执行完毕，但是还有未被存储的对象，防止GC导致对象丢失
		int loopCount = 0;
		while (notifiedList.size() > 0) {
			try {
				loopCount++;
				long now = System.currentTimeMillis();
				boolean isCheck = false;
				Thread.sleep(5000);
				{	//剔除version不等于0的数据
					Iterator<T> itor = notifiedList.iterator();
					while (itor.hasNext()) {
						T t = itor.next();
						try {
							int version = versionField.getInt(t);
							if (version != 0) {
								long id = idField.getLong(t);
								itor.remove();
								entityManager.flush(t);
								savedNum++;
								if (DataMoveManager.logger.isInfoEnabled()) {
									DataMoveManager.logger.info(log + " [只剩下通知了底层，但未存储的] [移除已经存储的对象] [id:" + id + "]");
								}
							} else if (now > lastCheckTime + checkVersionTime) {
								if (!isCheck) {
									isCheck = true;
								}
								long id = idField.getLong(t);
								long ct = entityManager.count(clazz, idField.getName() + "="+id);
								if (ct > 0) {
									itor.remove();
									if (DataMoveManager.logger.isInfoEnabled()) {
										DataMoveManager.logger.info(log + " [已经存入数据库但是version变为0] [移除已经存储的对象] [id:" + id + "]");
									}
								}
							}
							if (version == 0 && loopCount > DataMoveManager.重新存储) {
								entityManager.flush(t);
								itor.remove();
							}
						} catch (Exception e) {
							DataMoveManager.logger.error("DataUnit出错:", e);
						}
					}
					if (loopCount > DataMoveManager.重新存储) {
						loopCount = 0;
					}
					if (isCheck) {
						lastCheckTime = now;
						isCheck = false;
					}
				}
			} catch (Exception e) {
				DataMoveManager.logger.error("[异常3]", e);
			}
		}
		DataMoveManager.instance.dataMoveMaps.put(clazz, counts);
		long cost = System.currentTimeMillis() - startTime;
		counts[2] = cost;
		running = false;
		ids = null;
//		entityManager.notifyAll();
 		DataMoveManager.logger.warn("***********************************************************************");
		DataMoveManager.logger.warn("**** [" + clazz.getSimpleName() + "] [checkPlayer:"+checkPlayer+"] [执行结束] [耗时:"+counts[2]+"毫秒] ***");
		DataMoveManager.logger.warn("[" + clazz.getSimpleName() + "] [总数:"+counts[0]+"] [转移数据:"+counts[1]+"]");
		DataMoveManager.logger.warn("***********************************************************************");
	}

	public Queue<T> getInput() {
		return input;
	}

	public void setInput(Queue<T> input) {
		this.input = input;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Field getVersionField() {
		return versionField;
	}

	public void setVersionField(Field versionField) {
		this.versionField = versionField;
	}

	public Field getIdField() {
		return idField;
	}

	public void setIdField(Field idField) {
		this.idField = idField;
	}

	public DataCollect2<T> getDataCollect() {
		return dataCollect;
	}

	public void setDataCollect(DataCollect2<T> dataCollect) {
		this.dataCollect = dataCollect;
	}

	public long getSavedNum() {
		return savedNum;
	}

	public void setSavedNum(long savedNum) {
		this.savedNum = savedNum;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getReceiveTimes() {
		return receiveTimes;
	}

	public void setReceiveTimes(int receiveTimes) {
		this.receiveTimes = receiveTimes;
	}

	public int getStopTimes() {
		return stopTimes;
	}

	public void setStopTimes(int stopTimes) {
		this.stopTimes = stopTimes;
	}

	public long getReceiveTotalNum() {
		return receiveTotalNum;
	}

	public void setReceiveTotalNum(long receiveTotalNum) {
		this.receiveTotalNum = receiveTotalNum;
	}

	public byte getCheckType() {
		return checkType;
	}

	public void setCheckType(byte checkType) {
		this.checkType = checkType;
	}
	
	

}
