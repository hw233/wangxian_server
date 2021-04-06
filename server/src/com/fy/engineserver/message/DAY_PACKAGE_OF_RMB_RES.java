package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 每日礼包<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardType</td><td>int[]</td><td>cardType.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nums</td><td>long[]</td><td>nums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids2</td><td>long[]</td><td>ids2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mums2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mums2</td><td>long[]</td><td>mums2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids3.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids3</td><td>long[]</td><td>ids3.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nums3.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nums3</td><td>long[]</td><td>nums3.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modeName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modeName</td><td>String</td><td>modeName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionName</td><td>String</td><td>functionName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chargeIds[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeIds[0]</td><td>String</td><td>chargeIds[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chargeIds[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeIds[1]</td><td>String</td><td>chargeIds[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chargeIds[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeIds[2]</td><td>String</td><td>chargeIds[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>specConfigs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>specConfigs[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>specConfigs[0]</td><td>String</td><td>specConfigs[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>specConfigs[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>specConfigs[1]</td><td>String</td><td>specConfigs[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>specConfigs[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>specConfigs[2]</td><td>String</td><td>specConfigs[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buyType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buyType</td><td>int[]</td><td>buyType.length</td><td>*</td></tr>
 * </table>
 */
public class DAY_PACKAGE_OF_RMB_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] cardType;
	long[] ids;
	long[] nums;
	long[] ids2;
	long[] mums2;
	long[] ids3;
	long[] nums3;
	String modeName;
	String functionName;
	String[] chargeIds;
	String[] specConfigs;
	int[] buyType;

	public DAY_PACKAGE_OF_RMB_RES(){
	}

	public DAY_PACKAGE_OF_RMB_RES(long seqNum,int[] cardType,long[] ids,long[] nums,long[] ids2,long[] mums2,long[] ids3,long[] nums3,String modeName,String functionName,String[] chargeIds,String[] specConfigs,int[] buyType){
		this.seqNum = seqNum;
		this.cardType = cardType;
		this.ids = ids;
		this.nums = nums;
		this.ids2 = ids2;
		this.mums2 = mums2;
		this.ids3 = ids3;
		this.nums3 = nums3;
		this.modeName = modeName;
		this.functionName = functionName;
		this.chargeIds = chargeIds;
		this.specConfigs = specConfigs;
		this.buyType = buyType;
	}

	public DAY_PACKAGE_OF_RMB_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		cardType = new int[len];
		for(int i = 0 ; i < cardType.length ; i++){
			cardType[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids = new long[len];
		for(int i = 0 ; i < ids.length ; i++){
			ids[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		nums = new long[len];
		for(int i = 0 ; i < nums.length ; i++){
			nums[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids2 = new long[len];
		for(int i = 0 ; i < ids2.length ; i++){
			ids2[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		mums2 = new long[len];
		for(int i = 0 ; i < mums2.length ; i++){
			mums2[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids3 = new long[len];
		for(int i = 0 ; i < ids3.length ; i++){
			ids3[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		nums3 = new long[len];
		for(int i = 0 ; i < nums3.length ; i++){
			nums3[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		modeName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		functionName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		chargeIds = new String[len];
		for(int i = 0 ; i < chargeIds.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			chargeIds[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		specConfigs = new String[len];
		for(int i = 0 ; i < specConfigs.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			specConfigs[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		buyType = new int[len];
		for(int i = 0 ; i < buyType.length ; i++){
			buyType[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FFF142;
	}

	public String getTypeDescription() {
		return "DAY_PACKAGE_OF_RMB_RES";
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
		len += cardType.length * 4;
		len += 4;
		len += ids.length * 8;
		len += 4;
		len += nums.length * 8;
		len += 4;
		len += ids2.length * 8;
		len += 4;
		len += mums2.length * 8;
		len += 4;
		len += ids3.length * 8;
		len += 4;
		len += nums3.length * 8;
		len += 2;
		try{
			len +=modeName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=functionName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < chargeIds.length; i++){
			len += 2;
			try{
				len += chargeIds[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < specConfigs.length; i++){
			len += 2;
			try{
				len += specConfigs[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += buyType.length * 4;
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

			buffer.putInt(cardType.length);
			for(int i = 0 ; i < cardType.length; i++){
				buffer.putInt(cardType[i]);
			}
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(nums.length);
			for(int i = 0 ; i < nums.length; i++){
				buffer.putLong(nums[i]);
			}
			buffer.putInt(ids2.length);
			for(int i = 0 ; i < ids2.length; i++){
				buffer.putLong(ids2[i]);
			}
			buffer.putInt(mums2.length);
			for(int i = 0 ; i < mums2.length; i++){
				buffer.putLong(mums2[i]);
			}
			buffer.putInt(ids3.length);
			for(int i = 0 ; i < ids3.length; i++){
				buffer.putLong(ids3[i]);
			}
			buffer.putInt(nums3.length);
			for(int i = 0 ; i < nums3.length; i++){
				buffer.putLong(nums3[i]);
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = modeName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = functionName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(chargeIds.length);
			for(int i = 0 ; i < chargeIds.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = chargeIds[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(specConfigs.length);
			for(int i = 0 ; i < specConfigs.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = specConfigs[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(buyType.length);
			for(int i = 0 ; i < buyType.length; i++){
				buffer.putInt(buyType[i]);
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
	 *	1:每日，2:修真3:渡劫
	 */
	public int[] getCardType(){
		return cardType;
	}

	/**
	 * 设置属性：
	 *	1:每日，2:修真3:渡劫
	 */
	public void setCardType(int[] cardType){
		this.cardType = cardType;
	}

	/**
	 * 获取属性：
	 *	每日礼包ids
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	每日礼包ids
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	每日礼包ids
	 */
	public long[] getNums(){
		return nums;
	}

	/**
	 * 设置属性：
	 *	每日礼包ids
	 */
	public void setNums(long[] nums){
		this.nums = nums;
	}

	/**
	 * 获取属性：
	 *	修真ids
	 */
	public long[] getIds2(){
		return ids2;
	}

	/**
	 * 设置属性：
	 *	修真ids
	 */
	public void setIds2(long[] ids2){
		this.ids2 = ids2;
	}

	/**
	 * 获取属性：
	 *	每日礼包ids
	 */
	public long[] getMums2(){
		return mums2;
	}

	/**
	 * 设置属性：
	 *	每日礼包ids
	 */
	public void setMums2(long[] mums2){
		this.mums2 = mums2;
	}

	/**
	 * 获取属性：
	 *	渡劫ids
	 */
	public long[] getIds3(){
		return ids3;
	}

	/**
	 * 设置属性：
	 *	渡劫ids
	 */
	public void setIds3(long[] ids3){
		this.ids3 = ids3;
	}

	/**
	 * 获取属性：
	 *	每日礼包ids
	 */
	public long[] getNums3(){
		return nums3;
	}

	/**
	 * 设置属性：
	 *	每日礼包ids
	 */
	public void setNums3(long[] nums3){
		this.nums3 = nums3;
	}

	/**
	 * 获取属性：
	 *	充值卡名字
	 */
	public String getModeName(){
		return modeName;
	}

	/**
	 * 设置属性：
	 *	充值卡名字
	 */
	public void setModeName(String modeName){
		this.modeName = modeName;
	}

	/**
	 * 获取属性：
	 *	充值功能名字
	 */
	public String getFunctionName(){
		return functionName;
	}

	/**
	 * 设置属性：
	 *	充值功能名字
	 */
	public void setFunctionName(String functionName){
		this.functionName = functionName;
	}

	/**
	 * 获取属性：
	 *	充值ID
	 */
	public String[] getChargeIds(){
		return chargeIds;
	}

	/**
	 * 设置属性：
	 *	充值ID
	 */
	public void setChargeIds(String[] chargeIds){
		this.chargeIds = chargeIds;
	}

	/**
	 * 获取属性：
	 *	苹果充值档
	 */
	public String[] getSpecConfigs(){
		return specConfigs;
	}

	/**
	 * 设置属性：
	 *	苹果充值档
	 */
	public void setSpecConfigs(String[] specConfigs){
		this.specConfigs = specConfigs;
	}

	/**
	 * 获取属性：
	 *	1:已经购买过,2：需要购买修正卡，3：需要购买渡劫卡
	 */
	public int[] getBuyType(){
		return buyType;
	}

	/**
	 * 设置属性：
	 *	1:已经购买过,2：需要购买修正卡，3：需要购买渡劫卡
	 */
	public void setBuyType(int[] buyType){
		this.buyType = buyType;
	}

}