package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端向服务器发送的资源更新请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resultString.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resultString</td><td>String</td><td>resultString.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>dataLength</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>packetNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>patchType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PATCH_CLIENT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	String resultString;
	int dataLength;
	int packetNum;
	byte patchType;

	public PATCH_CLIENT_RES(long seqNum,byte result,String resultString,int dataLength,int packetNum,byte patchType){
		this.seqNum = seqNum;
		this.result = result;
		this.resultString = resultString;
		this.dataLength = dataLength;
		this.packetNum = packetNum;
		this.patchType = patchType;
	}

	public PATCH_CLIENT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		resultString = new String(content,offset,len,"UTF-8");
		offset += len;
		dataLength = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		packetNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		patchType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x80000F23;
	}

	public String getTypeDescription() {
		return "PATCH_CLIENT_RES";
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
		len += 1;
		len += 2;
		try{
			len +=resultString.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 1;
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

			buffer.put(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = resultString.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(dataLength);
			buffer.putInt(packetNum);
			buffer.put(patchType);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	结果，0表示成功，其他表示错误，
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果，0表示成功，其他表示错误，
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	错误信息
	 */
	public String getResultString(){
		return resultString;
	}

	/**
	 * 设置属性：
	 *	错误信息
	 */
	public void setResultString(String resultString){
		this.resultString = resultString;
	}

	/**
	 * 获取属性：
	 *	资源补丁包的大小，单位为字节
	 */
	public int getDataLength(){
		return dataLength;
	}

	/**
	 * 设置属性：
	 *	资源补丁包的大小，单位为字节
	 */
	public void setDataLength(int dataLength){
		this.dataLength = dataLength;
	}

	/**
	 * 获取属性：
	 *	资源补丁包的数量
	 */
	public int getPacketNum(){
		return packetNum;
	}

	/**
	 * 设置属性：
	 *	资源补丁包的数量
	 */
	public void setPacketNum(int packetNum){
		this.packetNum = packetNum;
	}

	/**
	 * 获取属性：
	 *	0-补丁包，1-全量包
	 */
	public byte getPatchType(){
		return patchType;
	}

	/**
	 * 设置属性：
	 *	0-补丁包，1-全量包
	 */
	public void setPatchType(byte patchType){
		this.patchType = patchType;
	}

}
