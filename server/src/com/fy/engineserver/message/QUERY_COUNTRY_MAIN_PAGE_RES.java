package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 国家主页面查询，服务器传回结果，当客户端收到RES后弹出国家主页面UI<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>countryName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>countryName</td><td>String</td><td>countryName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingName</td><td>String</td><td>kingName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>dasimaName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>dasimaName</td><td>String</td><td>dasimaName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>seniorGeneralName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seniorGeneralName</td><td>String</td><td>seniorGeneralName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>marshalName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>marshalName</td><td>String</td><td>marshalName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>primeMinisterName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>primeMinisterName</td><td>String</td><td>primeMinisterName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>policeByKingName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>policeByKingName</td><td>String</td><td>policeByKingName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yushidafuByKingName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yushidafuByKingName</td><td>String</td><td>yushidafuByKingName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>policeByMarshalName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>policeByMarshalName</td><td>String</td><td>policeByMarshalName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yushidafuByPrimeMinisterName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yushidafuByPrimeMinisterName</td><td>String</td><td>yushidafuByPrimeMinisterName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouxunNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouxunNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouxunNames[0]</td><td>String</td><td>shouxunNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouxunNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouxunNames[1]</td><td>String</td><td>shouxunNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shouxunNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shouxunNames[2]</td><td>String</td><td>shouxunNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>biaozhangNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>biaozhangNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>biaozhangNames[0]</td><td>String</td><td>biaozhangNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>biaozhangNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>biaozhangNames[1]</td><td>String</td><td>biaozhangNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>biaozhangNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>biaozhangNames[2]</td><td>String</td><td>biaozhangNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>kingFengLuTake</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>dasimaFengLuTake</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>seniorGeneralFengLuTake</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>marshalFengLuTake</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>primeMinisterFengLuTake</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>policeByKingFengLuTake</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yushidafuByKingFengLuTake</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>policeByMarshalFengLuTake</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yushidafuByPrimeMinisterFengLuTake</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>vote.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>vote</td><td>String</td><td>vote.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>notice.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>notice</td><td>String</td><td>notice.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>guozhanZiYuan</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class QUERY_COUNTRY_MAIN_PAGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String countryName;
	String kingName;
	String dasimaName;
	String seniorGeneralName;
	String marshalName;
	String primeMinisterName;
	String policeByKingName;
	String yushidafuByKingName;
	String policeByMarshalName;
	String yushidafuByPrimeMinisterName;
	long[] ids;
	String[] shouxunNames;
	String[] biaozhangNames;
	byte kingFengLuTake;
	byte dasimaFengLuTake;
	byte seniorGeneralFengLuTake;
	byte marshalFengLuTake;
	byte primeMinisterFengLuTake;
	byte policeByKingFengLuTake;
	byte yushidafuByKingFengLuTake;
	byte policeByMarshalFengLuTake;
	byte yushidafuByPrimeMinisterFengLuTake;
	String vote;
	String notice;
	long guozhanZiYuan;

	public QUERY_COUNTRY_MAIN_PAGE_RES(){
	}

	public QUERY_COUNTRY_MAIN_PAGE_RES(long seqNum,String countryName,String kingName,String dasimaName,String seniorGeneralName,String marshalName,String primeMinisterName,String policeByKingName,String yushidafuByKingName,String policeByMarshalName,String yushidafuByPrimeMinisterName,long[] ids,String[] shouxunNames,String[] biaozhangNames,byte kingFengLuTake,byte dasimaFengLuTake,byte seniorGeneralFengLuTake,byte marshalFengLuTake,byte primeMinisterFengLuTake,byte policeByKingFengLuTake,byte yushidafuByKingFengLuTake,byte policeByMarshalFengLuTake,byte yushidafuByPrimeMinisterFengLuTake,String vote,String notice,long guozhanZiYuan){
		this.seqNum = seqNum;
		this.countryName = countryName;
		this.kingName = kingName;
		this.dasimaName = dasimaName;
		this.seniorGeneralName = seniorGeneralName;
		this.marshalName = marshalName;
		this.primeMinisterName = primeMinisterName;
		this.policeByKingName = policeByKingName;
		this.yushidafuByKingName = yushidafuByKingName;
		this.policeByMarshalName = policeByMarshalName;
		this.yushidafuByPrimeMinisterName = yushidafuByPrimeMinisterName;
		this.ids = ids;
		this.shouxunNames = shouxunNames;
		this.biaozhangNames = biaozhangNames;
		this.kingFengLuTake = kingFengLuTake;
		this.dasimaFengLuTake = dasimaFengLuTake;
		this.seniorGeneralFengLuTake = seniorGeneralFengLuTake;
		this.marshalFengLuTake = marshalFengLuTake;
		this.primeMinisterFengLuTake = primeMinisterFengLuTake;
		this.policeByKingFengLuTake = policeByKingFengLuTake;
		this.yushidafuByKingFengLuTake = yushidafuByKingFengLuTake;
		this.policeByMarshalFengLuTake = policeByMarshalFengLuTake;
		this.yushidafuByPrimeMinisterFengLuTake = yushidafuByPrimeMinisterFengLuTake;
		this.vote = vote;
		this.notice = notice;
		this.guozhanZiYuan = guozhanZiYuan;
	}

	public QUERY_COUNTRY_MAIN_PAGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		countryName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		kingName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		dasimaName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		seniorGeneralName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		marshalName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		primeMinisterName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		policeByKingName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		yushidafuByKingName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		policeByMarshalName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		yushidafuByPrimeMinisterName = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids = new long[len];
		for(int i = 0 ; i < ids.length ; i++){
			ids[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		shouxunNames = new String[len];
		for(int i = 0 ; i < shouxunNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			shouxunNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		biaozhangNames = new String[len];
		for(int i = 0 ; i < biaozhangNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			biaozhangNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		kingFengLuTake = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		dasimaFengLuTake = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		seniorGeneralFengLuTake = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		marshalFengLuTake = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		primeMinisterFengLuTake = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		policeByKingFengLuTake = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		yushidafuByKingFengLuTake = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		policeByMarshalFengLuTake = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		yushidafuByPrimeMinisterFengLuTake = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		vote = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		notice = new String(content,offset,len,"UTF-8");
		offset += len;
		guozhanZiYuan = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70000AA0;
	}

	public String getTypeDescription() {
		return "QUERY_COUNTRY_MAIN_PAGE_RES";
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
			len +=countryName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=kingName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=dasimaName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=seniorGeneralName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=marshalName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=primeMinisterName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=policeByKingName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=yushidafuByKingName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=policeByMarshalName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=yushidafuByPrimeMinisterName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += ids.length * 8;
		len += 4;
		for(int i = 0 ; i < shouxunNames.length; i++){
			len += 2;
			try{
				len += shouxunNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < biaozhangNames.length; i++){
			len += 2;
			try{
				len += biaozhangNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 1;
		len += 2;
		try{
			len +=vote.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=notice.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
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
			 tmpBytes1 = countryName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = kingName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = dasimaName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = seniorGeneralName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = marshalName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = primeMinisterName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = policeByKingName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = yushidafuByKingName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = policeByMarshalName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = yushidafuByPrimeMinisterName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(shouxunNames.length);
			for(int i = 0 ; i < shouxunNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = shouxunNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(biaozhangNames.length);
			for(int i = 0 ; i < biaozhangNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = biaozhangNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.put(kingFengLuTake);
			buffer.put(dasimaFengLuTake);
			buffer.put(seniorGeneralFengLuTake);
			buffer.put(marshalFengLuTake);
			buffer.put(primeMinisterFengLuTake);
			buffer.put(policeByKingFengLuTake);
			buffer.put(yushidafuByKingFengLuTake);
			buffer.put(policeByMarshalFengLuTake);
			buffer.put(yushidafuByPrimeMinisterFengLuTake);
				try{
			tmpBytes1 = vote.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = notice.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(guozhanZiYuan);
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
	 *	国家名
	 */
	public String getCountryName(){
		return countryName;
	}

	/**
	 * 设置属性：
	 *	国家名
	 */
	public void setCountryName(String countryName){
		this.countryName = countryName;
	}

	/**
	 * 获取属性：
	 *	国王，天尊
	 */
	public String getKingName(){
		return kingName;
	}

	/**
	 * 设置属性：
	 *	国王，天尊
	 */
	public void setKingName(String kingName){
		this.kingName = kingName;
	}

	/**
	 * 获取属性：
	 *	大司马，玄阴真圣
	 */
	public String getDasimaName(){
		return dasimaName;
	}

	/**
	 * 设置属性：
	 *	大司马，玄阴真圣
	 */
	public void setDasimaName(String dasimaName){
		this.dasimaName = dasimaName;
	}

	/**
	 * 获取属性：
	 *	大将军，纯阳真圣
	 */
	public String getSeniorGeneralName(){
		return seniorGeneralName;
	}

	/**
	 * 设置属性：
	 *	大将军，纯阳真圣
	 */
	public void setSeniorGeneralName(String seniorGeneralName){
		this.seniorGeneralName = seniorGeneralName;
	}

	/**
	 * 获取属性：
	 *	元帅，九天司命
	 */
	public String getMarshalName(){
		return marshalName;
	}

	/**
	 * 设置属性：
	 *	元帅，九天司命
	 */
	public void setMarshalName(String marshalName){
		this.marshalName = marshalName;
	}

	/**
	 * 获取属性：
	 *	宰相，九天司空
	 */
	public String getPrimeMinisterName(){
		return primeMinisterName;
	}

	/**
	 * 设置属性：
	 *	宰相，九天司空
	 */
	public void setPrimeMinisterName(String primeMinisterName){
		this.primeMinisterName = primeMinisterName;
	}

	/**
	 * 获取属性：
	 *	巡捕国王任命，真武神君
	 */
	public String getPoliceByKingName(){
		return policeByKingName;
	}

	/**
	 * 设置属性：
	 *	巡捕国王任命，真武神君
	 */
	public void setPoliceByKingName(String policeByKingName){
		this.policeByKingName = policeByKingName;
	}

	/**
	 * 获取属性：
	 *	御史大夫国王任命，道陵星君
	 */
	public String getYushidafuByKingName(){
		return yushidafuByKingName;
	}

	/**
	 * 设置属性：
	 *	御史大夫国王任命，道陵星君
	 */
	public void setYushidafuByKingName(String yushidafuByKingName){
		this.yushidafuByKingName = yushidafuByKingName;
	}

	/**
	 * 获取属性：
	 *	巡捕元帅任命，翔武神君
	 */
	public String getPoliceByMarshalName(){
		return policeByMarshalName;
	}

	/**
	 * 设置属性：
	 *	巡捕元帅任命，翔武神君
	 */
	public void setPoliceByMarshalName(String policeByMarshalName){
		this.policeByMarshalName = policeByMarshalName;
	}

	/**
	 * 获取属性：
	 *	御史大夫宰相任命，葛玄星君
	 */
	public String getYushidafuByPrimeMinisterName(){
		return yushidafuByPrimeMinisterName;
	}

	/**
	 * 设置属性：
	 *	御史大夫宰相任命，葛玄星君
	 */
	public void setYushidafuByPrimeMinisterName(String yushidafuByPrimeMinisterName){
		this.yushidafuByPrimeMinisterName = yushidafuByPrimeMinisterName;
	}

	/**
	 * 获取属性：
	 *	各个官员的id，从国王开始，顺序为国家官职序号
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	各个官员的id，从国王开始，顺序为国家官职序号
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	授勋的国家官员
	 */
	public String[] getShouxunNames(){
		return shouxunNames;
	}

	/**
	 * 设置属性：
	 *	授勋的国家官员
	 */
	public void setShouxunNames(String[] shouxunNames){
		this.shouxunNames = shouxunNames;
	}

	/**
	 * 获取属性：
	 *	表彰的国家官员
	 */
	public String[] getBiaozhangNames(){
		return biaozhangNames;
	}

	/**
	 * 设置属性：
	 *	表彰的国家官员
	 */
	public void setBiaozhangNames(String[] biaozhangNames){
		this.biaozhangNames = biaozhangNames;
	}

	/**
	 * 获取属性：
	 *	国王，天尊，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public byte getKingFengLuTake(){
		return kingFengLuTake;
	}

	/**
	 * 设置属性：
	 *	国王，天尊，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public void setKingFengLuTake(byte kingFengLuTake){
		this.kingFengLuTake = kingFengLuTake;
	}

	/**
	 * 获取属性：
	 *	大司马，玄阴真圣，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public byte getDasimaFengLuTake(){
		return dasimaFengLuTake;
	}

	/**
	 * 设置属性：
	 *	大司马，玄阴真圣，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public void setDasimaFengLuTake(byte dasimaFengLuTake){
		this.dasimaFengLuTake = dasimaFengLuTake;
	}

	/**
	 * 获取属性：
	 *	大将军，纯阳真圣，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public byte getSeniorGeneralFengLuTake(){
		return seniorGeneralFengLuTake;
	}

	/**
	 * 设置属性：
	 *	大将军，纯阳真圣，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public void setSeniorGeneralFengLuTake(byte seniorGeneralFengLuTake){
		this.seniorGeneralFengLuTake = seniorGeneralFengLuTake;
	}

	/**
	 * 获取属性：
	 *	元帅，九天司命，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public byte getMarshalFengLuTake(){
		return marshalFengLuTake;
	}

	/**
	 * 设置属性：
	 *	元帅，九天司命，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public void setMarshalFengLuTake(byte marshalFengLuTake){
		this.marshalFengLuTake = marshalFengLuTake;
	}

	/**
	 * 获取属性：
	 *	宰相，九天司空，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public byte getPrimeMinisterFengLuTake(){
		return primeMinisterFengLuTake;
	}

	/**
	 * 设置属性：
	 *	宰相，九天司空，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public void setPrimeMinisterFengLuTake(byte primeMinisterFengLuTake){
		this.primeMinisterFengLuTake = primeMinisterFengLuTake;
	}

	/**
	 * 获取属性：
	 *	巡捕国王任命，真武神君，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public byte getPoliceByKingFengLuTake(){
		return policeByKingFengLuTake;
	}

	/**
	 * 设置属性：
	 *	巡捕国王任命，真武神君，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public void setPoliceByKingFengLuTake(byte policeByKingFengLuTake){
		this.policeByKingFengLuTake = policeByKingFengLuTake;
	}

	/**
	 * 获取属性：
	 *	御史大夫国王任命，道陵星君，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public byte getYushidafuByKingFengLuTake(){
		return yushidafuByKingFengLuTake;
	}

	/**
	 * 设置属性：
	 *	御史大夫国王任命，道陵星君，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public void setYushidafuByKingFengLuTake(byte yushidafuByKingFengLuTake){
		this.yushidafuByKingFengLuTake = yushidafuByKingFengLuTake;
	}

	/**
	 * 获取属性：
	 *	巡捕元帅任命，翔武神君，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public byte getPoliceByMarshalFengLuTake(){
		return policeByMarshalFengLuTake;
	}

	/**
	 * 设置属性：
	 *	巡捕元帅任命，翔武神君，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public void setPoliceByMarshalFengLuTake(byte policeByMarshalFengLuTake){
		this.policeByMarshalFengLuTake = policeByMarshalFengLuTake;
	}

	/**
	 * 获取属性：
	 *	御史大夫宰相任命，葛玄星君，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public byte getYushidafuByPrimeMinisterFengLuTake(){
		return yushidafuByPrimeMinisterFengLuTake;
	}

	/**
	 * 设置属性：
	 *	御史大夫宰相任命，葛玄星君，领取俸禄标记，0未发放，1发放未领取，2已领取俸禄
	 */
	public void setYushidafuByPrimeMinisterFengLuTake(byte yushidafuByPrimeMinisterFengLuTake){
		this.yushidafuByPrimeMinisterFengLuTake = yushidafuByPrimeMinisterFengLuTake;
	}

	/**
	 * 获取属性：
	 *	国家总投票率
	 */
	public String getVote(){
		return vote;
	}

	/**
	 * 设置属性：
	 *	国家总投票率
	 */
	public void setVote(String vote){
		this.vote = vote;
	}

	/**
	 * 获取属性：
	 *	公告
	 */
	public String getNotice(){
		return notice;
	}

	/**
	 * 设置属性：
	 *	公告
	 */
	public void setNotice(String notice){
		this.notice = notice;
	}

	/**
	 * 获取属性：
	 *	国战资源
	 */
	public long getGuozhanZiYuan(){
		return guozhanZiYuan;
	}

	/**
	 * 设置属性：
	 *	国战资源
	 */
	public void setGuozhanZiYuan(long guozhanZiYuan){
		this.guozhanZiYuan = guozhanZiYuan;
	}

}