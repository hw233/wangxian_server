package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求服务器，装备打孔的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>confirmType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resultString.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resultString</td><td>String</td><td>resultString.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gemIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gemIds</td><td>long[]</td><td>gemIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>availableDrillTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxDrillTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialName</td><td>String</td><td>materialName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialAmount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messages.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messages[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messages[0]</td><td>String</td><td>messages[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messages[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messages[1]</td><td>String</td><td>messages[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>messages[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>messages[2]</td><td>String</td><td>messages[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>price</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class EQUIPMENT_DRILL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte confirmType;
	byte result;
	String resultString;
	long equipmentId;
	long[] gemIds;
	int availableDrillTimes;
	int maxDrillTimes;
	String materialName;
	int materialAmount;
	String[] messages;
	int price;
	int index;

	public EQUIPMENT_DRILL_RES(){
	}

	public EQUIPMENT_DRILL_RES(long seqNum,byte confirmType,byte result,String resultString,long equipmentId,long[] gemIds,int availableDrillTimes,int maxDrillTimes,String materialName,int materialAmount,String[] messages,int price,int index){
		this.seqNum = seqNum;
		this.confirmType = confirmType;
		this.result = result;
		this.resultString = resultString;
		this.equipmentId = equipmentId;
		this.gemIds = gemIds;
		this.availableDrillTimes = availableDrillTimes;
		this.maxDrillTimes = maxDrillTimes;
		this.materialName = materialName;
		this.materialAmount = materialAmount;
		this.messages = messages;
		this.price = price;
		this.index = index;
	}

	public EQUIPMENT_DRILL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		confirmType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		resultString = new String(content,offset,len,"UTF-8");
		offset += len;
		equipmentId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		gemIds = new long[len];
		for(int i = 0 ; i < gemIds.length ; i++){
			gemIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		availableDrillTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxDrillTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		materialName = new String(content,offset,len,"UTF-8");
		offset += len;
		materialAmount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		messages = new String[len];
		for(int i = 0 ; i < messages.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			messages[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		price = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F1EEF2;
	}

	public String getTypeDescription() {
		return "EQUIPMENT_DRILL_RES";
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
		len += 1;
		len += 1;
		len += 2;
		try{
			len +=resultString.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 4;
		len += gemIds.length * 8;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=materialName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		for(int i = 0 ; i < messages.length; i++){
			len += 2;
			try{
				len += messages[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
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

			buffer.put(confirmType);
			buffer.put(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = resultString.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(equipmentId);
			buffer.putInt(gemIds.length);
			for(int i = 0 ; i < gemIds.length; i++){
				buffer.putLong(gemIds[i]);
			}
			buffer.putInt(availableDrillTimes);
			buffer.putInt(maxDrillTimes);
				try{
			tmpBytes1 = materialName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(materialAmount);
			buffer.putInt(messages.length);
			for(int i = 0 ; i < messages.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = messages[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(price);
			buffer.putInt(index);
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
	 *	确认标记，0代表给服务器发消息，想要重置，需要服务器传回重置所需物品的信息。1代表确认重置
	 */
	public byte getConfirmType(){
		return confirmType;
	}

	/**
	 * 设置属性：
	 *	确认标记，0代表给服务器发消息，想要重置，需要服务器传回重置所需物品的信息。1代表确认重置
	 */
	public void setConfirmType(byte confirmType){
		this.confirmType = confirmType;
	}

	/**
	 * 获取属性：
	 *	打孔查询模式下， 0标识成功返回了信息，1标识玩家没有此装备，2标识此装备不可打孔；打孔模式下：0标识打孔成功，1标识打孔未成功，消耗了材料，2标识打孔失败
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	打孔查询模式下， 0标识成功返回了信息，1标识玩家没有此装备，2标识此装备不可打孔；打孔模式下：0标识打孔成功，1标识打孔未成功，消耗了材料，2标识打孔失败
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	结果提示信息
	 */
	public String getResultString(){
		return resultString;
	}

	/**
	 * 设置属性：
	 *	结果提示信息
	 */
	public void setResultString(String resultString){
		this.resultString = resultString;
	}

	/**
	 * 获取属性：
	 *	要升级的装备，玩家的背包中哦你必须有此装备
	 */
	public long getEquipmentId(){
		return equipmentId;
	}

	/**
	 * 设置属性：
	 *	要升级的装备，玩家的背包中哦你必须有此装备
	 */
	public void setEquipmentId(long equipmentId){
		this.equipmentId = equipmentId;
	}

	/**
	 * 获取属性：
	 *	对应的各个孔上的宝石，没有宝石对应的值为-1
	 */
	public long[] getGemIds(){
		return gemIds;
	}

	/**
	 * 设置属性：
	 *	对应的各个孔上的宝石，没有宝石对应的值为-1
	 */
	public void setGemIds(long[] gemIds){
		this.gemIds = gemIds;
	}

	/**
	 * 获取属性：
	 *	还可以打几次孔，0标识不能再打孔了
	 */
	public int getAvailableDrillTimes(){
		return availableDrillTimes;
	}

	/**
	 * 设置属性：
	 *	还可以打几次孔，0标识不能再打孔了
	 */
	public void setAvailableDrillTimes(int availableDrillTimes){
		this.availableDrillTimes = availableDrillTimes;
	}

	/**
	 * 获取属性：
	 *	此装备最多能打多少次孔
	 */
	public int getMaxDrillTimes(){
		return maxDrillTimes;
	}

	/**
	 * 设置属性：
	 *	此装备最多能打多少次孔
	 */
	public void setMaxDrillTimes(int maxDrillTimes){
		this.maxDrillTimes = maxDrillTimes;
	}

	/**
	 * 获取属性：
	 *	打孔需要的材料
	 */
	public String getMaterialName(){
		return materialName;
	}

	/**
	 * 设置属性：
	 *	打孔需要的材料
	 */
	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	/**
	 * 获取属性：
	 *	一共可以放置多少的材料
	 */
	public int getMaterialAmount(){
		return materialAmount;
	}

	/**
	 * 设置属性：
	 *	一共可以放置多少的材料
	 */
	public void setMaterialAmount(int materialAmount){
		this.materialAmount = materialAmount;
	}

	/**
	 * 获取属性：
	 *	成功率提示信息
	 */
	public String[] getMessages(){
		return messages;
	}

	/**
	 * 设置属性：
	 *	成功率提示信息
	 */
	public void setMessages(String[] messages){
		this.messages = messages;
	}

	/**
	 * 获取属性：
	 *	所需的手续费
	 */
	public int getPrice(){
		return price;
	}

	/**
	 * 设置属性：
	 *	所需的手续费
	 */
	public void setPrice(int price){
		this.price = price;
	}

	/**
	 * 获取属性：
	 *	装备在背包中的位置
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	装备在背包中的位置
	 */
	public void setIndex(int index){
		this.index = index;
	}

}