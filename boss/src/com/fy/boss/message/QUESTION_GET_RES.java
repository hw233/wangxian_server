package com.fy.boss.message;

import java.nio.ByteBuffer;

import com.fy.boss.message.BossMessageFactory;
import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获得预定义问题<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>questions.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>questions[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>questions[0]</td><td>String</td><td>questions[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>questions[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>questions[1]</td><td>String</td><td>questions[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>questions[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>questions[2]</td><td>String</td><td>questions[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUESTION_GET_RES implements ResponseMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	String[] questions;

	public QUESTION_GET_RES(long seqNum,String[] questions){
		this.seqNum = seqNum;
		this.questions = questions;
	}

	public QUESTION_GET_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 5024000) throw new Exception("array length ["+len+"] big than the max length [5024000]");
		questions = new String[len];
		for(int i = 0 ; i < questions.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
			questions[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x8000E004;
	}

	public String getTypeDescription() {
		return "QUESTION_GET_RES";
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
		for(int i = 0 ; i < questions.length; i++){
			len += 2;
			try{
				len += questions[i].getBytes("UTF-8").length;
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

			buffer.putInt(questions.length);
			for(int i = 0 ; i < questions.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = questions[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	预定义的问题
	 */
	public String[] getQuestions(){
		return questions;
	}

	/**
	 * 设置属性：
	 *	预定义的问题
	 */
	public void setQuestions(String[] questions){
		this.questions = questions;
	}

}