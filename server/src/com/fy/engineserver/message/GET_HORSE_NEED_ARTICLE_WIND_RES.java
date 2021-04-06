package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取成长、升级技能需要物品描述窗口信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>windType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>desCription.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>desCription</td><td>String</td><td>desCription.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class GET_HORSE_NEED_ARTICLE_WIND_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long horseId;
	long articleId;
	int articleNum;
	byte windType;
	String desCription;

	public GET_HORSE_NEED_ARTICLE_WIND_RES(){
	}

	public GET_HORSE_NEED_ARTICLE_WIND_RES(long seqNum,long horseId,long articleId,int articleNum,byte windType,String desCription){
		this.seqNum = seqNum;
		this.horseId = horseId;
		this.articleId = articleId;
		this.articleNum = articleNum;
		this.windType = windType;
		this.desCription = desCription;
	}

	public GET_HORSE_NEED_ARTICLE_WIND_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		horseId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		articleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		articleNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		windType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		desCription = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x70F0EF64;
	}

	public String getTypeDescription() {
		return "GET_HORSE_NEED_ARTICLE_WIND_RES";
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
		len += 1;
		len += 2;
		try{
			len +=desCription.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putLong(horseId);
			buffer.putLong(articleId);
			buffer.putInt(articleNum);
			buffer.put(windType);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = desCription.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	坐骑id
	 */
	public long getHorseId(){
		return horseId;
	}

	/**
	 * 设置属性：
	 *	坐骑id
	 */
	public void setHorseId(long horseId){
		this.horseId = horseId;
	}

	/**
	 * 获取属性：
	 *	需要的物品id
	 */
	public long getArticleId(){
		return articleId;
	}

	/**
	 * 设置属性：
	 *	需要的物品id
	 */
	public void setArticleId(long articleId){
		this.articleId = articleId;
	}

	/**
	 * 获取属性：
	 *	需要的物品数量
	 */
	public int getArticleNum(){
		return articleNum;
	}

	/**
	 * 设置属性：
	 *	需要的物品数量
	 */
	public void setArticleNum(int articleNum){
		this.articleNum = articleNum;
	}

	/**
	 * 获取属性：
	 *	1为打开成长需要物品窗口   2为打开技能升级需要物品窗口
	 */
	public byte getWindType(){
		return windType;
	}

	/**
	 * 设置属性：
	 *	1为打开成长需要物品窗口   2为打开技能升级需要物品窗口
	 */
	public void setWindType(byte windType){
		this.windType = windType;
	}

	/**
	 * 获取属性：
	 *	描述
	 */
	public String getDesCription(){
		return desCription;
	}

	/**
	 * 设置属性：
	 *	描述
	 */
	public void setDesCription(String desCription){
		this.desCription = desCription;
	}

}