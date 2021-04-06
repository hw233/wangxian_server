package com.fy.engineserver.menu.cave;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveDoorplate;
import com.fy.engineserver.homestead.cave.CaveFence;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.cave.CaveMainBuilding;
import com.fy.engineserver.homestead.cave.CavePethouse;
import com.fy.engineserver.homestead.cave.CavePosition;
import com.fy.engineserver.homestead.cave.CaveStorehouse;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.homestead.exceptions.PointNotFoundException;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.CompoundReturn;

public class Option_Cave_NotifyCave extends CaveOption implements NeedCheckPurview, FaeryConfig {
	public Option_Cave_NotifyCave() {
	}

	@Override
	public void doSelect(Game game, Player player) {
		
		try {
			if (UnitServerFunctionManager.needCloseFunctuin(Function.仙府)) {
				player.sendError(Translate.合服功能关闭提示);
				return;
			}
			if (player.getCaveId() <= 0) {
				player.sendError(Translate.text_cave_007);
				return;
			}
			Cave cave = FaeryManager.getInstance().getCave(player);
			if (cave != null) {
				player.sendError(Translate.text_cave_098);
				return;
			}
			if (!FaeryManager.getInstance().getKhatamMap().containsKey(player.getId())) {
				player.sendError(Translate.text_cave_008);
				return;
			}

			if (FaeryManager.logger.isWarnEnabled()) {
				FaeryManager.logger.warn(player.getLogString() + "[解封仙府]");
			}
			cave = FaeryManager.caveEm.find(player.getCaveId());

			if (cave == null) {
				player.sendError(Translate.text_cave_007);
				if (FaeryManager.logger.isWarnEnabled()) {
					FaeryManager.logger.warn(player.getLogString() + "[解封仙府] [仙府不存在] [仙府ID:{}]", new Object[] { player.getCaveId() });
				}
				return;
			}
			// 查看所有仙府 如果有这个ID的仙府,则置零//一定在重新分配之前做
			List<Faery> faerys = FaeryManager.getInstance().getCountryMap().get(Integer.valueOf(player.getCountry()));
			if (faerys != null) {
				for (int i = 0; i < faerys.size(); i++) {
					Faery faery = faerys.get(i);
					for (int j = 0; j < faery.getCaveIds().length; j++) {
						if (faery.getCaveIds()[j] != null && faery.getCaveIds()[j] == cave.getId()) {
							faery.getCaveIds()[j] = 0L;
							FaeryManager.faeryEm.notifyFieldChange(faery, "caveIds");
							if (CaveSubSystem.logger.isWarnEnabled()) {
								CaveSubSystem.logger.warn(player.getLogString() + "[解封仙府] [把原来记录的记录为0] [原来的FaeryId:{}] [原来的位置:{}]", new Object[] { faery.getId(), j });
							}
						}
					}
				}
			}
			CompoundReturn cr = cave.notifyCave();
			if (cr.getBooleanValue()) {

				Object obj = cr.getObjValue();
				if (obj != null) {
					CavePosition position = (CavePosition) obj;
					// 是否要通知玩家新的位置????
					Faery faery = FaeryManager.getInstance().getFaery(position.getFaeryId());
					if (faery == null) {
						FaeryManager.logger.warn(player.getLogString() + "[解封仙府] [异常] [仙府不存在:{}]", new Object[] { position.toString() });
						return;
					}

					if (FaeryManager.logger.isWarnEnabled()) {
						FaeryManager.logger.warn(player.getLogString() + "[解封仙府] [成功] [新位置:{}] [田地个数A:{}]", new Object[] { position.toString(), cave.getFields().size() });
					}

					int index = position.getIndex();
					CaveMainBuilding main = cave.getMainBuilding();
					Point point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_MAIN);
					main.setPoint(point);
					if (main.getSchedules() != null) {
						main.getSchedules().clear();
					}
					cave.notifyFieldChange(main.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_FENCE);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FENCE);
					}
					CaveFence fence = cave.getFence();
					fence.setStatus(FENCE_STATUS_CLOSE);
					fence.setPoint(point);
					if (fence.getSchedules() != null) {
						fence.getSchedules().clear();
					}
					cave.notifyFieldChange(fence.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_PETHOUSE);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_PETHOUSE);
					}
					CavePethouse pethouse = cave.getPethouse();
					pethouse.setPoint(point);
					cave.setPethouse(pethouse);
					if (pethouse.getSchedules() != null) {
						pethouse.getSchedules().clear();
					}
					cave.notifyFieldChange(pethouse.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_DOORPLATE);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_DOORPLATE);
					}
					CaveDoorplate doorplate = cave.getDoorplate();
					doorplate.setPoint(point);
					if (doorplate.getSchedules() != null) {
						doorplate.getSchedules().clear();
					}
					cave.notifyFieldChange(doorplate.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_STORE);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_STORE);
					}
					CaveStorehouse storehouse = cave.getStorehouse();
					storehouse.setPoint(point);
					if (storehouse.getSchedules() != null) {
						storehouse.getSchedules().clear();
					}
					cave.notifyFieldChange(storehouse.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_FIELD1);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD1);
					}

					CaveField caveField1 = cave.getFields().get(0);
					caveField1.setPoint(point);
					caveField1.setPlantStatus(null);
					caveField1.setCaveFieldBombData(null);
					if (caveField1.getSchedules() != null) {
						caveField1.getSchedules().clear();
					}
					cave.notifyFieldChange(caveField1.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_FIELD2);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD2);
					}

					CaveField caveField2 = cave.getFields().get(1);
					caveField2.setPoint(point);
					caveField2.setPlantStatus(null);
					caveField2.setCaveFieldBombData(null);
					if (caveField2.getSchedules() != null) {
						caveField2.getSchedules().clear();
					}
					cave.notifyFieldChange(caveField2.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_FIELD3);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD3);
					}

					CaveField caveField3 = cave.getFields().get(2);
					caveField3.setPoint(point);
					caveField3.setPlantStatus(null);
					caveField3.setCaveFieldBombData(null);
					if (caveField3.getSchedules() != null) {
						caveField3.getSchedules().clear();
					}
					cave.notifyFieldChange(caveField3.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_FIELD4);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD4);
					}

					CaveField caveField4 = cave.getFields().get(3);
					caveField4.setPoint(point);
					caveField4.setPlantStatus(null);
					caveField4.setCaveFieldBombData(null);
					if (caveField4.getSchedules() != null) {
						caveField4.getSchedules().clear();
					}
					cave.notifyFieldChange(caveField4.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_FIELD5);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD5);
					}

					CaveField caveField5 = cave.getFields().get(4);
					caveField5.setPoint(point);
					caveField5.setPlantStatus(null);
					caveField5.setCaveFieldBombData(null);
					if (caveField5.getSchedules() != null) {
						caveField5.getSchedules().clear();
					}
					cave.notifyFieldChange(caveField5.getType());

					point = FaeryManager.getInstance().getPoint(index, CAVE_BUILDING_TYPE_FIELD6);
					if (point == null) {
						throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD6);
					}

					CaveField caveField6 = cave.getFields().get(5);
					caveField6.setPoint(point);
					caveField6.setPlantStatus(null);
					caveField6.setCaveFieldBombData(null);
					if (caveField6.getSchedules() != null) {
						caveField6.getSchedules().clear();
					}
					cave.notifyFieldChange(caveField6.getType());

					// ----------------------------------------------------------------
					cave.setFaery(faery);
					cave.setIndex(index);

					cave.onLoadInitNpc();

					player.setFaeryId(faery.getId());
					player.setCaveId(cave.getId());

					faery.getCaveIds()[position.getIndex()] = cave.getId();
					faery.getCaves()[position.getIndex()] = cave;

					FaeryManager.faeryEm.notifyFieldChange(faery, "caveIds");
					if (cave.getSchedules() != null) {
						cave.getSchedules().clear();
					}

					player.sendError(Translate.translateString(Translate.text_cave_096, new String[][] { { Translate.STRING_1, faery.getName() } }));

					if (FaeryManager.logger.isWarnEnabled()) {
						FaeryManager.logger.warn(player.getLogString() + "[解封仙府] [成功] [新位置:{}] [田地个数B:{}]", new Object[] { position.toString(), cave.getFields().size() });
					}
				} else {
					if (FaeryManager.logger.isWarnEnabled()) {
						FaeryManager.logger.warn(player.getLogString() + "[解封仙府] [异常] [没有找到新的对应位置]");
					}
				}
				FaeryManager.getInstance().noticeDynamic(player);
				player.addMessageToRightBag(FaeryManager.getInstance().caveSimple(player,null));
			} else {
				int errorCode = cr.getIntValue();
				if (errorCode == 1) {
					player.sendError(Translate.text_cave_008);
				} else if (errorCode == 2) {
					player.sendError(Translate.text_cave_009);
				}
			}
		} catch (Throwable e) {
			FaeryManager.logger.error("[仙府解封异常]", e);
		}

	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		// Cave cave = FaeryManager.getInstance().getCave(player);
		// if (cave == null) {
		// return false;
		// }
		// if (cave.getStatus() != FaeryConfig.CAVE_STATUS_KHATAM) {
		// return false;
		// }
		return FaeryManager.getInstance().getKhatamMap().containsKey(player.getId());
	}
}
