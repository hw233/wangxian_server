package com.fy.boss.message;

import com.fy.boss.message.BossMessageFactory;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求充值记录<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statusDesp.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statusDesp[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statusDesp[0]</td><td>String</td><td>statusDesp[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statusDesp[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statusDesp[1]</td><td>String</td><td>statusDesp[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>statusDesp[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>statusDesp[2]</td><td>String</td><td>statusDesp[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderId.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderId[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderId[0]</td><td>String</td><td>orderId[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderId[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderId[1]</td><td>String</td><td>orderId[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>orderId[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderId[2]</td><td>String</td><td>orderId[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>createTime.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>createTime</td><td>long[]</td><td>createTime.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingAmount.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savingAmount</td><td>long[]</td><td>savingAmount.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingMedium.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savingMedium[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingMedium[0]</td><td>String</td><td>savingMedium[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savingMedium[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingMedium[1]</td><td>String</td><td>savingMedium[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savingMedium[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingMedium[2]</td><td>String</td><td>savingMedium[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingTotalNum</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savingPageNum</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SAVING_RECORD_RES implements ResponseMessage{

	static BossMessageFactory mf = BossMessageFactory.getInstance();

	long seqNum;
	String[] statusDesp;
	String[] orderId;
	long[] createTime;
	long[] savingAmount;
	String[] savingMedium;
	long savingTotalNum;
	long savingPageNum;

	public SAVING_RECORD_RES(){
	}

	public SAVING_RECORD_RES(long seqNum,String[] statusDesp,String[] orderId,long[] createTime,long[] savingAmount,String[] savingMedium,long savingTotalNum,long savingPageNum){
		this.seqNum = seqNum;
		this.statusDesp = statusDesp;
		this.orderId = orderId;
		this.createTime = createTime;
		this.savingAmount = savingAmount;
		this.savingMedium = savingMedium;
		this.savingTotalNum = savingTotalNum;
		this.savingPageNum = savingPageNum;
	}

	public SAVING_RECORD_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 5024000) throw new Exception("array length ["+len+"] big than the max length [5024000]");
		statusDesp = new String[len];
		for(int i = 0 ; i < statusDesp.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
			statusDesp[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 5024000) throw new Exception("array length ["+len+"] big than the max length [5024000]");
		orderId = new String[len];
		for(int i = 0 ; i < orderId.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
			orderId[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 5024000) throw new Exception("array length ["+len+"] big than the max length [5024000]");
		createTime = new long[len];
		for(int i = 0 ; i < createTime.length ; i++){
			createTime[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 5024000) throw new Exception("array length ["+len+"] big than the max length [5024000]");
		savingAmount = new long[len];
		for(int i = 0 ; i < savingAmount.length ; i++){
			savingAmount[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 5024000) throw new Exception("array length ["+len+"] big than the max length [5024000]");
		savingMedium = new String[len];
		for(int i = 0 ; i < savingMedium.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 10240000) throw new Exception("string length ["+len+"] big than the max length [10240000]");
			savingMedium[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		savingTotalNum = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		savingPageNum = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x8000F002;
	}

	public String getTypeDescription() {
		return "SAVING_RECORD_RES";
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
		for(int i = 0 ; i < statusDesp.length; i++){
			len += 2;
			try{
				len += statusDesp[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < orderId.length; i++){
			len += 2;
			try{
				len += orderId[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += createTime.length * 8;
		len += 4;
		len += savingAmount.length * 8;
		len += 4;
		for(int i = 0 ; i < savingMedium.length; i++){
			len += 2;
			try{
				len += savingMedium[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 8;
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

			buffer.putInt(statusDesp.length);
			for(int i = 0 ; i < statusDesp.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = statusDesp[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(orderId.length);
			for(int i = 0 ; i < orderId.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = orderId[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(createTime.length);
			for(int i = 0 ; i < createTime.length; i++){
				buffer.putLong(createTime[i]);
			}
			buffer.putInt(savingAmount.length);
			for(int i = 0 ; i < savingAmount.length; i++){
				buffer.putLong(savingAmount[i]);
			}
			buffer.putInt(savingMedium.length);
			for(int i = 0 ; i < savingMedium.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = savingMedium[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putLong(savingTotalNum);
			buffer.putLong(savingPageNum);
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
	 *	订单状态
	 */
	public String[] getStatusDesp(){
		return statusDesp;
	}

	/**
	 * 设置属性：
	 *	订单状态
	 */
	public void setStatusDesp(String[] statusDesp){
		this.statusDesp = statusDesp;
	}

	/**
	 * 获取属性：
	 *	订单
	 */
	public String[] getOrderId(){
		return orderId;
	}

	/**
	 * 设置属性：
	 *	订单
	 */
	public void setOrderId(String[] orderId){
		this.orderId = orderId;
	}

	/**
	 * 获取属性：
	 *	订单发生时间
	 */
	public long[] getCreateTime(){
		return createTime;
	}

	/**
	 * 设置属性：
	 *	订单发生时间
	 */
	public void setCreateTime(long[] createTime){
		this.createTime = createTime;
	}

	/**
	 * 获取属性：
	 *	充值额度：分
	 */
	public long[] getSavingAmount(){
		return savingAmount;
	}

	/**
	 * 设置属性：
	 *	充值额度：分
	 */
	public void setSavingAmount(long[] savingAmount){
		this.savingAmount = savingAmount;
	}

	/**
	 * 获取属性：
	 *	充值介质
	 */
	public String[] getSavingMedium(){
		return savingMedium;
	}

	/**
	 * 设置属性：
	 *	充值介质
	 */
	public void setSavingMedium(String[] savingMedium){
		this.savingMedium = savingMedium;
	}

	/**
	 * 获取属性：
	 *	总充值记录数
	 */
	public long getSavingTotalNum(){
		return savingTotalNum;
	}

	/**
	 * 设置属性：
	 *	总充值记录数
	 */
	public void setSavingTotalNum(long savingTotalNum){
		this.savingTotalNum = savingTotalNum;
	}

	/**
	 * 获取属性：
	 *	总页数
	 */
	public long getSavingPageNum(){
		return savingPageNum;
	}

	/**
	 * 设置属性：
	 *	总页数
	 */
	public void setSavingPageNum(long savingPageNum){
		this.savingPageNum = savingPageNum;
	}

}