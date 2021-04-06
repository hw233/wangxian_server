package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.talent.TalentSkillClientInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 天赋技能界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>canUsePoints</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aPoints</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bPoints</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMess</td><td>String</td><td>helpMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>aName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aName</td><td>String</td><td>aName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bName</td><td>String</td><td>bName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos.length</td><td>int</td><td>4个字节</td><td>TalentSkillClientInfo数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].name</td><td>String</td><td>skillInfos[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].currLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].icon</td><td>String</td><td>skillInfos[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].mess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].mess</td><td>String</td><td>skillInfos[0].mess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].relyId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].talentType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].isOpen</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[0].skillLimitMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[0].skillLimitMess</td><td>String</td><td>skillInfos[0].skillLimitMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].name</td><td>String</td><td>skillInfos[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].currLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].icon</td><td>String</td><td>skillInfos[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].mess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].mess</td><td>String</td><td>skillInfos[1].mess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].relyId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].talentType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].isOpen</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[1].skillLimitMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[1].skillLimitMess</td><td>String</td><td>skillInfos[1].skillLimitMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].name</td><td>String</td><td>skillInfos[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].currLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].maxLevel</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].icon</td><td>String</td><td>skillInfos[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].mess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].mess</td><td>String</td><td>skillInfos[2].mess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].relyId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].talentType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].isOpen</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>skillInfos[2].skillLimitMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>skillInfos[2].skillLimitMess</td><td>String</td><td>skillInfos[2].skillLimitMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_TALENT_SKILLS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	int canUsePoints;
	int aPoints;
	int bPoints;
	String helpMess;
	String aName;
	String bName;
	TalentSkillClientInfo[] skillInfos;

	public QUERY_TALENT_SKILLS_RES(){
	}

	public QUERY_TALENT_SKILLS_RES(long seqNum,long playerId,int canUsePoints,int aPoints,int bPoints,String helpMess,String aName,String bName,TalentSkillClientInfo[] skillInfos){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.canUsePoints = canUsePoints;
		this.aPoints = aPoints;
		this.bPoints = bPoints;
		this.helpMess = helpMess;
		this.aName = aName;
		this.bName = bName;
		this.skillInfos = skillInfos;
	}

	public QUERY_TALENT_SKILLS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		canUsePoints = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		aPoints = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		bPoints = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		helpMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		aName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		bName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		skillInfos = new TalentSkillClientInfo[len];
		for(int i = 0 ; i < skillInfos.length ; i++){
			skillInfos[i] = new TalentSkillClientInfo();
			skillInfos[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfos[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillInfos[i].setCurrLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillInfos[i].setMaxLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfos[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfos[i].setMess(new String(content,offset,len,"UTF-8"));
			offset += len;
			skillInfos[i].setRelyId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillInfos[i].setTalentType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			skillInfos[i].setIsOpen(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			skillInfos[i].setSkillLimitMess(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x70FF0132;
	}

	public String getTypeDescription() {
		return "QUERY_TALENT_SKILLS_RES";
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
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=helpMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=aName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=bName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < skillInfos.length ; i++){
			len += 4;
			len += 2;
			if(skillInfos[i].getName() != null){
				try{
				len += skillInfos[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 2;
			if(skillInfos[i].getIcon() != null){
				try{
				len += skillInfos[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(skillInfos[i].getMess() != null){
				try{
				len += skillInfos[i].getMess().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 1;
			len += 2;
			if(skillInfos[i].getSkillLimitMess() != null){
				try{
				len += skillInfos[i].getSkillLimitMess().getBytes("UTF-8").length;
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

			buffer.putLong(playerId);
			buffer.putInt(canUsePoints);
			buffer.putInt(aPoints);
			buffer.putInt(bPoints);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = helpMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = aName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = bName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(skillInfos.length);
			for(int i = 0 ; i < skillInfos.length ; i++){
				buffer.putInt((int)skillInfos[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = skillInfos[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)skillInfos[i].getCurrLevel());
				buffer.putInt((int)skillInfos[i].getMaxLevel());
				try{
				tmpBytes2 = skillInfos[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = skillInfos[i].getMess().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)skillInfos[i].getRelyId());
				buffer.putInt((int)skillInfos[i].getTalentType());
				buffer.put((byte)(skillInfos[i].isIsOpen()==false?0:1));
				try{
				tmpBytes2 = skillInfos[i].getSkillLimitMess().getBytes("UTF-8");
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
	 *	角色id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	角色id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
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
	 *	帮助信息
	 */
	public String getHelpMess(){
		return helpMess;
	}

	/**
	 * 设置属性：
	 *	帮助信息
	 */
	public void setHelpMess(String helpMess){
		this.helpMess = helpMess;
	}

	/**
	 * 获取属性：
	 *	帮助信息
	 */
	public String getAName(){
		return aName;
	}

	/**
	 * 设置属性：
	 *	帮助信息
	 */
	public void setAName(String aName){
		this.aName = aName;
	}

	/**
	 * 获取属性：
	 *	帮助信息
	 */
	public String getBName(){
		return bName;
	}

	/**
	 * 设置属性：
	 *	帮助信息
	 */
	public void setBName(String bName){
		this.bName = bName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public TalentSkillClientInfo[] getSkillInfos(){
		return skillInfos;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setSkillInfos(TalentSkillClientInfo[] skillInfos){
		this.skillInfos = skillInfos;
	}

}