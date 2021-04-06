package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求Q币与游戏的兑换项目列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>qBiAmount.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>qBiAmount</td><td>int[]</td><td>qBiAmount.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yuanbao.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yuanbao</td><td>int[]</td><td>yuanbao.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>userQbiAmount</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class TAIWAN_QUERY_EXCHANGE_ITEM_LIST_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] qBiAmount;
	int[] yuanbao;
	long userQbiAmount;

	public TAIWAN_QUERY_EXCHANGE_ITEM_LIST_RES(long seqNum,int[] qBiAmount,int[] yuanbao,long userQbiAmount){
		this.seqNum = seqNum;
		this.qBiAmount = qBiAmount;
		this.yuanbao = yuanbao;
		this.userQbiAmount = userQbiAmount;
	}

	public TAIWAN_QUERY_EXCHANGE_ITEM_LIST_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		qBiAmount = new int[len];
		for(int i = 0 ; i < qBiAmount.length ; i++){
			qBiAmount[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		yuanbao = new int[len];
		for(int i = 0 ; i < yuanbao.length ; i++){
			yuanbao[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		userQbiAmount = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x8002A006;
	}

	public String getTypeDescription() {
		return "TAIWAN_QUERY_EXCHANGE_ITEM_LIST_RES";
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
		len += qBiAmount.length * 4;
		len += 4;
		len += yuanbao.length * 4;
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

			buffer.putInt(qBiAmount.length);
			for(int i = 0 ; i < qBiAmount.length; i++){
				buffer.putInt(qBiAmount[i]);
			}
			buffer.putInt(yuanbao.length);
			for(int i = 0 ; i < yuanbao.length; i++){
				buffer.putInt(yuanbao[i]);
			}
			buffer.putLong(userQbiAmount);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	Q币金额
	 */
	public int[] getQBiAmount(){
		return qBiAmount;
	}

	/**
	 * 设置属性：
	 *	Q币金额
	 */
	public void setQBiAmount(int[] qBiAmount){
		this.qBiAmount = qBiAmount;
	}

	/**
	 * 获取属性：
	 *	可兑换的元宝数量
	 */
	public int[] getYuanbao(){
		return yuanbao;
	}

	/**
	 * 设置属性：
	 *	可兑换的元宝数量
	 */
	public void setYuanbao(int[] yuanbao){
		this.yuanbao = yuanbao;
	}

	/**
	 * 获取属性：
	 *	玩家的Q币余额
	 */
	public long getUserQbiAmount(){
		return userQbiAmount;
	}

	/**
	 * 设置属性：
	 *	玩家的Q币余额
	 */
	public void setUserQbiAmount(long userQbiAmount){
		this.userQbiAmount = userQbiAmount;
	}

}
