package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 国家王者之印使用次数<br>
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
 * <tr bgcolor="#FAFAFA" align="center"><td>kingWangZhe.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>kingWangZhe</td><td>long[]</td><td>kingWangZhe.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>dasimaWangZhe.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>dasimaWangZhe</td><td>long[]</td><td>dasimaWangZhe.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>seniorGeneralWangZhe.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seniorGeneralWangZhe</td><td>long[]</td><td>seniorGeneralWangZhe.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>marshalWangZhe.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>marshalWangZhe</td><td>long[]</td><td>marshalWangZhe.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>primeMinisterWangZhe.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>primeMinisterWangZhe</td><td>long[]</td><td>primeMinisterWangZhe.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>policeByKingWangZhe.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>policeByKingWangZhe</td><td>long[]</td><td>policeByKingWangZhe.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yushidafuByKingWangZhe.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yushidafuByKingWangZhe</td><td>long[]</td><td>yushidafuByKingWangZhe.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>policeByMarshalWangZhe.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>policeByMarshalWangZhe</td><td>long[]</td><td>policeByMarshalWangZhe.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>yushidafuByPrimeMinisterWangZhe.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>yushidafuByPrimeMinisterWangZhe</td><td>long[]</td><td>yushidafuByPrimeMinisterWangZhe.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_COUNTRY_WANGZHEZHIYIN_RES implements ResponseMessage{

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
	long[] kingWangZhe;
	long[] dasimaWangZhe;
	long[] seniorGeneralWangZhe;
	long[] marshalWangZhe;
	long[] primeMinisterWangZhe;
	long[] policeByKingWangZhe;
	long[] yushidafuByKingWangZhe;
	long[] policeByMarshalWangZhe;
	long[] yushidafuByPrimeMinisterWangZhe;

	public QUERY_COUNTRY_WANGZHEZHIYIN_RES(){
	}

	public QUERY_COUNTRY_WANGZHEZHIYIN_RES(long seqNum,String countryName,String kingName,String dasimaName,String seniorGeneralName,String marshalName,String primeMinisterName,String policeByKingName,String yushidafuByKingName,String policeByMarshalName,String yushidafuByPrimeMinisterName,long[] kingWangZhe,long[] dasimaWangZhe,long[] seniorGeneralWangZhe,long[] marshalWangZhe,long[] primeMinisterWangZhe,long[] policeByKingWangZhe,long[] yushidafuByKingWangZhe,long[] policeByMarshalWangZhe,long[] yushidafuByPrimeMinisterWangZhe){
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
		this.kingWangZhe = kingWangZhe;
		this.dasimaWangZhe = dasimaWangZhe;
		this.seniorGeneralWangZhe = seniorGeneralWangZhe;
		this.marshalWangZhe = marshalWangZhe;
		this.primeMinisterWangZhe = primeMinisterWangZhe;
		this.policeByKingWangZhe = policeByKingWangZhe;
		this.yushidafuByKingWangZhe = yushidafuByKingWangZhe;
		this.policeByMarshalWangZhe = policeByMarshalWangZhe;
		this.yushidafuByPrimeMinisterWangZhe = yushidafuByPrimeMinisterWangZhe;
	}

	public QUERY_COUNTRY_WANGZHEZHIYIN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
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
		kingWangZhe = new long[len];
		for(int i = 0 ; i < kingWangZhe.length ; i++){
			kingWangZhe[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		dasimaWangZhe = new long[len];
		for(int i = 0 ; i < dasimaWangZhe.length ; i++){
			dasimaWangZhe[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		seniorGeneralWangZhe = new long[len];
		for(int i = 0 ; i < seniorGeneralWangZhe.length ; i++){
			seniorGeneralWangZhe[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		marshalWangZhe = new long[len];
		for(int i = 0 ; i < marshalWangZhe.length ; i++){
			marshalWangZhe[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		primeMinisterWangZhe = new long[len];
		for(int i = 0 ; i < primeMinisterWangZhe.length ; i++){
			primeMinisterWangZhe[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		policeByKingWangZhe = new long[len];
		for(int i = 0 ; i < policeByKingWangZhe.length ; i++){
			policeByKingWangZhe[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		yushidafuByKingWangZhe = new long[len];
		for(int i = 0 ; i < yushidafuByKingWangZhe.length ; i++){
			yushidafuByKingWangZhe[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		policeByMarshalWangZhe = new long[len];
		for(int i = 0 ; i < policeByMarshalWangZhe.length ; i++){
			policeByMarshalWangZhe[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		yushidafuByPrimeMinisterWangZhe = new long[len];
		for(int i = 0 ; i < yushidafuByPrimeMinisterWangZhe.length ; i++){
			yushidafuByPrimeMinisterWangZhe[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70000AA4;
	}

	public String getTypeDescription() {
		return "QUERY_COUNTRY_WANGZHEZHIYIN_RES";
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
		len += kingWangZhe.length * 8;
		len += 4;
		len += dasimaWangZhe.length * 8;
		len += 4;
		len += seniorGeneralWangZhe.length * 8;
		len += 4;
		len += marshalWangZhe.length * 8;
		len += 4;
		len += primeMinisterWangZhe.length * 8;
		len += 4;
		len += policeByKingWangZhe.length * 8;
		len += 4;
		len += yushidafuByKingWangZhe.length * 8;
		len += 4;
		len += policeByMarshalWangZhe.length * 8;
		len += 4;
		len += yushidafuByPrimeMinisterWangZhe.length * 8;
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
			buffer.putInt(kingWangZhe.length);
			for(int i = 0 ; i < kingWangZhe.length; i++){
				buffer.putLong(kingWangZhe[i]);
			}
			buffer.putInt(dasimaWangZhe.length);
			for(int i = 0 ; i < dasimaWangZhe.length; i++){
				buffer.putLong(dasimaWangZhe[i]);
			}
			buffer.putInt(seniorGeneralWangZhe.length);
			for(int i = 0 ; i < seniorGeneralWangZhe.length; i++){
				buffer.putLong(seniorGeneralWangZhe[i]);
			}
			buffer.putInt(marshalWangZhe.length);
			for(int i = 0 ; i < marshalWangZhe.length; i++){
				buffer.putLong(marshalWangZhe[i]);
			}
			buffer.putInt(primeMinisterWangZhe.length);
			for(int i = 0 ; i < primeMinisterWangZhe.length; i++){
				buffer.putLong(primeMinisterWangZhe[i]);
			}
			buffer.putInt(policeByKingWangZhe.length);
			for(int i = 0 ; i < policeByKingWangZhe.length; i++){
				buffer.putLong(policeByKingWangZhe[i]);
			}
			buffer.putInt(yushidafuByKingWangZhe.length);
			for(int i = 0 ; i < yushidafuByKingWangZhe.length; i++){
				buffer.putLong(yushidafuByKingWangZhe[i]);
			}
			buffer.putInt(policeByMarshalWangZhe.length);
			for(int i = 0 ; i < policeByMarshalWangZhe.length; i++){
				buffer.putLong(policeByMarshalWangZhe[i]);
			}
			buffer.putInt(yushidafuByPrimeMinisterWangZhe.length);
			for(int i = 0 ; i < yushidafuByPrimeMinisterWangZhe.length; i++){
				buffer.putLong(yushidafuByPrimeMinisterWangZhe[i]);
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
	 *	国王，天尊，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public long[] getKingWangZhe(){
		return kingWangZhe;
	}

	/**
	 * 设置属性：
	 *	国王，天尊，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public void setKingWangZhe(long[] kingWangZhe){
		this.kingWangZhe = kingWangZhe;
	}

	/**
	 * 获取属性：
	 *	大司马，玄阴真圣，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public long[] getDasimaWangZhe(){
		return dasimaWangZhe;
	}

	/**
	 * 设置属性：
	 *	大司马，玄阴真圣，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public void setDasimaWangZhe(long[] dasimaWangZhe){
		this.dasimaWangZhe = dasimaWangZhe;
	}

	/**
	 * 获取属性：
	 *	大将军，纯阳真圣，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public long[] getSeniorGeneralWangZhe(){
		return seniorGeneralWangZhe;
	}

	/**
	 * 设置属性：
	 *	大将军，纯阳真圣，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public void setSeniorGeneralWangZhe(long[] seniorGeneralWangZhe){
		this.seniorGeneralWangZhe = seniorGeneralWangZhe;
	}

	/**
	 * 获取属性：
	 *	元帅，九天司命，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public long[] getMarshalWangZhe(){
		return marshalWangZhe;
	}

	/**
	 * 设置属性：
	 *	元帅，九天司命，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public void setMarshalWangZhe(long[] marshalWangZhe){
		this.marshalWangZhe = marshalWangZhe;
	}

	/**
	 * 获取属性：
	 *	宰相，九天司空，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public long[] getPrimeMinisterWangZhe(){
		return primeMinisterWangZhe;
	}

	/**
	 * 设置属性：
	 *	宰相，九天司空，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public void setPrimeMinisterWangZhe(long[] primeMinisterWangZhe){
		this.primeMinisterWangZhe = primeMinisterWangZhe;
	}

	/**
	 * 获取属性：
	 *	巡捕国王任命，真武神君，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public long[] getPoliceByKingWangZhe(){
		return policeByKingWangZhe;
	}

	/**
	 * 设置属性：
	 *	巡捕国王任命，真武神君，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public void setPoliceByKingWangZhe(long[] policeByKingWangZhe){
		this.policeByKingWangZhe = policeByKingWangZhe;
	}

	/**
	 * 获取属性：
	 *	御史大夫国王任命，道陵星君，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public long[] getYushidafuByKingWangZhe(){
		return yushidafuByKingWangZhe;
	}

	/**
	 * 设置属性：
	 *	御史大夫国王任命，道陵星君，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public void setYushidafuByKingWangZhe(long[] yushidafuByKingWangZhe){
		this.yushidafuByKingWangZhe = yushidafuByKingWangZhe;
	}

	/**
	 * 获取属性：
	 *	巡捕元帅任命，翔武神君，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public long[] getPoliceByMarshalWangZhe(){
		return policeByMarshalWangZhe;
	}

	/**
	 * 设置属性：
	 *	巡捕元帅任命，翔武神君，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public void setPoliceByMarshalWangZhe(long[] policeByMarshalWangZhe){
		this.policeByMarshalWangZhe = policeByMarshalWangZhe;
	}

	/**
	 * 获取属性：
	 *	御史大夫宰相任命，葛玄星君，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public long[] getYushidafuByPrimeMinisterWangZhe(){
		return yushidafuByPrimeMinisterWangZhe;
	}

	/**
	 * 设置属性：
	 *	御史大夫宰相任命，葛玄星君，王者之印使用次数，二维数组，数组第一位当天使用次数，数组第二位使用历史记录
	 */
	public void setYushidafuByPrimeMinisterWangZhe(long[] yushidafuByPrimeMinisterWangZhe){
		this.yushidafuByPrimeMinisterWangZhe = yushidafuByPrimeMinisterWangZhe;
	}

}