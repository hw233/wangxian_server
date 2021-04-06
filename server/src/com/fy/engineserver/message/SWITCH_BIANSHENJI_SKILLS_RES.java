package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 切换变身技能<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bianshenid</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currpoints</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currStat</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillids</td><td>int[]</td><td>skillids.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillpoints.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillpoints</td><td>int[]</td><td>skillpoints.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillstats.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillstats</td><td>int[]</td><td>skillstats.length</td><td>*</td></tr>
 * </table>
 */
public class SWITCH_BIANSHENJI_SKILLS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int bianshenid;
	int currpoints;
	int currStat;
	int[] skillids;
	int[] skillpoints;
	int[] skillstats;

	public SWITCH_BIANSHENJI_SKILLS_RES(){
	}

	public SWITCH_BIANSHENJI_SKILLS_RES(long seqNum,int bianshenid,int currpoints,int currStat,int[] skillids,int[] skillpoints,int[] skillstats){
		this.seqNum = seqNum;
		this.bianshenid = bianshenid;
		this.currpoints = currpoints;
		this.currStat = currStat;
		this.skillids = skillids;
		this.skillpoints = skillpoints;
		this.skillstats = skillstats;
	}

	public SWITCH_BIANSHENJI_SKILLS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		bianshenid = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currpoints = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currStat = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillids = new int[len];
		for(int i = 0 ; i < skillids.length ; i++){
			skillids[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillpoints = new int[len];
		for(int i = 0 ; i < skillpoints.length ; i++){
			skillpoints[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillstats = new int[len];
		for(int i = 0 ; i < skillstats.length ; i++){
			skillstats[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0084;
	}

	public String getTypeDescription() {
		return "SWITCH_BIANSHENJI_SKILLS_RES";
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
		len += 4;
		len += 4;
		len += skillids.length * 4;
		len += 4;
		len += skillpoints.length * 4;
		len += 4;
		len += skillstats.length * 4;
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

			buffer.putInt(bianshenid);
			buffer.putInt(currpoints);
			buffer.putInt(currStat);
			buffer.putInt(skillids.length);
			for(int i = 0 ; i < skillids.length; i++){
				buffer.putInt(skillids[i]);
			}
			buffer.putInt(skillpoints.length);
			for(int i = 0 ; i < skillpoints.length; i++){
				buffer.putInt(skillpoints[i]);
			}
			buffer.putInt(skillstats.length);
			for(int i = 0 ; i < skillstats.length; i++){
				buffer.putInt(skillstats[i]);
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
	 *	无帮助说明
	 */
	public int getBianshenid(){
		return bianshenid;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setBianshenid(int bianshenid){
		this.bianshenid = bianshenid;
	}

	/**
	 * 获取属性：
	 *	当前拥有的豆
	 */
	public int getCurrpoints(){
		return currpoints;
	}

	/**
	 * 设置属性：
	 *	当前拥有的豆
	 */
	public void setCurrpoints(int currpoints){
		this.currpoints = currpoints;
	}

	/**
	 * 获取属性：
	 *	当前形态,0:人,1:兽
	 */
	public int getCurrStat(){
		return currStat;
	}

	/**
	 * 设置属性：
	 *	当前形态,0:人,1:兽
	 */
	public void setCurrStat(int currStat){
		this.currStat = currStat;
	}

	/**
	 * 获取属性：
	 *	技能id集
	 */
	public int[] getSkillids(){
		return skillids;
	}

	/**
	 * 设置属性：
	 *	技能id集
	 */
	public void setSkillids(int[] skillids){
		this.skillids = skillids;
	}

	/**
	 * 获取属性：
	 *	使用技能所需豆
	 */
	public int[] getSkillpoints(){
		return skillpoints;
	}

	/**
	 * 设置属性：
	 *	使用技能所需豆
	 */
	public void setSkillpoints(int[] skillpoints){
		this.skillpoints = skillpoints;
	}

	/**
	 * 获取属性：
	 *	状态0:可以用，1:未学习
	 */
	public int[] getSkillstats(){
		return skillstats;
	}

	/**
	 * 设置属性：
	 *	状态0:可以用，1:未学习
	 */
	public void setSkillstats(int[] skillstats){
		this.skillstats = skillstats;
	}

}