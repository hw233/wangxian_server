package com.fy.engineserver.jiazu2.manager;

import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.BiaoCheEntity;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.jiazu2.instance.JiazuXiulian;
import com.fy.engineserver.jiazu2.model.BiaoCheQianghuaModel;
import com.fy.engineserver.jiazu2.model.JiazuRenwuModel;
import com.fy.engineserver.jiazu2.model.JiazuSkillModel;
import com.fy.engineserver.jiazu2.model.JiazuTaskModel4Client;
import com.fy.engineserver.jiazu2.model.PracticeModel;
import com.fy.engineserver.jiazu2.model.QifuModel;
import com.fy.engineserver.message.COMMON_RELEVANT_DES_REQ;
import com.fy.engineserver.message.COMMON_RELEVANT_DES_RES;
import com.fy.engineserver.message.GET_JIAZU_SKILL_INFO_REQ;
import com.fy.engineserver.message.GET_JIAZU_SKILL_INFO_RES;
import com.fy.engineserver.message.JIAZU_BIAOCHE_QIANGHUA_INFO_REQ;
import com.fy.engineserver.message.JIAZU_BIAOCHE_QIANGHUA_INFO_RES;
import com.fy.engineserver.message.JIAZU_BIAOCHE_QIANGHUA_REQ;
import com.fy.engineserver.message.JIAZU_BIAOCHE_QIANGHUA_RES;
import com.fy.engineserver.message.JIAZU_FENGYING_STATUS_REQ;
import com.fy.engineserver.message.JIAZU_FENGYING_STATUS__RES;
import com.fy.engineserver.message.JIAZU_JINENG_INFO_REQ;
import com.fy.engineserver.message.JIAZU_JINENG_INFO_RES;
import com.fy.engineserver.message.JIAZU_JINENG_REQ;
import com.fy.engineserver.message.JIAZU_JINENG_RES;
import com.fy.engineserver.message.JIAZU_RELEVANT_DES_REQ;
import com.fy.engineserver.message.JIAZU_RELEVANT_DES_RES;
import com.fy.engineserver.message.JIAZU_TASK_JIEQU_REQ;
import com.fy.engineserver.message.JIAZU_TASK_REFRESH_REQ;
import com.fy.engineserver.message.JIAZU_TASK_REFRESH_RES;
import com.fy.engineserver.message.JIAZU_TASK_WINDOW_OPEN_REQ;
import com.fy.engineserver.message.JIAZU_TASK_WINDOW_OPEN_RES;
import com.fy.engineserver.message.JIAZU_XIULIAN_INFO_REQ;
import com.fy.engineserver.message.JIAZU_XIULIAN_INFO_RES;
import com.fy.engineserver.message.JIAZU_XIULIAN_REQ;
import com.fy.engineserver.message.JIAZU_XIULIAN_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.prizes.TaskPrize;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfExp;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.targets.TaskTarget;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class JiazuSystem extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "JiazuSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"JIAZU_BIAOCHE_QIANGHUA_INFO_REQ", "JIAZU_BIAOCHE_QIANGHUA_REQ", "JIAZU_XIULIAN_INFO_REQ"
				,"JIAZU_XIULIAN_REQ", "JIAZU_JINENG_INFO_REQ", "JIAZU_JINENG_REQ", "JIAZU_TASK_WINDOW_OPEN_REQ"
				,"JIAZU_TASK_JIEQU_REQ","JIAZU_TASK_REFRESH_REQ", "GET_JIAZU_SKILL_INFO_REQ", "JIAZU_FENGYING_STATUS_REQ"
				,"COMMON_RELEVANT_DES_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, JiazuManager2.logger);
		if(JiazuManager2.logger.isDebugEnabled()) {
			JiazuManager2.logger.debug("[收到玩家请求] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
		}
		try {
			if(message instanceof JIAZU_BIAOCHE_QIANGHUA_INFO_REQ) {			
				return handle_JIAZU_BIAOCHE_QIANGHUA_INFO_REQ(conn, message, player);
			} else if (message instanceof JIAZU_BIAOCHE_QIANGHUA_REQ) {		
				return handle_JIAZU_BIAOCHE_QIANGHUA_REQ(conn, message, player);
			} else if (message instanceof JIAZU_XIULIAN_INFO_REQ) {
				return handle_JIAZU_XIULIAN_INFO_REQ(conn, message, player);
			} else if (message instanceof JIAZU_XIULIAN_REQ) {
				return handle_JIAZU_XIULIAN_REQ(conn, message, player);
			} else if (message instanceof JIAZU_JINENG_INFO_REQ) {
				return handle_JIAZU_JINENG_INFO_REQ(conn, message, player);
			} else if (message instanceof JIAZU_JINENG_REQ) {
				return handle_JIAZU_JINENG_REQ(conn, message, player);
			} else if (message instanceof JIAZU_TASK_WINDOW_OPEN_REQ) {
				return handle_JIAZU_TASK_WINDOW_OPEN_REQ(conn, message, player);
			} else if (message instanceof JIAZU_TASK_JIEQU_REQ) {
				return handle_JIAZU_TASK_JIEQU_REQ(conn, message, player);
			} else if (message instanceof JIAZU_TASK_REFRESH_REQ) {
				return handle_JIAZU_TASK_REFRESH_REQ(conn, message, player);
			} else if (message instanceof GET_JIAZU_SKILL_INFO_REQ) {
				return handle_GET_JIAZU_SKILL_INFO_REQ(conn, message, player);
			} else if (message instanceof JIAZU_FENGYING_STATUS_REQ) {
				return handle_JIAZU_FENGYING_STATUS_REQ(conn, message, player);
			} else if (message instanceof COMMON_RELEVANT_DES_REQ) {
				return handle_JIAZU_RELEVANT_DES_REQ(conn, message, player);
			}
		} catch(Exception e) {
			JiazuManager2.logger.error("[新家族] [处理协议出错] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]", e);
		}
		return null;
	}
	/**
	 * 家族任务列表面板
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_TASK_WINDOW_OPEN_REQ(Connection conn, RequestMessage message, Player player) {
		String result = checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return null;
		}
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
		List<Long> taskIdList = JiazuManager2.instance.getPlayerJiazuTask(player);
		JiazuTaskModel4Client[] tasks = new JiazuTaskModel4Client[taskIdList.size()];
		for (int i=0; i<taskIdList.size(); i++) {
			Task task = TaskManager.getInstance().getTask(taskIdList.get(i));
			JiazuRenwuModel jrm = JiazuManager2.instance.taskMap.get(taskIdList.get(i));
			tasks[i] = new JiazuTaskModel4Client();
			tasks[i].setTaskId(taskIdList.get(i));
			tasks[i].setTaskName(task.getName());
			tasks[i].setDes(task.getDes());
			tasks[i].setStar(jrm.getStar());
			String rewardDes = JiazuManager2.instance.translate.get(2);
			long addExp = 0;
			TaskPrize[] prise = task.getPrizes();
			if (prise != null) {
				for (TaskPrize tp : prise) {
					if (tp instanceof TaskPrizeOfExp) {
						addExp = ((TaskPrizeOfExp)tp).getExp(player);
						break;
					}
				}
			}
			tasks[i].setRewardDes(String.format(rewardDes, jrm.getAddXiulian() + "", jrm.getAddJiazuZiyuan()/1000 + "", addExp + ""));
			TaskTarget[] tg = task.getTargets();
			String des4Tar = "";
			int targetLevel = -1;
			if (tg != null && tg.length > 0) {
				des4Tar = tg[0].gettarDes();
				targetLevel = tg[0].gettarLevel();
				if (JiazuManager2.logger.isDebugEnabled()) {
					JiazuManager2.logger.debug("[家族测试] [返回客户端的描述] [" + des4Tar + "] [" + player.getLogString() + "]");
				}
			}
			tasks[i].setTargetDes(des4Tar);
			tasks[i].setTargetLevel(targetLevel);
		}
		int[] tempCom = JiazuManager2.instance.getAlreadyCompTaskNumAndMaxNum(player);
		String str = String.format(JiazuManager2.instance.translate.get(1), BillingCenter.得到带单位的银两(JiazuManager2.instance.base.getCost4RefreshTask()));
		JIAZU_TASK_WINDOW_OPEN_RES resp = new JIAZU_TASK_WINDOW_OPEN_RES(message.getSequnceNum(), jm.getJiazuSalary(), str, tempCom[0]-tempCom[1], tempCom[0], tasks);
		return resp;
	}
	/**
	 * 接任务弹窗二次确认
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_TASK_JIEQU_REQ(Connection conn, RequestMessage message, Player player) {
		JIAZU_TASK_JIEQU_REQ req = (JIAZU_TASK_JIEQU_REQ) message;
		String result = checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return null;
		}
		JiazuManager2.instance.takeOneTaksConfirm(player, req.getTaskId());
		return null;
//		byte res = 0;
//		JIAZU_TASK_JIEQU_RES resp = new JIAZU_TASK_JIEQU_RES(message.getSequnceNum(), res);
//		return resp;
	}
	
	public ResponseMessage handle_JIAZU_RELEVANT_DES_REQ(Connection conn, RequestMessage message, Player player) {
		COMMON_RELEVANT_DES_REQ req = (COMMON_RELEVANT_DES_REQ) message;
		String des = "";
		if (req.getWindowId() == JiazuManager2.家族面板描述) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			SeptStation ss = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
			if (jiazu != null && ss != null) {
				des = JiazuManager2.instance.releventDes.get(req.getTargetName());
				if (Translate.家族资金.equals(req.getTargetName())) {
					des = String.format(des, BillingCenter.得到带单位的银两(ss.getInfo().getMaxCoin()));
				} else if (Translate.灵脉值.equals(req.getTargetName())) {
					des = String.format(des, BillingCenter.得到带单位的银两(ss.getInfo().getMaxSprint()));	
				} else if (Translate.家族人数.equals(req.getTargetName())) {
					int maxNum = jiazu.maxMember();
					des = String.format(des, maxNum+"");	
				}
			} else if (jiazu != null) {
				des = JiazuManager2.instance.releventDes.get(req.getTargetName());
				if (Translate.家族资金.equals(req.getTargetName())) {
					des = String.format(des, 0+Translate.文);
				} else if (Translate.灵脉值.equals(req.getTargetName())) {
					des = String.format(des,  0+Translate.文);	
				} else if (Translate.家族人数.equals(req.getTargetName())) {
					int maxNum = jiazu.maxMember();
					des = String.format(des, maxNum+"");	
				}
			}
		}
		if (JiazuManager2.logger.isDebugEnabled()) {
			JiazuManager2.logger.debug("[handle_JIAZU_RELEVANT_DES_REQ] [req.getId:" + req.getWindowId() + "] [req.getname:" + req.getTargetName() + "] [resp.des:" + des + "]");
		}
		COMMON_RELEVANT_DES_RES resp = new COMMON_RELEVANT_DES_RES(message.getSequnceNum(),req.getWindowId(), req.getWindowName(), req.getBtnName(), req.getTargetName(), des);
		return resp;
	}
	public ResponseMessage handle_JIAZU_FENGYING_STATUS_REQ(Connection conn, RequestMessage message, Player player) {
		String result = this.checkJiazu(player);
		if (result == null) {
//			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			String des = "";
//			if (jiazu.getJiazuStatus() == JiazuManager2.封印家族功能) {
				des = Translate.家族封印提示;
//			}
			JIAZU_FENGYING_STATUS__RES resp = new JIAZU_FENGYING_STATUS__RES(message.getSequnceNum(), /*jiazu.getJiazuStatus()*/(byte)-1, des);
			return resp;
		}
		return null;
	}
	public ResponseMessage handle_GET_JIAZU_SKILL_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		GET_JIAZU_SKILL_INFO_REQ req = (GET_JIAZU_SKILL_INFO_REQ) message;
		String description = "";
		PracticeModel pm = JiazuManager2.instance.praticeMaps.get(req.getSkillId());
		JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
		JiazuXiulian jxl = jm2.getJiazuXiulianBySkillId(req.getSkillId());
		int skLevel = 0;
		if (jxl != null) {
			skLevel = jxl.getSkillLevel();
		}
		if (pm != null ) {
			description = pm.getNextLevelInfoShow(skLevel);
		}
		GET_JIAZU_SKILL_INFO_RES resp = new GET_JIAZU_SKILL_INFO_RES(message.getSequnceNum(), req.getSkillId(), description);
		return resp;
	}
	/**
	 * 刷新家族任务列表
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_TASK_REFRESH_REQ(Connection conn, RequestMessage message, Player player) {
		String result = checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return null;
		}
		boolean r = JiazuManager2.instance.refreshTaskList(player, false, false);
		JIAZU_TASK_REFRESH_RES resp = new JIAZU_TASK_REFRESH_RES(message.getSequnceNum(), (byte) (r?1:0));
		return resp;
	}
	
	public String checkJiazu(Player player) {
		if(player.getJiazuId() <= 0) {
			return Translate.您还没有家族;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return Translate.您还没有家族;
		}
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (jm == null) {
			return Translate.您还没有家族;
		}
		return null;
	}
	/**
	 * 上香信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_XIULIAN_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		if(player.getJiazuId() <= 0) {
			player.sendError(Translate.您还没有家族);
			return null;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.您还没有家族);
			return null;
		}
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (jm == null) {
			player.sendError(Translate.您还没有家族);
			return null;
		}
		JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
		jm2.reset();
		long gongzi = player.getWage();
		long xiulianZhi = jm2.getPracticeValue();
		int maxShangxiangNums = JiazuManager2.instance.qifuTimes.get(player.getVipLevel());
		int currentShangxiang = jm2.getQifuTimes();
		int[] shangxiangId = new int[JiazuManager2.instance.qifuMaps.size()];
		int[] costType = new int[JiazuManager2.instance.qifuMaps.size()];
		int[] rewardXiulian = new int[JiazuManager2.instance.qifuMaps.size()];
		long[] costNums = new long[JiazuManager2.instance.qifuMaps.size()];
		String[] description = new String[shangxiangId.length]; 
		
		Iterator<Integer> ite = JiazuManager2.instance.qifuMaps.keySet().iterator();
		int index = 0;
		while(ite.hasNext()) {
			int key = ite.next();
			QifuModel qm = JiazuManager2.instance.qifuMaps.get(key);
			shangxiangId[index] = key;
			costType[index] = qm.getCostType();
			rewardXiulian[index] = qm.getAddNum();
			costNums[index] = qm.getCostNum();
			String dess = BillingCenter.得到带单位的银两(qm.getAddJiazuZiyuan());
			description[index] = String.format(Translate.增加修炼值, qm.getAddNum() + "", dess);
			index++;
		}
		
		JIAZU_XIULIAN_INFO_RES resp = new JIAZU_XIULIAN_INFO_RES(message.getSequnceNum(), gongzi, xiulianZhi, 
				maxShangxiangNums, currentShangxiang, shangxiangId, description, costType, 
				costNums, rewardXiulian);
		return resp;
	}
	/**
	 * 上香
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_XIULIAN_REQ(Connection conn, RequestMessage message, Player player) {
		if(player.getJiazuId() <= 0) {
			player.sendError(Translate.您还没有家族);
			return null;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.您还没有家族);
			return null;
		}
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
		if (jm == null) {
			player.sendError(Translate.您还没有家族);
			return null;
		}
		JIAZU_XIULIAN_REQ req = (JIAZU_XIULIAN_REQ) message;
		JiazuEntityManager2.instance.qifu(player, req.getShangxiangId(), false);
		JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
		long gongzi = player.getWage();
		long xiulianZhi = jm2.getPracticeValue();
		int maxShangxiangNums = JiazuManager2.instance.qifuTimes.get(player.getVipLevel());
		int currentShangxiang = jm2.getQifuTimes();
		JIAZU_XIULIAN_RES resp = new JIAZU_XIULIAN_RES(message.getSequnceNum(), req.getShangxiangId(), gongzi, xiulianZhi, 
				maxShangxiangNums, currentShangxiang);
		return resp;
	}
	/**
	 * 家族技能信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_JINENG_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		String result = checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return null;
		}
		JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
		long xiulianZhi = jm2.getPracticeValue();
		byte buildType = 0;		//先乱写了。。以后可能改成挂npc窗口
		JiazuSkillModel[] jiazuSkills = new JiazuSkillModel[JiazuManager2.instance.praticeMaps.size()];
		Iterator<Integer> ite = JiazuManager2.instance.praticeMaps.keySet().iterator();
		int index = 0;
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		SeptStation station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		while(ite.hasNext()) {
			int skillId = ite.next();
			JiazuXiulian xiul = jm2.getJiazuXiulianBySkillId(skillId);
			int currentLevel = 0;
			int currentExp = 0;
			if (xiul != null) {
				currentLevel = xiul.getSkillLevel();
				currentExp = (int) xiul.getSkillExp();
			}
			PracticeModel pm = JiazuManager2.instance.praticeMaps.get(skillId);
			JiazuXiulian jxl = jm2.getJiazuXiulianBySkillId(pm.getSkillId());
			String description = "";
			int skLevel = 0;
			if (jxl != null) {
				skLevel = jxl.getSkillLevel();
			}
			if (pm != null ) {
				description = pm.getInfoShow(skLevel);
			}
			JiazuSkillModel jsm = new JiazuSkillModel();
			jsm.setSkillId(pm.getSkillId());
			jsm.setCurrentLevelDes(description);
			jsm.setSkillName(pm.getSkillName());
			jsm.setIcon(pm.getIcon());
			int weaponLevel = station.getBuildingLevel(BuildingType.武器坊);
			int armorLevel = station.getBuildingLevel(BuildingType.防具坊);
			PracticeModel model = JiazuManager2.instance.praticeMaps.get(skillId);
			int canLearnLevel = model.getCanLearnLevel(weaponLevel, armorLevel);
			jsm.setMaxLevel(canLearnLevel);
			jsm.setCurrentLevel(currentLevel);
			jsm.setCurrentExp(currentExp);
			jsm.setNeedExp((int) pm.getExp4next()[currentLevel]);
			jsm.setCostType(pm.getCostSiliverType()[currentLevel]);
			jsm.setCostNum(JiazuManager2.instance.base.getCostSiliverNums());
			jiazuSkills[index++] = jsm;
		}
		JIAZU_JINENG_INFO_RES resp = new JIAZU_JINENG_INFO_RES(message.getSequnceNum(), xiulianZhi, buildType, jiazuSkills, JiazuManager2.instance.base.getCostXiulian());
		return resp;
	}
	/**
	 * 学习家族技能
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_JINENG_REQ(Connection conn, RequestMessage message, Player player) {
		JIAZU_JINENG_REQ req = (JIAZU_JINENG_REQ) message;
		String result = checkJiazu(player);
		if (result != null) {
			player.sendError(result);
			return null;
		}
		JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
		String result1 = JiazuEntityManager2.instance.upgradePratice(player, req.getSkillId(), req.getLearnType(), false);
		if (result1 != null) {
			return null;
		}
		JiazuSkillModel jiazuSkills = new JiazuSkillModel();
		JiazuXiulian xiul = jm2.getJiazuXiulianBySkillId(req.getSkillId());
		int currentLevel = 0;
		int currentExp = 0;
		if (xiul != null) {
			currentLevel = xiul.getSkillLevel();
			currentExp = (int) xiul.getSkillExp();
		}
		PracticeModel pm = JiazuManager2.instance.praticeMaps.get(req.getSkillId());
		JiazuXiulian jxl = jm2.getJiazuXiulianBySkillId(pm.getSkillId());
		String description = "";
		int skLevel = 0;
		if (jxl != null) {
			skLevel = jxl.getSkillLevel();
		}
		if (pm != null ) {
			description = pm.getInfoShow(skLevel);
		}
		jiazuSkills.setCurrentLevelDes(description);
		jiazuSkills.setSkillId(pm.getSkillId());
		jiazuSkills.setSkillName(pm.getSkillName());
		jiazuSkills.setIcon(pm.getIcon());
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		SeptStation station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
		if (xiul != null) {
			currentLevel = xiul.getSkillLevel();
			currentExp = (int) xiul.getSkillExp();
		}
		int weaponLevel = station.getBuildingLevel(BuildingType.武器坊);
		int armorLevel = station.getBuildingLevel(BuildingType.防具坊);
		int canLearnLevel = pm.getCanLearnLevel(weaponLevel, armorLevel);
		jiazuSkills.setMaxLevel(canLearnLevel);
		jiazuSkills.setCurrentLevel(currentLevel);
		jiazuSkills.setCurrentExp(currentExp);
		if (currentLevel >= pm.getExp4next().length) {
			currentLevel = pm.getExp4next().length - 1;
			jiazuSkills.setCurrentExp((int) pm.getExp4next()[currentLevel]);
		} 
		jiazuSkills.setNeedExp((int) pm.getExp4next()[currentLevel]);
		jiazuSkills.setCostType(pm.getCostSiliverType()[currentLevel]);
		jiazuSkills.setCostNum(JiazuManager2.instance.base.getCostSiliverNums());
		JIAZU_JINENG_RES resp = new JIAZU_JINENG_RES(message.getSequnceNum(), jm2.getPracticeValue(), jiazuSkills);
		return resp;
	}
	
	/**
	 * 家族镖车信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_BIAOCHE_QIANGHUA_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.getJiazuId() <= 0) {
			player.sendError(Translate.您还没有家族);
			return null;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.您还没有家族);
			return null;
		}
		int[] maxLevels = new int[]{BiaocheEntityManager.镖车强化最大等级, BiaocheEntityManager.镖车强化最大等级};
		int[] currentLevels = new int[2];
		int[] maxProcess = new int[]{BiaocheEntityManager.强化最大进度, BiaocheEntityManager.强化最大进度};
		int[] process = new int[2];
		long[] costNums = new long[2];
		int[] needJiazuLevels = new int[2];
		String[] des1 = new String[]{"", ""};
		String[] des2 = new String[]{"", ""};
		BiaoCheQianghuaModel bqm = null;
		BiaoCheQianghuaModel nbqm = null;
		BiaoCheEntity entity = BiaocheEntityManager.instance.getEntity(jiazu.getJiazuID());
		for (int i=0; i<entity.getStrongerType().length; i++) {
			if (entity.getStrongerType()[i] == BiaocheEntityManager.强化血量) {
				currentLevels[0] = entity.getStrongerLevel()[i];
				process[0] = entity.getProcess()[i];
				if (currentLevels[0] >= BiaocheEntityManager.镖车强化最大等级) {
					process[0] = maxProcess[0];
				}
				bqm = JiazuManager2.instance.biaocheMap.get(BiaocheEntityManager.强化血量).get(currentLevels[0]);
				int addNums = bqm == null? 0 : bqm.getAddNum();
				des1[0] = String.format(Translate.镖车血量描述, addNums + "%");
				if (currentLevels[0] < BiaocheEntityManager.镖车强化最大等级) {
					nbqm = JiazuManager2.instance.biaocheMap.get(BiaocheEntityManager.强化血量).get(currentLevels[0] + 1);
					des2[0] = String.format(Translate.镖车血量描述, nbqm.getAddNum() + "%");
					needJiazuLevels[0] = nbqm.getNeedJiazuLevel();
					if (nbqm.getCostJiazuMoney() > 0) {
						costNums[0] = nbqm.getCostJiazuMoney() / BiaocheEntityManager.强化最大进度;
					} else {
						costNums[0] = nbqm.getCostLingmai() / BiaocheEntityManager.强化最大进度;
					}
				}
			} else if (entity.getStrongerType()[i] == BiaocheEntityManager.强化双防) {
				currentLevels[1] = entity.getStrongerLevel()[i];
				process[1] = entity.getProcess()[i];
				if (currentLevels[1] >= BiaocheEntityManager.镖车强化最大等级) {
					process[1] = maxProcess[1];
				}
				bqm = JiazuManager2.instance.biaocheMap.get(BiaocheEntityManager.强化双防).get(currentLevels[1]);
				int addNums = bqm == null? 0 : bqm.getAddNum();
				des1[1] = String.format(Translate.镖车双防描述, addNums/BiaocheEntityManager.infoShowNumRate + "%");
				if (currentLevels[1] < BiaocheEntityManager.镖车强化最大等级) {
					nbqm = JiazuManager2.instance.biaocheMap.get(BiaocheEntityManager.强化双防).get(currentLevels[1] + 1);
					des2[1] = String.format(Translate.镖车双防描述, nbqm.getAddNum()/BiaocheEntityManager.infoShowNumRate + "%");
					needJiazuLevels[1] = nbqm.getNeedJiazuLevel();
					if (nbqm.getCostJiazuMoney() > 0) {
						costNums[1] = nbqm.getCostJiazuMoney() / BiaocheEntityManager.强化最大进度;
					} else {
						costNums[1] = nbqm.getCostLingmai() / BiaocheEntityManager.强化最大进度;
					}
				}
			}
		}
		JIAZU_BIAOCHE_QIANGHUA_INFO_RES resp = new JIAZU_BIAOCHE_QIANGHUA_INFO_RES(message.getSequnceNum(), jiazu.getLevel(), jiazu.getJiazuMoney(), jiazu.getConstructionConsume(),
				maxLevels, currentLevels, maxProcess, process, 
				costNums, des1, des2, needJiazuLevels);
		return resp;
	}
	
	/**
	 * 家族镖车强化
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_JIAZU_BIAOCHE_QIANGHUA_REQ(Connection conn, RequestMessage message, Player player) {
		JIAZU_BIAOCHE_QIANGHUA_REQ req = (JIAZU_BIAOCHE_QIANGHUA_REQ) message;
		byte result = BiaocheEntityManager.instance.upgragdeBiaoche(player, req.getQianghuaType());
		if (result > 0) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			BiaoCheEntity be = BiaocheEntityManager.instance.getEntity(jiazu.getJiazuID());
			int currentLevels = 0;
			int process = 0;
			for (int i=0; i<be.getStrongerType().length; i++) {
				if (be.getStrongerType()[i] == req.getQianghuaType()) {
					currentLevels = be.getStrongerLevel()[i];
					process = be.getProcess()[i];
					if (currentLevels >= BiaocheEntityManager.镖车强化最大等级) {
						process = BiaocheEntityManager.强化最大进度;
					}
					break;
				}
			}
			BiaoCheQianghuaModel cbqm = JiazuManager2.instance.biaocheMap.get(req.getQianghuaType()).get(currentLevels);
			long costNums = 0;
			String des = req.getQianghuaType() == BiaocheEntityManager.强化血量 ? Translate.镖车血量描述 : Translate.镖车双防描述;
			int addNums = cbqm == null? 0 : cbqm.getAddNum();
			if ( req.getQianghuaType() == BiaocheEntityManager.强化双防) {
				addNums = addNums / BiaocheEntityManager.infoShowNumRate ;
			}
			String des1 = String.format(des, addNums + "%");
			String des2 = "";
			int needJiauZulevel = 0;
			if (currentLevels < BiaocheEntityManager.镖车强化最大等级) {
				BiaoCheQianghuaModel bqm = JiazuManager2.instance.biaocheMap.get(req.getQianghuaType()).get(currentLevels+1);
				if (bqm.getCostJiazuMoney() > 0) {
					costNums = bqm.getCostJiazuMoney() / BiaocheEntityManager.强化最大进度;
				} else if (bqm.getCostLingmai() > 0) {
					costNums = bqm.getCostLingmai() / BiaocheEntityManager.强化最大进度;
				}
				int addNums2 = bqm == null? 0 : bqm.getAddNum();
				if ( req.getQianghuaType() == BiaocheEntityManager.强化双防) {
					addNums2 = addNums2 / BiaocheEntityManager.infoShowNumRate ;
				}
				des2 = String.format(des, addNums2 + "%");
				needJiauZulevel = bqm.getNeedJiazuLevel();
			}
			byte isLevelUp = (byte) (result == 2 ? 1: 0);
			JIAZU_BIAOCHE_QIANGHUA_RES resp = new JIAZU_BIAOCHE_QIANGHUA_RES(message.getSequnceNum(), req.getQianghuaType(), isLevelUp, 
					jiazu.getJiazuMoney(), jiazu.getConstructionConsume(), BiaocheEntityManager.镖车强化最大等级, currentLevels+1, BiaocheEntityManager.强化最大进度, 
					process, costNums, des1, des2, needJiauZulevel);
			if (JiazuSubSystem.logger.isDebugEnabled()) {
				JiazuSubSystem.logger.debug("[handle_JIAZU_BIAOCHE_QIANGHUA_REQ] [des1:" + des1 + "] [des2:" + des2 + "] [" + player.getLogString() + "]");
			}
			return resp;
		}
		return null;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
