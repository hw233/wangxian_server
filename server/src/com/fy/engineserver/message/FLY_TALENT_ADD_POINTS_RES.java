package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.talent.TalentSkillClientInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 天赋加减点<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canUsePoints</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aPoints</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bPoints</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo.length</td><td>int</td><td>4个字节</td><td>TalentSkillClientInfo数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[0].name</td><td>String</td><td>skillInfo[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[0].currLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[0].icon</td><td>String</td><td>skillInfo[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[0].mess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[0].mess</td><td>String</td><td>skillInfo[0].mess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[0].relyId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[0].talentType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[0].isOpen</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[0].skillLimitMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[0].skillLimitMess</td><td>String</td><td>skillInfo[0].skillLimitMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[1].name</td><td>String</td><td>skillInfo[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[1].currLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[1].icon</td><td>String</td><td>skillInfo[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[1].mess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[1].mess</td><td>String</td><td>skillInfo[1].mess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[1].relyId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[1].talentType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[1].isOpen</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[1].skillLimitMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[1].skillLimitMess</td><td>String</td><td>skillInfo[1].skillLimitMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[2].name</td><td>String</td><td>skillInfo[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[2].currLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[2].icon</td><td>String</td><td>skillInfo[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[2].mess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[2].mess</td><td>String</td><td>skillInfo[2].mess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[2].relyId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[2].talentType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[2].isOpen</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfo[2].skillLimitMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfo[2].skillLimitMess</td><td>String</td><td>skillInfo[2].skillLimitMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class FLY_TALENT_ADD_POINTS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int canUsePoints;
	int aPoints;
	int bPoints;
	TalentSkillClientInfo[] skillInfo;

	public FLY_TALENT_ADD_POINTS_RES(){
	}

	public FLY_TALENT_ADD_POINTS_RES(long seqNum,int canUsePoints,int aPoints,int bPoints,TalentSkillClientInfo[] skillInfo){
		this.seqNum = seqNum;
		this.canUsePoints = canUsePoints;
		this.aPoints = aPoints;
		this.bPoints = bPoints;
		this.skillInfo = skillInfo;
	}

	public FLY_TALENT_ADD_POINTS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		canUsePoints = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		aPoints = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		bPoints = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillInfo = new TalentSkillClientInfo[len];
		for(int i = 0 ; i < skillInfo.length ; i++){
			skillInfo[i] = new TalentSkillClientInfo();
			skillInfo[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfo[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillInfo[i].setCurrLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillInfo[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfo[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfo[i].setMess(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillInfo[i].setRelyId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillInfo[i].setTalentType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillInfo[i].setIsOpen(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfo[i].setSkillLimitMess(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x70FF0134;
	}

	public String getTypeDescription() {
		return "FLY_TALENT_ADD_POINTS_RES";
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
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < skillInfo.length ; i++){
			len += 4;
			len += 2;
			if(skillInfo[i].getName() != null){
				try{
				len += skillInfo[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 2;
			if(skillInfo[i].getIcon() != null){
				try{
				len += skillInfo[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillInfo[i].getMess() != null){
				try{
				len += skillInfo[i].getMess().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 1;
			len += 2;
			if(skillInfo[i].getSkillLimitMess() != null){
				try{
				len += skillInfo[i].getSkillLimitMess().getBytes("UTF-8").length;
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

			buffer.putInt(canUsePoints);
			buffer.putInt(aPoints);
			buffer.putInt(bPoints);
			buffer.putInt(skillInfo.length);
			for(int i = 0 ; i < skillInfo.length ; i++){
				buffer.putInt((int)skillInfo[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillInfo[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)skillInfo[i].getCurrLevel());
				buffer.putInt((int)skillInfo[i].getMaxLevel());
				try{
				tmpBytes2 = skillInfo[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillInfo[i].getMess().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)skillInfo[i].getRelyId());
				buffer.putInt((int)skillInfo[i].getTalentType());
				buffer.put((byte)(skillInfo[i].isIsOpen()==false?0:1));
				try{
				tmpBytes2 = skillInfo[i].getSkillLimitMess().getBytes("UTF-8");
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
	 *	可用点数
	 */
	public int getCanUsePoints(){
		return canUsePoints;
	}

	/**
	 * 设置属性：
	 *	可用点数
	 */
	public void setCanUsePoints(int canUsePoints){
		this.canUsePoints = canUsePoints;
	}

	/**
	 * 获取属性：
	 *	A天赋已加点数
	 */
	public int getAPoints(){
		return aPoints;
	}

	/**
	 * 设置属性：
	 *	A天赋已加点数
	 */
	public void setAPoints(int aPoints){
		this.aPoints = aPoints;
	}

	/**
	 * 获取属性：
	 *	B天赋已加点数
	 */
	public int getBPoints(){
		return bPoints;
	}

	/**
	 * 设置属性：
	 *	B天赋已加点数
	 */
	public void setBPoints(int bPoints){
		this.bPoints = bPoints;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public TalentSkillClientInfo[] getSkillInfo(){
		return skillInfo;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setSkillInfo(TalentSkillClientInfo[] skillInfo){
		this.skillInfo = skillInfo;
	}

}