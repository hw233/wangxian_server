package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器发送给客户端，通知客户端技能CD状态，防止冷却时间长的技能在服务端释放失败，导致无谓的长时间冷却。<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>categoryName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>categoryName</td><td>String</td><td>categoryName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>startTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cdType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PROPS_CD_MODIFY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerid;
	String categoryName;
	long startTime;
	byte cdType;

	public PROPS_CD_MODIFY_REQ(){
	}

	public PROPS_CD_MODIFY_REQ(long seqNum,long playerid,String categoryName,long startTime,byte cdType){
		this.seqNum = seqNum;
		this.playerid = playerid;
		this.categoryName = categoryName;
		this.startTime = startTime;
		this.cdType = cdType;
	}

	public PROPS_CD_MODIFY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		categoryName = new String(content,offset,len,"UTF-8");
		offset += len;
		startTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		cdType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x0200EF09;
	}

	public String getTypeDescription() {
		return "PROPS_CD_MODIFY_REQ";
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
		len += 2;
		try{
			len +=categoryName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
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

			buffer.putLong(playerid);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = categoryName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(startTime);
			buffer.put(cdType);
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
	 *	玩家ID
	 */
	public long getPlayerid(){
		return playerid;
	}

	/**
	 * 设置属性：
	 *	玩家ID
	 */
	public void setPlayerid(long playerid){
		this.playerid = playerid;
	}

	/**
	 * 获取属性：
	 *	物品种类
	 */
	public String getCategoryName(){
		return categoryName;
	}

	/**
	 * 设置属性：
	 *	物品种类
	 */
	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	/**
	 * 获取属性：
	 *	物品种类开始执行的时间
	 */
	public long getStartTime(){
		return startTime;
	}

	/**
	 * 设置属性：
	 *	物品种类开始执行的时间
	 */
	public void setStartTime(long startTime){
		this.startTime = startTime;
	}

	/**
	 * 获取属性：
	 *	0标识设置cd，1标识取消cd
	 */
	public byte getCdType(){
		return cdType;
	}

	/**
	 * 设置属性：
	 *	0标识设置cd，1标识取消cd
	 */
	public void setCdType(byte cdType){
		this.cdType = cdType;
	}

}