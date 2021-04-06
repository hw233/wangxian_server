package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.client.Shouhun4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 一键替换装备兽魂（最优）<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels.length</td><td>int</td><td>4个字节</td><td>Shouhun4Client数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[0].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[0].currentExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[0].needExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[0].attrType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[0].attrNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[0].colorType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[1].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[1].currentExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[1].needExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[1].attrType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[1].attrNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[1].colorType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[2].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[2].currentExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[2].needExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[2].attrType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModels[2].attrNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModels[2].colorType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class REPLACE_ALL_SHOUHUN_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Shouhun4Client[] shouhunModels;

	public REPLACE_ALL_SHOUHUN_RES(){
	}

	public REPLACE_ALL_SHOUHUN_RES(long seqNum,Shouhun4Client[] shouhunModels){
		this.seqNum = seqNum;
		this.shouhunModels = shouhunModels;
	}

	public REPLACE_ALL_SHOUHUN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		shouhunModels = new Shouhun4Client[len];
		for(int i = 0 ; i < shouhunModels.length ; i++){
			shouhunModels[i] = new Shouhun4Client();
			shouhunModels[i].setArticleId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			shouhunModels[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			shouhunModels[i].setCurrentExp((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			shouhunModels[i].setNeedExp((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			shouhunModels[i].setAttrType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			shouhunModels[i].setAttrNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			shouhunModels[i].setColorType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0102;
	}

	public String getTypeDescription() {
		return "REPLACE_ALL_SHOUHUN_RES";
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
		for(int i = 0 ; i < shouhunModels.length ; i++){
			len += 8;
			len += 4;
			len += 8;
			len += 8;
			len += 4;
			len += 4;
			len += 4;
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

			buffer.putInt(shouhunModels.length);
			for(int i = 0 ; i < shouhunModels.length ; i++){
				buffer.putLong(shouhunModels[i].getArticleId());
				buffer.putInt((int)shouhunModels[i].getLevel());
				buffer.putLong(shouhunModels[i].getCurrentExp());
				buffer.putLong(shouhunModels[i].getNeedExp());
				buffer.putInt((int)shouhunModels[i].getAttrType());
				buffer.putInt((int)shouhunModels[i].getAttrNum());
				buffer.putInt((int)shouhunModels[i].getColorType());
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
	 *	无帮助说明
	 */
	public Shouhun4Client[] getShouhunModels(){
		return shouhunModels;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setShouhunModels(Shouhun4Client[] shouhunModels){
		this.shouhunModels = shouhunModels;
	}

}