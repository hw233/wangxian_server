package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端玩家目标进度变化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chapterName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chapterName</td><td>String</td><td>chapterName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aimId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>progress</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currChapterScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totalScore</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chapterReceiveType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class NOTICE_PLAYER_MUBIAO_CHANGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String chapterName;
	int aimId;
	long progress;
	int currChapterScore;
	int totalScore;
	byte chapterReceiveType;

	public NOTICE_PLAYER_MUBIAO_CHANGE_RES(){
	}

	public NOTICE_PLAYER_MUBIAO_CHANGE_RES(long seqNum,String chapterName,int aimId,long progress,int currChapterScore,int totalScore,byte chapterReceiveType){
		this.seqNum = seqNum;
		this.chapterName = chapterName;
		this.aimId = aimId;
		this.progress = progress;
		this.currChapterScore = currChapterScore;
		this.totalScore = totalScore;
		this.chapterReceiveType = chapterReceiveType;
	}

	public NOTICE_PLAYER_MUBIAO_CHANGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chapterName = new String(content,offset,len,"UTF-8");
		offset += len;
		aimId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		progress = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		currChapterScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		totalScore = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		chapterReceiveType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x70F0EF89;
	}

	public String getTypeDescription() {
		return "NOTICE_PLAYER_MUBIAO_CHANGE_RES";
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
			len +=chapterName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 8;
		len += 4;
		len += 4;
		len += 1;
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
			 tmpBytes1 = chapterName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(aimId);
			buffer.putLong(progress);
			buffer.putInt(currChapterScore);
			buffer.putInt(totalScore);
			buffer.put(chapterReceiveType);
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
	 *	所属章节名
	 */
	public String getChapterName(){
		return chapterName;
	}

	/**
	 * 设置属性：
	 *	所属章节名
	 */
	public void setChapterName(String chapterName){
		this.chapterName = chapterName;
	}

	/**
	 * 获取属性：
	 *	目标id
	 */
	public int getAimId(){
		return aimId;
	}

	/**
	 * 设置属性：
	 *	目标id
	 */
	public void setAimId(int aimId){
		this.aimId = aimId;
	}

	/**
	 * 获取属性：
	 *	目标进度
	 */
	public long getProgress(){
		return progress;
	}

	/**
	 * 设置属性：
	 *	目标进度
	 */
	public void setProgress(long progress){
		this.progress = progress;
	}

	/**
	 * 获取属性：
	 *	当前章节积分
	 */
	public int getCurrChapterScore(){
		return currChapterScore;
	}

	/**
	 * 设置属性：
	 *	当前章节积分
	 */
	public void setCurrChapterScore(int currChapterScore){
		this.currChapterScore = currChapterScore;
	}

	/**
	 * 获取属性：
	 *	总积分
	 */
	public int getTotalScore(){
		return totalScore;
	}

	/**
	 * 设置属性：
	 *	总积分
	 */
	public void setTotalScore(int totalScore){
		this.totalScore = totalScore;
	}

	/**
	 * 获取属性：
	 *	此章节奖励领取状态
	 */
	public byte getChapterReceiveType(){
		return chapterReceiveType;
	}

	/**
	 * 设置属性：
	 *	此章节奖励领取状态
	 */
	public void setChapterReceiveType(byte chapterReceiveType){
		this.chapterReceiveType = chapterReceiveType;
	}

}