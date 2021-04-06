package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 取渡劫关于技能的信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>dujieNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillOneLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillOneLevels</td><td>int[]</td><td>skillOneLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>needOneDuJies.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needOneDuJies</td><td>int[]</td><td>needOneDuJies.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillTwoLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillTwoLevels</td><td>int[]</td><td>skillTwoLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>needTwoDuJies.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needTwoDuJies</td><td>int[]</td><td>needTwoDuJies.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_DUJIE_SKILL_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int dujieNum;
	int[] skillOneLevels;
	int[] needOneDuJies;
	int[] skillTwoLevels;
	int[] needTwoDuJies;

	public QUERY_DUJIE_SKILL_INFO_RES(){
	}

	public QUERY_DUJIE_SKILL_INFO_RES(long seqNum,int dujieNum,int[] skillOneLevels,int[] needOneDuJies,int[] skillTwoLevels,int[] needTwoDuJies){
		this.seqNum = seqNum;
		this.dujieNum = dujieNum;
		this.skillOneLevels = skillOneLevels;
		this.needOneDuJies = needOneDuJies;
		this.skillTwoLevels = skillTwoLevels;
		this.needTwoDuJies = needTwoDuJies;
	}

	public QUERY_DUJIE_SKILL_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		dujieNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillOneLevels = new int[len];
		for(int i = 0 ; i < skillOneLevels.length ; i++){
			skillOneLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		needOneDuJies = new int[len];
		for(int i = 0 ; i < needOneDuJies.length ; i++){
			needOneDuJies[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillTwoLevels = new int[len];
		for(int i = 0 ; i < skillTwoLevels.length ; i++){
			skillTwoLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		needTwoDuJies = new int[len];
		for(int i = 0 ; i < needTwoDuJies.length ; i++){
			needTwoDuJies[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x700000EB;
	}

	public String getTypeDescription() {
		return "QUERY_DUJIE_SKILL_INFO_RES";
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
		len += 4;
		len += skillOneLevels.length * 4;
		len += 4;
		len += needOneDuJies.length * 4;
		len += 4;
		len += skillTwoLevels.length * 4;
		len += 4;
		len += needTwoDuJies.length * 4;
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

			buffer.putInt(dujieNum);
			buffer.putInt(skillOneLevels.length);
			for(int i = 0 ; i < skillOneLevels.length; i++){
				buffer.putInt(skillOneLevels[i]);
			}
			buffer.putInt(needOneDuJies.length);
			for(int i = 0 ; i < needOneDuJies.length; i++){
				buffer.putInt(needOneDuJies[i]);
			}
			buffer.putInt(skillTwoLevels.length);
			for(int i = 0 ; i < skillTwoLevels.length; i++){
				buffer.putInt(skillTwoLevels[i]);
			}
			buffer.putInt(needTwoDuJies.length);
			for(int i = 0 ; i < needTwoDuJies.length; i++){
				buffer.putInt(needTwoDuJies[i]);
			}
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
	 *	渡过几次劫
	 */
	public int getDujieNum(){
		return dujieNum;
	}

	/**
	 * 设置属性：
	 *	渡过几次劫
	 */
	public void setDujieNum(int dujieNum){
		this.dujieNum = dujieNum;
	}

	/**
	 * 获取属性：
	 *	第一套技能阶段
	 */
	public int[] getSkillOneLevels(){
		return skillOneLevels;
	}

	/**
	 * 设置属性：
	 *	第一套技能阶段
	 */
	public void setSkillOneLevels(int[] skillOneLevels){
		this.skillOneLevels = skillOneLevels;
	}

	/**
	 * 获取属性：
	 *	第一套需要的渡劫阶段
	 */
	public int[] getNeedOneDuJies(){
		return needOneDuJies;
	}

	/**
	 * 设置属性：
	 *	第一套需要的渡劫阶段
	 */
	public void setNeedOneDuJies(int[] needOneDuJies){
		this.needOneDuJies = needOneDuJies;
	}

	/**
	 * 获取属性：
	 *	第二套技能阶段
	 */
	public int[] getSkillTwoLevels(){
		return skillTwoLevels;
	}

	/**
	 * 设置属性：
	 *	第二套技能阶段
	 */
	public void setSkillTwoLevels(int[] skillTwoLevels){
		this.skillTwoLevels = skillTwoLevels;
	}

	/**
	 * 获取属性：
	 *	第二套需要的渡劫阶段
	 */
	public int[] getNeedTwoDuJies(){
		return needTwoDuJies;
	}

	/**
	 * 设置属性：
	 *	第二套需要的渡劫阶段
	 */
	public void setNeedTwoDuJies(int[] needTwoDuJies){
		this.needTwoDuJies = needTwoDuJies;
	}

}