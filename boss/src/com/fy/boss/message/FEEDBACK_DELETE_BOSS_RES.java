package com.fy.boss.message;

import com.fy.boss.message.BossMessageFactory;
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
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resultMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resultMess</td><td>String</td><td>resultMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class FEEDBACK_DELETE_BOSS_RES implements ResponseMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	long id;
	String resultMess;
	byte result;

	public FEEDBACK_DELETE_BOSS_RES(){
	}

	public FEEDBACK_DELETE_BOSS_RES(long seqNum,long id,String resultMess,byte result){
		this.seqNum = seqNum;
		this.id = id;
		this.resultMess = resultMess;
		this.result = result;
	}

	public FEEDBACK_DELETE_BOSS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		resultMess = new String(content,offset,len,"UTF-8");
		offset += len;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x8000F015;
	}

	public String getTypeDescription() {
		return "FEEDBACK_DELETE_BOSS_RES";
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
			len +=resultMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
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

			buffer.putLong(id);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = resultMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(result);
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
	 *	反馈唯一标识
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	反馈唯一标识
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	成功失败原因
	 */
	public String getResultMess(){
		return resultMess;
	}

	/**
	 * 设置属性：
	 *	成功失败原因
	 */
	public void setResultMess(String resultMess){
		this.resultMess = resultMess;
	}

	/**
	 * 获取属性：
	 *	结果, 0-成功, 1-失败
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果, 0-成功, 1-失败
	 */
	public void setResult(byte result){
		this.result = result;
	}

}