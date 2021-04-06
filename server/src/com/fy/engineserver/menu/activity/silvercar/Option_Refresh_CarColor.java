package com.fy.engineserver.menu.activity.silvercar;

import com.fy.engineserver.activity.silvercar.SilvercarManager;
import com.fy.engineserver.activity.silvercar.SilvercarRefreshTimebar;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.sprite.npc.BiaoCheNpc;
import com.fy.engineserver.sprite.npc.FollowableNPC;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;

/**
 * 刷新镖车的菜单。初始化时要设置挂在哪个NPC身上
 * 
 * 
 */
public class Option_Refresh_CarColor extends Option implements NeedRecordNPC, NeedCheckPurview,
		Cloneable {

	private NPC npc;

	public Option_Refresh_CarColor() {

	}

	/**
	 * 必须加这个方法
	 */
	public Option copy(Game game, Player p) {
		Option_Refresh_CarColor o = new Option_Refresh_CarColor();
		o.setOptionId(this.getOptionId());
		o.setIconId(this.getIconId());
		o.setColor(this.getColor());
		o.setText(this.getText());
		o.setOId(this.getOId());
		
		return o;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		try {
			FollowableNPC followableNPC = player.getFollowableNPC();
			if (followableNPC == null) {
				player.sendError(Translate.text_silverCar_010);
				return;
			}

			if (!(followableNPC instanceof BiaoCheNpc)) {
				player.sendError(Translate.text_silverCar_010);
				return;
			}
			int oldColor = followableNPC.getGrade();

			if (oldColor == -1) {
				player.sendError(Translate.text_silverCar_011);
				return;
			}
			BiaoCheNpc biaoCheNpc = (BiaoCheNpc) followableNPC;
			
			if (!BiaoCheNpc.isNear(player.getX(), player.getY(), biaoCheNpc.getX(), biaoCheNpc.getY(), biaoCheNpc.getRadius())) {
				player.sendError(Translate.text_silverCar_019);
				return;
			}
			
			if (npc instanceof BiaoCheNpc) {
				BiaoCheNpc target = (BiaoCheNpc) npc;
				if (target.getRefreshNPC().size() >= 5) {
					player.sendError(Translate.text_silverCar_020);
					return;
				}
			}
			if (npc instanceof BiaoCheNpc && (biaoCheNpc.getRefershTimes() >= getMaxrefershTimes(player))) {
				player.sendError(Translate.text_silverCar_021);
				return;
			}
			
			if(biaoCheNpc.getRefreshNPC().contains(getNPC().getId())){
				player.sendError(Translate.text_silverCar_022);
				return;
			}
			
			SilvercarRefreshTimebar silvercarRefreshTimebar = new SilvercarRefreshTimebar(player, oldColor, npc, biaoCheNpc);
			long barTime = TimeTool.TimeDistance.SECOND.getTimeMillis() * 5;
			player.getTimerTaskAgent().createTimerTask(silvercarRefreshTimebar, barTime, TimerTask.type_领取BUFF);

			NOTICE_CLIENT_READ_TIMEBAR_REQ req = new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), barTime, Translate.text_silverCar_023);
			player.addMessageToRightBag(req);
		} catch (Exception e) {
			SilvercarManager.logger.error(player.getLogString() + "[在NPC:{}加货][异常]", npc == null ? "NULL" : npc.getName(), e);
		}
	}

	private int getMaxrefershTimes(Player player) {
		return player.getVipLevel() < 4 ? 1 : 2;
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}

	@Override
	public boolean canSee(Player player) {
		FollowableNPC followableNPC = player.getFollowableNPC();
		if (TaskSubSystem.logger.isWarnEnabled()) {
			TaskSubSystem.logger.warn(player.getLogString() + "[查看中转商] [跟随NPC:" + (followableNPC == null ? "NULL" : followableNPC.getClass()) + "]");
		}
		if (followableNPC == null || !(followableNPC instanceof BiaoCheNpc)) {
			return false;
		}
		if (getNPC() instanceof BiaoCheNpc) {
			if (((BiaoCheNpc) getNPC()).getGrade() < 3) {// 镖车的话,只能是紫色以上的才显示
				return false;
			}
		}
		BiaoCheNpc biaoCheNpc = (BiaoCheNpc) player.getFollowableNPC();
		return !biaoCheNpc.getRefreshNPC().contains(getNPC().getId());
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
