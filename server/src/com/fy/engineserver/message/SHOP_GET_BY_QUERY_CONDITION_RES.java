package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.shop.client.Goods;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 根据条件查询到满足条件的商品组装成一个商店<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>marketType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shopName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shopName</td><td>String</td><td>shopName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>version</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shopType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>coins.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>coins</td><td>long[]</td><td>coins.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods.length</td><td>int</td><td>4个字节</td><td>Goods数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].articleName</td><td>String</td><td>goods[0].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].bundleNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].pType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].oldPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].littleSellIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].littleSellIcon</td><td>String</td><td>goods[0].littleSellIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].exchangeArticleNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].exchangeArticleNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].exchangeArticleNames[0]</td><td>String</td><td>goods[0].exchangeArticleNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].exchangeArticleNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].exchangeArticleNames[1]</td><td>String</td><td>goods[0].exchangeArticleNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].exchangeArticleNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].exchangeArticleNames[2]</td><td>String</td><td>goods[0].exchangeArticleNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].exchangeArticleNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].exchangeArticleNums</td><td>int[]</td><td>goods[0].exchangeArticleNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].reputeLimit</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].description</td><td>String</td><td>goods[0].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].exchangeArticleDescription.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].exchangeArticleDescription</td><td>String</td><td>goods[0].exchangeArticleDescription.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].articleName</td><td>String</td><td>goods[1].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].bundleNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].pType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].oldPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].littleSellIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].littleSellIcon</td><td>String</td><td>goods[1].littleSellIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleNames[0]</td><td>String</td><td>goods[1].exchangeArticleNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleNames[1]</td><td>String</td><td>goods[1].exchangeArticleNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleNames[2]</td><td>String</td><td>goods[1].exchangeArticleNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleNums</td><td>int[]</td><td>goods[1].exchangeArticleNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].reputeLimit</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].description</td><td>String</td><td>goods[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleDescription.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleDescription</td><td>String</td><td>goods[1].exchangeArticleDescription.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].articleName</td><td>String</td><td>goods[2].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].bundleNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].pType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].oldPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].littleSellIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].littleSellIcon</td><td>String</td><td>goods[2].littleSellIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].exchangeArticleNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].exchangeArticleNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].exchangeArticleNames[0]</td><td>String</td><td>goods[2].exchangeArticleNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].exchangeArticleNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].exchangeArticleNames[1]</td><td>String</td><td>goods[2].exchangeArticleNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].exchangeArticleNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].exchangeArticleNames[2]</td><td>String</td><td>goods[2].exchangeArticleNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].exchangeArticleNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].exchangeArticleNums</td><td>int[]</td><td>goods[2].exchangeArticleNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].reputeLimit</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].description</td><td>String</td><td>goods[2].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].exchangeArticleDescription.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].exchangeArticleDescription</td><td>String</td><td>goods[2].exchangeArticleDescription.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class SHOP_GET_BY_QUERY_CONDITION_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte marketType;
	String shopName;
	int version;
	byte shopType;
	byte equipmentType;
	long[] coins;
	Goods[] goods;

	public SHOP_GET_BY_QUERY_CONDITION_RES(){
	}

	public SHOP_GET_BY_QUERY_CONDITION_RES(long seqNum,byte marketType,String shopName,int version,byte shopType,byte equipmentType,long[] coins,Goods[] goods){
		this.seqNum = seqNum;
		this.marketType = marketType;
		this.shopName = shopName;
		this.version = version;
		this.shopType = shopType;
		this.equipmentType = equipmentType;
		this.coins = coins;
		this.goods = goods;
	}

	public SHOP_GET_BY_QUERY_CONDITION_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		marketType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		shopName = new String(content,offset,len,"UTF-8");
		offset += len;
		version = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		shopType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		equipmentType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		coins = new long[len];
		for(int i = 0 ; i < coins.length ; i++){
			coins[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		goods = new Goods[len];
		for(int i = 0 ; i < goods.length ; i++){
			goods[i] = new Goods();
			goods[i].setArticleId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			goods[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			goods[i].setArticleName(new String(content,offset,len,"UTF-8"));
			offset += len;
			goods[i].setLevel((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			goods[i].setColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			goods[i].setBundleNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			goods[i].setPType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			goods[i].setOldPrice((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			goods[i].setPrice((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			goods[i].setLittleSellIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] exchangeArticleNames_0001 = new String[len];
			for(int j = 0 ; j < exchangeArticleNames_0001.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				exchangeArticleNames_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			goods[i].setExchangeArticleNames(exchangeArticleNames_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] exchangeArticleNums_0002 = new int[len];
			for(int j = 0 ; j < exchangeArticleNums_0002.length ; j++){
				exchangeArticleNums_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			goods[i].setExchangeArticleNums(exchangeArticleNums_0002);
			goods[i].setReputeLimit(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			goods[i].setDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			goods[i].setExchangeArticleDescription(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x7000EA12;
	}

	public String getTypeDescription() {
		return "SHOP_GET_BY_QUERY_CONDITION_RES";
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
		len += 4;
		len += 1;
		len += 1;
		len += 4;
		len += coins.length * 8;
		len += 4;
		for(int i = 0 ; i < goods.length ; i++){
			len += 8;
			len += 4;
			len += 2;
			if(goods[i].getArticleName() != null){
				try{
				len += goods[i].getArticleName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(goods[i].getLittleSellIcon() != null){
				try{
				len += goods[i].getLittleSellIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] exchangeArticleNames = goods[i].getExchangeArticleNames();
			for(int j = 0 ; j < exchangeArticleNames.length; j++){
				len += 2;
				try{
					len += exchangeArticleNames[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += goods[i].getExchangeArticleNums().length * 4;
			len += 1;
			len += 2;
			if(goods[i].getDescription() != null){
				try{
				len += goods[i].getDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(goods[i].getExchangeArticleDescription() != null){
				try{
				len += goods[i].getExchangeArticleDescription().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
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
			buffer.putInt(version);
			buffer.put(shopType);
			buffer.put(equipmentType);
			buffer.putInt(coins.length);
			for(int i = 0 ; i < coins.length; i++){
				buffer.putLong(coins[i]);
			}
			buffer.putInt(goods.length);
			for(int i = 0 ; i < goods.length ; i++){
				buffer.putLong(goods[i].getArticleId());
				buffer.putInt((int)goods[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = goods[i].getArticleName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)goods[i].getLevel());
				buffer.putInt((int)goods[i].getColor());
				buffer.putInt((int)goods[i].getBundleNum());
				buffer.putInt((int)goods[i].getPType());
				buffer.putInt((int)goods[i].getOldPrice());
				buffer.putInt((int)goods[i].getPrice());
				try{
				tmpBytes2 = goods[i].getLittleSellIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(goods[i].getExchangeArticleNames().length);
				String[] exchangeArticleNames_0003 = goods[i].getExchangeArticleNames();
				for(int j = 0 ; j < exchangeArticleNames_0003.length ; j++){
				try{
					buffer.putShort((short)exchangeArticleNames_0003[j].getBytes("UTF-8").length);
					buffer.put(exchangeArticleNames_0003[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt(goods[i].getExchangeArticleNums().length);
				int[] exchangeArticleNums_0004 = goods[i].getExchangeArticleNums();
				for(int j = 0 ; j < exchangeArticleNums_0004.length ; j++){
					buffer.putInt(exchangeArticleNums_0004[j]);
				}
				buffer.put((byte)(goods[i].isReputeLimit()==false?0:1));
				try{
				tmpBytes2 = goods[i].getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = goods[i].getExchangeArticleDescription().getBytes("UTF-8");
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
	 *	商店的版本
	 */
	public int getVersion(){
		return version;
	}

	/**
	 * 设置属性：
	 *	商店的版本
	 */
	public void setVersion(int version){
		this.version = version;
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
	 *	装备类型
	 */
	public byte getEquipmentType(){
		return equipmentType;
	}

	/**
	 * 设置属性：
	 *	装备类型
	 */
	public void setEquipmentType(byte equipmentType){
		this.equipmentType = equipmentType;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public long[] getCoins(){
		return coins;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setCoins(long[] coins){
		this.coins = coins;
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