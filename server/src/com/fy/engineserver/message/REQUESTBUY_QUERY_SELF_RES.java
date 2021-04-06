package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.trade.requestbuy.RequestBuyInfo4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看自己的求购<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>startNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client.length</td><td>int</td><td>4个字节</td><td>RequestBuyInfo4Client数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].requestBuyId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[0].mainType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].mainType</td><td>String</td><td>requestBuyInfo4Client[0].mainType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[0].subType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].subType</td><td>String</td><td>requestBuyInfo4Client[0].subType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[0].entityId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[0].icon</td><td>String</td><td>requestBuyInfo4Client[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[0].articleName</td><td>String</td><td>requestBuyInfo4Client[0].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[0].grademax</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[0].perPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].leftNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[0].releasePlayerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].releasePlayerName</td><td>String</td><td>requestBuyInfo4Client[0].releasePlayerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[0].leftTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[0].leftTime</td><td>String</td><td>requestBuyInfo4Client[0].leftTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].requestBuyId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[1].mainType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].mainType</td><td>String</td><td>requestBuyInfo4Client[1].mainType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[1].subType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].subType</td><td>String</td><td>requestBuyInfo4Client[1].subType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[1].entityId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[1].icon</td><td>String</td><td>requestBuyInfo4Client[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[1].articleName</td><td>String</td><td>requestBuyInfo4Client[1].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[1].grademax</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[1].perPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].leftNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[1].releasePlayerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].releasePlayerName</td><td>String</td><td>requestBuyInfo4Client[1].releasePlayerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[1].leftTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[1].leftTime</td><td>String</td><td>requestBuyInfo4Client[1].leftTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].requestBuyId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[2].mainType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].mainType</td><td>String</td><td>requestBuyInfo4Client[2].mainType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[2].subType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].subType</td><td>String</td><td>requestBuyInfo4Client[2].subType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[2].entityId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[2].icon</td><td>String</td><td>requestBuyInfo4Client[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[2].articleName</td><td>String</td><td>requestBuyInfo4Client[2].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].grade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[2].grademax</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[2].perPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].leftNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[2].releasePlayerName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].releasePlayerName</td><td>String</td><td>requestBuyInfo4Client[2].releasePlayerName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client[2].leftTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client[2].leftTime</td><td>String</td><td>requestBuyInfo4Client[2].leftTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class REQUESTBUY_QUERY_SELF_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int startNum;
	int allNum;
	RequestBuyInfo4Client[] requestBuyInfo4Client;

	public REQUESTBUY_QUERY_SELF_RES(){
	}

	public REQUESTBUY_QUERY_SELF_RES(long seqNum,int startNum,int allNum,RequestBuyInfo4Client[] requestBuyInfo4Client){
		this.seqNum = seqNum;
		this.startNum = startNum;
		this.allNum = allNum;
		this.requestBuyInfo4Client = requestBuyInfo4Client;
	}

	public REQUESTBUY_QUERY_SELF_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		startNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		allNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		requestBuyInfo4Client = new RequestBuyInfo4Client[len];
		for(int i = 0 ; i < requestBuyInfo4Client.length ; i++){
			requestBuyInfo4Client[i] = new RequestBuyInfo4Client();
			requestBuyInfo4Client[i].setRequestBuyId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			requestBuyInfo4Client[i].setMainType(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			requestBuyInfo4Client[i].setSubType(new String(content,offset,len,"UTF-8"));
			offset += len;
			requestBuyInfo4Client[i].setEntityId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			requestBuyInfo4Client[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			requestBuyInfo4Client[i].setArticleName(new String(content,offset,len,"utf-8"));
			offset += len;
			requestBuyInfo4Client[i].setGrade((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			requestBuyInfo4Client[i].setGrademax((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			requestBuyInfo4Client[i].setColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			requestBuyInfo4Client[i].setPerPrice((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			requestBuyInfo4Client[i].setLeftNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			requestBuyInfo4Client[i].setReleasePlayerName(new String(content,offset,len,"utf-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			requestBuyInfo4Client[i].setLeftTime(new String(content,offset,len,"utf-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x70F00103;
	}

	public String getTypeDescription() {
		return "REQUESTBUY_QUERY_SELF_RES";
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
		len += 4;
		len += 4;
		for(int i = 0 ; i < requestBuyInfo4Client.length ; i++){
			len += 8;
			len += 2;
			if(requestBuyInfo4Client[i].getMainType() != null){
				try{
				len += requestBuyInfo4Client[i].getMainType().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(requestBuyInfo4Client[i].getSubType() != null){
				try{
				len += requestBuyInfo4Client[i].getSubType().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 8;
			len += 2;
			if(requestBuyInfo4Client[i].getIcon() != null){
				try{
				len += requestBuyInfo4Client[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(requestBuyInfo4Client[i].getArticleName() != null){
				try{
				len += requestBuyInfo4Client[i].getArticleName().getBytes("utf-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += 8;
			len += 4;
			len += 2;
			if(requestBuyInfo4Client[i].getReleasePlayerName() != null){
				try{
				len += requestBuyInfo4Client[i].getReleasePlayerName().getBytes("utf-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			}
			len += 2;
			if(requestBuyInfo4Client[i].getLeftTime() != null){
				try{
				len += requestBuyInfo4Client[i].getLeftTime().getBytes("utf-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			}
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

			buffer.putInt(startNum);
			buffer.putInt(allNum);
			buffer.putInt(requestBuyInfo4Client.length);
			for(int i = 0 ; i < requestBuyInfo4Client.length ; i++){
				buffer.putLong(requestBuyInfo4Client[i].getRequestBuyId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = requestBuyInfo4Client[i].getMainType().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = requestBuyInfo4Client[i].getSubType().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putLong(requestBuyInfo4Client[i].getEntityId());
				try{
				tmpBytes2 = requestBuyInfo4Client[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = requestBuyInfo4Client[i].getArticleName().getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)requestBuyInfo4Client[i].getGrade());
				buffer.putInt((int)requestBuyInfo4Client[i].getGrademax());
				buffer.putInt((int)requestBuyInfo4Client[i].getColor());
				buffer.putLong(requestBuyInfo4Client[i].getPerPrice());
				buffer.putInt((int)requestBuyInfo4Client[i].getLeftNum());
				try{
				tmpBytes2 = requestBuyInfo4Client[i].getReleasePlayerName().getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = requestBuyInfo4Client[i].getLeftTime().getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	起始位置
	 */
	public int getStartNum(){
		return startNum;
	}

	/**
	 * 设置属性：
	 *	起始位置
	 */
	public void setStartNum(int startNum){
		this.startNum = startNum;
	}

	/**
	 * 获取属性：
	 *	总个数
	 */
	public int getAllNum(){
		return allNum;
	}

	/**
	 * 设置属性：
	 *	总个数
	 */
	public void setAllNum(int allNum){
		this.allNum = allNum;
	}

	/**
	 * 获取属性：
	 *	求购信息
	 */
	public RequestBuyInfo4Client[] getRequestBuyInfo4Client(){
		return requestBuyInfo4Client;
	}

	/**
	 * 设置属性：
	 *	求购信息
	 */
	public void setRequestBuyInfo4Client(RequestBuyInfo4Client[] requestBuyInfo4Client){
		this.requestBuyInfo4Client = requestBuyInfo4Client;
	}

}