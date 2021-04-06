package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.client.Shouhun4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 兽魂吞噬<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModel.articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModel.level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModel.currentExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModel.needExp</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModel.attrType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModel.attrNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModel.colorType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouhunModel.icons.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouhunModel.icons</td><td>String</td><td>shouhunModel.icons.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class SHOUHUN_TUNSHI_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	Shouhun4Client shouhunModel;

	public SHOUHUN_TUNSHI_RES(){
	}

	public SHOUHUN_TUNSHI_RES(long seqNum,Shouhun4Client shouhunModel){
		this.seqNum = seqNum;
		this.shouhunModel = shouhunModel;
	}

	public SHOUHUN_TUNSHI_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		shouhunModel = new Shouhun4Client();
		shouhunModel.setArticleId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		shouhunModel.setLevel((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		shouhunModel.setCurrentExp((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		shouhunModel.setNeedExp((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		shouhunModel.setAttrType((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		shouhunModel.setAttrNum((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		shouhunModel.setColorType((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		shouhunModel.setIcons(new String(content,offset,len,"UTF-8"));
		offset += len;
	}

	public int getType() {
		return 0x70FF014;
	}

	public String getTypeDescription() {
		return "SHOUHUN_TUNSHI_RES";
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
		len += 8;
		len += 8;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		if(shouhunModel.getIcons() != null){
			try{
			len += shouhunModel.getIcons().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
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

			buffer.putLong(shouhunModel.getArticleId());
			buffer.putInt((int)shouhunModel.getLevel());
			buffer.putLong(shouhunModel.getCurrentExp());
			buffer.putLong(shouhunModel.getNeedExp());
			buffer.putInt((int)shouhunModel.getAttrType());
			buffer.putInt((int)shouhunModel.getAttrNum());
			buffer.putInt((int)shouhunModel.getColorType());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = shouhunModel.getIcons().getBytes("UTF-8");
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
	 *	无帮助说明
	 */
	public Shouhun4Client getShouhunModel(){
		return shouhunModel;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setShouhunModel(Shouhun4Client shouhunModel){
		this.shouhunModel = shouhunModel;
	}

}