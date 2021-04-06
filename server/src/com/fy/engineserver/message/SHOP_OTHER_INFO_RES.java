package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.shop.client.Goods;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * shop其他信息 跟着SHOP_GET_RES 一起发给客户端<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>marketType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shopName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shopName</td><td>String</td><td>shopName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shopType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods.length</td><td>int</td><td>4个字节</td><td>Goods数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].buyBind</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].otherInfo.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].otherInfo[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].otherInfo[0]</td><td>String</td><td>goods[0].otherInfo[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].otherInfo[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].otherInfo[1]</td><td>String</td><td>goods[0].otherInfo[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].otherInfo[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].otherInfo[2]</td><td>String</td><td>goods[0].otherInfo[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].buyBind</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].otherInfo.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].otherInfo[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].otherInfo[0]</td><td>String</td><td>goods[1].otherInfo[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].otherInfo[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].otherInfo[1]</td><td>String</td><td>goods[1].otherInfo[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].otherInfo[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].otherInfo[2]</td><td>String</td><td>goods[1].otherInfo[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].buyBind</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].otherInfo.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].otherInfo[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].otherInfo[0]</td><td>String</td><td>goods[2].otherInfo[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].otherInfo[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].otherInfo[1]</td><td>String</td><td>goods[2].otherInfo[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].otherInfo[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].otherInfo[2]</td><td>String</td><td>goods[2].otherInfo[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SHOP_OTHER_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte marketType;
	String shopName;
	byte shopType;
	Goods[] goods;

	public SHOP_OTHER_INFO_RES(){
	}

	public SHOP_OTHER_INFO_RES(long seqNum,byte marketType,String shopName,byte shopType,Goods[] goods){
		this.seqNum = seqNum;
		this.marketType = marketType;
		this.shopName = shopName;
		this.shopType = shopType;
		this.goods = goods;
	}

	public SHOP_OTHER_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		marketType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		shopName = new String(content,offset,len,"UTF-8");
		offset += len;
		shopType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		goods = new Goods[len];
		for(int i = 0 ; i < goods.length ; i++){
			goods[i] = new Goods();
			goods[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			goods[i].setBuyBind(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] otherInfo_0001 = new String[len];
			for(int j = 0 ; j < otherInfo_0001.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				otherInfo_0001[j] = new String(content,offset,len);
				offset += len;
			}
			goods[i].setOtherInfo(otherInfo_0001);
		}
	}

	public int getType() {
		return 0x7000EA05;
	}

	public String getTypeDescription() {
		return "SHOP_OTHER_INFO_RES";
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
		len += 2;
		try{
			len +=shopName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 4;
		for(int i = 0 ; i < goods.length ; i++){
			len += 4;
			len += 1;
			len += 4;
			String[] otherInfo = goods[i].getOtherInfo();
			for(int j = 0 ; j < otherInfo.length; j++){
				len += 2;
				len += otherInfo[j].getBytes().length;
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

			buffer.put(marketType);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = shopName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(shopType);
			buffer.putInt(goods.length);
			for(int i = 0 ; i < goods.length ; i++){
				buffer.putInt((int)goods[i].getId());
				buffer.put((byte)(goods[i].isBuyBind()==false?0:1));
				buffer.putInt(goods[i].getOtherInfo().length);
				String[] otherInfo_0002 = goods[i].getOtherInfo();
				for(int j = 0 ; j < otherInfo_0002.length ; j++){
					buffer.putShort((short)otherInfo_0002[j].getBytes().length);
					buffer.put(otherInfo_0002[j].getBytes());
				}
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
	 *	商城的类型，1为元宝商城
	 */
	public byte getMarketType(){
		return marketType;
	}

	/**
	 * 设置属性：
	 *	商城的类型，1为元宝商城
	 */
	public void setMarketType(byte marketType){
		this.marketType = marketType;
	}

	/**
	 * 获取属性：
	 *	商店名字
	 */
	public String getShopName(){
		return shopName;
	}

	/**
	 * 设置属性：
	 *	商店名字
	 */
	public void setShopName(String shopName){
		this.shopName = shopName;
	}

	/**
	 * 获取属性：
	 *	商店类型，策划需要严格按照类型统一原则定义，一个商店卖的东西只能是一种货币类型购买，客户端根据商店类型来显示玩家拥有数
	 */
	public byte getShopType(){
		return shopType;
	}

	/**
	 * 设置属性：
	 *	商店类型，策划需要严格按照类型统一原则定义，一个商店卖的东西只能是一种货币类型购买，客户端根据商店类型来显示玩家拥有数
	 */
	public void setShopType(byte shopType){
		this.shopType = shopType;
	}

	/**
	 * 获取属性：
	 *	商店中的商品
	 */
	public Goods[] getGoods(){
		return goods;
	}

	/**
	 * 设置属性：
	 *	商店中的商品
	 */
	public void setGoods(Goods[] goods){
		this.goods = goods;
	}

}