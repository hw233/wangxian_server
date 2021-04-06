package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开锁定界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleIds</td><td>long[]</td><td>articleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleTypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleTypes</td><td>int[]</td><td>articleTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>enchantStatus.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>enchantStatus</td><td>int[]</td><td>enchantStatus.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description1.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description1[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description1[0]</td><td>String</td><td>description1[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description1[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description1[1]</td><td>String</td><td>description1[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description1[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description1[2]</td><td>String</td><td>description1[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description2[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description2[0]</td><td>String</td><td>description2[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description2[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description2[1]</td><td>String</td><td>description2[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>description2[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>description2[2]</td><td>String</td><td>description2[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class OPEN_ENCHANT_LOCK_WINDOW_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] articleIds;
	int[] articleTypes;
	int[] enchantStatus;
	String[] description1;
	String[] description2;

	public OPEN_ENCHANT_LOCK_WINDOW_RES(){
	}

	public OPEN_ENCHANT_LOCK_WINDOW_RES(long seqNum,long[] articleIds,int[] articleTypes,int[] enchantStatus,String[] description1,String[] description2){
		this.seqNum = seqNum;
		this.articleIds = articleIds;
		this.articleTypes = articleTypes;
		this.enchantStatus = enchantStatus;
		this.description1 = description1;
		this.description2 = description2;
	}

	public OPEN_ENCHANT_LOCK_WINDOW_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleIds = new long[len];
		for(int i = 0 ; i < articleIds.length ; i++){
			articleIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		articleTypes = new int[len];
		for(int i = 0 ; i < articleTypes.length ; i++){
			articleTypes[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		enchantStatus = new int[len];
		for(int i = 0 ; i < enchantStatus.length ; i++){
			enchantStatus[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		description1 = new String[len];
		for(int i = 0 ; i < description1.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			description1[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		description2 = new String[len];
		for(int i = 0 ; i < description2.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			description2[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x70FF0094;
	}

	public String getTypeDescription() {
		return "OPEN_ENCHANT_LOCK_WINDOW_RES";
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
		len += articleIds.length * 8;
		len += 4;
		len += articleTypes.length * 4;
		len += 4;
		len += enchantStatus.length * 4;
		len += 4;
		for(int i = 0 ; i < description1.length; i++){
			len += 2;
			try{
				len += description1[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < description2.length; i++){
			len += 2;
			try{
				len += description2[i].getBytes("UTF-8").length;
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

			buffer.putInt(articleIds.length);
			for(int i = 0 ; i < articleIds.length; i++){
				buffer.putLong(articleIds[i]);
			}
			buffer.putInt(articleTypes.length);
			for(int i = 0 ; i < articleTypes.length; i++){
				buffer.putInt(articleTypes[i]);
			}
			buffer.putInt(enchantStatus.length);
			for(int i = 0 ; i < enchantStatus.length; i++){
				buffer.putInt(enchantStatus[i]);
			}
			buffer.putInt(description1.length);
			for(int i = 0 ; i < description1.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = description1[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(description2.length);
			for(int i = 0 ; i < description2.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = description2[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	玩家身上可以附魔的装备id列表(-1代表没装备)
	 */
	public long[] getArticleIds(){
		return articleIds;
	}

	/**
	 * 设置属性：
	 *	玩家身上可以附魔的装备id列表(-1代表没装备)
	 */
	public void setArticleIds(long[] articleIds){
		this.articleIds = articleIds;
	}

	/**
	 * 获取属性：
	 *	对应装备类型
	 */
	public int[] getArticleTypes(){
		return articleTypes;
	}

	/**
	 * 设置属性：
	 *	对应装备类型
	 */
	public void setArticleTypes(int[] articleTypes){
		this.articleTypes = articleTypes;
	}

	/**
	 * 获取属性：
	 *	附魔类型  0未附魔  1有附魔 2有附魔已锁定
	 */
	public int[] getEnchantStatus(){
		return enchantStatus;
	}

	/**
	 * 设置属性：
	 *	附魔类型  0未附魔  1有附魔 2有附魔已锁定
	 */
	public void setEnchantStatus(int[] enchantStatus){
		this.enchantStatus = enchantStatus;
	}

	/**
	 * 获取属性：
	 *	附魔名称
	 */
	public String[] getDescription1(){
		return description1;
	}

	/**
	 * 设置属性：
	 *	附魔名称
	 */
	public void setDescription1(String[] description1){
		this.description1 = description1;
	}

	/**
	 * 获取属性：
	 *	耐久度
	 */
	public String[] getDescription2(){
		return description2;
	}

	/**
	 * 设置属性：
	 *	耐久度
	 */
	public void setDescription2(String[] description2){
		this.description2 = description2;
	}

}