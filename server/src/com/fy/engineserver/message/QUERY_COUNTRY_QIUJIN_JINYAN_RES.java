package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.country.data.CountryQiujinAndJinyanForClient;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询本国囚禁和禁言的人，在线<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryQiujinAndJinyanForClients.length</td><td>int</td><td>4个字节</td><td>CountryQiujinAndJinyanForClient数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryQiujinAndJinyanForClients[0].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryQiujinAndJinyanForClients[0].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryQiujinAndJinyanForClients[0].playerName</td><td>String</td><td>countryQiujinAndJinyanForClients[0].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryQiujinAndJinyanForClients[0].lastQiujinTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryQiujinAndJinyanForClients[0].lastJinyanTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryQiujinAndJinyanForClients[1].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryQiujinAndJinyanForClients[1].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryQiujinAndJinyanForClients[1].playerName</td><td>String</td><td>countryQiujinAndJinyanForClients[1].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryQiujinAndJinyanForClients[1].lastQiujinTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryQiujinAndJinyanForClients[1].lastJinyanTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryQiujinAndJinyanForClients[2].playerId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryQiujinAndJinyanForClients[2].playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryQiujinAndJinyanForClients[2].playerName</td><td>String</td><td>countryQiujinAndJinyanForClients[2].playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryQiujinAndJinyanForClients[2].lastQiujinTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryQiujinAndJinyanForClients[2].lastJinyanTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_COUNTRY_QIUJIN_JINYAN_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	CountryQiujinAndJinyanForClient[] countryQiujinAndJinyanForClients;

	public QUERY_COUNTRY_QIUJIN_JINYAN_RES(){
	}

	public QUERY_COUNTRY_QIUJIN_JINYAN_RES(long seqNum,CountryQiujinAndJinyanForClient[] countryQiujinAndJinyanForClients){
		this.seqNum = seqNum;
		this.countryQiujinAndJinyanForClients = countryQiujinAndJinyanForClients;
	}

	public QUERY_COUNTRY_QIUJIN_JINYAN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		countryQiujinAndJinyanForClients = new CountryQiujinAndJinyanForClient[len];
		for(int i = 0 ; i < countryQiujinAndJinyanForClients.length ; i++){
			countryQiujinAndJinyanForClients[i] = new CountryQiujinAndJinyanForClient();
			countryQiujinAndJinyanForClients[i].setPlayerId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			countryQiujinAndJinyanForClients[i].setPlayerName(new String(content,offset,len,"UTF-8"));
			offset += len;
			countryQiujinAndJinyanForClients[i].setLastQiujinTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			countryQiujinAndJinyanForClients[i].setLastJinyanTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
		}
	}

	public int getType() {
		return 0x70000AAA;
	}

	public String getTypeDescription() {
		return "QUERY_COUNTRY_QIUJIN_JINYAN_RES";
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
		for(int i = 0 ; i < countryQiujinAndJinyanForClients.length ; i++){
			len += 8;
			len += 2;
			if(countryQiujinAndJinyanForClients[i].getPlayerName() != null){
				try{
				len += countryQiujinAndJinyanForClients[i].getPlayerName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 8;
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

			buffer.putInt(countryQiujinAndJinyanForClients.length);
			for(int i = 0 ; i < countryQiujinAndJinyanForClients.length ; i++){
				buffer.putLong(countryQiujinAndJinyanForClients[i].getPlayerId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = countryQiujinAndJinyanForClients[i].getPlayerName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(countryQiujinAndJinyanForClients[i].getLastQiujinTime());
				buffer.putLong(countryQiujinAndJinyanForClients[i].getLastJinyanTime());
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
	public CountryQiujinAndJinyanForClient[] getCountryQiujinAndJinyanForClients(){
		return countryQiujinAndJinyanForClients;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setCountryQiujinAndJinyanForClients(CountryQiujinAndJinyanForClient[] countryQiujinAndJinyanForClients){
		this.countryQiujinAndJinyanForClients = countryQiujinAndJinyanForClients;
	}

}