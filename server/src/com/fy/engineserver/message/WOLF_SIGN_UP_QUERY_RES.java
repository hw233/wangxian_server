package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求报名界面信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ruleInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ruleInfo</td><td>String</td><td>ruleInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>failInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>failInfo</td><td>String</td><td>failInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>successInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>successInfo</td><td>String</td><td>successInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fengShenInfo.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fengShenInfo</td><td>String</td><td>fengShenInfo.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>joinPlayerNum.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>joinPlayerNum</td><td>String</td><td>joinPlayerNum.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>joinTimes.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>joinTimes</td><td>String</td><td>joinTimes.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityTime</td><td>String</td><td>activityTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hasSignUp</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>signUpNums</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>joinNums</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class WOLF_SIGN_UP_QUERY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String ruleInfo;
	String failInfo;
	String successInfo;
	String fengShenInfo;
	String joinPlayerNum;
	String joinTimes;
	String activityTime;
	boolean hasSignUp;
	int signUpNums;
	int joinNums;

	public WOLF_SIGN_UP_QUERY_RES(){
	}

	public WOLF_SIGN_UP_QUERY_RES(long seqNum,String ruleInfo,String failInfo,String successInfo,String fengShenInfo,String joinPlayerNum,String joinTimes,String activityTime,boolean hasSignUp,int signUpNums,int joinNums){
		this.seqNum = seqNum;
		this.ruleInfo = ruleInfo;
		this.failInfo = failInfo;
		this.successInfo = successInfo;
		this.fengShenInfo = fengShenInfo;
		this.joinPlayerNum = joinPlayerNum;
		this.joinTimes = joinTimes;
		this.activityTime = activityTime;
		this.hasSignUp = hasSignUp;
		this.signUpNums = signUpNums;
		this.joinNums = joinNums;
	}

	public WOLF_SIGN_UP_QUERY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		ruleInfo = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		failInfo = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		successInfo = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		fengShenInfo = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		joinPlayerNum = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		joinTimes = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityTime = new String(content,offset,len,"UTF-8");
		offset += len;
		hasSignUp = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		signUpNums = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		joinNums = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70FFF005;
	}

	public String getTypeDescription() {
		return "WOLF_SIGN_UP_QUERY_RES";
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
			len +=ruleInfo.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=failInfo.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=successInfo.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=fengShenInfo.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=joinPlayerNum.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=joinTimes.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=activityTime.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = ruleInfo.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = failInfo.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = successInfo.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = fengShenInfo.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = joinPlayerNum.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = joinTimes.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = activityTime.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(hasSignUp==false?0:1));
			buffer.putInt(signUpNums);
			buffer.putInt(joinNums);
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
	 *	条件1
	 */
	public String getRuleInfo(){
		return ruleInfo;
	}

	/**
	 * 设置属性：
	 *	条件1
	 */
	public void setRuleInfo(String ruleInfo){
		this.ruleInfo = ruleInfo;
	}

	/**
	 * 获取属性：
	 *	失败信息
	 */
	public String getFailInfo(){
		return failInfo;
	}

	/**
	 * 设置属性：
	 *	失败信息
	 */
	public void setFailInfo(String failInfo){
		this.failInfo = failInfo;
	}

	/**
	 * 获取属性：
	 *	成功信息
	 */
	public String getSuccessInfo(){
		return successInfo;
	}

	/**
	 * 设置属性：
	 *	成功信息
	 */
	public void setSuccessInfo(String successInfo){
		this.successInfo = successInfo;
	}

	/**
	 * 获取属性：
	 *	封神信息
	 */
	public String getFengShenInfo(){
		return fengShenInfo;
	}

	/**
	 * 设置属性：
	 *	封神信息
	 */
	public void setFengShenInfo(String fengShenInfo){
		this.fengShenInfo = fengShenInfo;
	}

	/**
	 * 获取属性：
	 *	参与人数
	 */
	public String getJoinPlayerNum(){
		return joinPlayerNum;
	}

	/**
	 * 设置属性：
	 *	参与人数
	 */
	public void setJoinPlayerNum(String joinPlayerNum){
		this.joinPlayerNum = joinPlayerNum;
	}

	/**
	 * 获取属性：
	 *	参与次数
	 */
	public String getJoinTimes(){
		return joinTimes;
	}

	/**
	 * 设置属性：
	 *	参与次数
	 */
	public void setJoinTimes(String joinTimes){
		this.joinTimes = joinTimes;
	}

	/**
	 * 获取属性：
	 *	活动时间
	 */
	public String getActivityTime(){
		return activityTime;
	}

	/**
	 * 设置属性：
	 *	活动时间
	 */
	public void setActivityTime(String activityTime){
		this.activityTime = activityTime;
	}

	/**
	 * 获取属性：
	 *	是否已经报名
	 */
	public boolean getHasSignUp(){
		return hasSignUp;
	}

	/**
	 * 设置属性：
	 *	是否已经报名
	 */
	public void setHasSignUp(boolean hasSignUp){
		this.hasSignUp = hasSignUp;
	}

	/**
	 * 获取属性：
	 *	当前报名人数
	 */
	public int getSignUpNums(){
		return signUpNums;
	}

	/**
	 * 设置属性：
	 *	当前报名人数
	 */
	public void setSignUpNums(int signUpNums){
		this.signUpNums = signUpNums;
	}

	/**
	 * 获取属性：
	 *	参加次数
	 */
	public int getJoinNums(){
		return joinNums;
	}

	/**
	 * 设置属性：
	 *	参加次数
	 */
	public void setJoinNums(int joinNums){
		this.joinNums = joinNums;
	}

}