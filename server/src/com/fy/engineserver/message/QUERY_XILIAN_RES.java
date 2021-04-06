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
 * <tr bgcolor="#FFFFFF" align="center"><td>qilingType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currentPropValue.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentPropValue</td><td>String</td><td>currentPropValue.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propValueRange.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propValueRange</td><td>String</td><td>propValueRange.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>expXiLianDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>expXiLianDes</td><td>String</td><td>expXiLianDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yinziXiLianDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yinziXiLianDes</td><td>String</td><td>yinziXiLianDes.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class QUERY_XILIAN_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long articleId;
	int qilingType;
	String currentPropValue;
	String propValueRange;
	String expXiLianDes;
	String yinziXiLianDes;

	public QUERY_XILIAN_RES(){
	}

	public QUERY_XILIAN_RES(long seqNum,long articleId,int qilingType,String currentPropValue,String propValueRange,String expXiLianDes,String yinziXiLianDes){
		this.seqNum = seqNum;
		this.articleId = articleId;
		this.qilingType = qilingType;
		this.currentPropValue = currentPropValue;
		this.propValueRange = propValueRange;
		this.expXiLianDes = expXiLianDes;
		this.yinziXiLianDes = yinziXiLianDes;
	}

	public QUERY_XILIAN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		articleId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		qilingType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		currentPropValue = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		propValueRange = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		expXiLianDes = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		yinziXiLianDes = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x70EEEABC;
	}

	public String getTypeDescription() {
		return "QUERY_XILIAN_RES";
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
		len += 2;
		try{
			len +=currentPropValue.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=propValueRange.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=expXiLianDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=yinziXiLianDes.getBytes("UTF-8").length;
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

			buffer.putLong(articleId);
			buffer.putInt(qilingType);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = currentPropValue.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = propValueRange.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = expXiLianDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = yinziXiLianDes.getBytes("UTF-8");
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
	 *	器灵类型
	 */
	public int getQilingType(){
		return qilingType;
	}

	/**
	 * 设置属性：
	 *	器灵类型
	 */
	public void setQilingType(int qilingType){
		this.qilingType = qilingType;
	}

	/**
	 * 获取属性：
	 *	当前器灵属性值描述
	 */
	public String getCurrentPropValue(){
		return currentPropValue;
	}

	/**
	 * 设置属性：
	 *	当前器灵属性值描述
	 */
	public void setCurrentPropValue(String currentPropValue){
		this.currentPropValue = currentPropValue;
	}

	/**
	 * 获取属性：
	 *	洗练范围描述
	 */
	public String getPropValueRange(){
		return propValueRange;
	}

	/**
	 * 设置属性：
	 *	洗练范围描述
	 */
	public void setPropValueRange(String propValueRange){
		this.propValueRange = propValueRange;
	}

	/**
	 * 获取属性：
	 *	用经验洗脸描述
	 */
	public String getExpXiLianDes(){
		return expXiLianDes;
	}

	/**
	 * 设置属性：
	 *	用经验洗脸描述
	 */
	public void setExpXiLianDes(String expXiLianDes){
		this.expXiLianDes = expXiLianDes;
	}

	/**
	 * 获取属性：
	 *	用银子洗练描述
	 */
	public String getYinziXiLianDes(){
		return yinziXiLianDes;
	}

	/**
	 * 设置属性：
	 *	用银子洗练描述
	 */
	public void setYinziXiLianDes(String yinziXiLianDes){
		this.yinziXiLianDes = yinziXiLianDes;
	}

}