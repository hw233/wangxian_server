package com.fy.engineserver.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.message.CHAT_CHANNEL_SET_REQ;
import com.fy.engineserver.message.CHAT_CHANNEL_SET_RES;
import com.fy.engineserver.message.CHAT_CHANNEL_STATUS_REQ;
import com.fy.engineserver.message.CHAT_CHANNEL_STATUS_RES;
import com.fy.engineserver.message.CHAT_MESSAGE_REQ;
import com.fy.engineserver.message.CHAT_VOICE_INFO_REQ;
import com.fy.engineserver.message.CHAT_VOICE_INFO_RES;
import com.fy.engineserver.message.CHAT_VOICE_REQ;
import com.fy.engineserver.message.CHAT_VOICE_RES;
import com.fy.engineserver.message.CHAT_VOICE_SUCC_REQ;
import com.fy.engineserver.message.FEE_CHAT_CONFIRM_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.smith.RelationShipHelper;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class ChatSubSystem implements GameSubSystem {

//	static Logger logger = Logger.getLogger(ChatSubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(ChatSubSystem.class);

	GameNetworkFramework framework;
	PlayerManager playerManager;
	ChatMessageService chatMessageService;

	public void setGameNetworkFramework(GameNetworkFramework framework) {
		this.framework = framework;
	}

	public void setPlayerManager(PlayerManager pm) {
		playerManager = pm;
	}

	public void setChatMessageService(ChatMessageService chatMessageService) {
		this.chatMessageService = chatMessageService;
	}

	public void initialize() throws Exception {
		
		
		ServiceStartRecord.startLog(this);
		//
	}

	public String[] getInterestedMessageTypes() {
		return new String[] { "CHAT_MESSAGE_REQ","FEE_CHAT_CONFIRM_REQ","CHAT_CHANNEL_STATUS_REQ",
				"CHAT_CHANNEL_SET_REQ", "CHAT_VOICE_REQ", "CHAT_VOICE_INFO_REQ"};
	}

	public String getName() {
		return "ChatSubSystem";
	}

	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		if(logger.isInfoEnabled())
			logger.info("[handleReqeustMessage] [chat_message]");
		Player player = (Player) conn.getAttachmentData("Player");
		if (player != null) {
			player.setLastRequestTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		}
		if (message instanceof CHAT_MESSAGE_REQ) {
			CHAT_MESSAGE_REQ req = (CHAT_MESSAGE_REQ) message;
			ChatMessage chatMsg = req.getChatMessage();
			chatMsg.setAccessoryItem(req.getChatMessageItem());
			chatMsg.setAccessoryTask(req.getChatMessageTask());
			//-----------------------------------------------------
			if(chatMsg.getMessageText().indexOf("/-1")>=0){
				int index=chatMsg.getMessageText().indexOf("/-1");
				String s=chatMsg.getMessageText().replaceAll("/-1", "");
				StringBuffer sb=new StringBuffer(s);
				sb.insert(index, "/-1");
				chatMsg.setMessageText(sb.toString());
			}
			if(chatMsg.getMessageText().indexOf("/-2")>=0){
				int index=chatMsg.getMessageText().indexOf("/-2");
				String s=chatMsg.getMessageText().replaceAll("/-2", "");
				StringBuffer sb=new StringBuffer(s);
				sb.insert(index, "/-2");
				chatMsg.setMessageText(sb.toString());
			}
			//-----------------------------------------------------
			
			//String playerName = chatMsg.getSenderName();
			long id=chatMsg.getSenderId();
			//Player sender = playerManager.getPlayer(playerName);
			Player sender = playerManager.getPlayer(id);
			if(sender.isInBattleField() && sender.getDuelFieldState()>0){
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_2588);
				sender.addMessageToRightBag(hint);
				return null;
			}
			
			try{
				if(WolfManager.getInstance().isWolfGame(sender)){
					sender.sendError(Translate.副本中不能聊天);
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			try {
				if(RelationShipHelper.checkForbidAndSendMessage(sender)) {
					if(RelationShipHelper.logger.isInfoEnabled()) {
						RelationShipHelper.logger.info("[玩家因为打金行为被限制聊天] ["+sender.getLogString()+"]");
					}
					return null;
				}
			} catch(Throwable e) {e.printStackTrace();}

			//禁言，只允许私聊
			if(sender.isJinyan() && chatMsg.getSort() != ChatChannelType.PERSONAL){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.你被禁言了);
				sender.addMessageToRightBag(hreq);
				return null;
			}
			if(sender.getId() == player.getId()) {
				
				chatMessageService.sendPlayerMessage(chatMsg);
//				logger.info("[发送消息] [频道:"+ChatChannelType.getTypeDesp(chatMsg.getSort())+"] [类型:"+chatMsg.getChatType()+"] [发送人:"+chatMsg.getSenderName()+"] [接受人:{"+chatMsg.getReceiverId()+":"+chatMsg.getReceiverName()+"}] [消息内容"+chatMsg.getMessageText()+"]");
				if(logger.isInfoEnabled())
					logger.info("[发送消息] [频道:{}] [类型:{}] [发送人:{}] [接受人:{{}:{}}] [消息内容{}]", new Object[]{ChatChannelType.getTypeDesp(chatMsg.getSort()),chatMsg.getChatType(),chatMsg.getSenderName(),chatMsg.getReceiverId(),chatMsg.getReceiverName(),chatMsg.getMessageText()});
				return null;
			} else {
//				logger.info("[发送消息] [失败:外挂模拟协议发送] [connection_player:{"+player.getName()+"}{"+player.getId()+"}{"+player.getUsername()+"}] [频道:"+ChatChannelType.getTypeDesp(chatMsg.getSort())+"] [类型:"+chatMsg.getChatType()+"] [发送人:"+chatMsg.getSenderName()+"] [接受人:{"+chatMsg.getReceiverId()+":"+chatMsg.getReceiverName()+"}] [消息内容"+chatMsg.getMessageText()+"]");
				if(logger.isInfoEnabled())
					logger.info("[发送消息] [失败:外挂模拟协议发送] [connection_player:{{}}{{}}{{}}] [频道:{}] [类型:{}] [发送人:{}] [接受人:{{}:{}}] [消息内容{}]", new Object[]{player.getName(),player.getId(),player.getUsername(),ChatChannelType.getTypeDesp(chatMsg.getSort()),chatMsg.getChatType(),chatMsg.getSenderName(),chatMsg.getReceiverId(),chatMsg.getReceiverName(),chatMsg.getMessageText()});
			}
		} else if(message instanceof FEE_CHAT_CONFIRM_REQ) {
			
		} else if(message instanceof CHAT_CHANNEL_STATUS_REQ) {
			CHAT_CHANNEL_STATUS_REQ req = (CHAT_CHANNEL_STATUS_REQ)message;
			byte[] channelStatus = player.getChatChannelStatus();
			CHAT_CHANNEL_STATUS_RES res = new CHAT_CHANNEL_STATUS_RES(req.getSequnceNum(), channelStatus);
//			logger.info("[获取频道状态] ["+player.getName()+"]");
			if(logger.isInfoEnabled())
				logger.info("[获取频道状态] [{}]", new Object[]{player.getName()});
			return res;
		} else if(message instanceof CHAT_CHANNEL_SET_REQ) {
			CHAT_CHANNEL_SET_REQ req = (CHAT_CHANNEL_SET_REQ)message;
			byte sort = req.getSort();
			byte status = req.getStatus();
			if(sort != ChatChannelType.SYSTEM) {
				player.setChatChannelStatus(sort, status);
			}
			byte[] channelStatus = player.getChatChannelStatus();
			CHAT_CHANNEL_SET_RES res = new CHAT_CHANNEL_SET_RES(req.getSequnceNum(), channelStatus);
//			logger.info("[设置频道状态] ["+player.getName()+"] ["+ChatChannelType.getTypeDesp(sort)+"] ["+(status==0?"关闭":"开启")+"]");
			if(logger.isInfoEnabled())
				logger.info("[设置频道状态] [{}] [{}] [{}]", new Object[]{player.getName(),ChatChannelType.getTypeDesp(sort),(status==0?"关闭":"开启")});
			return res;
		}else if (message instanceof CHAT_VOICE_REQ) {
			if(player.isInBattleField() && player.getDuelFieldState()>0){
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_2588);
				player.addMessageToRightBag(hint);
				return null;
			}
			CHAT_VOICE_REQ req = (CHAT_VOICE_REQ)message;
			logger.info("[收到CHAT_VOICE_REQ] + ["+req.getReceiverId()+"]");
			try {
				if (req.getSort() == ChatChannelType.PERSONAL) {
					//暂时只有私聊
					if (PlayerManager.getInstance().isOnline(req.getReceiverId())) {
						Player receiver = PlayerManager.getInstance().getPlayer(req.getReceiverId());
						if (receiver.getDuelFieldState() > 0) {
							HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_2585 + receiver.getName() + Translate.text_2587);
							player.addMessageToRightBag(hint);
							return null;
						}
						if (ChatMessageService.getInstance().isSilence(player.getId()) == 2) {
							EnterLimitManager.logger.warn("[用户被限制语音] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+player.getLevel()+"] 银子:["+player.getSilver()+"]");
							return null;
						}
						
						if (ChatMessageService.getInstance().isSilence(receiver.getId()) == 2) {
							EnterLimitManager.logger.warn("[用户被限制语音] ["+receiver.getUsername()+"] ["+receiver.getName()+"] ["+receiver.getId()+"] ["+receiver.getLevel()+"] 银子:["+receiver.getSilver()+"]");
							return null;
						}
						if(ChatMessageService.getInstance().limitFuBenVoice(receiver)){
							player.sendError(Translate.副本不能语音);
							EnterLimitManager.logger.warn("[用户被限制语音] [特殊副本玩家限制] ["+receiver.getUsername()+"] ["+receiver.getName()+"] ["+receiver.getId()+"] ["+receiver.getLevel()+"] 银子:["+receiver.getSilver()+"]");
							return null;
						}
						if (receiver.isBlackuser(player.getId())) {
							HINT_REQ reqq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.对方已把您加入黑名单您不能发出聊天信息);
							player.addMessageToRightBag(reqq);
							CHAT_VOICE_SUCC_REQ rr = new CHAT_VOICE_SUCC_REQ(GameMessageFactory.nextSequnceNum(), req.getVoiceKeyID(), false);
							player.addMessageToRightBag(rr);
							return null;
						}
						receiver.addMessageToRightBag(new CHAT_VOICE_RES(GameMessageFactory.nextSequnceNum(), req.getSort(),
								player.getId(), player.getName(), req.getVoiceKeyID(), req.getVoiceTime(), 
								req.getVoiceSize(), req.getVoiceInfoNum(), new String[0]));
						CHAT_VOICE_SUCC_REQ rr = new CHAT_VOICE_SUCC_REQ(GameMessageFactory.nextSequnceNum(), req.getVoiceKeyID(), true);
						player.addMessageToRightBag(rr);
						SocialManager.getInstance().addTemporaryFriend(receiver, player.getId());
						logger.warn("[发送语音] [发送者:"+player.getLogString()+"] [接受者:"+receiver.getLogString()+"] [频道:"+req.getSort()+"] [key:"+req.getVoiceKeyID()+"] [时长:"+req.getVoiceTime()+"] [大小"+req.getVoiceSize()+"] [拆分数目:"+req.getVoiceInfoNum()+"]");
					}else {
						player.send_HINT_REQ(Translate.您的语音对象已经不在线了, (byte)5);
						CHAT_VOICE_SUCC_REQ rr = new CHAT_VOICE_SUCC_REQ(GameMessageFactory.nextSequnceNum(), req.getVoiceKeyID(), false);
						player.addMessageToRightBag(rr);
					}
				}else {
					logger.warn("[不开放此频道语音] ["+req.getSort()+"] ["+player.getLogString()+"]");
					CHAT_VOICE_SUCC_REQ rr = new CHAT_VOICE_SUCC_REQ(GameMessageFactory.nextSequnceNum(), req.getVoiceKeyID(), false);
					player.addMessageToRightBag(rr);
				}
			} catch(Exception e) {
				logger.error("CHAT_VOICE_REQ出错" ,e);
			}
		}else if (message instanceof CHAT_VOICE_INFO_REQ) {
			logger.info("[收到CHAT_VOICE_INFO_REQ]");
			try {
				if(player.isInBattleField() && player.getDuelFieldState()>0){
					return null;
				}
				CHAT_VOICE_INFO_REQ req = (CHAT_VOICE_INFO_REQ)message;
				if (req.getSort() == ChatChannelType.PERSONAL) {
					//暂时只有私聊
					if (PlayerManager.getInstance().isOnline(req.getReceiverId())) {
						Player receiver = PlayerManager.getInstance().getPlayer(req.getReceiverId());
						if (receiver.getDuelFieldState() > 0) {
							return null;
						}
						if (receiver.isBlackuser(player.getId())) {
							return null;
						}
						if (ChatMessageService.getInstance().isSilence(player.getId()) == 2) {
							EnterLimitManager.logger.warn("[用户被限制语音] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+player.getLevel()+"] 银子:["+player.getSilver()+"]");
							return null;
						}
						
						if (ChatMessageService.getInstance().isSilence(receiver.getId()) == 2) {
							EnterLimitManager.logger.warn("[用户被限制语音] ["+receiver.getUsername()+"] ["+receiver.getName()+"] ["+receiver.getId()+"] ["+receiver.getLevel()+"] 银子:["+receiver.getSilver()+"]");
							return null;
						}
						receiver.addMessageToRightBag(new CHAT_VOICE_INFO_RES(GameMessageFactory.nextSequnceNum(), 
								req.getSort(), player.getId(), req.getVoiceKeyID(), req.getInfoIndex(), req.getInfo()));
						logger.warn("[收到语音文件] [发送者:"+player.getLogString()+"] [接受者:"+receiver.getLogString()+"] [频道:"+req.getSort()+"] [key:"+req.getVoiceKeyID()+"] [index:"+req.getInfoIndex()+"] [size:"+req.getInfo().length+"]");
					}else {
//						player.sendError(Translate.您的语音对象已经不在线了);
					}
				}else {
					logger.warn("[不开放此频道语音] ["+req.getSort()+"] ["+player.getLogString()+"]");
				}
			} catch(Exception e) {
				logger.error("CHAT_VOICE_INFO_REQ出错" ,e);
			}
		}
		return null;
	}

	public boolean handleResponseMessage(Connection conn, RequestMessage req,
			ResponseMessage message) throws ConnectionException, Exception {
		return false;
	}
	
	
}
