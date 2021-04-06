package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端通知服务器，精灵沿着导航点移动到目的地，以及到达时间<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>startTimestamp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>speed</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>destineTimestamp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>startX</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>startY</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>endX</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>endY</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>signPosts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>signPosts</td><td>short[]</td><td>signPosts.length</td><td>*</td></tr>
 * </table>
 */
public class PLAYER_MOVE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long startTimestamp;
	int speed;
	long destineTimestamp;
	short startX;
	short startY;
	short endX;
	short endY;
	short[] signPosts;

	public PLAYER_MOVE_REQ(){
	}

	public PLAYER_MOVE_REQ(long seqNum,long startTimestamp,int speed,long destineTimestamp,short startX,short startY,short endX,short endY,short[] signPosts){
		this.seqNum = seqNum;
		this.startTimestamp = startTimestamp;
		this.speed = speed;
		this.destineTimestamp = destineTimestamp;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.signPosts = signPosts;
	}

	public PLAYER_MOVE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		startTimestamp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		speed = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		destineTimestamp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		startX = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		startY = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		endX = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		endY = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		signPosts = new short[len];
		for(int i = 0 ; i < signPosts.length ; i++){
			signPosts[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
	}

	public int getType() {
		return 0x000000C0;
	}

	public String getTypeDescription() {
		return "PLAYER_MOVE_REQ";
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
		len += 8;
		len += 2;
		len += 2;
		len += 2;
		len += 2;
		len += 4;
		len += signPosts.length * 2;
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

			buffer.putLong(startTimestamp);
			buffer.putInt(speed);
			buffer.putLong(destineTimestamp);
			buffer.putShort(startX);
			buffer.putShort(startY);
			buffer.putShort(endX);
			buffer.putShort(endY);
			buffer.putInt(signPosts.length);
			for(int i = 0 ; i < signPosts.length; i++){
				buffer.putShort(signPosts[i]);
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
	 *	客户端当前时间
	 */
	public long getStartTimestamp(){
		return startTimestamp;
	}

	/**
	 * 设置属性：
	 *	客户端当前时间
	 */
	public void setStartTimestamp(long startTimestamp){
		this.startTimestamp = startTimestamp;
	}

	/**
	 * 获取属性：
	 *	速度
	 */
	public int getSpeed(){
		return speed;
	}

	/**
	 * 设置属性：
	 *	速度
	 */
	public void setSpeed(int speed){
		this.speed = speed;
	}

	/**
	 * 获取属性：
	 *	到达目的地的时刻
	 */
	public long getDestineTimestamp(){
		return destineTimestamp;
	}

	/**
	 * 设置属性：
	 *	到达目的地的时刻
	 */
	public void setDestineTimestamp(long destineTimestamp){
		this.destineTimestamp = destineTimestamp;
	}

	/**
	 * 获取属性：
	 *	出发地x坐标
	 */
	public short getStartX(){
		return startX;
	}

	/**
	 * 设置属性：
	 *	出发地x坐标
	 */
	public void setStartX(short startX){
		this.startX = startX;
	}

	/**
	 * 获取属性：
	 *	出发地y坐标
	 */
	public short getStartY(){
		return startY;
	}

	/**
	 * 设置属性：
	 *	出发地y坐标
	 */
	public void setStartY(short startY){
		this.startY = startY;
	}

	/**
	 * 获取属性：
	 *	目的地x坐标
	 */
	public short getEndX(){
		return endX;
	}

	/**
	 * 设置属性：
	 *	目的地x坐标
	 */
	public void setEndX(short endX){
		this.endX = endX;
	}

	/**
	 * 获取属性：
	 *	目的地y坐标
	 */
	public short getEndY(){
		return endY;
	}

	/**
	 * 设置属性：
	 *	目的地y坐标
	 */
	public void setEndY(short endY){
		this.endY = endY;
	}

	/**
	 * 获取属性：
	 *	导航点编号
	 */
	public short[] getSignPosts(){
		return signPosts;
	}

	/**
	 * 设置属性：
	 *	导航点编号
	 */
	public void setSignPosts(short[] signPosts){
		this.signPosts = signPosts;
	}

}