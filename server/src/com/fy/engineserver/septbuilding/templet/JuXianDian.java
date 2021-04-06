package com.fy.engineserver.septbuilding.templet;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.petHouse.HouseData;
import com.fy.engineserver.jiazu.petHouse.PetHouseManager;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sept.exception.ActionIsCDException;
import com.fy.engineserver.sept.exception.BuildingNotFoundException;
import com.fy.engineserver.sept.exception.MaxLvException;
import com.fy.engineserver.sept.exception.OtherBuildingNotLvUpException;
import com.fy.engineserver.sept.exception.ResNotEnoughException;
import com.fy.engineserver.sept.exception.WrongLvOnLvDownException;
import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity;
import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.septstation.service.SeptStationManager.ActionType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 模板实例_聚仙殿
 * 
 * 
 */
@SimpleEmbeddable
public class JuXianDian extends SeptBuildingTemplet {

	private static JuXianDian instance;
	/** 资金上限 */
//	private transient long[] maxCoin = new long[MAX_LEVEL];
	/** 基础维修费用 */
	private transient int[] baseMaintainCost = new int[MAX_LEVEL];

	private transient String confPath;
	/** 是否服务器降级，如果服务器降级不用计算cd，切需要返还百分之80的升级消耗  而且有可能会解散家族*/
	public transient volatile boolean leverDownByServer = false;
	
	public transient static final long 返还比率 = 80;

	private JuXianDian() {

	}

	public static JuXianDian getInstance() {
		return instance;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.setBuildingType(BuildingType.聚仙殿);
		instance.setName(getBuildingType().getName());
		instance.initDepend();
		instance.load();
		SeptBuildingManager.type_templet_map.put(instance.getBuildingType(), instance);
		ServiceStartRecord.startLog(this);
	}

	@Override
	public void loadeSpecially(Element root) {
		Element[] es = null;
//		es = XmlUtil.getChildrenByName(root, "maxCoin");
//		instance.setMaxCoin(StringTool.string2LongArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

		es = XmlUtil.getChildrenByName(root, "baseMaintainCost");
		instance.setBaseMaintainCost(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
	}

	@Override
	public boolean levelUp(SeptStation station, long NPCId) throws ResNotEnoughException, OtherBuildingNotLvUpException, MaxLvException {
		SeptBuildingEntity JXDEntity = station.getSeptBuild(this.getBuildingType());
		int currLevel = station.getBuildingLevel(BuildingType.聚仙殿);
		if (currLevel >= MAX_LEVEL) {
			throw new MaxLvException();
		}
		// // 得到家族的资源，判断是否足够升级
		// if (!this.resEnough(station)) {
		// throw new ResNotEnoughException();
		// }
		// 其他基础建筑的等级是否达到了升级支持
		
		boolean otherSupport = true;
		StringBuffer sbf = new StringBuffer();// BuildingType type :
		for (Iterator<Long> it = station.getBuildings().keySet().iterator(); it.hasNext();) {
			BuildingType type = station.getBuildings().get(it.next()).getBuildingState().getTempletType().getBuildingType();
			if (type.isBase()) {// 主建筑就是当前建筑,且只有一个 这里就不做特殊判断了
				if (station.getBuildingLevel(type) < currLevel) {
					otherSupport = false;
					sbf.append(type.getName()).append(".");// OR break;
				}
			}
		}
		if (!otherSupport) {
			throw new OtherBuildingNotLvUpException();
		}
		// 得到家族的资源，判断是否足够升级
		if (!this.resEnough(station)) {
			throw new ResNotEnoughException();
		}
		JXDEntity.setInBuild(true);
		
		
		JXDEntity.releaseTask(ActionType.VLUP, station.getGame());
		station.initInfo();
		return true;
	}

	@Override
	public boolean levelDown(SeptStation station, long NPCId, boolean confirm, Player p) throws WrongLvOnLvDownException, BuildingNotFoundException, ActionIsCDException {
		SeptBuildingEntity JXDEntity = station.getSeptBuild(BuildingType.聚仙殿);
		if (JXDEntity == null) {
			throw new BuildingNotFoundException();
		}
		long timeDistance = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - station.getLastDestoryTime();
		if (!leverDownByServer && timeDistance < SeptStationManager.lvDownCD) {		//2014年9月29日  服务器降级不计算cd
			throw new ActionIsCDException(SeptStationManager.timeDistanceLong2String(SeptStationManager.lvDownCD - timeDistance));
		}
		if (!confirm) {
			JiazuManager2.popConfirmLevelDown(p, station, NPCId, this, this.getName());
			return false;
		}
		int currLevel = station.getBuildingLevel(BuildingType.聚仙殿);
		if (currLevel <= 1) {			//2014年9月29日  如果等级降到0自动解散家族
			if (leverDownByServer) {		//解散家族
				if (p.getCountryPosition() == CountryManager.国王) {
					return false;
				}
				Jiazu jiazu = station.getJiazu();
				if (jiazu.getLevel() > 1) {
					JiazuManager.logger.warn("[维护] [异常] [家族等级超过1服务器却要解散] [" + jiazu.getLogString() + "] [jiazuMoney:" + jiazu.getJiazuMoney() + "] [level:" + jiazu.getLevel() + "] [" + p.getLogString() + "]");
					return false;
				}
				List<HouseData> jiazuDatas = PetHouseManager.getInstance().getJiazuDatas(jiazu.getJiazuID());
				if(jiazuDatas != null && jiazuDatas.size() > 0){
					JiazuManager.logger.warn("[维护] [家族中有宠物正在挂机不能解散] [解散失败] [" + jiazu.getLogString() + "] [jiazuMoney:" + jiazu.getJiazuMoney() + "] [level:" + jiazu.getLevel() + "] [" + p.getLogString() + "]");
					return false;
				}
				JiazuManager.logger.warn("[维护] [家族等级小于1] [解散] [" + jiazu.getLogString() + "] [jiazuMoney:" + jiazu.getJiazuMoney() + "] [level:" + jiazu.getLevel() + "] [" + p.getLogString() + "]");
				try {
					JiazuManager.jiazuEm.remove(jiazu);			//暂时先不做删除操作
				} catch (Exception e1) {
					TaskSubSystem.logger.info(p.getLogString() + "[服务器解散家族] [存库异常] [" + station.getJiazu().getName() + "] [" + p.getLogString() + "]", e1);
				}
				JiazuManager jiazuManager = JiazuManager.getInstance();
				Set<JiazuMember> set = jiazuManager.getJiazuMember(jiazu.getJiazuID());
				ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(jiazu.getZongPaiId());
				if (zp != null) {
					try {
						zp.getJiazuIds().remove(jiazu.getJiazuID());
						try {
							if (zp.getMasterId() == p.getId() && zp.getJiazuIds().size() > 0) {
								for (int kk=0; kk<zp.getJiazuIds().size(); kk++) {
									Jiazu temp = jiazuManager.getJiazu(zp.getJiazuIds().get(kk));
									if (temp != null) {
										zp.setMasterId(temp.getJiazuMaster());
										JiazuSubSystem.logger.warn("[服务器解散家族] [退让宗主] [新宗主id:" + zp.getMasterId() + "]");
										break;
									}
								}
							}
						} catch (Exception e) {
							JiazuSubSystem.logger.error("[服务器解散家族] [退让宗主] [异常] [" + jiazu.getLogString() + "]", e);
						}
						ZongPaiManager.em.notifyFieldChange(zp, "jiazuIds");
					} catch (Exception e) {
						JiazuSubSystem.logger.error("[服务器解散家族] [宗派获取异常] [异常]", e);
					}
				}
				PlayerManager playerManager = PlayerManager.getInstance();
				for (JiazuMember mem : set) {
					Player mplayer = null;
					try {
						mplayer = playerManager.getPlayer(mem.getPlayerID());
						mem.setCurrentWeekContribution(0);			//解散家族清除成员本周家族贡献
						if (mplayer != null) {
							if (mplayer.getJiazuId() != jiazu.getJiazuID()) {
								continue;
							}
							mplayer.setJiazuName(null);
							mplayer.setJiazuId(0);
							mplayer.setLeaveJiazuTime(System.currentTimeMillis());
							mplayer.initJiazuTitleAndIcon();
							mplayer.setZongPaiName("");
							mplayer.initZongPaiName();
							if (PlayerManager.getInstance().isOnline(mplayer.getId())) {
								SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
								mplayer.sendError(Translate.text_jiazu_017);
								if (septStation != null && septStation.getGame().contains(mplayer)) {// 在家族驻地内
									septStation.setUsed(false);
									septStation.getGame().transferGame(mplayer, new TransportData(0, 0, 0, 0, mplayer.getHomeMapName(), mplayer.getHomeX(), mplayer.getHomeY()));
								} 
							} else {// 不在线的发邮件
								MailManager.getInstance().sendMail(mplayer.getId(), null, Translate.text_6380, Translate.text_6379, 0, 0, 0, "");
								
								mplayer.setX(mplayer.getResurrectionX());
								mplayer.setY(mplayer.getResurrectionY());
								mplayer.setGame(mplayer.getResurrectionMapName());
							}
							if (JiazuSubSystem.logger.isWarnEnabled()) {
								JiazuSubSystem.logger.warn("[家族解散] [通知家族成员] [" + jiazu.getLogString() + "] [" + mplayer.getLogString() + "]");
							}
						}
						JiazuManager.memberEm.remove(mem);
					} catch (Exception e) {
						JiazuSubSystem.logger.error("[服务器解散家族] [异常]", e);
					}
				}
			} else {
				throw new WrongLvOnLvDownException();
			}
		}
		long backMoney = 0;					//服务器降级返还升级费用
		long backLingmai = 0;
		for (Iterator<Long> iterator = station.getBuildings().keySet().iterator(); iterator.hasNext();) {
			BuildingType type = station.getSeptBuildingByNPCId(iterator.next()).getBuildingState().getTempletType().getBuildingType();
			SeptBuildingEntity entity = station.getSeptBuild(type);
			if (entity == null) {
				continue;
			}
			if (entity.getBuildingState().getLevel() >= currLevel) {
				entity.getBuildingState().setLevel(currLevel - 1);
				entity.modifyNPCOutShow();
				backMoney += entity.getBuildingState().getTempletType().getLvUpCostCoin()[currLevel-1];
				backLingmai += entity.getBuildingState().getTempletType().getLvUpCostSpirit()[currLevel-1];
				JiazuSubSystem.logger.warn("[家族修改] [服务器强制降级家族主建筑] [返还资金:" + entity.getBuildingState().getTempletType().getLvUpCostCoin()[currLevel-1] + "] [返还灵脉:" + entity.getBuildingState().getTempletType().getLvUpCostSpirit()[currLevel-1] + "] [" + entity.getNpc().getName() + "] [currLevel:" + currLevel + "] [eLevel:" + entity.getBuildingState().getLevel() + "]");
			}
		}
		if (currLevel > 1) {
			JXDEntity.getBuildingState().setLevel(currLevel - 1);
			JXDEntity.modifyNPCOutShow();
		}
//		JXDEntity.getBuildingState().setLevel(currLevel - 1);
//		backMoney += JXDEntity.getBuildingState().getTempletType().getLvUpCostCoin()[currLevel-1];
//		backLingmai += JXDEntity.getBuildingState().getTempletType().getLvUpCostSpirit()[currLevel-1];
//		JXDEntity.modifyNPCOutShow();
//		JXDEntity.modifyGroudNpc();
//		station.getJiazu().setLevel(JXDEntity.getBuildingState().getLevel());
//		JiazuSubSystem.logger.warn("[家族修改] [服务器强制降级家族主建筑] [返还资金:" + JXDEntity.getBuildingState().getTempletType().getLvUpCostCoin()[currLevel-1] + "] [返还灵脉:" + JXDEntity.getBuildingState().getTempletType().getLvUpCostSpirit()[currLevel-1] + "] [" + JXDEntity.getNpc().getName() + "] [currLevel:" + currLevel + "] [eLevel:" + JXDEntity.getBuildingState().getLevel() + "]");
		if (!leverDownByServer) {
			station.setLastDestoryTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		} else {		//返还家族资金和灵脉值
			backMoney = (long) (backMoney * SeptStationManager.backRate);
			backLingmai = (long) (backLingmai * SeptStationManager.backRate);
			try {
				station.notifyFieldChange("buildingEntities");
				Jiazu jiazu = station.getJiazu();
				jiazu.addMoneyAndConsumeWithoutLimit(backMoney, backLingmai);
				JiazuSubSystem.logger.warn("[家族修改] [服务器强制降级家族主建筑] [返还家族资金:" + backMoney + "] [返回后家族资金:" + jiazu.getJiazuMoney() + "] [返还家族灵脉值:"
						+ backLingmai + "] [返还后家族灵脉值:" + jiazu.getConstructionConsume() + "] [" + jiazu.getLogString() + "]");
			}catch (Exception e) {
				JiazuSubSystem.logger.warn("[家族修改] [服务器强制降级家族主建筑] [返还家族资金:" + backMoney + "] [返还家族灵脉值:" + backLingmai + "] [" + station.getJiazu() == null ? ("驻地:"+station.getId()) : station.getJiazu().getJiazuID() + "]", e);
			}
			leverDownByServer = false;
		}
		try {
			station.getJiazu().setLevel(JXDEntity.getBuildingState().getLevel());
		} catch (Exception e) {
			JiazuSubSystem.logger.error("[家族修改] [聚仙殿降级] [降低家族等级] [异常] [" + p.getLogString() + "] ", e);
		}
		station.initInfo();
		return true;
	}

	/**
	 * 主建筑,需要其他的建筑都达到当前等级才能升级
	 */
	@Override
	public boolean canLvUp(SeptStation station) {
		SeptBuildingEntity buildingEntity = station.getSeptBuild(this.getBuildingType());
		int level = buildingEntity.getBuildingState().getLevel();
		// TODO 资源是不是够
		for (Iterator<Long> iterator = station.getBuildings().keySet().iterator(); iterator.hasNext();) {
			BuildingType type = station.getSeptBuildingByNPCId(iterator.next()).getBuildingState().getTempletType().getBuildingType();
			if (!type.isBase()) {
				continue;
			}
			if (station.getSeptBuild(type).getBuildingState().getLevel() < level) {
				// TODO 发消息给客户端，说明有建筑的等级不足
				return false;
			}

		}
		return true;
	}

	public int[] getBaseMaintainCost() {
		return baseMaintainCost;
	}

	public void setBaseMaintainCost(int[] baseMaintainCost) {
		this.baseMaintainCost = baseMaintainCost;
	}

//	public long[] getMaxCoin() {
//		return maxCoin;
//	}
//
//	public void setMaxCoin(long[] maxCoin) {
//		this.maxCoin = maxCoin;
//	}

	@Override
	public void initDepend() {
		// TODO Auto-generated method stub

	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer(super.toString());
//		sbf.append("\t\t<font color=green>资金上限[").append(StringTool.longArr2String(getMaxCoin(), ",")).append("]</font><BR/>");
		sbf.append("\t\t<font color=green>基础维修费用[").append(StringTool.intArr2String(getBaseMaintainCost(), ",")).append("]</font><BR/>");
		return sbf.toString();
	}
}
