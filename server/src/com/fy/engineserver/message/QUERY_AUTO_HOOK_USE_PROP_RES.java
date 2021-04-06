package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询自动挂机可使用道具列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shopName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shopName</td><td>String</td><td>shopName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shopVersion</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerHpGoodsId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPlayerHpGoodsId</td><td>int[]</td><td>propPlayerHpGoodsId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerHpName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPlayerHpName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerHpName[0]</td><td>String</td><td>propPlayerHpName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPlayerHpName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerHpName[1]</td><td>String</td><td>propPlayerHpName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPlayerHpName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerHpName[2]</td><td>String</td><td>propPlayerHpName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerMpGoodsId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPlayerMpGoodsId</td><td>int[]</td><td>propPlayerMpGoodsId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerMpName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPlayerMpName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerMpName[0]</td><td>String</td><td>propPlayerMpName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPlayerMpName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerMpName[1]</td><td>String</td><td>propPlayerMpName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPlayerMpName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPlayerMpName[2]</td><td>String</td><td>propPlayerMpName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHpGoodsId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetHpGoodsId</td><td>int[]</td><td>propPetHpGoodsId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHpName.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetHpName[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHpName[0]</td><td>String</td><td>propPetHpName[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetHpName[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHpName[1]</td><td>String</td><td>propPetHpName[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetHpName[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHpName[2]</td><td>String</td><td>propPetHpName[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHappyGoodsId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetHappyGoodsId</td><td>int[]</td><td>propPetHappyGoodsId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHappy.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetHappy[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHappy[0]</td><td>String</td><td>propPetHappy[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetHappy[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHappy[1]</td><td>String</td><td>propPetHappy[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetHappy[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetHappy[2]</td><td>String</td><td>propPetHappy[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetLifeGoodsId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetLifeGoodsId</td><td>int[]</td><td>propPetLifeGoodsId.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetLife.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetLife[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetLife[0]</td><td>String</td><td>propPetLife[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetLife[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetLife[1]</td><td>String</td><td>propPetLife[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propPetLife[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propPetLife[2]</td><td>String</td><td>propPetLife[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_AUTO_HOOK_USE_PROP_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String shopName;
	int shopVersion;
	int[] propPlayerHpGoodsId;
	String[] propPlayerHpName;
	int[] propPlayerMpGoodsId;
	String[] propPlayerMpName;
	int[] propPetHpGoodsId;
	String[] propPetHpName;
	int[] propPetHappyGoodsId;
	String[] propPetHappy;
	int[] propPetLifeGoodsId;
	String[] propPetLife;

	public QUERY_AUTO_HOOK_USE_PROP_RES(){
	}

	public QUERY_AUTO_HOOK_USE_PROP_RES(long seqNum,String shopName,int shopVersion,int[] propPlayerHpGoodsId,String[] propPlayerHpName,int[] propPlayerMpGoodsId,String[] propPlayerMpName,int[] propPetHpGoodsId,String[] propPetHpName,int[] propPetHappyGoodsId,String[] propPetHappy,int[] propPetLifeGoodsId,String[] propPetLife){
		this.seqNum = seqNum;
		this.shopName = shopName;
		this.shopVersion = shopVersion;
		this.propPlayerHpGoodsId = propPlayerHpGoodsId;
		this.propPlayerHpName = propPlayerHpName;
		this.propPlayerMpGoodsId = propPlayerMpGoodsId;
		this.propPlayerMpName = propPlayerMpName;
		this.propPetHpGoodsId = propPetHpGoodsId;
		this.propPetHpName = propPetHpName;
		this.propPetHappyGoodsId = propPetHappyGoodsId;
		this.propPetHappy = propPetHappy;
		this.propPetLifeGoodsId = propPetLifeGoodsId;
		this.propPetLife = propPetLife;
	}

	public QUERY_AUTO_HOOK_USE_PROP_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		shopName = new String(content,offset,len,"UTF-8");
		offset += len;
		shopVersion = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPlayerHpGoodsId = new int[len];
		for(int i = 0 ; i < propPlayerHpGoodsId.length ; i++){
			propPlayerHpGoodsId[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPlayerHpName = new String[len];
		for(int i = 0 ; i < propPlayerHpName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propPlayerHpName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPlayerMpGoodsId = new int[len];
		for(int i = 0 ; i < propPlayerMpGoodsId.length ; i++){
			propPlayerMpGoodsId[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPlayerMpName = new String[len];
		for(int i = 0 ; i < propPlayerMpName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propPlayerMpName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPetHpGoodsId = new int[len];
		for(int i = 0 ; i < propPetHpGoodsId.length ; i++){
			propPetHpGoodsId[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPetHpName = new String[len];
		for(int i = 0 ; i < propPetHpName.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propPetHpName[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPetHappyGoodsId = new int[len];
		for(int i = 0 ; i < propPetHappyGoodsId.length ; i++){
			propPetHappyGoodsId[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPetHappy = new String[len];
		for(int i = 0 ; i < propPetHappy.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propPetHappy[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPetLifeGoodsId = new int[len];
		for(int i = 0 ; i < propPetLifeGoodsId.length ; i++){
			propPetLifeGoodsId[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		propPetLife = new String[len];
		for(int i = 0 ; i < propPetLife.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propPetLife[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x70000FA8;
	}

	public String getTypeDescription() {
		return "QUERY_AUTO_HOOK_USE_PROP_RES";
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
			len +=shopName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += propPlayerHpGoodsId.length * 4;
		len += 4;
		for(int i = 0 ; i < propPlayerHpName.length; i++){
			len += 2;
			try{
				len += propPlayerHpName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += propPlayerMpGoodsId.length * 4;
		len += 4;
		for(int i = 0 ; i < propPlayerMpName.length; i++){
			len += 2;
			try{
				len += propPlayerMpName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += propPetHpGoodsId.length * 4;
		len += 4;
		for(int i = 0 ; i < propPetHpName.length; i++){
			len += 2;
			try{
				len += propPetHpName[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += propPetHappyGoodsId.length * 4;
		len += 4;
		for(int i = 0 ; i < propPetHappy.length; i++){
			len += 2;
			try{
				len += propPetHappy[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += propPetLifeGoodsId.length * 4;
		len += 4;
		for(int i = 0 ; i < propPetLife.length; i++){
			len += 2;
			try{
				len += propPetLife[i].getBytes("UTF-8").length;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = shopName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(shopVersion);
			buffer.putInt(propPlayerHpGoodsId.length);
			for(int i = 0 ; i < propPlayerHpGoodsId.length; i++){
				buffer.putInt(propPlayerHpGoodsId[i]);
			}
			buffer.putInt(propPlayerHpName.length);
			for(int i = 0 ; i < propPlayerHpName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = propPlayerHpName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(propPlayerMpGoodsId.length);
			for(int i = 0 ; i < propPlayerMpGoodsId.length; i++){
				buffer.putInt(propPlayerMpGoodsId[i]);
			}
			buffer.putInt(propPlayerMpName.length);
			for(int i = 0 ; i < propPlayerMpName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = propPlayerMpName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(propPetHpGoodsId.length);
			for(int i = 0 ; i < propPetHpGoodsId.length; i++){
				buffer.putInt(propPetHpGoodsId[i]);
			}
			buffer.putInt(propPetHpName.length);
			for(int i = 0 ; i < propPetHpName.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = propPetHpName[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(propPetHappyGoodsId.length);
			for(int i = 0 ; i < propPetHappyGoodsId.length; i++){
				buffer.putInt(propPetHappyGoodsId[i]);
			}
			buffer.putInt(propPetHappy.length);
			for(int i = 0 ; i < propPetHappy.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = propPetHappy[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(propPetLifeGoodsId.length);
			for(int i = 0 ; i < propPetLifeGoodsId.length; i++){
				buffer.putInt(propPetLifeGoodsId[i]);
			}
			buffer.putInt(propPetLife.length);
			for(int i = 0 ; i < propPetLife.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = propPetLife[i].getBytes("UTF-8");
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
	 *	商店名称
	 */
	public String getShopName(){
		return shopName;
	}

	/**
	 * 设置属性：
	 *	商店名称
	 */
	public void setShopName(String shopName){
		this.shopName = shopName;
	}

	/**
	 * 获取属性：
	 *	商店版本
	 */
	public int getShopVersion(){
		return shopVersion;
	}

	/**
	 * 设置属性：
	 *	商店版本
	 */
	public void setShopVersion(int shopVersion){
		this.shopVersion = shopVersion;
	}

	/**
	 * 获取属性：
	 *	血药商品ID
	 */
	public int[] getPropPlayerHpGoodsId(){
		return propPlayerHpGoodsId;
	}

	/**
	 * 设置属性：
	 *	血药商品ID
	 */
	public void setPropPlayerHpGoodsId(int[] propPlayerHpGoodsId){
		this.propPlayerHpGoodsId = propPlayerHpGoodsId;
	}

	/**
	 * 获取属性：
	 *	血药名称
	 */
	public String[] getPropPlayerHpName(){
		return propPlayerHpName;
	}

	/**
	 * 设置属性：
	 *	血药名称
	 */
	public void setPropPlayerHpName(String[] propPlayerHpName){
		this.propPlayerHpName = propPlayerHpName;
	}

	/**
	 * 获取属性：
	 *	蓝药商品ID
	 */
	public int[] getPropPlayerMpGoodsId(){
		return propPlayerMpGoodsId;
	}

	/**
	 * 设置属性：
	 *	蓝药商品ID
	 */
	public void setPropPlayerMpGoodsId(int[] propPlayerMpGoodsId){
		this.propPlayerMpGoodsId = propPlayerMpGoodsId;
	}

	/**
	 * 获取属性：
	 *	蓝药名称
	 */
	public String[] getPropPlayerMpName(){
		return propPlayerMpName;
	}

	/**
	 * 设置属性：
	 *	蓝药名称
	 */
	public void setPropPlayerMpName(String[] propPlayerMpName){
		this.propPlayerMpName = propPlayerMpName;
	}

	/**
	 * 获取属性：
	 *	宠物血药商品ID
	 */
	public int[] getPropPetHpGoodsId(){
		return propPetHpGoodsId;
	}

	/**
	 * 设置属性：
	 *	宠物血药商品ID
	 */
	public void setPropPetHpGoodsId(int[] propPetHpGoodsId){
		this.propPetHpGoodsId = propPetHpGoodsId;
	}

	/**
	 * 获取属性：
	 *	宠物血药名称
	 */
	public String[] getPropPetHpName(){
		return propPetHpName;
	}

	/**
	 * 设置属性：
	 *	宠物血药名称
	 */
	public void setPropPetHpName(String[] propPetHpName){
		this.propPetHpName = propPetHpName;
	}

	/**
	 * 获取属性：
	 *	宠物快乐度药品商品ID
	 */
	public int[] getPropPetHappyGoodsId(){
		return propPetHappyGoodsId;
	}

	/**
	 * 设置属性：
	 *	宠物快乐度药品商品ID
	 */
	public void setPropPetHappyGoodsId(int[] propPetHappyGoodsId){
		this.propPetHappyGoodsId = propPetHappyGoodsId;
	}

	/**
	 * 获取属性：
	 *	宠物快乐度药品名称
	 */
	public String[] getPropPetHappy(){
		return propPetHappy;
	}

	/**
	 * 设置属性：
	 *	宠物快乐度药品名称
	 */
	public void setPropPetHappy(String[] propPetHappy){
		this.propPetHappy = propPetHappy;
	}

	/**
	 * 获取属性：
	 *	宠物寿命药品商品ID
	 */
	public int[] getPropPetLifeGoodsId(){
		return propPetLifeGoodsId;
	}

	/**
	 * 设置属性：
	 *	宠物寿命药品商品ID
	 */
	public void setPropPetLifeGoodsId(int[] propPetLifeGoodsId){
		this.propPetLifeGoodsId = propPetLifeGoodsId;
	}

	/**
	 * 获取属性：
	 *	宠物寿命药品名称
	 */
	public String[] getPropPetLife(){
		return propPetLife;
	}

	/**
	 * 设置属性：
	 *	宠物寿命药品名称
	 */
	public void setPropPetLife(String[] propPetLife){
		this.propPetLife = propPetLife;
	}

}