package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.country.data.CountryInfoForClient;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 国家任命玩家查询，客户端发任命协议给服务器，服务器传回任命后的结果，当客户端收到RES后弹出任命UI<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients.length</td><td>int</td><td>4个字节</td><td>CountryInfoForClient数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[0].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[0].countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[0].playerName</td><td>String</td><td>countryInfoForClients[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[0].zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[0].zongPaiName</td><td>String</td><td>countryInfoForClients[0].zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[0].onLine</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[0].biaozhang</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[0].shouxun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[1].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[1].countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[1].playerName</td><td>String</td><td>countryInfoForClients[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[1].zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[1].zongPaiName</td><td>String</td><td>countryInfoForClients[1].zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[1].onLine</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[1].biaozhang</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[1].shouxun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[2].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[2].countryPosition</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[2].playerName</td><td>String</td><td>countryInfoForClients[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[2].zongPaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[2].zongPaiName</td><td>String</td><td>countryInfoForClients[2].zongPaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[2].onLine</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryInfoForClients[2].biaozhang</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryInfoForClients[2].shouxun</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_COUNTRY_COMMISSION_OR_RECALL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	CountryInfoForClient[] countryInfoForClients;

	public QUERY_COUNTRY_COMMISSION_OR_RECALL_RES(){
	}

	public QUERY_COUNTRY_COMMISSION_OR_RECALL_RES(long seqNum,CountryInfoForClient[] countryInfoForClients){
		this.seqNum = seqNum;
		this.countryInfoForClients = countryInfoForClients;
	}

	public QUERY_COUNTRY_COMMISSION_OR_RECALL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		countryInfoForClients = new CountryInfoForClient[len];
		for(int i = 0 ; i < countryInfoForClients.length ; i++){
			countryInfoForClients[i] = new CountryInfoForClient();
			countryInfoForClients[i].setPlayerId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			countryInfoForClients[i].setCountryPosition((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			countryInfoForClients[i].setPlayerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			countryInfoForClients[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			countryInfoForClients[i].setZongPaiName(new String(content,offset,len,"UTF-8"));
			offset += len;
			countryInfoForClients[i].setOnLine(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			countryInfoForClients[i].setBiaozhang(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			countryInfoForClients[i].setShouxun(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
		}
	}

	public int getType() {
		return 0x70000AA2;
	}

	public String getTypeDescription() {
		return "QUERY_COUNTRY_COMMISSION_OR_RECALL_RES";
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
		for(int i = 0 ; i < countryInfoForClients.length ; i++){
			len += 8;
			len += 4;
			len += 2;
			if(countryInfoForClients[i].getPlayerName() != null){
				try{
				len += countryInfoForClients[i].getPlayerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 2;
			if(countryInfoForClients[i].getZongPaiName() != null){
				try{
				len += countryInfoForClients[i].getZongPaiName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 1;
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

			buffer.putInt(countryInfoForClients.length);
			for(int i = 0 ; i < countryInfoForClients.length ; i++){
				buffer.putLong(countryInfoForClients[i].getPlayerId());
				buffer.putInt((int)countryInfoForClients[i].getCountryPosition());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = countryInfoForClients[i].getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)countryInfoForClients[i].getLevel());
				try{
				tmpBytes2 = countryInfoForClients[i].getZongPaiName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(countryInfoForClients[i].isOnLine()==false?0:1));
				buffer.put((byte)(countryInfoForClients[i].isBiaozhang()==false?0:1));
				buffer.put((byte)(countryInfoForClients[i].isShouxun()==false?0:1));
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
	 *	无帮助说明
	 */
	public CountryInfoForClient[] getCountryInfoForClients(){
		return countryInfoForClients;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setCountryInfoForClients(CountryInfoForClient[] countryInfoForClients){
		this.countryInfoForClients = countryInfoForClients;
	}

}