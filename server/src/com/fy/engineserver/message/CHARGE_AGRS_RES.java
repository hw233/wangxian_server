package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 充值参数<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modeName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>modeName</td><td>String</td><td>modeName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chargeId</td><td>String</td><td>chargeId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>specConfig.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>specConfig</td><td>String</td><td>specConfig.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeValue.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>chargeValue</td><td>String</td><td>chargeValue.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chargeType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>money</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>args.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>args[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>args[0]</td><td>String</td><td>args[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>args[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>args[1]</td><td>String</td><td>args[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>args[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>args[2]</td><td>String</td><td>args[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CHARGE_AGRS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String modeName;
	String chargeId;
	String specConfig;
	String chargeValue;
	int chargeType;
	long money;
	String[] args;

	public CHARGE_AGRS_RES(){
	}

	public CHARGE_AGRS_RES(long seqNum,String modeName,String chargeId,String specConfig,String chargeValue,int chargeType,long money,String[] args){
		this.seqNum = seqNum;
		this.modeName = modeName;
		this.chargeId = chargeId;
		this.specConfig = specConfig;
		this.chargeValue = chargeValue;
		this.chargeType = chargeType;
		this.money = money;
		this.args = args;
	}

	public CHARGE_AGRS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		modeName = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chargeId = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		specConfig = new String(content,offset,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		chargeValue = new String(content,offset,len);
		offset += len;
		chargeType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		money = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		args = new String[len];
		for(int i = 0 ; i < args.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			args[i] = new String(content,offset,len);
		offset += len;
		}
	}

	public int getType() {
		return 0x70FF0074;
	}

	public String getTypeDescription() {
		return "CHARGE_AGRS_RES";
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
		len +=modeName.getBytes().length;
		len += 2;
		len +=chargeId.getBytes().length;
		len += 2;
		len +=specConfig.getBytes().length;
		len += 2;
		len +=chargeValue.getBytes().length;
		len += 4;
		len += 8;
		len += 4;
		for(int i = 0 ; i < args.length; i++){
			len += 2;
			len += args[i].getBytes().length;
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
			tmpBytes1 = modeName.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = chargeId.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = specConfig.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = chargeValue.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(chargeType);
			buffer.putLong(money);
			buffer.putInt(args.length);
			for(int i = 0 ; i < args.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = args[i].getBytes();
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
	 *	充值渠道
	 */
	public String getModeName(){
		return modeName;
	}

	/**
	 * 设置属性：
	 *	充值渠道
	 */
	public void setModeName(String modeName){
		this.modeName = modeName;
	}

	/**
	 * 获取属性：
	 *	充值金额id
	 */
	public String getChargeId(){
		return chargeId;
	}

	/**
	 * 设置属性：
	 *	充值金额id
	 */
	public void setChargeId(String chargeId){
		this.chargeId = chargeId;
	}

	/**
	 * 获取属性：
	 *	特殊配置
	 */
	public String getSpecConfig(){
		return specConfig;
	}

	/**
	 * 设置属性：
	 *	特殊配置
	 */
	public void setSpecConfig(String specConfig){
		this.specConfig = specConfig;
	}

	/**
	 * 获取属性：
	 *	充值参数值
	 */
	public String getChargeValue(){
		return chargeValue;
	}

	/**
	 * 设置属性：
	 *	充值参数值
	 */
	public void setChargeValue(String chargeValue){
		this.chargeValue = chargeValue;
	}

	/**
	 * 获取属性：
	 *	充值原因类型
	 */
	public int getChargeType(){
		return chargeType;
	}

	/**
	 * 设置属性：
	 *	充值原因类型
	 */
	public void setChargeType(int chargeType){
		this.chargeType = chargeType;
	}

	/**
	 * 获取属性：
	 *	钱(单位分)
	 */
	public long getMoney(){
		return money;
	}

	/**
	 * 设置属性：
	 *	钱(单位分)
	 */
	public void setMoney(long money){
		this.money = money;
	}

	/**
	 * 获取属性：
	 *	参数名
	 */
	public String[] getArgs(){
		return args;
	}

	/**
	 * 设置属性：
	 *	参数名
	 */
	public void setArgs(String[] args){
		this.args = args;
	}

}