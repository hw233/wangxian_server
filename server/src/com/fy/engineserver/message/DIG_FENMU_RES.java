package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 挖坟<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fenmuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fenmuName</td><td>String</td><td>fenmuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fenmuIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fendiIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>receiveType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>useChanziType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftYinChanzi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>leftJinChanzi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftLiuliChanzi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class DIG_FENMU_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String fenmuName;
	int fenmuIndex;
	int fendiIndex;
	byte receiveType;
	long articleId;
	byte useChanziType;
	int leftYinChanzi;
	int leftJinChanzi;
	int leftLiuliChanzi;

	public DIG_FENMU_RES(){
	}

	public DIG_FENMU_RES(long seqNum,String fenmuName,int fenmuIndex,int fendiIndex,byte receiveType,long articleId,byte useChanziType,int leftYinChanzi,int leftJinChanzi,int leftLiuliChanzi){
		this.seqNum = seqNum;
		this.fenmuName = fenmuName;
		this.fenmuIndex = fenmuIndex;
		this.fendiIndex = fendiIndex;
		this.receiveType = receiveType;
		this.articleId = articleId;
		this.useChanziType = useChanziType;
		this.leftYinChanzi = leftYinChanzi;
		this.leftJinChanzi = leftJinChanzi;
		this.leftLiuliChanzi = leftLiuliChanzi;
	}

	public DIG_FENMU_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		fenmuName = new String(content,offset,len,"UTF-8");
		offset += len;
		fenmuIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		fendiIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		receiveType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		articleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		useChanziType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		leftYinChanzi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		leftJinChanzi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		leftLiuliChanzi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F0EF80;
	}

	public String getTypeDescription() {
		return "DIG_FENMU_RES";
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
			len +=fenmuName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 1;
		len += 8;
		len += 1;
		len += 4;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = fenmuName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(fenmuIndex);
			buffer.putInt(fendiIndex);
			buffer.put(receiveType);
			buffer.putLong(articleId);
			buffer.put(useChanziType);
			buffer.putInt(leftYinChanzi);
			buffer.putInt(leftJinChanzi);
			buffer.putInt(leftLiuliChanzi);
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
	 *	坟墓名
	 */
	public String getFenmuName(){
		return fenmuName;
	}

	/**
	 * 设置属性：
	 *	坟墓名
	 */
	public void setFenmuName(String fenmuName){
		this.fenmuName = fenmuName;
	}

	/**
	 * 获取属性：
	 *	坟墓索引id
	 */
	public int getFenmuIndex(){
		return fenmuIndex;
	}

	/**
	 * 设置属性：
	 *	坟墓索引id
	 */
	public void setFenmuIndex(int fenmuIndex){
		this.fenmuIndex = fenmuIndex;
	}

	/**
	 * 获取属性：
	 *	坟地索引
	 */
	public int getFendiIndex(){
		return fendiIndex;
	}

	/**
	 * 设置属性：
	 *	坟地索引
	 */
	public void setFendiIndex(int fendiIndex){
		this.fendiIndex = fendiIndex;
	}

	/**
	 * 获取属性：
	 *	领取状态
	 */
	public byte getReceiveType(){
		return receiveType;
	}

	/**
	 * 设置属性：
	 *	领取状态
	 */
	public void setReceiveType(byte receiveType){
		this.receiveType = receiveType;
	}

	/**
	 * 获取属性：
	 *	挖取的物品id
	 */
	public long getArticleId(){
		return articleId;
	}

	/**
	 * 设置属性：
	 *	挖取的物品id
	 */
	public void setArticleId(long articleId){
		this.articleId = articleId;
	}

	/**
	 * 获取属性：
	 *	使用的铲子类型
	 */
	public byte getUseChanziType(){
		return useChanziType;
	}

	/**
	 * 设置属性：
	 *	使用的铲子类型
	 */
	public void setUseChanziType(byte useChanziType){
		this.useChanziType = useChanziType;
	}

	/**
	 * 获取属性：
	 *	银铲子剩余
	 */
	public int getLeftYinChanzi(){
		return leftYinChanzi;
	}

	/**
	 * 设置属性：
	 *	银铲子剩余
	 */
	public void setLeftYinChanzi(int leftYinChanzi){
		this.leftYinChanzi = leftYinChanzi;
	}

	/**
	 * 获取属性：
	 *	金铲子剩余
	 */
	public int getLeftJinChanzi(){
		return leftJinChanzi;
	}

	/**
	 * 设置属性：
	 *	金铲子剩余
	 */
	public void setLeftJinChanzi(int leftJinChanzi){
		this.leftJinChanzi = leftJinChanzi;
	}

	/**
	 * 获取属性：
	 *	琉璃铲子剩余
	 */
	public int getLeftLiuliChanzi(){
		return leftLiuliChanzi;
	}

	/**
	 * 设置属性：
	 *	琉璃铲子剩余
	 */
	public void setLeftLiuliChanzi(int leftLiuliChanzi){
		this.leftLiuliChanzi = leftLiuliChanzi;
	}

}