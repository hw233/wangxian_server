package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 摇骰子请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fightType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>point1</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>point2</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>point3</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>time</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SHOOK_DICE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long id;
	int fightType;
	int point1;
	int point2;
	int point3;
	int time;

	public SHOOK_DICE_RES(){
	}

	public SHOOK_DICE_RES(long seqNum,long id,int fightType,int point1,int point2,int point3,int time){
		this.seqNum = seqNum;
		this.id = id;
		this.fightType = fightType;
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
		this.time = time;
	}

	public SHOOK_DICE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		fightType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		point1 = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		point2 = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		point3 = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		time = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70FF0157;
	}

	public String getTypeDescription() {
		return "SHOOK_DICE_RES";
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

			buffer.putLong(id);
			buffer.putInt(fightType);
			buffer.putInt(point1);
			buffer.putInt(point2);
			buffer.putInt(point3);
			buffer.putInt(time);
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
	 *	家族或宗派id
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	家族或宗派id
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	城战0,矿战1
	 */
	public int getFightType(){
		return fightType;
	}

	/**
	 * 设置属性：
	 *	城战0,矿战1
	 */
	public void setFightType(int fightType){
		this.fightType = fightType;
	}

	/**
	 * 获取属性：
	 *	骰子点数
	 */
	public int getPoint1(){
		return point1;
	}

	/**
	 * 设置属性：
	 *	骰子点数
	 */
	public void setPoint1(int point1){
		this.point1 = point1;
	}

	/**
	 * 获取属性：
	 *	骰子点数
	 */
	public int getPoint2(){
		return point2;
	}

	/**
	 * 设置属性：
	 *	骰子点数
	 */
	public void setPoint2(int point2){
		this.point2 = point2;
	}

	/**
	 * 获取属性：
	 *	骰子点数
	 */
	public int getPoint3(){
		return point3;
	}

	/**
	 * 设置属性：
	 *	骰子点数
	 */
	public void setPoint3(int point3){
		this.point3 = point3;
	}

	/**
	 * 获取属性：
	 *	摇骰子等待时间
	 */
	public int getTime(){
		return time;
	}

	/**
	 * 设置属性：
	 *	摇骰子等待时间
	 */
	public void setTime(int time){
		this.time = time;
	}

}