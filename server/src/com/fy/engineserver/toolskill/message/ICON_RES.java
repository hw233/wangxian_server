package com.fy.engineserver.toolskill.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求技能图标数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons.length</td><td>int</td><td>2</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[0]</td><td>String</td><td>icons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[1]</td><td>String</td><td>icons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>icons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>icons[2]</td><td>String</td><td>icons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>imageLength.length</td><td>int</td><td>2</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>imageLength</td><td>int[]</td><td>imageLength.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>imageData.length</td><td>int</td><td>2</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>imageData</td><td>byte[]</td><td>imageData.length</td><td>数组实际长度</td></tr>
 * </table>
 */
public class ICON_RES implements ResponseMessage{

	static CareerAndSkillFactory mf = CareerAndSkillFactory.getInstance();

	long seqNum;
	String[] icons;
	int[] imageLength;
	byte[] imageData;

	public ICON_RES(long seqNum,String[] icons,int[] imageLength,byte[] imageData){
		this.seqNum = seqNum;
		this.icons = icons;
		this.imageLength = imageLength;
		this.imageData = imageData;
	}

	public ICON_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 512) throw new Exception("array length ["+len+"] big than the max length [512]");
		icons = new String[len];
		for(int i = 0 ; i < icons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 1024) throw new Exception("string length ["+len+"] big than the max length [1024]");
			icons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 512) throw new Exception("array length ["+len+"] big than the max length [512]");
		imageLength = new int[len];
		for(int i = 0 ; i < imageLength.length ; i++){
			imageLength[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 5242880) throw new Exception("array length ["+len+"] big than the max length [5242880]");
		imageData = new byte[len];
		System.arraycopy(content,offset,imageData,0,len);
		offset += len;
	}

	public int getType() {
		return 0x80000001;
	}

	public String getTypeDescription() {
		return "ICON_RES";
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
		for(int i = 0 ; i < icons.length; i++){
			len += 2;
			try{
				len += icons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
				throw new RuntimeException("unsupported encoding [UTF-8]");
			}
		}
		len += 2;
		len += imageLength.length * 4;
		len += 4;
		len += imageData.length;
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

			buffer.putShort((short)icons.length);
			for(int i = 0 ; i < icons.length; i++){
				byte[] tmpBytes2 = icons[i].getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putShort((short)imageLength.length);
			for(int i = 0 ; i < imageLength.length; i++){
				buffer.putInt(imageLength[i]);
			}
			buffer.putInt(imageData.length);
			buffer.put(imageData);
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :" + e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	技能图标名称
	 */
	public String[] getIcons(){
		return icons;
	}

	/**
	 * 设置属性：
	 *	技能图标名称
	 */
	public void setIcons(String[] icons){
		this.icons = icons;
	}

	/**
	 * 获取属性：
	 *	技能图标数据长度
	 */
	public int[] getImageLength(){
		return imageLength;
	}

	/**
	 * 设置属性：
	 *	技能图标数据长度
	 */
	public void setImageLength(int[] imageLength){
		this.imageLength = imageLength;
	}

	/**
	 * 获取属性：
	 *	技能图标数据
	 */
	public byte[] getImageData(){
		return imageData;
	}

	/**
	 * 设置属性：
	 *	技能图标数据
	 */
	public void setImageData(byte[] imageData){
		this.imageData = imageData;
	}

}
