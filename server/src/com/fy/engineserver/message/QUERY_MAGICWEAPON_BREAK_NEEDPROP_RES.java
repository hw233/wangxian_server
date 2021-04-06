package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 法宝进阶突破查询<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>magicweaponId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>magicweaponDes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>magicweaponDes</td><td>String</td><td>magicweaponDes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>needAeName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needAeName</td><td>String</td><td>needAeName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>succeedProb</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long magicweaponId;
	String magicweaponDes;
	String needAeName;
	int succeedProb;

	public QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES(){
	}

	public QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES(long seqNum,long magicweaponId,String magicweaponDes,String needAeName,int succeedProb){
		this.seqNum = seqNum;
		this.magicweaponId = magicweaponId;
		this.magicweaponDes = magicweaponDes;
		this.needAeName = needAeName;
		this.succeedProb = succeedProb;
	}

	public QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		magicweaponId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		magicweaponDes = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		needAeName = new String(content,offset,len,"UTF-8");
		offset += len;
		succeedProb = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70FF0147;
	}

	public String getTypeDescription() {
		return "QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES";
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
		len += 2;
		try{
			len +=magicweaponDes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=needAeName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
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

			buffer.putLong(magicweaponId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = magicweaponDes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = needAeName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(succeedProb);
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
	 *	法宝id
	 */
	public long getMagicweaponId(){
		return magicweaponId;
	}

	/**
	 * 设置属性：
	 *	法宝id
	 */
	public void setMagicweaponId(long magicweaponId){
		this.magicweaponId = magicweaponId;
	}

	/**
	 * 获取属性：
	 *	法宝阶信息
	 */
	public String getMagicweaponDes(){
		return magicweaponDes;
	}

	/**
	 * 设置属性：
	 *	法宝阶信息
	 */
	public void setMagicweaponDes(String magicweaponDes){
		this.magicweaponDes = magicweaponDes;
	}

	/**
	 * 获取属性：
	 *	需要的材料名
	 */
	public String getNeedAeName(){
		return needAeName;
	}

	/**
	 * 设置属性：
	 *	需要的材料名
	 */
	public void setNeedAeName(String needAeName){
		this.needAeName = needAeName;
	}

	/**
	 * 获取属性：
	 *	成功率
	 */
	public int getSucceedProb(){
		return succeedProb;
	}

	/**
	 * 设置属性：
	 *	成功率
	 */
	public void setSucceedProb(int succeedProb){
		this.succeedProb = succeedProb;
	}

}