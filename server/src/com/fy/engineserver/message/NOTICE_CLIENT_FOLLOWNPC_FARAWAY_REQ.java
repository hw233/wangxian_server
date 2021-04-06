package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端护送NPC太远了<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcGame.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcGame</td><td>String</td><td>npcGame.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcX</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcY</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String npcGame;
	int npcX;
	int npcY;

	public NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ(){
	}

	public NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ(long seqNum,String npcGame,int npcX,int npcY){
		this.seqNum = seqNum;
		this.npcGame = npcGame;
		this.npcX = npcX;
		this.npcY = npcY;
	}

	public NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		npcGame = new String(content,offset,len,"UTF-8");
		offset += len;
		npcX = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		npcY = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00000FAC;
	}

	public String getTypeDescription() {
		return "NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ";
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
			len +=npcGame.getBytes("UTF-8").length;
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
			 tmpBytes1 = npcGame.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(npcX);
			buffer.putInt(npcY);
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
	 *	NPC所在地图名
	 */
	public String getNpcGame(){
		return npcGame;
	}

	/**
	 * 设置属性：
	 *	NPC所在地图名
	 */
	public void setNpcGame(String npcGame){
		this.npcGame = npcGame;
	}

	/**
	 * 获取属性：
	 *	NPC所在X
	 */
	public int getNpcX(){
		return npcX;
	}

	/**
	 * 设置属性：
	 *	NPC所在X
	 */
	public void setNpcX(int npcX){
		this.npcX = npcX;
	}

	/**
	 * 获取属性：
	 *	NPC所在Y
	 */
	public int getNpcY(){
		return npcY;
	}

	/**
	 * 设置属性：
	 *	NPC所在Y
	 */
	public void setNpcY(int npcY){
		this.npcY = npcY;
	}

}