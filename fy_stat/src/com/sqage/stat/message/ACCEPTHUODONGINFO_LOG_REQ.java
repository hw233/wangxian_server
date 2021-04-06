package com.sqage.stat.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.sqage.stat.model.AcceptHuoDonginfoFlow;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 接受活动日志<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>acceptHuoDonginfoFlow.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>acceptHuoDonginfoFlow.createDate</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>acceptHuoDonginfoFlow.fenQu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>acceptHuoDonginfoFlow.fenQu</td><td>String</td><td>acceptHuoDonginfoFlow.fenQu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>acceptHuoDonginfoFlow.userName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>acceptHuoDonginfoFlow.userName</td><td>String</td><td>acceptHuoDonginfoFlow.userName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>acceptHuoDonginfoFlow.gameLevel.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>acceptHuoDonginfoFlow.gameLevel</td><td>String</td><td>acceptHuoDonginfoFlow.gameLevel.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>acceptHuoDonginfoFlow.taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>acceptHuoDonginfoFlow.taskName</td><td>String</td><td>acceptHuoDonginfoFlow.taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>acceptHuoDonginfoFlow.taskType.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>acceptHuoDonginfoFlow.taskType</td><td>String</td><td>acceptHuoDonginfoFlow.taskType.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>acceptHuoDonginfoFlow.jixing.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>acceptHuoDonginfoFlow.jixing</td><td>String</td><td>acceptHuoDonginfoFlow.jixing.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class ACCEPTHUODONGINFO_LOG_REQ implements RequestMessage{

	static StatMessageFactory mf = StatMessageFactory.getInstance();

	long seqNum;
	AcceptHuoDonginfoFlow acceptHuoDonginfoFlow;

	public ACCEPTHUODONGINFO_LOG_REQ(long seqNum,AcceptHuoDonginfoFlow acceptHuoDonginfoFlow){
		this.seqNum = seqNum;
		this.acceptHuoDonginfoFlow = acceptHuoDonginfoFlow;
	}

	public ACCEPTHUODONGINFO_LOG_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		acceptHuoDonginfoFlow = new AcceptHuoDonginfoFlow();
		acceptHuoDonginfoFlow.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		acceptHuoDonginfoFlow.setCreateDate((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		acceptHuoDonginfoFlow.setFenQu(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		acceptHuoDonginfoFlow.setUserName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		acceptHuoDonginfoFlow.setGameLevel(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		acceptHuoDonginfoFlow.setTaskName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		acceptHuoDonginfoFlow.setTaskType(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		acceptHuoDonginfoFlow.setJixing(new String(content,offset,len,"UTF-8"));
		offset += len;
	}

	public int getType() {
		return 0x00000012;
	}

	public String getTypeDescription() {
		return "ACCEPTHUODONGINFO_LOG_REQ";
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
		len += 8;
		len += 8;
		len += 2;
		if(acceptHuoDonginfoFlow.getFenQu() != null){
			try{
			len += acceptHuoDonginfoFlow.getFenQu().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(acceptHuoDonginfoFlow.getUserName() != null){
			try{
			len += acceptHuoDonginfoFlow.getUserName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(acceptHuoDonginfoFlow.getGameLevel() != null){
			try{
			len += acceptHuoDonginfoFlow.getGameLevel().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(acceptHuoDonginfoFlow.getTaskName() != null){
			try{
			len += acceptHuoDonginfoFlow.getTaskName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(acceptHuoDonginfoFlow.getTaskType() != null){
			try{
			len += acceptHuoDonginfoFlow.getTaskType().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(acceptHuoDonginfoFlow.getJixing() != null){
			try{
			len += acceptHuoDonginfoFlow.getJixing().getBytes("UTF-8").length;
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

			buffer.putLong(acceptHuoDonginfoFlow.getId());
			buffer.putLong(acceptHuoDonginfoFlow.getCreateDate());
			byte[] tmpBytes1 = acceptHuoDonginfoFlow.getFenQu().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = acceptHuoDonginfoFlow.getUserName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = acceptHuoDonginfoFlow.getGameLevel().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = acceptHuoDonginfoFlow.getTaskName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = acceptHuoDonginfoFlow.getTaskType().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = acceptHuoDonginfoFlow.getJixing().getBytes("UTF-8");
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
	 *	接受活动日志
	 */
	public AcceptHuoDonginfoFlow getAcceptHuoDonginfoFlow(){
		return acceptHuoDonginfoFlow;
	}

	/**
	 * 设置属性：
	 *	接受活动日志
	 */
	public void setAcceptHuoDonginfoFlow(AcceptHuoDonginfoFlow acceptHuoDonginfoFlow){
		this.acceptHuoDonginfoFlow = acceptHuoDonginfoFlow;
	}

}