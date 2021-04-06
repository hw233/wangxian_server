package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 开启瓶子<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleIds</td><td>long[]</td><td>articleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNums</td><td>int[]</td><td>articleNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>openFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>openCount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class BOTTLE_OPEN_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] articleIds;
	int[] articleNums;
	boolean openFlag;
	int openCount;

	public BOTTLE_OPEN_RES(long seqNum,long[] articleIds,int[] articleNums,boolean openFlag,int openCount){
		this.seqNum = seqNum;
		this.articleIds = articleIds;
		this.articleNums = articleNums;
		this.openFlag = openFlag;
		this.openCount = openCount;
	}

	public BOTTLE_OPEN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleIds = new long[len];
		for(int i = 0 ; i < articleIds.length ; i++){
			articleIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleNums = new int[len];
		for(int i = 0 ; i < articleNums.length ; i++){
			articleNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		openFlag = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		openCount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x8F300012;
	}

	public String getTypeDescription() {
		return "BOTTLE_OPEN_RES";
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
		len += articleIds.length * 8;
		len += 4;
		len += articleNums.length * 4;
		len += 1;
		len += 4;
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

			buffer.putInt(articleIds.length);
			for(int i = 0 ; i < articleIds.length; i++){
				buffer.putLong(articleIds[i]);
			}
			buffer.putInt(articleNums.length);
			for(int i = 0 ; i < articleNums.length; i++){
				buffer.putInt(articleNums[i]);
			}
			buffer.put((byte)(openFlag==false?0:1));
			buffer.putInt(openCount);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	瓶子中物品的临时id
	 */
	public long[] getArticleIds(){
		return articleIds;
	}

	/**
	 * 设置属性：
	 *	瓶子中物品的临时id
	 */
	public void setArticleIds(long[] articleIds){
		this.articleIds = articleIds;
	}

	/**
	 * 获取属性：
	 *	瓶子中物品的数量
	 */
	public int[] getArticleNums(){
		return articleNums;
	}

	/**
	 * 设置属性：
	 *	瓶子中物品的数量
	 */
	public void setArticleNums(int[] articleNums){
		this.articleNums = articleNums;
	}

	/**
	 * 获取属性：
	 *	瓶子是否开启
	 */
	public boolean getOpenFlag(){
		return openFlag;
	}

	/**
	 * 设置属性：
	 *	瓶子是否开启
	 */
	public void setOpenFlag(boolean openFlag){
		this.openFlag = openFlag;
	}

	/**
	 * 获取属性：
	 *	可以开启的瓶子数量
	 */
	public int getOpenCount(){
		return openCount;
	}

	/**
	 * 设置属性：
	 *	可以开启的瓶子数量
	 */
	public void setOpenCount(int openCount){
		this.openCount = openCount;
	}

}
