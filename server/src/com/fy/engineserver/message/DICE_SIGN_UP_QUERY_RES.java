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
 * <tr bgcolor="#FAFAFA" align="center"><td>joinTimeMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>joinTimeMess</td><td>String</td><td>joinTimeMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>joinTypeMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>joinTypeMess</td><td>String</td><td>joinTypeMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>joinHelpMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>joinHelpMess</td><td>String</td><td>joinHelpMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityMess.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityMess</td><td>String</td><td>activityMess.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activityReward.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activityReward</td><td>String</td><td>activityReward.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>signUpNums</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hasSignUp</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nums</td><td>int[]</td><td>nums.length</td><td>*</td></tr>
 * </table>
 */
public class DICE_SIGN_UP_QUERY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String joinTimeMess;
	String joinTypeMess;
	String joinHelpMess;
	String activityMess;
	String activityReward;
	int signUpNums;
	boolean hasSignUp;
	long[] ids;
	int[] nums;

	public DICE_SIGN_UP_QUERY_RES(){
	}

	public DICE_SIGN_UP_QUERY_RES(long seqNum,String joinTimeMess,String joinTypeMess,String joinHelpMess,String activityMess,String activityReward,int signUpNums,boolean hasSignUp,long[] ids,int[] nums){
		this.seqNum = seqNum;
		this.joinTimeMess = joinTimeMess;
		this.joinTypeMess = joinTypeMess;
		this.joinHelpMess = joinHelpMess;
		this.activityMess = activityMess;
		this.activityReward = activityReward;
		this.signUpNums = signUpNums;
		this.hasSignUp = hasSignUp;
		this.ids = ids;
		this.nums = nums;
	}

	public DICE_SIGN_UP_QUERY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		joinTimeMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		joinTypeMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		joinHelpMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityMess = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		activityReward = new String(content,offset,len,"UTF-8");
		offset += len;
		signUpNums = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		hasSignUp = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
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
		nums = new int[len];
		for(int i = 0 ; i < nums.length ; i++){
			nums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FFF034;
	}

	public String getTypeDescription() {
		return "DICE_SIGN_UP_QUERY_RES";
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
			len +=joinTimeMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=joinTypeMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=joinHelpMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=activityMess.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=activityReward.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 1;
		len += 4;
		len += ids.length * 8;
		len += 4;
		len += nums.length * 4;
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
			 tmpBytes1 = joinTimeMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = joinTypeMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = joinHelpMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = activityMess.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = activityReward.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(signUpNums);
			buffer.put((byte)(hasSignUp==false?0:1));
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(nums.length);
			for(int i = 0 ; i < nums.length; i++){
				buffer.putInt(nums[i]);
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
	 *	参与时间描述
	 */
	public String getJoinTimeMess(){
		return joinTimeMess;
	}

	/**
	 * 设置属性：
	 *	参与时间描述
	 */
	public void setJoinTimeMess(String joinTimeMess){
		this.joinTimeMess = joinTimeMess;
	}

	/**
	 * 获取属性：
	 *	参与方式
	 */
	public String getJoinTypeMess(){
		return joinTypeMess;
	}

	/**
	 * 设置属性：
	 *	参与方式
	 */
	public void setJoinTypeMess(String joinTypeMess){
		this.joinTypeMess = joinTypeMess;
	}

	/**
	 * 获取属性：
	 *	进入描述
	 */
	public String getJoinHelpMess(){
		return joinHelpMess;
	}

	/**
	 * 设置属性：
	 *	进入描述
	 */
	public void setJoinHelpMess(String joinHelpMess){
		this.joinHelpMess = joinHelpMess;
	}

	/**
	 * 获取属性：
	 *	活动介绍
	 */
	public String getActivityMess(){
		return activityMess;
	}

	/**
	 * 设置属性：
	 *	活动介绍
	 */
	public void setActivityMess(String activityMess){
		this.activityMess = activityMess;
	}

	/**
	 * 获取属性：
	 *	活动奖励
	 */
	public String getActivityReward(){
		return activityReward;
	}

	/**
	 * 设置属性：
	 *	活动奖励
	 */
	public void setActivityReward(String activityReward){
		this.activityReward = activityReward;
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
	 *	奖励id
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	奖励id
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	奖励数量
	 */
	public int[] getNums(){
		return nums;
	}

	/**
	 * 设置属性：
	 *	奖励数量
	 */
	public void setNums(int[] nums){
		this.nums = nums;
	}

}