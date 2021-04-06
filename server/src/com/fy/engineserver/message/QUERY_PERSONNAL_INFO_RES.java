package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询玩家附加信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>onlineTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seeState</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>brithDay.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>brithDay</td><td>String</td><td>brithDay.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>age</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>star</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>country</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>province</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>city</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>loving.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>loving</td><td>String</td><td>loving.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mood.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mood</td><td>String</td><td>mood.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>personShow.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>personShow</td><td>String</td><td>personShow.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class QUERY_PERSONNAL_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int onlineTime;
	byte seeState;
	String brithDay;
	int age;
	int star;
	int country;
	int province;
	int city;
	String loving;
	String mood;
	String personShow;

	public QUERY_PERSONNAL_INFO_RES(){
	}

	public QUERY_PERSONNAL_INFO_RES(long seqNum,int onlineTime,byte seeState,String brithDay,int age,int star,int country,int province,int city,String loving,String mood,String personShow){
		this.seqNum = seqNum;
		this.onlineTime = onlineTime;
		this.seeState = seeState;
		this.brithDay = brithDay;
		this.age = age;
		this.star = star;
		this.country = country;
		this.province = province;
		this.city = city;
		this.loving = loving;
		this.mood = mood;
		this.personShow = personShow;
	}

	public QUERY_PERSONNAL_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		onlineTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		seeState = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		brithDay = new String(content,offset,len,"UTF-8");
		offset += len;
		age = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		star = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		country = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		province = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		city = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		loving = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		mood = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		personShow = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x7000EC07;
	}

	public String getTypeDescription() {
		return "QUERY_PERSONNAL_INFO_RES";
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
		len += 1;
		len += 2;
		try{
			len +=brithDay.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=loving.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=mood.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=personShow.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			buffer.putInt(onlineTime);
			buffer.put(seeState);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = brithDay.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(age);
			buffer.putInt(star);
			buffer.putInt(country);
			buffer.putInt(province);
			buffer.putInt(city);
				try{
			tmpBytes1 = loving.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = mood.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = personShow.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	在线时间
	 */
	public int getOnlineTime(){
		return onlineTime;
	}

	/**
	 * 设置属性：
	 *	在线时间
	 */
	public void setOnlineTime(int onlineTime){
		this.onlineTime = onlineTime;
	}

	/**
	 * 获取属性：
	 *	可见状态 0完全公开 1仅好友可见 2完全保密
	 */
	public byte getSeeState(){
		return seeState;
	}

	/**
	 * 设置属性：
	 *	可见状态 0完全公开 1仅好友可见 2完全保密
	 */
	public void setSeeState(byte seeState){
		this.seeState = seeState;
	}

	/**
	 * 获取属性：
	 *	生日
	 */
	public String getBrithDay(){
		return brithDay;
	}

	/**
	 * 设置属性：
	 *	生日
	 */
	public void setBrithDay(String brithDay){
		this.brithDay = brithDay;
	}

	/**
	 * 获取属性：
	 *	年龄
	 */
	public int getAge(){
		return age;
	}

	/**
	 * 设置属性：
	 *	年龄
	 */
	public void setAge(int age){
		this.age = age;
	}

	/**
	 * 获取属性：
	 *	星座
	 */
	public int getStar(){
		return star;
	}

	/**
	 * 设置属性：
	 *	星座
	 */
	public void setStar(int star){
		this.star = star;
	}

	/**
	 * 获取属性：
	 *	国家
	 */
	public int getCountry(){
		return country;
	}

	/**
	 * 设置属性：
	 *	国家
	 */
	public void setCountry(int country){
		this.country = country;
	}

	/**
	 * 获取属性：
	 *	省
	 */
	public int getProvince(){
		return province;
	}

	/**
	 * 设置属性：
	 *	省
	 */
	public void setProvince(int province){
		this.province = province;
	}

	/**
	 * 获取属性：
	 *	市
	 */
	public int getCity(){
		return city;
	}

	/**
	 * 设置属性：
	 *	市
	 */
	public void setCity(int city){
		this.city = city;
	}

	/**
	 * 获取属性：
	 *	爱好
	 */
	public String getLoving(){
		return loving;
	}

	/**
	 * 设置属性：
	 *	爱好
	 */
	public void setLoving(String loving){
		this.loving = loving;
	}

	/**
	 * 获取属性：
	 *	心情
	 */
	public String getMood(){
		return mood;
	}

	/**
	 * 设置属性：
	 *	心情
	 */
	public void setMood(String mood){
		this.mood = mood;
	}

	/**
	 * 获取属性：
	 *	个人说明
	 */
	public String getPersonShow(){
		return personShow;
	}

	/**
	 * 设置属性：
	 *	个人说明
	 */
	public void setPersonShow(String personShow){
		this.personShow = personShow;
	}

}