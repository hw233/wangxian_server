package com.tencent.java.sdk;
/**
 * 支付单信息
 */
import org.json.*;
/**
 * 支付定单信息来自第三方请求的json内容
 * @author teajtang
 *
 */
public class PaymentItem {
	/*
	 * 请求json文本例子
	 *   "paymentItems":[
      {
        "itemId":"ex101",
        "itemName":"马克上衣",
        "unitPrice":"300",
        "quantity":"1",
        "imageUrl":"http://example.com/ex101.jpg",
        "description":"马克上衣说明"
      },
	 */
	private String itemId       = new String();
	private String itemName     = new String();
	private String imageUrl     = new String();
	private String description  = new String();
	private String type         = new String();
	private String subType      = new String();
	private int    unitPrice    = -1;
	private int    quantity     = 1;
	private int    goodsId      = 1;
	
	public static PaymentItem fromJsonObj(JSONObject o)
	{
		PaymentItem p = new PaymentItem();
		try
		{
			p.setItemId(o.getString("itemId"));
			p.setItemName(o.getString("itemName"));
			p.setImageUrl(o.getString("imageUrl"));
			p.setDescription(o.getString("description"));
			p.setUnitPrice(o.getInt("unitPrice"));
			p.setQuantity(o.getInt("quantity"));
			p.setType(o.getString("type"));
			p.setSubType(o.getString("subType"));
			p.setGoodsId(o.optInt("goodsId", -1));
		} catch (JSONException e)
		{
			e.printStackTrace();
			p = null;			
		}

		return p;
	}
	
	public int getGoodsId()
	{
		return goodsId;
	}

	public void setGoodsId(int goodsId)
	{
		this.goodsId = goodsId;
	}

	JSONObject toJsonObj()
	{
		try{
		JSONObject o = new JSONObject();
		o.put("itemId",   itemId);
		o.put("itemName", itemName);
		o.put("unitPrice", unitPrice);
		o.put("quantity", quantity);
		o.put("subType", subType);
		o.put("type", type);
		
		if (!imageUrl.isEmpty())
			o.put("imageUrl", imageUrl);
		
		if (!description.isEmpty())
			o.put("description", description);	
	
		if (goodsId > 1)
			o.put("goodsId", goodsId);
		
		return o;
		
		}catch(JSONException e)
		{
			e.printStackTrace();
		}		
		return null;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	boolean valid()
	{
		if (itemId.length() == 0 ||
				quantity < 1)
			return false;
		else
			return true;
			
	}
	public String getSubType()
	{
		return subType;
	}
	public String getType()
	{
		return type;
	}
	public void setSubType(String subType)
	{
		this.subType = subType;
	}
	public void setType(String type)
	{
		this.type = type;
	}
}
