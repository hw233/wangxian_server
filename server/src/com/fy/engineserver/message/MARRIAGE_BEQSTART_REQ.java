package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 求婚初始化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sextype</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>Entityid.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>Entityid</td><td>long[]</td><td>Entityid.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name[0]</td><td>String</td><td>name[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name[1]</td><td>String</td><td>name[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name[2]</td><td>String</td><td>name[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>flowerNum.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>flowerNum</td><td>int[]</td><td>flowerNum.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>haveFlower.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>haveFlower</td><td>int[]</td><td>haveFlower.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>flowerMoney.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>flowerMoney</td><td>long[]</td><td>flowerMoney.length</td><td>*</td></tr>
 * </table>
 */
public class MARRIAGE_BEQSTART_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte sextype;
	long[] Entityid;
	String[] name;
	int[] flowerNum;
	int[] haveFlower;
	long[] flowerMoney;

	public MARRIAGE_BEQSTART_REQ(){
	}

	public MARRIAGE_BEQSTART_REQ(long seqNum,byte sextype,long[] Entityid,String[] name,int[] flowerNum,int[] haveFlower,long[] flowerMoney){
		this.seqNum = seqNum;
		this.sextype = sextype;
		this.Entityid = Entityid;
		this.name = name;
		this.flowerNum = flowerNum;
		this.haveFlower = haveFlower;
		this.flowerMoney = flowerMoney;
	}

	public MARRIAGE_BEQSTART_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		sextype = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		Entityid = new long[len];
		for(int i = 0 ; i < Entityid.length ; i++){
			Entityid[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		name = new String[len];
		for(int i = 0 ; i < name.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			name[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		flowerNum = new int[len];
		for(int i = 0 ; i < flowerNum.length ; i++){
			flowerNum[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		haveFlower = new int[len];
		for(int i = 0 ; i < haveFlower.length ; i++){
			haveFlower[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		flowerMoney = new long[len];
		for(int i = 0 ; i < flowerMoney.length ; i++){
			flowerMoney[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x00010002;
	}

	public String getTypeDescription() {
		return "MARRIAGE_BEQSTART_REQ";
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
		len += 4;
		len += Entityid.length * 8;
		len += 4;
		for(int i = 0 ; i < name.length; i++){
			len += 2;
			try{
				len += name[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += flowerNum.length * 4;
		len += 4;
		len += haveFlower.length * 4;
		len += 4;
		len += flowerMoney.length * 8;
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

			buffer.put(sextype);
			buffer.putInt(Entityid.length);
			for(int i = 0 ; i < Entityid.length; i++){
				buffer.putLong(Entityid[i]);
			}
			buffer.putInt(name.length);
			for(int i = 0 ; i < name.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = name[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(flowerNum.length);
			for(int i = 0 ; i < flowerNum.length; i++){
				buffer.putInt(flowerNum[i]);
			}
			buffer.putInt(haveFlower.length);
			for(int i = 0 ; i < haveFlower.length; i++){
				buffer.putInt(haveFlower[i]);
			}
			buffer.putInt(flowerMoney.length);
			for(int i = 0 ; i < flowerMoney.length; i++){
				buffer.putLong(flowerMoney[i]);
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
	 *	0是花，1是糖
	 */
	public byte getSextype(){
		return sextype;
	}

	/**
	 * 设置属性：
	 *	0是花，1是糖
	 */
	public void setSextype(byte sextype){
		this.sextype = sextype;
	}

	/**
	 * 获取属性：
	 *	对应的物品EntityID,零时生成
	 */
	public long[] getEntityid(){
		return Entityid;
	}

	/**
	 * 设置属性：
	 *	对应的物品EntityID,零时生成
	 */
	public void setEntityid(long[] Entityid){
		this.Entityid = Entityid;
	}

	/**
	 * 获取属性：
	 *	对应得具体物品名称
	 */
	public String[] getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	对应得具体物品名称
	 */
	public void setName(String[] name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	可选花的个数
	 */
	public int[] getFlowerNum(){
		return flowerNum;
	}

	/**
	 * 设置属性：
	 *	可选花的个数
	 */
	public void setFlowerNum(int[] flowerNum){
		this.flowerNum = flowerNum;
	}

	/**
	 * 获取属性：
	 *	包裹已有
	 */
	public int[] getHaveFlower(){
		return haveFlower;
	}

	/**
	 * 设置属性：
	 *	包裹已有
	 */
	public void setHaveFlower(int[] haveFlower){
		this.haveFlower = haveFlower;
	}

	/**
	 * 获取属性：
	 *	花的单价
	 */
	public long[] getFlowerMoney(){
		return flowerMoney;
	}

	/**
	 * 设置属性：
	 *	花的单价
	 */
	public void setFlowerMoney(long[] flowerMoney){
		this.flowerMoney = flowerMoney;
	}

}