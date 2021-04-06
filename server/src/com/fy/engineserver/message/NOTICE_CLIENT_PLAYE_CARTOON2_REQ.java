package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端播放动画2<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatatypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avatatypes</td><td>int[]</td><td>avatatypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatas.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avatas[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatas[0]</td><td>String</td><td>avatas[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avatas[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatas[1]</td><td>String</td><td>avatas[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avatas[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatas[2]</td><td>String</td><td>avatas[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatatypes2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avatatypes2</td><td>int[]</td><td>avatatypes2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatas2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avatas2[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatas2[0]</td><td>String</td><td>avatas2[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avatas2[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatas2[1]</td><td>String</td><td>avatas2[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avatas2[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avatas2[2]</td><td>String</td><td>avatas2[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NOTICE_CLIENT_PLAYE_CARTOON2_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int[] avatatypes;
	String[] avatas;
	int[] avatatypes2;
	String[] avatas2;

	public NOTICE_CLIENT_PLAYE_CARTOON2_REQ(){
	}

	public NOTICE_CLIENT_PLAYE_CARTOON2_REQ(long seqNum,int[] avatatypes,String[] avatas,int[] avatatypes2,String[] avatas2){
		this.seqNum = seqNum;
		this.avatatypes = avatatypes;
		this.avatas = avatas;
		this.avatatypes2 = avatatypes2;
		this.avatas2 = avatas2;
	}

	public NOTICE_CLIENT_PLAYE_CARTOON2_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avatatypes = new int[len];
		for(int i = 0 ; i < avatatypes.length ; i++){
			avatatypes[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avatas = new String[len];
		for(int i = 0 ; i < avatas.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			avatas[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avatatypes2 = new int[len];
		for(int i = 0 ; i < avatatypes2.length ; i++){
			avatatypes2[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		avatas2 = new String[len];
		for(int i = 0 ; i < avatas2.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			avatas2[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x00FF0141;
	}

	public String getTypeDescription() {
		return "NOTICE_CLIENT_PLAYE_CARTOON2_REQ";
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
		len += avatatypes.length * 4;
		len += 4;
		for(int i = 0 ; i < avatas.length; i++){
			len += 2;
			try{
				len += avatas[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += avatatypes2.length * 4;
		len += 4;
		for(int i = 0 ; i < avatas2.length; i++){
			len += 2;
			try{
				len += avatas2[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			buffer.putInt(avatatypes.length);
			for(int i = 0 ; i < avatatypes.length; i++){
				buffer.putInt(avatatypes[i]);
			}
			buffer.putInt(avatas.length);
			for(int i = 0 ; i < avatas.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = avatas[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(avatatypes2.length);
			for(int i = 0 ; i < avatatypes2.length; i++){
				buffer.putInt(avatatypes2[i]);
			}
			buffer.putInt(avatas2.length);
			for(int i = 0 ; i < avatas2.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = avatas2[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
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
	 *	无帮助说明
	 */
	public int[] getAvatatypes(){
		return avatatypes;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAvatatypes(int[] avatatypes){
		this.avatatypes = avatatypes;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getAvatas(){
		return avatas;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAvatas(String[] avatas){
		this.avatas = avatas;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getAvatatypes2(){
		return avatatypes2;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAvatatypes2(int[] avatatypes2){
		this.avatatypes2 = avatatypes2;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String[] getAvatas2(){
		return avatas2;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAvatas2(String[] avatas2){
		this.avatas2 = avatas2;
	}

}