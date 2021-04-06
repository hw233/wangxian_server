package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器想让客户端播放动画，比如装备升级成功播放动画<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>avata.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>avata</td><td>String</td><td>avata.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>action.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>action</td><td>String</td><td>action.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>round</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>locationType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>locationX</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>locationY</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PLAY_ANIMATION_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String avata;
	String action;
	byte round;
	byte locationType;
	byte locationX;
	byte locationY;

	public PLAY_ANIMATION_REQ(){
	}

	public PLAY_ANIMATION_REQ(long seqNum,String avata,String action,byte round,byte locationType,byte locationX,byte locationY){
		this.seqNum = seqNum;
		this.avata = avata;
		this.action = action;
		this.round = round;
		this.locationType = locationType;
		this.locationX = locationX;
		this.locationY = locationY;
	}

	public PLAY_ANIMATION_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		avata = new String(content,offset,len,"utf-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		action = new String(content,offset,len,"utf-8");
		offset += len;
		round = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		locationType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		locationX = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		locationY = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x000003FF;
	}

	public String getTypeDescription() {
		return "PLAY_ANIMATION_REQ";
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
			len +=avata.getBytes("utf-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [utf-8]",e);
		}
		len += 2;
		try{
			len +=action.getBytes("utf-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [utf-8]",e);
		}
		len += 1;
		len += 1;
		len += 1;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = avata.getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = action.getBytes("utf-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [utf-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(round);
			buffer.put(locationType);
			buffer.put(locationX);
			buffer.put(locationY);
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
	 *	动画组名称比如人类_男
	 */
	public String getAvata(){
		return avata;
	}

	/**
	 * 设置属性：
	 *	动画组名称比如人类_男
	 */
	public void setAvata(String avata){
		this.avata = avata;
	}

	/**
	 * 获取属性：
	 *	动画名比如站_上
	 */
	public String getAction(){
		return action;
	}

	/**
	 * 设置属性：
	 *	动画名比如站_上
	 */
	public void setAction(String action){
		this.action = action;
	}

	/**
	 * 获取属性：
	 *	动画播放次数
	 */
	public byte getRound(){
		return round;
	}

	/**
	 * 设置属性：
	 *	动画播放次数
	 */
	public void setRound(byte round){
		this.round = round;
	}

	/**
	 * 获取属性：
	 *	动画播放位置类型，0为默认位置类型，1为武器强化中间孔的位置，2为鉴定炼化绑定等的武器位置，3为宠物合成主宠位置，4
	 */
	public byte getLocationType(){
		return locationType;
	}

	/**
	 * 设置属性：
	 *	动画播放位置类型，0为默认位置类型，1为武器强化中间孔的位置，2为鉴定炼化绑定等的武器位置，3为宠物合成主宠位置，4
	 */
	public void setLocationType(byte locationType){
		this.locationType = locationType;
	}

	/**
	 * 获取属性：
	 *	动画播放位置X，当播放位置类型为默认类型0时，这个值表示横坐标百分比。播放位置类型不是默认类型时，这个值没有意义
	 */
	public byte getLocationX(){
		return locationX;
	}

	/**
	 * 设置属性：
	 *	动画播放位置X，当播放位置类型为默认类型0时，这个值表示横坐标百分比。播放位置类型不是默认类型时，这个值没有意义
	 */
	public void setLocationX(byte locationX){
		this.locationX = locationX;
	}

	/**
	 * 获取属性：
	 *	动画播放位置Y，当播放位置类型为默认类型0时，这个值表示纵坐标百分比。播放位置类型不是默认类型时，这个值没有意义
	 */
	public byte getLocationY(){
		return locationY;
	}

	/**
	 * 设置属性：
	 *	动画播放位置Y，当播放位置类型为默认类型0时，这个值表示纵坐标百分比。播放位置类型不是默认类型时，这个值没有意义
	 */
	public void setLocationY(byte locationY){
		this.locationY = locationY;
	}

}