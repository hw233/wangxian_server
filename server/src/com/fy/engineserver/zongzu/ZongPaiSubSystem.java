package com.fy.engineserver.zongzu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.society.zongpai.Option_Zongpai_Create_Confirm;
import com.fy.engineserver.message.ADD_ZONGPAI_REQ;
import com.fy.engineserver.message.CREATE_ZONGPAI_APPLY_RES;
import com.fy.engineserver.message.CREATE_ZONGPAI_CHECK_NAME_REQ;
import com.fy.engineserver.message.CREATE_ZONGPAI_CHECK_NAME_RES;
import com.fy.engineserver.message.CREATE_ZONGPAI_INPUT_PASSWORD_REQ;
import com.fy.engineserver.message.DEMISE_ZONGPAI_REQ;
import com.fy.engineserver.message.DISMISS_ZONGPAI_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.KICK_ZONGPAI_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.UPDATE_ZONGPAI_DECLARATION_REQ;
import com.fy.engineserver.message.UPDATE_ZONGPAI_DECLARATION_RES;
import com.fy.engineserver.message.ZONGPAI_INFO_RES;
import com.fy.engineserver.message.ZONGZHU_CHECK_REQ;
import com.fy.engineserver.message.ZONGZHU_CHECK_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class ZongPaiSubSystem extends SubSystemAdapter  {
	
	public	static Logger logger = LoggerFactory.getLogger(ZongPaiManager.class);
	protected PlayerManager playerManager;
	private ZongPaiManager zongPaiManager;

	public void setZongPaiManager(ZongPaiManager zongPaiManager) {
		this.zongPaiManager = zongPaiManager;
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{
				"CREATE_ZONGPAI_APPLY_REQ","DISMISS_ZONGPAI_REQ","ZONGZHU_CHECK_REQ","UPDATE_ZONGPAI_DECLARATION_REQ",
				"KICK_ZONGPAI_REQ","DEMISE_ZONGPAI_REQ","LEAVE_ZONGPAI_APPLY_REQ","ADD_ZONGPAI_REQ","QUERY_ZONGPAI_REQ",
				"CREATE_ZONGPAI_CHECK_NAME_REQ","CREATE_ZONGPAI_INPUT_PASSWORD_REQ"
		};
	}

	@Override
	public String getName() {
		return "ZongPaiSubSystem";
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}
		try {
			Class clazz = this.getClass();
			Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
			ResponseMessage res = null;
			m1.setAccessible(true);
			res = (ResponseMessage) m1.invoke(this, conn, message, player);
			return res;
		} catch (InvocationTargetException e) {
			// TODO: handle exception
			e.getTargetException().printStackTrace();
			throw e;
		}
	
	}

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}
	
	
	/**
	 * 创建宗派申请
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CREATE_ZONGPAI_APPLY_REQ(Connection conn,RequestMessage message, Player player) {
		
		String result = this.zongPaiManager.创建宗派合法性判断(player);
		if(result != null){
			CREATE_ZONGPAI_APPLY_RES res = new CREATE_ZONGPAI_APPLY_RES(message.getSequnceNum(), result);
			return res;
		}
		return null;
		
	}
	
	/**
	 * 创建宗派宗派名验证
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CREATE_ZONGPAI_CHECK_NAME_REQ(Connection conn,RequestMessage message, Player player) {
		
		String result = this.zongPaiManager.创建宗派合法性判断(player);
		if(result == null){
			return null;
		}
		CREATE_ZONGPAI_CHECK_NAME_RES res = null;
		if("".equals(result)){
			CREATE_ZONGPAI_CHECK_NAME_REQ req = (CREATE_ZONGPAI_CHECK_NAME_REQ)message;
			result = this.zongPaiManager.zongPaiNameValidate(req.getName(),req.getDeclaration());
			res = new CREATE_ZONGPAI_CHECK_NAME_RES(message.getSequnceNum(), result, req.getName(), req.getDeclaration());
		}else{
			res = new CREATE_ZONGPAI_CHECK_NAME_RES(message.getSequnceNum(), result, "", "");
		}
		return res;
	}
	
	/**
	 * 创建宗派输入密码
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_CREATE_ZONGPAI_INPUT_PASSWORD_REQ(Connection conn,RequestMessage message, Player player) {
		CREATE_ZONGPAI_INPUT_PASSWORD_REQ req = (CREATE_ZONGPAI_INPUT_PASSWORD_REQ)message;
		String password = req.getPassword();
		String password2 = req.getPassword2();
		String passwordHint = req.getPasswordHint();
		String name = req.getName();
		String declaration = req.getDeclaration();
		String result = "";
		result = this.zongPaiManager.创建宗派合法性判断(player);
		if(result == null){
			return null;
		}
		if("".equals(result)){
			result = this.zongPaiManager.zongPaiNameValidate(name, declaration);
			if("".equals(result)){
				if(!password.equals(password2)){
					result = Translate.俩次密码不一样;
				}else if(password.getBytes().length>ZongPaiManager.ZONGPAI_PASSWROD_MAX || password.getBytes().length<ZongPaiManager.ZONGPAI_PASSWROD_MIN ){
					result = Translate.密码长度不符;
				}else if(passwordHint.getBytes().length > ZongPaiManager.ZONGPAI_PASSWRODHINT_NUM){
					result = Translate.密码提示太长;
				}else{
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					mw.setTitle(Translate.创建宗派);
					String description = Translate.translateString(Translate.你确定要建立xx宗派吗, new String[][] {{Translate.STRING_1,ZongPaiManager.创建宗派花费锭},{Translate.STRING_2,name}});
					mw.setDescriptionInUUB(description);
					Option_Zongpai_Create_Confirm option = new Option_Zongpai_Create_Confirm();
					option.setZongPaiName(name);
					option.setPassword(password);
					option.setPassword2(password2);
					option.setPasswordHint(passwordHint);
					option.setDeclaration(declaration);
					option.setText(Translate.创建);
					Option_Cancel cancel = new Option_Cancel();
					cancel.setText(Translate.取消);
					mw.setOptions(new Option[]{option,cancel});
					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(res);
					return null;
				}
			}
		}else{
			if(ZongPaiManager.logger.isWarnEnabled()){
				ZongPaiManager.logger.warn("[创建宗派错误] ["+player.getLogString()+"] ["+result+"]");
			}
		}
		player.send_HINT_REQ(result);
		return null;
	}
	
	/**
	 * 宗主权限判定
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ZONGZHU_CHECK_REQ(Connection conn,RequestMessage message, Player player) {
		
		String result = this.zongPaiManager.宗主身份判断(player);
		ZONGZHU_CHECK_REQ req = (ZONGZHU_CHECK_REQ)message;
		ZONGZHU_CHECK_RES res = new ZONGZHU_CHECK_RES(message.getSequnceNum(), result,req.getOptype());
		return res;
	}

	
	/**
	 * 解散宗派确定
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_DISMISS_ZONGPAI_REQ(Connection conn,RequestMessage message, Player player) {
		
		DISMISS_ZONGPAI_REQ req = (DISMISS_ZONGPAI_REQ)message;
		String password = req.getPassword();
		
		ZongPai zp = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
		if(zp == null){
			player.send_HINT_REQ(Translate.你没有家族宗派);
			logger.error("[解散宗派错误] [还没有宗派] ["+player.getLogString()+"]");
			return null;
		}
		long masterId = zp.getMasterId();
		if(masterId != player.getId()){
			player.send_HINT_REQ(Translate.不是宗主);
			return null;
		}
		
		if(player.getCountryPosition() == CountryManager.国王){
			player.send_HINT_REQ(Translate.国王不能解散宗派);
			return null;
		}

		if(zp.getPassword().equals(password)){
			ZongPaiManager zongPaiManager = ZongPaiManager.getInstance();
			zongPaiManager.dismissZongPaiConfirm(player);
		}else{
			player.send_HINT_REQ(Translate.宗派密码不正确);
		}
		return null;
	}
	
	/**
	 * 修改宗派宗旨确定
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_UPDATE_ZONGPAI_DECLARATION_REQ(Connection conn,RequestMessage message, Player player) {

		try {
			UPDATE_ZONGPAI_DECLARATION_REQ req = (UPDATE_ZONGPAI_DECLARATION_REQ)message;
			String declaration = req.getName();
			if(declaration == null || declaration.equals("")){
				logger.error("[修改宗派宗旨错误] [参数错误] ["+player.getLogString()+"]");
				return null;
			}
			String result = this.zongPaiManager.zongPaiDeclarationValidate(declaration);
			if(result.equals("")){
				result = this.zongPaiManager.updateDeclaration(player, declaration);
				ZongPai zp = zongPaiManager.getZongPaiByPlayerId(player.getId());
				Player[] ps = playerManager.getOnlineInZongpai(zp.getId());
				UPDATE_ZONGPAI_DECLARATION_RES res = new UPDATE_ZONGPAI_DECLARATION_RES(req.getSequnceNum(),declaration);
				for(Player p :ps){
					p.addMessageToRightBag(res);
				}
				if(logger.isWarnEnabled()){
					logger.warn("[修改宗派宗旨完成] ["+player.getLogString()+"]");
				}
			}else{
				player.send_HINT_REQ(result);
				if(logger.isWarnEnabled()){
					logger.warn("[修改宗派宗旨错误] ["+player.getLogString()+"] ["+result+"]");
				}
			}
		} catch (Exception e) {
			logger.error("[修改宗派宗旨异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	/**
	 * 逐出宗派确定
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_KICK_ZONGPAI_REQ(Connection conn,RequestMessage message, Player player) {
		
		try {
			KICK_ZONGPAI_REQ req =(KICK_ZONGPAI_REQ)message;
			long jiazuId = req.getJiazuId();
			
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
			if(jiazu == null){
				logger.error("[宗主逐出家族错误] [参数错误] ["+player.getLogString()+"] [jiazuId:"+jiazuId+"]");
				return null;
			}
			ZongPai zp = this.zongPaiManager.getZongPaiByPlayerId(player.getId());
			if(zp != null && zp.getJiazuIds().contains(jiazuId)){
				String result = this.zongPaiManager.kickFromZongPaiConfirm(player, jiazuId);
				if(result.equals("")){
					
					player.sendError(Translate.踢出家族成功);
					if(logger.isWarnEnabled()){
						logger.warn("[宗主逐出家族成功] ["+player.getLogString()+"] [jiazuId:"+jiazuId+"]");
					}
				}else{
					player.send_HINT_REQ(result);
					if(logger.isWarnEnabled()){
						logger.warn("[宗主逐出家族失败] ["+player.getLogString()+"] [jiazuId:"+jiazuId+"] ["+result+"]");
					}
				}
			}else{
				logger.error("[宗主逐出参数错误] ["+player.getLogString()+"] [jiazuId:"+jiazuId+"]");
			}
		} catch (Exception e) {
			logger.error("[宗主逐出家族异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	
	
	/**
	 * 宗主禅让确定
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_DEMISE_ZONGPAI_REQ(Connection conn,RequestMessage message, Player player) {
		
		DEMISE_ZONGPAI_REQ req = (DEMISE_ZONGPAI_REQ)message;
		long jiazuId = req.getJiazuId();
		ZongPai zp = this.zongPaiManager.getZongPaiByPlayerId(player.getId());
		if(zp == null || !zp.getJiazuIds().contains(jiazuId)){
			logger.error("[查询宗派错误] ["+player.getLogString()+"] ["+(zp != null?zp.getId():"null")+"] [jiazuId:"+jiazuId+"]");
			return null;
		}
		
		if(zp.getMasterId() != player.getId()){
			logger.error("[宗主禅让错误] [不是宗主] ["+player.getLogString()+"] [zp:"+zp.getId()+"]");
			return null;
		}
		
		if(!zp.getPassword().equals(req.getPassword())){
			logger.error("[宗主禅让错误] [密码不对] ["+player.getLogString()+"] ["+req.getPassword()+"]");
			player.send_HINT_REQ(Translate.宗派密码不正确);
			return null;
		}
		
		try {
			this.zongPaiManager.demiseZongPai(player,jiazuId);
		} catch (Exception e) {
			logger.error("[宗主禅让异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	
	/**
	 * 离开宗派
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_LEAVE_ZONGPAI_APPLY_REQ(Connection conn,RequestMessage message, Player player) {
		try {
			this.zongPaiManager.leaveFromZongPaiApply(player);
		} catch (Exception e) {
			logger.error("[离开宗派异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	
	/**
	 * 添加宗派
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ADD_ZONGPAI_REQ(Connection conn,RequestMessage message, Player player) {
		ADD_ZONGPAI_REQ req = (ADD_ZONGPAI_REQ)message;
		long id = req.getPlayerId();
		
		Player playerA = null;
		try {
			playerA = playerManager.getPlayer(id);
		} catch (Exception e) {
			ZongPaiManager.logger.error("[添加宗派异常] ["+player.getLogString()+"]",e);
		}
		if(playerA != null){
			try {
				this.zongPaiManager.addToZongPai(player, playerA);
			} catch (Exception e) {
				logger.error("[添加宗派异常] ["+player.getLogString()+"]",e);
			}
		}
		return null;
	}
	
	/**
	 * 查询宗派
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_ZONGPAI_REQ(Connection conn,RequestMessage message, Player player) {

		ZONGPAI_INFO_RES res = ZongPaiManager.getInstance().queryZongPaiInfo(player);
		if(res != null){
			player.addMessageToRightBag(res);
		}
		return null;
	}
	
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}
}
