package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>contentMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>contentMess</td><td>String</td><td>contentMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totalScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class END_MINIGAME_ACTIVITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String contentMess;
	int totalScore;
	int currentScore;

	public END_MINIGAME_ACTIVITY_RES(){
	}

	public END_MINIGAME_ACTIVITY_RES(long seqNum,String contentMess,int totalScore,int currentScore){
		this.seqNum = seqNum;
		this.contentMess = contentMess;
		this.totalScore = totalScore;
		this.currentScore = currentScore;
	}

	public END_MINIGAME_ACTIVITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		contentMess = new String(content,offset,len,"UTF-8");
		offset += len;
		totalScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		currentScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x8E0EAA32;
	}

	public String getTypeDescription() {
		return "END_MINIGAME_ACTIVITY_RES";
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
		len += 2;
		try{
			len +=contentMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = contentMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(totalScore);
			buffer.putInt(currentScore);
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
	 *	提示内容---保留，用不用待确定
	 */
	public String getContentMess(){
		return contentMess;
	}

	/**
	 * 设置属性：
	 *	提示内容---保留，用不用待确定
	 */
	public void setContentMess(String contentMess){
		this.contentMess = contentMess;
	}

	/**
	 * 获取属性：
	 *	玩家总积分（此积分为每关结束后的累加值-之后大于60会有额外奖励）
	 */
	public int getTotalScore(){
		return totalScore;
	}

	/**
	 * 设置属性：
	 *	玩家总积分（此积分为每关结束后的累加值-之后大于60会有额外奖励）
	 */
	public void setTotalScore(int totalScore){
		this.totalScore = totalScore;
	}

	/**
	 * 获取属性：
	 *	当前小游戏总得分
	 */
	public int getCurrentScore(){
		return currentScore;
	}

	/**
	 * 设置属性：
	 *	当前小游戏总得分
	 */
	public void setCurrentScore(int currentScore){
		this.currentScore = currentScore;
	}

}