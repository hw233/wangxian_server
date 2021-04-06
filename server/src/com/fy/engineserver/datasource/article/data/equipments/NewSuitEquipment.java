package com.fy.engineserver.datasource.article.data.equipments;

import java.util.Arrays;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.language.Translate;

public class NewSuitEquipment {

	//套装名
	private String suitName;
	private String suitName_stat;
	//套装部件
	private String suitPart[];
	private String suitPart_stat[];
	//套装生效
	private int suitResult[];
	private long qixue[];
	private long neigong[];
	private long waigong[];
	private long neifang[];
	private long waifang[];
	private long minzhong[];
	private int propNum = 6;
	public long propValues [][] = new long[propNum][]; 
	public int propNames [] = new int[propNum];
	public String propNames_ch [] = new String[propNum];
	public void initValue(){
		propValues[0]=qixue;
		propValues[1]=neigong;
		propValues[2]=waigong;
		propValues[3]=neifang;
		propValues[4]=waifang;
		propValues[5]=minzhong;
		propNames_ch[0]=Translate.血量;
		propNames_ch[1]=Translate.内功;
		propNames_ch[2]=Translate.外功;
		propNames_ch[3]=Translate.法术防御;
		propNames_ch[4]=Translate.物理防御;
		propNames_ch[5]=Translate.命中;
		propNames[0]=MagicWeaponConstant.hpNum;
		propNames[1]=MagicWeaponConstant.magicAttNum;
		propNames[2]=MagicWeaponConstant.physicAttNum;
		propNames[3]=MagicWeaponConstant.magicDefanceNum;
		propNames[4]=MagicWeaponConstant.physicDefanceNum;
		propNames[5]=MagicWeaponConstant.hitNum;
	}
	public String getSuitName() {
		return suitName;
	}
	public void setSuitName(String suitName) {
		this.suitName = suitName;
	}
	public String getSuitName_stat() {
		return suitName_stat;
	}
	public void setSuitName_stat(String suitName_stat) {
		this.suitName_stat = suitName_stat;
	}
	public String[] getSuitPart() {
		return suitPart;
	}
	public void setSuitPart(String[] suitPart) {
		this.suitPart = suitPart;
	}
	public String[] getSuitPart_stat() {
		return suitPart_stat;
	}
	public void setSuitPart_stat(String[] suitPart_stat) {
		this.suitPart_stat = suitPart_stat;
	}
	public int[] getSuitResult() {
		return suitResult;
	}
	public void setSuitResult(int[] suitResult) {
		this.suitResult = suitResult;
	}
	public long[] getQixue() {
		return qixue;
	}
	public void setQixue(long[] qixue) {
		this.qixue = qixue;
	}
	public long[] getNeigong() {
		return neigong;
	}
	public void setNeigong(long[] neigong) {
		this.neigong = neigong;
	}
	public long[] getWaigong() {
		return waigong;
	}
	public void setWaigong(long[] waigong) {
		this.waigong = waigong;
	}
	public long[] getNeifang() {
		return neifang;
	}
	public void setNeifang(long[] neifang) {
		this.neifang = neifang;
	}
	public long[] getWaifang() {
		return waifang;
	}
	public void setWaifang(long[] waifang) {
		this.waifang = waifang;
	}
	public long[] getMinzhong() {
		return minzhong;
	}
	public void setMinzhong(long[] minzhong) {
		this.minzhong = minzhong;
	}
	@Override
	public String toString() {
		return "NewSuitEquipment [suitName=" + suitName + ", suitName_stat=" + suitName_stat + ", suitPart=" + Arrays.toString(suitPart) + ", suitPart_stat=" + Arrays.toString(suitPart_stat) + ", suitResult=" + Arrays.toString(suitResult) + ", qixue=" + Arrays.toString(qixue) + ", neigong=" + Arrays.toString(neigong) + ", waigong=" + Arrays.toString(waigong) + ", neifang=" + Arrays.toString(neifang) + ", waifang=" + Arrays.toString(waifang) + ", minzhong=" + Arrays.toString(minzhong) + "]";
	}
	
}
