package com.fy.engineserver.menu.society.zongpai;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CREATE_ZONGPAI_SUCCESS_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.zongzu.ZongPaiSubSystem;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;

/**
 * 
 * @author Administrator
 *
 */
public class Option_Zongpai_Create_Confirm extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	private String zongPaiName;
	private String password;
	private String password2;
	private String passwordHint;
	private String declaration;

	
	@Override
	public void doSelect(Game game, Player player) {
		String result = "";
		//合服打折活动
		CompoundReturn cr = ActivityManagers.getInstance().getValue(1, player);
		if(cr!=null && cr.getBooleanValue()){
			ZongPaiManager.创建宗派花费 = cr.getIntValue();
		}
		//
		result = ZongPaiManager.getInstance().创建宗派合法性判断(player);
		if(result == null){
			return ;
		}
		if("".equals(result)){
			result = ZongPaiManager.getInstance().zongPaiNameValidate(zongPaiName, declaration);
			if("".equals(result)){
				if(!password.equals(password2)){
					result = Translate.俩次密码不一样;
				}else if(password.getBytes().length>ZongPaiManager.ZONGPAI_PASSWROD_MAX || password.getBytes().length < ZongPaiManager.ZONGPAI_PASSWROD_MIN ){
					result = Translate.密码长度不符;
				}else if(passwordHint.getBytes().length > ZongPaiManager.ZONGPAI_PASSWRODHINT_NUM){
					result = Translate.密码提示太长;
				}else{
					//创建宗派
					//扣钱
					BillingCenter bc = BillingCenter.getInstance();
					try{
						bc.playerExpense(player, ZongPaiManager.创建宗派花费, CurrencyType.YINZI, ExpenseReasonType.创建宗派, "创建宗派");
					}catch(Exception ex){
						result = Translate.扣费失败;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
						player.addMessageToRightBag(hreq);
						ZongPaiManager.logger.error("[创建宗派扣费失败] ["+player.getLogString()+"] ["+ZongPaiManager.创建宗派花费+"]",ex);
						return;
					}
					
					long id = 0;
					try {
						id = ZongPaiManager.em.nextId();
					} catch (Exception e) {
						ZongPaiManager.logger.error("[宗派生成id错误] ["+player.getLogString()+"]",e);
						return ;
					}
					ZongPai zp = new ZongPai(id);
					zp.setPassword(password);
					zp.setPassword2(password2);
					zp.setPasswordHint(passwordHint);
					zp.setCreateTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					zp.setLevel(0);
					zp.setZpname(zongPaiName);
					zp.setDeclaration(declaration);
					zp.getJiazuIds().add(player.getJiazuId());
					zp.setMasterId(player.getId());
					
					Jiazu jz = JiazuManager.getInstance().getJiazu(player.getJiazuId());
					if(jz != null){
						jz.setZongPaiId(zp.getId());
						ZongPaiManager.getInstance().createZongPai(player,zp);
						
						ZongPaiManager.getInstance().setAllZongPaiName(jz,zongPaiName);
						
//						恭喜<创建者姓名>成功建立<宗派名称>，正式开宗立派广收门徒！（国家）
						String des = Translate.translateString(Translate.恭喜创建者姓名成功建立宗派名称正式开宗立派广收门徒, new String[][]{{Translate.PLAYER_NAME_1,player.getName()},{Translate.STRING_1,zp.getZpname()}});
						
						Country country = CountryManager.getInstance().getCountryMap().get(player.getCountry());
						ChatMessageService cms = ChatMessageService.getInstance();
						try {
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(des);
							if(country != null){
								cms.sendMessageToCountry(country.getCountryId(), msg);
								if (ZongPaiManager.logger.isWarnEnabled()){
									ZongPaiManager.logger.warn("[创建宗派发送广播] ["+player.getLogString()+"] ["+zp.getZpname()+"]");
								}
							}
							
						} catch (Exception ex) {
							ZongPaiManager.logger.error("[创建宗派发送广播异常] ["+player.getLogString()+"] ["+zp.getZpname()+"]",ex);
						}
						if(ZongPaiSubSystem.logger.isWarnEnabled()){
							ZongPaiSubSystem.logger.warn("[创建宗派成功] ["+player.getLogString()+"] ["+zp.getLogString()+"]");
						}
						player.send_HINT_REQ(Translate.translateString(Translate.创建xx宗派成功, new String[][]{{Translate.STRING_1,zp.getZpname()}}));
						CREATE_ZONGPAI_SUCCESS_REQ res = new CREATE_ZONGPAI_SUCCESS_REQ(GameMessageFactory.nextSequnceNum());
						player.addMessageToRightBag(res);
					}
				}
			}else{
				if(ZongPaiManager.logger.isWarnEnabled()){
					ZongPaiManager.logger.warn("[创建宗派错误] ["+player.getLogString()+"] ["+result+"]");
				}
			}
		}else{
			if(ZongPaiManager.logger.isWarnEnabled()){
				ZongPaiManager.logger.warn("[创建宗派错误] ["+player.getLogString()+"] ["+result+"]");
			}
		}
		
	}
	public String getZongPaiName() {
		return zongPaiName;
	}
	public void setZongPaiName(String zongPaiName) {
		this.zongPaiName = zongPaiName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getPasswordHint() {
		return passwordHint;
	}
	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}
	public String getDeclaration() {
		return declaration;
	}
	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	
	
}
