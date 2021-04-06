package com.fy.engineserver.septstation;

/**
 * 活动数据
 * 
 * 
 */
public class ActivityInfo {

	private int id;
	private String name;
	private String title;
	private String icon;
	private String content;

	public ActivityInfo(int id, String name, String title, String icon, String content) {
		this.id = id;
		this.name = name;
		this.title = title;
		this.icon = icon;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ActivityInfo [id=" + id + ", name=" + name + ", title=" + title + ", icon=" + icon + ", content=" + content + "]";
	}
}
