package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 仙装合成<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tuZhiId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>newEquipmentId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialNames[0]</td><td>String</td><td>materialNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialNames[1]</td><td>String</td><td>materialNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialNames[2]</td><td>String</td><td>materialNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialIcons.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialIcons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialIcons[0]</td><td>String</td><td>materialIcons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialIcons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialIcons[1]</td><td>String</td><td>materialIcons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialIcons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialIcons[2]</td><td>String</td><td>materialIcons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialMess.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialMess[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialMess[0]</td><td>String</td><td>materialMess[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialMess[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialMess[1]</td><td>String</td><td>materialMess[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialMess[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialMess[2]</td><td>String</td><td>materialMess[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>needNums</td><td>int[]</td><td>needNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>types</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMess</td><td>String</td><td>showMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costSilver</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class GOD_EQUIPMENT_UPGRADE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long tuZhiId;
	long newEquipmentId;
	String[] materialNames;
	String[] materialIcons;
	String[] materialMess;
	int[] needNums;
	byte types;
	String showMess;
	long costSilver;

	public GOD_EQUIPMENT_UPGRADE_RES(){
	}

	public GOD_EQUIPMENT_UPGRADE_RES(long seqNum,long tuZhiId,long newEquipmentId,String[] materialNames,String[] materialIcons,String[] materialMess,int[] needNums,byte types,String showMess,long costSilver){
		this.seqNum = seqNum;
		this.tuZhiId = tuZhiId;
		this.newEquipmentId = newEquipmentId;
		this.materialNames = materialNames;
		this.materialIcons = materialIcons;
		this.materialMess = materialMess;
		this.needNums = needNums;
		this.types = types;
		this.showMess = showMess;
		this.costSilver = costSilver;
	}

	public GOD_EQUIPMENT_UPGRADE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		tuZhiId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		newEquipmentId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		materialNames = new String[len];
		for(int i = 0 ; i < materialNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			materialNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		materialIcons = new String[len];
		for(int i = 0 ; i < materialIcons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			materialIcons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		materialMess = new String[len];
		for(int i = 0 ; i < materialMess.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			materialMess[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		needNums = new int[len];
		for(int i = 0 ; i < needNums.length ; i++){
			needNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		types = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		showMess = new String(content,offset,len,"UTF-8");
		offset += len;
		costSilver = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70F0EF24;
	}

	public String getTypeDescription() {
		return "GOD_EQUIPMENT_UPGRADE_RES";
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
		len += 8;
		len += 4;
		for(int i = 0 ; i < materialNames.length; i++){
			len += 2;
			try{
				len += materialNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < materialIcons.length; i++){
			len += 2;
			try{
				len += materialIcons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < materialMess.length; i++){
			len += 2;
			try{
				len += materialMess[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += needNums.length * 4;
		len += 1;
		len += 2;
		try{
			len +=showMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putLong(tuZhiId);
			buffer.putLong(newEquipmentId);
			buffer.putInt(materialNames.length);
			for(int i = 0 ; i < materialNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = materialNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(materialIcons.length);
			for(int i = 0 ; i < materialIcons.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = materialIcons[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(materialMess.length);
			for(int i = 0 ; i < materialMess.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = materialMess[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(needNums.length);
			for(int i = 0 ; i < needNums.length; i++){
				buffer.putInt(needNums[i]);
			}
			buffer.put(types);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = showMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(costSilver);
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
	 *	图纸id
	 */
	public long getTuZhiId(){
		return tuZhiId;
	}

	/**
	 * 设置属性：
	 *	图纸id
	 */
	public void setTuZhiId(long tuZhiId){
		this.tuZhiId = tuZhiId;
	}

	/**
	 * 获取属性：
	 *	目标装备id
	 */
	public long getNewEquipmentId(){
		return newEquipmentId;
	}

	/**
	 * 设置属性：
	 *	目标装备id
	 */
	public void setNewEquipmentId(long newEquipmentId){
		this.newEquipmentId = newEquipmentId;
	}

	/**
	 * 获取属性：
	 *	材料name集合，如果type是0，第一个是装备name
	 */
	public String[] getMaterialNames(){
		return materialNames;
	}

	/**
	 * 设置属性：
	 *	材料name集合，如果type是0，第一个是装备name
	 */
	public void setMaterialNames(String[] materialNames){
		this.materialNames = materialNames;
	}

	/**
	 * 获取属性：
	 *	材料icon
	 */
	public String[] getMaterialIcons(){
		return materialIcons;
	}

	/**
	 * 设置属性：
	 *	材料icon
	 */
	public void setMaterialIcons(String[] materialIcons){
		this.materialIcons = materialIcons;
	}

	/**
	 * 获取属性：
	 *	材料泡泡
	 */
	public String[] getMaterialMess(){
		return materialMess;
	}

	/**
	 * 设置属性：
	 *	材料泡泡
	 */
	public void setMaterialMess(String[] materialMess){
		this.materialMess = materialMess;
	}

	/**
	 * 获取属性：
	 *	升级所需材料数
	 */
	public int[] getNeedNums(){
		return needNums;
	}

	/**
	 * 设置属性：
	 *	升级所需材料数
	 */
	public void setNeedNums(int[] needNums){
		this.needNums = needNums;
	}

	/**
	 * 获取属性：
	 *	操作类型：0.仙装合成  1.其他合成
	 */
	public byte getTypes(){
		return types;
	}

	/**
	 * 设置属性：
	 *	操作类型：0.仙装合成  1.其他合成
	 */
	public void setTypes(byte types){
		this.types = types;
	}

	/**
	 * 获取属性：
	 *	功能描述
	 */
	public String getShowMess(){
		return showMess;
	}

	/**
	 * 设置属性：
	 *	功能描述
	 */
	public void setShowMess(String showMess){
		this.showMess = showMess;
	}

	/**
	 * 获取属性：
	 *	消耗银两
	 */
	public long getCostSilver(){
		return costSilver;
	}

	/**
	 * 设置属性：
	 *	消耗银两
	 */
	public void setCostSilver(long costSilver){
		this.costSilver = costSilver;
	}

}