package com.fy.engineserver.menu.zhaoHuiMiMa;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_QUERY_WINDOW_REQ;
import com.fy.engineserver.message.QUERY_PWD_PROTECT_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.vip.Option_VipInfo_Cancel;
import com.fy.engineserver.vip.Option_VipInfo_OK;
import com.fy.engineserver.vip.vipinfo.VipInfoRecordManager;

public class Option_SetMiBao extends Option {

	public Option_SetMiBao() {
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		String secretAnswer = player.getPassport().getSecretAnswer();
		if (secretAnswer != null && !"".equals(secretAnswer)) {
			player.sendError(Translate.您已经设置过密保不能再设置);
			return;
		} else {
			if (!CoreSubSystem.客户端密保容错) {
				String question = player.getPassport().getSecretQuestion();
				question = question == null ? "" : question;
				QUERY_PWD_PROTECT_RES res = new QUERY_PWD_PROTECT_RES(GameMessageFactory.nextSequnceNum(), question, CoreSubSystem.allQuestion);
				player.addMessageToRightBag(res);
			} else {
				WindowManager wm = WindowManager.getInstance();
				MenuWindow mw = wm.createTempMenuWindow(600);
				//创建一个确认按钮和取消按钮
				Option_VipInfo_OK optionOk = new Option_VipInfo_OK();
				optionOk.setOType(Option.OPTION_TYPE_SERVER_FUNCTION);
				Option_VipInfo_Cancel optionCancel = new Option_VipInfo_Cancel();
				optionCancel.setOType(Option.OPTION_TYPE_SERVER_FUNCTION);
				optionOk.setText(com.fy.engineserver.datasource.language.Translate.确定);
				optionCancel.setText(com.fy.engineserver.datasource.language.Translate.取消);
				mw.setOptions(new Option[]{optionOk,optionCancel});
				
				String title = "密保设置";
				int index = player.random.nextInt(CoreSubSystem.allQuestion.length - 1);
				
				String desc = CoreSubSystem.allQuestion[index];
				String[] inputTitles = new String[]{"输入答案:"};
				byte[] inputTypes = new byte[]{1};
				byte[] maxLength = new byte[]{20};
				String[] defaultContent = new String[]{""};
				byte[] png = new byte[]{0};
				
				VipInfoRecordManager.tempCache.put(player.getId(), desc);
				
				NEW_QUERY_WINDOW_REQ req = new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),1, title,desc,inputTitles,inputTypes,maxLength,defaultContent,png,mw.getOptions());
				player.addMessageToRightBag(req);
			}
		}
	}
}
