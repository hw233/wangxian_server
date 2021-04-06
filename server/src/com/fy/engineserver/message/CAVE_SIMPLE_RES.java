package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看仙府简信息,是否有仙府,仙府状态<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>caveStatus</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>caveStatusDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>caveStatusDes</td><td>String</td><td>caveStatusDes.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class CAVE_SIMPLE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int caveStatus;
	String caveStatusDes;

	public CAVE_SIMPLE_RES(){
	}

	public CAVE_SIMPLE_RES(long seqNum,int caveStatus,String caveStatusDes){
		this.seqNum = seqNum;
		this.caveStatus = caveStatus;
		this.caveStatusDes = caveStatusDes;
	}

	public CAVE_SIMPLE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		caveStatus = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		caveStatusDes = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x8F00002C;
	}

	public String getTypeDescription() {
		return "CAVE_SIMPLE_RES";
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
		len += 2;
		try{
			len +=caveStatusDes.getBytes("UTF-8").length;
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

			buffer.putInt(caveStatus);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = caveStatusDes.getBytes("UTF-8");
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
	 *	仙府状态:-1无仙府.0:正常.1:封印.2:删除.3:锁定
	 */
	public int getCaveStatus(){
		return caveStatus;
	}

	/**
	 * 设置属性：
	 *	仙府状态:-1无仙府.0:正常.1:封印.2:删除.3:锁定
	 */
	public void setCaveStatus(int caveStatus){
		this.caveStatus = caveStatus;
	}

	/**
	 * 获取属性：
	 *	仙府状态描述
	 */
	public String getCaveStatusDes(){
		return caveStatusDes;
	}

	/**
	 * 设置属性：
	 *	仙府状态描述
	 */
	public void setCaveStatusDes(String caveStatusDes){
		this.caveStatusDes = caveStatusDes;
	}

}