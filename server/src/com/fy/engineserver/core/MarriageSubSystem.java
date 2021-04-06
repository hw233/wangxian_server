package com.fy.engineserver.core;

import java.lang.reflect.Method;

import org.slf4j.Logger;

import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.zhaoHuiMiMa.Option_SetMiBao;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.MARRIAGE_ASSIGN_CHOOSE1_REQ;
import com.fy.engineserver.message.MARRIAGE_ASSIGN_CHOOSE1_RES;
import com.fy.engineserver.message.MARRIAGE_BEQ_REQ;
import com.fy.engineserver.message.MARRIAGE_CANCEL_GUEST_REQ;
import com.fy.engineserver.message.MARRIAGE_CANCEL_GUEST_RES;
import com.fy.engineserver.message.MARRIAGE_CANCEL_REQ;
import com.fy.engineserver.message.MARRIAGE_CHOOSE_GUEST_REQ;
import com.fy.engineserver.message.MARRIAGE_CHOOSE_GUEST_RES;
import com.fy.engineserver.message.MARRIAGE_DELAYTIME_BEGIN_REQ;
import com.fy.engineserver.message.MARRIAGE_FINISH_REQ;
import com.fy.engineserver.message.MARRIAGE_GUEST_OVER_REQ;
import com.fy.engineserver.message.MARRIAGE_JIAOHUAN_REQ;
import com.fy.engineserver.message.MARRIAGE_JOIN_HUNLI_SCREEN_REQ;
import com.fy.engineserver.message.MARRIAGE_REQ;
import com.fy.engineserver.message.MARRIAGE_TARGET_MENU_REQ;
import com.fy.engineserver.message.MARRIAGE_TARGET_SEND_REQ;
import com.fy.engineserver.message.MARRIAGE_TIME_OK_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class MarriageSubSystem extends SubSystemAdapter{

	Logger logger = MarriageManager.logger;
	
	public static MarriageSubSystem instance;
	
	public static MarriageSubSystem getInstance(){
		return instance;
	}
	
	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}
	
	@Override
	public String getName() {
		return "MarriageSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{
				"MARRIAGE_TEST_REQ", "MARRIAGE_BEQ_REQ", "MARRIAGE_REQ", "MARRIAGE_ASSIGN_REQ", "MARRIAGE_ASSIGN_CHOOSE1_REQ",
				"MARRIAGE_CHOOSE_GUEST_REQ", "MARRIAGE_CANCEL_GUEST_REQ", "MARRIAGE_GUEST_OVER_REQ", "MARRIAGE_TIME_REQ", "MARRIAGE_TIME_OK_REQ",
				"MARRIAGE_FINISH_REQ", "MARRIAGE_ASSIGN_CHOOSE2_REQ", "MARRIAGE_JIAOHUAN_REQ", "MARRIAGE_CANCEL_REQ", "MARRIAGE_JOIN_HUNLI_SCREEN_REQ",
				"GET_MARRIAGE_FRIEND_REQ","MARRIAGE_TARGET_MENU_REQ", "MARRIAGE_DELAYTIME_BEGIN_REQ", "MARRIAGE_TARGET_SEND_REQ"
		};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}

		Class clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		m1.setAccessible(true);
		return (ResponseMessage) m1.invoke(this, conn, message, player);
	}

	@Override
	public boolean handleResponseMessage(Connection conn,RequestMessage request, ResponseMessage response)throws ConnectionException, Exception {
		return false;
	}
	
	//测试协议处理，以后会删除掉
	public ResponseMessage handle_MARRIAGE_TEST_REQ(Connection conn, RequestMessage message, Player player){
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle("");
		mw.setDescriptionInUUB("");
//		Option_Marriage_BegPlayer option1 = new Option_Marriage_BegPlayer(MarriageManager.getInstance().BEQ_TYPE_HUA);
//		option1.setText("送花求婚");
//		Option_Marriage option2 = new Option_Marriage();
//		option2.setText("结婚");
//		Option_Marriage_BegPlayer option3 = new Option_Marriage_BegPlayer(MarriageManager.getInstance().BEQ_TYPE_TANG);
//		option3.setText("送糖求婚");
//		Option_Marriage_Assign option4 = new Option_Marriage_Assign();
//		option4.setText("布置婚礼");
//		Option_LiHun option5 = new Option_LiHun(0);
//		option5.setText("离婚");
//		Option_LiHun option6 = new Option_LiHun(1);
//		option6.setText("强制离婚");
//		Option_jiaohuan option7 = new Option_jiaohuan();
//		option7.setText("交换戒指");
//		Option_joinHunLiScreen option8 = new Option_joinHunLiScreen();
//		option8.setText("参加婚礼");
//		Option_MarriageDelay option9 = new Option_MarriageDelay();
//		option9.setText("延时婚礼");
//		Option_CangKuMiMa option10 = new Option_CangKuMiMa();
//		option10.setText("仓库密码找回");
//		Option_JiaZuMiMa option11 = new Option_JiaZuMiMa();
//		option11.setText("家族密码找回");
//		Option_ZongPaiMiMa option12 = new Option_ZongPaiMiMa();
//		option12.setText("宗派密码找回");
//		mw.setOptions(new Option[]{option1, option2, option3, option4, option5, option6, option7, option8, option9, option10, option11, option12});
		Option_SetMiBao option1 = new Option_SetMiBao();
		option1.setText("设置密保");
		mw.setOptions(new Option[]{option1});
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(creq);
		return null;
	}

	public ResponseMessage handle_MARRIAGE_BEQ_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_BEQ_REQ req = (MARRIAGE_BEQ_REQ)message;
		int selectIndex = req.getSelectIndex();
		String playerName = req.getPlayerName();
		String say = req.getSay();
		MarriageManager.getInstance().msg_Beq(player, selectIndex, playerName, say);
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_REQ req = (MARRIAGE_REQ)message;
		MarriageManager.getInstance().msg_marriage_req(player, req.getPlayerName());
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_ASSIGN_REQ(Connection conn, RequestMessage message, Player player){
		MarriageManager.getInstance().msg_assign(player);
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_ASSIGN_CHOOSE1_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_ASSIGN_CHOOSE1_REQ req = (MARRIAGE_ASSIGN_CHOOSE1_REQ)message;
		CompoundReturn compoundReturn = MarriageManager.getInstance().msg_assign_choose1(player, req.getIndex());
		if(compoundReturn != null && compoundReturn.getBooleanValue()){
			return new MARRIAGE_ASSIGN_CHOOSE1_RES(req.getSequnceNum());
		}
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_CHOOSE_GUEST_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_CHOOSE_GUEST_REQ req = (MARRIAGE_CHOOSE_GUEST_REQ)message;
		CompoundReturn compoundReturn = MarriageManager.getInstance().msg_chooseGuest(player, req.getPlayerId());
		if(compoundReturn!=null){
			return new MARRIAGE_CHOOSE_GUEST_RES(GameMessageFactory.nextSequnceNum(), (byte)0, (byte)0, compoundReturn.getLongValue(), compoundReturn.getStringValue(),compoundReturn.getByteValue());
		}else{
			return new MARRIAGE_CHOOSE_GUEST_RES(GameMessageFactory.nextSequnceNum(), (byte)1, (byte)0, 0, "",(byte)0);
		}
	}
	
	public ResponseMessage handle_MARRIAGE_CANCEL_GUEST_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_CANCEL_GUEST_REQ req = (MARRIAGE_CANCEL_GUEST_REQ)message;
		CompoundReturn compoundReturn = MarriageManager.getInstance().msg_cancelGuest(player, req.getPlayerId());
		if(compoundReturn!=null){
			return new MARRIAGE_CANCEL_GUEST_RES(req.getSequnceNum(), (byte)0, req.getPlayerId());
		}else{
			return new MARRIAGE_CANCEL_GUEST_RES(req.getSequnceNum(), (byte)1, req.getPlayerId());
		}
	}
	
	public ResponseMessage handle_MARRIAGE_GUEST_OVER_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_GUEST_OVER_REQ req = (MARRIAGE_GUEST_OVER_REQ)message;
		MarriageManager.getInstance().msg_guest_over(player, req.getIsOver());
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_TIME_REQ(Connection conn, RequestMessage message, Player player){
		MarriageManager.getInstance().msg_time(player);
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_TIME_OK_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_TIME_OK_REQ req = (MARRIAGE_TIME_OK_REQ)message;
		MarriageManager.getInstance().msg_timeOk(player, req.getTime());
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_FINISH_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_FINISH_REQ res = (MARRIAGE_FINISH_REQ)message;
		MarriageManager.getInstance().msg_finish(player, res.getIsOk());
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_ASSIGN_CHOOSE2_REQ(Connection conn, RequestMessage message, Player player){
		MarriageManager.getInstance().msg_chooseGuest(player);
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_JIAOHUAN_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_JIAOHUAN_REQ req = (MARRIAGE_JIAOHUAN_REQ)message;
		MarriageManager.getInstance().msg_jiaohuan(player, req.getIsOk());
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_CANCEL_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_CANCEL_REQ req = (MARRIAGE_CANCEL_REQ)message;
		MarriageManager.getInstance().msg_cancel(player, req.getCancelType());
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_JOIN_HUNLI_SCREEN_REQ(Connection conn, RequestMessage message, Player player){
		MARRIAGE_JOIN_HUNLI_SCREEN_REQ req = (MARRIAGE_JOIN_HUNLI_SCREEN_REQ)message;
		MarriageManager.getInstance().msg_joinHunLiScreen(player, req.getInfoId());
		
		return null;
	}
	
	public ResponseMessage handle_GET_MARRIAGE_FRIEND_REQ(Connection conn,RequestMessage message,Player player){
		return MarriageManager.getInstance().msg_getFriendList(message,player);
	}
	
	public ResponseMessage handle_MARRIAGE_TARGET_MENU_REQ(Connection conn,RequestMessage message,Player player){
		MARRIAGE_TARGET_MENU_REQ req = (MARRIAGE_TARGET_MENU_REQ)message;
		long targetId = req.getPlayerId();
		return MarriageManager.getInstance().msg_targetMenuBeqStart(player, targetId);
	}
	
	public ResponseMessage handle_MARRIAGE_DELAYTIME_BEGIN_REQ(Connection conn,RequestMessage message,Player player){
		MarriageManager.getInstance().msg_DelayTime(player, ((MARRIAGE_DELAYTIME_BEGIN_REQ)message).getTime());
		return null;
	}
	
	public ResponseMessage handle_MARRIAGE_TARGET_SEND_REQ(Connection conn,RequestMessage message,Player player){
		MARRIAGE_TARGET_SEND_REQ req = (MARRIAGE_TARGET_SEND_REQ)message;
		MarriageManager.getInstance().msg_targetSend(player, req.getPlayerId(), req.getFlowType(), req.getFlowNum());
		return null;
	}
	
}
