package com.fy.engineserver.menu.transitrobbery;

import org.apache.commons.lang.ArrayUtils;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.TransitRobbery.model.TempGuanliAvata;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_FEISHENG_DONGHUA_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

public class Option_Confirm_guanli extends Option{
	
	private long feishengPlayerId;

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		if(!player.isOnline()){
			return;
		}
		if (player.getLevel() > 220) {
			return ;
		}
		TempGuanliAvata tt = TransitRobberyManager.getInstance().tempFeishen.get(feishengPlayerId);
		String[] temp = tt.feishengjilu.split(",");
		if(temp.length < 3){
			TransitRobberyManager.logger.info("数据不对：" + temp.length);
			return;
		}
		byte career = Byte.parseByte(temp[0]);
		String name = temp[1];
		byte country = Byte.parseByte(temp[2]);
		String zunpai = "";
		String jiazu = "";
		if(temp.length >= 5){
			zunpai = temp[3];
			jiazu = temp[4];
		}
		
		String[] avatar = tt.avatar;
		byte[] avatarType = tt.avatarType;
		try {
			Player feisheng = GamePlayerManager.getInstance().getPlayer(name);
			if(!TransitRobberyManager.getInstance().getFeishengExpAward(feisheng.getId(), player)){
				player.sendError(Translate.重复观礼飞升);
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			TransitRobberyManager.logger.error("飞升观礼获取player异常,name=" + name , e);
			return;
		}
		byte[] newAvatarType = RobberyConstant.avatarType;
		String[] newAvatar = null;
		switch (career) {
		case 1: 		//修罗
			newAvatar = RobberyConstant.douluoAvatar;
			break;			
		case 3: 		//仙心
			newAvatar = RobberyConstant.lingzunAvatar;
			break;			
		case 4: 		//武皇
			newAvatar = RobberyConstant.wuhuangAvatar;
			break;			
		case 2: 		//鬼杀
			newAvatar = RobberyConstant.guishaAvatar;
			break;	
		case 5:
			newAvatar = RobberyConstant.shoukuiAvatar;
			break;
		}
		boolean existChiBang = false;
		for(byte bb : avatarType){
			if(bb == 3){
				existChiBang = true;
				break;
			}
		}
		if(!existChiBang){
			newAvatarType = ArrayUtils.remove(newAvatarType, newAvatarType.length-1);
			newAvatar = (String[]) ArrayUtils.remove(newAvatar, newAvatar.length-1);
		}
		boolean isShou = false;
		int rank = 0;
		if (avatar.length == 1) {
			for (int i=0; i<Player.bianshenAvata.length; i++) {
				if (Player.bianshenAvata[i][0].equals(avatar[0])) {
					rank = i;
					isShou = true;
					break;
				}
			}
		}
		if (isShou) {
			newAvatarType = new byte[]{0,13};
			newAvatar = Player.bianshenAvata4Feisheng[rank];
		}
		
		NOTIFY_FEISHENG_DONGHUA_REQ resp = new NOTIFY_FEISHENG_DONGHUA_REQ(GameMessageFactory.nextSequnceNum(), (byte)2, career, name, country
				,zunpai, jiazu, avatarType, avatar, newAvatarType, newAvatar);
		player.addMessageToRightBag(resp);
		TransitRobberyManager.getInstance().confirmGuanLi(player.getId());
		if (TransitRobberyManager.logger.isInfoEnabled()) {
			TransitRobberyManager.logger.info("[确认观礼] [成功] [" + player.getLogString() + "] [" + tt + "]");
		}
	}

	public long getFeishengPlayerId() {
		return feishengPlayerId;
	}

	public void setFeishengPlayerId(long feishengPlayerId) {
		this.feishengPlayerId = feishengPlayerId;
	}
	
}
