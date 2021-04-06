package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端发送指令取出交易所中的钱,会收到响应，不成功，服务器会通过HINT_REQ指令提示错误，不发送响应<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>money</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yuanbo</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>blockedMoney</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>blockedYuanbo</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class EXCHANGE_TAKE_MONEY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long money;
	long yuanbo;
	long blockedMoney;
	long blockedYuanbo;

	public EXCHANGE_TAKE_MONEY_RES(long seqNum,long money,long yuanbo,long blockedMoney,long blockedYuanbo){
		this.seqNum = seqNum;
		this.money = money;
		this.yuanbo = yuanbo;
		this.blockedMoney = blockedMoney;
		this.blockedYuanbo = blockedYuanbo;
	}

	public EXCHANGE_TAKE_MONEY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		money = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		yuanbo = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		blockedMoney = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		blockedYuanbo = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0xF0F0EF12;
	}

	public String getTypeDescription() {
		return "EXCHANGE_TAKE_MONEY_RES";
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

}
