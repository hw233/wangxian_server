package com.fy.engineserver.menu.pet;

import java.util.ArrayList;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.article.data.props.RefreshMonsterPackage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.ENTER_PET_DAO_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.petdao.PetDao;
import com.fy.engineserver.sprite.petdao.PetDaoManager;

public class Option_Pet_Enter_MiCheng_Sure extends Option{

	private ENTER_PET_DAO_REQ req;
	
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		int michenglevel = req.getMinlevel();
		int michengtype = req.getDaotype();
		long 进入迷城花费 = PetDaoManager.getInstance().getSilverEnter(michenglevel,michengtype)*1000;
		
		if(player.getLevel()<michenglevel){
			player.sendError(Translate.进入迷城等级限制);
			return;
		}
		
		Game currgame = player.getCurrentGame();
		if(currgame!=null){
			String mapname = PetDaoManager.mapnames.get(michengtype);
			if(mapname!=null && mapname.trim().length()>0){
				ArrayList<RefreshMonsterPackage> boxminfos = PetDaoManager.packageinfos.get(new Integer(michenglevel)).get(new Integer(michengtype));
				
				PetDao pd = PetDaoManager.getInstance().createNewGame(player, mapname,michenglevel,michengtype);
				pd.getMc().setBoxnum(boxminfos.size());
				pd.getMc().setBoxovernum(boxminfos.size());
				PetDaoManager.getInstance().datas.put(player.getId(),pd);
				ArrayList<RefreshMonsterPackage> overBox = new ArrayList<RefreshMonsterPackage>();
				overBox.addAll(boxminfos);
				pd.setOverBox(overBox);
				if(pd!=null){
					//扣钱
					boolean istrans = false;
					BillingCenter bc = BillingCenter.getInstance();
					String costname = "";
					try {
						costname =  PetDaoManager.getInstance().是否有迷城体验卷(player,michengtype);
						if(costname!=null && costname.trim().length()>0){
							player.removeArticle(costname, "进入迷城删除");
							player.sendError(Translate.translateString(Translate.消耗了迷城体验卷, new String[][]{{Translate.STRING_1,costname}}));
							istrans = true;
						}else{
							costname = "否";
							if(player.getSilver()<进入迷城花费){
								player.sendError(Translate.圣兽阁余额不足);
								return;
							}
							bc.playerExpense(player, 进入迷城花费, CurrencyType.YINZI, ExpenseReasonType.进入宠物迷城, "进入宠物迷城", VipExpActivityManager.petdao_expends_activity);
							istrans = true;
						}
						ActivityManagers.getInstance().进入迷城活动(player,mapname,michengtype);
					} catch (Exception ex) {
						PetDaoManager.log.warn("[宠物迷城] [申请进入迷城] [扣费失败] [进入迷城花费:"+进入迷城花费+"] ["+player.getLogString()+"]",ex);
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.扣费失败);
						player.addMessageToRightBag(hreq);
						return;
					}
						
					if(istrans){
						int xy[] = PetDaoManager.xypiont[michengtype];
						currgame.transferGame(player, new TransportData(0, 0, 0, 0, mapname, xy[0], xy[1]));
						PetDaoManager.log.warn("[宠物迷城] [申请进入迷城] [成功] [boxminfos:"+boxminfos.size()+"] [overboxs:"+(pd.getOverBox()==null?"null":pd.getOverBox().size())+"] [进入迷城花费:"+进入迷城花费+"] [是否有体验:"+costname+"] [迷城等级："+michenglevel+"] [迷城类型："+michengtype+"] [地图:"+mapname+"] ["+player.getLogString()+"] ["+(Translate.translateString(Translate.消耗了迷城体验卷, new String[][]{{Translate.STRING_1,costname}}))+"]");
					}else{
						PetDaoManager.log.warn("[宠物迷城] [申请进入迷城] [失败] [overboxs:"+(pd.getOverBox()==null?"null":pd.getOverBox().size())+"] [进入迷城花费:"+进入迷城花费+"] [是否有体验:"+costname+"] [迷城等级："+michenglevel+"] [迷城类型："+michengtype+"] [地图:"+mapname+"] ["+player.getLogString()+"] ["+(Translate.translateString(Translate.消耗了迷城体验卷, new String[][]{{Translate.STRING_1,costname}}))+"]");
					}
				}
			}else{
				PetDaoManager.log.warn("[宠物迷城] [申请进入迷城] [失败] [原因：获得地图名出错] [michengtype:"+michengtype+"] [mapnames:"+PetDaoManager.mapnames.size()+"] ["+player.getLogString()+"]");
			}
		}else{
			PetDaoManager.log.warn("[宠物迷城] [申请进入迷城] [失败] [原因：获得game数据出错] ["+player.getLogString()+"]");
		}
	}

	public ENTER_PET_DAO_REQ getReq() {
		return req;
	}

	public void setReq(ENTER_PET_DAO_REQ req) {
		this.req = req;
	}
	
}
