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
 * <tr bgcolor="#FAFAFA" align="center"><td>gameType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initDataArr.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>initDataArr[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initDataArr[0]</td><td>String</td><td>initDataArr[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>initDataArr[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initDataArr[1]</td><td>String</td><td>initDataArr[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>initDataArr[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>initDataArr[2]</td><td>String</td><td>initDataArr[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class HANDLE_MINIGAME_ACTIVITY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte gameType;
	String[] initDataArr;

	public HANDLE_MINIGAME_ACTIVITY_REQ(){
	}

	public HANDLE_MINIGAME_ACTIVITY_REQ(long seqNum,byte gameType,String[] initDataArr){
		this.seqNum = seqNum;
		this.gameType = gameType;
		this.initDataArr = initDataArr;
	}

	public HANDLE_MINIGAME_ACTIVITY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		gameType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		initDataArr = new String[len];
		for(int i = 0 ; i < initDataArr.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			initDataArr[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x0E0EAA31;
	}

	public String getTypeDescription() {
		return "HANDLE_MINIGAME_ACTIVITY_REQ";
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
		len += 1;
		len += 4;
		for(int i = 0 ; i < initDataArr.length; i++){
			len += 2;
			try{
				len += initDataArr[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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

			buffer.put(gameType);
			buffer.putInt(initDataArr.length);
			for(int i = 0 ; i < initDataArr.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = initDataArr[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	拼图游戏(0),记忆力游戏(1),对对碰(2),管道(3)
	 */
	public byte getGameType(){
		return gameType;
	}

	/**
	 * 设置属性：
	 *	拼图游戏(0),记忆力游戏(1),对对碰(2),管道(3)
	 */
	public void setGameType(byte gameType){
		this.gameType = gameType;
	}

	/**
	 * 获取属性：
	 *	初始化数据数组（拼图、记忆力，发送用户改变后的数组序列，数据格式通初始化格式。对对碰、管道只一个数据，玩家点击的游戏位置编号）
	 */
	public String[] getInitDataArr(){
		return initDataArr;
	}

	/**
	 * 设置属性：
	 *	初始化数据数组（拼图、记忆力，发送用户改变后的数组序列，数据格式通初始化格式。对对碰、管道只一个数据，玩家点击的游戏位置编号）
	 */
	public void setInitDataArr(String[] initDataArr){
		this.initDataArr = initDataArr;
	}

}