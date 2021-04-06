package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器通知客户端，0标识2秒就消失的提示窗口，1标识信息到聊天栏, 2标识在屏幕上方显示文字，持续几秒,最多3条10秒，颜色服务器控制，3标识从屏幕中间右向左滚动 字一个个显示一个个消失，颜色服务器控制，4标识在屏幕下方从左向右显示滚动信息并带粒子, 2条，变颜色1秒，持续10秒, 5覆盖所有窗口上面的提示窗口，一定时间后消失点击不消失，并加入聊天中<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mode</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hintContent.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hintContent</td><td>String</td><td>hintContent.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class HINT_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte mode;
	String hintContent;

	public HINT_REQ(){
	}

	public HINT_REQ(long seqNum,byte mode,String hintContent){
		this.seqNum = seqNum;
		this.mode = mode;
		this.hintContent = hintContent;
	}

	public HINT_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		mode = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		hintContent = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x00F0EF00;
	}

	public String getTypeDescription() {
		return "HINT_REQ";
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
			len +=hintContent.getBytes("UTF-8").length;
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

			buffer.put(mode);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = hintContent.getBytes("UTF-8");
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
	 *	0标识2秒就消失的提示窗口，1标识信息到聊天栏, 2标识在屏幕上方显示文字，持续几秒,最多3条10秒，颜色服务器控制，3标识从屏幕中间右向左滚动 字一个个显示一个个消失，颜色服务器控制，4标识在屏幕下方从左向右显示滚动信息并带粒子, 2条，变颜色1秒，持续10秒, 5覆盖所有窗口上面的提示窗口，一定时间后消失点击不消失，并加入聊天中
	 */
	public byte getMode(){
		return mode;
	}

	/**
	 * 设置属性：
	 *	0标识2秒就消失的提示窗口，1标识信息到聊天栏, 2标识在屏幕上方显示文字，持续几秒,最多3条10秒，颜色服务器控制，3标识从屏幕中间右向左滚动 字一个个显示一个个消失，颜色服务器控制，4标识在屏幕下方从左向右显示滚动信息并带粒子, 2条，变颜色1秒，持续10秒, 5覆盖所有窗口上面的提示窗口，一定时间后消失点击不消失，并加入聊天中
	 */
	public void setMode(byte mode){
		this.mode = mode;
	}

	/**
	 * 获取属性：
	 *	提示信息
	 */
	public String getHintContent(){
		return hintContent;
	}

	/**
	 * 设置属性：
	 *	提示信息
	 */
	public void setHintContent(String hintContent){
		this.hintContent = hintContent;
	}

}