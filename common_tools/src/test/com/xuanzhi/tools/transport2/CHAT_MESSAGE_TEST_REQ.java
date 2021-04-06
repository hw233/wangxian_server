package com.xuanzhi.tools.transport2;

import java.nio.ByteBuffer;
import com.xuanzhi.tools.transport.*;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>spriteId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>spriteName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>spriteName</td><td>String</td><td>spriteName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>spriteDesp</td><td>String</td><td>16</td><td>固定长度的字符串，不够长度右边用 补齐，多余的截取。汉字可能被截成乱码</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>message.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>message</td><td>String</td><td>message.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class CHAT_MESSAGE_TEST_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int spriteId;
	String spriteName;
	String spriteDesp;
	String message;

	public CHAT_MESSAGE_TEST_REQ(){
	}

	public CHAT_MESSAGE_TEST_REQ(long seqNum,int spriteId,String spriteName,String spriteDesp,String message){
		this.seqNum = seqNum;
		this.spriteId = spriteId;
		this.spriteName = spriteName;
		this.spriteDesp = spriteDesp;
		this.message = message;
	}

	public CHAT_MESSAGE_TEST_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		spriteId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		spriteName = new String(content,offset,len);
		offset += len;
		spriteDesp = new String(content,offset,16).trim();
		offset += 16;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		message = new String(content,offset,len);
		offset += len;
	}

	public int getType() {
		return 0x00000002;
	}

	public String getTypeDescription() {
		return "CHAT_MESSAGE_TEST_REQ";
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
		len +=spriteName.getBytes().length;
		len += 16;
		len += 2;
		len +=message.getBytes().length;
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

			buffer.putInt(spriteId);
			byte[] tmpBytes1;
			tmpBytes1 = spriteName.getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = spriteDesp.getBytes();
			if(tmpBytes1.length >= 16){
				buffer.put(tmpBytes1,0,16);
			}else{
				buffer.put(tmpBytes1);
				buffer.put(new byte[16-tmpBytes1.length]);
			}
			tmpBytes1 = message.getBytes();
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
	 *	无帮助说明
	 */
	public int getSpriteId(){
		return spriteId;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setSpriteId(int spriteId){
		this.spriteId = spriteId;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String getSpriteName(){
		return spriteName;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setSpriteName(String spriteName){
		this.spriteName = spriteName;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public String getSpriteDesp(){
		return spriteDesp;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setSpriteDesp(String spriteDesp){
		this.spriteDesp = spriteDesp;
	}

	/**
	 * 获取属性：
	 *	内容
	 */
	public String getMessage(){
		return message;
	}

	/**
	 * 设置属性：
	 *	内容
	 */
	public void setMessage(String message){
		this.message = message;
	}

}