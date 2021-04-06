package com.fy.engineserver.menu.notice;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.notice.NoticeForever;
import com.fy.engineserver.notice.NoticeManager;
import com.fy.engineserver.sprite.Player;

public class Option_Query_Notice_Content extends Option{
	
	/**
	 * 选择公告title
	 */
	private String titlename;

	private String typename;
	
	public void doSelect(Game game,Player player){
		NoticeManager nm = NoticeManager.getInstance();
		Map<String, List<NoticeForever>> foreverNotices = nm.getForeverNotices();
		if(foreverNotices!=null && foreverNotices.size()>0){
			if(typename!=null&&titlename!=null){
				List<NoticeForever> list = new LinkedList<NoticeForever>();
				list = foreverNotices.get(typename);
				String content = "";
				for(NoticeForever nf:list){
					if(nf.getTitlename()!=null &&nf.getTitlename().equals(titlename)){
						content = nf.getNoticeContent();
					}
				}
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				mw.setTitle(titlename);
				mw.setDescriptionInUUB(content);
				mw.setOptions(new Option[]{});
				CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), new Option[]{});
				player.addMessageToRightBag(req);
			}
		}else{
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.永久公告为空提示);
			player.addMessageToRightBag(hreq);
			return;
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_永久公告_CONTENT;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.永久公告类型内容;
	}

	public String getTitlename() {
		return titlename;
	}

	public void setTitlename(String titlename) {
		this.titlename = titlename;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}


}
