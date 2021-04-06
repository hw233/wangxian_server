package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 用户客户端信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>clientId</td><td>String</td><td>clientId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>channel.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>channel</td><td>String</td><td>channel.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientPlatform.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>clientPlatform</td><td>String</td><td>clientPlatform.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientFull.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>clientFull</td><td>String</td><td>clientFull.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientProgramVersion.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>clientProgramVersion</td><td>String</td><td>clientProgramVersion.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>clientResourceVersion.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>clientResourceVersion</td><td>String</td><td>clientResourceVersion.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>phoneType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>phoneType</td><td>String</td><td>phoneType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>network.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>network</td><td>String</td><td>network.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gpu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gpu</td><td>String</td><td>gpu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>gpuOtherName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>gpuOtherName</td><td>String</td><td>gpuOtherName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>UUID.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>UUID</td><td>String</td><td>UUID.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>DEVICEID.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>DEVICEID</td><td>String</td><td>DEVICEID.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>IMSI.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>IMSI</td><td>String</td><td>IMSI.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>MACADDRESS.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>MACADDRESS</td><td>String</td><td>MACADDRESS.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isExistOtherServer</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>isStartServerSucess</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>isStartServerBindFail</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class USER_CLIENT_INFO_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String clientId;
	String channel;
	String clientPlatform;
	String clientFull;
	String clientProgramVersion;
	String clientResourceVersion;
	String phoneType;
	String network;
	String gpu;
	String gpuOtherName;
	String UUID;
	String DEVICEID;
	String IMSI;
	String MACADDRESS;
	boolean isExistOtherServer;
	boolean isStartServerSucess;
	boolean isStartServerBindFail;

	public USER_CLIENT_INFO_REQ(){
	}

	public USER_CLIENT_INFO_REQ(long seqNum,String clientId,String channel,String clientPlatform,String clientFull,String clientProgramVersion,String clientResourceVersion,String phoneType,String network,String gpu,String gpuOtherName,String UUID,String DEVICEID,String IMSI,String MACADDRESS,boolean isExistOtherServer,boolean isStartServerSucess,boolean isStartServerBindFail){
		this.seqNum = seqNum;
		this.clientId = clientId;
		this.channel = channel;
		this.clientPlatform = clientPlatform;
		this.clientFull = clientFull;
		this.clientProgramVersion = clientProgramVersion;
		this.clientResourceVersion = clientResourceVersion;
		this.phoneType = phoneType;
		this.network = network;
		this.gpu = gpu;
		this.gpuOtherName = gpuOtherName;
		this.UUID = UUID;
		this.DEVICEID = DEVICEID;
		this.IMSI = IMSI;
		this.MACADDRESS = MACADDRESS;
		this.isExistOtherServer = isExistOtherServer;
		this.isStartServerSucess = isStartServerSucess;
		this.isStartServerBindFail = isStartServerBindFail;
	}

	public USER_CLIENT_INFO_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		clientId = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		channel = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		clientPlatform = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		clientFull = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		clientProgramVersion = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		clientResourceVersion = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		phoneType = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		network = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gpu = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		gpuOtherName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		UUID = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		DEVICEID = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		IMSI = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		MACADDRESS = new String(content,offset,len,"UTF-8");
		offset += len;
		isExistOtherServer = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		isStartServerSucess = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		isStartServerBindFail = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x000EE007;
	}

	public String getTypeDescription() {
		return "USER_CLIENT_INFO_REQ";
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
			len +=clientId.getBytes("UTF-8").length;
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
			len +=clientPlatform.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=clientFull.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=clientProgramVersion.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=clientResourceVersion.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=phoneType.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=network.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=gpu.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=gpuOtherName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=UUID.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=DEVICEID.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=IMSI.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=MACADDRESS.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 1;
		len += 1;
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
			 tmpBytes1 = clientId.getBytes("UTF-8");
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
			tmpBytes1 = clientPlatform.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = clientFull.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = clientProgramVersion.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = clientResourceVersion.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = phoneType.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = network.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = gpu.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = gpuOtherName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = UUID.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = DEVICEID.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = IMSI.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = MACADDRESS.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(isExistOtherServer==false?0:1));
			buffer.put((byte)(isStartServerSucess==false?0:1));
			buffer.put((byte)(isStartServerBindFail==false?0:1));
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
	public String getClientId(){
		return clientId;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setClientId(String clientId){
		this.clientId = clientId;
	}

	/**
	 * 获取属性：
	 *	渠道串，例如：ODANL_DWB01，jar包或者jad中包含此信息
	 */
	public String getChannel(){
		return channel;
	}

	/**
	 * 设置属性：
	 *	渠道串，例如：ODANL_DWB01，jar包或者jad中包含此信息
	 */
	public void setChannel(String channel){
		this.channel = channel;
	}

	/**
	 * 获取属性：
	 *	客户端类型，比如ios,android
	 */
	public String getClientPlatform(){
		return clientPlatform;
	}

	/**
	 * 设置属性：
	 *	客户端类型，比如ios,android
	 */
	public void setClientPlatform(String clientPlatform){
		this.clientPlatform = clientPlatform;
	}

	/**
	 * 获取属性：
	 *	全包，半包
	 */
	public String getClientFull(){
		return clientFull;
	}

	/**
	 * 设置属性：
	 *	全包，半包
	 */
	public void setClientFull(String clientFull){
		this.clientFull = clientFull;
	}

	/**
	 * 获取属性：
	 *	客户端程序版本
	 */
	public String getClientProgramVersion(){
		return clientProgramVersion;
	}

	/**
	 * 设置属性：
	 *	客户端程序版本
	 */
	public void setClientProgramVersion(String clientProgramVersion){
		this.clientProgramVersion = clientProgramVersion;
	}

	/**
	 * 获取属性：
	 *	客户端资源版本
	 */
	public String getClientResourceVersion(){
		return clientResourceVersion;
	}

	/**
	 * 设置属性：
	 *	客户端资源版本
	 */
	public void setClientResourceVersion(String clientResourceVersion){
		this.clientResourceVersion = clientResourceVersion;
	}

	/**
	 * 获取属性：
	 *	真正机器的机型信息
	 */
	public String getPhoneType(){
		return phoneType;
	}

	/**
	 * 设置属性：
	 *	真正机器的机型信息
	 */
	public void setPhoneType(String phoneType){
		this.phoneType = phoneType;
	}

	/**
	 * 获取属性：
	 *	WIFI,gprs,3dnet
	 */
	public String getNetwork(){
		return network;
	}

	/**
	 * 设置属性：
	 *	WIFI,gprs,3dnet
	 */
	public void setNetwork(String network){
		this.network = network;
	}

	/**
	 * 获取属性：
	 *	pvr,png,ati,etc
	 */
	public String getGpu(){
		return gpu;
	}

	/**
	 * 设置属性：
	 *	pvr,png,ati,etc
	 */
	public void setGpu(String gpu){
		this.gpu = gpu;
	}

	/**
	 * 获取属性：
	 *	pvr,png,ati,etc
	 */
	public String getGpuOtherName(){
		return gpuOtherName;
	}

	/**
	 * 设置属性：
	 *	pvr,png,ati,etc
	 */
	public void setGpuOtherName(String gpuOtherName){
		this.gpuOtherName = gpuOtherName;
	}

	/**
	 * 获取属性：
	 *	ios 唯一用户编号
	 */
	public String getUUID(){
		return UUID;
	}

	/**
	 * 设置属性：
	 *	ios 唯一用户编号
	 */
	public void setUUID(String UUID){
		this.UUID = UUID;
	}

	/**
	 * 获取属性：
	 *	android手机设备号
	 */
	public String getDEVICEID(){
		return DEVICEID;
	}

	/**
	 * 设置属性：
	 *	android手机设备号
	 */
	public void setDEVICEID(String DEVICEID){
		this.DEVICEID = DEVICEID;
	}

	/**
	 * 获取属性：
	 *	android 国际移动用户识别码
	 */
	public String getIMSI(){
		return IMSI;
	}

	/**
	 * 设置属性：
	 *	android 国际移动用户识别码
	 */
	public void setIMSI(String IMSI){
		this.IMSI = IMSI;
	}

	/**
	 * 获取属性：
	 *	ios and android 网卡地址
	 */
	public String getMACADDRESS(){
		return MACADDRESS;
	}

	/**
	 * 设置属性：
	 *	ios and android 网卡地址
	 */
	public void setMACADDRESS(String MACADDRESS){
		this.MACADDRESS = MACADDRESS;
	}

	/**
	 * 获取属性：
	 *	是否存在其他客户端(收到回复消息)，并试图杀掉这个其他客户端
	 */
	public boolean getIsExistOtherServer(){
		return isExistOtherServer;
	}

	/**
	 * 设置属性：
	 *	是否存在其他客户端(收到回复消息)，并试图杀掉这个其他客户端
	 */
	public void setIsExistOtherServer(boolean isExistOtherServer){
		this.isExistOtherServer = isExistOtherServer;
	}

	/**
	 * 获取属性：
	 *	是否启动客户端服务
	 */
	public boolean getIsStartServerSucess(){
		return isStartServerSucess;
	}

	/**
	 * 设置属性：
	 *	是否启动客户端服务
	 */
	public void setIsStartServerSucess(boolean isStartServerSucess){
		this.isStartServerSucess = isStartServerSucess;
	}

	/**
	 * 获取属性：
	 *	是否启动客户端服务绑端口失败
	 */
	public boolean getIsStartServerBindFail(){
		return isStartServerBindFail;
	}

	/**
	 * 设置属性：
	 *	是否启动客户端服务绑端口失败
	 */
	public void setIsStartServerBindFail(boolean isStartServerBindFail){
		this.isStartServerBindFail = isStartServerBindFail;
	}

}