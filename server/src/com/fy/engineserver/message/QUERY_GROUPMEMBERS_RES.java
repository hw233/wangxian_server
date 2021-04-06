package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.society.Player_RelatinInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取玩家列表,好友或黑名单,或仇人<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo.length</td><td>int</td><td>4个字节</td><td>Player_RelatinInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[0].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[0].name</td><td>String</td><td>player_relationInfo[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[0].icon</td><td>String</td><td>player_relationInfo[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[0].isonline</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[0].mood.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[0].mood</td><td>String</td><td>player_relationInfo[0].mood.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[1].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[1].name</td><td>String</td><td>player_relationInfo[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[1].icon</td><td>String</td><td>player_relationInfo[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[1].isonline</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[1].mood.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[1].mood</td><td>String</td><td>player_relationInfo[1].mood.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[2].career</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[2].name</td><td>String</td><td>player_relationInfo[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[2].icon</td><td>String</td><td>player_relationInfo[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[2].isonline</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>player_relationInfo[2].mood.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>player_relationInfo[2].mood</td><td>String</td><td>player_relationInfo[2].mood.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_GROUPMEMBERS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Player_RelatinInfo[] player_relationInfo;

	public QUERY_GROUPMEMBERS_RES(){
	}

	public QUERY_GROUPMEMBERS_RES(long seqNum,Player_RelatinInfo[] player_relationInfo){
		this.seqNum = seqNum;
		this.player_relationInfo = player_relationInfo;
	}

	public QUERY_GROUPMEMBERS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		player_relationInfo = new Player_RelatinInfo[len];
		for(int i = 0 ; i < player_relationInfo.length ; i++){
			player_relationInfo[i] = new Player_RelatinInfo();
			player_relationInfo[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			player_relationInfo[i].setCareer((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			player_relationInfo[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			player_relationInfo[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			player_relationInfo[i].setIsonline(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			player_relationInfo[i].setMood(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x7000EC17;
	}

	public String getTypeDescription() {
		return "QUERY_GROUPMEMBERS_RES";
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
		for(int i = 0 ; i < player_relationInfo.length ; i++){
			len += 8;
			len += 1;
			len += 2;
			if(player_relationInfo[i].getName() != null){
				try{
				len += player_relationInfo[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(player_relationInfo[i].getIcon() != null){
				try{
				len += player_relationInfo[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(player_relationInfo[i].getMood() != null){
				try{
				len += player_relationInfo[i].getMood().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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

			buffer.putInt(player_relationInfo.length);
			for(int i = 0 ; i < player_relationInfo.length ; i++){
				buffer.putLong(player_relationInfo[i].getId());
				buffer.put((byte)player_relationInfo[i].getCareer());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = player_relationInfo[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = player_relationInfo[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(player_relationInfo[i].isIsonline()==false?0:1));
				try{
				tmpBytes2 = player_relationInfo[i].getMood().getBytes("UTF-8");
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
	 *	返回社会关系
	 */
	public Player_RelatinInfo[] getPlayer_relationInfo(){
		return player_relationInfo;
	}

	/**
	 * 设置属性：
	 *	返回社会关系
	 */
	public void setPlayer_relationInfo(Player_RelatinInfo[] player_relationInfo){
		this.player_relationInfo = player_relationInfo;
	}

}