package com.fy.engineserver.menu.notice;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.notice.NoticeForever;
import com.fy.engineserver.notice.NoticeManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;

public class Option_Query_Notice_By_Title extends Option implements NeedRecordNPC{
	
	/**
	 * 选择公告类型
	 */
	private String typename;
	
	private String titlename;
	
	private NPC npc;

	public void doSelect(Game game,Player player){
		NoticeManager nm = NoticeManager.getInstance();
		Map<String, List<NoticeForever>> foreverNotices = nm.getForeverNotices();
		List<NoticeForever> list = new LinkedList<NoticeForever>();
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.公告);
		mw.setDescriptionInUUB("");
		mw.setNpcId(getNPC() == null ? 0 : getNPC().getId());
		if(foreverNotices!=null && foreverNotices.size()>0){
			if(typename!=null){
				if(titlename!=null){
					list = foreverNotices.get(typename);
					String content = "";
					for(NoticeForever nf:list){
						if(nf.getTitlename()!=null &&nf.getTitlename().equals(titlename)){
							content = nf.getNoticeContent();
						}
					}
					mw.setDescriptionInUUB(content);
					Option_Cancel cancle = new Option_Cancel();
					cancle.setText(Translate.反馈状态关闭);
					mw.setOptions(new Option[]{cancle});
					QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw,mw.getOptions());
					player.addMessageToRightBag(req);
				}
		}else{
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.永久公告为空提示);
			player.addMessageToRightBag(hreq);
			return;
		}
	}
	}
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_永久公告_TITLE;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.永久公告类型标题;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getTitlename() {
		return titlename;
	}

	public void setTitlename(String titlename) {
		this.titlename = titlename;
	}
	@Override
	public NPC getNPC() {
		// TODO Auto-generated method stub
		return npc;
	}
	@Override
	public void setNPC(NPC npc) {
		// TODO Auto-generated method stub
		this.npc = npc;
	}
	

}
