package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 管理玩家的社会关系，包括好友、黑名单和仇人。操作结果用通用提示指令通知<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mood.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mood</td><td>String</td><td>mood.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>career.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>career</td><td>String</td><td>career.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gang.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gang</td><td>String</td><td>gang.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazu</td><td>String</td><td>jiazu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>honor.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>honor</td><td>String</td><td>honor.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>relationShip</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>birth.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>birth</td><td>String</td><td>birth.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>star</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>online</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>autoBack</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SOCIAL_QUERY_PLAYERINFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long id;
	String name;
	String mood;
	int level;
	String career;
	String gang;
	String jiazu;
	String honor;
	byte relationShip;
	String birth;
	int star;
	boolean online;
	byte autoBack;

	public SOCIAL_QUERY_PLAYERINFO_RES(){
	}

	public SOCIAL_QUERY_PLAYERINFO_RES(long seqNum,long id,String name,String mood,int level,String career,String gang,String jiazu,String honor,byte relationShip,String birth,int star,boolean online,byte autoBack){
		this.seqNum = seqNum;
		this.id = id;
		this.name = name;
		this.mood = mood;
		this.level = level;
		this.career = career;
		this.gang = gang;
		this.jiazu = jiazu;
		this.honor = honor;
		this.relationShip = relationShip;
		this.birth = birth;
		this.star = star;
		this.online = online;
		this.autoBack = autoBack;
	}

	public SOCIAL_QUERY_PLAYERINFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		mood = new String(content,offset,len,"UTF-8");
		offset += len;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		career = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gang = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		jiazu = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		honor = new String(content,offset,len,"UTF-8");
		offset += len;
		relationShip = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		birth = new String(content,offset,len,"UTF-8");
		offset += len;
		star = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		online = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		autoBack = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x7000EC03;
	}

	public String getTypeDescription() {
		return "SOCIAL_QUERY_PLAYERINFO_RES";
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
		len += 8;
		len += 2;
		try{
			len +=name.getBytes("UTF-8").length;
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
		len += 4;
		len += 2;
		try{
			len +=career.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=gang.getBytes("UTF-8").length;
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
		len += 2;
		try{
			len +=honor.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 2;
		try{
			len +=birth.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 1;
		len += 1;
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

			buffer.putLong(id);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = name.getBytes("UTF-8");
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
			buffer.putInt(level);
				try{
			tmpBytes1 = career.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = gang.getBytes("UTF-8");
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
				try{
			tmpBytes1 = honor.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(relationShip);
				try{
			tmpBytes1 = birth.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(star);
			buffer.put((byte)(online==false?0:1));
			buffer.put(autoBack);
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
	 *	id
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	id
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	名字
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	名字
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	玩家心情
	 */
	public String getMood(){
		return mood;
	}

	/**
	 * 设置属性：
	 *	玩家心情
	 */
	public void setMood(String mood){
		this.mood = mood;
	}

	/**
	 * 获取属性：
	 *	玩家级别
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	玩家级别
	 */
	public void setLevel(int level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	职业
	 */
	public String getCareer(){
		return career;
	}

	/**
	 * 设置属性：
	 *	职业
	 */
	public void setCareer(String career){
		this.career = career;
	}

	/**
	 * 获取属性：
	 *	帮会
	 */
	public String getGang(){
		return gang;
	}

	/**
	 * 设置属性：
	 *	帮会
	 */
	public void setGang(String gang){
		this.gang = gang;
	}

	/**
	 * 获取属性：
	 *	家族
	 */
	public String getJiazu(){
		return jiazu;
	}

	/**
	 * 设置属性：
	 *	家族
	 */
	public void setJiazu(String jiazu){
		this.jiazu = jiazu;
	}

	/**
	 * 获取属性：
	 *	称号
	 */
	public String getHonor(){
		return honor;
	}

	/**
	 * 设置属性：
	 *	称号
	 */
	public void setHonor(String honor){
		this.honor = honor;
	}

	/**
	 * 获取属性：
	 *	关系
	 */
	public byte getRelationShip(){
		return relationShip;
	}

	/**
	 * 设置属性：
	 *	关系
	 */
	public void setRelationShip(byte relationShip){
		this.relationShip = relationShip;
	}

	/**
	 * 获取属性：
	 *	生日
	 */
	public String getBirth(){
		return birth;
	}

	/**
	 * 设置属性：
	 *	生日
	 */
	public void setBirth(String birth){
		this.birth = birth;
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
	 *	是否在线
	 */
	public boolean getOnline(){
		return online;
	}

	/**
	 * 设置属性：
	 *	是否在线
	 */
	public void setOnline(boolean online){
		this.online = online;
	}

	/**
	 * 获取属性：
	 *	状态
	 */
	public byte getAutoBack(){
		return autoBack;
	}

	/**
	 * 设置属性：
	 *	状态
	 */
	public void setAutoBack(byte autoBack){
		this.autoBack = autoBack;
	}

}