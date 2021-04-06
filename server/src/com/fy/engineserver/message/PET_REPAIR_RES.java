package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 宠物炼化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>repairEntityIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>repairEntityIds</td><td>long[]</td><td>repairEntityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>repairEntityNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>repairEntityNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>repairEntityNames[0]</td><td>String</td><td>repairEntityNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>repairEntityNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>repairEntityNames[1]</td><td>String</td><td>repairEntityNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>repairEntityNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>repairEntityNames[2]</td><td>String</td><td>repairEntityNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>repairLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>repairLevels</td><td>int[]</td><td>repairLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>repairSilverNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>repairSilverNums</td><td>int[]</td><td>repairSilverNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>repairValue</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>score</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>growthClass</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>quality</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strengthQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>dexterityQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>spellpowerQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>constitutionQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>dingliQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>minStrengthQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>minDexterityQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>minSpellpowerQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>minConstitutionQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>minDingliQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxStrengthQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxDexterityQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxSpellpowerQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxConstitutionQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxDingliQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showStrengthQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showDexterityQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showSpellpowerQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showConstitutionQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showDingliQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMinStrengthQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMinDexterityQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMinSpellpowerQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMinConstitutionQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMinDingliQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMaxStrengthQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMaxDexterityQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMaxSpellpowerQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMaxConstitutionQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMaxDingliQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>replace</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempscore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempGrowthClass</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempQuality</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempstrengthQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempdexterityQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempspellpowerQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempconstitutionQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempdingliQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempminStrengthQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempminDexterityQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempminSpellpowerQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempminConstitutionQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempminDingliQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempmaxStrengthQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempmaxDexterityQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempmaxSpellpowerQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempmaxConstitutionQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempmaxDingliQualityInit</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempshowStrengthQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempshowDexterityQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempshowSpellpowerQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempshowConstitutionQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempshowDingliQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempshowMinStrengthQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempshowMinDexterityQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempshowMinSpellpowerQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempshowMinConstitutionQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempshowMinDingliQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempshowMaxStrengthQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempshowMaxDexterityQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempshowMaxSpellpowerQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempshowMaxConstitutionQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempshowMaxDingliQuality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PET_REPAIR_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] repairEntityIds;
	String[] repairEntityNames;
	int[] repairLevels;
	int[] repairSilverNums;
	int repairValue;
	int score;
	byte growthClass;
	byte quality;
	int strengthQualityInit;
	int dexterityQualityInit;
	int spellpowerQualityInit;
	int constitutionQualityInit;
	int dingliQualityInit;
	int minStrengthQualityInit;
	int minDexterityQualityInit;
	int minSpellpowerQualityInit;
	int minConstitutionQualityInit;
	int minDingliQualityInit;
	int maxStrengthQualityInit;
	int maxDexterityQualityInit;
	int maxSpellpowerQualityInit;
	int maxConstitutionQualityInit;
	int maxDingliQualityInit;
	int showStrengthQuality;
	int showDexterityQuality;
	int showSpellpowerQuality;
	int showConstitutionQuality;
	int showDingliQuality;
	int showMinStrengthQuality;
	int showMinDexterityQuality;
	int showMinSpellpowerQuality;
	int showMinConstitutionQuality;
	int showMinDingliQuality;
	int showMaxStrengthQuality;
	int showMaxDexterityQuality;
	int showMaxSpellpowerQuality;
	int showMaxConstitutionQuality;
	int showMaxDingliQuality;
	boolean replace;
	int tempscore;
	byte tempGrowthClass;
	byte tempQuality;
	int tempstrengthQualityInit;
	int tempdexterityQualityInit;
	int tempspellpowerQualityInit;
	int tempconstitutionQualityInit;
	int tempdingliQualityInit;
	int tempminStrengthQualityInit;
	int tempminDexterityQualityInit;
	int tempminSpellpowerQualityInit;
	int tempminConstitutionQualityInit;
	int tempminDingliQualityInit;
	int tempmaxStrengthQualityInit;
	int tempmaxDexterityQualityInit;
	int tempmaxSpellpowerQualityInit;
	int tempmaxConstitutionQualityInit;
	int tempmaxDingliQualityInit;
	int tempshowStrengthQuality;
	int tempshowDexterityQuality;
	int tempshowSpellpowerQuality;
	int tempshowConstitutionQuality;
	int tempshowDingliQuality;
	int tempshowMinStrengthQuality;
	int tempshowMinDexterityQuality;
	int tempshowMinSpellpowerQuality;
	int tempshowMinConstitutionQuality;
	int tempshowMinDingliQuality;
	int tempshowMaxStrengthQuality;
	int tempshowMaxDexterityQuality;
	int tempshowMaxSpellpowerQuality;
	int tempshowMaxConstitutionQuality;
	int tempshowMaxDingliQuality;

	public PET_REPAIR_RES(){
	}

	public PET_REPAIR_RES(long seqNum,long[] repairEntityIds,String[] repairEntityNames,int[] repairLevels,int[] repairSilverNums,int repairValue,int score,byte growthClass,byte quality,int strengthQualityInit,int dexterityQualityInit,int spellpowerQualityInit,int constitutionQualityInit,int dingliQualityInit,int minStrengthQualityInit,int minDexterityQualityInit,int minSpellpowerQualityInit,int minConstitutionQualityInit,int minDingliQualityInit,int maxStrengthQualityInit,int maxDexterityQualityInit,int maxSpellpowerQualityInit,int maxConstitutionQualityInit,int maxDingliQualityInit,int showStrengthQuality,int showDexterityQuality,int showSpellpowerQuality,int showConstitutionQuality,int showDingliQuality,int showMinStrengthQuality,int showMinDexterityQuality,int showMinSpellpowerQuality,int showMinConstitutionQuality,int showMinDingliQuality,int showMaxStrengthQuality,int showMaxDexterityQuality,int showMaxSpellpowerQuality,int showMaxConstitutionQuality,int showMaxDingliQuality,boolean replace,int tempscore,byte tempGrowthClass,byte tempQuality,int tempstrengthQualityInit,int tempdexterityQualityInit,int tempspellpowerQualityInit,int tempconstitutionQualityInit,int tempdingliQualityInit,int tempminStrengthQualityInit,int tempminDexterityQualityInit,int tempminSpellpowerQualityInit,int tempminConstitutionQualityInit,int tempminDingliQualityInit,int tempmaxStrengthQualityInit,int tempmaxDexterityQualityInit,int tempmaxSpellpowerQualityInit,int tempmaxConstitutionQualityInit,int tempmaxDingliQualityInit,int tempshowStrengthQuality,int tempshowDexterityQuality,int tempshowSpellpowerQuality,int tempshowConstitutionQuality,int tempshowDingliQuality,int tempshowMinStrengthQuality,int tempshowMinDexterityQuality,int tempshowMinSpellpowerQuality,int tempshowMinConstitutionQuality,int tempshowMinDingliQuality,int tempshowMaxStrengthQuality,int tempshowMaxDexterityQuality,int tempshowMaxSpellpowerQuality,int tempshowMaxConstitutionQuality,int tempshowMaxDingliQuality){
		this.seqNum = seqNum;
		this.repairEntityIds = repairEntityIds;
		this.repairEntityNames = repairEntityNames;
		this.repairLevels = repairLevels;
		this.repairSilverNums = repairSilverNums;
		this.repairValue = repairValue;
		this.score = score;
		this.growthClass = growthClass;
		this.quality = quality;
		this.strengthQualityInit = strengthQualityInit;
		this.dexterityQualityInit = dexterityQualityInit;
		this.spellpowerQualityInit = spellpowerQualityInit;
		this.constitutionQualityInit = constitutionQualityInit;
		this.dingliQualityInit = dingliQualityInit;
		this.minStrengthQualityInit = minStrengthQualityInit;
		this.minDexterityQualityInit = minDexterityQualityInit;
		this.minSpellpowerQualityInit = minSpellpowerQualityInit;
		this.minConstitutionQualityInit = minConstitutionQualityInit;
		this.minDingliQualityInit = minDingliQualityInit;
		this.maxStrengthQualityInit = maxStrengthQualityInit;
		this.maxDexterityQualityInit = maxDexterityQualityInit;
		this.maxSpellpowerQualityInit = maxSpellpowerQualityInit;
		this.maxConstitutionQualityInit = maxConstitutionQualityInit;
		this.maxDingliQualityInit = maxDingliQualityInit;
		this.showStrengthQuality = showStrengthQuality;
		this.showDexterityQuality = showDexterityQuality;
		this.showSpellpowerQuality = showSpellpowerQuality;
		this.showConstitutionQuality = showConstitutionQuality;
		this.showDingliQuality = showDingliQuality;
		this.showMinStrengthQuality = showMinStrengthQuality;
		this.showMinDexterityQuality = showMinDexterityQuality;
		this.showMinSpellpowerQuality = showMinSpellpowerQuality;
		this.showMinConstitutionQuality = showMinConstitutionQuality;
		this.showMinDingliQuality = showMinDingliQuality;
		this.showMaxStrengthQuality = showMaxStrengthQuality;
		this.showMaxDexterityQuality = showMaxDexterityQuality;
		this.showMaxSpellpowerQuality = showMaxSpellpowerQuality;
		this.showMaxConstitutionQuality = showMaxConstitutionQuality;
		this.showMaxDingliQuality = showMaxDingliQuality;
		this.replace = replace;
		this.tempscore = tempscore;
		this.tempGrowthClass = tempGrowthClass;
		this.tempQuality = tempQuality;
		this.tempstrengthQualityInit = tempstrengthQualityInit;
		this.tempdexterityQualityInit = tempdexterityQualityInit;
		this.tempspellpowerQualityInit = tempspellpowerQualityInit;
		this.tempconstitutionQualityInit = tempconstitutionQualityInit;
		this.tempdingliQualityInit = tempdingliQualityInit;
		this.tempminStrengthQualityInit = tempminStrengthQualityInit;
		this.tempminDexterityQualityInit = tempminDexterityQualityInit;
		this.tempminSpellpowerQualityInit = tempminSpellpowerQualityInit;
		this.tempminConstitutionQualityInit = tempminConstitutionQualityInit;
		this.tempminDingliQualityInit = tempminDingliQualityInit;
		this.tempmaxStrengthQualityInit = tempmaxStrengthQualityInit;
		this.tempmaxDexterityQualityInit = tempmaxDexterityQualityInit;
		this.tempmaxSpellpowerQualityInit = tempmaxSpellpowerQualityInit;
		this.tempmaxConstitutionQualityInit = tempmaxConstitutionQualityInit;
		this.tempmaxDingliQualityInit = tempmaxDingliQualityInit;
		this.tempshowStrengthQuality = tempshowStrengthQuality;
		this.tempshowDexterityQuality = tempshowDexterityQuality;
		this.tempshowSpellpowerQuality = tempshowSpellpowerQuality;
		this.tempshowConstitutionQuality = tempshowConstitutionQuality;
		this.tempshowDingliQuality = tempshowDingliQuality;
		this.tempshowMinStrengthQuality = tempshowMinStrengthQuality;
		this.tempshowMinDexterityQuality = tempshowMinDexterityQuality;
		this.tempshowMinSpellpowerQuality = tempshowMinSpellpowerQuality;
		this.tempshowMinConstitutionQuality = tempshowMinConstitutionQuality;
		this.tempshowMinDingliQuality = tempshowMinDingliQuality;
		this.tempshowMaxStrengthQuality = tempshowMaxStrengthQuality;
		this.tempshowMaxDexterityQuality = tempshowMaxDexterityQuality;
		this.tempshowMaxSpellpowerQuality = tempshowMaxSpellpowerQuality;
		this.tempshowMaxConstitutionQuality = tempshowMaxConstitutionQuality;
		this.tempshowMaxDingliQuality = tempshowMaxDingliQuality;
	}

	public PET_REPAIR_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		repairEntityIds = new long[len];
		for(int i = 0 ; i < repairEntityIds.length ; i++){
			repairEntityIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		repairEntityNames = new String[len];
		for(int i = 0 ; i < repairEntityNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			repairEntityNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		repairLevels = new int[len];
		for(int i = 0 ; i < repairLevels.length ; i++){
			repairLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		repairSilverNums = new int[len];
		for(int i = 0 ; i < repairSilverNums.length ; i++){
			repairSilverNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		repairValue = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		score = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		growthClass = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		quality = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		strengthQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		dexterityQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		spellpowerQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		constitutionQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		dingliQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		minStrengthQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		minDexterityQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		minSpellpowerQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		minConstitutionQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		minDingliQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxStrengthQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxDexterityQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxSpellpowerQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxConstitutionQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxDingliQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showStrengthQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showDexterityQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showSpellpowerQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showConstitutionQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showDingliQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMinStrengthQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMinDexterityQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMinSpellpowerQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMinConstitutionQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMinDingliQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMaxStrengthQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMaxDexterityQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMaxSpellpowerQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMaxConstitutionQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		showMaxDingliQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		replace = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		tempscore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempGrowthClass = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		tempQuality = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		tempstrengthQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempdexterityQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempspellpowerQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempconstitutionQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempdingliQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempminStrengthQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempminDexterityQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempminSpellpowerQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempminConstitutionQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempminDingliQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempmaxStrengthQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempmaxDexterityQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempmaxSpellpowerQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempmaxConstitutionQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempmaxDingliQualityInit = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowStrengthQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowDexterityQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowSpellpowerQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowConstitutionQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowDingliQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMinStrengthQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMinDexterityQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMinSpellpowerQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMinConstitutionQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMinDingliQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMaxStrengthQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMaxDexterityQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMaxSpellpowerQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMaxConstitutionQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		tempshowMaxDingliQuality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x7000FF63;
	}

	public String getTypeDescription() {
		return "PET_REPAIR_RES";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}

	public long getSequnceNum(){
		return seqNum;
	}

	private int packet_length = 0;

	public int getLength() {
		if(packet_length > 0) return packet_length;
		int len =  mf.getNumOfByteForMessageLength() + 4 + 4;
		len += 4;
		len += repairEntityIds.length * 8;
		len += 4;
		for(int i = 0 ; i < repairEntityNames.length; i++){
			len += 2;
			try{
				len += repairEntityNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += repairLevels.length * 4;
		len += 4;
		len += repairSilverNums.length * 4;
		len += 4;
		len += 4;
		len += 1;
		len += 1;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 1;
		len += 4;
		len += 1;
		len += 1;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		int oldPos = buffer.position();
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putInt(repairEntityIds.length);
			for(int i = 0 ; i < repairEntityIds.length; i++){
				buffer.putLong(repairEntityIds[i]);
			}
			buffer.putInt(repairEntityNames.length);
			for(int i = 0 ; i < repairEntityNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = repairEntityNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(repairLevels.length);
			for(int i = 0 ; i < repairLevels.length; i++){
				buffer.putInt(repairLevels[i]);
			}
			buffer.putInt(repairSilverNums.length);
			for(int i = 0 ; i < repairSilverNums.length; i++){
				buffer.putInt(repairSilverNums[i]);
			}
			buffer.putInt(repairValue);
			buffer.putInt(score);
			buffer.put(growthClass);
			buffer.put(quality);
			buffer.putInt(strengthQualityInit);
			buffer.putInt(dexterityQualityInit);
			buffer.putInt(spellpowerQualityInit);
			buffer.putInt(constitutionQualityInit);
			buffer.putInt(dingliQualityInit);
			buffer.putInt(minStrengthQualityInit);
			buffer.putInt(minDexterityQualityInit);
			buffer.putInt(minSpellpowerQualityInit);
			buffer.putInt(minConstitutionQualityInit);
			buffer.putInt(minDingliQualityInit);
			buffer.putInt(maxStrengthQualityInit);
			buffer.putInt(maxDexterityQualityInit);
			buffer.putInt(maxSpellpowerQualityInit);
			buffer.putInt(maxConstitutionQualityInit);
			buffer.putInt(maxDingliQualityInit);
			buffer.putInt(showStrengthQuality);
			buffer.putInt(showDexterityQuality);
			buffer.putInt(showSpellpowerQuality);
			buffer.putInt(showConstitutionQuality);
			buffer.putInt(showDingliQuality);
			buffer.putInt(showMinStrengthQuality);
			buffer.putInt(showMinDexterityQuality);
			buffer.putInt(showMinSpellpowerQuality);
			buffer.putInt(showMinConstitutionQuality);
			buffer.putInt(showMinDingliQuality);
			buffer.putInt(showMaxStrengthQuality);
			buffer.putInt(showMaxDexterityQuality);
			buffer.putInt(showMaxSpellpowerQuality);
			buffer.putInt(showMaxConstitutionQuality);
			buffer.putInt(showMaxDingliQuality);
			buffer.put((byte)(replace==false?0:1));
			buffer.putInt(tempscore);
			buffer.put(tempGrowthClass);
			buffer.put(tempQuality);
			buffer.putInt(tempstrengthQualityInit);
			buffer.putInt(tempdexterityQualityInit);
			buffer.putInt(tempspellpowerQualityInit);
			buffer.putInt(tempconstitutionQualityInit);
			buffer.putInt(tempdingliQualityInit);
			buffer.putInt(tempminStrengthQualityInit);
			buffer.putInt(tempminDexterityQualityInit);
			buffer.putInt(tempminSpellpowerQualityInit);
			buffer.putInt(tempminConstitutionQualityInit);
			buffer.putInt(tempminDingliQualityInit);
			buffer.putInt(tempmaxStrengthQualityInit);
			buffer.putInt(tempmaxDexterityQualityInit);
			buffer.putInt(tempmaxSpellpowerQualityInit);
			buffer.putInt(tempmaxConstitutionQualityInit);
			buffer.putInt(tempmaxDingliQualityInit);
			buffer.putInt(tempshowStrengthQuality);
			buffer.putInt(tempshowDexterityQuality);
			buffer.putInt(tempshowSpellpowerQuality);
			buffer.putInt(tempshowConstitutionQuality);
			buffer.putInt(tempshowDingliQuality);
			buffer.putInt(tempshowMinStrengthQuality);
			buffer.putInt(tempshowMinDexterityQuality);
			buffer.putInt(tempshowMinSpellpowerQuality);
			buffer.putInt(tempshowMinConstitutionQuality);
			buffer.putInt(tempshowMinDingliQuality);
			buffer.putInt(tempshowMaxStrengthQuality);
			buffer.putInt(tempshowMaxDexterityQuality);
			buffer.putInt(tempshowMaxSpellpowerQuality);
			buffer.putInt(tempshowMaxConstitutionQuality);
			buffer.putInt(tempshowMaxDingliQuality);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		int newPos = buffer.position();
		buffer.position(oldPos);
		buffer.put(mf.numberToByteArray(newPos-oldPos,mf.getNumOfByteForMessageLength()));
		buffer.position(newPos);
		return newPos-oldPos;
	}

	/**
	 * 获取属性：
	 *	三种炼妖级别的三种物品id
	 */
	public long[] getRepairEntityIds(){
		return repairEntityIds;
	}

	/**
	 * 设置属性：
	 *	三种炼妖级别的三种物品id
	 */
	public void setRepairEntityIds(long[] repairEntityIds){
		this.repairEntityIds = repairEntityIds;
	}

	/**
	 * 获取属性：
	 *	三种炼妖级别的三种物品name
	 */
	public String[] getRepairEntityNames(){
		return repairEntityNames;
	}

	/**
	 * 设置属性：
	 *	三种炼妖级别的三种物品name
	 */
	public void setRepairEntityNames(String[] repairEntityNames){
		this.repairEntityNames = repairEntityNames;
	}

	/**
	 * 获取属性：
	 *	三种炼妖级别对应的vip级别
	 */
	public int[] getRepairLevels(){
		return repairLevels;
	}

	/**
	 * 设置属性：
	 *	三种炼妖级别对应的vip级别
	 */
	public void setRepairLevels(int[] repairLevels){
		this.repairLevels = repairLevels;
	}

	/**
	 * 获取属性：
	 *	三种炼妖级别对应的刷新银子数量
	 */
	public int[] getRepairSilverNums(){
		return repairSilverNums;
	}

	/**
	 * 设置属性：
	 *	三种炼妖级别对应的刷新银子数量
	 */
	public void setRepairSilverNums(int[] repairSilverNums){
		this.repairSilverNums = repairSilverNums;
	}

	/**
	 * 获取属性：
	 *	炼妖值当前
	 */
	public int getRepairValue(){
		return repairValue;
	}

	/**
	 * 设置属性：
	 *	炼妖值当前
	 */
	public void setRepairValue(int repairValue){
		this.repairValue = repairValue;
	}

	/**
	 * 获取属性：
	 *	得分
	 */
	public int getScore(){
		return score;
	}

	/**
	 * 设置属性：
	 *	得分
	 */
	public void setScore(int score){
		this.score = score;
	}

	/**
	 * 获取属性：
	 *	宠物的成长品质
	 */
	public byte getGrowthClass(){
		return growthClass;
	}

	/**
	 * 设置属性：
	 *	宠物的成长品质
	 */
	public void setGrowthClass(byte growthClass){
		this.growthClass = growthClass;
	}

	/**
	 * 获取属性：
	 *	宠物的品质，非为5档，0~4：普兽（白）、灵兽（绿）、仙兽（蓝）、神兽（紫）、圣兽（橙），需要鉴定
	 */
	public byte getQuality(){
		return quality;
	}

	/**
	 * 设置属性：
	 *	宠物的品质，非为5档，0~4：普兽（白）、灵兽（绿）、仙兽（蓝）、神兽（紫）、圣兽（橙），需要鉴定
	 */
	public void setQuality(byte quality){
		this.quality = quality;
	}

	/**
	 * 获取属性：
	 *	宠物的力量初值
	 */
	public int getStrengthQualityInit(){
		return strengthQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的力量初值
	 */
	public void setStrengthQualityInit(int strengthQualityInit){
		this.strengthQualityInit = strengthQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的身法初值
	 */
	public int getDexterityQualityInit(){
		return dexterityQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的身法初值
	 */
	public void setDexterityQualityInit(int dexterityQualityInit){
		this.dexterityQualityInit = dexterityQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力初值
	 */
	public int getSpellpowerQualityInit(){
		return spellpowerQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力初值
	 */
	public void setSpellpowerQualityInit(int spellpowerQualityInit){
		this.spellpowerQualityInit = spellpowerQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力初值
	 */
	public int getConstitutionQualityInit(){
		return constitutionQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力初值
	 */
	public void setConstitutionQualityInit(int constitutionQualityInit){
		this.constitutionQualityInit = constitutionQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的定力初值
	 */
	public int getDingliQualityInit(){
		return dingliQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的定力初值
	 */
	public void setDingliQualityInit(int dingliQualityInit){
		this.dingliQualityInit = dingliQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的力量初值(最小值)
	 */
	public int getMinStrengthQualityInit(){
		return minStrengthQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的力量初值(最小值)
	 */
	public void setMinStrengthQualityInit(int minStrengthQualityInit){
		this.minStrengthQualityInit = minStrengthQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的身法初值(最小值)
	 */
	public int getMinDexterityQualityInit(){
		return minDexterityQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的身法初值(最小值)
	 */
	public void setMinDexterityQualityInit(int minDexterityQualityInit){
		this.minDexterityQualityInit = minDexterityQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力初值(最小值)
	 */
	public int getMinSpellpowerQualityInit(){
		return minSpellpowerQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力初值(最小值)
	 */
	public void setMinSpellpowerQualityInit(int minSpellpowerQualityInit){
		this.minSpellpowerQualityInit = minSpellpowerQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力初值(最小值)
	 */
	public int getMinConstitutionQualityInit(){
		return minConstitutionQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力初值(最小值)
	 */
	public void setMinConstitutionQualityInit(int minConstitutionQualityInit){
		this.minConstitutionQualityInit = minConstitutionQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的定力初值(最小值)
	 */
	public int getMinDingliQualityInit(){
		return minDingliQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的定力初值(最小值)
	 */
	public void setMinDingliQualityInit(int minDingliQualityInit){
		this.minDingliQualityInit = minDingliQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的力量初值(满值)
	 */
	public int getMaxStrengthQualityInit(){
		return maxStrengthQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的力量初值(满值)
	 */
	public void setMaxStrengthQualityInit(int maxStrengthQualityInit){
		this.maxStrengthQualityInit = maxStrengthQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的身法初值(满值)
	 */
	public int getMaxDexterityQualityInit(){
		return maxDexterityQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的身法初值(满值)
	 */
	public void setMaxDexterityQualityInit(int maxDexterityQualityInit){
		this.maxDexterityQualityInit = maxDexterityQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力初值(满值)
	 */
	public int getMaxSpellpowerQualityInit(){
		return maxSpellpowerQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力初值(满值)
	 */
	public void setMaxSpellpowerQualityInit(int maxSpellpowerQualityInit){
		this.maxSpellpowerQualityInit = maxSpellpowerQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力初值(满值)
	 */
	public int getMaxConstitutionQualityInit(){
		return maxConstitutionQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力初值(满值)
	 */
	public void setMaxConstitutionQualityInit(int maxConstitutionQualityInit){
		this.maxConstitutionQualityInit = maxConstitutionQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的定力初值(满值)
	 */
	public int getMaxDingliQualityInit(){
		return maxDingliQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的定力初值(满值)
	 */
	public void setMaxDingliQualityInit(int maxDingliQualityInit){
		this.maxDingliQualityInit = maxDingliQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的力量资质
	 */
	public int getShowStrengthQuality(){
		return showStrengthQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的力量资质
	 */
	public void setShowStrengthQuality(int showStrengthQuality){
		this.showStrengthQuality = showStrengthQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的身法资质
	 */
	public int getShowDexterityQuality(){
		return showDexterityQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的身法资质
	 */
	public void setShowDexterityQuality(int showDexterityQuality){
		this.showDexterityQuality = showDexterityQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力资质
	 */
	public int getShowSpellpowerQuality(){
		return showSpellpowerQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力资质
	 */
	public void setShowSpellpowerQuality(int showSpellpowerQuality){
		this.showSpellpowerQuality = showSpellpowerQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力资质
	 */
	public int getShowConstitutionQuality(){
		return showConstitutionQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力资质
	 */
	public void setShowConstitutionQuality(int showConstitutionQuality){
		this.showConstitutionQuality = showConstitutionQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的定力资质
	 */
	public int getShowDingliQuality(){
		return showDingliQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的定力资质
	 */
	public void setShowDingliQuality(int showDingliQuality){
		this.showDingliQuality = showDingliQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的力量资质(最小值)
	 */
	public int getShowMinStrengthQuality(){
		return showMinStrengthQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的力量资质(最小值)
	 */
	public void setShowMinStrengthQuality(int showMinStrengthQuality){
		this.showMinStrengthQuality = showMinStrengthQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的身法资质(最小值)
	 */
	public int getShowMinDexterityQuality(){
		return showMinDexterityQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的身法资质(最小值)
	 */
	public void setShowMinDexterityQuality(int showMinDexterityQuality){
		this.showMinDexterityQuality = showMinDexterityQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力资质(最小值)
	 */
	public int getShowMinSpellpowerQuality(){
		return showMinSpellpowerQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力资质(最小值)
	 */
	public void setShowMinSpellpowerQuality(int showMinSpellpowerQuality){
		this.showMinSpellpowerQuality = showMinSpellpowerQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力资质(最小值)
	 */
	public int getShowMinConstitutionQuality(){
		return showMinConstitutionQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力资质(最小值)
	 */
	public void setShowMinConstitutionQuality(int showMinConstitutionQuality){
		this.showMinConstitutionQuality = showMinConstitutionQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的定力资质(最小值)
	 */
	public int getShowMinDingliQuality(){
		return showMinDingliQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的定力资质(最小值)
	 */
	public void setShowMinDingliQuality(int showMinDingliQuality){
		this.showMinDingliQuality = showMinDingliQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的力量资质(满值)
	 */
	public int getShowMaxStrengthQuality(){
		return showMaxStrengthQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的力量资质(满值)
	 */
	public void setShowMaxStrengthQuality(int showMaxStrengthQuality){
		this.showMaxStrengthQuality = showMaxStrengthQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的身法资质(满值)
	 */
	public int getShowMaxDexterityQuality(){
		return showMaxDexterityQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的身法资质(满值)
	 */
	public void setShowMaxDexterityQuality(int showMaxDexterityQuality){
		this.showMaxDexterityQuality = showMaxDexterityQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力资质(满值)
	 */
	public int getShowMaxSpellpowerQuality(){
		return showMaxSpellpowerQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力资质(满值)
	 */
	public void setShowMaxSpellpowerQuality(int showMaxSpellpowerQuality){
		this.showMaxSpellpowerQuality = showMaxSpellpowerQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力资质(满值)
	 */
	public int getShowMaxConstitutionQuality(){
		return showMaxConstitutionQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力资质(满值)
	 */
	public void setShowMaxConstitutionQuality(int showMaxConstitutionQuality){
		this.showMaxConstitutionQuality = showMaxConstitutionQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的定力资质(满值)
	 */
	public int getShowMaxDingliQuality(){
		return showMaxDingliQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的定力资质(满值)
	 */
	public void setShowMaxDingliQuality(int showMaxDingliQuality){
		this.showMaxDingliQuality = showMaxDingliQuality;
	}

	/**
	 * 获取属性：
	 *	是否已经替换过(true 下边不做处理)
	 */
	public boolean getReplace(){
		return replace;
	}

	/**
	 * 设置属性：
	 *	是否已经替换过(true 下边不做处理)
	 */
	public void setReplace(boolean replace){
		this.replace = replace;
	}

	/**
	 * 获取属性：
	 *	得分
	 */
	public int getTempscore(){
		return tempscore;
	}

	/**
	 * 设置属性：
	 *	得分
	 */
	public void setTempscore(int tempscore){
		this.tempscore = tempscore;
	}

	/**
	 * 获取属性：
	 *	宠物的成长品质
	 */
	public byte getTempGrowthClass(){
		return tempGrowthClass;
	}

	/**
	 * 设置属性：
	 *	宠物的成长品质
	 */
	public void setTempGrowthClass(byte tempGrowthClass){
		this.tempGrowthClass = tempGrowthClass;
	}

	/**
	 * 获取属性：
	 *	宠物的品质，非为5档，0~4：普兽（白）、灵兽（绿）、仙兽（蓝）、神兽（紫）、圣兽（橙），需要鉴定
	 */
	public byte getTempQuality(){
		return tempQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的品质，非为5档，0~4：普兽（白）、灵兽（绿）、仙兽（蓝）、神兽（紫）、圣兽（橙），需要鉴定
	 */
	public void setTempQuality(byte tempQuality){
		this.tempQuality = tempQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的力量初值
	 */
	public int getTempstrengthQualityInit(){
		return tempstrengthQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的力量初值
	 */
	public void setTempstrengthQualityInit(int tempstrengthQualityInit){
		this.tempstrengthQualityInit = tempstrengthQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的身法初值
	 */
	public int getTempdexterityQualityInit(){
		return tempdexterityQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的身法初值
	 */
	public void setTempdexterityQualityInit(int tempdexterityQualityInit){
		this.tempdexterityQualityInit = tempdexterityQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力初值
	 */
	public int getTempspellpowerQualityInit(){
		return tempspellpowerQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力初值
	 */
	public void setTempspellpowerQualityInit(int tempspellpowerQualityInit){
		this.tempspellpowerQualityInit = tempspellpowerQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力初值
	 */
	public int getTempconstitutionQualityInit(){
		return tempconstitutionQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力初值
	 */
	public void setTempconstitutionQualityInit(int tempconstitutionQualityInit){
		this.tempconstitutionQualityInit = tempconstitutionQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的定力初值
	 */
	public int getTempdingliQualityInit(){
		return tempdingliQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的定力初值
	 */
	public void setTempdingliQualityInit(int tempdingliQualityInit){
		this.tempdingliQualityInit = tempdingliQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的力量初值(最小值)
	 */
	public int getTempminStrengthQualityInit(){
		return tempminStrengthQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的力量初值(最小值)
	 */
	public void setTempminStrengthQualityInit(int tempminStrengthQualityInit){
		this.tempminStrengthQualityInit = tempminStrengthQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的身法初值(最小值)
	 */
	public int getTempminDexterityQualityInit(){
		return tempminDexterityQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的身法初值(最小值)
	 */
	public void setTempminDexterityQualityInit(int tempminDexterityQualityInit){
		this.tempminDexterityQualityInit = tempminDexterityQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力初值(最小值)
	 */
	public int getTempminSpellpowerQualityInit(){
		return tempminSpellpowerQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力初值(最小值)
	 */
	public void setTempminSpellpowerQualityInit(int tempminSpellpowerQualityInit){
		this.tempminSpellpowerQualityInit = tempminSpellpowerQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力初值(最小值)
	 */
	public int getTempminConstitutionQualityInit(){
		return tempminConstitutionQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力初值(最小值)
	 */
	public void setTempminConstitutionQualityInit(int tempminConstitutionQualityInit){
		this.tempminConstitutionQualityInit = tempminConstitutionQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的定力初值(最小值)
	 */
	public int getTempminDingliQualityInit(){
		return tempminDingliQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的定力初值(最小值)
	 */
	public void setTempminDingliQualityInit(int tempminDingliQualityInit){
		this.tempminDingliQualityInit = tempminDingliQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的力量初值(满值)
	 */
	public int getTempmaxStrengthQualityInit(){
		return tempmaxStrengthQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的力量初值(满值)
	 */
	public void setTempmaxStrengthQualityInit(int tempmaxStrengthQualityInit){
		this.tempmaxStrengthQualityInit = tempmaxStrengthQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的身法初值(满值)
	 */
	public int getTempmaxDexterityQualityInit(){
		return tempmaxDexterityQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的身法初值(满值)
	 */
	public void setTempmaxDexterityQualityInit(int tempmaxDexterityQualityInit){
		this.tempmaxDexterityQualityInit = tempmaxDexterityQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力初值(满值)
	 */
	public int getTempmaxSpellpowerQualityInit(){
		return tempmaxSpellpowerQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力初值(满值)
	 */
	public void setTempmaxSpellpowerQualityInit(int tempmaxSpellpowerQualityInit){
		this.tempmaxSpellpowerQualityInit = tempmaxSpellpowerQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力初值(满值)
	 */
	public int getTempmaxConstitutionQualityInit(){
		return tempmaxConstitutionQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力初值(满值)
	 */
	public void setTempmaxConstitutionQualityInit(int tempmaxConstitutionQualityInit){
		this.tempmaxConstitutionQualityInit = tempmaxConstitutionQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的定力初值(满值)
	 */
	public int getTempmaxDingliQualityInit(){
		return tempmaxDingliQualityInit;
	}

	/**
	 * 设置属性：
	 *	宠物的定力初值(满值)
	 */
	public void setTempmaxDingliQualityInit(int tempmaxDingliQualityInit){
		this.tempmaxDingliQualityInit = tempmaxDingliQualityInit;
	}

	/**
	 * 获取属性：
	 *	宠物的力量资质
	 */
	public int getTempshowStrengthQuality(){
		return tempshowStrengthQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的力量资质
	 */
	public void setTempshowStrengthQuality(int tempshowStrengthQuality){
		this.tempshowStrengthQuality = tempshowStrengthQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的身法资质
	 */
	public int getTempshowDexterityQuality(){
		return tempshowDexterityQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的身法资质
	 */
	public void setTempshowDexterityQuality(int tempshowDexterityQuality){
		this.tempshowDexterityQuality = tempshowDexterityQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力资质
	 */
	public int getTempshowSpellpowerQuality(){
		return tempshowSpellpowerQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力资质
	 */
	public void setTempshowSpellpowerQuality(int tempshowSpellpowerQuality){
		this.tempshowSpellpowerQuality = tempshowSpellpowerQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力资质
	 */
	public int getTempshowConstitutionQuality(){
		return tempshowConstitutionQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力资质
	 */
	public void setTempshowConstitutionQuality(int tempshowConstitutionQuality){
		this.tempshowConstitutionQuality = tempshowConstitutionQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的定力资质
	 */
	public int getTempshowDingliQuality(){
		return tempshowDingliQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的定力资质
	 */
	public void setTempshowDingliQuality(int tempshowDingliQuality){
		this.tempshowDingliQuality = tempshowDingliQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的力量资质(最小值)
	 */
	public int getTempshowMinStrengthQuality(){
		return tempshowMinStrengthQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的力量资质(最小值)
	 */
	public void setTempshowMinStrengthQuality(int tempshowMinStrengthQuality){
		this.tempshowMinStrengthQuality = tempshowMinStrengthQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的身法资质(最小值)
	 */
	public int getTempshowMinDexterityQuality(){
		return tempshowMinDexterityQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的身法资质(最小值)
	 */
	public void setTempshowMinDexterityQuality(int tempshowMinDexterityQuality){
		this.tempshowMinDexterityQuality = tempshowMinDexterityQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力资质(最小值)
	 */
	public int getTempshowMinSpellpowerQuality(){
		return tempshowMinSpellpowerQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力资质(最小值)
	 */
	public void setTempshowMinSpellpowerQuality(int tempshowMinSpellpowerQuality){
		this.tempshowMinSpellpowerQuality = tempshowMinSpellpowerQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力资质(最小值)
	 */
	public int getTempshowMinConstitutionQuality(){
		return tempshowMinConstitutionQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力资质(最小值)
	 */
	public void setTempshowMinConstitutionQuality(int tempshowMinConstitutionQuality){
		this.tempshowMinConstitutionQuality = tempshowMinConstitutionQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的定力资质(最小值)
	 */
	public int getTempshowMinDingliQuality(){
		return tempshowMinDingliQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的定力资质(最小值)
	 */
	public void setTempshowMinDingliQuality(int tempshowMinDingliQuality){
		this.tempshowMinDingliQuality = tempshowMinDingliQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的力量资质(满值)
	 */
	public int getTempshowMaxStrengthQuality(){
		return tempshowMaxStrengthQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的力量资质(满值)
	 */
	public void setTempshowMaxStrengthQuality(int tempshowMaxStrengthQuality){
		this.tempshowMaxStrengthQuality = tempshowMaxStrengthQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的身法资质(满值)
	 */
	public int getTempshowMaxDexterityQuality(){
		return tempshowMaxDexterityQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的身法资质(满值)
	 */
	public void setTempshowMaxDexterityQuality(int tempshowMaxDexterityQuality){
		this.tempshowMaxDexterityQuality = tempshowMaxDexterityQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的灵力资质(满值)
	 */
	public int getTempshowMaxSpellpowerQuality(){
		return tempshowMaxSpellpowerQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的灵力资质(满值)
	 */
	public void setTempshowMaxSpellpowerQuality(int tempshowMaxSpellpowerQuality){
		this.tempshowMaxSpellpowerQuality = tempshowMaxSpellpowerQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的耐力资质(满值)
	 */
	public int getTempshowMaxConstitutionQuality(){
		return tempshowMaxConstitutionQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的耐力资质(满值)
	 */
	public void setTempshowMaxConstitutionQuality(int tempshowMaxConstitutionQuality){
		this.tempshowMaxConstitutionQuality = tempshowMaxConstitutionQuality;
	}

	/**
	 * 获取属性：
	 *	宠物的定力资质(满值)
	 */
	public int getTempshowMaxDingliQuality(){
		return tempshowMaxDingliQuality;
	}

	/**
	 * 设置属性：
	 *	宠物的定力资质(满值)
	 */
	public void setTempshowMaxDingliQuality(int tempshowMaxDingliQuality){
		this.tempshowMaxDingliQuality = tempshowMaxDingliQuality;
	}

}