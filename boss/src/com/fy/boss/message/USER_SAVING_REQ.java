package com.fy.boss.message;

import com.fy.boss.message.BossMessageFactory;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 用户充值<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>username.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>username</td><td>String</td><td>username.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playername.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>playername</td><td>String</td><td>playername.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>servername.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>servername</td><td>String</td><td>servername.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>channel.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>channel</td><td>String</td><td>channel.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>os.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>os</td><td>String</td><td>os.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardtype.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardtype</td><td>String</td><td>cardtype.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardno.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardno</td><td>String</td><td>cardno.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cardpass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cardpass</td><td>String</td><td>cardpass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mianzhi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>goodsType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>amount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderID.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderID</td><td>String</td><td>orderID.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class USER_SAVING_REQ implements RequestMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	String username;
	String playername;
	String servername;
	String channel;
	String os;
	String cardtype;
	String cardno;
	String cardpass;
	int mianzhi;
	int goodsType;
	int amount;
	String orderID;

	public USER_SAVING_REQ(){
	}

	public USER_SAVING_REQ(long seqNum,String username,String playername,String servername,String channel,String os,String cardtype,String cardno,String cardpass,int mianzhi,int goodsType,int amount,String orderID){
		this.seqNum = seqNum;
		this.username = username;
		this.playername = playername;
		this.servername = servername;
		this.channel = channel;
		this.os = os;
		this.cardtype = cardtype;
		this.cardno = cardno;
		this.cardpass = cardpass;
		this.mianzhi = mianzhi;
		this.goodsType = goodsType;
		this.amount = amount;
		this.orderID = orderID;
	}

	public USER_SAVING_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		username = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		playername = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		servername = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		channel = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		os = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		cardtype = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		cardno = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		cardpass = new String(content,offset,len,"UTF-8");
		offset += len;
		mianzhi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		goodsType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		amount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
		orderID = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x0000F001;
	}

	public String getTypeDescription() {
		return "USER_SAVING_REQ";
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
			len +=username.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=playername.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=servername.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=channel.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=os.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=cardtype.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=cardno.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=cardpass.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=orderID.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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
			 tmpBytes1 = username.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = playername.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = servername.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = channel.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = os.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = cardtype.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = cardno.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = cardpass.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(mianzhi);
			buffer.putInt(goodsType);
			buffer.putInt(amount);
				try{
			tmpBytes1 = orderID.getBytes("UTF-8");
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
	 *	账号名
	 */
	public String getUsername(){
		return username;
	}

	/**
	 * 设置属性：
	 *	账号名
	 */
	public void setUsername(String username){
		this.username = username;
	}

	/**
	 * 获取属性：
	 *	那个玩家过来的，可为空
	 */
	public String getPlayername(){
		return playername;
	}

	/**
	 * 设置属性：
	 *	那个玩家过来的，可为空
	 */
	public void setPlayername(String playername){
		this.playername = playername;
	}

	/**
	 * 获取属性：
	 *	服务器
	 */
	public String getServername(){
		return servername;
	}

	/**
	 * 设置属性：
	 *	服务器
	 */
	public void setServername(String servername){
		this.servername = servername;
	}

	/**
	 * 获取属性：
	 *	充值的客户端渠道
	 */
	public String getChannel(){
		return channel;
	}

	/**
	 * 设置属性：
	 *	充值的客户端渠道
	 */
	public void setChannel(String channel){
		this.channel = channel;
	}

	/**
	 * 获取属性：
	 *	客户端OS
	 */
	public String getOs(){
		return os;
	}

	/**
	 * 设置属性：
	 *	客户端OS
	 */
	public void setOs(String os){
		this.os = os;
	}

	/**
	 * 获取属性：
	 *	卡类型, 移动充值卡|联通一卡付|电信充值卡|盛大卡|征途卡|骏网一卡通|久游卡|网易卡|完美卡|搜狐卡
	 */
	public String getCardtype(){
		return cardtype;
	}

	/**
	 * 设置属性：
	 *	卡类型, 移动充值卡|联通一卡付|电信充值卡|盛大卡|征途卡|骏网一卡通|久游卡|网易卡|完美卡|搜狐卡
	 */
	public void setCardtype(String cardtype){
		this.cardtype = cardtype;
	}

	/**
	 * 获取属性：
	 *	卡序列号
	 */
	public String getCardno(){
		return cardno;
	}

	/**
	 * 设置属性：
	 *	卡序列号
	 */
	public void setCardno(String cardno){
		this.cardno = cardno;
	}

	/**
	 * 获取属性：
	 *	卡密码
	 */
	public String getCardpass(){
		return cardpass;
	}

	/**
	 * 设置属性：
	 *	卡密码
	 */
	public void setCardpass(String cardpass){
		this.cardpass = cardpass;
	}

	/**
	 * 获取属性：
	 *	卡面值，单位：分
	 */
	public int getMianzhi(){
		return mianzhi;
	}

	/**
	 * 设置属性：
	 *	卡面值，单位：分
	 */
	public void setMianzhi(int mianzhi){
		this.mianzhi = mianzhi;
	}

	/**
	 * 获取属性：
	 *	预留字段， 货物，用于需要定义货物的地方
	 */
	public int getGoodsType(){
		return goodsType;
	}

	/**
	 * 设置属性：
	 *	预留字段， 货物，用于需要定义货物的地方
	 */
	public void setGoodsType(int goodsType){
		this.goodsType = goodsType;
	}

	/**
	 * 获取属性：
	 *	预留字段， 数量，用于需要数量的地方
	 */
	public int getAmount(){
		return amount;
	}

	/**
	 * 设置属性：
	 *	预留字段， 数量，用于需要数量的地方
	 */
	public void setAmount(int amount){
		this.amount = amount;
	}

	/**
	 * 获取属性：
	 *	预留字段， 用于记录第三方的订单号
	 */
	public String getOrderID(){
		return orderID;
	}

	/**
	 * 设置属性：
	 *	预留字段， 用于记录第三方的订单号
	 */
	public void setOrderID(String orderID){
		this.orderID = orderID;
	}

}