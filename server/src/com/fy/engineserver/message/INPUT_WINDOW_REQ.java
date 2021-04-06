package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器要求客户端弹出一个输入窗口，要求用户输入。用户输入确认后，客户端发送OPTION_INPUT_REQ指令给服务器<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>windowId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>title</td><td>String</td><td>title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>descriptionInUUB.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descriptionInUUB</td><td>String</td><td>descriptionInUUB.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inputType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxSize</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defaultContent.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defaultContent</td><td>String</td><td>defaultContent.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>okStr.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>okStr</td><td>String</td><td>okStr.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cancelStr.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cancelStr</td><td>String</td><td>cancelStr.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>png.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>png</td><td>byte[]</td><td>png.length</td><td>数组实际长度</td></tr>
 * </table>
 */
public class INPUT_WINDOW_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int windowId;
	String title;
	String descriptionInUUB;
	byte inputType;
	byte maxSize;
	String defaultContent;
	String okStr;
	String cancelStr;
	byte[] png;

	public INPUT_WINDOW_REQ(){
	}

	public INPUT_WINDOW_REQ(long seqNum,int windowId,String title,String descriptionInUUB,byte inputType,byte maxSize,String defaultContent,String okStr,String cancelStr,byte[] png){
		this.seqNum = seqNum;
		this.windowId = windowId;
		this.title = title;
		this.descriptionInUUB = descriptionInUUB;
		this.inputType = inputType;
		this.maxSize = maxSize;
		this.defaultContent = defaultContent;
		this.okStr = okStr;
		this.cancelStr = cancelStr;
		this.png = png;
	}

	public INPUT_WINDOW_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		windowId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		title = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		descriptionInUUB = new String(content,offset,len,"UTF-8");
		offset += len;
		inputType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		maxSize = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		defaultContent = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		okStr = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		cancelStr = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		png = new byte[len];
		System.arraycopy(content,offset,png,0,len);
		offset += len;
	}

	public int getType() {
		return 0x00F0EEEF;
	}

	public String getTypeDescription() {
		return "INPUT_WINDOW_REQ";
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
			len +=title.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=descriptionInUUB.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 1;
		len += 2;
		try{
			len +=defaultContent.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=okStr.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=cancelStr.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += png.length;
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

			buffer.putInt(windowId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = title.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = descriptionInUUB.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(inputType);
			buffer.put(maxSize);
				try{
			tmpBytes1 = defaultContent.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = okStr.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = cancelStr.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(png.length);
			buffer.put(png);
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
	 *	窗口的Id
	 */
	public int getWindowId(){
		return windowId;
	}

	/**
	 * 设置属性：
	 *	窗口的Id
	 */
	public void setWindowId(int windowId){
		this.windowId = windowId;
	}

	/**
	 * 获取属性：
	 *	窗口的Id
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * 设置属性：
	 *	窗口的Id
	 */
	public void setTitle(String title){
		this.title = title;
	}

	/**
	 * 获取属性：
	 *	窗口的描述，UUB格式
	 */
	public String getDescriptionInUUB(){
		return descriptionInUUB;
	}

	/**
	 * 设置属性：
	 *	窗口的描述，UUB格式
	 */
	public void setDescriptionInUUB(String descriptionInUUB){
		this.descriptionInUUB = descriptionInUUB;
	}

	/**
	 * 获取属性：
	 *	输入内容的类型，0标识输入数字，1随意
	 */
	public byte getInputType(){
		return inputType;
	}

	/**
	 * 设置属性：
	 *	输入内容的类型，0标识输入数字，1随意
	 */
	public void setInputType(byte inputType){
		this.inputType = inputType;
	}

	/**
	 * 获取属性：
	 *	输入内容的最大长度，1个汉字长度为2
	 */
	public byte getMaxSize(){
		return maxSize;
	}

	/**
	 * 设置属性：
	 *	输入内容的最大长度，1个汉字长度为2
	 */
	public void setMaxSize(byte maxSize){
		this.maxSize = maxSize;
	}

	/**
	 * 获取属性：
	 *	默认内容
	 */
	public String getDefaultContent(){
		return defaultContent;
	}

	/**
	 * 设置属性：
	 *	默认内容
	 */
	public void setDefaultContent(String defaultContent){
		this.defaultContent = defaultContent;
	}

	/**
	 * 获取属性：
	 *	确认按钮上显示的文字，只有用户选择了确认按钮，才发生回复给服务器
	 */
	public String getOkStr(){
		return okStr;
	}

	/**
	 * 设置属性：
	 *	确认按钮上显示的文字，只有用户选择了确认按钮，才发生回复给服务器
	 */
	public void setOkStr(String okStr){
		this.okStr = okStr;
	}

	/**
	 * 获取属性：
	 *	取消按钮上显示的文字
	 */
	public String getCancelStr(){
		return cancelStr;
	}

	/**
	 * 设置属性：
	 *	取消按钮上显示的文字
	 */
	public void setCancelStr(String cancelStr){
		this.cancelStr = cancelStr;
	}

	/**
	 * 获取属性：
	 *	窗口的PNG图片
	 */
	public byte[] getPng(){
		return png;
	}

	/**
	 * 设置属性：
	 *	窗口的PNG图片
	 */
	public void setPng(byte[] png){
		this.png = png;
	}

}