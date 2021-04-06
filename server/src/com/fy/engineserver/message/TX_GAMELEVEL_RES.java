package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 腾讯魔钻信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tx_gamelevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>agio</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>iconPng.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>iconPng</td><td>String</td><td>iconPng.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class TX_GAMELEVEL_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int tx_gamelevel;
	int agio;
	String iconPng;

	public TX_GAMELEVEL_RES(){
	}

	public TX_GAMELEVEL_RES(long seqNum,int tx_gamelevel,int agio,String iconPng){
		this.seqNum = seqNum;
		this.tx_gamelevel = tx_gamelevel;
		this.agio = agio;
		this.iconPng = iconPng;
	}

	public TX_GAMELEVEL_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		tx_gamelevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		agio = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		iconPng = new String(content,offset,len);
		offset += len;
	}

	public int getType() {
		return 0x7000EC31;
	}

	public String getTypeDescription() {
		return "TX_GAMELEVEL_RES";
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
		len += 2;
		len +=iconPng.getBytes().length;
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

			buffer.putInt(tx_gamelevel);
			buffer.putInt(agio);
			byte[] tmpBytes1;
			tmpBytes1 = iconPng.getBytes();
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
	 *	腾讯魔钻是否开通
	 */
	public int getTx_gamelevel(){
		return tx_gamelevel;
	}

	/**
	 * 设置属性：
	 *	腾讯魔钻是否开通
	 */
	public void setTx_gamelevel(int tx_gamelevel){
		this.tx_gamelevel = tx_gamelevel;
	}

	/**
	 * 获取属性：
	 *	打折比例 100为不打折
	 */
	public int getAgio(){
		return agio;
	}

	/**
	 * 设置属性：
	 *	打折比例 100为不打折
	 */
	public void setAgio(int agio){
		this.agio = agio;
	}

	/**
	 * 获取属性：
	 *	打折图片名字
	 */
	public String getIconPng(){
		return iconPng;
	}

	/**
	 * 设置属性：
	 *	打折图片名字
	 */
	public void setIconPng(String iconPng){
		this.iconPng = iconPng;
	}

}