package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 发送国家官员信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
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
 * </table>
 */
public class QUERY_COUNTRY_OFFICER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String kingName;
	String dasimaName;
	String seniorGeneralName;
	String marshalName;
	String primeMinisterName;
	String policeByKingName;
	String yushidafuByKingName;
	String policeByMarshalName;
	String yushidafuByPrimeMinisterName;

	public QUERY_COUNTRY_OFFICER_RES(){
	}

	public QUERY_COUNTRY_OFFICER_RES(long seqNum,String kingName,String dasimaName,String seniorGeneralName,String marshalName,String primeMinisterName,String policeByKingName,String yushidafuByKingName,String policeByMarshalName,String yushidafuByPrimeMinisterName){
		this.seqNum = seqNum;
		this.kingName = kingName;
		this.dasimaName = dasimaName;
		this.seniorGeneralName = seniorGeneralName;
		this.marshalName = marshalName;
		this.primeMinisterName = primeMinisterName;
		this.policeByKingName = policeByKingName;
		this.yushidafuByKingName = yushidafuByKingName;
		this.policeByMarshalName = policeByMarshalName;
		this.yushidafuByPrimeMinisterName = yushidafuByPrimeMinisterName;
	}

	public QUERY_COUNTRY_OFFICER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
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
	}

	public int getType() {
		return 0x70FF0162;
	}

	public String getTypeDescription() {
		return "QUERY_COUNTRY_OFFICER_RES";
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

}