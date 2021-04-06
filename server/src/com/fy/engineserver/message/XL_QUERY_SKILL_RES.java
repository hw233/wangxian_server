package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 仙灵技能<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>publicCDTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIds</td><td>int[]</td><td>skillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[0]</td><td>String</td><td>icons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[1]</td><td>String</td><td>icons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[2]</td><td>String</td><td>icons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cdTimes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cdTimes</td><td>long[]</td><td>cdTimes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nums</td><td>int[]</td><td>nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countdownTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class XL_QUERY_SKILL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int publicCDTime;
	int[] skillIds;
	String[] icons;
	long[] cdTimes;
	int[] nums;
	long countdownTime;

	public XL_QUERY_SKILL_RES(){
	}

	public XL_QUERY_SKILL_RES(long seqNum,int publicCDTime,int[] skillIds,String[] icons,long[] cdTimes,int[] nums,long countdownTime){
		this.seqNum = seqNum;
		this.publicCDTime = publicCDTime;
		this.skillIds = skillIds;
		this.icons = icons;
		this.cdTimes = cdTimes;
		this.nums = nums;
		this.countdownTime = countdownTime;
	}

	public XL_QUERY_SKILL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		publicCDTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
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
		icons = new String[len];
		for(int i = 0 ; i < icons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			icons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		cdTimes = new long[len];
		for(int i = 0 ; i < cdTimes.length ; i++){
			cdTimes[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		nums = new int[len];
		for(int i = 0 ; i < nums.length ; i++){
			nums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		countdownTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70FFF086;
	}

	public String getTypeDescription() {
		return "XL_QUERY_SKILL_RES";
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
		len += skillIds.length * 4;
		len += 4;
		for(int i = 0 ; i < icons.length; i++){
			len += 2;
			try{
				len += icons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += cdTimes.length * 8;
		len += 4;
		len += nums.length * 4;
		len += 8;
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

			buffer.putInt(publicCDTime);
			buffer.putInt(skillIds.length);
			for(int i = 0 ; i < skillIds.length; i++){
				buffer.putInt(skillIds[i]);
			}
			buffer.putInt(icons.length);
			for(int i = 0 ; i < icons.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = icons[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(cdTimes.length);
			for(int i = 0 ; i < cdTimes.length; i++){
				buffer.putLong(cdTimes[i]);
			}
			buffer.putInt(nums.length);
			for(int i = 0 ; i < nums.length; i++){
				buffer.putInt(nums[i]);
			}
			buffer.putLong(countdownTime);
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
	 *	公cd
	 */
	public int getPublicCDTime(){
		return publicCDTime;
	}

	/**
	 * 设置属性：
	 *	公cd
	 */
	public void setPublicCDTime(int publicCDTime){
		this.publicCDTime = publicCDTime;
	}

	/**
	 * 获取属性：
	 *	技能id
	 */
	public int[] getSkillIds(){
		return skillIds;
	}

	/**
	 * 设置属性：
	 *	技能id
	 */
	public void setSkillIds(int[] skillIds){
		this.skillIds = skillIds;
	}

	/**
	 * 获取属性：
	 *	技能图标
	 */
	public String[] getIcons(){
		return icons;
	}

	/**
	 * 设置属性：
	 *	技能图标
	 */
	public void setIcons(String[] icons){
		this.icons = icons;
	}

	/**
	 * 获取属性：
	 *	cd时间
	 */
	public long[] getCdTimes(){
		return cdTimes;
	}

	/**
	 * 设置属性：
	 *	cd时间
	 */
	public void setCdTimes(long[] cdTimes){
		this.cdTimes = cdTimes;
	}

	/**
	 * 获取属性：
	 *	道具个数
	 */
	public int[] getNums(){
		return nums;
	}

	/**
	 * 设置属性：
	 *	道具个数
	 */
	public void setNums(int[] nums){
		this.nums = nums;
	}

	/**
	 * 获取属性：
	 *	关卡倒计时
	 */
	public long getCountdownTime(){
		return countdownTime;
	}

	/**
	 * 设置属性：
	 *	关卡倒计时
	 */
	public void setCountdownTime(long countdownTime){
		this.countdownTime = countdownTime;
	}

}