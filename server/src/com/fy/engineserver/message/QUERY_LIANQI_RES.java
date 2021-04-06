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
 * <tr bgcolor="#FFFFFF" align="center"><td>needArticle.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needArticle</td><td>String</td><td>needArticle.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>basicCost</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bindCosts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bindCosts</td><td>long[]</td><td>bindCosts.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_LIANQI_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long articleId;
	String needArticle;
	long basicCost;
	long[] bindCosts;

	public QUERY_LIANQI_RES(){
	}

	public QUERY_LIANQI_RES(long seqNum,long articleId,String needArticle,long basicCost,long[] bindCosts){
		this.seqNum = seqNum;
		this.articleId = articleId;
		this.needArticle = needArticle;
		this.basicCost = basicCost;
		this.bindCosts = bindCosts;
	}

	public QUERY_LIANQI_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		articleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		needArticle = new String(content,offset,len,"UTF-8");
		offset += len;
		basicCost = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		bindCosts = new long[len];
		for(int i = 0 ; i < bindCosts.length ; i++){
			bindCosts[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70EEAABA;
	}

	public String getTypeDescription() {
		return "QUERY_LIANQI_RES";
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
		len += 2;
		try{
			len +=needArticle.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 4;
		len += bindCosts.length * 8;
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
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = needArticle.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(basicCost);
			buffer.putInt(bindCosts.length);
			for(int i = 0 ; i < bindCosts.length; i++){
				buffer.putLong(bindCosts[i]);
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
	 *	炼器所需物品
	 */
	public String getNeedArticle(){
		return needArticle;
	}

	/**
	 * 设置属性：
	 *	炼器所需物品
	 */
	public void setNeedArticle(String needArticle){
		this.needArticle = needArticle;
	}

	/**
	 * 获取属性：
	 *	炼器基本花费
	 */
	public long getBasicCost(){
		return basicCost;
	}

	/**
	 * 设置属性：
	 *	炼器基本花费
	 */
	public void setBasicCost(long basicCost){
		this.basicCost = basicCost;
	}

	/**
	 * 获取属性：
	 *	炼器锁定属性槽的花费，数组表示锁定一个花费多少银子，锁定两个花费多少银子，锁定三个花费多少银子
	 */
	public long[] getBindCosts(){
		return bindCosts;
	}

	/**
	 * 设置属性：
	 *	炼器锁定属性槽的花费，数组表示锁定一个花费多少银子，锁定两个花费多少银子，锁定三个花费多少银子
	 */
	public void setBindCosts(long[] bindCosts){
		this.bindCosts = bindCosts;
	}

}