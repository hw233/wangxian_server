package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.data.WangZheTransferPointOnMap;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Country_wangzhezhiyin;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.petdao.PetDaoManager;


/**
 * 
 * 王者之印道具
 * 
 */
public class WangZheZhiYinProps extends Props{
	
	@Override
	public boolean isUsedUndisappear() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 使用传送符。
	 * 1，判断传送目标地图是不是玩家当前所在地图。如果是，发送SET_POSITION_REQ指令
	 * 2，如果不是，直接把玩家从当前地图中退出，设置跳转点，发送CHANGE_GAME_REQ指令
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		try{
			if(!super.use(game,player,ae)){
				return false;
			}
			WindowManager mm = WindowManager.getInstance();
			MenuWindow mw = mm.createTempMenuWindow(600);
			mw.setTitle(Translate.使用王者之印);
			List<Option> optionList = new ArrayList<Option>();
			Option cancelOption = new Option_UseCancel();
			cancelOption.setColor(0xFFFFFF);
			cancelOption.setText(Translate.取消);
			CountryManager cm = CountryManager.getInstance();
			Hashtable<Long,Integer> map = cm.data.使用王者之印的次数Map;
			int count = 0;
			if(map.get(player.getId()) != null){
				count = map.get(player.getId());
			}
			String description = Translate.使用王者之印;
			int money = cm.使用王者之印花费(count);
			description = Translate.translateString(Translate.使用王者之印次数, new String[][]{{Translate.COUNT_1,(count+1)+""},{Translate.COUNT_2,BillingCenter.得到带单位的银两(money)}});
			mw.setDescriptionInUUB(description);
			HashMap<String, WangZheTransferPointOnMap> chinaMapNameTransferPoints = cm.chinaMapNameTransferPoints2.get(new Integer(player.getCountry()));
			for(String gameName : chinaMapNameTransferPoints.keySet()){
				Option_Country_wangzhezhiyin mapOption = new Option_Country_wangzhezhiyin();
				mapOption.setMapName(gameName);
				mapOption.setText(gameName);
				optionList.add(mapOption);
			}
			optionList.add(cancelOption);
			mw.setOptions(optionList.toArray(new Option[0]));
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
			player.addMessageToRightBag(res);
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {
		
		String resultStr = super.canUse(p);
		if(resultStr == null){
			CountryManager cm = CountryManager.getInstance();
			Game game = p.getCurrentGame();
			if(game == null){
				return Translate.不在地图不能使用;
			}
			int 官职 = cm.官职(p.getCountry(), p.getId());
			int[] 可以使用王者的官职 = CountryManager.拥有使用王者之令权利的官员;
			boolean canUse = false;
			for(int guanzhi : 可以使用王者的官职){
				if(guanzhi == 官职){
					canUse = true;
					break;
				}
			}
			if(!canUse){
				return Translate.您没有权限使用;
			}
			if(game.country != p.getCountry()){
				return Translate.只能在本国地图使用;
			}
			if(!cm.玩家是否可以传送(p)){
				return Translate.您目前状态不能使用王者之印;
			}
			PetDaoManager pdm = PetDaoManager.getInstance();
			
			if(pdm.isPetDao(game.gi.name)){
				return Translate.圣兽阁中不能使用混元至圣;
			}
			try{
//				if(JJCManager.isJJCBattle(game.gi.name)){
//					return Translate.战场中不能使用混元至圣;
//				}
			}catch(Exception e){
				e.printStackTrace();
			}
			if(SealManager.getInstance().isSealDownCity(game.gi.name)){
				return Translate.封印副本中不能使用混元至圣;
			}
		}
		return resultStr;
	}
}
