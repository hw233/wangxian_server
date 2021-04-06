package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.disaster.instance.DisasterPlayer;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端金猴天灾游戏排名<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>disasterPlayers.length</td><td>int</td><td>4个字节</td><td>DisasterPlayer数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>disasterPlayers[0].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>disasterPlayers[0].playerNames.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>disasterPlayers[0].playerNames</td><td>String</td><td>disasterPlayers[0].playerNames.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>disasterPlayers[0].deadTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>disasterPlayers[0].leftHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>disasterPlayers[1].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>disasterPlayers[1].playerNames.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>disasterPlayers[1].playerNames</td><td>String</td><td>disasterPlayers[1].playerNames.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>disasterPlayers[1].deadTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>disasterPlayers[1].leftHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>disasterPlayers[2].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>disasterPlayers[2].playerNames.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>disasterPlayers[2].playerNames</td><td>String</td><td>disasterPlayers[2].playerNames.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>disasterPlayers[2].deadTimes</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>disasterPlayers[2].leftHp</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class DISASTER_RANK_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	DisasterPlayer[] disasterPlayers;

	public DISASTER_RANK_INFO_RES(){
	}

	public DISASTER_RANK_INFO_RES(long seqNum,DisasterPlayer[] disasterPlayers){
		this.seqNum = seqNum;
		this.disasterPlayers = disasterPlayers;
	}

	public DISASTER_RANK_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		disasterPlayers = new DisasterPlayer[len];
		for(int i = 0 ; i < disasterPlayers.length ; i++){
			disasterPlayers[i] = new DisasterPlayer();
			disasterPlayers[i].setPlayerId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			disasterPlayers[i].setPlayerNames(new String(content,offset,len,"UTF-8"));
			offset += len;
			disasterPlayers[i].setDeadTimes((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			disasterPlayers[i].setLeftHp((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0149;
	}

	public String getTypeDescription() {
		return "DISASTER_RANK_INFO_RES";
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
		for(int i = 0 ; i < disasterPlayers.length ; i++){
			len += 8;
			len += 2;
			if(disasterPlayers[i].getPlayerNames() != null){
				try{
				len += disasterPlayers[i].getPlayerNames().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
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

			buffer.putInt(disasterPlayers.length);
			for(int i = 0 ; i < disasterPlayers.length ; i++){
				buffer.putLong(disasterPlayers[i].getPlayerId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = disasterPlayers[i].getPlayerNames().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)disasterPlayers[i].getDeadTimes());
				buffer.putInt((int)disasterPlayers[i].getLeftHp());
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
	 *	数组按照排名先后顺序
	 */
	public DisasterPlayer[] getDisasterPlayers(){
		return disasterPlayers;
	}

	/**
	 * 设置属性：
	 *	数组按照排名先后顺序
	 */
	public void setDisasterPlayers(DisasterPlayer[] disasterPlayers){
		this.disasterPlayers = disasterPlayers;
	}

}