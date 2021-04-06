package com.fy.engineserver.sprite.npc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.disaster.DisasterManager;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureNpc;
import com.fy.engineserver.activity.fireActivity.CommonFireActivityNpc;
import com.fy.engineserver.activity.fireActivity.FireActivityNpc;
import com.fy.engineserver.activity.pickFlower.FlowerNpc;
import com.fy.engineserver.activity.pickFlower.MagicWeaponNpc;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.SpriteSubSystem;
import com.fy.engineserver.sprite.monster.MonsterManager;
import com.fy.engineserver.sprite.monster.ReadSpriteExcelManager;
import com.fy.engineserver.sprite.npc.npcaction.ExecuteActiveSkill;
import com.fy.engineserver.sprite.npc.npcaction.FindMovePathAndMove;
import com.fy.engineserver.sprite.npc.npcaction.NpcAction;
import com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem;
import com.fy.engineserver.sprite.npc.npcaction.SaySomething;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.text.XmlUtil;

public class MemoryNPCManager implements NPCManager {

	private static NPCManager self;

	public static NPCManager getNPCManager() {
		return self;
	}

	public static class NPCTempalte {
		public int NPCCategoryId;
		public NPC npc;

		public NPCTempalte(int NPCCategoryId, NPC npc) {
			this.NPCCategoryId = NPCCategoryId;
			this.npc = npc;
		}

		public NPC newNPC() {
			NPC n = null;
			n = (NPC) npc.clone();
			return n;
		}
	}

	LinkedHashMap<Integer, NPCTempalte> templates = new LinkedHashMap<Integer, NPCTempalte>();
	public LinkedHashMap<String, NPCTempalte> templatesNameMap = new LinkedHashMap<String, NPCTempalte>();
	public LinkedHashMap<Integer, NpcAction> npcActionMap = new LinkedHashMap<Integer, NpcAction>();

	// List<NPC> playerList = Collections.synchronizedList(new ArrayList<NPC>());

	Map<Long, NPC> npcMap = Collections.synchronizedMap(new HashMap<Long, NPC>());
	Map<Integer, Integer> npcWidMap = new HashMap<Integer, Integer>();
	private String NPCWidPath;

	File NPCFile = new File("D:\\mywork\\工作用的资料\\npc.xls");

	protected TaskManager taskManager;

	public static int 掉落NPC的templateId = 1000000;
	public static long 掉落NPC的存在时间 = 60000;
	public static long 所有人都可以拾取的时长 = 30000;

	public static final int 一般npc所在sheet = 0;
	public static final int FlopCaijiNpc所在sheet = 1;
	public static final int npc行为_SaySomething_所在sheet = 2;
	public static final int 家族驻地NPC_所在sheet = 3;
	public static final int 家园NPC_所在sheet = 4;
	public static final int 家园DOORNPC_所在sheet = 5;
	public static final int 火墙npc_所在sheet = 6;
	public static final int SealTaskFlushNPC封印任务刷新npc_所在sheet = 7;
	public static final int npc行为FindMovePathAndMove找到路径进行移动_所在sheet = 8;
	public static final int npc行为ExecuteActiveSkill使用技能_所在sheet = 9;
	public static final int 篝火活动npc_所在sheet = 10;
	public static final int 任务采集npc_所在sheet = 11;
	public static final int 暴风雪npc_所在sheet = 12;
	public static final int 祝福果活动npc_所在sheet = 15;
	public static final int 镖车npc_所在sheet = 16;
	public static final int 矿NPC_所在sheet = 17;
	public static final int 需要被菜单记录的NPC_所在sheet = 18;
	public static final int 普通篝火NPC_所在sheet = 19;
	public static final int 地表NPC_所在sheet = 20;
	public static final int 国战NPC_所在sheet = 21;
	public static final int 城战NPC_所在sheet = 22;
	public static final int 地图宠物岛NPC_所在sheet = 23;
	public static final int 地图体力挂机NPC_所在sheet = 24;
	public static final int 地图万宝阁NPC_所在sheet = 25;
	public static final int 采花NPC_所在sheet = 26;
	public static final int 年兽_所在sheet = 27;
	public static final int 法宝npc_所在sheet = 28;
	public static final int 仙蒂npc_所在sheet = 29;
	public static final int 翅膀副本npc_所在sheet = 30;
	public static final int 仙丹修炼npc_所在sheet = 31;
	public static final int 仙界宝箱npc_所在sheet = 32;
	public static final int 宝箱乱斗npc_所在sheet = 33;
	public static final int 金猴天灾npc_所在sheet = 34;
	public static final int 攻击特定目标的npc_所在sheet = 35;
	public static final int 时间限制地图NPC_所在sheet = 36;
	public static final int 八卦仙阙npc_所在sheet = 37;

	public static final int 一般npc属性_nPCCategoryId_列 = 0;
	public static final int 一般npc属性_title_列 = 1;
	public static final int 一般npc属性_name_列 = 2;
	public static final int 一般npc属性_country_列 = 3;
	public static final int 一般npc属性_icon = 4;
	public static final int 一般npc属性_career_列 = 5;
	public static final int 一般npc属性_npcMark_列 = 6;
	public static final int 一般npc属性_hpMark_列 = 7;
	public static final int 一般npc属性_apMark_列 = 8;
	public static final int 一般npc属性_color_列 = 9;
	public static final int 一般npc属性_level_列 = 10;
	public static final int 一般npc属性_height_列 = 11;
	public static final int 一般npc属性_avataRace_列 = 12;
	public static final int 一般npc属性_avataSex_列 = 13;
	public static final int 一般npc属性_putOnEquipmentAvata_列 = 14;
	public static final int 一般npc属性_spriteType_列 = 15;
	public static final int 一般npc属性_npcType_列 = 16;
	public static final int 一般npc属性_commonAttackSpeed_列 = 17;
	public static final int 一般npc属性_commonAttackRange_列 = 18;
	public static final int 一般npc属性_maxHP_列 = 19;
	public static final int 一般npc属性_maxMP_列 = 20;
	public static final int 一般npc属性_hpRecoverBase_列 = 21;
	public static final int 一般npc属性_moveSpeed_列 = 22;
	public static final int 一般npc属性_NPC对应窗口的Id_列 = 23;
	public static final int 一般npc属性_NPC粒子 = 24;
	public static final int 一般npc属性_NPC粒子位置x = 25;
	public static final int 一般npc属性_NPC粒子位置y = 26;
	public static final int 一般npc属性_NPC脚下粒子 = 27;
	public static final int 一般npc属性_NPC脚下粒子位置x = 28;
	public static final int 一般npc属性_NPC脚下粒子位置y = 29;
	public static final int 一般npc属性_NPC体型大小 = 30;
	public static final int 一般npc属性_NPC对话 = 31;

	public static final int 一般npc属性_exeTimeLimit_列 = 32;
	public static final int 一般npc属性_minDistance_列 = 33;
	public static final int 一般npc属性_maxDistance_列 = 34;
	public static final int 一般npc属性_hpPercent_列 = 35;
	public static final int 一般npc属性_maxExeTimes_列 = 36;
	public static final int 一般npc属性_exeTimeGap_列 = 37;
	public static final int 一般npc属性_ignoreMoving_列 = 38;
	public static final int 一般npc属性_actionId_列 = 39;

	public static final int NpcAction_actionId = 0;
	public static final int NpcAction_description = 1;
	public static final int NpcAction_content = 2;

	public static final int NpcAction_FindMovePathAndMove_patrolingTimeInterval = 3;
	public static final int NpcAction_FindMovePathAndMove_range = 4;

	public static final int NpcAction_ExecuteActiveSkill_skillId = 3;
	public static final int NpcAction_ExecuteActiveSkill_level = 4;

	public File getNPCFile() {
		return NPCFile;
	}

	public void setNPCFile(File NPCFile) {
		this.NPCFile = NPCFile;
	}

	public Map<Integer, Integer> getNpcWidMap() {
		return npcWidMap;
	}

	public void setNpcWidMap(Map<Integer, Integer> npcWidMap) {
		this.npcWidMap = npcWidMap;
	}

	public String getNPCWidPath() {
		return NPCWidPath;
	}

	public void setNPCWidPath(String nPCWidPath) {
		NPCWidPath = nPCWidPath;
	}

	public void init() throws Exception {
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		loadFromWid();
		if (NPCFile != null && NPCFile.isFile() && NPCFile.exists()) {
			FileInputStream fis = new FileInputStream(NPCFile);
			loadFrom(fis);
		} else {
			System.out.println("提示：NPC配置文件[" + NPCFile + "]不存在，系统自动创建一些NPC来测试");
		}

		NPCTempalte ts[] = this.getNPCTemaplates();
		// for(int i = 0 ; i < ts.length ; i++){
		// NPC n = ts[i].npc;
		// Task tasks[] = this.taskManager.getTasksByNPC(n.getName());
		// Task tasks2[] = this.taskManager.getTasksByNPCForDeliver(n.getName());
		// if((tasks != null && tasks.length > 0) || (tasks2 != null && tasks2.length > 0)){
		// n.setTaskMark(true);
		// }else{
		// n.setTaskMark(false);
		// }
		// }

		self = this;

		ServiceStartRecord.startLog(this);
	}

	public static final int CategoryId列 = 0;
	public static final int Wid列 = 2;

	public void loadFromWid() throws Exception {
		HSSFWorkbook hssfWorkbook = null;
		InputStream is = new FileInputStream(NPCWidPath);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		hssfWorkbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = null;
		int sheets = hssfWorkbook.getNumberOfSheets();
		for (int i = 0; i < sheets; i++) {
			sheet = hssfWorkbook.getSheetAt(i);
			if (sheet == null) return;
			int rows = sheet.getPhysicalNumberOfRows();
			for (int j = 1; j < rows; j++) {
				HSSFRow row = sheet.getRow(j);
				if (row != null) {
					ActivitySubSystem.logger.warn("[npcWid.xls] [sheet:" + i + "] [row:" + j + "]");
					HSSFCell cell = row.getCell(CategoryId列);
					int categoryId = 0;
					try {
						categoryId = Integer.parseInt(cell.getStringCellValue().trim());
					} catch (Exception ex) {
						try {
							categoryId = (int) cell.getNumericCellValue();
						} catch (Exception e) {
							MonsterManager.logger.error(sheet.getSheetName(), e);
						}

					}
					cell = row.getCell(Wid列);
					int Wid = -1;
					if (cell != null) {
						Wid = (int) cell.getNumericCellValue();
					}
					npcWidMap.put(categoryId, Wid);
				}
			}
		}
		ActivitySubSystem.logger.warn("[npcWid.xls] [加载完毕]");
	}

	public void loadFrom(String data) throws Exception {

		Document dom = XmlUtil.loadString(data);
		Element root = dom.getDocumentElement();
		Element npcEles[] = XmlUtil.getChildrenByName(root, "npc");
		templates.clear();
		templatesNameMap.clear();
		for (int i = 0; i < npcEles.length; i++) {

			HashMap<String, String> map = new HashMap<String, String>();

			Element e = npcEles[i];
			Element eles[] = XmlUtil.getChildrenByName(e, "property");
			for (int j = 0; j < eles.length; j++) {
				String key = XmlUtil.getAttributeAsString(eles[j], "key", null, TransferLanguage.getMap());
				String value = XmlUtil.getAttributeAsString(eles[j], "value", null, TransferLanguage.getMap());
				if (key != null && value != null) {
					map.put(key, value);
				}
			}

			String className = map.remove("className");
			String categoryName = map.remove("categoryName");
			int categoryId = Integer.parseInt(map.remove("categoryId"));
			map.remove("shouldRefreshAttackParams");
			map.remove("isAvatarAnimation");
			map.remove("isClosed");

			Class<?> cl = Class.forName(className);
			Object o = cl.newInstance();

			String keys[] = map.keySet().toArray(new String[0]);
			for (int j = 0; j < keys.length; j++) {
				String key = keys[j];
				String value = map.get(keys[j]);
				try {
					setObjectValue(cl, o, key, value);
				} catch (Exception ex) {
					System.out.println("[初始化NPC，设置属性出错] [" + map.get("name") + "] [属性：" + key + "] [value:" + value + "]");
					ex.printStackTrace(System.out);
				}
			}
			NPC npc = (NPC) o;

			npc.setNPCCategoryId(categoryId);
			npc.setDeadLastingTime(npc.getDeadLastingTime() * 1000);
			npc.setFlushFrequency(npc.getFlushFrequency() * 1000);

			Element e2 = XmlUtil.getStrictChildByName(e, "additional-data");
			if (e2 != null) {
				npc.setAdditionData(e2);
			}

			NPCTempalte nt = new NPCTempalte(categoryId, (NPC) o);
			templates.put(categoryId, nt);
			templatesNameMap.put(nt.npc.getName(), nt);
			// System.out.println("加载NPC模板 ["+categoryName+"] ["+categoryId+"] ["+className+"] ["+npc.getName()+"]");
		}

		NPCTempalte ts[] = this.getNPCTemaplates();
		// for (int i = 0; i < ts.length; i++) {
		// NPC n = ts[i].npc;
		// Task tasks[] = this.taskManager.getTasksByNPC(n.getName());
		// if (tasks != null && tasks.length > 0) {
		// n.setTaskMark(true);
		// } else {
		// n.setTaskMark(false);
		// }
		// }
	}

	public void loadFrom(InputStream is) throws Exception {
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(npc行为_SaySomething_所在sheet);
		try {
			int rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				SaySomething na = new SaySomething();
				if (row != null) {
					HSSFCell hc = row.getCell(NpcAction_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						na.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						na.setActionId(actionId);
					}

					hc = row.getCell(NpcAction_description);
					try {
						String description = hc.getStringCellValue().trim();
						na.setDescription(description);
					} catch (Exception ex) {

					}

					hc = row.getCell(NpcAction_content);
					try {
						String content = hc.getStringCellValue().trim();
						na.setContent(content);
					} catch (Exception ex) {

					}
				}
				npcActionMap.put(na.getActionId(), na);
			}

			sheet = workbook.getSheetAt(npc行为FindMovePathAndMove找到路径进行移动_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				FindMovePathAndMove na = new FindMovePathAndMove();
				if (row != null) {
					HSSFCell hc = row.getCell(NpcAction_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						na.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						na.setActionId(actionId);
					}

					hc = row.getCell(NpcAction_description);
					try {
						String description = (hc.getStringCellValue().trim());
						na.setDescription(description);
					} catch (Exception ex) {

					}

					hc = row.getCell(NpcAction_content);
					try {
						String content = hc.getStringCellValue().trim();
						na.setContent(content);
					} catch (Exception ex) {

					}

					hc = row.getCell(NpcAction_FindMovePathAndMove_patrolingTimeInterval);
					try {
						long patrolingTimeInterval = Long.parseLong(hc.getStringCellValue().trim());
						na.patrolingTimeInterval = patrolingTimeInterval;
					} catch (Exception ex) {
						try {
							long patrolingTimeInterval = (long) hc.getNumericCellValue();
							na.patrolingTimeInterval = patrolingTimeInterval;
						} catch (Exception e) {

						}
					}

					hc = row.getCell(NpcAction_FindMovePathAndMove_range);
					try {
						short range = Short.parseShort(hc.getStringCellValue().trim());
						na.range = range;
					} catch (Exception ex) {
						try {
							short range = (short) hc.getNumericCellValue();
							na.range = range;
						} catch (Exception e) {

						}
					}
				}
				npcActionMap.put(na.getActionId(), na);
			}

			sheet = workbook.getSheetAt(npc行为ExecuteActiveSkill使用技能_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				ExecuteActiveSkill na = new ExecuteActiveSkill();
				if (row != null) {
					HSSFCell hc = row.getCell(NpcAction_actionId);
					try {
						int actionId = Integer.parseInt(hc.getStringCellValue().trim());
						na.setActionId(actionId);
					} catch (Exception ex) {
						int actionId = (int) hc.getNumericCellValue();
						na.setActionId(actionId);
					}

					hc = row.getCell(NpcAction_description);
					try {
						String description = (hc.getStringCellValue().trim());
						na.setDescription(description);
					} catch (Exception ex) {

					}

					hc = row.getCell(NpcAction_content);
					try {
						String content = hc.getStringCellValue().trim();
						// na.setContent(content);
					} catch (Exception ex) {

					}

					hc = row.getCell(NpcAction_ExecuteActiveSkill_skillId);
					try {
						int skillId = Integer.parseInt(hc.getStringCellValue().trim());
						na.skillId = skillId;
					} catch (Exception ex) {
						try {
							int skillId = (int) hc.getNumericCellValue();
							na.skillId = skillId;
						} catch (Exception e) {

						}
					}

					hc = row.getCell(NpcAction_ExecuteActiveSkill_level);
					try {
						int level = Integer.parseInt(hc.getStringCellValue().trim());
						na.skillLevel = level;
					} catch (Exception ex) {
						try {
							int level = (int) hc.getNumericCellValue();
							na.skillLevel = level;
						} catch (Exception e) {

						}
					}
				}
				npcActionMap.put(na.getActionId(), na);
			}

			sheet = workbook.getSheetAt(篝火活动npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				FireActivityNpc npc = new FireActivityNpc();

				if (row != null) {
					try {
						r = 一般npc属性设置(npc, sheet, r, rows);
					} catch (Exception e) {
						e.printStackTrace();
						MonsterManager.logger.error(sheet.getSheetName() + "行" + r + "异常", e);
					}
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);

			}

			sheet = workbook.getSheetAt(普通篝火NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				CommonFireActivityNpc npc = new CommonFireActivityNpc();
				if (row != null) {
					try {
						r = 一般npc属性设置(npc, sheet, r, rows);
					} catch (Exception e) {
						e.printStackTrace();
						MonsterManager.logger.error(sheet.getSheetName() + "行" + r + "异常", e);
					}
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(一般npc所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				NPC npc = new NPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(FlopCaijiNpc所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				FlopCaijiNpc npc = new FlopCaijiNpc();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			sheet = workbook.getSheetAt(家族驻地NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				SeptStationNPC npc = new SeptStationNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}

				npc.setAvatas(StringTool.string2Array(row.getCell(一般npc属性_actionId_列 + 1).getStringCellValue().trim(), ",", String.class));

				ResourceManager.getInstance().setAvata(npc);

				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			sheet = workbook.getSheetAt(家园NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				CaveNPC npc = new CaveNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}

				npc.setAvatas(StringTool.string2Array(row.getCell(一般npc属性_actionId_列 + 1).getStringCellValue().trim(), ",", String.class));
				HSSFCell cell = row.getCell(一般npc属性_actionId_列 + 2);
				if (cell != null) {
					npc.setBuildingAvatas(StringTool.string2Array(row.getCell(一般npc属性_actionId_列 + 2).getStringCellValue().trim(), ",", String.class));
				}

				ResourceManager.getInstance().setAvata(npc);

				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			sheet = workbook.getSheetAt(家园DOORNPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				CaveDoorNPC npc = new CaveDoorNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
					npc.setAvatas(StringTool.string2Array(row.getCell(一般npc属性_actionId_列 + 1).getStringCellValue().trim(), ",", String.class));
					npc.setAvatas1(StringTool.string2Array(row.getCell(一般npc属性_actionId_列 + 2).getStringCellValue().trim(), ",", String.class));
				}

				ResourceManager.getInstance().setAvata(npc);

				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(火墙npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				FireWallNPC npc = new FireWallNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(SealTaskFlushNPC封印任务刷新npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				SealTaskFlushNPC npc = new SealTaskFlushNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(国战NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				GuozhanNPC npc = new GuozhanNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(城战NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				CityFightNPC npc = new CityFightNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

						sheet = workbook.getSheetAt(地图宠物岛NPC_所在sheet);
						rows = sheet.getPhysicalNumberOfRows();
					for (int r = 1; r < rows; r++) {
							HSSFRow row = sheet.getRow(r);
							DiTuPetNPC npc = new DiTuPetNPC();
							if (row != null) {
								r = 一般npc属性设置(npc, sheet, r, rows);
							}
							NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
							templates.put(npc.getNPCCategoryId(), nt);
							templatesNameMap.put(nt.npc.getName(), nt);
						}
			
			sheet = workbook.getSheetAt(地图体力挂机NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				DiTuTiLiNPC npc = new DiTuTiLiNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(地图万宝阁NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				DiTuWanBaoNPC npc = new DiTuWanBaoNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			
			sheet = workbook.getSheetAt(仙蒂npc_所在sheet);
				rows = sheet.getPhysicalNumberOfRows();
				for (int r = 1; r < rows; r++) {
					HSSFRow row = sheet.getRow(r);
					DiTuXianDiNPC npc = new DiTuXianDiNPC();
						if (row != null) {
								r = 一般npc属性设置(npc, sheet, r, rows);
						}
						NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
						templates.put(npc.getNPCCategoryId(), nt);
						templatesNameMap.put(nt.npc.getName(), nt);
				}
				
				try {
					sheet = workbook.getSheetAt(八卦仙阙npc_所在sheet);
					rows = sheet.getPhysicalNumberOfRows();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						DiTuBaGuaXianQueNPC npc = new DiTuBaGuaXianQueNPC();
						if (row != null) {
							r = 一般npc属性设置(npc, sheet, r, rows);
						}
						NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
						Game.logger.warn("[加载npctest] ["+npc.getNPCCategoryId()+"] [nt:"+nt+"] ["+nt.npc.getName()+"]");
						templates.put(npc.getNPCCategoryId(), nt);
						templatesNameMap.put(nt.npc.getName(), nt);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Game.logger.warn("[加载npctest异常] [2]",e);
				}
				

			sheet = workbook.getSheetAt(任务采集npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				TaskCollectionNPC npc = new TaskCollectionNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
					try {
						npc.setTaskNames(Arrays.asList(StringTool.string2Array((row.getCell(一般npc属性_actionId_列 + 1).getStringCellValue()), ",", String.class)));
						npc.setArticleName((row.getCell(一般npc属性_actionId_列 + 2).getStringCellValue()));
						npc.setArticleColor((int) (row.getCell(一般npc属性_actionId_列 + 3).getNumericCellValue()));
						npc.setArticleMinNum((int) (row.getCell(一般npc属性_actionId_列 + 4).getNumericCellValue()));
						npc.setArticleMaxNum((int) (row.getCell(一般npc属性_actionId_列 + 5).getNumericCellValue()));
						npc.setCollectionBarTime((long) (TimeTool.TimeDistance.SECOND.getTimeMillis() * row.getCell(一般npc属性_actionId_列 + 6).getNumericCellValue()));
					} catch (Exception e) {
						CoreSubSystem.logger.error("[加载采集NPC错误]", e);
					}
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(暴风雪npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				BlizzardNPC npc = new BlizzardNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}


			sheet = workbook.getSheetAt(祝福果活动npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				ForLuckFruitNPC npc = new ForLuckFruitNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(镖车npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				BiaoCheNpc npc = new BiaoCheNpc();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				npc.setLife((long) (TimeTool.TimeDistance.SECOND.getTimeMillis() * row.getCell(一般npc属性_actionId_列 + 1).getNumericCellValue()));
				npc.setRadius((int) (row.getCell(一般npc属性_actionId_列 + 2).getNumericCellValue()));
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(矿NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				OreNPC npc = new OreNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			sheet = workbook.getSheetAt(需要被菜单记录的NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				CommonRecordByOptionNPC npc = new CommonRecordByOptionNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(采花NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				FlowerNpc npc = new FlowerNpc();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			sheet = workbook.getSheetAt(年兽_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				ShowTitleAsHpNPC npc = new ShowTitleAsHpNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
					npc.setBornValue((int) (row.getCell(一般npc属性_actionId_列 + 1).getNumericCellValue()));
					npc.setDisappearValue((int) (row.getCell(一般npc属性_actionId_列 + 2).getNumericCellValue()));
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}

			sheet = workbook.getSheetAt(法宝npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				MagicWeaponNpc npc = new MagicWeaponNpc();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				} else {
					continue;
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			
			sheet = workbook.getSheetAt(翅膀副本npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				WingCarbonNPC npc = new WingCarbonNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				} else {
					continue;
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			
			sheet = workbook.getSheetAt(仙丹修炼npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				FrunaceNPC npc = new FrunaceNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				} else {
					continue;
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			
			sheet = workbook.getSheetAt(仙界宝箱npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				FairylandTreasureNpc npc = new FairylandTreasureNpc();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			
			sheet = workbook.getSheetAt(宝箱乱斗npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				ChestFightNPC npc = new ChestFightNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			
			sheet = workbook.getSheetAt(金猴天灾npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				DisasterFireNPC npc = new DisasterFireNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				npc.setTriggerCDs(ReadFileTool.getLongArrByString(row, 一般npc属性_actionId_列 + 1, DisasterManager.logger, ";", 0));
				npc.setDamage(ReadFileTool.getInt(row, 一般npc属性_actionId_列 + 2, DisasterManager.logger));
//				(long) row.getCell(一般npc属性_actionId_列 + 1).getNumericCellValue());
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			sheet = workbook.getSheetAt(攻击特定目标的npc_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				AppointedAttackNPC npc = new AppointedAttackNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
//				(long) row.getCell(一般npc属性_actionId_列 + 1).getNumericCellValue());
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			sheet = workbook.getSheetAt(时间限制地图NPC_所在sheet);
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				DituLimitNPC npc = new DituLimitNPC();
				if (row != null) {
					r = 一般npc属性设置(npc, sheet, r, rows);
				}
				npc.setDituType(ReadFileTool.getInt(row, 一般npc属性_actionId_列 + 1, ActivitySubSystem.logger));
				npc.setLevelLimit(ReadFileTool.getInt(row, 一般npc属性_actionId_列 + 2, ActivitySubSystem.logger));
//				(long) row.getCell(一般npc属性_actionId_列 + 1).getNumericCellValue());
				NPCTempalte nt = new NPCTempalte(npc.getNPCCategoryId(), npc);
				templates.put(npc.getNPCCategoryId(), nt);
				templatesNameMap.put(nt.npc.getName(), nt);
			}
			

		} catch (Exception ex) {
			MonsterManager.logger.error(sheet.getSheetName(), ex);
			throw ex;
		}

	}

	/**
	 * 返回当前运行到的行数
	 * 比如只运行了第一行，那么返回0
	 * @param npc
	 * @param sheet
	 * @param rowNum
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public int 一般npc属性设置(NPC npc, HSSFSheet sheet, int rowNum, int rows) throws Exception {
		HSSFRow row = sheet.getRow(rowNum);
		if (row != null) {
			HSSFCell hc = row.getCell(一般npc属性_nPCCategoryId_列);
			try {
				int nPCCategoryId = Integer.parseInt(hc.getStringCellValue().trim());
				npc.setNPCCategoryId(nPCCategoryId);
			} catch (Exception ex) {
				try {
					int nPCCategoryId = (int) hc.getNumericCellValue();
					npc.setNPCCategoryId(nPCCategoryId);
				} catch (Exception e) {
					MonsterManager.logger.error(sheet.getSheetName(), e);
				}

			}

			hc = row.getCell(一般npc属性_title_列);
			try {
				String title = (hc.getStringCellValue().trim());
				npc.setTitle(title);
			} catch (Exception ex) {
				npc.setTitle("");
			}

			hc = row.getCell(一般npc属性_name_列);
			try {
				String name = (hc.getStringCellValue().trim());
				npc.setName(name);
				MonsterManager.logger.warn("加载到了NPC:" + name);
			} catch (Exception ex) {
				MonsterManager.logger.warn("加载NPC错误:" + sheet.getSheetName() + "[" + row.getRowNum() + "行]");
				throw ex;
			}

			hc = row.getCell(一般npc属性_country_列);
			try {
				byte country = Byte.parseByte(hc.getStringCellValue().trim());
				npc.setCountry(country);
			} catch (Exception ex) {
				byte country = (byte) hc.getNumericCellValue();
				npc.setCountry(country);
			}

			hc = row.getCell(一般npc属性_icon);
			try {
				String icon = hc.getStringCellValue().trim();
				npc.icon = icon;
			} catch (Exception ex) {
				System.out.println("====================npc icon 不存在==========npc:"+npc.getName()+"--"+ex);
			}

			hc = row.getCell(一般npc属性_career_列);
			try {
				byte career = Byte.parseByte(hc.getStringCellValue().trim());
				npc.setCareer(career);
			} catch (Exception ex) {
				byte career = (byte) hc.getNumericCellValue();
				npc.setCareer(career);
			}

			hc = row.getCell(一般npc属性_npcMark_列);
			try {
				double npcMark = Double.parseDouble(hc.getStringCellValue().trim());
				npc.setNpcMark(npcMark);
			} catch (Exception ex) {
				double npcMark = (double) hc.getNumericCellValue();
				npc.setNpcMark(npcMark);
			}

			hc = row.getCell(一般npc属性_hpMark_列);
			try {
				double hpMark = Double.parseDouble(hc.getStringCellValue().trim());
				npc.setHpMark(hpMark);
			} catch (Exception ex) {
				double hpMark = (double) hc.getNumericCellValue();
				npc.setHpMark(hpMark);
			}

			hc = row.getCell(一般npc属性_apMark_列);
			try {
				double apMark = Double.parseDouble(hc.getStringCellValue().trim());
				npc.setApMark(apMark);
			} catch (Exception ex) {
				double apMark = (double) hc.getNumericCellValue();
				npc.setApMark(apMark);
			}

			hc = row.getCell(一般npc属性_color_列);
			try {
				byte color = Byte.parseByte(hc.getStringCellValue().trim());
				npc.setColor(color);
			} catch (Exception ex) {
				byte color = (byte) hc.getNumericCellValue();
				npc.setColor(color);
			}

			hc = row.getCell(一般npc属性_level_列);
			try {
				int level = Integer.parseInt(hc.getStringCellValue().trim());
				npc.setLevel(level);
			} catch (Exception ex) {
				int level = (int) hc.getNumericCellValue();
				npc.setLevel(level);
			}

			hc = row.getCell(一般npc属性_height_列);
			try {
				int height = Integer.parseInt(hc.getStringCellValue().trim());
				npc.setHeight(height);
			} catch (Exception ex) {
				try {
					int height = (int) hc.getNumericCellValue();
					npc.setHeight(height);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_avataRace_列);
			try {
				String avataRace = hc.getStringCellValue().trim();
				npc.setAvataRace(avataRace);
			} catch (Exception ex) {
				npc.setAvataRace("");
			}

			hc = row.getCell(一般npc属性_avataSex_列);
			try {
				String avataSex = hc.getStringCellValue().trim();
				npc.setAvataSex(avataSex);
			} catch (Exception ex) {
				npc.setAvataSex("");
			}

			hc = row.getCell(一般npc属性_putOnEquipmentAvata_列);
			try {
				String putOnEquipmentAvata = hc.getStringCellValue().trim();
				npc.setPutOnEquipmentAvata(putOnEquipmentAvata);
			} catch (Exception ex) {
				npc.setPutOnEquipmentAvata("");
			}

			hc = row.getCell(一般npc属性_spriteType_列);
			try {
				byte spriteType = Byte.parseByte(hc.getStringCellValue().trim());
				npc.setSpriteType(spriteType);
			} catch (Exception ex) {
				try {
					byte spriteType = (byte) hc.getNumericCellValue();
					npc.setSpriteType(spriteType);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_npcType_列);
			try {
				byte npcType = Byte.parseByte(hc.getStringCellValue().trim());
				npc.setNpcType(npcType);
			} catch (Exception ex) {
				try {
					byte npcType = (byte) hc.getNumericCellValue();
					npc.setNpcType(npcType);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_commonAttackSpeed_列);
			try {
				int commonAttackSpeed = Integer.parseInt(hc.getStringCellValue().trim());
				npc.setCommonAttackSpeed(commonAttackSpeed);
			} catch (Exception ex) {
				try {
					int commonAttackSpeed = (int) hc.getNumericCellValue();
					npc.setCommonAttackSpeed(commonAttackSpeed);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_commonAttackRange_列);
			try {
				int commonAttackRange = Integer.parseInt(hc.getStringCellValue().trim());
				npc.setCommonAttackRange(commonAttackRange);
			} catch (Exception ex) {
				try {
					int commonAttackRange = (int) hc.getNumericCellValue();
					npc.setCommonAttackRange(commonAttackRange);
				} catch (Exception e) {

				}
			}
			//
			// hc = row.getCell(一般npc属性_maxHP_列);
			// try {
			// int maxHP = Integer.parseInt(hc.getStringCellValue().trim());
			// npc.setMaxHP(maxHP);
			// } catch (Exception ex) {
			// int maxHP = (int) hc.getNumericCellValue();
			// npc.setMaxHP(maxHP);
			// }
			//
			// hc = row.getCell(一般npc属性_maxMP_列);
			// try {
			// int maxMP = Integer.parseInt(hc.getStringCellValue().trim());
			// npc.setMaxMP(maxMP);
			// } catch (Exception ex) {
			// int maxMP = (int) hc.getNumericCellValue();
			// npc.setMaxMP(maxMP);
			// }

			hc = row.getCell(一般npc属性_hpRecoverBase_列);
			try {
				int hpRecoverBase = Integer.parseInt(hc.getStringCellValue().trim());
				npc.setHpRecoverBase(hpRecoverBase);
			} catch (Exception ex) {
				try {
					int hpRecoverBase = (int) hc.getNumericCellValue();
					npc.setHpRecoverBase(hpRecoverBase);
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_moveSpeed_列);
			try {
				int moveSpeed = (int) hc.getNumericCellValue();
				npc.setSpeedA(moveSpeed);
			} catch (Exception ex) {

			}

			hc = row.getCell(一般npc属性_NPC对应窗口的Id_列);
			try {
				if (npcWidMap != null) {
					if (npcWidMap.get(npc.getNPCCategoryId()) != null) {
						int windowId = npcWidMap.get(npc.getNPCCategoryId());
						if (windowId > 0) {
							npc.setWindowId(windowId);
						}
					} else {
						if (JiazuSubSystem.logger.isDebugEnabled()) {
							JiazuSubSystem.logger.debug("[测试] [npc对应wid不存在] [" + npc.getnPCCategoryId() + "]");
						}
						npc.setWindowId(-1);
					}
				} else {
					ActivitySubSystem.logger.error("[npc与windowId对应关系不存在]");
				}
				// int windowId = Integer.parseInt(hc.getStringCellValue().trim());
			} catch (Exception ex) {
				// try {
				// int windowId = (int) hc.getNumericCellValue();
				// npc.setWindowId(windowId);
				// } catch (Exception e) {
				//
				// }
				ActivitySubSystem.logger.error("[设置npc的窗口id出错]");
			}

			hc = row.getCell(一般npc属性_NPC粒子);
			String ray = "";
			if (hc != null) {
				try {
					ray = hc.getStringCellValue().trim();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			npc.setParticleName(ray);

			hc = row.getCell(一般npc属性_NPC粒子位置x);
			short rayX = 0;
			if (hc != null) {
				try {
					rayX = (short) hc.getNumericCellValue();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			npc.setParticleX(rayX);

			hc = row.getCell(一般npc属性_NPC粒子位置y);
			short rayY = 50;
			if (hc != null) {
				try {
					rayY = (short) hc.getNumericCellValue();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			npc.setParticleY(rayY);
			hc = row.getCell(一般npc属性_NPC脚下粒子);
			String footParticle = "";
			if (hc != null) {
				try {
					footParticle = hc.getStringCellValue().trim();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			npc.setFootParticleName(footParticle);

			hc = row.getCell(一般npc属性_NPC脚下粒子位置x);
			short footParticleX = 0;
			if (hc != null) {
				try {
					footParticleX = (short) hc.getNumericCellValue();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			npc.setFootParticleX(footParticleX);

			hc = row.getCell(一般npc属性_NPC脚下粒子位置y);
			short footParticleY = 50;
			if (hc != null) {
				try {
					footParticleY = (short) hc.getNumericCellValue();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			npc.setFootParticleY(footParticleY);

			hc = row.getCell(一般npc属性_NPC体型大小);
			if (hc != null) {
				try {
					short objectScale = 0;
					objectScale = (short) hc.getNumericCellValue();
					npc.setObjectScale(objectScale);
				} catch (Exception e) {
					if (SpriteSubSystem.logger.isWarnEnabled()) SpriteSubSystem.logger.warn("[init] [error] [ ID: " + npc.getnPCCategoryId() + "", e);
				}
			}

			hc = row.getCell(一般npc属性_NPC对话);
			String dialog = "";
			if (hc != null) {
				try {
					dialog = hc.getStringCellValue();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			npc.setDialogContent(dialog);

			设置sprite属性值(npc);
			ArrayList<NpcExecuteItem> items = new ArrayList<NpcExecuteItem>();
			rowNum = 设置npc的ai(items, sheet, rowNum, rows);
			npc.setItems(items.toArray(new NpcExecuteItem[0]));

		}
		return rowNum;
	}

	/**
	 * 返回当前运行到的行数
	 * 比如只运行了第一行，那么返回0
	 * @param npc
	 * @param sheet
	 * @param rowNum
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public int 设置npc的ai(ArrayList<NpcExecuteItem> items, HSSFSheet sheet, int rowNum, int maxNum) throws Exception {
		int start = rowNum;
		for (int i = start; i < maxNum; i++) {
			if (i != start) {
				HSSFRow row = sheet.getRow(i);
				try {
					HSSFCell hc = row.getCell(一般npc属性_nPCCategoryId_列);
					if (hc.getNumericCellValue() != 0) {
						return i - 1;
					}
				} catch (Exception ex) {

				}
			}

			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				throw new Exception(sheet.getSheetName() + "第" + i + "行为空");
			}
			NpcExecuteItem item = new NpcExecuteItem();
			boolean has = false;
			HSSFCell hc = row.getCell(一般npc属性_exeTimeLimit_列);
			try {
				long exeTimeLimit = Long.parseLong(hc.getStringCellValue().trim());
				item.setExeTimeLimit(exeTimeLimit);
				has = true;
			} catch (Exception ex) {
				try {
					long exeTimeLimit = (long) hc.getNumericCellValue();
					item.setExeTimeLimit(exeTimeLimit);
					has = true;
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_minDistance_列);
			try {
				int minDistance = Integer.parseInt(hc.getStringCellValue().trim());
				item.setMinDistance(minDistance);
				has = true;
			} catch (Exception ex) {
				try {
					int minDistance = (int) hc.getNumericCellValue();
					item.setMinDistance(minDistance);
					has = true;
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_maxDistance_列);
			try {
				int maxDistance = Integer.parseInt(hc.getStringCellValue().trim());
				item.setMaxDistance(maxDistance);
				has = true;
			} catch (Exception ex) {
				try {
					int maxDistance = (int) hc.getNumericCellValue();
					item.setMaxDistance(maxDistance);
					has = true;
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_hpPercent_列);
			try {
				int hpPercent = Integer.parseInt(hc.getStringCellValue().trim());
				item.setHpPercent(hpPercent);
				has = true;
			} catch (Exception ex) {
				try {
					int hpPercent = (int) hc.getNumericCellValue();
					item.setHpPercent(hpPercent);
					has = true;
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_maxExeTimes_列);
			try {
				int maxExeTimes = Integer.parseInt(hc.getStringCellValue().trim());
				item.setMaxExeTimes(maxExeTimes);
				has = true;
			} catch (Exception ex) {
				try {
					int maxExeTimes = (int) hc.getNumericCellValue();
					item.setMaxExeTimes(maxExeTimes);
					has = true;
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_exeTimeGap_列);
			try {
				long exeTimeGap = Long.parseLong(hc.getStringCellValue().trim());
				item.setExeTimeGap(exeTimeGap);
				has = true;
			} catch (Exception ex) {
				try {
					long exeTimeGap = (long) hc.getNumericCellValue();
					item.setExeTimeGap(exeTimeGap);
					has = true;
				} catch (Exception e) {

				}
			}

			hc = row.getCell(一般npc属性_ignoreMoving_列);
			try {
				boolean ignoreMoving = hc.getBooleanCellValue();
				item.setIgnoreMoving(ignoreMoving);
				has = true;
			} catch (Exception ex) {

			}

			hc = row.getCell(一般npc属性_actionId_列);
			try {
				int actionId = Integer.parseInt(hc.getStringCellValue().trim());
				item.setActionId(actionId);
				has = true;
			} catch (Exception ex) {
				try {
					int actionId = (int) hc.getNumericCellValue();
					item.setActionId(actionId);
					has = true;
				} catch (Exception e) {

				}
			}
			if (has) {
				if (npcActionMap.get(item.getActionId()) != null) {
					item.setAction(npcActionMap.get(item.getActionId()));
				}
				items.add(item);
			}
		}

		return maxNum - 1;
	}

	public void 设置sprite属性值(NPC sprite) {
		int level = sprite.getLevel();
		byte career = sprite.getCareer();
		ReadSpriteExcelManager rem = ReadSpriteExcelManager.getInstance();
		if (level >= rem.careerExcelDatas[career].length) {
			level = rem.careerExcelDatas[career].length - 1;
		}
		int[] values = rem.careerExcelDatas[career][level];
		for (int i = 0; i < values.length; i++) {
			int value = values[i];
			// 还需要根据npcMark和hpMark和apMark来具体决定数值
			if (value != 0) {
				value = (int) (value * sprite.getNpcMark());
				switch (i) {
				case 0:
					value = (int) (value * (1 + sprite.getHpMark()));
					if (value <= 0) {
						System.out.println("[npc配置出错] [" + sprite.getName() + "] [血量出问题] [配置表的hp:" + values[i] + "] [hp:" + value + "] [npcMark:" + sprite.getNpcMark() + "] [hpMark:" + sprite.getHpMark() + "]");
						value = 200000000;
					}
					sprite.setMaxHPA(value);
					sprite.setHp(sprite.getMaxHP());
					break;
				case 1:
					value = (int) (value * (1 + sprite.getApMark()));
					sprite.setPhyAttackA(value);
					break;
				case 2:
					value = (int) (value * (1 + sprite.getApMark()));
					sprite.setMagicAttackA(value);
					break;
				case 3:
					sprite.setPhyDefenceA(value);
					break;
				case 4:
					sprite.setMagicDefenceA(value);
					break;
				case 5:
					sprite.setMaxMPA(value);
					break;
				case 6:
					sprite.setBreakDefenceA(value);
					break;
				case 7:
					sprite.setHitA(value);
					break;
				case 8:
					sprite.setDodgeA(value);
					break;
				case 9:
					sprite.setAccurateA(value);
					break;
				case 10:
					sprite.setCriticalHitA(value);
					break;
				case 11:
					sprite.setCriticalDefenceA(value);
					break;
				case 12:
					sprite.setFireAttackA(value);
					break;
				case 13:
					sprite.setBlizzardAttackA(value);
					break;
				case 14:
					sprite.setWindAttackA(value);
					break;
				case 15:
					sprite.setThunderAttackA(value);
					break;
				case 16:
					sprite.setFireDefenceA(value);
					break;
				case 17:
					sprite.setBlizzardDefenceA(value);
					break;
				case 18:
					sprite.setWindDefenceA(value);
					break;
				case 19:
					sprite.setThunderDefenceA(value);
					break;
				case 20:
					sprite.setFireIgnoreDefenceA(value);
					break;
				case 21:
					sprite.setBlizzardIgnoreDefenceA(value);
					break;
				case 22:
					sprite.setWindIgnoreDefenceA(value);
					break;
				case 23:
					sprite.setThunderIgnoreDefenceA(value);
					break;
				}
			}
		}

	}

	/**
	 * 设置一个对象的属性，需要此对象的属性提供setter和getter方法
	 * @param cl
	 * @param o
	 * @param key
	 * @param value
	 * @throws Exception
	 */

	public static void setObjectValue(Class cl, Object o, String key, String value) throws Exception {
		Field f = null;
		Class clazz = cl;
		while (clazz != null && f == null) {
			try {
				f = clazz.getDeclaredField(key);
			} catch (Exception e) {
				clazz = clazz.getSuperclass();
			}
		}
		if (f == null) {
			throw new Exception("NPC类[" + cl.getName() + "]没有属性[" + key + "]，请检查！");
		}

		Class t = f.getType();
		Method m = null;
		try {
			m = cl.getMethod("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1), new Class[] { t });
		} catch (Exception e) {
			throw new Exception("NPC类[" + cl.getName() + "]属性[" + key + "]的设置方法[" + ("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1) + "(" + t.getName() + ")") + "]，请检查！");
		}
		Object v = null;
		try {
			if (t == Byte.class || t == Byte.TYPE) {
				v = (byte) Integer.parseInt(value);
			} else if (t == Short.class || t == Short.TYPE) {
				v = Short.parseShort(value);
			} else if (t == Integer.class || t == Integer.TYPE) {
				v = Integer.parseInt(value);
			} else if (t == Long.class || t == Long.TYPE) {
				v = Long.parseLong(value);
			} else if (t == Boolean.class || t == Boolean.TYPE) {
				v = Boolean.parseBoolean(value);
			} else if (t == String.class) {
				value = TransferLanguage.transferString(value);
				v = value;
			} else if (t.isArray()) {
				if (value.trim().length() == 0) {
					v = Array.newInstance(t, 0);
				} else {
					String ss[] = value.split(",");
					ArrayList<String> al = new ArrayList<String>();
					for (int i = 0; i < ss.length; i++) {
						if (ss[i].trim().length() > 0) {
							al.add(ss[i].trim());
						}
					}
					ss = al.toArray(new String[0]);
					t = t.getComponentType();
					v = Array.newInstance(t, ss.length);
					for (int i = 0; i < ss.length; i++) {
						Object v2 = null;
						if (t == Byte.class || t == Byte.TYPE) {
							v2 = Byte.parseByte(ss[i]);
						} else if (t == Short.class || t == Short.TYPE) {
							v2 = Short.parseShort(ss[i]);
						} else if (t == Integer.class || t == Integer.TYPE) {
							v2 = Integer.parseInt(ss[i]);
						} else if (t == Long.class || t == Long.TYPE) {
							v2 = Long.parseLong(ss[i]);
						} else if (t == Boolean.class || t == Boolean.TYPE) {
							v2 = Boolean.parseBoolean(ss[i]);
						} else if (t == String.class) {
							v2 = ss[i];
						} else {
							throw new Exception("NPC类[" + cl.getName() + "]属性[" + key + "]为复杂的数据类型【" + t.getName() + "的数组】，暂时不支持！");
						}
						Array.set(v, i, v2);
					}
				}
			} else {
				throw new Exception("NPC类[" + cl.getName() + "]属性[" + key + "]为复杂的数据类型【" + t.getName() + "】，暂时不支持！");
			}
		} catch (NumberFormatException e) {
			throw new Exception("NPC类[" + cl.getName() + "]属性[" + key + "]为数据【" + value + "】解析出错，请检查配置文件！");
		}
		m.invoke(o, new Object[] { v });
	}

	public NPCTempalte getNPCTempalteByCategoryId(int categoryId) {
		return templates.get(categoryId);
	}

	public NPCTempalte getNPCTempalteByCategoryName(String name) {
		return templatesNameMap.get(name);
	}

	/**
	 * 根据一个种类，创建新的一个个体
	 * @param NPCCategoryId
	 * @return
	 */
	public NPC createNPC(int NPCCategoryId) {
		NPCTempalte st = templates.get(NPCCategoryId);
		if (st == null) {
			FairylandTreasureManager.logger.warn("[仙界宝箱] [NPCCategoryId:" + NPCCategoryId + "] [NPCTempalte=null]");
			return null;
		}
		NPC s = st.newNPC();
		if (s != null) {
			s.setId(Sprite.nextId());
			s.init();
			npcMap.put(s.getId(), s);
		}
		return s;
	}

	/**
	 * 创建法宝npc
	 * @param p
	 * @param mwName
	 * @param entity
	 * @return
	 */
	// public MagicWeaponNpc createMagicWeapon(Player p, String mwName, NewMagicWeaponEntity entity) {
	// ArticleManager am = ArticleManager.getInstance();
	// if(am != null) {
	// try {
	// MagicWeapon mw = (MagicWeapon) am.getArticle(mwName);
	// MagicWeaponNpc npc = new MagicWeaponNpc();
	// npc.setOwnerId(p.getId());
	// npc.setName(mwName);
	// if(mw.getIllution_icons() != null && mw.getIllution_icons().size() > 0 && entity.getIllusionLevel() > 0) {
	// npc.setIcon(mw.getIllution_icons().get(entity.getIllusionLevel() - 1));
	// } else {
	// npc.setIcon(mw.getIconId());
	// }
	// npc.setOwnerName(p.getName());
	// return npc;
	// } catch(Exception e) {
	// MagicWeaponManager.logger.error("" + mwName, e);
	// }
	// }
	// return null;
	// }

	/**
	 * 外部创建了一个新的NPC
	 * 此方法会设置npc的id以及调用npc的init方法
	 * @param npc
	 */
	public void addNewNPC(NPC npc) {
		npc.setId(Sprite.nextId());
		npc.init();
		npcMap.put(npc.getId(), npc);
	}

	/**
	 * 根据一个种类，创建新的一个个体
	 * @param NPCCategoryId
	 * @return
	 */
	public NPC createNPC(NPC template) {
		return createNPC(template.getNPCCategoryId());
	}

	public void removeNPC(NPC s) {
		npcMap.remove(s.getId());
	}

	public int getAmountOfNPCs() {
		return npcMap.size();
	}

	public NPC getNPC(long autoId) {
		return npcMap.get(autoId);
	}

	/**
	 * 通过地图名字和NPC名字获得NPC
	 * @param gameName
	 * @param NPCName
	 * @return
	 */
	// public NPC getNPCByGameAndName(String gameName, String NPCName) {
	// TaskManager.logger.info("\t\t[查找任务地图{}][NPC名字{}]", new Object[] { gameName, NPCName });
	// try {
	// Game game = GameManager.getInstance().getDisplayGameMap().get(gameName);
	// TaskManager.logger.info("\t\t[查找任务地图{}][NPC名字{}][地图{}]", new Object[] { gameName, NPCName, game == null });
	//
	// if (game != null) {
	// TaskManager.logger.info("[查找任务地图{}][NPC名字{}][地图上生物{}]", new Object[] { gameName, NPCName, game.getLivingObjects() });
	// for (LivingObject lo : game.getLivingObjects()) {
	// if (lo instanceof NPC) {
	// TaskManager.logger.info("[getNPCByGameAndName][地图{}][NPC{}][要检查NPC{}]", new Object[] { gameName, ((NPC) lo).getName(), NPCName });
	// if (((NPC) lo).getName().equals(NPCName)) {
	// return ((NPC) lo);
	// } else {
	// }
	// } else {
	// }
	// }
	// } else {
	// TaskManager.logger.info("[getNPCByGameAndName][地图不存在{}{}]", new Object[] { gameName, NPCName });
	// }
	// } catch (Exception e) {
	// TaskManager.logger.info("ERROR", e);
	// }
	// return null;
	// }

	public NPC[] getNPCsByPage(int start, int size) {
		int end = start + size;
		ArrayList<NPC> al = new ArrayList<NPC>();
		NPC npcs[] = npcMap.values().toArray(new NPC[0]);
		for (int i = start; i < end && i < npcs.length; i++) {
			NPC p = npcs[i];
			al.add(p);
		}
		return al.toArray(new NPC[0]);
	}

	public boolean isExists(int autoId) {
		return getNPC(autoId) != null;
	}

	public NPCTempalte[] getNPCTemaplates() {
		return templates.values().toArray(new NPCTempalte[0]);
	}

	public NPCTempalte getNPCTempalteById(int categoryId) {
		return templates.get(categoryId);
	}

	// public NPC getNpcByCategoryId(int categoryId){
	// for (int i = 0; i < playerList.size(); i++) {
	// NPC p = playerList.get(i);
	// if (p.getNPCCategoryId() == categoryId)
	// return p;
	// }
	// return null;
	// }

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public static void main(String[] args) {
		MemoryNPCManager nm = new MemoryNPCManager();
		try {
			// nm.init();
			nm.setNPCWidPath("E:/code/game_mieshi_server/conf/game_init_config/npcWid.xls");
			nm.loadFromWid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LinkedHashMap<Integer, NPCTempalte> getTemplates() {
		return templates;
	}

	public void setTemplates(LinkedHashMap<Integer, NPCTempalte> templates) {
		this.templates = templates;
	}

}
