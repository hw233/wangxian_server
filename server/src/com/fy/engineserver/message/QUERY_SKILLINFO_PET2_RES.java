package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求宠物某个《天赋or天生》技能的详细信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>id</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description</td><td>String</td><td>description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_SKILLINFO_PET2_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long petId;
	int id;
	String description;
	int level;

	public QUERY_SKILLINFO_PET2_RES(){
	}

	public QUERY_SKILLINFO_PET2_RES(long seqNum,long petId,int id,String description,int level){
		this.seqNum = seqNum;
		this.petId = petId;
		this.id = id;
		this.description = description;
		this.level = level;
	}

	public QUERY_SKILLINFO_PET2_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		petId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		id = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		description = new String(content,offset,len,"UTF-8");
		offset += len;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x8E0EAA65;
	}

	public String getTypeDescription() {
		return "QUERY_SKILLINFO_PET2_RES";
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
		len += 4;
		len += 2;
		try{
			len +=description.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
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

			buffer.putLong(petId);
			buffer.putInt(id);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = description.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(level);
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
	 *	拥有这个技能的宠物id
	 */
	public long getPetId(){
		return petId;
	}

	/**
	 * 设置属性：
	 *	拥有这个技能的宠物id
	 */
	public void setPetId(long petId){
		this.petId = petId;
	}

	/**
	 * 获取属性：
	 *	技能编号
	 */
	public int getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	技能编号
	 */
	public void setId(int id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	技能某个等级的详细描述（UUB格式）
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * 设置属性：
	 *	技能某个等级的详细描述（UUB格式）
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * 获取属性：
	 *	等级
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	等级
	 */
	public void setLevel(int level){
		this.level = level;
	}

}