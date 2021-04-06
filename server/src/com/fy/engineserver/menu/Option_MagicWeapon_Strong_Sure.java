package com.fy.engineserver.menu;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.message.MAGICWEAPON_STRONG_REQ;
import com.fy.engineserver.sprite.Player;
/**
 * 法宝强化确认
 * 
 *
 */
public class Option_MagicWeapon_Strong_Sure extends Option{

	private MAGICWEAPON_STRONG_REQ req;
	
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(req!=null){
			MagicWeaponManager.instance.magicWeaponStrong(player, req, true);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public MAGICWEAPON_STRONG_REQ getReq() {
		return this.req;
	}

	public void setReq(MAGICWEAPON_STRONG_REQ req) {
		this.req = req;
	}
	
	
}
