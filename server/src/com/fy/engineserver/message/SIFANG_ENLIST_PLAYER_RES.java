package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.sifang.info.SiFangSimplePlayerInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 设定参赛玩家<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangPlayers.length</td><td>int</td><td>4个字节</td><td>SiFangSimplePlayerInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sifangPlayers[0].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangPlayers[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sifangPlayers[0].playerName</td><td>String</td><td>sifangPlayers[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangPlayers[0].career</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sifangPlayers[0].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangPlayers[1].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sifangPlayers[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangPlayers[1].playerName</td><td>String</td><td>sifangPlayers[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sifangPlayers[1].career</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangPlayers[1].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sifangPlayers[2].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangPlayers[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sifangPlayers[2].playerName</td><td>String</td><td>sifangPlayers[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sifangPlayers[2].career</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sifangPlayers[2].playerLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chooseID.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chooseID</td><td>long[]</td><td>chooseID.length</td><td>*</td></tr>
 * </table>
 */
public class SIFANG_ENLIST_PLAYER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int sifangType;
	int maxNum;
	SiFangSimplePlayerInfo[] sifangPlayers;
	long[] chooseID;

	public SIFANG_ENLIST_PLAYER_RES(){
	}

	public SIFANG_ENLIST_PLAYER_RES(long seqNum,int sifangType,int maxNum,SiFangSimplePlayerInfo[] sifangPlayers,long[] chooseID){
		this.seqNum = seqNum;
		this.sifangType = sifangType;
		this.maxNum = maxNum;
		this.sifangPlayers = sifangPlayers;
		this.chooseID = chooseID;
	}

	public SIFANG_ENLIST_PLAYER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		sifangType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		sifangPlayers = new SiFangSimplePlayerInfo[len];
		for(int i = 0 ; i < sifangPlayers.length ; i++){
			sifangPlayers[i] = new SiFangSimplePlayerInfo();
			sifangPlayers[i].setPlayerId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			sifangPlayers[i].setPlayerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			sifangPlayers[i].setCareer((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			sifangPlayers[i].setPlayerLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		chooseID = new long[len];
		for(int i = 0 ; i < chooseID.length ; i++){
			chooseID[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70100002;
	}

	public String getTypeDescription() {
		return "SIFANG_ENLIST_PLAYER_RES";
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
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < sifangPlayers.length ; i++){
			len += 8;
			len += 2;
			if(sifangPlayers[i].getPlayerName() != null){
				try{
				len += sifangPlayers[i].getPlayerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
		}
		len += 4;
		len += chooseID.length * 8;
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

			buffer.putInt(sifangType);
			buffer.putInt(maxNum);
			buffer.putInt(sifangPlayers.length);
			for(int i = 0 ; i < sifangPlayers.length ; i++){
				buffer.putLong(sifangPlayers[i].getPlayerId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = sifangPlayers[i].getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)sifangPlayers[i].getCareer());
				buffer.putInt((int)sifangPlayers[i].getPlayerLevel());
			}
			buffer.putInt(chooseID.length);
			for(int i = 0 ; i < chooseID.length; i++){
				buffer.putLong(chooseID[i]);
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
	 *	活动类型
	 */
	public int getSifangType(){
		return sifangType;
	}

	/**
	 * 设置属性：
	 *	活动类型
	 */
	public void setSifangType(int sifangType){
		this.sifangType = sifangType;
	}

	/**
	 * 获取属性：
	 *	可以选择的玩家总数
	 */
	public int getMaxNum(){
		return maxNum;
	}

	/**
	 * 设置属性：
	 *	可以选择的玩家总数
	 */
	public void setMaxNum(int maxNum){
		this.maxNum = maxNum;
	}

	/**
	 * 获取属性：
	 *	可以被选的家族成员
	 */
	public SiFangSimplePlayerInfo[] getSifangPlayers(){
		return sifangPlayers;
	}

	/**
	 * 设置属性：
	 *	可以被选的家族成员
	 */
	public void setSifangPlayers(SiFangSimplePlayerInfo[] sifangPlayers){
		this.sifangPlayers = sifangPlayers;
	}

	/**
	 * 获取属性：
	 *	选中的玩家ID
	 */
	public long[] getChooseID(){
		return chooseID;
	}

	/**
	 * 设置属性：
	 *	选中的玩家ID
	 */
	public void setChooseID(long[] chooseID){
		this.chooseID = chooseID;
	}

}