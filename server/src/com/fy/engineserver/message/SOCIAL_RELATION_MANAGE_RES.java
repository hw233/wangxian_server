package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 管理玩家的社会关系，包括好友、黑名单和仇人。操作结果用通用提示指令通知<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result</td><td>String</td><td>result.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>operation</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ptype</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SOCIAL_RELATION_MANAGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String result;
	byte operation;
	byte ptype;
	long id;

	public SOCIAL_RELATION_MANAGE_RES(){
	}

	public SOCIAL_RELATION_MANAGE_RES(long seqNum,String result,byte operation,byte ptype,long id){
		this.seqNum = seqNum;
		this.result = result;
		this.operation = operation;
		this.ptype = ptype;
		this.id = id;
	}

	public SOCIAL_RELATION_MANAGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		result = new String(content,offset,len,"UTF-8");
		offset += len;
		operation = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		ptype = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x7000EC02;
	}

	public String getTypeDescription() {
		return "SOCIAL_RELATION_MANAGE_RES";
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
		len += 2;
		try{
			len +=result.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 1;
		len += 8;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = result.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(operation);
			buffer.put(ptype);
			buffer.putLong(id);
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
	 *	返回结果
	 */
	public String getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	返回结果
	 */
	public void setResult(String result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	0-添加，1-删除
	 */
	public byte getOperation(){
		return operation;
	}

	/**
	 * 设置属性：
	 *	0-添加，1-删除
	 */
	public void setOperation(byte operation){
		this.operation = operation;
	}

	/**
	 * 获取属性：
	 *	类型, 0-好友, 1-黑名单, 2-仇人
	 */
	public byte getPtype(){
		return ptype;
	}

	/**
	 * 设置属性：
	 *	类型, 0-好友, 1-黑名单, 2-仇人
	 */
	public void setPtype(byte ptype){
		this.ptype = ptype;
	}

	/**
	 * 获取属性：
	 *	玩家id
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	玩家id
	 */
	public void setId(long id){
		this.id = id;
	}

}