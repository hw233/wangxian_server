package com.fy.boss.deploy;

import java.util.List;

public class Module {
	private String name;
	
	private String type;
	
	private String from;
	
	private String to;
	
	private List<String> excepts;
	
	private String parser;
	
	private boolean common;

	@Override
	public String toString() {
		return "[common=" + common + ", excepts=" + excepts + ", from="
				+ from + ", name=" + name + ", parser=" + parser + ", to=" + to
				+ ", type=" + type + "]";
	}

	/**
	 * 得到名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 得到类型
	 * @return
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 源
	 * @return
	 */
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * 目的地
	 * @return
	 */
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * 处理类
	 * @return
	 */
	public String getParser() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = parser;
	}

	public List<String> getExcepts() {
		return excepts;
	}

	public void setExcepts(List<String> excepts) {
		this.excepts = excepts;
	}

	public boolean isCommon() {
		return common;
	}

	public void setCommon(boolean common) {
		this.common = common;
	}
	
	
}
