package com.fy.engineserver.activity.xianling;

import com.fy.engineserver.util.SimpleKey;

public class XianLingSkill {
	@SimpleKey
	private int skillId; // 技能id
	private String name; // 技能名
	private String articleCNName;// 对应道具统计名
	private String icon;// 图标
	private String particle; // 粒子
	private int cdTime;// cd时间
	private int puclicCDTime;// 公cd时间
	private int buffLastTime;// buff持续时间

	public XianLingSkill() {
	}

	public XianLingSkill(int skillId, String name, String articleCNName, String icon, String particle, int cdTime, int puclicCDTime, int buffLastTime) {
		this.skillId = skillId;
		this.name = name;
		this.articleCNName = articleCNName;
		this.icon = icon;
		this.particle = particle;
		this.cdTime = cdTime;
		this.puclicCDTime = puclicCDTime;
		this.buffLastTime = buffLastTime;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArticleCNName() {
		return articleCNName;
	}

	public void setArticleCNName(String articleCNName) {
		this.articleCNName = articleCNName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getCdTime() {
		return cdTime;
	}

	public void setCdTime(int cdTime) {
		this.cdTime = cdTime;
	}

	public int getPuclicCDTime() {
		return puclicCDTime;
	}

	public void setPuclicCDTime(int puclicCDTime) {
		this.puclicCDTime = puclicCDTime;
	}

	public int getBuffLastTime() {
		return buffLastTime;
	}

	public void setBuffLastTime(int buffLastTime) {
		this.buffLastTime = buffLastTime;
	}

	public String getParticle() {
		return particle;
	}

	public void setParticle(String particle) {
		this.particle = particle;
	}

}
