package com.sqage.stat.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.sqage.stat.model.LibaoFlow;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 周月礼包日志<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>LibaoFlow.daoJuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>LibaoFlow.daoJuName</td><td>String</td><td>LibaoFlow.daoJuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>LibaoFlow.createDate</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>LibaoFlow.fenQu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>LibaoFlow.fenQu</td><td>String</td><td>LibaoFlow.fenQu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>LibaoFlow.count</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>LibaoFlow.danjia</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>LibaoFlow.type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>LibaoFlow.column1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>LibaoFlow.column1</td><td>String</td><td>LibaoFlow.column1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>LibaoFlow.column2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>LibaoFlow.column2</td><td>String</td><td>LibaoFlow.column2.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class LIBAO_LOG_REQ implements RequestMessage{

	static StatMessageFactory mf = StatMessageFactory.getInstance();

	long seqNum;
	LibaoFlow LibaoFlow;

	public LIBAO_LOG_REQ(long seqNum,LibaoFlow LibaoFlow){
		this.seqNum = seqNum;
		this.LibaoFlow = LibaoFlow;
	}

	public LIBAO_LOG_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		LibaoFlow = new LibaoFlow();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		LibaoFlow.setDaoJuName(new String(content,offset,len,"UTF-8"));
		offset += len;
		LibaoFlow.setCreateDate((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		LibaoFlow.setFenQu(new String(content,offset,len,"UTF-8"));
		offset += len;
		LibaoFlow.setCount((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		LibaoFlow.setDanjia((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		LibaoFlow.setType((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		LibaoFlow.setColumn1(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		LibaoFlow.setColumn2(new String(content,offset,len,"UTF-8"));
		offset += len;
	}

	public int getType() {
		return 0x00000019;
	}

	public String getTypeDescription() {
		return "LIBAO_LOG_REQ";
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
		if(LibaoFlow.getDaoJuName() != null){
			try{
			len += LibaoFlow.getDaoJuName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 2;
		if(LibaoFlow.getFenQu() != null){
			try{
			len += LibaoFlow.getFenQu().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 8;
		len += 4;
		len += 2;
		if(LibaoFlow.getColumn1() != null){
			try{
			len += LibaoFlow.getColumn1().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(LibaoFlow.getColumn2() != null){
			try{
			len += LibaoFlow.getColumn2().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
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

			byte[] tmpBytes1 = LibaoFlow.getDaoJuName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(LibaoFlow.getCreateDate());
			tmpBytes1 = LibaoFlow.getFenQu().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)LibaoFlow.getCount());
			buffer.putLong(LibaoFlow.getDanjia());
			buffer.putInt((int)LibaoFlow.getType());
			tmpBytes1 = LibaoFlow.getColumn1().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = LibaoFlow.getColumn2().getBytes("UTF-8");
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
	 *	周月礼包日志
	 */
	public LibaoFlow getLibaoFlow(){
		return LibaoFlow;
	}

	/**
	 * 设置属性：
	 *	周月礼包日志
	 */
	public void setLibaoFlow(LibaoFlow LibaoFlow){
		this.LibaoFlow = LibaoFlow;
	}

}