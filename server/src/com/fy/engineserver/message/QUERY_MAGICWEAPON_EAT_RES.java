package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 法宝吞噬<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costMess</td><td>String</td><td>costMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicPropertyMess.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicPropertyMess[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicPropertyMess[0]</td><td>String</td><td>basicPropertyMess[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicPropertyMess[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicPropertyMess[1]</td><td>String</td><td>basicPropertyMess[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicPropertyMess[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicPropertyMess[2]</td><td>String</td><td>basicPropertyMess[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>basicNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicNums</td><td>long[]</td><td>basicNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>addNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>addNums</td><td>long[]</td><td>addNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currAndNextJieJi.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currAndNextJieJi[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currAndNextJieJi[0]</td><td>String</td><td>currAndNextJieJi[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currAndNextJieJi[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currAndNextJieJi[1]</td><td>String</td><td>currAndNextJieJi[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currAndNextJieJi[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currAndNextJieJi[2]</td><td>String</td><td>currAndNextJieJi[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currExps</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>upgradeExps</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_MAGICWEAPON_EAT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String costMess;
	String[] basicPropertyMess;
	long[] basicNums;
	long[] addNums;
	String[] currAndNextJieJi;
	long currExps;
	long upgradeExps;

	public QUERY_MAGICWEAPON_EAT_RES(){
	}

	public QUERY_MAGICWEAPON_EAT_RES(long seqNum,String costMess,String[] basicPropertyMess,long[] basicNums,long[] addNums,String[] currAndNextJieJi,long currExps,long upgradeExps){
		this.seqNum = seqNum;
		this.costMess = costMess;
		this.basicPropertyMess = basicPropertyMess;
		this.basicNums = basicNums;
		this.addNums = addNums;
		this.currAndNextJieJi = currAndNextJieJi;
		this.currExps = currExps;
		this.upgradeExps = upgradeExps;
	}

	public QUERY_MAGICWEAPON_EAT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		costMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		basicPropertyMess = new String[len];
		for(int i = 0 ; i < basicPropertyMess.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			basicPropertyMess[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		basicNums = new long[len];
		for(int i = 0 ; i < basicNums.length ; i++){
			basicNums[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		addNums = new long[len];
		for(int i = 0 ; i < addNums.length ; i++){
			addNums[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		currAndNextJieJi = new String[len];
		for(int i = 0 ; i < currAndNextJieJi.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			currAndNextJieJi[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		currExps = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		upgradeExps = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70F0EF07;
	}

	public String getTypeDescription() {
		return "QUERY_MAGICWEAPON_EAT_RES";
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
			len +=costMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < basicPropertyMess.length; i++){
			len += 2;
			try{
				len += basicPropertyMess[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += basicNums.length * 8;
		len += 4;
		len += addNums.length * 8;
		len += 4;
		for(int i = 0 ; i < currAndNextJieJi.length; i++){
			len += 2;
			try{
				len += currAndNextJieJi[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = costMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(basicPropertyMess.length);
			for(int i = 0 ; i < basicPropertyMess.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = basicPropertyMess[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(basicNums.length);
			for(int i = 0 ; i < basicNums.length; i++){
				buffer.putLong(basicNums[i]);
			}
			buffer.putInt(addNums.length);
			for(int i = 0 ; i < addNums.length; i++){
				buffer.putLong(addNums[i]);
			}
			buffer.putInt(currAndNextJieJi.length);
			for(int i = 0 ; i < currAndNextJieJi.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = currAndNextJieJi[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putLong(currExps);
			buffer.putLong(upgradeExps);
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
	 *	主法宝消耗说明
	 */
	public String getCostMess(){
		return costMess;
	}

	/**
	 * 设置属性：
	 *	主法宝消耗说明
	 */
	public void setCostMess(String costMess){
		this.costMess = costMess;
	}

	/**
	 * 获取属性：
	 *	法宝吞噬基础属性描述
	 */
	public String[] getBasicPropertyMess(){
		return basicPropertyMess;
	}

	/**
	 * 设置属性：
	 *	法宝吞噬基础属性描述
	 */
	public void setBasicPropertyMess(String[] basicPropertyMess){
		this.basicPropertyMess = basicPropertyMess;
	}

	/**
	 * 获取属性：
	 *	基础属性值
	 */
	public long[] getBasicNums(){
		return basicNums;
	}

	/**
	 * 设置属性：
	 *	基础属性值
	 */
	public void setBasicNums(long[] basicNums){
		this.basicNums = basicNums;
	}

	/**
	 * 获取属性：
	 *	升级增加属性值
	 */
	public long[] getAddNums(){
		return addNums;
	}

	/**
	 * 设置属性：
	 *	升级增加属性值
	 */
	public void setAddNums(long[] addNums){
		this.addNums = addNums;
	}

	/**
	 * 获取属性：
	 *	当前和下一阶级
	 */
	public String[] getCurrAndNextJieJi(){
		return currAndNextJieJi;
	}

	/**
	 * 设置属性：
	 *	当前和下一阶级
	 */
	public void setCurrAndNextJieJi(String[] currAndNextJieJi){
		this.currAndNextJieJi = currAndNextJieJi;
	}

	/**
	 * 获取属性：
	 *	法宝当前加成经验
	 */
	public long getCurrExps(){
		return currExps;
	}

	/**
	 * 设置属性：
	 *	法宝当前加成经验
	 */
	public void setCurrExps(long currExps){
		this.currExps = currExps;
	}

	/**
	 * 获取属性：
	 *	法宝升最大级所需经验
	 */
	public long getUpgradeExps(){
		return upgradeExps;
	}

	/**
	 * 设置属性：
	 *	法宝升最大级所需经验
	 */
	public void setUpgradeExps(long upgradeExps){
		this.upgradeExps = upgradeExps;
	}

}