package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 结束通知<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enterRoomNums</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allExps</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nums</td><td>int[]</td><td>nums.length</td><td>*</td></tr>
 * </table>
 */
public class DICE_END_NOTICE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int enterRoomNums;
	long allExps;
	long[] ids;
	int[] nums;

	public DICE_END_NOTICE_RES(){
	}

	public DICE_END_NOTICE_RES(long seqNum,int enterRoomNums,long allExps,long[] ids,int[] nums){
		this.seqNum = seqNum;
		this.enterRoomNums = enterRoomNums;
		this.allExps = allExps;
		this.ids = ids;
		this.nums = nums;
	}

	public DICE_END_NOTICE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		enterRoomNums = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		allExps = (long)mf.byteArrayToNumber(content,offset,8);
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
	}

	public int getType() {
		return 0x70FF0161;
	}

	public String getTypeDescription() {
		return "DICE_END_NOTICE_RES";
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
		len += 8;
		len += 4;
		len += ids.length * 8;
		len += 4;
		len += nums.length * 4;
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

			buffer.putInt(enterRoomNums);
			buffer.putLong(allExps);
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(nums.length);
			for(int i = 0 ; i < nums.length; i++){
				buffer.putInt(nums[i]);
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
	 *	进入房间数
	 */
	public int getEnterRoomNums(){
		return enterRoomNums;
	}

	/**
	 * 设置属性：
	 *	进入房间数
	 */
	public void setEnterRoomNums(int enterRoomNums){
		this.enterRoomNums = enterRoomNums;
	}

	/**
	 * 获取属性：
	 *	总经验
	 */
	public long getAllExps(){
		return allExps;
	}

	/**
	 * 设置属性：
	 *	总经验
	 */
	public void setAllExps(long allExps){
		this.allExps = allExps;
	}

	/**
	 * 获取属性：
	 *	奖励id
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	奖励id
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	奖励数量
	 */
	public int[] getNums(){
		return nums;
	}

	/**
	 * 设置属性：
	 *	奖励数量
	 */
	public void setNums(int[] nums){
		this.nums = nums;
	}

}