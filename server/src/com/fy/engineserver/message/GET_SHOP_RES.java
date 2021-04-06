package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.shop.client.Goods;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 商店首界面<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shopName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shopName</td><td>String</td><td>shopName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isOpen</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>starttime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>point</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activity</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allShopNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allShopNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allShopNames[0]</td><td>String</td><td>allShopNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allShopNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allShopNames[1]</td><td>String</td><td>allShopNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>allShopNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allShopNames[2]</td><td>String</td><td>allShopNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
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
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].overNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].goodEndTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].serverNumlimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].playerNumlimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].goodMaxNumLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[0].littleSellIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[0].littleSellIcon</td><td>String</td><td>goods[0].littleSellIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].articleId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].articleName</td><td>String</td><td>goods[1].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].bundleNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].pType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].oldPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].price</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].littleSellIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].littleSellIcon</td><td>String</td><td>goods[1].littleSellIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleNames.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleNames[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleNames[0]</td><td>String</td><td>goods[1].exchangeArticleNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleNames[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleNames[1]</td><td>String</td><td>goods[1].exchangeArticleNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleNames[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleNames[2]</td><td>String</td><td>goods[1].exchangeArticleNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleNums</td><td>int[]</td><td>goods[1].exchangeArticleNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].reputeLimit</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].description</td><td>String</td><td>goods[1].description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].exchangeArticleDescription.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].exchangeArticleDescription</td><td>String</td><td>goods[1].exchangeArticleDescription.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].overNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].goodEndTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].serverNumlimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].playerNumlimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].goodMaxNumLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[1].littleSellIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[1].littleSellIcon</td><td>String</td><td>goods[1].littleSellIcon.length</td><td>字符串对应的byte数组</td></tr>
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
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].overNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].goodEndTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].serverNumlimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].playerNumlimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].goodMaxNumLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goods[2].littleSellIcon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>goods[2].littleSellIcon</td><td>String</td><td>goods[2].littleSellIcon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class GET_SHOP_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String shopName;
	boolean isOpen;
	long starttime;
	long point;
	long activity;
	String[] allShopNames;
	Goods[] goods;

	public GET_SHOP_RES(){
	}

	public GET_SHOP_RES(long seqNum,String shopName,boolean isOpen,long starttime,long point,long activity,String[] allShopNames,Goods[] goods){
		this.seqNum = seqNum;
		this.shopName = shopName;
		this.isOpen = isOpen;
		this.starttime = starttime;
		this.point = point;
		this.activity = activity;
		this.allShopNames = allShopNames;
		this.goods = goods;
	}

	public GET_SHOP_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		shopName = new String(content,offset,len,"UTF-8");
		offset += len;
		isOpen = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		starttime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		point = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		activity = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		allShopNames = new String[len];
		for(int i = 0 ; i < allShopNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			allShopNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
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
			goods[i].setOverNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			goods[i].setGoodEndTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			goods[i].setServerNumlimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			goods[i].setPlayerNumlimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			goods[i].setGoodMaxNumLimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			goods[i].setLittleSellIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x8E0EAA38;
	}

	public String getTypeDescription() {
		return "GET_SHOP_RES";
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
		len += 1;
		len += 8;
		len += 8;
		len += 8;
		len += 4;
		for(int i = 0 ; i < allShopNames.length; i++){
			len += 2;
			try{
				len += allShopNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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
			len += 4;
			len += 8;
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
			buffer.put((byte)(isOpen==false?0:1));
			buffer.putLong(starttime);
			buffer.putLong(point);
			buffer.putLong(activity);
			buffer.putInt(allShopNames.length);
			for(int i = 0 ; i < allShopNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = allShopNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
				buffer.putInt((int)goods[i].getOverNum());
				buffer.putLong(goods[i].getGoodEndTime());
				buffer.putInt((int)goods[i].getServerNumlimit());
				buffer.putInt((int)goods[i].getPlayerNumlimit());
				buffer.putInt((int)goods[i].getGoodMaxNumLimit());
				try{
				tmpBytes2 = goods[i].getLittleSellIcon().getBytes("UTF-8");
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
	 *	当前选中的商店名字
	 */
	public String getShopName(){
		return shopName;
	}

	/**
	 * 设置属性：
	 *	当前选中的商店名字
	 */
	public void setShopName(String shopName){
		this.shopName = shopName;
	}

	/**
	 * 获取属性：
	 *	是否开启
	 */
	public boolean getIsOpen(){
		return isOpen;
	}

	/**
	 * 设置属性：
	 *	是否开启
	 */
	public void setIsOpen(boolean isOpen){
		this.isOpen = isOpen;
	}

	/**
	 * 获取属性：
	 *	开放时间
	 */
	public long getStarttime(){
		return starttime;
	}

	/**
	 * 设置属性：
	 *	开放时间
	 */
	public void setStarttime(long starttime){
		this.starttime = starttime;
	}

	/**
	 * 获取属性：
	 *	玩家积分
	 */
	public long getPoint(){
		return point;
	}

	/**
	 * 设置属性：
	 *	玩家积分
	 */
	public void setPoint(long point){
		this.point = point;
	}

	/**
	 * 获取属性：
	 *	玩家活跃度
	 */
	public long getActivity(){
		return activity;
	}

	/**
	 * 设置属性：
	 *	玩家活跃度
	 */
	public void setActivity(long activity){
		this.activity = activity;
	}

	/**
	 * 获取属性：
	 *	所有商城名
	 */
	public String[] getAllShopNames(){
		return allShopNames;
	}

	/**
	 * 设置属性：
	 *	所有商城名
	 */
	public void setAllShopNames(String[] allShopNames){
		this.allShopNames = allShopNames;
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