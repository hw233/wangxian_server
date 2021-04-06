package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 玩家修改信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playername.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playername</td><td>String</td><td>playername.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sex</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>career</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PLAYER_UPDATE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long player;
	String playername;
	byte sex;
	byte career;

	public PLAYER_UPDATE_REQ(){
	}

	public PLAYER_UPDATE_REQ(long seqNum,long player,String playername,byte sex,byte career){
		this.seqNum = seqNum;
		this.player = player;
		this.playername = playername;
		this.sex = sex;
		this.career = career;
	}

	public PLAYER_UPDATE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		player = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		playername = new String(content,offset,len,"UTF-8");
		offset += len;
		sex = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		career = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x00000024;
	}

	public String getTypeDescription() {
		return "PLAYER_UPDATE_REQ";
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
			len +=playername.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
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

			buffer.putLong(player);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = playername.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(sex);
			buffer.put(career);
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
	 *	玩家唯一的编号
	 */
	public long getPlayer(){
		return player;
	}

	/**
	 * 设置属性：
	 *	玩家唯一的编号
	 */
	public void setPlayer(long player){
		this.player = player;
	}

	/**
	 * 获取属性：
	 *	玩家新名称
	 */
	public String getPlayername(){
		return playername;
	}

	/**
	 * 设置属性：
	 *	玩家新名称
	 */
	public void setPlayername(String playername){
		this.playername = playername;
	}

	/**
	 * 获取属性：
	 *	新性别
	 */
	public byte getSex(){
		return sex;
	}

	/**
	 * 设置属性：
	 *	新性别
	 */
	public void setSex(byte sex){
		this.sex = sex;
	}

	/**
	 * 获取属性：
	 *	角色的新门派或职业
	 */
	public byte getCareer(){
		return career;
	}

	/**
	 * 设置属性：
	 *	角色的新门派或职业
	 */
	public void setCareer(byte career){
		this.career = career;
	}

}