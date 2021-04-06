package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mainArticleId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialArticleId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mainBagType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialBagType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tunshiType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class TUNSHI_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long mainArticleId;
	long materialArticleId;
	byte mainBagType;
	byte materialBagType;
	byte tunshiType;

	public TUNSHI_REQ(){
	}

	public TUNSHI_REQ(long seqNum,long mainArticleId,long materialArticleId,byte mainBagType,byte materialBagType,byte tunshiType){
		this.seqNum = seqNum;
		this.mainArticleId = mainArticleId;
		this.materialArticleId = materialArticleId;
		this.mainBagType = mainBagType;
		this.materialBagType = materialBagType;
		this.tunshiType = tunshiType;
	}

	public TUNSHI_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		mainArticleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		materialArticleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		mainBagType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		materialBagType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		tunshiType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x00EEEABB;
	}

	public String getTypeDescription() {
		return "TUNSHI_REQ";
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
		len += 1;
		len += 1;
		len += 1;
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

			buffer.putLong(mainArticleId);
			buffer.putLong(materialArticleId);
			buffer.put(mainBagType);
			buffer.put(materialBagType);
			buffer.put(tunshiType);
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
	 *	主要器灵id
	 */
	public long getMainArticleId(){
		return mainArticleId;
	}

	/**
	 * 设置属性：
	 *	主要器灵id
	 */
	public void setMainArticleId(long mainArticleId){
		this.mainArticleId = mainArticleId;
	}

	/**
	 * 获取属性：
	 *	辅助器灵id
	 */
	public long getMaterialArticleId(){
		return materialArticleId;
	}

	/**
	 * 设置属性：
	 *	辅助器灵id
	 */
	public void setMaterialArticleId(long materialArticleId){
		this.materialArticleId = materialArticleId;
	}

	/**
	 * 获取属性：
	 *	主要器灵的背包类型，即在哪个背包
	 */
	public byte getMainBagType(){
		return mainBagType;
	}

	/**
	 * 设置属性：
	 *	主要器灵的背包类型，即在哪个背包
	 */
	public void setMainBagType(byte mainBagType){
		this.mainBagType = mainBagType;
	}

	/**
	 * 获取属性：
	 *	辅助器灵的背包类型，即在哪个背包
	 */
	public byte getMaterialBagType(){
		return materialBagType;
	}

	/**
	 * 设置属性：
	 *	辅助器灵的背包类型，即在哪个背包
	 */
	public void setMaterialBagType(byte materialBagType){
		this.materialBagType = materialBagType;
	}

	/**
	 * 获取属性：
	 *	0单独吞噬，1全部吞噬
	 */
	public byte getTunshiType(){
		return tunshiType;
	}

	/**
	 * 设置属性：
	 *	0单独吞噬，1全部吞噬
	 */
	public void setTunshiType(byte tunshiType){
		this.tunshiType = tunshiType;
	}

}