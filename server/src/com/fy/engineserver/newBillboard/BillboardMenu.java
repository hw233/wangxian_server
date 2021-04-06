package com.fy.engineserver.newBillboard;

import java.util.ArrayList;
import java.util.List;


public class BillboardMenu {

	private String menuName;
	private String[] subMenus;
	private List<String> subMenusList;
	private List<String> titles;
	private List<String> titleNums;
	private int menuMaxNum;
	private int subMenuMaxNum;
	private List<String> classNames;
	
	private List<String> descriptions;
	private List<Boolean> dayOrMonths;
	
	public List<Billboard> createBillboard()throws Exception{
		
		if(subMenusList.size() == titles.size() && titleNums.size() == classNames.size() && subMenusList.size() == titleNums.size()){
			List<Billboard> list = new ArrayList<Billboard>();
			subMenus = subMenusList.toArray(new String[0]);
			int max = subMenusList.size();
			for(int i=0;i<max;i++){
				
				String classname = BillboardsManager.baseFile+classNames.get(i);
				Class cl = null;
				try {
					cl = Class.forName(classname);
					try {
						Billboard bb = (Billboard)cl.newInstance();
						bb.setMenu(this.menuName);
						bb.setSubMenu(this.subMenusList.get(i));
						String title = this.titles.get(i);
						String titleNum = this.titleNums.get(i);
						if(title.split(":").length == titleNum.split(";").length){
							
							bb.setTitles(title.split(":"));
							String[] temp = titleNum.split(";");
							int[] nums = new int[temp.length];
							for(int j=0;j<temp.length;j++){
								nums[j] = Integer.parseInt(temp[j]);
							}
							bb.setTitlesMaxNum(nums);
							bb.setDescription(this.descriptions.get(i));
						}else{
							throw new RuntimeException("生成排行榜对象错误，title跟titleNum不对应"+this.menuName);
						}
						bb.setDateOrMonth(this.dayOrMonths.get(i));
						list.add(bb);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					throw new Exception("生成排行榜错误没有这个排行榜类"+classname);
				}
			}
			return list;
		}else{
			BillboardsManager.logger.error("[生成排行榜错误] ["+menuName+"]");
			throw new RuntimeException("生成排行榜错误");
		}
	}
	
	

	/*******************************************/
	
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getMenuMaxNum() {
		return menuMaxNum;
	}

	public void setMenuMaxNum(int menuMaxNum) {
		this.menuMaxNum = menuMaxNum;
	}

	public int getSubMenuMaxNum() {
		return subMenuMaxNum;
	}

	public void setSubMenuMaxNum(int subMenuMaxNum) {
		this.subMenuMaxNum = subMenuMaxNum;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public List<String> getSubMenusList() {
		return subMenusList;
	}
	public void setSubMenusList(List<String> subMenusList) {
		this.subMenusList = subMenusList;
	}

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public List<String> getTitleNums() {
		return titleNums;
	}

	public void setTitleNums(List<String> titleNums) {
		this.titleNums = titleNums;
	}

	public List<String> getClassNames() {
		return classNames;
	}

	public void setClassNames(List<String> classNames) {
		this.classNames = classNames;
	}

	public String[] getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(String[] subMenus) {
		this.subMenus = subMenus;
	}

	public List<Boolean> getDayOrMonths() {
		return dayOrMonths;
	}

	public void setDayOrMonths(List<Boolean> dayOrMonths) {
		this.dayOrMonths = dayOrMonths;
	}
	
}
