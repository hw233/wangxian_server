package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 确认神识<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>magicweaponid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nums</td><td>int[]</td><td>nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>usesilver</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CONFIRM_SHENSHI_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long magicweaponid;
	long[] ids;
	int[] nums;
	int usesilver;

	public CONFIRM_SHENSHI_REQ(){
	}

	public CONFIRM_SHENSHI_REQ(long seqNum,long magicweaponid,long[] ids,int[] nums,int usesilver){
		this.seqNum = seqNum;
		this.magicweaponid = magicweaponid;
		this.ids = ids;
		this.nums = nums;
		this.usesilver = usesilver;
	}

	public CONFIRM_SHENSHI_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		magicweaponid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids = new long[len];
		for(int i = 0 ; i < ids.length ; i++){
			ids[i] = (long)mf.byteArrayToNumber(content,offset,8);
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
		usesilver = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00F0EF04;
	}

	public String getTypeDescription() {
		return "CONFIRM_SHENSHI_REQ";
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
		len += 4;
		len += ids.length * 8;
		len += 4;
		len += nums.length * 4;
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

			buffer.putLong(magicweaponid);
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(nums.length);
			for(int i = 0 ; i < nums.length; i++){
				buffer.putInt(nums[i]);
			}
			buffer.putInt(usesilver);
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
	 *	法宝ID
	 */
	public long getMagicweaponid(){
		return magicweaponid;
	}

	/**
	 * 设置属性：
	 *	法宝ID
	 */
	public void setMagicweaponid(long magicweaponid){
		this.magicweaponid = magicweaponid;
	}

	/**
	 * 获取属性：
	 *	神识符id
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	神识符id
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	数量
	 */
	public int[] getNums(){
		return nums;
	}

	/**
	 * 设置属性：
	 *	数量
	 */
	public void setNums(int[] nums){
		this.nums = nums;
	}

	/**
	 * 获取属性：
	 *	0:使用神识符；1:使用银子
	 */
	public int getUsesilver(){
		return usesilver;
	}

	/**
	 * 设置属性：
	 *	0:使用神识符；1:使用银子
	 */
	public void setUsesilver(int usesilver){
		this.usesilver = usesilver;
	}

}