package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 活跃度活动抽奖<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lotteryID.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lotteryID</td><td>long[]</td><td>lotteryID.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lotteryNum.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lotteryNum</td><td>int[]</td><td>lotteryNum.length</td><td>*</td></tr>
 * </table>
 */
public class LOTTERY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] lotteryID;
	int[] lotteryNum;

	public LOTTERY_RES(){
	}

	public LOTTERY_RES(long seqNum,long[] lotteryID,int[] lotteryNum){
		this.seqNum = seqNum;
		this.lotteryID = lotteryID;
		this.lotteryNum = lotteryNum;
	}

	public LOTTERY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lotteryID = new long[len];
		for(int i = 0 ; i < lotteryID.length ; i++){
			lotteryID[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lotteryNum = new int[len];
		for(int i = 0 ; i < lotteryNum.length ; i++){
			lotteryNum[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x8E0EAA08;
	}

	public String getTypeDescription() {
		return "LOTTERY_RES";
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
		len += lotteryID.length * 8;
		len += 4;
		len += lotteryNum.length * 4;
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

			buffer.putInt(lotteryID.length);
			for(int i = 0 ; i < lotteryID.length; i++){
				buffer.putLong(lotteryID[i]);
			}
			buffer.putInt(lotteryNum.length);
			for(int i = 0 ; i < lotteryNum.length; i++){
				buffer.putInt(lotteryNum[i]);
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
	 *	参与抽奖物品id
	 */
	public long[] getLotteryID(){
		return lotteryID;
	}

	/**
	 * 设置属性：
	 *	参与抽奖物品id
	 */
	public void setLotteryID(long[] lotteryID){
		this.lotteryID = lotteryID;
	}

	/**
	 * 获取属性：
	 *	物品数量
	 */
	public int[] getLotteryNum(){
		return lotteryNum;
	}

	/**
	 * 设置属性：
	 *	物品数量
	 */
	public void setLotteryNum(int[] lotteryNum){
		this.lotteryNum = lotteryNum;
	}

}