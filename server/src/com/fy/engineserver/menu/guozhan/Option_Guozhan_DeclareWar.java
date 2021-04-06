package com.fy.engineserver.menu.guozhan;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.guozhan.DeclareWar;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.xuanzhi.tools.text.StringUtil;
/**
 * 申请国战
 * 
 * 
 *
 */
public class Option_Guozhan_DeclareWar extends Option{

	/**
	 * 处理申请国战，当职位满足条件时，返回当日可被宣战的国家列表
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(UnitServerFunctionManager.needCloseFunctuin(Function.国战)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
		long start = System.currentTimeMillis();
		CountryManager cm = CountryManager.getInstance();
		int title = cm.官职(player.getCountry(), player.getId());
		if(title != CountryManager.国王 && title != CountryManager.元帅 && title != CountryManager.大将军) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您的职位不够);
			player.addMessageToRightBag(hintreq);
			return;
		}
		GuozhanOrganizer org = GuozhanOrganizer.getInstance();
		DeclareWar dw = org.getCountryDeclareWar(player.getCountry());
		if(dw != null) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.本国今日已经宣战);
			player.addMessageToRightBag(hintreq);
			return;
		}
		List<Country> clist = GuozhanOrganizer.getInstance().getCanBeDeclareWarCountryList(player.getCountry());
		if(clist != null && clist.size() > 0) {
			Option ops[] = new Option[clist.size()];
			for(int i=0; i<clist.size(); i++) {
				Option_Guozhan_DeclareCountryList oc = new Option_Guozhan_DeclareCountryList(clist.get(i).getCountryId());
				ops[i] = oc;
				String text = cm.得到国家名(clist.get(i).getCountryId());
				oc.setText(text);
				oc.setOptionId(StringUtil.randomString(10));
				oc.setColor(0xffffff);
			}
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(60 * 2);
			mw.setTitle(Translate.请选择宣战国家);
			mw.setOptions(ops);
			mw.setDescriptionInUUB(Translate.请选择您要宣战的国家);
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
			player.addMessageToRightBag(res);
			if(GuozhanOrganizer.logger.isDebugEnabled())
				GuozhanOrganizer.logger.debug("[打开宣战国家列表] ["+player.getLogString()+"] [可宣战数量:"+clist.size()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		} else {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, "今日没有可以宣战的国家");
			player.addMessageToRightBag(hintreq);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}
}
