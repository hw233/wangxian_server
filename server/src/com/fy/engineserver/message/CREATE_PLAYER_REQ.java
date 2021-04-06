package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端向服务器发送创建新的角色的请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>race</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sex</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>country</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>career</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>quickcreate</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>recommendid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CREATE_PLAYER_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String name;
	byte race;
	byte sex;
	byte country;
	byte career;
	boolean quickcreate;
	long recommendid;

	public CREATE_PLAYER_REQ(){
	}

	public CREATE_PLAYER_REQ(long seqNum,String name,byte race,byte sex,byte country,byte career,boolean quickcreate,long recommendid){
		this.seqNum = seqNum;
		this.name = name;
		this.race = race;
		this.sex = sex;
		this.country = country;
		this.career = career;
		this.quickcreate = quickcreate;
		this.recommendid = recommendid;
	}

	public CREATE_PLAYER_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		race = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		sex = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		country = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		career = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		quickcreate = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		recommendid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x00000212;
	}

	public String getTypeDescription() {
		return "CREATE_PLAYER_REQ";
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
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 8;
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
			 tmpBytes1 = name.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(race);
			buffer.put(sex);
			buffer.put(country);
			buffer.put(career);
			buffer.put((byte)(quickcreate==false?0:1));
			buffer.putLong(recommendid);
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
	 *	玩家的昵称
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	玩家的昵称
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	1-人，2-妖
	 */
	public byte getRace(){
		return race;
	}

	/**
	 * 设置属性：
	 *	1-人，2-妖
	 */
	public void setRace(byte race){
		this.race = race;
	}

	/**
	 * 获取属性：
	 *	性别
	 */
	public byte getSex(){
		return sex;
	}

	/**
	 * 设置属性：
	 *	性别
	 */
	public void setSex(byte sex){
		this.sex = sex;
	}

	/**
	 * 获取属性：
	 *	国家0，1，2，3，..
	 */
	public byte getCountry(){
		return country;
	}

	/**
	 * 设置属性：
	 *	国家0，1，2，3，..
	 */
	public void setCountry(byte country){
		this.country = country;
	}

	/**
	 * 获取属性：
	 *	玩家的职业
	 */
	public byte getCareer(){
		return career;
	}

	/**
	 * 设置属性：
	 *	玩家的职业
	 */
	public void setCareer(byte career){
		this.career = career;
	}

	/**
	 * 获取属性：
	 *	是否是快速创建
	 */
	public boolean getQuickcreate(){
		return quickcreate;
	}

	/**
	 * 设置属性：
	 *	是否是快速创建
	 */
	public void setQuickcreate(boolean quickcreate){
		this.quickcreate = quickcreate;
	}

	/**
	 * 获取属性：
	 *	推荐关系ID,jad中包含此信息，-1为jad不含此值
	 */
	public long getRecommendid(){
		return recommendid;
	}

	/**
	 * 设置属性：
	 *	推荐关系ID,jad中包含此信息，-1为jad不含此值
	 */
	public void setRecommendid(long recommendid){
		this.recommendid = recommendid;
	}

}