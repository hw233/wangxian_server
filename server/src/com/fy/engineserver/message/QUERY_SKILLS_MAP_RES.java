package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求技能列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIds</td><td>int[]</td><td>skillIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIcons.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIcons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIcons[0]</td><td>String</td><td>skillIcons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIcons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIcons[1]</td><td>String</td><td>skillIcons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillIcons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillIcons[2]</td><td>String</td><td>skillIcons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillType</td><td>int[]</td><td>skillType.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_SKILLS_MAP_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] skillIds;
	String[] skillIcons;
	int[] skillType;

	public QUERY_SKILLS_MAP_RES(){
	}

	public QUERY_SKILLS_MAP_RES(long seqNum,int[] skillIds,String[] skillIcons,int[] skillType){
		this.seqNum = seqNum;
		this.skillIds = skillIds;
		this.skillIcons = skillIcons;
		this.skillType = skillType;
	}

	public QUERY_SKILLS_MAP_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillIds = new int[len];
		for(int i = 0 ; i < skillIds.length ; i++){
			skillIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillIcons = new String[len];
		for(int i = 0 ; i < skillIcons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillIcons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		skillType = new int[len];
		for(int i = 0 ; i < skillType.length ; i++){
			skillType[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70F0EF50;
	}

	public String getTypeDescription() {
		return "QUERY_SKILLS_MAP_RES";
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
		len += skillIds.length * 4;
		len += 4;
		for(int i = 0 ; i < skillIcons.length; i++){
			len += 2;
			try{
				len += skillIcons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += skillType.length * 4;
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

			buffer.putInt(skillIds.length);
			for(int i = 0 ; i < skillIds.length; i++){
				buffer.putInt(skillIds[i]);
			}
			buffer.putInt(skillIcons.length);
			for(int i = 0 ; i < skillIcons.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillIcons[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(skillType.length);
			for(int i = 0 ; i < skillType.length; i++){
				buffer.putInt(skillType[i]);
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
	 *	技能id列表
	 */
	public int[] getSkillIds(){
		return skillIds;
	}

	/**
	 * 设置属性：
	 *	技能id列表
	 */
	public void setSkillIds(int[] skillIds){
		this.skillIds = skillIds;
	}

	/**
	 * 获取属性：
	 *	技能icon
	 */
	public String[] getSkillIcons(){
		return skillIcons;
	}

	/**
	 * 设置属性：
	 *	技能icon
	 */
	public void setSkillIcons(String[] skillIcons){
		this.skillIcons = skillIcons;
	}

	/**
	 * 获取属性：
	 *	0初级技能  1高级技能
	 */
	public int[] getSkillType(){
		return skillType;
	}

	/**
	 * 设置属性：
	 *	0初级技能  1高级技能
	 */
	public void setSkillType(int[] skillType){
		this.skillType = skillType;
	}

}