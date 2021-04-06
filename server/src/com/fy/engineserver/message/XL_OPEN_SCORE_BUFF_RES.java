package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开积分卡界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tempId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>num</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>seniorempId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seniorum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class XL_OPEN_SCORE_BUFF_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long tempId;
	int num;
	long seniorempId;
	int seniorum;

	public XL_OPEN_SCORE_BUFF_RES(){
	}

	public XL_OPEN_SCORE_BUFF_RES(long seqNum,long tempId,int num,long seniorempId,int seniorum){
		this.seqNum = seqNum;
		this.tempId = tempId;
		this.num = num;
		this.seniorempId = seniorempId;
		this.seniorum = seniorum;
	}

	public XL_OPEN_SCORE_BUFF_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		tempId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		num = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		seniorempId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		seniorum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70FFF074;
	}

	public String getTypeDescription() {
		return "XL_OPEN_SCORE_BUFF_RES";
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

			buffer.putLong(tempId);
			buffer.putInt(num);
			buffer.putLong(seniorempId);
			buffer.putInt(seniorum);
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
	 *	普通积分卡
	 */
	public long getTempId(){
		return tempId;
	}

	/**
	 * 设置属性：
	 *	普通积分卡
	 */
	public void setTempId(long tempId){
		this.tempId = tempId;
	}

	/**
	 * 获取属性：
	 *	普通积分卡数量
	 */
	public int getNum(){
		return num;
	}

	/**
	 * 设置属性：
	 *	普通积分卡数量
	 */
	public void setNum(int num){
		this.num = num;
	}

	/**
	 * 获取属性：
	 *	高级积分卡
	 */
	public long getSeniorempId(){
		return seniorempId;
	}

	/**
	 * 设置属性：
	 *	高级积分卡
	 */
	public void setSeniorempId(long seniorempId){
		this.seniorempId = seniorempId;
	}

	/**
	 * 获取属性：
	 *	高级积分卡数量
	 */
	public int getSeniorum(){
		return seniorum;
	}

	/**
	 * 设置属性：
	 *	高级积分卡数量
	 */
	public void setSeniorum(int seniorum){
		this.seniorum = seniorum;
	}

}