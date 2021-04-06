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
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.notice.NoticeForever;
import com.fy.engineserver.notice.NoticeManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;

public class Option_Query_Notice_Forever extends Option implements NeedRecordNPC{
	private NPC npc;
	
	private String selectType;
	/**
	 * 公告
	 */
	public void doSelect(Game game,Player player){
		NoticeManager nm = NoticeManager.getInstance();
		Map<String, List<NoticeForever>> foreverNotices = nm.getForeverNotices();
		if(foreverNotices!=null && foreverNotices.size()>0){
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(Translate.公告);
			mw.setDescriptionInUUB("");
			mw.setNpcId(getNPC() == null ? 0 : getNPC().getId());
			if(selectType!=null){
				List<NoticeForever> list = foreverNotices.get(selectType);
				List<Option> options = new LinkedList<Option>();
				if(list!=null){
					for(NoticeForever nf:list){
						try{
							if(nf.isEffective()){
								Option_Query_Notice_By_Title option = new Option_Query_Notice_By_Title();
								option.setText(nf.getTitlename());
								option.setNPC(npc);
								option.setTypename(selectType);
								option.setTitlename(nf.getTitlename());
								options.add(option);
							}
						}catch(Exception e){
							e.printStackTrace();
							System.out.println(e);
						}
					}
					mw.setOptions(options.toArray(new Option[0]));
					QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
					player.addMessageToRightBag(req);
				}else{
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.永久公告为空提示);
					player.addMessageToRightBag(hreq);
					return;
				}
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
		return OptionConstants.SERVER_FUNCTION_永久公告;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.永久公告类型;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		// TODO Auto-generated method stub
		this.npc = npc;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	

}
