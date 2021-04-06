package com.fy.engineserver.menu.transitrobbery;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.TransitRobbery.model.TempGuanliAvata;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_FEISHENG_DONGHUA_REQ;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;

public class Option_Confirm_FeiSheng extends Option{

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		TransitRobberyManager.logger.info("[受到玩家飞升请求][" + player.getLogString() + "]");
		byte[] avatarType = player.getAvataType();
		String[] avatar = player.getAvata();
		String str = TransitRobberyEntityManager.getInstance().feiSheng(player);
		TransitRobberyManager.logger.info("[受到玩家飞升请求][" + player.getLogString() + "][飞升判断结果=" + str + "]");
		if(str == null){
//			player.sendError(Translate.飞升成功);
			String feishengjilu = player.getCareer() + "," + player.getName() + "," + player.getCountry() + "," + player.getZongPaiName() + "," + player.getJiazuName();
			byte[] tempatype = Arrays.copyOf(avatarType,avatarType.length);
			String[] tempar = Arrays.copyOf(avatar,avatar.length);
			TempGuanliAvata tt = new TempGuanliAvata(player.getId(), feishengjilu, tempar, tempatype);
			TransitRobberyManager.getInstance().tempFeishen.put(player.getId(), tt);
			if (TransitRobberyManager.logger.isInfoEnabled()) {
				TransitRobberyManager.logger.info("[飞升] [记录飞升时avata] [" + tt + "]");
			}
			
			String[] newAvatar = null;
			byte[] newAvatarType = RobberyConstant.avatarType;
			switch (player.getCareer()) {
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
			case 5 :
				newAvatar = RobberyConstant.shoukuiAvatar;
				break;
			}
			boolean existChiBang = false;
			for(byte bb : player.getAvataType()){
				if(bb == 3){
					existChiBang = true;
					break;
				}
			}
			if(!existChiBang){
				newAvatarType = ArrayUtils.remove(newAvatarType, newAvatarType.length-1);
				newAvatar = (String[]) ArrayUtils.remove(newAvatar, newAvatar.length-1);
			}
			if (player.isShouStatus()) {
				newAvatarType = new byte[]{0,13};
				newAvatar = Player.bianshenAvata4Feisheng[player.getPlayerRank()];
			}
			NOTIFY_FEISHENG_DONGHUA_REQ resp = new NOTIFY_FEISHENG_DONGHUA_REQ(GameMessageFactory.nextSequnceNum(), (byte)1, player.getCareer(), player.getName(), player.getCountry()
					,player.getZongPaiName(), player.getJiazuName(), avatarType, avatar, newAvatarType, newAvatar);
			player.addMessageToRightBag(resp);
			doPopShowFeisheng(player);
			Horse h2 = null;
			for (long horseId : player.getCurrSoul().getHorseArr()) {
				 Horse h = HorseManager.getInstance().getHorseById(horseId, player);
				 if (h != null && !h.isFly()) {
					 h2 = h;
					 break;
				 }
			}
			if (h2 != null) {
				ResourceManager.getInstance().getHorseAvataForPlayer(h2, player);
			}
		} else {
			player.sendError(str);
		}
	}
	
	private void doPopShowFeisheng(Player player){
		TempGuanliAvata tt = TransitRobberyManager.getInstance().tempFeishen.get(player.getId());
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		String[] temp = tt.feishengjilu.split(",");
		if(temp.length < 3){
			return;
		}
//		byte career = Byte.parseByte(temp[0]);
		String name = temp[1];
		String country = CountryManager.得到国家名(Integer.parseInt(temp[2]));
		String msg = Translate.translateString(Translate.飞升推送, new String[][] {{Translate.STRING_1, country}, {Translate.STRING_2, name}});
		mw.setDescriptionInUUB(msg);
		Option_Confirm_guanli option1 = new Option_Confirm_guanli();
		option1.setFeishengPlayerId(player.getId());
		option1.setText(MinigameConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		for(int i=0; i<players.length; i++){
			try{
				if(players[i].getId() == player.getId()){
					continue;
				}
				if(!check4GuanLi(players[i])) {
					continue;
				}
				if (players[i].getLevel() > 220) {
					continue;
				}
				players[i].addMessageToRightBag(creq);
			}catch(Exception e) {
				TransitRobberyManager.logger.error("[观礼推送弹框出错]{}{}",players[i].getLogString(),e);
			}
		}
	}
	
	private boolean check4GuanLi(Player player) {
		boolean result = true;
		if(TransitRobberyEntityManager.getInstance().isPlayerInRobbery(player.getId())) {		//渡劫
			result = false;
		} else{
			Game game = player.getCurrentGame();
			if(game == null || QianCengTaManager.getInstance().isInQianCengTanGame(game.gi.name)) {				//千层塔
				result = false;
			} else if(player.duelFieldState == 1) {																//比武
				result = false;
			}
		}
		return result;
	}
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

}
