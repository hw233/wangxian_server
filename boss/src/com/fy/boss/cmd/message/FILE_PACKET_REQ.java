package com.fy.boss.cmd.message;

import com.fy.boss.cmd.message.CMDMessageFactory;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 拷贝数据包<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>filedata.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>filedata</td><td>byte[]</td><td>filedata.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>total</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>filename.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>filename</td><td>String</td><td>filename.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savepath.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savepath</td><td>String</td><td>savepath.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class FILE_PACKET_REQ implements RequestMessage{

	static CMDMessageFactory mf = CMDMessageFactory.getInstance();

	long seqNum;
	byte[] filedata;
	int total;
	int index;
	String filename;
	String savepath;

	public FILE_PACKET_REQ(long seqNum,byte[] filedata,int total,int index,String filename,String savepath){
		this.seqNum = seqNum;
		this.filedata = filedata;
		this.total = total;
		this.index = index;
		this.filename = filename;
		this.savepath = savepath;
	}

	public FILE_PACKET_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		filedata = new byte[len];
		System.arraycopy(content,offset,filedata,0,len);
		offset += len;
		total = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		filename = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		savepath = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x0000A001;
	}

	public String getTypeDescription() {
		return "FILE_PACKET_REQ";
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
		len += 4;
		len += filedata.length;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=filename.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=savepath.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
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

			buffer.putInt(filedata.length);
			buffer.put(filedata);
			buffer.putInt(total);
			buffer.putInt(index);
			byte[] tmpBytes1 = filename.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = savepath.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	文件数据
	 */
	public byte[] getFiledata(){
		return filedata;
	}

	/**
	 * 设置属性：
	 *	文件数据
	 */
	public void setFiledata(byte[] filedata){
		this.filedata = filedata;
	}

	/**
	 * 获取属性：
	 *	数据包个数
	 */
	public int getTotal(){
		return total;
	}

	/**
	 * 设置属性：
	 *	数据包个数
	 */
	public void setTotal(int total){
		this.total = total;
	}

	/**
	 * 获取属性：
	 *	数据包下标
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	数据包下标
	 */
	public void setIndex(int index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	文件名称
	 */
	public String getFilename(){
		return filename;
	}

	/**
	 * 设置属性：
	 *	文件名称
	 */
	public void setFilename(String filename){
		this.filename = filename;
	}

	/**
	 * 获取属性：
	 *	保存路径
	 */
	public String getSavepath(){
		return savepath;
	}

	/**
	 * 设置属性：
	 *	保存路径
	 */
	public void setSavepath(String savepath){
		this.savepath = savepath;
	}

}
