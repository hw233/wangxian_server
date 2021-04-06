package com.fy.engineserver.menu;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.message.CONFIRM_MAGICWEAPON_EAT_REQ;
import com.fy.engineserver.message.JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ;
import com.fy.engineserver.message.JIHUO_MAGICWEAPON_HIDDLE_PROP_RES;
import com.fy.engineserver.message.REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ;
import com.fy.engineserver.message.REFRESH_MAGICWEAPON_HIDDLE_PROP_RES;
import com.fy.engineserver.sprite.Player;

public class Option_MagicWeapon_Eat_Sure extends Option{

	long id;
	CONFIRM_MAGICWEAPON_EAT_REQ req;
	JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ req2;
	REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ req3;
	int type;//1：激活
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CONFIRM_MAGICWEAPON_EAT_REQ getReq() {
		return req;
	}

	public void setReq(CONFIRM_MAGICWEAPON_EAT_REQ req) {
		this.req = req;
	}

	public JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ getReq2() {
		return req2;
	}

	public void setReq2(JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ req2) {
		this.req2 = req2;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ getReq3() {
		return req3;
	}

	public void setReq3(REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ req3) {
		this.req3 = req3;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(type==1){
			try {
				JIHUO_MAGICWEAPON_HIDDLE_PROP_RES res = MagicWeaponManager.instance.jihuoMagicWeapon(player, req2, true);
				player.addMessageToRightBag(res);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(req!=null){
			MagicWeaponManager.instance.issure(player, req);
		}else if(req3!=null){
			try {
				REFRESH_MAGICWEAPON_HIDDLE_PROP_RES res = MagicWeaponManager.instance.refrshMagicWeaponProp(player, req3,true);
				player.addMessageToRightBag(res);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
