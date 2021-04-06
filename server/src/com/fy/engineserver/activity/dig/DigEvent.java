package com.fy.engineserver.activity.dig;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 挖宝事件
 * @author Administrator
 * 
 */
public abstract class DigEvent {

	private String useArticleName;
	private String useArticleName_stat;
	private int eventType;

	public DigEvent(String useArticleName, String useArticleNameStat, int eventType) {
		this.useArticleName = useArticleName;
		useArticleName_stat = useArticleNameStat;
		this.eventType = eventType;
	}

	public abstract void execute(Player player, Game game);

	public String getUseArticleName() {
		return useArticleName;
	}

	public void setUseArticleName(String useArticleName) {
		this.useArticleName = useArticleName;
	}

	public String getUseArticleName_stat() {
		return useArticleName_stat;
	}

	public void setUseArticleName_stat(String useArticleNameStat) {
		useArticleName_stat = useArticleNameStat;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "DigEvent [eventType=" + eventType + ", useArticleName=" + useArticleName + ", useArticleName_stat=" + useArticleName_stat + "]";
	}

}
