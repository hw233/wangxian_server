package com.sqage.stat.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.sqage.stat.model.Battle_PlayerStatFlow;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 每个人的功勋时间日志<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>battle_PlayerStatFlow.type.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>battle_PlayerStatFlow.type</td><td>String</td><td>battle_PlayerStatFlow.type.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>battle_PlayerStatFlow.fenqu.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>battle_PlayerStatFlow.fenqu</td><td>String</td><td>battle_PlayerStatFlow.fenqu.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>battle_PlayerStatFlow.gongxun.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>battle_PlayerStatFlow.gongxun</td><td>String</td><td>battle_PlayerStatFlow.gongxun.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>battle_PlayerStatFlow.PlayerCount</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>battle_PlayerStatFlow.createTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>battle_PlayerStatFlow.column1.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>battle_PlayerStatFlow.column1</td><td>String</td><td>battle_PlayerStatFlow.column1.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>battle_PlayerStatFlow.column2.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>battle_PlayerStatFlow.column2</td><td>String</td><td>battle_PlayerStatFlow.column2.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class BATTLE_PLAYERSTAT_LOG_REQ implements RequestMessage{

	static StatMessageFactory mf = StatMessageFactory.getInstance();

	long seqNum;
	Battle_PlayerStatFlow battle_PlayerStatFlow;

	public BATTLE_PLAYERSTAT_LOG_REQ(long seqNum,Battle_PlayerStatFlow battle_PlayerStatFlow){
		this.seqNum = seqNum;
		this.battle_PlayerStatFlow = battle_PlayerStatFlow;
	}

	public BATTLE_PLAYERSTAT_LOG_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		battle_PlayerStatFlow = new Battle_PlayerStatFlow();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		battle_PlayerStatFlow.setType(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		battle_PlayerStatFlow.setFenqu(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		battle_PlayerStatFlow.setGongxun(new String(content,offset,len,"UTF-8"));
		offset += len;
		battle_PlayerStatFlow.setPlayerCount((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		battle_PlayerStatFlow.setCreateTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		battle_PlayerStatFlow.setColumn1(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		battle_PlayerStatFlow.setColumn2(new String(content,offset,len,"UTF-8"));
		offset += len;
	}

	public int getType() {
		return 0x00000023;
	}

	public String getTypeDescription() {
		return "BATTLE_PLAYERSTAT_LOG_REQ";
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
		if(battle_PlayerStatFlow.getType() != null){
			try{
			len += battle_PlayerStatFlow.getType().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(battle_PlayerStatFlow.getFenqu() != null){
			try{
			len += battle_PlayerStatFlow.getFenqu().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(battle_PlayerStatFlow.getGongxun() != null){
			try{
			len += battle_PlayerStatFlow.getGongxun().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 8;
		len += 8;
		len += 2;
		if(battle_PlayerStatFlow.getColumn1() != null){
			try{
			len += battle_PlayerStatFlow.getColumn1().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(battle_PlayerStatFlow.getColumn2() != null){
			try{
			len += battle_PlayerStatFlow.getColumn2().getBytes("UTF-8").length;
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

			byte[] tmpBytes1 = battle_PlayerStatFlow.getType().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = battle_PlayerStatFlow.getFenqu().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = battle_PlayerStatFlow.getGongxun().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(battle_PlayerStatFlow.getPlayerCount());
			buffer.putLong(battle_PlayerStatFlow.getCreateTime());
			tmpBytes1 = battle_PlayerStatFlow.getColumn1().getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = battle_PlayerStatFlow.getColumn2().getBytes("UTF-8");
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
	 *	每个人的功勋时间日志
	 */
	public Battle_PlayerStatFlow getBattle_PlayerStatFlow(){
		return battle_PlayerStatFlow;
	}

	/**
	 * 设置属性：
	 *	每个人的功勋时间日志
	 */
	public void setBattle_PlayerStatFlow(Battle_PlayerStatFlow battle_PlayerStatFlow){
		this.battle_PlayerStatFlow = battle_PlayerStatFlow;
	}

}