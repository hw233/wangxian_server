package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 队长修改团队物品分配规则。客户端、服务器均可发起<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>assignRule</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>colorType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class TEAM_SET_ASSIGN_RULE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte assignRule;
	byte colorType;

	public TEAM_SET_ASSIGN_RULE_REQ(){
	}

	public TEAM_SET_ASSIGN_RULE_REQ(long seqNum,byte assignRule,byte colorType){
		this.seqNum = seqNum;
		this.assignRule = assignRule;
		this.colorType = colorType;
	}

	public TEAM_SET_ASSIGN_RULE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		assignRule = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		colorType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x00000F0B;
	}

	public String getTypeDescription() {
		return "TEAM_SET_ASSIGN_RULE_REQ";
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
		len += 1;
		len += 1;
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

			buffer.put(assignRule);
			buffer.put(colorType);
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
	 *	物品分配规则：0标识自由分配，1标识顺序分配
	 */
	public byte getAssignRule(){
		return assignRule;
	}

	/**
	 * 设置属性：
	 *	物品分配规则：0标识自由分配，1标识顺序分配
	 */
	public void setAssignRule(byte assignRule){
		this.assignRule = assignRule;
	}

	/**
	 * 获取属性：
	 *	区分物品好坏的颜色(现在没用)
	 */
	public byte getColorType(){
		return colorType;
	}

	/**
	 * 设置属性：
	 *	区分物品好坏的颜色(现在没用)
	 */
	public void setColorType(byte colorType){
		this.colorType = colorType;
	}

}