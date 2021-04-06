package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 使用瓶子后查看瓶子中的信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleIds</td><td>long[]</td><td>articleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNums</td><td>int[]</td><td>articleNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selectArticleIndexs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selectArticleIndexs</td><td>int[]</td><td>selectArticleIndexs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>openFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class BOTTLE_OPEN_INFO_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long articleId;
	long[] articleIds;
	int[] articleNums;
	int[] selectArticleIndexs;
	boolean openFlag;

	public BOTTLE_OPEN_INFO_REQ(){
	}

	public BOTTLE_OPEN_INFO_REQ(long seqNum,long articleId,long[] articleIds,int[] articleNums,int[] selectArticleIndexs,boolean openFlag){
		this.seqNum = seqNum;
		this.articleId = articleId;
		this.articleIds = articleIds;
		this.articleNums = articleNums;
		this.selectArticleIndexs = selectArticleIndexs;
		this.openFlag = openFlag;
	}

	public BOTTLE_OPEN_INFO_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		articleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
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
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		selectArticleIndexs = new int[len];
		for(int i = 0 ; i < selectArticleIndexs.length ; i++){
			selectArticleIndexs[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		openFlag = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x0F300010;
	}

	public String getTypeDescription() {
		return "BOTTLE_OPEN_INFO_REQ";
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
		len += 4;
		len += articleIds.length * 8;
		len += 4;
		len += articleNums.length * 4;
		len += 4;
		len += selectArticleIndexs.length * 4;
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

			buffer.putLong(articleId);
			buffer.putInt(articleIds.length);
			for(int i = 0 ; i < articleIds.length; i++){
				buffer.putLong(articleIds[i]);
			}
			buffer.putInt(articleNums.length);
			for(int i = 0 ; i < articleNums.length; i++){
				buffer.putInt(articleNums[i]);
			}
			buffer.putInt(selectArticleIndexs.length);
			for(int i = 0 ; i < selectArticleIndexs.length; i++){
				buffer.putInt(selectArticleIndexs[i]);
			}
			buffer.put((byte)(openFlag==false?0:1));
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
	 *	瓶子id
	 */
	public long getArticleId(){
		return articleId;
	}

	/**
	 * 设置属性：
	 *	瓶子id
	 */
	public void setArticleId(long articleId){
		this.articleId = articleId;
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
	 *	瓶子中选中的物品的序号
	 */
	public int[] getSelectArticleIndexs(){
		return selectArticleIndexs;
	}

	/**
	 * 设置属性：
	 *	瓶子中选中的物品的序号
	 */
	public void setSelectArticleIndexs(int[] selectArticleIndexs){
		this.selectArticleIndexs = selectArticleIndexs;
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

}