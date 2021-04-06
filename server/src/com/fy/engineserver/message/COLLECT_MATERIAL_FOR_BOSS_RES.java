package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 捐献材料削弱boss<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resultMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resultMess</td><td>String</td><td>resultMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needMaterialId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossBuffName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossBuffName</td><td>String</td><td>bossBuffName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossBuffIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossBuffIcon</td><td>String</td><td>bossBuffIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bossBuffInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossBuffInfo</td><td>String</td><td>bossBuffInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currPoints</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allPoints</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>conllectType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bossType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialName</td><td>String</td><td>materialName.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class COLLECT_MATERIAL_FOR_BOSS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String resultMess;
	long needMaterialId;
	String bossBuffName;
	String bossBuffIcon;
	String bossBuffInfo;
	long currPoints;
	long allPoints;
	int conllectType;
	int bossType;
	String materialName;

	public COLLECT_MATERIAL_FOR_BOSS_RES(){
	}

	public COLLECT_MATERIAL_FOR_BOSS_RES(long seqNum,String resultMess,long needMaterialId,String bossBuffName,String bossBuffIcon,String bossBuffInfo,long currPoints,long allPoints,int conllectType,int bossType,String materialName){
		this.seqNum = seqNum;
		this.resultMess = resultMess;
		this.needMaterialId = needMaterialId;
		this.bossBuffName = bossBuffName;
		this.bossBuffIcon = bossBuffIcon;
		this.bossBuffInfo = bossBuffInfo;
		this.currPoints = currPoints;
		this.allPoints = allPoints;
		this.conllectType = conllectType;
		this.bossType = bossType;
		this.materialName = materialName;
	}

	public COLLECT_MATERIAL_FOR_BOSS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		resultMess = new String(content,offset,len,"UTF-8");
		offset += len;
		needMaterialId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		bossBuffName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		bossBuffIcon = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		bossBuffInfo = new String(content,offset,len,"UTF-8");
		offset += len;
		currPoints = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		allPoints = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		conllectType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		bossType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		materialName = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x70F0EF66;
	}

	public String getTypeDescription() {
		return "COLLECT_MATERIAL_FOR_BOSS_RES";
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
			len +=resultMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 2;
		try{
			len +=bossBuffName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=bossBuffIcon.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=bossBuffInfo.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 8;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=materialName.getBytes("UTF-8").length;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = resultMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(needMaterialId);
				try{
			tmpBytes1 = bossBuffName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = bossBuffIcon.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = bossBuffInfo.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(currPoints);
			buffer.putLong(allPoints);
			buffer.putInt(conllectType);
			buffer.putInt(bossType);
				try{
			tmpBytes1 = materialName.getBytes("UTF-8");
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
	 *	描述信息
	 */
	public String getResultMess(){
		return resultMess;
	}

	/**
	 * 设置属性：
	 *	描述信息
	 */
	public void setResultMess(String resultMess){
		this.resultMess = resultMess;
	}

	/**
	 * 获取属性：
	 *	所需材料id
	 */
	public long getNeedMaterialId(){
		return needMaterialId;
	}

	/**
	 * 设置属性：
	 *	所需材料id
	 */
	public void setNeedMaterialId(long needMaterialId){
		this.needMaterialId = needMaterialId;
	}

	/**
	 * 获取属性：
	 *	boss削弱buff
	 */
	public String getBossBuffName(){
		return bossBuffName;
	}

	/**
	 * 设置属性：
	 *	boss削弱buff
	 */
	public void setBossBuffName(String bossBuffName){
		this.bossBuffName = bossBuffName;
	}

	/**
	 * 获取属性：
	 *	boss削弱buff图标
	 */
	public String getBossBuffIcon(){
		return bossBuffIcon;
	}

	/**
	 * 设置属性：
	 *	boss削弱buff图标
	 */
	public void setBossBuffIcon(String bossBuffIcon){
		this.bossBuffIcon = bossBuffIcon;
	}

	/**
	 * 获取属性：
	 *	buff描述
	 */
	public String getBossBuffInfo(){
		return bossBuffInfo;
	}

	/**
	 * 设置属性：
	 *	buff描述
	 */
	public void setBossBuffInfo(String bossBuffInfo){
		this.bossBuffInfo = bossBuffInfo;
	}

	/**
	 * 获取属性：
	 *	当前进度
	 */
	public long getCurrPoints(){
		return currPoints;
	}

	/**
	 * 设置属性：
	 *	当前进度
	 */
	public void setCurrPoints(long currPoints){
		this.currPoints = currPoints;
	}

	/**
	 * 获取属性：
	 *	总进度
	 */
	public long getAllPoints(){
		return allPoints;
	}

	/**
	 * 设置属性：
	 *	总进度
	 */
	public void setAllPoints(long allPoints){
		this.allPoints = allPoints;
	}

	/**
	 * 获取属性：
	 *	type:0-默认的，1-buff的
	 */
	public int getConllectType(){
		return conllectType;
	}

	/**
	 * 设置属性：
	 *	type:0-默认的，1-buff的
	 */
	public void setConllectType(int conllectType){
		this.conllectType = conllectType;
	}

	/**
	 * 获取属性：
	 *	type:0-Aboss，1-Bboss
	 */
	public int getBossType(){
		return bossType;
	}

	/**
	 * 设置属性：
	 *	type:0-Aboss，1-Bboss
	 */
	public void setBossType(int bossType){
		this.bossType = bossType;
	}

	/**
	 * 获取属性：
	 *	材料名字
	 */
	public String getMaterialName(){
		return materialName;
	}

	/**
	 * 设置属性：
	 *	材料名字
	 */
	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

}