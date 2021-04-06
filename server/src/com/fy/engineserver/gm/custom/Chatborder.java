package com.fy.engineserver.gm.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gm.record.ActionManager;
import com.fy.engineserver.gm.record.RecordManager;
import com.fy.engineserver.gm.record.RecordMessage;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.text.DateUtil;

public class Chatborder {

	public String[] getMessageByname(String gmname) {
		/**
		 * 获取某一个gm的所有消息
		 */
		try {
			ChatMessageService cms = ChatMessageService.getInstance();
			RecordManager rmanager = RecordManager.getInstance();
			List<ChatMessage> cmess = cms.getGmMessages(gmname);
			String res[] = new String[cmess.size()];
			List<RecordMessage> rms = new ArrayList<RecordMessage>();
			for (int i = 0; i < cmess.size(); i++) {
				RecordMessage rm = new RecordMessage();	
				ChatMessage cm = cmess.get(i);
				rm.setFname(cm.getSenderName()+":"+cm.getSenderId());
				rm.setToname(cm.getReceiverName());
				rm.setSendDate(DateUtil.formatDate(new Date(cm.getChatTime()),"yyyy-MM-dd-HH-mm-ss"));
				rm.setMessage(cm.getMessageText());
				rms.add(rm);
				String m = cm.getSenderName()+"("+cm.getSenderId()
						+ ")#@#"
						+ DateUtil.formatDate(new Date(cm.getChatTime()),
								"yyyy-MM-dd HH:mm:ss");
				m = m + "#@#" + cm.getMessageText();
				res[i] = m;

			}
			if(Game.logger.isInfoEnabled()){
				Game.logger.info("[获取GM消息] [GM:"+gmname+"] [消息数量："+cmess.size()+"] [聊天消息m数量:"+res.length+"]");
			}
			rmanager.saveRecord(rms, "alonechat");
			cms.clearGmMessages(gmname);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return new String[] {};
		}
	}

	public void sendMessage(String gmname,long playerId,String content){
		ChatMessageService cms = ChatMessageService.getInstance();
		cms.sendGmMessage(gmname, playerId, content);
		
	}
	public String[] getMessageWord() {
		/**
		 *  获取世界频道消息
		 */
		try {
			RecordManager rmanager = RecordManager.getInstance();
			ChatMessageService cms = ChatMessageService.getInstance();
			List<ChatMessage> cmess = cms.getWorldMessageList();
			String res[] = new String[cmess.size()];
			List<RecordMessage> rms = new ArrayList<RecordMessage>();
			for (int i = 0; i < cmess.size(); i++) {
				RecordMessage rm = new RecordMessage();	
				ChatMessage cm = cmess.get(i);
				String m = cm.getSenderName()+"("+cm.getSenderId()
						+ ")#@#"
						+ DateUtil.formatDate(new Date(cm.getChatTime()),
								"yyyy-MM-dd HH:mm:ss");
				m = m + "#@#" + cm.getMessageText();
				rm.setFname(cm.getSenderName()+":"+cm.getSenderId());
				rm.setMessage(cm.getMessageText());
				rm.setSendDate(DateUtil.formatDate(new Date(cm.getChatTime()),
								"yyyy-MM-dd HH:mm:ss"));
				rm.setToname(Translate.text_4220);
				rms.add(rm);
				res[i] = m;

			}
			rmanager.saveRecord(rms,"wordchat" );
			cms.getWorldMessageList().clear();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return new String[] {};
		}
	}

	public void sendMessageWord(String gmname ,String content){
		ChatMessageService cms = ChatMessageService.getInstance();
		cms.sendGmMessageToWorld(gmname, content);
		
	}

	public String tankuang(String pid,String message,String gmname){
		PlayerManager pmanager = PlayerManager.getInstance();
		ActionManager amanager = ActionManager.getInstance();
		try {
			Player p = pmanager.getPlayer(Long.parseLong(pid.trim()));
			if(ActionManager.logger.isInfoEnabled()){
				ActionManager.logger.info("[弹框提示] [账号："+p.getUsername()+"] [角色名："+p.getName()+"] [ID："+p.getId()+"] [弹框消息："+message+"] [gm操作人："+gmname+"]");
			}
			MenuWindow mw = new MenuWindow();
			mw.setTitle(Translate.text_4223);
			mw.setDescriptionInUUB(message);
			mw.setWidth(1000);
			mw.setHeight(1000);
			Option_Cancel o = new Option_Cancel();
			o.setText(Translate.text_2901);
			mw.setOptions(new Option[]{o});
			QUERY_WINDOW_RES re = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),
					mw,mw.getOptions());
			p.addMessageToRightBag(re);
			amanager.save(gmname,Translate.text_4224+message+"]");
			return Translate.text_4225;
		}catch (Exception e) {
			return Translate.text_4226;
		}
 		
	}
}
