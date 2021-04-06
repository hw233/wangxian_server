package com.fy.engineserver.prestige;

import com.fy.engineserver.datasource.language.Translate;


public class PrestigeManager {

	private Prestige[] prestiges;

	protected static PrestigeManager self;

	public static PrestigeManager getInstance() {
		if(self == null){
			self = new PrestigeManager();
		}
		return self;
	}

	public PrestigeManager(){
		setPrestiges();
	}
	public Prestige[] getPrestiges() {
		return prestiges;
	}

	public void setPrestiges() {
		Prestige[] pss = new Prestige[14];
		pss[0] = new Prestige(Translate.text_5564);
		pss[1] = new Prestige(Translate.text_5565);
		pss[2] = new Prestige(Translate.text_5566);
		pss[3] = new Prestige(Translate.text_5567);
		pss[4] = new Prestige(Translate.text_5568);
		pss[5] = new Prestige(Translate.text_5569);
		pss[6] = new Prestige(Translate.text_5570);
		pss[7] = new Prestige(Translate.text_5571);
		pss[8] = new Prestige(Translate.text_5572);
		pss[9] = new Prestige(Translate.text_5573);
		pss[10] = new Prestige(Translate.text_5574);
		pss[11] = new Prestige(Translate.text_5575);
		pss[12] = new Prestige(Translate.text_5576);
		pss[13] = new Prestige(Translate.text_5577);
		this.prestiges = pss;
	}

	/**
	 * 声望数值，数组下标为声望级别
	 */
	public static final int[] prestigeValues = new int[]{0,1000,2000,3000,4000,5000,6000};

	/**
	 * 声望数值，数组下标为声望级别
	 */
	public static final String[] prestigeLevelName = new String[]{Translate.text_5578,Translate.text_5579,Translate.text_5580,Translate.text_5581,Translate.text_5582,Translate.text_5583,Translate.text_5584};

	/**
	 * 根据声望级别返回所需声望数值
	 */
	public int getPrestigeValue(int level){
		if(level >=0 && level < prestigeValues.length){
			return prestigeValues[level];
		}
		return -1;
	}

	/**
	 * 根据声望级别返回声望等级所对应的名字
	 */
	public String getPrestigeLevelName(int level){
		if(level >=0 && level < prestigeLevelName.length){
			return prestigeLevelName[level];
		}
		return "";
	}

	/**
	 * 得到服务器所有声望类
	 */
	public Prestige[] getAllPrestige(){
		if(prestiges != null){
			return prestiges;
		}
		return new Prestige[0];
	}

	/**
	 * 根据声望名取得声望类
	 */
	public Prestige getPrestigeByName(String name){
		if(prestiges != null){
			for(Prestige p : prestiges){
				if(p != null && p.getName() != null && p.getName().equals(name)){
					return p;
				}
			}
		}
		return null;
	}
}
