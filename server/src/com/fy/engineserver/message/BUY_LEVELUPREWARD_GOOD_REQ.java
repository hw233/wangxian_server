package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 购买冲级返利<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>rewardName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>rewardName</td><td>String</td><td>rewardName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>channelName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>channelName</td><td>String</td><td>channelName.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class BUY_LEVELUPREWARD_GOOD_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int rewardId;
	String rewardName;
	String channelName;

	public BUY_LEVELUPREWARD_GOOD_REQ(){
	}

	public BUY_LEVELUPREWARD_GOOD_REQ(long seqNum,int rewardId,String rewardName,String channelName){
		this.seqNum = seqNum;
		this.rewardId = rewardId;
		this.rewardName = rewardName;
		this.channelName = channelName;
	}

	public BUY_LEVELUPREWARD_GOOD_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		rewardId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		rewardName = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		channelName = new String(content,offset,len);
		offset += len;
	}

	public int getType() {
		return 0x00FF0080;
	}

	public String getTypeDescription() {
		return "BUY_LEVELUPREWARD_GOOD_REQ";
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
		len += 2;
		len +=rewardName.getBytes().length;
		len += 2;
		len +=channelName.getBytes().length;
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

			buffer.putInt(rewardId);
			byte[] tmpBytes1;
			tmpBytes1 = rewardName.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = channelName.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	返利页签id
	 */
	public int getRewardId(){
		return rewardId;
	}

	/**
	 * 设置属性：
	 *	返利页签id
	 */
	public void setRewardId(int rewardId){
		this.rewardId = rewardId;
	}

	/**
	 * 获取属性：
	 *	返利页签名
	 */
	public String getRewardName(){
		return rewardName;
	}

	/**
	 * 设置属性：
	 *	返利页签名
	 */
	public void setRewardName(String rewardName){
		this.rewardName = rewardName;
	}

	/**
	 * 获取属性：
	 *	渠道
	 */
	public String getChannelName(){
		return channelName;
	}

	/**
	 * 设置属性：
	 *	渠道
	 */
	public void setChannelName(String channelName){
		this.channelName = channelName;
	}

}