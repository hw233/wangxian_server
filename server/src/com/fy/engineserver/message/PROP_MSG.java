package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 具体prop对象的描述，加一些按钮<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>knapIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>num</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showName</td><td>String</td><td>showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>iconId</td><td>String</td><td>iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>colorType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>msg.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>msg</td><td>String</td><td>msg.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class PROP_MSG implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int knapIndex;
	int num;
	long id;
	String showName;
	String iconId;
	byte colorType;
	String msg;

	public PROP_MSG(){
	}

	public PROP_MSG(long seqNum,int knapIndex,int num,long id,String showName,String iconId,byte colorType,String msg){
		this.seqNum = seqNum;
		this.knapIndex = knapIndex;
		this.num = num;
		this.id = id;
		this.showName = showName;
		this.iconId = iconId;
		this.colorType = colorType;
		this.msg = msg;
	}

	public PROP_MSG(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		knapIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		num = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		showName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		iconId = new String(content,offset,len,"UTF-8");
		offset += len;
		colorType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		msg = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x0F200005;
	}

	public String getTypeDescription() {
		return "PROP_MSG";
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
		len += 4;
		len += 8;
		len += 2;
		try{
			len +=showName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=iconId.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 2;
		try{
			len +=msg.getBytes("UTF-8").length;
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

			buffer.putInt(knapIndex);
			buffer.putInt(num);
			buffer.putLong(id);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = showName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = iconId.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(colorType);
				try{
			tmpBytes1 = msg.getBytes("UTF-8");
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
	 *	背包中的索引
	 */
	public int getKnapIndex(){
		return knapIndex;
	}

	/**
	 * 设置属性：
	 *	背包中的索引
	 */
	public void setKnapIndex(int knapIndex){
		this.knapIndex = knapIndex;
	}

	/**
	 * 获取属性：
	 *	数量
	 */
	public int getNum(){
		return num;
	}

	/**
	 * 设置属性：
	 *	数量
	 */
	public void setNum(int num){
		this.num = num;
	}

	/**
	 * 获取属性：
	 *	物品实体的唯一标识
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	物品实体的唯一标识
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	物品的显示名称
	 */
	public String getShowName(){
		return showName;
	}

	/**
	 * 设置属性：
	 *	物品的显示名称
	 */
	public void setShowName(String showName){
		this.showName = showName;
	}

	/**
	 * 获取属性：
	 *	普通物品的图标
	 */
	public String getIconId(){
		return iconId;
	}

	/**
	 * 设置属性：
	 *	普通物品的图标
	 */
	public void setIconId(String iconId){
		this.iconId = iconId;
	}

	/**
	 * 获取属性：
	 *	颜色类型
	 */
	public byte getColorType(){
		return colorType;
	}

	/**
	 * 设置属性：
	 *	颜色类型
	 */
	public void setColorType(byte colorType){
		this.colorType = colorType;
	}

	/**
	 * 获取属性：
	 *	大泡泡内容
	 */
	public String getMsg(){
		return msg;
	}

	/**
	 * 设置属性：
	 *	大泡泡内容
	 */
	public void setMsg(String msg){
		this.msg = msg;
	}

}