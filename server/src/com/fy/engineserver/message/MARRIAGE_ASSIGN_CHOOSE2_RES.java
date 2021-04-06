package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 让客户端选择嘉宾<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isback</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>num</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chooseType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chooseType</td><td>byte[]</td><td>chooseType.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerId</td><td>long[]</td><td>playerId.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[0]</td><td>String</td><td>playerName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[1]</td><td>String</td><td>playerName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName[2]</td><td>String</td><td>playerName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerCareer.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerCareer</td><td>byte[]</td><td>playerCareer.length</td><td>数组实际长度</td></tr>
 * </table>
 */
public class MARRIAGE_ASSIGN_CHOOSE2_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean isback;
	int num;
	byte[] chooseType;
	long[] playerId;
	String[] playerName;
	byte[] playerCareer;

	public MARRIAGE_ASSIGN_CHOOSE2_RES(){
	}

	public MARRIAGE_ASSIGN_CHOOSE2_RES(long seqNum,boolean isback,int num,byte[] chooseType,long[] playerId,String[] playerName,byte[] playerCareer){
		this.seqNum = seqNum;
		this.isback = isback;
		this.num = num;
		this.chooseType = chooseType;
		this.playerId = playerId;
		this.playerName = playerName;
		this.playerCareer = playerCareer;
	}

	public MARRIAGE_ASSIGN_CHOOSE2_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		isback = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		num = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		chooseType = new byte[len];
		System.arraycopy(content,offset,chooseType,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerId = new long[len];
		for(int i = 0 ; i < playerId.length ; i++){
			playerId[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerName = new String[len];
		for(int i = 0 ; i < playerName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			playerName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		playerCareer = new byte[len];
		System.arraycopy(content,offset,playerCareer,0,len);
		offset += len;
	}

	public int getType() {
		return 0x70010008;
	}

	public String getTypeDescription() {
		return "MARRIAGE_ASSIGN_CHOOSE2_RES";
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
		len += 4;
		len += chooseType.length;
		len += 4;
		len += playerId.length * 8;
		len += 4;
		for(int i = 0 ; i < playerName.length; i++){
			len += 2;
			try{
				len += playerName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += playerCareer.length;
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

			buffer.put((byte)(isback==false?0:1));
			buffer.putInt(num);
			buffer.putInt(chooseType.length);
			buffer.put(chooseType);
			buffer.putInt(playerId.length);
			for(int i = 0 ; i < playerId.length; i++){
				buffer.putLong(playerId[i]);
			}
			buffer.putInt(playerName.length);
			for(int i = 0 ; i < playerName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = playerName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(playerCareer.length);
			buffer.put(playerCareer);
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
	 *	是否可以返回上一步
	 */
	public boolean getIsback(){
		return isback;
	}

	/**
	 * 设置属性：
	 *	是否可以返回上一步
	 */
	public void setIsback(boolean isback){
		this.isback = isback;
	}

	/**
	 * 获取属性：
	 *	可选宾客数目
	 */
	public int getNum(){
		return num;
	}

	/**
	 * 设置属性：
	 *	可选宾客数目
	 */
	public void setNum(int num){
		this.num = num;
	}

	/**
	 * 获取属性：
	 *	谁选择的，0是我， 1是对方
	 */
	public byte[] getChooseType(){
		return chooseType;
	}

	/**
	 * 设置属性：
	 *	谁选择的，0是我， 1是对方
	 */
	public void setChooseType(byte[] chooseType){
		this.chooseType = chooseType;
	}

	/**
	 * 获取属性：
	 *	选择了某个玩家
	 */
	public long[] getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	选择了某个玩家
	 */
	public void setPlayerId(long[] playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	玩家名称
	 */
	public String[] getPlayerName(){
		return playerName;
	}

	/**
	 * 设置属性：
	 *	玩家名称
	 */
	public void setPlayerName(String[] playerName){
		this.playerName = playerName;
	}

	/**
	 * 获取属性：
	 *	玩家职业
	 */
	public byte[] getPlayerCareer(){
		return playerCareer;
	}

	/**
	 * 设置属性：
	 *	玩家职业
	 */
	public void setPlayerCareer(byte[] playerCareer){
		this.playerCareer = playerCareer;
	}

}