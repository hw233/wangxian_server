package com.fy.boss.gm.gmuser;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public abstract class NoticeTimeType {
	/**
	 * 时间类型名:每天，每周
	 */
	private String typename;
	
	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public abstract boolean needSendNotice(long now);
	
	public abstract boolean isvalid(long now);
	
}
