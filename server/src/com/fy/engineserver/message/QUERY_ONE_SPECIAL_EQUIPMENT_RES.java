package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询指定某一特殊特殊装备<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentName</td><td>String</td><td>equpmentName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icon</td><td>String</td><td>icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equpmentLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equpmentClass</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>storyDescribe.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>storyDescribe</td><td>String</td><td>storyDescribe.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentInfo</td><td>String</td><td>equipmentInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerNames[0]</td><td>String</td><td>ownerNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerNames[1]</td><td>String</td><td>ownerNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerNames[2]</td><td>String</td><td>ownerNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerCareer.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerCareer</td><td>byte[]</td><td>ownerCareer.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerCountry.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerCountry</td><td>byte[]</td><td>ownerCountry.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerLevel.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerLevel</td><td>int[]</td><td>ownerLevel.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ownerClassLevel.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerClassLevel</td><td>short[]</td><td>ownerClassLevel.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_ONE_SPECIAL_EQUIPMENT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String equpmentName;
	String icon;
	int equpmentLevel;
	int equpmentClass;
	String storyDescribe;
	String equipmentInfo;
	String[] ownerNames;
	byte[] ownerCareer;
	byte[] ownerCountry;
	int[] ownerLevel;
	short[] ownerClassLevel;

	public QUERY_ONE_SPECIAL_EQUIPMENT_RES(){
	}

	public QUERY_ONE_SPECIAL_EQUIPMENT_RES(long seqNum,String equpmentName,String icon,int equpmentLevel,int equpmentClass,String storyDescribe,String equipmentInfo,String[] ownerNames,byte[] ownerCareer,byte[] ownerCountry,int[] ownerLevel,short[] ownerClassLevel){
		this.seqNum = seqNum;
		this.equpmentName = equpmentName;
		this.icon = icon;
		this.equpmentLevel = equpmentLevel;
		this.equpmentClass = equpmentClass;
		this.storyDescribe = storyDescribe;
		this.equipmentInfo = equipmentInfo;
		this.ownerNames = ownerNames;
		this.ownerCareer = ownerCareer;
		this.ownerCountry = ownerCountry;
		this.ownerLevel = ownerLevel;
		this.ownerClassLevel = ownerClassLevel;
	}

	public QUERY_ONE_SPECIAL_EQUIPMENT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		equpmentName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		icon = new String(content,offset,len,"UTF-8");
		offset += len;
		equpmentLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		equpmentClass = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		storyDescribe = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		equipmentInfo = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ownerNames = new String[len];
		for(int i = 0 ; i < ownerNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			ownerNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ownerCareer = new byte[len];
		System.arraycopy(content,offset,ownerCareer,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ownerCountry = new byte[len];
		System.arraycopy(content,offset,ownerCountry,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ownerLevel = new int[len];
		for(int i = 0 ; i < ownerLevel.length ; i++){
			ownerLevel[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ownerClassLevel = new short[len];
		for(int i = 0 ; i < ownerClassLevel.length ; i++){
			ownerClassLevel[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
	}

	public int getType() {
		return 0x700F0201;
	}

	public String getTypeDescription() {
		return "QUERY_ONE_SPECIAL_EQUIPMENT_RES";
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
			len +=equpmentName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=icon.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=storyDescribe.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=equipmentInfo.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < ownerNames.length; i++){
			len += 2;
			try{
				len += ownerNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += ownerCareer.length;
		len += 4;
		len += ownerCountry.length;
		len += 4;
		len += ownerLevel.length * 4;
		len += 4;
		len += ownerClassLevel.length * 2;
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
			 tmpBytes1 = equpmentName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = icon.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(equpmentLevel);
			buffer.putInt(equpmentClass);
				try{
			tmpBytes1 = storyDescribe.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = equipmentInfo.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(ownerNames.length);
			for(int i = 0 ; i < ownerNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = ownerNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(ownerCareer.length);
			buffer.put(ownerCareer);
			buffer.putInt(ownerCountry.length);
			buffer.put(ownerCountry);
			buffer.putInt(ownerLevel.length);
			for(int i = 0 ; i < ownerLevel.length; i++){
				buffer.putInt(ownerLevel[i]);
			}
			buffer.putInt(ownerClassLevel.length);
			for(int i = 0 ; i < ownerClassLevel.length; i++){
				buffer.putShort(ownerClassLevel[i]);
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
	 *	特殊装备名称
	 */
	public String getEqupmentName(){
		return equpmentName;
	}

	/**
	 * 设置属性：
	 *	特殊装备名称
	 */
	public void setEqupmentName(String equpmentName){
		this.equpmentName = equpmentName;
	}

	/**
	 * 获取属性：
	 *	装备图标
	 */
	public String getIcon(){
		return icon;
	}

	/**
	 * 设置属性：
	 *	装备图标
	 */
	public void setIcon(String icon){
		this.icon = icon;
	}

	/**
	 * 获取属性：
	 *	特殊装备级别
	 */
	public int getEqupmentLevel(){
		return equpmentLevel;
	}

	/**
	 * 设置属性：
	 *	特殊装备级别
	 */
	public void setEqupmentLevel(int equpmentLevel){
		this.equpmentLevel = equpmentLevel;
	}

	/**
	 * 获取属性：
	 *	特殊装备境界
	 */
	public int getEqupmentClass(){
		return equpmentClass;
	}

	/**
	 * 设置属性：
	 *	特殊装备境界
	 */
	public void setEqupmentClass(int equpmentClass){
		this.equpmentClass = equpmentClass;
	}

	/**
	 * 获取属性：
	 *	特殊装备故事描述
	 */
	public String getStoryDescribe(){
		return storyDescribe;
	}

	/**
	 * 设置属性：
	 *	特殊装备故事描述
	 */
	public void setStoryDescribe(String storyDescribe){
		this.storyDescribe = storyDescribe;
	}

	/**
	 * 获取属性：
	 *	特殊装备描述
	 */
	public String getEquipmentInfo(){
		return equipmentInfo;
	}

	/**
	 * 设置属性：
	 *	特殊装备描述
	 */
	public void setEquipmentInfo(String equipmentInfo){
		this.equipmentInfo = equipmentInfo;
	}

	/**
	 * 获取属性：
	 *	拥有者名字
	 */
	public String[] getOwnerNames(){
		return ownerNames;
	}

	/**
	 * 设置属性：
	 *	拥有者名字
	 */
	public void setOwnerNames(String[] ownerNames){
		this.ownerNames = ownerNames;
	}

	/**
	 * 获取属性：
	 *	拥有者职业
	 */
	public byte[] getOwnerCareer(){
		return ownerCareer;
	}

	/**
	 * 设置属性：
	 *	拥有者职业
	 */
	public void setOwnerCareer(byte[] ownerCareer){
		this.ownerCareer = ownerCareer;
	}

	/**
	 * 获取属性：
	 *	拥有者国家
	 */
	public byte[] getOwnerCountry(){
		return ownerCountry;
	}

	/**
	 * 设置属性：
	 *	拥有者国家
	 */
	public void setOwnerCountry(byte[] ownerCountry){
		this.ownerCountry = ownerCountry;
	}

	/**
	 * 获取属性：
	 *	拥有者等级
	 */
	public int[] getOwnerLevel(){
		return ownerLevel;
	}

	/**
	 * 设置属性：
	 *	拥有者等级
	 */
	public void setOwnerLevel(int[] ownerLevel){
		this.ownerLevel = ownerLevel;
	}

	/**
	 * 获取属性：
	 *	拥有者境界
	 */
	public short[] getOwnerClassLevel(){
		return ownerClassLevel;
	}

	/**
	 * 设置属性：
	 *	拥有者境界
	 */
	public void setOwnerClassLevel(short[] ownerClassLevel){
		this.ownerClassLevel = ownerClassLevel;
	}

}