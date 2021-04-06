package com.fy.engineserver.menu.loverday;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.cave.CaveOption;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;

/**
 * 赠送礼物窗口
 * 
 * 
 */
public class Option_SendGift extends Option implements NeedRecordNPC, NeedCheckPurview {
	public static long startTime = TimeTool.formatter.varChar19.parse("2013-03-14 00:00:00");
	
	
	public static long endTime = TimeTool.formatter.varChar19.parse("2013-03-29 00:00:00");
	
	private NPC npc;

	/** 要送的物品名字 */
	private String articleName;
	/** 称号(男) */
	private String titleForMale;
	/** 称号(女) */
	private String titleForFemale;

	public Option_SendGift() {

	}

	public Option_SendGift(String articleName, String titleForMale, String titleForFemale) {
		this.articleName = articleName;
		this.titleForMale = titleForMale;
		this.titleForFemale = titleForFemale;
	}

	@Override
	public void doSelect(Game game, Player player) {
		ArticleEntity ae = player.getArticleEntity(articleName);
		if (ae == null) {
			player.sendError("你没有所需物品:" + articleName);
			return;
		}
		byte sex = player.getSex();
		// 查看好友(在线.且异性)
		List<Long> friendlist = SocialManager.getInstance().getFriendlist(player);
		List<Player> players = new ArrayList<Player>();
		if (friendlist != null) {
			for (Long friendId : friendlist) {
				if (GamePlayerManager.getInstance().isOnline(friendId)) {
					Player p = null;
					try {
						p = GamePlayerManager.getInstance().getPlayer(friendId);
					} catch (Exception e) {
						ActivitySubSystem.logger.error("[情人节]", e);
					}
					if (p != null) {
						if (p.getSex() != sex) {
							players.add(p);
						}
					}
				}
			}
		}
		if (players.size() == 0) {
			player.sendError("你没有在线的异性好友");
			return;
		}
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60);
		mw.setDescriptionInUUB("选择你的有情人");
		mw.setNpcId(npc.getId());
		mw.setTitle("有情人终成眷属");
		List<Option> options = new ArrayList<Option>();
		for (Player p : players) {
			Option_Friend option_Friend = new Option_Friend(p.getId(), p.getName(), this);
			option_Friend.setText(p.getName());
			option_Friend.setColor(0xEA9DF0);
			options.add(option_Friend);
		}
		mw.setOptions(options.toArray(new Option[0]));

		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getTitleForMale() {
		return titleForMale;
	}

	public void setTitleForMale(String titleForMale) {
		this.titleForMale = titleForMale;
	}

	public String getTitleForFemale() {
		return titleForFemale;
	}

	public void setTitleForFemale(String titleForFemale) {
		this.titleForFemale = titleForFemale;
	}

	@Override
	public NPC getNPC() {
		// TODO Auto-generated method stub
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}

	@Override
	public boolean canSee(Player player) {
		long now = SystemTime.currentTimeMillis();
		if (now > startTime && now < endTime) {
			return true;
		}
		return false;
	}
}
