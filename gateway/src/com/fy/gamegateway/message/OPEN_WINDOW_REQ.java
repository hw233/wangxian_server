package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端弹出窗口<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>title</td><td>String</td><td>title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>descriptionInUUB.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descriptionInUUB</td><td>String</td><td>descriptionInUUB.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>width</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>height</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>btns.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>btns[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>btns[0]</td><td>String</td><td>btns[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>btns[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>btns[1]</td><td>String</td><td>btns[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>btns[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>btns[2]</td><td>String</td><td>btns[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>oType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>oType</td><td>byte[]</td><td>oType.length</td><td>数组实际长度</td></tr>
 * </table>
 */
public class OPEN_WINDOW_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int id;
	String title;
	String descriptionInUUB;
	int width;
	int height;
	String[] btns;
	byte[] oType;

	public OPEN_WINDOW_REQ(){
	}

	public OPEN_WINDOW_REQ(long seqNum,int id,String title,String descriptionInUUB,int width,int height,String[] btns,byte[] oType){
		this.seqNum = seqNum;
		this.id = id;
		this.title = title;
		this.descriptionInUUB = descriptionInUUB;
		this.width = width;
		this.height = height;
		this.btns = btns;
		this.oType = oType;
	}

	public OPEN_WINDOW_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (int)mf.byteArrayToNumber(content,offset,4);
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
		width = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		height = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		btns = new String[len];
		for(int i = 0 ; i < btns.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			btns[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		oType = new byte[len];
		System.arraycopy(content,offset,oType,0,len);
		offset += len;
	}

	public int getType() {
		return 0x66666667;
	}

	public String getTypeDescription() {
		return "OPEN_WINDOW_REQ";
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
		len += 4;
		len += 4;
		len += 4;
		for(int i = 0 ; i < btns.length; i++){
			len += 2;
			try{
				len += btns[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += oType.length;
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

			buffer.putInt(id);
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
			buffer.putInt(width);
			buffer.putInt(height);
			buffer.putInt(btns.length);
			for(int i = 0 ; i < btns.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = btns[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(oType.length);
			buffer.put(oType);
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
	public int getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	窗口的Id
	 */
	public void setId(int id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	窗口的标题
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * 设置属性：
	 *	窗口的标题
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
	 *	指定宽度 0为默认 超过屏幕最大宽度为屏幕最大宽度
	 */
	public int getWidth(){
		return width;
	}

	/**
	 * 设置属性：
	 *	指定宽度 0为默认 超过屏幕最大宽度为屏幕最大宽度
	 */
	public void setWidth(int width){
		this.width = width;
	}

	/**
	 * 获取属性：
	 *	指定高度 0为默认 超过屏幕最大高度为屏幕最大高度
	 */
	public int getHeight(){
		return height;
	}

	/**
	 * 设置属性：
	 *	指定高度 0为默认 超过屏幕最大高度为屏幕最大高度
	 */
	public void setHeight(int height){
		this.height = height;
	}

	/**
	 * 获取属性：
	 *	按钮
	 */
	public String[] getBtns(){
		return btns;
	}

	/**
	 * 设置属性：
	 *	按钮
	 */
	public void setBtns(String[] btns){
		this.btns = btns;
	}

	/**
	 * 获取属性：
	 *	按钮的功能:0--关闭窗口,1-退出游戏
	 */
	public byte[] getOType(){
		return oType;
	}

	/**
	 * 设置属性：
	 *	按钮的功能:0--关闭窗口,1-退出游戏
	 */
	public void setOType(byte[] oType){
		this.oType = oType;
	}

}