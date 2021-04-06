package com.fy.engineserver.articleProtect;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

public class PlayerArticleProtectData implements ArticleProtectDataValues {
	
	public static SimpleEntityManager<ArticleProtectData> em;

	private long playerID;
	
	private List<ArticleProtectData> datas = new ArrayList<ArticleProtectData>();
	
	private long lastTime;
	
	public ArticleProtectData getProtectData(ArticleEntity entity) {
		ArticleProtectData[] datas = this.datas.toArray(new ArticleProtectData[0]);
		boolean isPet = false;
		if (entity instanceof PetEggPropsEntity || entity instanceof PetPropsEntity) {
			isPet = true;
		}
		for (ArticleProtectData data : datas) {
			if (data.getArticleType() == ArticleProtect_ArticleType_Equpment && !isPet) {
				if (data.getArticleID() == entity.getId()) {
					return data;
				}
			}else if (data.getArticleType() == ArticleProtect_ArticleType_Gem && !isPet) {
				if (data.getArticleID() == entity.getId()) {
					return data;
				}
			}else if (data.getArticleType() == ArticleProtect_ArticleType_Pet && isPet) {
				if (entity instanceof PetEggPropsEntity) {
					if (data.getArticleID() == ((PetEggPropsEntity)entity).getPetId()) {
						return data;
					}
				}else if (entity instanceof PetPropsEntity) {
					if (data.getArticleID() == ((PetPropsEntity)entity).getPetId()) {
						return data;
					}
				}
			}
		}
		return null;
	}
	
	public boolean canBlock(ArticleEntity entity, boolean isHigh) {
		ArticleProtectData[] datas = this.datas.toArray(new ArticleProtectData[0]);
		boolean isPet = false;
		if (entity instanceof PetEggPropsEntity || entity instanceof PetPropsEntity) {
			isPet = true;
		}
		for (ArticleProtectData data : datas) {
			if (data.getArticleType() == ArticleProtect_ArticleType_Equpment && !isPet) {
				if (data.getArticleID() == entity.getId()) {
					if (data.getRemoveTime() <= 0) {
						if (data.getState() == ArticleProtect_State_High) {
							return false;
						}else if (isHigh) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
			}else if (data.getArticleType() == ArticleProtect_ArticleType_Gem && !isPet) {
				if (data.getArticleID() == entity.getId()) {
					if (data.getRemoveTime() <= 0) {
						if (data.getState() == ArticleProtect_State_High) {
							return false;
						}else if (isHigh) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
			}else if (data.getArticleType() == ArticleProtect_ArticleType_Pet && isPet) {
				if (entity instanceof PetEggPropsEntity) {
					if (data.getArticleID() == ((PetEggPropsEntity)entity).getPetId()) {
						if (data.getRemoveTime() <= 0) {
							if (data.getState() == ArticleProtect_State_High) {
								return false;
							}else if (isHigh) {
								return true;
							}
							return false;
						}else {
							return true;
						}
					}
				}else if (entity instanceof PetPropsEntity) {
					if (data.getArticleID() == ((PetPropsEntity)entity).getPetId()) {
						if (data.getRemoveTime() <= 0) {
							if (data.getState() == ArticleProtect_State_High) {
								return false;
							}else if (isHigh) {
								return true;
							}
							return false;
						}else {
							return true;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	public boolean canUnBlock(ArticleEntity entity) {
		ArticleProtectData[] datas = this.datas.toArray(new ArticleProtectData[0]);
		boolean isPet = false;
		if (entity instanceof PetEggPropsEntity || entity instanceof PetPropsEntity) {
			isPet = true;
		}
		for (ArticleProtectData data : datas) {
			if (data.getArticleType() == ArticleProtect_ArticleType_Equpment && !isPet) {
				if (data.getArticleID() == entity.getId()) {
					if (data.getRemoveTime() <= 0) {
						return true;
					}else {
						return false;
					}
				}
			} else if (data.getArticleType() == ArticleProtect_ArticleType_Equpment && entity instanceof NewMagicWeaponEntity) {
				if (data.getArticleID() == entity.getId()) {
					if (data.getRemoveTime() <= 0) {
						return true;
					}else {
						return false;
					}
				}
			} else if (data.getArticleType() == ArticleProtect_ArticleType_Gem && !isPet) {
				if (data.getArticleID() == entity.getId()) {
					if (data.getRemoveTime() <= 0) {
						return true;
					}else {
						return false;
					}
				}
			}else if (data.getArticleType() == ArticleProtect_ArticleType_Pet && isPet) {
				if (entity instanceof PetEggPropsEntity) {
					if (data.getArticleID() == ((PetEggPropsEntity)entity).getPetId()) {
						if (data.getRemoveTime() <= 0) {
							return true;
						}else {
							return false;
						}
					}
				}else if (entity instanceof PetPropsEntity) {
					if (data.getArticleID() == ((PetPropsEntity)entity).getPetId()) {
						if (data.getRemoveTime() <= 0) {
							return true;
						}else {
							return false;
						}
					}
				}
			}
		}
		
		return false;
	}

	public ArticleProtectClientInfo[] getInfos (Player player) {
		List<ArticleProtectClientInfo> infos = new ArrayList<ArticleProtectClientInfo>();
		ArticleProtectData[] datas = this.datas.toArray(new ArticleProtectData[0]);
		for (ArticleProtectData data : datas) {
			if (data.getState() == ArticleProtect_State_Remove || data.getState() == ArticleProtect_State_Error) {
				continue;
			}
			if (data.getArticleType() == ArticleProtect_ArticleType_Equpment) {
				ArticleProtectClientInfo info = new ArticleProtectClientInfo();
				info.setArticleType(ArticleProtectClientInfo.ARTICLETYPE_KNAP);
				info.setArticleID(data.getArticleID());
				info.setState(data.getState());
				info.setRemoveTime(data.getRemoveTime());
				infos.add(info);
			}else if (data.getArticleType() == ArticleProtect_ArticleType_Gem) {
				ArticleProtectClientInfo info = new ArticleProtectClientInfo();
				info.setArticleType(ArticleProtectClientInfo.ARTICLETYPE_KNAP);
				info.setArticleID(data.getArticleID());
				info.setState(data.getState());
				info.setRemoveTime(data.getRemoveTime());
				infos.add(info);
			}else if (data.getArticleType() == ArticleProtect_ArticleType_Pet) {
				Pet pet = PetManager.getInstance().getPet(data.getArticleID());
				if (pet != null) {
					ArticleProtectClientInfo info = new ArticleProtectClientInfo();
					info.setArticleType(ArticleProtectClientInfo.ARTICLETYPE_KNAP);
					info.setArticleID(pet.getPetPropsId());
					info.setState(data.getState());
					info.setRemoveTime(data.getRemoveTime());
					infos.add(info);
					ArticleProtectClientInfo infopet = new ArticleProtectClientInfo();
					infopet.setArticleType(ArticleProtectClientInfo.ARTICLETYPE_PET);
					infopet.setArticleID(pet.getId());
					infopet.setState(data.getState());
					infopet.setRemoveTime(data.getRemoveTime());
					infos.add(infopet);
				}
			}
		}
		
		return infos.toArray(new ArticleProtectClientInfo[0]);
	}
	
	/**
	 * 新加一个锁魂记录，
	 * @param player
	 * @param articleType
	 * @param articleID
	 * @param state
	 * @return true为成功 false为失败
	 */
	public boolean addOne(Player player, int articleType, long articleID, int state) {
		try{
			ArticleProtectData data = new ArticleProtectData();
			data.setId(em.nextId());
			data.setPlayerID(player.getId());
			data.setArticleType(articleType);
			data.setArticleID(articleID);
			data.setState(state);
			em.notifyNewObject(data);
			datas.add(data);
			return true;
		}catch(Exception e) {
			ArticleProtectManager.logger.error("新添加一个出错", e);
		}
		return false;
	}
	
	public boolean blockOne(Player player, int articleType, long articleID, int state) {
		try{
			ArticleProtectData[] datas = this.datas.toArray(new ArticleProtectData[0]);
			for (ArticleProtectData data : datas) {
				if (data.getArticleID() == articleID) {
					data.setRemoveTime(-1);
					em.notifyFieldChange(data, "removeTime");
					data.setState(state);
					em.notifyFieldChange(data, "state");
					return true;
				}
			}
			return false;
		}catch(Exception e) {
			ArticleProtectManager.logger.error("锁魂 准备解锁那个出错", e);
		}
		return false;
	}
	
	/**
	 * 解锁一个
	 * @param player
	 * @param articleType
	 * @param articleID
	 * @return
	 */
	public boolean unBlockOne(Player player, int articleType, long articleID) {
		try{
			ArticleProtectData[] datas = this.datas.toArray(new ArticleProtectData[0]);
			for (ArticleProtectData data : datas) {
				if (data.getArticleID() == articleID) {
					data.setRemoveTime(System.currentTimeMillis() + ArticleProtectManager.UNBLOCK_TIME);
					em.notifyFieldChange(data, "removeTime");
					return true;
				}
			}
			return false;
		}catch(Exception e) {
			ArticleProtectManager.logger.error("unBlockOne出错", e);
		}
		return false;
	}
	
	public boolean relUnBlock() {
		try{
			boolean remove = false;
			ArticleProtectData[] datas = this.datas.toArray(new ArticleProtectData[0]);
			for (ArticleProtectData data : datas) {
				if (data.getRemoveTime() > 0) {
					if (System.currentTimeMillis() > data.getRemoveTime()) {
						try{
							em.remove(data);
							this.datas.remove(data);
							remove = true;
							ArticleProtectManager.logger.warn("[解魂真正成功] [{}] [{}]", new Object[]{getLogString(), data.getLogString(true)});
						}catch(Exception e) {
							ArticleProtectManager.logger.error("relUnBlock remove 出错", e);
						}
					}
				}
			}
			return remove;
		}catch(Exception e) {
			ArticleProtectManager.logger.error("relUnBlock出错", e);
		}
		return false;
	}
	public ArticleProtectData relUnBlock4Info() {
		try{
//			boolean remove = false;
			ArticleProtectData[] datas = this.datas.toArray(new ArticleProtectData[0]);
			for (ArticleProtectData data : datas) {
				if (data.getRemoveTime() > 0) {
					if (System.currentTimeMillis() > data.getRemoveTime()) {
						try{
							em.remove(data);
							this.datas.remove(data);
//							remove = true;
							ArticleProtectManager.logger.warn("[解魂真正成功] [{}] [{}]", new Object[]{getLogString(), data.getLogString(true)});
							return data;
						}catch(Exception e) {
							ArticleProtectManager.logger.error("relUnBlock remove 出错", e);
						}
					}
				}
			}
		}catch(Exception e) {
			ArticleProtectManager.logger.error("relUnBlock出错", e);
		}
		return null;
	}
	
	public void saveDatas() {
		ArticleProtectData[] datas = this.datas.toArray(new ArticleProtectData[0]);
		for (ArticleProtectData data : datas) {
			try{
				em.save(data);
			}catch(Exception e) {
				ArticleProtectManager.logger.error("saveDatas save出错" + data.getLogString(true), e);
			}
		}
	}
	
	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setDatas(List<ArticleProtectData> datas) {
		this.datas = datas;
	}

	public List<ArticleProtectData> getDatas() {
		return datas;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public long getLastTime() {
		return lastTime;
	}
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(playerID).append("] ");
		for (int i = 0; i < datas.size(); i++) {
			sb.append("[").append(datas.get(i).getLogString(false)).append("] ");
		}
		sb.append("[").append(lastTime).append("] ");
		return sb.toString();
	}
	
}
