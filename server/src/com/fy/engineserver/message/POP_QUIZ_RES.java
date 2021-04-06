package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.quiz.Subject;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 弹出题（题干，选项，名次）<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>number</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>subject.subjectId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>subject.trunk.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>subject.trunk</td><td>String</td><td>subject.trunk.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>subject.branchA.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>subject.branchA</td><td>String</td><td>subject.branchA.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>subject.branchB.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>subject.branchB</td><td>String</td><td>subject.branchB.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>subject.branchC.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>subject.branchC</td><td>String</td><td>subject.branchC.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>subject.branchD.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>subject.branchD</td><td>String</td><td>subject.branchD.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>remainTime</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>remainQuizNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>amplifierNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class POP_QUIZ_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int number;
	Subject subject;
	int remainTime;
	int remainQuizNum;
	int helpNum;
	int amplifierNum;

	public POP_QUIZ_RES(){
	}

	public POP_QUIZ_RES(long seqNum,int number,Subject subject,int remainTime,int remainQuizNum,int helpNum,int amplifierNum){
		this.seqNum = seqNum;
		this.number = number;
		this.subject = subject;
		this.remainTime = remainTime;
		this.remainQuizNum = remainQuizNum;
		this.helpNum = helpNum;
		this.amplifierNum = amplifierNum;
	}

	public POP_QUIZ_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		number = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		subject = new Subject();
		subject.setSubjectId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		subject.setTrunk(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		subject.setBranchA(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		subject.setBranchB(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		subject.setBranchC(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		subject.setBranchD(new String(content,offset,len,"UTF-8"));
		offset += len;
		remainTime = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		remainQuizNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		helpNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		amplifierNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x700F0100;
	}

	public String getTypeDescription() {
		return "POP_QUIZ_RES";
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
		len += 4;
		len += 2;
		if(subject.getTrunk() != null){
			try{
			len += subject.getTrunk().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(subject.getBranchA() != null){
			try{
			len += subject.getBranchA().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(subject.getBranchB() != null){
			try{
			len += subject.getBranchB().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(subject.getBranchC() != null){
			try{
			len += subject.getBranchC().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(subject.getBranchD() != null){
			try{
			len += subject.getBranchD().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 4;
		len += 4;
		len += 4;
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

			buffer.putInt(number);
			buffer.putInt((int)subject.getSubjectId());
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = subject.getTrunk().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = subject.getBranchA().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = subject.getBranchB().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = subject.getBranchC().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = subject.getBranchD().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(remainTime);
			buffer.putInt(remainQuizNum);
			buffer.putInt(helpNum);
			buffer.putInt(amplifierNum);
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
	 *	这次活动的第几题
	 */
	public int getNumber(){
		return number;
	}

	/**
	 * 设置属性：
	 *	这次活动的第几题
	 */
	public void setNumber(int number){
		this.number = number;
	}

	/**
	 * 获取属性：
	 *	题目
	 */
	public Subject getSubject(){
		return subject;
	}

	/**
	 * 设置属性：
	 *	题目
	 */
	public void setSubject(Subject subject){
		this.subject = subject;
	}

	/**
	 * 获取属性：
	 *	剩余时间(秒)
	 */
	public int getRemainTime(){
		return remainTime;
	}

	/**
	 * 设置属性：
	 *	剩余时间(秒)
	 */
	public void setRemainTime(int remainTime){
		this.remainTime = remainTime;
	}

	/**
	 * 获取属性：
	 *	剩余题数
	 */
	public int getRemainQuizNum(){
		return remainQuizNum;
	}

	/**
	 * 设置属性：
	 *	剩余题数
	 */
	public void setRemainQuizNum(int remainQuizNum){
		this.remainQuizNum = remainQuizNum;
	}

	/**
	 * 获取属性：
	 *	可用帮助的次数
	 */
	public int getHelpNum(){
		return helpNum;
	}

	/**
	 * 设置属性：
	 *	可用帮助的次数
	 */
	public void setHelpNum(int helpNum){
		this.helpNum = helpNum;
	}

	/**
	 * 获取属性：
	 *	可用放大镜的次数
	 */
	public int getAmplifierNum(){
		return amplifierNum;
	}

	/**
	 * 设置属性：
	 *	可用放大镜的次数
	 */
	public void setAmplifierNum(int amplifierNum){
		this.amplifierNum = amplifierNum;
	}

}