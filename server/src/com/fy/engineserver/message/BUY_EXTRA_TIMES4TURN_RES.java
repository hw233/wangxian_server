package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.dailyTurnActivity.model.TurnModel4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 购买额外参与次数<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info.turnId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>info.turnName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info.turnName</td><td>String</td><td>info.turnName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>info.conditions.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info.conditions[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>info.conditions[0]</td><td>String</td><td>info.conditions[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info.conditions[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>info.conditions[1]</td><td>String</td><td>info.conditions[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info.conditions[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>info.conditions[2]</td><td>String</td><td>info.conditions[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>info.conditionStatus.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>info.conditionStatus</td><td>int[]</td><td>info.conditionStatus.length</td><td>*</td></tr>
 * </table>
 */
public class BUY_EXTRA_TIMES4TURN_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	TurnModel4Client info;

	public BUY_EXTRA_TIMES4TURN_RES(){
	}

	public BUY_EXTRA_TIMES4TURN_RES(long seqNum,TurnModel4Client info){
		this.seqNum = seqNum;
		this.info = info;
	}

	public BUY_EXTRA_TIMES4TURN_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		info = new TurnModel4Client();
		info.setTurnId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		info.setTurnName(new String(content,offset,len));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		String[] conditions_0001 = new String[len];
		for(int j = 0 ; j < conditions_0001.length ; j++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			conditions_0001[j] = new String(content,offset,len);
				offset += len;
		}
		info.setConditions(conditions_0001);
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		int[] conditionStatus_0002 = new int[len];
		for(int j = 0 ; j < conditionStatus_0002.length ; j++){
			conditionStatus_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		info.setConditionStatus(conditionStatus_0002);
	}

	public int getType() {
		return 0x70FF0075;
	}

	public String getTypeDescription() {
		return "BUY_EXTRA_TIMES4TURN_RES";
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
		len += 2;
		if(info.getTurnName() != null){
			len += info.getTurnName().getBytes().length;
		}
		len += 4;
		String[] conditions = info.getConditions();
		for(int j = 0 ; j < conditions.length; j++){
			len += 2;
			len += conditions[j].getBytes().length;
		}
		len += 4;
		len += info.getConditionStatus().length * 4;
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

			buffer.putInt((int)info.getTurnId());
			byte[] tmpBytes1 ;
			tmpBytes1 = info.getTurnName().getBytes();
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(info.getConditions().length);
			String[] conditions_0003 = info.getConditions();
			for(int j = 0 ; j < conditions_0003.length ; j++){
				buffer.putShort((short)conditions_0003[j].getBytes().length);
				buffer.put(conditions_0003[j].getBytes());
			}
			buffer.putInt(info.getConditionStatus().length);
			int[] conditionStatus_0004 = info.getConditionStatus();
			for(int j = 0 ; j < conditionStatus_0004.length ; j++){
				buffer.putInt(conditionStatus_0004[j]);
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
	 *	转盘信息
	 */
	public TurnModel4Client getInfo(){
		return info;
	}

	/**
	 * 设置属性：
	 *	转盘信息
	 */
	public void setInfo(TurnModel4Client info){
		this.info = info;
	}

}