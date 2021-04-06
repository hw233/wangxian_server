package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取一个帮派仓库<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entityIds1.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entityIds1</td><td>long[]</td><td>entityIds1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entityIds2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entityIds2</td><td>long[]</td><td>entityIds2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entityIds3.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entityIds3</td><td>long[]</td><td>entityIds3.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>counts1.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>counts1</td><td>int[]</td><td>counts1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>counts2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>counts2</td><td>int[]</td><td>counts2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>counts3.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>counts3</td><td>int[]</td><td>counts3.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>funds</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yuanbaoToContribution</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yuanbaoToFunds</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class GANGWAREHOUSE_GET_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] entityIds1;
	long[] entityIds2;
	long[] entityIds3;
	int[] counts1;
	int[] counts2;
	int[] counts3;
	long funds;
	int yuanbaoToContribution;
	int yuanbaoToFunds;

	public GANGWAREHOUSE_GET_RES(){
	}

	public GANGWAREHOUSE_GET_RES(long seqNum,long[] entityIds1,long[] entityIds2,long[] entityIds3,int[] counts1,int[] counts2,int[] counts3,long funds,int yuanbaoToContribution,int yuanbaoToFunds){
		this.seqNum = seqNum;
		this.entityIds1 = entityIds1;
		this.entityIds2 = entityIds2;
		this.entityIds3 = entityIds3;
		this.counts1 = counts1;
		this.counts2 = counts2;
		this.counts3 = counts3;
		this.funds = funds;
		this.yuanbaoToContribution = yuanbaoToContribution;
		this.yuanbaoToFunds = yuanbaoToFunds;
	}

	public GANGWAREHOUSE_GET_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		entityIds1 = new long[len];
		for(int i = 0 ; i < entityIds1.length ; i++){
			entityIds1[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		entityIds2 = new long[len];
		for(int i = 0 ; i < entityIds2.length ; i++){
			entityIds2[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		entityIds3 = new long[len];
		for(int i = 0 ; i < entityIds3.length ; i++){
			entityIds3[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		counts1 = new int[len];
		for(int i = 0 ; i < counts1.length ; i++){
			counts1[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		counts2 = new int[len];
		for(int i = 0 ; i < counts2.length ; i++){
			counts2[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		counts3 = new int[len];
		for(int i = 0 ; i < counts3.length ; i++){
			counts3[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		funds = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		yuanbaoToContribution = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		yuanbaoToFunds = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x7000EF02;
	}

	public String getTypeDescription() {
		return "GANGWAREHOUSE_GET_RES";
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
		len += entityIds1.length * 8;
		len += 4;
		len += entityIds2.length * 8;
		len += 4;
		len += entityIds3.length * 8;
		len += 4;
		len += counts1.length * 4;
		len += 4;
		len += counts2.length * 4;
		len += 4;
		len += counts3.length * 4;
		len += 8;
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

			buffer.putInt(entityIds1.length);
			for(int i = 0 ; i < entityIds1.length; i++){
				buffer.putLong(entityIds1[i]);
			}
			buffer.putInt(entityIds2.length);
			for(int i = 0 ; i < entityIds2.length; i++){
				buffer.putLong(entityIds2[i]);
			}
			buffer.putInt(entityIds3.length);
			for(int i = 0 ; i < entityIds3.length; i++){
				buffer.putLong(entityIds3[i]);
			}
			buffer.putInt(counts1.length);
			for(int i = 0 ; i < counts1.length; i++){
				buffer.putInt(counts1[i]);
			}
			buffer.putInt(counts2.length);
			for(int i = 0 ; i < counts2.length; i++){
				buffer.putInt(counts2[i]);
			}
			buffer.putInt(counts3.length);
			for(int i = 0 ; i < counts3.length; i++){
				buffer.putInt(counts3[i]);
			}
			buffer.putLong(funds);
			buffer.putInt(yuanbaoToContribution);
			buffer.putInt(yuanbaoToFunds);
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
	 *	格子物品数组，值为-1的元素表示格子为空
	 */
	public long[] getEntityIds1(){
		return entityIds1;
	}

	/**
	 * 设置属性：
	 *	格子物品数组，值为-1的元素表示格子为空
	 */
	public void setEntityIds1(long[] entityIds1){
		this.entityIds1 = entityIds1;
	}

	/**
	 * 获取属性：
	 *	格子物品数组，值为-1的元素表示格子为空
	 */
	public long[] getEntityIds2(){
		return entityIds2;
	}

	/**
	 * 设置属性：
	 *	格子物品数组，值为-1的元素表示格子为空
	 */
	public void setEntityIds2(long[] entityIds2){
		this.entityIds2 = entityIds2;
	}

	/**
	 * 获取属性：
	 *	格子物品数组，值为-1的元素表示格子为空
	 */
	public long[] getEntityIds3(){
		return entityIds3;
	}

	/**
	 * 设置属性：
	 *	格子物品数组，值为-1的元素表示格子为空
	 */
	public void setEntityIds3(long[] entityIds3){
		this.entityIds3 = entityIds3;
	}

	/**
	 * 获取属性：
	 *	格子物品数量数组，值为0的元素表示格子为空
	 */
	public int[] getCounts1(){
		return counts1;
	}

	/**
	 * 设置属性：
	 *	格子物品数量数组，值为0的元素表示格子为空
	 */
	public void setCounts1(int[] counts1){
		this.counts1 = counts1;
	}

	/**
	 * 获取属性：
	 *	格子物品数量数组，值为0的元素表示格子为空
	 */
	public int[] getCounts2(){
		return counts2;
	}

	/**
	 * 设置属性：
	 *	格子物品数量数组，值为0的元素表示格子为空
	 */
	public void setCounts2(int[] counts2){
		this.counts2 = counts2;
	}

	/**
	 * 获取属性：
	 *	格子物品数量数组，值为0的元素表示格子为空
	 */
	public int[] getCounts3(){
		return counts3;
	}

	/**
	 * 设置属性：
	 *	格子物品数量数组，值为0的元素表示格子为空
	 */
	public void setCounts3(int[] counts3){
		this.counts3 = counts3;
	}

	/**
	 * 获取属性：
	 *	帮派资金
	 */
	public long getFunds(){
		return funds;
	}

	/**
	 * 设置属性：
	 *	帮派资金
	 */
	public void setFunds(long funds){
		this.funds = funds;
	}

	/**
	 * 获取属性：
	 *	元宝与贡献度的兑换比率
	 */
	public int getYuanbaoToContribution(){
		return yuanbaoToContribution;
	}

	/**
	 * 设置属性：
	 *	元宝与贡献度的兑换比率
	 */
	public void setYuanbaoToContribution(int yuanbaoToContribution){
		this.yuanbaoToContribution = yuanbaoToContribution;
	}

	/**
	 * 获取属性：
	 *	元宝与帮会资金的兑换比率
	 */
	public int getYuanbaoToFunds(){
		return yuanbaoToFunds;
	}

	/**
	 * 设置属性：
	 *	元宝与帮会资金的兑换比率
	 */
	public void setYuanbaoToFunds(int yuanbaoToFunds){
		this.yuanbaoToFunds = yuanbaoToFunds;
	}

}