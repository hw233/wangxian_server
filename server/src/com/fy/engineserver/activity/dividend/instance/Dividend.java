package com.fy.engineserver.activity.dividend.instance;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Dividend {
	/** 红利type，用以区分玩家购买的哪种红利，根据表给予玩家相应奖励 */
	private int type;
	/** 购买时间 */
	private long buyTime;
	/**  */
}
