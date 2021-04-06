package com.fy.engineserver.core;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.CheckAttribute;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_NEWPLAYER_LEAD_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;

@CheckAttribute("新手引导")
public class NewPlayerLeadDataManager {

	private static NewPlayerLeadDataManager instance;
	@CheckAttribute("文件路径")
	private String filePath;

	public static Logger logger = LoggerFactory.getLogger(NewPlayerLeadDataManager.class);
	@CheckAttribute("引导数据")
	private HashMap<Long, List<NewPlayerLeadData>> leadDataMap = new HashMap<Long, List<NewPlayerLeadData>>();
	@CheckAttribute(value = "需要引导的等级", des = "<等级,引导Id>")
	private HashMap<Integer, Long> needLeadLevels = new HashMap<Integer, Long>();
	@CheckAttribute(value = "接取任务时引导", des = "<任务名,引导Id>")
	private HashMap<String, Long> acceptTaskLead = new HashMap<String, Long>();
	@CheckAttribute(value = "达成任务时引导", des = "<任务名,引导Id>")
	private HashMap<String, Long> completeTaskLead = new HashMap<String, Long>();
	@CheckAttribute(value = "交付任务时引导", des = "<任务名,引导Id>")
	private HashMap<String, Long> deliverTaskLead = new HashMap<String, Long>();

	private NewPlayerLeadDataManager() {

	}

	public static NewPlayerLeadDataManager getInstance() {
		return instance;
	}

	private void load() throws Exception{
		try {
			File file = new File(getFilePath());
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);

			int maxRow = sheet.getRows();// 事件数据

			for (int i = 1; i < maxRow; i++) {
				Cell[] cells = sheet.getRow(i);
				int index = 0;
				long id = Long.valueOf(StringTool.modifyContent(cells[index++]));
				int type = Integer.valueOf(StringTool.modifyContent(cells[index++]));
				String windowName = (StringTool.modifyContent(cells[index++]));
				String activeXName = (StringTool.modifyContent(cells[index++]));
				String showMessage = (StringTool.modifyContent(cells[index++]));
				int outStackEvent = Integer.valueOf(StringTool.modifyContent(cells[index++]));
				int priority = Integer.valueOf(StringTool.modifyContent(cells[index++]));
				int forceType = Integer.valueOf(StringTool.modifyContent(cells[index++]));
				NewPlayerLeadData data = new NewPlayerLeadData(id, type, windowName, activeXName, showMessage, outStackEvent, priority,forceType);
				if (!leadDataMap.containsKey(id)) {
					leadDataMap.put(id, new ArrayList<NewPlayerLeadData>());
				}
				leadDataMap.get(id).add(data);
				logger.warn("id:"+id);
			}

			sheet = workbook.getSheet(1);// 事件
			maxRow = sheet.getRows();
			for (int i = 1; i < maxRow; i++) {
				Cell[] cells = sheet.getRow(i);
				int index = 0;
				int type = Integer.valueOf(StringTool.modifyContent(cells[index++]));
				String value = (StringTool.modifyContent(cells[index++]));
				long id = Long.valueOf(StringTool.modifyContent(cells[index++]));
				if(logger.isInfoEnabled())
					logger.info("[行:{}/{}][type:{}][value:{}][id:{}]", new Object[] { i, maxRow, type, value, id });
				check: switch (type) {
				// 0:接任务
				// 1:任务达成
				// 2:任务完成
				// 3:等级达到
				case 0:
					acceptTaskLead.put(value, id);
					break check;
				case 1:
					completeTaskLead.put(value, id);
					break check;
				case 2:
					deliverTaskLead.put(value, id);
					break check;
				case 3:
					needLeadLevels.put(Integer.valueOf(value), id);
					break check;
				default:
					break check;
				}
			}
		} catch (Exception e) {
			logger.error("加载文件异常", e);
			throw new Exception();
		}

	}

	/**
	 * 当角色升级时调用
	 * @param player
	 */
	public void onPlayerLevelup(Player player) {
		if (needLeadLevels.containsKey(player.getLevel())) {
			sendLead2Player(player, needLeadLevels.get(player.getLevel()));
		}
	}

	/**
	 * 向玩家发送引导信息
	 * @param player
	 * @param id
	 */
	public void sendLead2Player(Player player, long id) {
		List<NewPlayerLeadData> list = leadDataMap.get(id);
		if (list != null && !list.isEmpty()) {
			String[] windowNames = new String[list.size()];
			String[] activeXs = new String[list.size()];
			String[] showMessages = new String[list.size()];
			int[] outStackEvents = new int[list.size()];
			int[] types = new int[list.size()];
			int[] prioritys = new int[list.size()];
			for (int i = 0; i < list.size(); i++) {
				NewPlayerLeadData data = list.get(i);
				windowNames[i] = data.getWindowName();
				activeXs[i] = data.getActiveXName();
				showMessages[i] = data.getShowMessage();
				outStackEvents[i] = data.getOutStackEvent();
				prioritys[i] = data.getPriority();
				types[i] = data.getType();
				int forceType=data.getForceType();
				if(forceType==1){
					types[i]=types[i]|0x80;
				}
			}
			NOTICE_CLIENT_NEWPLAYER_LEAD_REQ req = new NOTICE_CLIENT_NEWPLAYER_LEAD_REQ(GameMessageFactory.nextSequnceNum(), windowNames, activeXs, showMessages, outStackEvents, prioritys,types);
			player.addMessageToRightBag(req);
			if(NewPlayerLeadDataManager.logger.isInfoEnabled())
				NewPlayerLeadDataManager.logger.info(player.getLogString() + "[发送引导协议][id:{}][size:{}]", new Object[] { id, list.size() });
		} else {
			if(NewPlayerLeadDataManager.logger.isInfoEnabled())
				NewPlayerLeadDataManager.logger.info(player.getLogString() + "[发送引导协议][失败][对应条目不存在][id:{}]", new Object[] { id });
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public HashMap<Long, List<NewPlayerLeadData>> getLeadDataMap() {
		return leadDataMap;
	}

	public void setLeadDataMap(HashMap<Long, List<NewPlayerLeadData>> leadDataMap) {
		this.leadDataMap = leadDataMap;
	}

	public HashMap<Integer, Long> getNeedLeadLevels() {
		return needLeadLevels;
	}

	public void setNeedLeadLevels(HashMap<Integer, Long> needLeadLevels) {
		this.needLeadLevels = needLeadLevels;
	}

	public HashMap<String, Long> getAcceptTaskLead() {
		return acceptTaskLead;
	}

	public void setAcceptTaskLead(HashMap<String, Long> acceptTaskLead) {
		this.acceptTaskLead = acceptTaskLead;
	}

	public HashMap<String, Long> getCompleteTaskLead() {
		return completeTaskLead;
	}

	public void setCompleteTaskLead(HashMap<String, Long> completeTaskLead) {
		this.completeTaskLead = completeTaskLead;
	}

	public HashMap<String, Long> getDeliverTaskLead() {
		return deliverTaskLead;
	}

	public void setDeliverTaskLead(HashMap<String, Long> deliverTaskLead) {
		this.deliverTaskLead = deliverTaskLead;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.load();
		ServiceStartRecord.startLog(this);
	}
}
