package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求灵髓宝石属性（等级、经验等）<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleExp</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propTypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propTypes</td><td>int[]</td><td>propTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propNums</td><td>int[]</td><td>propNums.length</td><td>*</td></tr>
 * </table>
 */
public class SOUL_ARTICLE_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long articleId;
	int articleLevel;
	long articleExp;
	int[] propTypes;
	int[] propNums;

	public SOUL_ARTICLE_INFO_RES(){
	}

	public SOUL_ARTICLE_INFO_RES(long seqNum,long articleId,int articleLevel,long articleExp,int[] propTypes,int[] propNums){
		this.seqNum = seqNum;
		this.articleId = articleId;
		this.articleLevel = articleLevel;
		this.articleExp = articleExp;
		this.propTypes = propTypes;
		this.propNums = propNums;
	}

	public SOUL_ARTICLE_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		articleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		articleLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		articleExp = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propTypes = new int[len];
		for(int i = 0 ; i < propTypes.length ; i++){
			propTypes[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propNums = new int[len];
		for(int i = 0 ; i < propNums.length ; i++){
			propNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FFF023;
	}

	public String getTypeDescription() {
		return "SOUL_ARTICLE_INFO_RES";
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
		len += 8;
		len += 4;
		len += propTypes.length * 4;
		len += 4;
		len += propNums.length * 4;
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
			buffer.putInt(articleLevel);
			buffer.putLong(articleExp);
			buffer.putInt(propTypes.length);
			for(int i = 0 ; i < propTypes.length; i++){
				buffer.putInt(propTypes[i]);
			}
			buffer.putInt(propNums.length);
			for(int i = 0 ; i < propNums.length; i++){
				buffer.putInt(propNums[i]);
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
	 *	灵髓宝石id
	 */
	public long getArticleId(){
		return articleId;
	}

	/**
	 * 设置属性：
	 *	灵髓宝石id
	 */
	public void setArticleId(long articleId){
		this.articleId = articleId;
	}

	/**
	 * 获取属性：
	 *	灵髓宝石等级·
	 */
	public int getArticleLevel(){
		return articleLevel;
	}

	/**
	 * 设置属性：
	 *	灵髓宝石等级·
	 */
	public void setArticleLevel(int articleLevel){
		this.articleLevel = articleLevel;
	}

	/**
	 * 获取属性：
	 *	灵髓宝石当前经验
	 */
	public long getArticleExp(){
		return articleExp;
	}

	/**
	 * 设置属性：
	 *	灵髓宝石当前经验
	 */
	public void setArticleExp(long articleExp){
		this.articleExp = articleExp;
	}

	/**
	 * 获取属性：
	 *	属性类型
	 */
	public int[] getPropTypes(){
		return propTypes;
	}

	/**
	 * 设置属性：
	 *	属性类型
	 */
	public void setPropTypes(int[] propTypes){
		this.propTypes = propTypes;
	}

	/**
	 * 获取属性：
	 *	对应值
	 */
	public int[] getPropNums(){
		return propNums;
	}

	/**
	 * 设置属性：
	 *	对应值
	 */
	public void setPropNums(int[] propNums){
		this.propNums = propNums;
	}

}