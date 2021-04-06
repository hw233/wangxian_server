package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 祈福开始或重新祈福<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>openedArticleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>openedArticleIds</td><td>long[]</td><td>openedArticleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>openedArticleNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>openedArticleNums</td><td>int[]</td><td>openedArticleNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hasFreeCliffordNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lookOverArticleIndexs.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lookOverArticleIndexs</td><td>int[]</td><td>lookOverArticleIndexs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>needArticleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needArticleName</td><td>String</td><td>needArticleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>haveArticleCount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canCliffordNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CLIFFORD_START_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] openedArticleIds;
	int[] openedArticleNums;
	int hasFreeCliffordNum;
	int[] lookOverArticleIndexs;
	String needArticleName;
	int haveArticleCount;
	int canCliffordNum;

	public CLIFFORD_START_RES(){
	}

	public CLIFFORD_START_RES(long seqNum,long[] openedArticleIds,int[] openedArticleNums,int hasFreeCliffordNum,int[] lookOverArticleIndexs,String needArticleName,int haveArticleCount,int canCliffordNum){
		this.seqNum = seqNum;
		this.openedArticleIds = openedArticleIds;
		this.openedArticleNums = openedArticleNums;
		this.hasFreeCliffordNum = hasFreeCliffordNum;
		this.lookOverArticleIndexs = lookOverArticleIndexs;
		this.needArticleName = needArticleName;
		this.haveArticleCount = haveArticleCount;
		this.canCliffordNum = canCliffordNum;
	}

	public CLIFFORD_START_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		openedArticleIds = new long[len];
		for(int i = 0 ; i < openedArticleIds.length ; i++){
			openedArticleIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		openedArticleNums = new int[len];
		for(int i = 0 ; i < openedArticleNums.length ; i++){
			openedArticleNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		hasFreeCliffordNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lookOverArticleIndexs = new int[len];
		for(int i = 0 ; i < lookOverArticleIndexs.length ; i++){
			lookOverArticleIndexs[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		needArticleName = new String(content,offset,len,"UTF-8");
		offset += len;
		haveArticleCount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		canCliffordNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x8F300016;
	}

	public String getTypeDescription() {
		return "CLIFFORD_START_RES";
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
		len += openedArticleIds.length * 8;
		len += 4;
		len += openedArticleNums.length * 4;
		len += 4;
		len += 4;
		len += lookOverArticleIndexs.length * 4;
		len += 2;
		try{
			len +=needArticleName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putInt(openedArticleIds.length);
			for(int i = 0 ; i < openedArticleIds.length; i++){
				buffer.putLong(openedArticleIds[i]);
			}
			buffer.putInt(openedArticleNums.length);
			for(int i = 0 ; i < openedArticleNums.length; i++){
				buffer.putInt(openedArticleNums[i]);
			}
			buffer.putInt(hasFreeCliffordNum);
			buffer.putInt(lookOverArticleIndexs.length);
			for(int i = 0 ; i < lookOverArticleIndexs.length; i++){
				buffer.putInt(lookOverArticleIndexs[i]);
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = needArticleName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(haveArticleCount);
			buffer.putInt(canCliffordNum);
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
	 *	祈福中的物品id，-1表示这个
	 */
	public long[] getOpenedArticleIds(){
		return openedArticleIds;
	}

	/**
	 * 设置属性：
	 *	祈福中的物品id，-1表示这个
	 */
	public void setOpenedArticleIds(long[] openedArticleIds){
		this.openedArticleIds = openedArticleIds;
	}

	/**
	 * 获取属性：
	 *	祈福中的物品数量
	 */
	public int[] getOpenedArticleNums(){
		return openedArticleNums;
	}

	/**
	 * 设置属性：
	 *	祈福中的物品数量
	 */
	public void setOpenedArticleNums(int[] openedArticleNums){
		this.openedArticleNums = openedArticleNums;
	}

	/**
	 * 获取属性：
	 *	免费祈福次数
	 */
	public int getHasFreeCliffordNum(){
		return hasFreeCliffordNum;
	}

	/**
	 * 设置属性：
	 *	免费祈福次数
	 */
	public void setHasFreeCliffordNum(int hasFreeCliffordNum){
		this.hasFreeCliffordNum = hasFreeCliffordNum;
	}

	/**
	 * 获取属性：
	 *	祈福查看后，查看到的物品位置
	 */
	public int[] getLookOverArticleIndexs(){
		return lookOverArticleIndexs;
	}

	/**
	 * 设置属性：
	 *	祈福查看后，查看到的物品位置
	 */
	public void setLookOverArticleIndexs(int[] lookOverArticleIndexs){
		this.lookOverArticleIndexs = lookOverArticleIndexs;
	}

	/**
	 * 获取属性：
	 *	祈福需要物品   名
	 */
	public String getNeedArticleName(){
		return needArticleName;
	}

	/**
	 * 设置属性：
	 *	祈福需要物品   名
	 */
	public void setNeedArticleName(String needArticleName){
		this.needArticleName = needArticleName;
	}

	/**
	 * 获取属性：
	 *	祈福需要物品   拥有个数  
	 */
	public int getHaveArticleCount(){
		return haveArticleCount;
	}

	/**
	 * 设置属性：
	 *	祈福需要物品   拥有个数  
	 */
	public void setHaveArticleCount(int haveArticleCount){
		this.haveArticleCount = haveArticleCount;
	}

	/**
	 * 获取属性：
	 *	可以祈福次数
	 */
	public int getCanCliffordNum(){
		return canCliffordNum;
	}

	/**
	 * 设置属性：
	 *	可以祈福次数
	 */
	public void setCanCliffordNum(int canCliffordNum){
		this.canCliffordNum = canCliffordNum;
	}

}