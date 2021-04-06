package com.fy.engineserver.message;

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
 * <tr bgcolor="#FAFAFA" align="center"><td>savingRecord.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savingRecord[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingRecord[0]</td><td>String</td><td>savingRecord[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savingRecord[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingRecord[1]</td><td>String</td><td>savingRecord[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>savingRecord[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingRecord[2]</td><td>String</td><td>savingRecord[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>savingTotalNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>totalpagenum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SAVING_HISTORY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] savingRecord;
	int savingTotalNum;
	int totalpagenum;

	public SAVING_HISTORY_RES(){
	}

	public SAVING_HISTORY_RES(long seqNum,String[] savingRecord,int savingTotalNum,int totalpagenum){
		this.seqNum = seqNum;
		this.savingRecord = savingRecord;
		this.savingTotalNum = savingTotalNum;
		this.totalpagenum = totalpagenum;
	}

	public SAVING_HISTORY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		savingRecord = new String[len];
		for(int i = 0 ; i < savingRecord.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			savingRecord[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		savingTotalNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		totalpagenum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x7000EF01;
	}

	public String getTypeDescription() {
		return "SAVING_HISTORY_RES";
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
		for(int i = 0 ; i < savingRecord.length; i++){
			len += 2;
			try{
				len += savingRecord[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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

			buffer.putInt(savingRecord.length);
			for(int i = 0 ; i < savingRecord.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = savingRecord[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(savingTotalNum);
			buffer.putInt(totalpagenum);
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
	 *	充值记录的描述
	 */
	public String[] getSavingRecord(){
		return savingRecord;
	}

	/**
	 * 设置属性：
	 *	充值记录的描述
	 */
	public void setSavingRecord(String[] savingRecord){
		this.savingRecord = savingRecord;
	}

	/**
	 * 获取属性：
	 *	总充值数
	 */
	public int getSavingTotalNum(){
		return savingTotalNum;
	}

	/**
	 * 设置属性：
	 *	总充值数
	 */
	public void setSavingTotalNum(int savingTotalNum){
		this.savingTotalNum = savingTotalNum;
	}

	/**
	 * 获取属性：
	 *	总页数
	 */
	public int getTotalpagenum(){
		return totalpagenum;
	}

	/**
	 * 设置属性：
	 *	总页数
	 */
	public void setTotalpagenum(int totalpagenum){
		this.totalpagenum = totalpagenum;
	}

}