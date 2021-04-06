package com.fy.engineserver.toolskill.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求BUFF数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs.length</td><td>int</td><td>2</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[0]</td><td>String</td><td>buffs[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[1]</td><td>String</td><td>buffs[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buffs[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buffs[2]</td><td>String</td><td>buffs[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class BUFF_RES implements ResponseMessage{

	static CareerAndSkillFactory mf = CareerAndSkillFactory.getInstance();

	long seqNum;
	String[] buffs;

	public BUFF_RES(long seqNum,String[] buffs){
		this.seqNum = seqNum;
		this.buffs = buffs;
	}

	public BUFF_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 512) throw new Exception("array length ["+len+"] big than the max length [512]");
		buffs = new String[len];
		for(int i = 0 ; i < buffs.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 1024) throw new Exception("string length ["+len+"] big than the max length [1024]");
			buffs[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x80000002;
	}

	public String getTypeDescription() {
		return "BUFF_RES";
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
		for(int i = 0 ; i < buffs.length; i++){
			len += 2;
			try{
				len += buffs[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
				throw new RuntimeException("unsupported encoding [UTF-8]");
			}
		}
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

			buffer.putShort((short)buffs.length);
			for(int i = 0 ; i < buffs.length; i++){
				byte[] tmpBytes2 = buffs[i].getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :" + e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	BUFF名称
	 */
	public String[] getBuffs(){
		return buffs;
	}

	/**
	 * 设置属性：
	 *	BUFF名称
	 */
	public void setBuffs(String[] buffs){
		this.buffs = buffs;
	}

}
