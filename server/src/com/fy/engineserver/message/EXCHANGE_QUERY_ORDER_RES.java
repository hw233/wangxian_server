package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端查询用户的订单信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>start</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>num</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>totalNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prices.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prices</td><td>int[]</td><td>prices.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>amounts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>amounts</td><td>int[]</td><td>amounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>dealAmounts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>dealAmounts</td><td>int[]</td><td>dealAmounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderTypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderTypes</td><td>byte[]</td><td>orderTypes.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderStates.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderStates</td><td>byte[]</td><td>orderStates.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastModifiedDates.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastModifiedDates[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastModifiedDates[0]</td><td>String</td><td>lastModifiedDates[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastModifiedDates[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastModifiedDates[1]</td><td>String</td><td>lastModifiedDates[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lastModifiedDates[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lastModifiedDates[2]</td><td>String</td><td>lastModifiedDates[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class EXCHANGE_QUERY_ORDER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int start;
	int num;
	int totalNum;
	long[] ids;
	int[] prices;
	int[] amounts;
	int[] dealAmounts;
	byte[] orderTypes;
	byte[] orderStates;
	String[] lastModifiedDates;

	public EXCHANGE_QUERY_ORDER_RES(long seqNum,int start,int num,int totalNum,long[] ids,int[] prices,int[] amounts,int[] dealAmounts,byte[] orderTypes,byte[] orderStates,String[] lastModifiedDates){
		this.seqNum = seqNum;
		this.start = start;
		this.num = num;
		this.totalNum = totalNum;
		this.ids = ids;
		this.prices = prices;
		this.amounts = amounts;
		this.dealAmounts = dealAmounts;
		this.orderTypes = orderTypes;
		this.orderStates = orderStates;
		this.lastModifiedDates = lastModifiedDates;
	}

	public EXCHANGE_QUERY_ORDER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		start = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		num = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		totalNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
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
		prices = new int[len];
		for(int i = 0 ; i < prices.length ; i++){
			prices[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		amounts = new int[len];
		for(int i = 0 ; i < amounts.length ; i++){
			amounts[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		dealAmounts = new int[len];
		for(int i = 0 ; i < dealAmounts.length ; i++){
			dealAmounts[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		orderTypes = new byte[len];
		System.arraycopy(content,offset,orderTypes,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		orderStates = new byte[len];
		System.arraycopy(content,offset,orderStates,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lastModifiedDates = new String[len];
		for(int i = 0 ; i < lastModifiedDates.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			lastModifiedDates[i] = new String(content,offset,len);
		offset += len;
		}
	}

	public int getType() {
		return 0xF0F0EF13;
	}

	public String getTypeDescription() {
		return "EXCHANGE_QUERY_ORDER_RES";
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
		len += 4;
		len += 4;
		len += 4;
		len += ids.length * 8;
		len += 4;
		len += prices.length * 4;
		len += 4;
		len += amounts.length * 4;
		len += 4;
		len += dealAmounts.length * 4;
		len += 4;
		len += orderTypes.length;
		len += 4;
		len += orderStates.length;
		len += 4;
		for(int i = 0 ; i < lastModifiedDates.length; i++){
			len += 2;
			len += lastModifiedDates[i].getBytes().length;
		}
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

			buffer.putInt(start);
			buffer.putInt(num);
			buffer.putInt(totalNum);
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(prices.length);
			for(int i = 0 ; i < prices.length; i++){
				buffer.putInt(prices[i]);
			}
			buffer.putInt(amounts.length);
			for(int i = 0 ; i < amounts.length; i++){
				buffer.putInt(amounts[i]);
			}
			buffer.putInt(dealAmounts.length);
			for(int i = 0 ; i < dealAmounts.length; i++){
				buffer.putInt(dealAmounts[i]);
			}
			buffer.putInt(orderTypes.length);
			buffer.put(orderTypes);
			buffer.putInt(orderStates.length);
			buffer.put(orderStates);
			buffer.putInt(lastModifiedDates.length);
			for(int i = 0 ; i < lastModifiedDates.length; i++){
				byte[] tmpBytes2 = lastModifiedDates[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	开始的编号
	 */
	public int getStart(){
		return start;
	}

	/**
	 * 设置属性：
	 *	开始的编号
	 */
	public void setStart(int start){
		this.start = start;
	}

	/**
	 * 获取属性：
	 *	返回订单的数量
	 */
	public int getNum(){
		return num;
	}

	/**
	 * 设置属性：
	 *	返回订单的数量
	 */
	public void setNum(int num){
		this.num = num;
	}

	/**
	 * 获取属性：
	 *	所有订单的数量
	 */
	public int getTotalNum(){
		return totalNum;
	}

	/**
	 * 设置属性：
	 *	所有订单的数量
	 */
	public void setTotalNum(int totalNum){
		this.totalNum = totalNum;
	}

	/**
	 * 获取属性：
	 *	各个订单的Id
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	各个订单的Id
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	各个订单的单价
	 */
	public int[] getPrices(){
		return prices;
	}

	/**
	 * 设置属性：
	 *	各个订单的单价
	 */
	public void setPrices(int[] prices){
		this.prices = prices;
	}

	/**
	 * 获取属性：
	 *	各个订单的数量
	 */
	public int[] getAmounts(){
		return amounts;
	}

	/**
	 * 设置属性：
	 *	各个订单的数量
	 */
	public void setAmounts(int[] amounts){
		this.amounts = amounts;
	}

	/**
	 * 获取属性：
	 *	各个订单的已经成交的数量
	 */
	public int[] getDealAmounts(){
		return dealAmounts;
	}

	/**
	 * 设置属性：
	 *	各个订单的已经成交的数量
	 */
	public void setDealAmounts(int[] dealAmounts){
		this.dealAmounts = dealAmounts;
	}

	/**
	 * 获取属性：
	 *	各个订单的类型，0标识买单，1标识卖单
	 */
	public byte[] getOrderTypes(){
		return orderTypes;
	}

	/**
	 * 设置属性：
	 *	各个订单的类型，0标识买单，1标识卖单
	 */
	public void setOrderTypes(byte[] orderTypes){
		this.orderTypes = orderTypes;
	}

	/**
	 * 获取属性：
	 *	各个订单的状态，0标识激活，1标识取消，2标识部分成交，3标识全部成交
	 */
	public byte[] getOrderStates(){
		return orderStates;
	}

	/**
	 * 设置属性：
	 *	各个订单的状态，0标识激活，1标识取消，2标识部分成交，3标识全部成交
	 */
	public void setOrderStates(byte[] orderStates){
		this.orderStates = orderStates;
	}

	/**
	 * 获取属性：
	 *	各个订单最后修改时间，精确到天
	 */
	public String[] getLastModifiedDates(){
		return lastModifiedDates;
	}

	/**
	 * 设置属性：
	 *	各个订单最后修改时间，精确到天
	 */
	public void setLastModifiedDates(String[] lastModifiedDates){
		this.lastModifiedDates = lastModifiedDates;
	}

}
