package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端发送指令查询交易所实时行情类别<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>money</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yuanbo</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>blockedMoney</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>blockedYuanbo</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>stampDuty</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buyingMarket.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buyingMarket</td><td>long[]</td><td>buyingMarket.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sellingMarket.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sellingMarket</td><td>long[]</td><td>sellingMarket.length</td><td>*</td></tr>
 * </table>
 */
public class EXCHANGE_QUERY_MARKET_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long money;
	long yuanbo;
	long blockedMoney;
	long blockedYuanbo;
	int stampDuty;
	long[] buyingMarket;
	long[] sellingMarket;

	public EXCHANGE_QUERY_MARKET_RES(long seqNum,long money,long yuanbo,long blockedMoney,long blockedYuanbo,int stampDuty,long[] buyingMarket,long[] sellingMarket){
		this.seqNum = seqNum;
		this.money = money;
		this.yuanbo = yuanbo;
		this.blockedMoney = blockedMoney;
		this.blockedYuanbo = blockedYuanbo;
		this.stampDuty = stampDuty;
		this.buyingMarket = buyingMarket;
		this.sellingMarket = sellingMarket;
	}

	public EXCHANGE_QUERY_MARKET_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		money = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		yuanbo = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		blockedMoney = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		blockedYuanbo = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		stampDuty = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		buyingMarket = new long[len];
		for(int i = 0 ; i < buyingMarket.length ; i++){
			buyingMarket[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		sellingMarket = new long[len];
		for(int i = 0 ; i < sellingMarket.length ; i++){
			sellingMarket[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0xF0F0EF10;
	}

	public String getTypeDescription() {
		return "EXCHANGE_QUERY_MARKET_RES";
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
		len += 8;
		len += 8;
		len += 4;
		len += 4;
		len += buyingMarket.length * 8;
		len += 4;
		len += sellingMarket.length * 8;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putLong(money);
			buffer.putLong(yuanbo);
			buffer.putLong(blockedMoney);
			buffer.putLong(blockedYuanbo);
			buffer.putInt(stampDuty);
			buffer.putInt(buyingMarket.length);
			for(int i = 0 ; i < buyingMarket.length; i++){
				buffer.putLong(buyingMarket[i]);
			}
			buffer.putInt(sellingMarket.length);
			for(int i = 0 ; i < sellingMarket.length; i++){
				buffer.putLong(sellingMarket[i]);
			}
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	交易所帐户中的金币数
	 */
	public long getMoney(){
		return money;
	}

	/**
	 * 设置属性：
	 *	交易所帐户中的金币数
	 */
	public void setMoney(long money){
		this.money = money;
	}

	/**
	 * 获取属性：
	 *	交易所帐户中的元宝数
	 */
	public long getYuanbo(){
		return yuanbo;
	}

	/**
	 * 设置属性：
	 *	交易所帐户中的元宝数
	 */
	public void setYuanbo(long yuanbo){
		this.yuanbo = yuanbo;
	}

	/**
	 * 获取属性：
	 *	交易所帐户中的冻结金币数
	 */
	public long getBlockedMoney(){
		return blockedMoney;
	}

	/**
	 * 设置属性：
	 *	交易所帐户中的冻结金币数
	 */
	public void setBlockedMoney(long blockedMoney){
		this.blockedMoney = blockedMoney;
	}

	/**
	 * 获取属性：
	 *	交易所帐户中的冻结元宝数
	 */
	public long getBlockedYuanbo(){
		return blockedYuanbo;
	}

	/**
	 * 设置属性：
	 *	交易所帐户中的冻结元宝数
	 */
	public void setBlockedYuanbo(long blockedYuanbo){
		this.blockedYuanbo = blockedYuanbo;
	}

	/**
	 * 获取属性：
	 *	印花税，卖方承担，基数为1000，10标识1%的印花税
	 */
	public int getStampDuty(){
		return stampDuty;
	}

	/**
	 * 设置属性：
	 *	印花税，卖方承担，基数为1000，10标识1%的印花税
	 */
	public void setStampDuty(int stampDuty){
		this.stampDuty = stampDuty;
	}

	/**
	 * 获取属性：
	 *	获得买方的行情，返回的是长度为num*2的数组，表达的是long[num][2]的二维数组，其中long[][0]标识单价，long[][1]标识此单价的数量，单价从最高价到最低价排序
	 */
	public long[] getBuyingMarket(){
		return buyingMarket;
	}

	/**
	 * 设置属性：
	 *	获得买方的行情，返回的是长度为num*2的数组，表达的是long[num][2]的二维数组，其中long[][0]标识单价，long[][1]标识此单价的数量，单价从最高价到最低价排序
	 */
	public void setBuyingMarket(long[] buyingMarket){
		this.buyingMarket = buyingMarket;
	}

	/**
	 * 获取属性：
	 *	获得卖方的行情，返回的是长度为num*2的数组，表达的是long[num][2]的二维数组，其中long[][0]标识单价，long[][1]标识此单价的数量，单价从最低价到最高价排序
	 */
	public long[] getSellingMarket(){
		return sellingMarket;
	}

	/**
	 * 设置属性：
	 *	获得卖方的行情，返回的是长度为num*2的数组，表达的是long[num][2]的二维数组，其中long[][0]标识单价，long[][1]标识此单价的数量，单价从最低价到最高价排序
	 */
	public void setSellingMarket(long[] sellingMarket){
		this.sellingMarket = sellingMarket;
	}

}
