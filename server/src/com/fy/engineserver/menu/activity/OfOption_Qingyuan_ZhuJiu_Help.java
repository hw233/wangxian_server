package com.fy.engineserver.menu.activity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;

public class OfOption_Qingyuan_ZhuJiu_Help extends Option{

	public OfOption_Qingyuan_ZhuJiu_Help() {
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
			String title = "<f color='0xff0000'>仙缘/论道召唤功能介绍</f>";
			String des = "1、召唤者召唤活动仙友需消耗一个仙缘论道令，国内仙缘/论道消耗1个仙缘论道令，国外仙缘/论道消耗2个仙缘论道令。\n2、点击召唤功能直接消耗对应道具。\n"
+"3、国内仙缘/论道必须在本国指定地点【凤栖梧桐】处才能使用召唤功能，国外仙缘/论道要到达活动任务中指定的国家【凤栖梧桐】处，才可以使用召唤功能。\n"
+"4、召唤【稍等】功能，点击之后缩小最小化，必须在规定的倒计时时间内进行传送，如倒计时为0，传送将被取消，召唤者的【仙缘论道令】消耗不返还。\n"
+"5、帮助召唤者完成任务的国内仙友，在完成任务后将会获得<f color='0xff0000'>1个【仙缘论道令碎片】</f>的奖励，国外仙友，在完成任务后将获得<f color='0xff0000'>2个【仙缘论道令碎片】</f>的奖励，奖励以邮件方式发送给好心人。";
			
			if(PlatformManager.getInstance().isPlatformOf(Platform.韩国)){
				title = "<f color='0xff0000'>인연/자주 소환기능 설명</f>";
				des = "1、선우를 소환하는데 빨간색 끈이 소모 됩니다.국내 인연/자주 일 경우 1개 빨간색 끈 소모，국외 인연/자주일 경우 2개 빨간색 끈 소모. \n2、소환기능을 클릭하면 직접 대응하는 아이템을 소모합니다.\n"
+"3、국내인연/자주는 반드시 본인 국가 지정한 [인연나무]에서만 소환기능을 사용가능하고，국외 인연/자주는 지정한 국가 [인연나무]에서만 소환기능 사용할 수 있습니다.\n"
+"4、소환【잠깐】기능，클릭 후 최소화되고 반드시 규정한 시간 내에 전송을 해야만 합니다. 만약 카운트가 0일경우 전송은 취소되고 소환한 수행자님의 [불근색 끈]은 소모되고 환불되지 않습니다.\n"
+"5、소환한 수행자를 도와서 퀘스트 완성한 국내 선우는 <f color='0xff0000'>1개【빨간색 끈 조각】</f>을 받을 수 있습니다.국외 도와준 선우는 퀘스트 완성 후 <f color='0xff0000'>2개【빨간색 끈 조각】</f>을 받을수 있고，보상은 메일로 발송됩니다.";
			}
			
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setDescriptionInUUB(des);
			mw.setTitle(title);
			Option_Cancel oc = new Option_Cancel();
			oc.setText(Translate.确定);
			mw.setOptions(new Option[]{oc});
			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(req);
	}
}
