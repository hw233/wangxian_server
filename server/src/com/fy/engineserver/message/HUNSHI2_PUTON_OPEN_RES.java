package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.hunshi.Hunshi2Cell;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开套装魂石镶嵌界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2cell.length</td><td>int</td><td>4个字节</td><td>Hunshi2Cell数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2cell[0].hunshiId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2cell[0].openType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2cell[0].open</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2cell[0].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2cell[0].des</td><td>String</td><td>hunshi2cell[0].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2cell[1].hunshiId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2cell[1].openType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2cell[1].open</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2cell[1].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2cell[1].des</td><td>String</td><td>hunshi2cell[1].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2cell[2].hunshiId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2cell[2].openType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2cell[2].open</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hunshi2cell[2].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunshi2cell[2].des</td><td>String</td><td>hunshi2cell[2].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descirption.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>descirption</td><td>String</td><td>descirption.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class HUNSHI2_PUTON_OPEN_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long playerId;
	Hunshi2Cell[] hunshi2cell;
	String descirption;

	public HUNSHI2_PUTON_OPEN_RES(){
	}

	public HUNSHI2_PUTON_OPEN_RES(long seqNum,long playerId,Hunshi2Cell[] hunshi2cell,String descirption){
		this.seqNum = seqNum;
		this.playerId = playerId;
		this.hunshi2cell = hunshi2cell;
		this.descirption = descirption;
	}

	public HUNSHI2_PUTON_OPEN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		hunshi2cell = new Hunshi2Cell[len];
		for(int i = 0 ; i < hunshi2cell.length ; i++){
			hunshi2cell[i] = new Hunshi2Cell();
			hunshi2cell[i].setHunshiId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			hunshi2cell[i].setOpenType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			hunshi2cell[i].setOpen(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			hunshi2cell[i].setDes(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		descirption = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x70FFF028;
	}

	public String getTypeDescription() {
		return "HUNSHI2_PUTON_OPEN_RES";
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
		len += 8;
		len += 4;
		for(int i = 0 ; i < hunshi2cell.length ; i++){
			len += 8;
			len += 4;
			len += 1;
			len += 2;
			if(hunshi2cell[i].getDes() != null){
				try{
				len += hunshi2cell[i].getDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 2;
		try{
			len +=descirption.getBytes("UTF-8").length;
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

			buffer.putLong(playerId);
			buffer.putInt(hunshi2cell.length);
			for(int i = 0 ; i < hunshi2cell.length ; i++){
				buffer.putLong(hunshi2cell[i].getHunshiId());
				buffer.putInt((int)hunshi2cell[i].getOpenType());
				buffer.put((byte)(hunshi2cell[i].isOpen()==false?0:1));
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = hunshi2cell[i].getDes().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = descirption.getBytes("UTF-8");
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
	 *	被查看的玩家id
	 */
	public long getPlayerId(){
		return playerId;
	}

	/**
	 * 设置属性：
	 *	被查看的玩家id
	 */
	public void setPlayerId(long playerId){
		this.playerId = playerId;
	}

	/**
	 * 获取属性：
	 *	套装魂石材料
	 */
	public Hunshi2Cell[] getHunshi2cell(){
		return hunshi2cell;
	}

	/**
	 * 设置属性：
	 *	套装魂石材料
	 */
	public void setHunshi2cell(Hunshi2Cell[] hunshi2cell){
		this.hunshi2cell = hunshi2cell;
	}

	/**
	 * 获取属性：
	 *	描述
	 */
	public String getDescirption(){
		return descirption;
	}

	/**
	 * 设置属性：
	 *	描述
	 */
	public void setDescirption(String descirption){
		this.descirption = descirption;
	}

}