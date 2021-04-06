package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端向服务器发送的版本检查指令<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>returnCode</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>URL.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>URL</td><td>String</td><td>URL.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resVersion.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resVersion</td><td>short[]</td><td>resVersion.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tips.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tips[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tips[0]</td><td>String</td><td>tips[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tips[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tips[1]</td><td>String</td><td>tips[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tips[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>tips[2]</td><td>String</td><td>tips[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>whiteuserurl.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>whiteuserurl</td><td>String</td><td>whiteuserurl.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>smsNumber.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>smsNumber</td><td>String</td><td>smsNumber.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>smsCode.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>smsCode</td><td>String</td><td>smsCode.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resultInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resultInfo</td><td>String</td><td>resultInfo.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class VERSION_CHECK_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte returnCode;
	String URL;
	short[] resVersion;
	String[] tips;
	String whiteuserurl;
	String smsNumber;
	String smsCode;
	String resultInfo;

	public VERSION_CHECK_RES(long seqNum,byte returnCode,String URL,short[] resVersion,String[] tips,String whiteuserurl,String smsNumber,String smsCode,String resultInfo){
		this.seqNum = seqNum;
		this.returnCode = returnCode;
		this.URL = URL;
		this.resVersion = resVersion;
		this.tips = tips;
		this.whiteuserurl = whiteuserurl;
		this.smsNumber = smsNumber;
		this.smsCode = smsCode;
		this.resultInfo = resultInfo;
	}

	public VERSION_CHECK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		returnCode = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		URL = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		resVersion = new short[len];
		for(int i = 0 ; i < resVersion.length ; i++){
			resVersion[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		tips = new String[len];
		for(int i = 0 ; i < tips.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			tips[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		whiteuserurl = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		smsNumber = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		smsCode = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		resultInfo = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x80000F22;
	}

	public String getTypeDescription() {
		return "VERSION_CHECK_RES";
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
			len +=URL.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += resVersion.length * 2;
		len += 4;
		for(int i = 0 ; i < tips.length; i++){
			len += 2;
			try{
				len += tips[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		try{
			len +=whiteuserurl.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=smsNumber.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=smsCode.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=resultInfo.getBytes("UTF-8").length;
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
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.put(returnCode);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = URL.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(resVersion.length);
			for(int i = 0 ; i < resVersion.length; i++){
				buffer.putShort(resVersion[i]);
			}
			buffer.putInt(tips.length);
			for(int i = 0 ; i < tips.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = tips[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
				try{
			tmpBytes1 = whiteuserurl.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = smsNumber.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = smsCode.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = resultInfo.getBytes("UTF-8");
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
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	0-客户端是最新版本，无需更新；1-当前客户端已经不能运行，必须更新,2-客户端安装包不恰当，提示安装合适的包
	 */
	public byte getReturnCode(){
		return returnCode;
	}

	/**
	 * 设置属性：
	 *	0-客户端是最新版本，无需更新；1-当前客户端已经不能运行，必须更新,2-客户端安装包不恰当，提示安装合适的包
	 */
	public void setReturnCode(byte returnCode){
		this.returnCode = returnCode;
	}

	/**
	 * 获取属性：
	 *	Jad文件的下载地址
	 */
	public String getURL(){
		return URL;
	}

	/**
	 * 设置属性：
	 *	Jad文件的下载地址
	 */
	public void setURL(String URL){
		this.URL = URL;
	}

	/**
	 * 获取属性：
	 *	最新的资源版本号
	 */
	public short[] getResVersion(){
		return resVersion;
	}

	/**
	 * 设置属性：
	 *	最新的资源版本号
	 */
	public void setResVersion(short[] resVersion){
		this.resVersion = resVersion;
	}

	/**
	 * 获取属性：
	 *	Tips
	 */
	public String[] getTips(){
		return tips;
	}

	/**
	 * 设置属性：
	 *	Tips
	 */
	public void setTips(String[] tips){
		this.tips = tips;
	}

	/**
	 * 获取属性：
	 *	白名单系统地址
	 */
	public String getWhiteuserurl(){
		return whiteuserurl;
	}

	/**
	 * 设置属性：
	 *	白名单系统地址
	 */
	public void setWhiteuserurl(String whiteuserurl){
		this.whiteuserurl = whiteuserurl;
	}

	/**
	 * 获取属性：
	 *	短信号码
	 */
	public String getSmsNumber(){
		return smsNumber;
	}

	/**
	 * 设置属性：
	 *	短信号码
	 */
	public void setSmsNumber(String smsNumber){
		this.smsNumber = smsNumber;
	}

	/**
	 * 获取属性：
	 *	短信代码
	 */
	public String getSmsCode(){
		return smsCode;
	}

	/**
	 * 设置属性：
	 *	短信代码
	 */
	public void setSmsCode(String smsCode){
		this.smsCode = smsCode;
	}

	/**
	 * 获取属性：
	 *	2-提示用户安装恰当包
	 */
	public String getResultInfo(){
		return resultInfo;
	}

	/**
	 * 设置属性：
	 *	2-提示用户安装恰当包
	 */
	public void setResultInfo(String resultInfo){
		this.resultInfo = resultInfo;
	}

}
