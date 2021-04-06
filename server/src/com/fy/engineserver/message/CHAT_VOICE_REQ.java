package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 发送语音聊天,这里面的文件大小是zip压缩后的<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sort</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>senderId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>receiverId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>voiceKeyID.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>voiceKeyID</td><td>String</td><td>voiceKeyID.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>voiceTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>voiceSize</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>voiceInfoNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherInfo.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherInfo[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherInfo[0]</td><td>String</td><td>otherInfo[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherInfo[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherInfo[1]</td><td>String</td><td>otherInfo[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>otherInfo[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>otherInfo[2]</td><td>String</td><td>otherInfo[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CHAT_VOICE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte sort;
	long senderId;
	long receiverId;
	String voiceKeyID;
	int voiceTime;
	int voiceSize;
	int voiceInfoNum;
	String[] otherInfo;

	public CHAT_VOICE_REQ(){
	}

	public CHAT_VOICE_REQ(long seqNum,byte sort,long senderId,long receiverId,String voiceKeyID,int voiceTime,int voiceSize,int voiceInfoNum,String[] otherInfo){
		this.seqNum = seqNum;
		this.sort = sort;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.voiceKeyID = voiceKeyID;
		this.voiceTime = voiceTime;
		this.voiceSize = voiceSize;
		this.voiceInfoNum = voiceInfoNum;
		this.otherInfo = otherInfo;
	}

	public CHAT_VOICE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		sort = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		senderId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		receiverId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		voiceKeyID = new String(content,offset,len,"UTF-8");
		offset += len;
		voiceTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		voiceSize = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		voiceInfoNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		otherInfo = new String[len];
		for(int i = 0 ; i < otherInfo.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			otherInfo[i] = new String(content,offset,len);
		offset += len;
		}
	}

	public int getType() {
		return 0x00000E09;
	}

	public String getTypeDescription() {
		return "CHAT_VOICE_REQ";
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
		len += 8;
		len += 8;
		len += 2;
		try{
			len +=voiceKeyID.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < otherInfo.length; i++){
			len += 2;
			len += otherInfo[i].getBytes().length;
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

			buffer.put(sort);
			buffer.putLong(senderId);
			buffer.putLong(receiverId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = voiceKeyID.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(voiceTime);
			buffer.putInt(voiceSize);
			buffer.putInt(voiceInfoNum);
			buffer.putInt(otherInfo.length);
			for(int i = 0 ; i < otherInfo.length; i++){
				byte[] tmpBytes2 ;
				tmpBytes2 = otherInfo[i].getBytes();
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
	 *	所属channel
	 */
	public byte getSort(){
		return sort;
	}

	/**
	 * 设置属性：
	 *	所属channel
	 */
	public void setSort(byte sort){
		this.sort = sort;
	}

	/**
	 * 获取属性：
	 *	发送人id
	 */
	public long getSenderId(){
		return senderId;
	}

	/**
	 * 设置属性：
	 *	发送人id
	 */
	public void setSenderId(long senderId){
		this.senderId = senderId;
	}

	/**
	 * 获取属性：
	 *	接受人id
	 */
	public long getReceiverId(){
		return receiverId;
	}

	/**
	 * 设置属性：
	 *	接受人id
	 */
	public void setReceiverId(long receiverId){
		this.receiverId = receiverId;
	}

	/**
	 * 获取属性：
	 *	语音标记ID
	 */
	public String getVoiceKeyID(){
		return voiceKeyID;
	}

	/**
	 * 设置属性：
	 *	语音标记ID
	 */
	public void setVoiceKeyID(String voiceKeyID){
		this.voiceKeyID = voiceKeyID;
	}

	/**
	 * 获取属性：
	 *	语音时长(毫秒)
	 */
	public int getVoiceTime(){
		return voiceTime;
	}

	/**
	 * 设置属性：
	 *	语音时长(毫秒)
	 */
	public void setVoiceTime(int voiceTime){
		this.voiceTime = voiceTime;
	}

	/**
	 * 获取属性：
	 *	语音文件大小byte是zip压缩后的
	 */
	public int getVoiceSize(){
		return voiceSize;
	}

	/**
	 * 设置属性：
	 *	语音文件大小byte是zip压缩后的
	 */
	public void setVoiceSize(int voiceSize){
		this.voiceSize = voiceSize;
	}

	/**
	 * 获取属性：
	 *	语音文件分段数目CHAT_VOICE_INFO_REQ这个数目
	 */
	public int getVoiceInfoNum(){
		return voiceInfoNum;
	}

	/**
	 * 设置属性：
	 *	语音文件分段数目CHAT_VOICE_INFO_REQ这个数目
	 */
	public void setVoiceInfoNum(int voiceInfoNum){
		this.voiceInfoNum = voiceInfoNum;
	}

	/**
	 * 获取属性：
	 *	预留字段
	 */
	public String[] getOtherInfo(){
		return otherInfo;
	}

	/**
	 * 设置属性：
	 *	预留字段
	 */
	public void setOtherInfo(String[] otherInfo){
		this.otherInfo = otherInfo;
	}

}