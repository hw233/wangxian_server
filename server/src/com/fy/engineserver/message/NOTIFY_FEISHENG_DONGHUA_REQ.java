package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器通知客户端飞升<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>career</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playerName</td><td>String</td><td>playerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>country</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>zunpaiName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>zunpaiName</td><td>String</td><td>zunpaiName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazu</td><td>String</td><td>jiazu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avataType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avataType</td><td>byte[]</td><td>avataType.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata[0]</td><td>String</td><td>avata[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata[1]</td><td>String</td><td>avata[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata[2]</td><td>String</td><td>avata[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avataType2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avataType2</td><td>byte[]</td><td>avataType2.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata2[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata2[0]</td><td>String</td><td>avata2[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata2[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata2[1]</td><td>String</td><td>avata2[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata2[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata2[2]</td><td>String</td><td>avata2[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NOTIFY_FEISHENG_DONGHUA_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte countType;
	byte career;
	String playerName;
	byte country;
	String zunpaiName;
	String jiazu;
	byte[] avataType;
	String[] avata;
	byte[] avataType2;
	String[] avata2;

	public NOTIFY_FEISHENG_DONGHUA_REQ(){
	}

	public NOTIFY_FEISHENG_DONGHUA_REQ(long seqNum,byte countType,byte career,String playerName,byte country,String zunpaiName,String jiazu,byte[] avataType,String[] avata,byte[] avataType2,String[] avata2){
		this.seqNum = seqNum;
		this.countType = countType;
		this.career = career;
		this.playerName = playerName;
		this.country = country;
		this.zunpaiName = zunpaiName;
		this.jiazu = jiazu;
		this.avataType = avataType;
		this.avata = avata;
		this.avataType2 = avataType2;
		this.avata2 = avata2;
	}

	public NOTIFY_FEISHENG_DONGHUA_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		countType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		career = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		playerName = new String(content,offset,len,"UTF-8");
		offset += len;
		country = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		zunpaiName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		jiazu = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avataType = new byte[len];
		System.arraycopy(content,offset,avataType,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avata = new String[len];
		for(int i = 0 ; i < avata.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			avata[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avataType2 = new byte[len];
		System.arraycopy(content,offset,avataType2,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avata2 = new String[len];
		for(int i = 0 ; i < avata2.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			avata2[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x8E0EAA87;
	}

	public String getTypeDescription() {
		return "NOTIFY_FEISHENG_DONGHUA_REQ";
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
		len += 1;
		len += 2;
		try{
			len +=playerName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 2;
		try{
			len +=zunpaiName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=jiazu.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += avataType.length;
		len += 4;
		for(int i = 0 ; i < avata.length; i++){
			len += 2;
			try{
				len += avata[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += avataType2.length;
		len += 4;
		for(int i = 0 ; i < avata2.length; i++){
			len += 2;
			try{
				len += avata2[i].getBytes("UTF-8").length;
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

			buffer.put(countType);
			buffer.put(career);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = playerName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(country);
				try{
			tmpBytes1 = zunpaiName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = jiazu.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(avataType.length);
			buffer.put(avataType);
			buffer.putInt(avata.length);
			for(int i = 0 ; i < avata.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = avata[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(avataType2.length);
			buffer.put(avataType2);
			buffer.putInt(avata2.length);
			for(int i = 0 ; i < avata2.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = avata2[i].getBytes("UTF-8");
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
	 *	1为自己飞升，2为观礼
	 */
	public byte getCountType(){
		return countType;
	}

	/**
	 * 设置属性：
	 *	1为自己飞升，2为观礼
	 */
	public void setCountType(byte countType){
		this.countType = countType;
	}

	/**
	 * 获取属性：
	 *	职业
	 */
	public byte getCareer(){
		return career;
	}

	/**
	 * 设置属性：
	 *	职业
	 */
	public void setCareer(byte career){
		this.career = career;
	}

	/**
	 * 获取属性：
	 *	飞升人的名字
	 */
	public String getPlayerName(){
		return playerName;
	}

	/**
	 * 设置属性：
	 *	飞升人的名字
	 */
	public void setPlayerName(String playerName){
		this.playerName = playerName;
	}

	/**
	 * 获取属性：
	 *	国家
	 */
	public byte getCountry(){
		return country;
	}

	/**
	 * 设置属性：
	 *	国家
	 */
	public void setCountry(byte country){
		this.country = country;
	}

	/**
	 * 获取属性：
	 *	尊派名字
	 */
	public String getZunpaiName(){
		return zunpaiName;
	}

	/**
	 * 设置属性：
	 *	尊派名字
	 */
	public void setZunpaiName(String zunpaiName){
		this.zunpaiName = zunpaiName;
	}

	/**
	 * 获取属性：
	 *	家族名
	 */
	public String getJiazu(){
		return jiazu;
	}

	/**
	 * 设置属性：
	 *	家族名
	 */
	public void setJiazu(String jiazu){
		this.jiazu = jiazu;
	}

	/**
	 * 获取属性：
	 *	avatar部件
	 */
	public byte[] getAvataType(){
		return avataType;
	}

	/**
	 * 设置属性：
	 *	avatar部件
	 */
	public void setAvataType(byte[] avataType){
		this.avataType = avataType;
	}

	/**
	 * 获取属性：
	 *	avatar部件全名
	 */
	public String[] getAvata(){
		return avata;
	}

	/**
	 * 设置属性：
	 *	avatar部件全名
	 */
	public void setAvata(String[] avata){
		this.avata = avata;
	}

	/**
	 * 获取属性：
	 *	飞升后avatar部件
	 */
	public byte[] getAvataType2(){
		return avataType2;
	}

	/**
	 * 设置属性：
	 *	飞升后avatar部件
	 */
	public void setAvataType2(byte[] avataType2){
		this.avataType2 = avataType2;
	}

	/**
	 * 获取属性：
	 *	飞升后avatar部件全名
	 */
	public String[] getAvata2(){
		return avata2;
	}

	/**
	 * 设置属性：
	 *	飞升后avatar部件全名
	 */
	public void setAvata2(String[] avata2){
		this.avata2 = avata2;
	}

}