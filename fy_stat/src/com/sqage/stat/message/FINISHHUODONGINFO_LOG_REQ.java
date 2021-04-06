package com.sqage.stat.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.sqage.stat.model.FinishHuoDonginfoFlow;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 完成活动日志<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>finishHuoDonginfoFlow.id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>finishHuoDonginfoFlow.fenQu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>finishHuoDonginfoFlow.fenQu</td><td>String</td><td>finishHuoDonginfoFlow.fenQu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>finishHuoDonginfoFlow.userName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>finishHuoDonginfoFlow.userName</td><td>String</td><td>finishHuoDonginfoFlow.userName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>finishHuoDonginfoFlow.taskName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>finishHuoDonginfoFlow.taskName</td><td>String</td><td>finishHuoDonginfoFlow.taskName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>finishHuoDonginfoFlow.status.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>finishHuoDonginfoFlow.status</td><td>String</td><td>finishHuoDonginfoFlow.status.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>finishHuoDonginfoFlow.getYOuXiBi</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>finishHuoDonginfoFlow.getWuPin</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>finishHuoDonginfoFlow.getDaoJu</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>finishHuoDonginfoFlow.award.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>finishHuoDonginfoFlow.award</td><td>String</td><td>finishHuoDonginfoFlow.award.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>finishHuoDonginfoFlow.date</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>finishHuoDonginfoFlow.jixing.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>finishHuoDonginfoFlow.jixing</td><td>String</td><td>finishHuoDonginfoFlow.jixing.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class FINISHHUODONGINFO_LOG_REQ implements RequestMessage{

	static StatMessageFactory mf = StatMessageFactory.getInstance();

	long seqNum;
	FinishHuoDonginfoFlow finishHuoDonginfoFlow;

	public FINISHHUODONGINFO_LOG_REQ(long seqNum,FinishHuoDonginfoFlow finishHuoDonginfoFlow){
		this.seqNum = seqNum;
		this.finishHuoDonginfoFlow = finishHuoDonginfoFlow;
	}

	public FINISHHUODONGINFO_LOG_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		finishHuoDonginfoFlow = new FinishHuoDonginfoFlow();
		finishHuoDonginfoFlow.setId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		finishHuoDonginfoFlow.setFenQu(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		finishHuoDonginfoFlow.setUserName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		finishHuoDonginfoFlow.setTaskName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		finishHuoDonginfoFlow.setStatus(new String(content,offset,len,"UTF-8"));
		offset += len;
		finishHuoDonginfoFlow.setGetYOuXiBi((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		finishHuoDonginfoFlow.setGetWuPin((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		finishHuoDonginfoFlow.setGetDaoJu((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		finishHuoDonginfoFlow.setAward(new String(content,offset,len,"UTF-8"));
		offset += len;
		finishHuoDonginfoFlow.setDate((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		finishHuoDonginfoFlow.setJixing(new String(content,offset,len,"UTF-8"));
		offset += len;
	}

	public int getType() {
		return 0x00000013;
	}

	public String getTypeDescription() {
		return "FINISHHUODONGINFO_LOG_REQ";
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
		len += 2;
		if(finishHuoDonginfoFlow.getFenQu() != null){
			try{
			len += finishHuoDonginfoFlow.getFenQu().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(finishHuoDonginfoFlow.getUserName() != null){
			try{
			len += finishHuoDonginfoFlow.getUserName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(finishHuoDonginfoFlow.getTaskName() != null){
			try{
			len += finishHuoDonginfoFlow.getTaskName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(finishHuoDonginfoFlow.getStatus() != null){
			try{
			len += finishHuoDonginfoFlow.getStatus().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		if(finishHuoDonginfoFlow.getAward() != null){
			try{
			len += finishHuoDonginfoFlow.getAward().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 2;
		if(finishHuoDonginfoFlow.getJixing() != null){
			try{
			len += finishHuoDonginfoFlow.getJixing().getBytes("UTF-8").length;
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

			buffer.putLong(finishHuoDonginfoFlow.getId());
			byte[] tmpBytes1 = finishHuoDonginfoFlow.getFenQu().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = finishHuoDonginfoFlow.getUserName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = finishHuoDonginfoFlow.getTaskName().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = finishHuoDonginfoFlow.getStatus().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)finishHuoDonginfoFlow.getGetYOuXiBi());
			buffer.putInt((int)finishHuoDonginfoFlow.getGetWuPin());
			buffer.putInt((int)finishHuoDonginfoFlow.getGetDaoJu());
			tmpBytes1 = finishHuoDonginfoFlow.getAward().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(finishHuoDonginfoFlow.getDate());
			tmpBytes1 = finishHuoDonginfoFlow.getJixing().getBytes("UTF-8");
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
	 *	完成活动日志
	 */
	public FinishHuoDonginfoFlow getFinishHuoDonginfoFlow(){
		return finishHuoDonginfoFlow;
	}

	/**
	 * 设置属性：
	 *	完成活动日志
	 */
	public void setFinishHuoDonginfoFlow(FinishHuoDonginfoFlow finishHuoDonginfoFlow){
		this.finishHuoDonginfoFlow = finishHuoDonginfoFlow;
	}

}