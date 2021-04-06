package com.fy.engineserver.datasource.article.data.magicweapon;

import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;

public class MagicWeapon extends Article implements Prop4MagicWeaponEat{
	
	/**
	 * 	    灵猴.霹雳法宝
	 *	  (神器)
	 * 阶级：凡品2阶
	 * 境界：金丹
	 * ☆ ☆ ☆ ☆
	 * 加持：提示基础属性50%
	 * 基础属性：
	 * 外功：+111
	 * 内功：+111
	 * 外防：+222
	 * 附加属性：
	 * 人物击杀怪物获得经验提示50%
	 * (需要阶级到达凡品2级激活)
	 * 隐藏属性：
	 * 外功：+111
	 * 内功：+111
	 * 外防：+222
	 * 闪避：20%
	 * 暴击：10%
	 */
	/**进化后法宝? 用不用再看*/
	private List<String> illution_icons;				//幻化后icon集合
	/**0:通用 1,2,3,4对应职业*/
	private byte careertype;					//0:通用 1,2,3,4对应职业
	/**0：本尊 1:元神*/
	private byte soultype;				//0：本尊 1:元神	
	/**装备等级限制*/
	private int levellimit;				//等级限制(法宝装备等级，可以用来取出法宝具体数值)
	/** 数值等级  */
	private int dataLevel;
	/**境界限制*/
	private int classlimit;				//境界限制
	/**耐久度*/
	private int naijiudu = 200;			
	/** 对应法宝npcid---召唤显示法宝用 */
	private int npcId;
	/** 变换形象等级 */
	private List<Integer> changeLevel;
	/** 变换形象后法宝npc的avatar */
	private List<String> illution_avatars;
	/** 变换形象后法宝npc粒子 */
	private List<String> illution_particle;
	/** 产出是否绑定  默认不绑 */
	private boolean obtainBind;

	/**
	 * 可用：返回null
	 * 不可用：返回不可用的原因
	 * @param p
	 * @return
	 */
	public String canuse(Player p,int soulType){
		if(p==null){
			return "使用法宝异常，请联系GM";
		}
		
		Soul soul = p.getSoul(soulType);
		
		if (this.levellimit > soul.getGrade()) {
			return Translate.级别太低不能使用;
		}
		
		if (this.classlimit > p.getClassLevel()) {
			return Translate.境界不够不能使用;
		}
	
		if (soul == null || this.careertype > 0 && this.careertype != soul.getCareer()) {
			return Translate.职业不符;
		}
		
		if(this.soultype >= 0 && this.soultype != soulType) {
			return Translate.元神不符;
		}
		
		return null;
	}
	
	
	
	
	@Override
	public String get物品一级分类() {
		// TODO Auto-generated method stub
		return Translate.法宝类;
	}
	@Override
	public String get物品二级分类() {
		return Translate.法宝类;
	}




	@Override
	public byte getArticleType() {
		// TODO Auto-generated method stub
		return 83;
	}


	public byte getSoultype() {
		return soultype;
	}
	public void setSoultype(byte soultype) {
		this.soultype = soultype;
	}
	public int getLevellimit() {
		return levellimit;
	}
	public void setLevellimit(int levellimit) {
		this.levellimit = levellimit;
	}
	public int getClasslimit() {
		return classlimit;
	}
	public void setClasslimit(int classlimit) {
		this.classlimit = classlimit;
	}
	public int getNaijiudu() {
		return naijiudu;
	}
	public void setNaijiudu(int naijiudu) {
		this.naijiudu = naijiudu;
	}
	public byte getCareertype() {
		return careertype;
	}
	public void setCareertype(byte careertype) {
		this.careertype = careertype;
	}

	public List<String> getIllution_icons() {
		return illution_icons;
	}
	public void setIllution_icons(String str) {
		if(str == null || str.isEmpty()) {
			return;
		}
		String[] temp = str.split(",");
		this.setIllution_icons(Arrays.asList(temp));
	}

	public void setIllution_icons(List<String> illution_icons) {
		this.illution_icons = illution_icons;
	}

	public int getDataLevel() {
		return dataLevel;
	}

	public void setDataLevel(int dataLevel) {
		this.dataLevel = dataLevel;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}


	public List<Integer> getChangeLevel() {
		return changeLevel;
	}


	public void setChangeLevel(List<Integer> changeLevel) {
		this.changeLevel = changeLevel;
	}
	public void setChangeLevel(String str) {
		if(str == null || str.isEmpty()) {
			return;
		}
		String[] temp = str.split(",");
		Integer[] ii = new Integer[temp.length];
		for(int i=0; i<ii.length; i++) {
			ii[i] = Integer.parseInt(temp[i]);
		}
		this.setChangeLevel(Arrays.asList(ii));
	}


	public List<String> getIllution_avatars() {
		return illution_avatars;
	}

	public void setIllution_avatars(String str) {
		if(str == null || str.isEmpty()) {
			return;
		}
		String[] temp = str.split(",");
		this.setIllution_avatars(Arrays.asList(temp));
	}
	public void setIllution_particle(String str) {
		if(str == null || str.isEmpty()) {
			return;
		}
		String[] temp = str.split(",");
		this.setIllution_particle(Arrays.asList(temp));
	}

	public void setIllution_avatars(List<String> illution_avatars) {
		this.illution_avatars = illution_avatars;
	}


	@Override
	public int getAddExp(Player p) {
		// TODO Auto-generated method stub
		return 0;
	}


	public List<String> getIllution_particle() {
		return illution_particle;
	}


	public void setIllution_particle(List<String> illution_particle) {
		this.illution_particle = illution_particle;
	}


	public boolean isObtainBind() {
		return obtainBind;
	}


	public void setObtainBind(boolean obtainBind) {
		this.obtainBind = obtainBind;
	}
	
}
