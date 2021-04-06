package com.fy.engineserver.qiancengta.info;

import java.util.Random;

import com.fy.engineserver.sprite.Player;

/**
 * 千层塔   物品掉落数据
 * 
 *
 */
public class QianCengTa_FlopSet {

	public static int FLOP_CONSULT = 10000;			//随机参照
	
	private String clientShowName;			//客户端显示名称
	
	private String[] articleNames;			//物品名称
	
	private int random;						//几率		万分之
	
	private int num;						//数量
	
	private int colorType;					//颜色
	
	private boolean isBind;					//是否绑定
	
	public void setClientShowName(String clientShowName) {
		this.clientShowName = clientShowName;
	}

	public String getClientShowName() {
		return clientShowName;
	}

	public void setArticleNames(String[] articleNames) {
		this.articleNames = articleNames;
	}

	public String[] getArticleNames() {
		return articleNames;
	}

	public void setRandom(int random) {
		this.random = random;
	}

	public int getRandom() {
		return random;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public int getColorType() {
		return colorType;
	}
	
	public String getFlopName(Player player){
		if (articleNames.length == 1) {
			return articleNames[0];
		}else{
			Random r = new Random();
			int next = r.nextInt(articleNames.length);
			//按职业顺序录入的
			return articleNames[next];
		}
	}
	
	public String toString(){
		String articleBuff = "";
		for (int i = 0; i < articleNames.length; i++) {
			articleBuff = articleBuff + articleNames[i] + ",";
		}
		return clientShowName + "=" + articleBuff + " r=" + random + " n=" + num + " c=" + colorType;
	}

	public void setBind(boolean isBind) {
		this.isBind = isBind;
	}

	public boolean isBind() {
		return isBind;
	}

}
