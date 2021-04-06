package com.fy.engineserver.menu.guozhan;

import java.util.Date;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.guozhan.DeclareWar;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.guozhan.exception.CountryAlreadyDeclareWarException;
import com.fy.engineserver.guozhan.exception.CountryCanNotBeDeclareWarException;
import com.fy.engineserver.guozhan.exception.DeclareWarTimeErrorException;
import com.fy.engineserver.guozhan.exception.OperationNotPermitException;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.xuanzhi.tools.text.DateUtil;
/**
 * 申请国战
 * 
 * 
 *
 */
public class Option_Guozhan_DeclareCountryList extends Option{

	private byte countryId;
	
	public Option_Guozhan_DeclareCountryList(byte countryId) {
		this.countryId = countryId;
	}
	
	/**
	 * 选择宣战国
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(UnitServerFunctionManager.needCloseFunctuin(Function.国战)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
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
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.今日已宣战, new String[][]{{"@country@", cm.得到国家名(dw.getEnemyCountryId())}}));
			player.addMessageToRightBag(hintreq);
			return;
		}
		String error = null;
		try {
			dw = org.declareWar(player, this.countryId);
			String enemyCountryName = CountryManager.得到国家名(dw.getEnemyCountryId());
			String content = Translate.translateString(Translate.宣战成功, new String[][]{{"@country@", enemyCountryName}, {"@time@", DateUtil.formatDate(new Date(dw.getStartFightTime()), Translate.text_年月日时分)}});
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, content);
			player.addMessageToRightBag(hintreq);
		} catch (OperationNotPermitException e) {
			// TODO Auto-generated catch block
			GuozhanOrganizer.logger.error("[宣战错误] ["+player.getLogString()+"]", e);
			error = Translate.您的职位不够;
		} catch (CountryCanNotBeDeclareWarException e) {
			// TODO Auto-generated catch block
			GuozhanOrganizer.logger.error("[宣战错误] ["+player.getLogString()+"]", e);
			error = Translate.不能再被宣战;
		} catch (DeclareWarTimeErrorException e) {
			// TODO Auto-generated catch block
			GuozhanOrganizer.logger.error("[宣战错误] ["+player.getLogString()+"]", e);
			error = Translate.translateString(Translate.宣战时间, new String[][]{{"@startTime@", org.getConstants().开始宣战时间},{"@endTime@", org.getConstants().结束宣战时间}});
		} catch (CountryAlreadyDeclareWarException e) {
			// TODO Auto-generated catch block
			GuozhanOrganizer.logger.error("[宣战错误] ["+player.getLogString()+"]", e);
			error = Translate.本国今日已经宣战;
		}
		if(error != null) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, error);
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
