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
 * <tr bgcolor="#FAFAFA" align="center"><td>articleId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bindIndexs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindIndexs</td><td>byte[]</td><td>bindIndexs.length</td><td>数组实际长度</td></tr>
 * </table>
 */
public class LIANQI_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long articleId;
	long materialId;
	byte[] bindIndexs;

	public LIANQI_REQ(){
	}

	public LIANQI_REQ(long seqNum,long articleId,long materialId,byte[] bindIndexs){
		this.seqNum = seqNum;
		this.articleId = articleId;
		this.materialId = materialId;
		this.bindIndexs = bindIndexs;
	}

	public LIANQI_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		articleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		materialId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bindIndexs = new byte[len];
		System.arraycopy(content,offset,bindIndexs,0,len);
		offset += len;
	}

	public int getType() {
		return 0x00EEAABB;
	}

	public String getTypeDescription() {
		return "LIANQI_REQ";
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
		len += 4;
		len += bindIndexs.length;
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

			buffer.putLong(articleId);
			buffer.putLong(materialId);
			buffer.putInt(bindIndexs.length);
			buffer.put(bindIndexs);
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
	 *	要查询物品id
	 */
	public long getArticleId(){
		return articleId;
	}

	/**
	 * 设置属性：
	 *	要查询物品id
	 */
	public void setArticleId(long articleId){
		this.articleId = articleId;
	}

	/**
	 * 获取属性：
	 *	炼器材料id
	 */
	public long getMaterialId(){
		return materialId;
	}

	/**
	 * 设置属性：
	 *	炼器材料id
	 */
	public void setMaterialId(long materialId){
		this.materialId = materialId;
	}

	/**
	 * 获取属性：
	 *	绑定index
	 */
	public byte[] getBindIndexs(){
		return bindIndexs;
	}

	/**
	 * 设置属性：
	 *	绑定index
	 */
	public void setBindIndexs(byte[] bindIndexs){
		this.bindIndexs = bindIndexs;
	}

}