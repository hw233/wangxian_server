package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 兽魂抽奖<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>takeType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleId0</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleName0.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleName0</td><td>String</td><td>articleName0.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleColor0</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNum0</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleId1.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleId1</td><td>long[]</td><td>articleId1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleName1.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleName1[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleName1[0]</td><td>String</td><td>articleName1[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleName1[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleName1[1]</td><td>String</td><td>articleName1[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleName1[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleName1[2]</td><td>String</td><td>articleName1[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleColor1.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleColor1</td><td>int[]</td><td>articleColor1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNum1.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleNum1</td><td>int[]</td><td>articleNum1.length</td><td>*</td></tr>
 * </table>
 */
public class TAKE_SHOUHUN_LUCK_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int takeType;
	long articleId0;
	String articleName0;
	int articleColor0;
	int articleNum0;
	long[] articleId1;
	String[] articleName1;
	int[] articleColor1;
	int[] articleNum1;

	public TAKE_SHOUHUN_LUCK_RES(){
	}

	public TAKE_SHOUHUN_LUCK_RES(long seqNum,int takeType,long articleId0,String articleName0,int articleColor0,int articleNum0,long[] articleId1,String[] articleName1,int[] articleColor1,int[] articleNum1){
		this.seqNum = seqNum;
		this.takeType = takeType;
		this.articleId0 = articleId0;
		this.articleName0 = articleName0;
		this.articleColor0 = articleColor0;
		this.articleNum0 = articleNum0;
		this.articleId1 = articleId1;
		this.articleName1 = articleName1;
		this.articleColor1 = articleColor1;
		this.articleNum1 = articleNum1;
	}

	public TAKE_SHOUHUN_LUCK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		takeType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		articleId0 = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		articleName0 = new String(content,offset,len);
		offset += len;
		articleColor0 = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		articleNum0 = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleId1 = new long[len];
		for(int i = 0 ; i < articleId1.length ; i++){
			articleId1[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleName1 = new String[len];
		for(int i = 0 ; i < articleName1.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			articleName1[i] = new String(content,offset,len);
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleColor1 = new int[len];
		for(int i = 0 ; i < articleColor1.length ; i++){
			articleColor1[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleNum1 = new int[len];
		for(int i = 0 ; i < articleNum1.length ; i++){
			articleNum1[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0108;
	}

	public String getTypeDescription() {
		return "TAKE_SHOUHUN_LUCK_RES";
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
		len += 8;
		len += 2;
		len +=articleName0.getBytes().length;
		len += 4;
		len += 4;
		len += 4;
		len += articleId1.length * 8;
		len += 4;
		for(int i = 0 ; i < articleName1.length; i++){
			len += 2;
			len += articleName1[i].getBytes().length;
		}
		len += 4;
		len += articleColor1.length * 4;
		len += 4;
		len += articleNum1.length * 4;
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

			buffer.putInt(takeType);
			buffer.putLong(articleId0);
			byte[] tmpBytes1;
			tmpBytes1 = articleName0.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(articleColor0);
			buffer.putInt(articleNum0);
			buffer.putInt(articleId1.length);
			for(int i = 0 ; i < articleId1.length; i++){
				buffer.putLong(articleId1[i]);
			}
			buffer.putInt(articleName1.length);
			for(int i = 0 ; i < articleName1.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = articleName1[i].getBytes();
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(articleColor1.length);
			for(int i = 0 ; i < articleColor1.length; i++){
				buffer.putInt(articleColor1[i]);
			}
			buffer.putInt(articleNum1.length);
			for(int i = 0 ; i < articleNum1.length; i++){
				buffer.putInt(articleNum1[i]);
			}
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
	 *	抽奖类型（0单抽  1十连抽）
	 */
	public int getTakeType(){
		return takeType;
	}

	/**
	 * 设置属性：
	 *	抽奖类型（0单抽  1十连抽）
	 */
	public void setTakeType(int takeType){
		this.takeType = takeType;
	}

	/**
	 * 获取属性：
	 *	赠送物品id
	 */
	public long getArticleId0(){
		return articleId0;
	}

	/**
	 * 设置属性：
	 *	赠送物品id
	 */
	public void setArticleId0(long articleId0){
		this.articleId0 = articleId0;
	}

	/**
	 * 获取属性：
	 *	赠送物品名
	 */
	public String getArticleName0(){
		return articleName0;
	}

	/**
	 * 设置属性：
	 *	赠送物品名
	 */
	public void setArticleName0(String articleName0){
		this.articleName0 = articleName0;
	}

	/**
	 * 获取属性：
	 *	赠送物品颜色
	 */
	public int getArticleColor0(){
		return articleColor0;
	}

	/**
	 * 设置属性：
	 *	赠送物品颜色
	 */
	public void setArticleColor0(int articleColor0){
		this.articleColor0 = articleColor0;
	}

	/**
	 * 获取属性：
	 *	赠送物品数量
	 */
	public int getArticleNum0(){
		return articleNum0;
	}

	/**
	 * 设置属性：
	 *	赠送物品数量
	 */
	public void setArticleNum0(int articleNum0){
		this.articleNum0 = articleNum0;
	}

	/**
	 * 获取属性：
	 *	抽取物品id
	 */
	public long[] getArticleId1(){
		return articleId1;
	}

	/**
	 * 设置属性：
	 *	抽取物品id
	 */
	public void setArticleId1(long[] articleId1){
		this.articleId1 = articleId1;
	}

	/**
	 * 获取属性：
	 *	抽取物品名
	 */
	public String[] getArticleName1(){
		return articleName1;
	}

	/**
	 * 设置属性：
	 *	抽取物品名
	 */
	public void setArticleName1(String[] articleName1){
		this.articleName1 = articleName1;
	}

	/**
	 * 获取属性：
	 *	抽取物品颜色
	 */
	public int[] getArticleColor1(){
		return articleColor1;
	}

	/**
	 * 设置属性：
	 *	抽取物品颜色
	 */
	public void setArticleColor1(int[] articleColor1){
		this.articleColor1 = articleColor1;
	}

	/**
	 * 获取属性：
	 *	抽取物品数量
	 */
	public int[] getArticleNum1(){
		return articleNum1;
	}

	/**
	 * 设置属性：
	 *	抽取物品数量
	 */
	public void setArticleNum1(int[] articleNum1){
		this.articleNum1 = articleNum1;
	}

}