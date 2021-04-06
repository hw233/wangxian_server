package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求坐骑相关数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>horseId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIds</td><td>int[]</td><td>skillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillLevel.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillLevel</td><td>int[]</td><td>skillLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIcons.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIcons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIcons[0]</td><td>String</td><td>skillIcons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIcons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIcons[1]</td><td>String</td><td>skillIcons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIcons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIcons[2]</td><td>String</td><td>skillIcons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillIds</td><td>int[]</td><td>tempSkillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillIcons.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillIcons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillIcons[0]</td><td>String</td><td>tempSkillIcons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillIcons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillIcons[1]</td><td>String</td><td>tempSkillIcons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillIcons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillIcons[2]</td><td>String</td><td>tempSkillIcons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillNames[0]</td><td>String</td><td>tempSkillNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillNames[1]</td><td>String</td><td>tempSkillNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillNames[2]</td><td>String</td><td>tempSkillNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillDes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillDes[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillDes[0]</td><td>String</td><td>tempSkillDes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillDes[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillDes[1]</td><td>String</td><td>tempSkillDes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tempSkillDes[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempSkillDes[2]</td><td>String</td><td>tempSkillDes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>free4Rank</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>free4Skill</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxBloodStar</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_HORSE_DATA_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	long horseId;
	int[] skillIds;
	int[] skillLevel;
	String[] skillIcons;
	int[] tempSkillIds;
	String[] tempSkillIcons;
	String[] tempSkillNames;
	String[] tempSkillDes;
	int free4Rank;
	int free4Skill;
	int maxBloodStar;

	public QUERY_HORSE_DATA_RES(){
	}

	public QUERY_HORSE_DATA_RES(long seqNum,long playerId,long horseId,int[] skillIds,int[] skillLevel,String[] skillIcons,int[] tempSkillIds,String[] tempSkillIcons,String[] tempSkillNames,String[] tempSkillDes,int free4Rank,int free4Skill,int maxBloodStar){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.horseId = horseId;
		this.skillIds = skillIds;
		this.skillLevel = skillLevel;
		this.skillIcons = skillIcons;
		this.tempSkillIds = tempSkillIds;
		this.tempSkillIcons = tempSkillIcons;
		this.tempSkillNames = tempSkillNames;
		this.tempSkillDes = tempSkillDes;
		this.free4Rank = free4Rank;
		this.free4Skill = free4Skill;
		this.maxBloodStar = maxBloodStar;
	}

	public QUERY_HORSE_DATA_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		horseId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillIds = new int[len];
		for(int i = 0 ; i < skillIds.length ; i++){
			skillIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillLevel = new int[len];
		for(int i = 0 ; i < skillLevel.length ; i++){
			skillLevel[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillIcons = new String[len];
		for(int i = 0 ; i < skillIcons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillIcons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		tempSkillIds = new int[len];
		for(int i = 0 ; i < tempSkillIds.length ; i++){
			tempSkillIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		tempSkillIcons = new String[len];
		for(int i = 0 ; i < tempSkillIcons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			tempSkillIcons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		tempSkillNames = new String[len];
		for(int i = 0 ; i < tempSkillNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			tempSkillNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		tempSkillDes = new String[len];
		for(int i = 0 ; i < tempSkillDes.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			tempSkillDes[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		free4Rank = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		free4Skill = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxBloodStar = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F0EF49;
	}

	public String getTypeDescription() {
		return "QUERY_HORSE_DATA_RES";
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
		len += 8;
		len += 8;
		len += 4;
		len += skillIds.length * 4;
		len += 4;
		len += skillLevel.length * 4;
		len += 4;
		for(int i = 0 ; i < skillIcons.length; i++){
			len += 2;
			try{
				len += skillIcons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += tempSkillIds.length * 4;
		len += 4;
		for(int i = 0 ; i < tempSkillIcons.length; i++){
			len += 2;
			try{
				len += tempSkillIcons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < tempSkillNames.length; i++){
			len += 2;
			try{
				len += tempSkillNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < tempSkillDes.length; i++){
			len += 2;
			try{
				len += tempSkillDes[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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

			buffer.putLong(playerId);
			buffer.putLong(horseId);
			buffer.putInt(skillIds.length);
			for(int i = 0 ; i < skillIds.length; i++){
				buffer.putInt(skillIds[i]);
			}
			buffer.putInt(skillLevel.length);
			for(int i = 0 ; i < skillLevel.length; i++){
				buffer.putInt(skillLevel[i]);
			}
			buffer.putInt(skillIcons.length);
			for(int i = 0 ; i < skillIcons.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillIcons[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(tempSkillIds.length);
			for(int i = 0 ; i < tempSkillIds.length; i++){
				buffer.putInt(tempSkillIds[i]);
			}
			buffer.putInt(tempSkillIcons.length);
			for(int i = 0 ; i < tempSkillIcons.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = tempSkillIcons[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(tempSkillNames.length);
			for(int i = 0 ; i < tempSkillNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = tempSkillNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(tempSkillDes.length);
			for(int i = 0 ; i < tempSkillDes.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = tempSkillDes[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(free4Rank);
			buffer.putInt(free4Skill);
			buffer.putInt(maxBloodStar);
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
	 *	角色id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	角色id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	马的id
	 */
	public long getHorseId(){
		return horseId;
	}

	/**
	 * 设置属性：
	 *	马的id
	 */
	public void setHorseId(long horseId){
		this.horseId = horseId;
	}

	/**
	 * 获取属性：
	 *	技能id列表
	 */
	public int[] getSkillIds(){
		return skillIds;
	}

	/**
	 * 设置属性：
	 *	技能id列表
	 */
	public void setSkillIds(int[] skillIds){
		this.skillIds = skillIds;
	}

	/**
	 * 获取属性：
	 *	技能等级
	 */
	public int[] getSkillLevel(){
		return skillLevel;
	}

	/**
	 * 设置属性：
	 *	技能等级
	 */
	public void setSkillLevel(int[] skillLevel){
		this.skillLevel = skillLevel;
	}

	/**
	 * 获取属性：
	 *	技能icon
	 */
	public String[] getSkillIcons(){
		return skillIcons;
	}

	/**
	 * 设置属性：
	 *	技能icon
	 */
	public void setSkillIcons(String[] skillIcons){
		this.skillIcons = skillIcons;
	}

	/**
	 * 获取属性：
	 *	临时技能（刷新出来没替换的技能列表）
	 */
	public int[] getTempSkillIds(){
		return tempSkillIds;
	}

	/**
	 * 设置属性：
	 *	临时技能（刷新出来没替换的技能列表）
	 */
	public void setTempSkillIds(int[] tempSkillIds){
		this.tempSkillIds = tempSkillIds;
	}

	/**
	 * 获取属性：
	 *	临时技能icon（刷新出来没替换的技能列表
	 */
	public String[] getTempSkillIcons(){
		return tempSkillIcons;
	}

	/**
	 * 设置属性：
	 *	临时技能icon（刷新出来没替换的技能列表
	 */
	public void setTempSkillIcons(String[] tempSkillIcons){
		this.tempSkillIcons = tempSkillIcons;
	}

	/**
	 * 获取属性：
	 *	临时技能名（刷新出来没替换的技能列表
	 */
	public String[] getTempSkillNames(){
		return tempSkillNames;
	}

	/**
	 * 设置属性：
	 *	临时技能名（刷新出来没替换的技能列表
	 */
	public void setTempSkillNames(String[] tempSkillNames){
		this.tempSkillNames = tempSkillNames;
	}

	/**
	 * 获取属性：
	 *	临时技能描述（刷新出来没替换的技能列表
	 */
	public String[] getTempSkillDes(){
		return tempSkillDes;
	}

	/**
	 * 设置属性：
	 *	临时技能描述（刷新出来没替换的技能列表
	 */
	public void setTempSkillDes(String[] tempSkillDes){
		this.tempSkillDes = tempSkillDes;
	}

	/**
	 * 获取属性：
	 *	剩余免费阶星级培养次数
	 */
	public int getFree4Rank(){
		return free4Rank;
	}

	/**
	 * 设置属性：
	 *	剩余免费阶星级培养次数
	 */
	public void setFree4Rank(int free4Rank){
		this.free4Rank = free4Rank;
	}

	/**
	 * 获取属性：
	 *	剩余免费刷新技能次数
	 */
	public int getFree4Skill(){
		return free4Skill;
	}

	/**
	 * 设置属性：
	 *	剩余免费刷新技能次数
	 */
	public void setFree4Skill(int free4Skill){
		this.free4Skill = free4Skill;
	}

	/**
	 * 获取属性：
	 *	最大可升血脉星级
	 */
	public int getMaxBloodStar(){
		return maxBloodStar;
	}

	/**
	 * 设置属性：
	 *	最大可升血脉星级
	 */
	public void setMaxBloodStar(int maxBloodStar){
		this.maxBloodStar = maxBloodStar;
	}

}