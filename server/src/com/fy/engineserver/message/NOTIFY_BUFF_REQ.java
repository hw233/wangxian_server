package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.buff.Buff;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器向客户端发送，通知客户端种植了一个新的BUFF<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>targetType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>targetId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buff.iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buff.iconId</td><td>String</td><td>buff.iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buff.level</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buff.invalidTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buff.startTime</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buff.seqId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buff.templateId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buff.groupId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buff.templateName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buff.templateName</td><td>String</td><td>buff.templateName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buff.description.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buff.description</td><td>String</td><td>buff.description.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buff.advantageous</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buff.forover</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * </table>
 */
public class NOTIFY_BUFF_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte targetType;
	long targetId;
	Buff buff;

	public NOTIFY_BUFF_REQ(){
	}

	public NOTIFY_BUFF_REQ(long seqNum,byte targetType,long targetId,Buff buff){
		this.seqNum = seqNum;
		this.targetType = targetType;
		this.targetId = targetId;
		this.buff = buff;
	}

	public NOTIFY_BUFF_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		targetType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		targetId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		buff = new Buff();
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		buff.setIconId(new String(content,offset,len,"UTF-8"));
		offset += len;
		buff.setLevel((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		buff.setInvalidTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		buff.setStartTime((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		buff.setSeqId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		buff.setTemplateId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		buff.setGroupId((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		buff.setTemplateName(new String(content,offset,len,"UTF-8"));
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		buff.setDescription(new String(content,offset,len,"UTF-8"));
		offset += len;
		buff.setAdvantageous(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
		buff.setForover(mf.byteArrayToNumber(content,offset,1) != 0);
		offset += 1;
	}

	public int getType() {
		return 0x000000D2;
	}

	public String getTypeDescription() {
		return "NOTIFY_BUFF_REQ";
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
		len += 8;
		len += 2;
		if(buff.getIconId() != null){
			try{
			len += buff.getIconId().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += 8;
		len += 8;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		if(buff.getTemplateName() != null){
			try{
			len += buff.getTemplateName().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		if(buff.getDescription() != null){
			try{
			len += buff.getDescription().getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
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

			buffer.put(targetType);
			buffer.putLong(targetId);
			byte[] tmpBytes1 ;
				try{
				tmpBytes1 = buff.getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt((int)buff.getLevel());
			buffer.putLong(buff.getInvalidTime());
			buffer.putLong(buff.getStartTime());
			buffer.putInt((int)buff.getSeqId());
			buffer.putInt((int)buff.getTemplateId());
			buffer.putInt((int)buff.getGroupId());
				try{
				tmpBytes1 = buff.getTemplateName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
				tmpBytes1 = buff.getDescription().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(buff.isAdvantageous()==false?0:1));
			buffer.put((byte)(buff.isForover()==false?0:1));
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
	 *	0标识玩家，1标识怪
	 */
	public byte getTargetType(){
		return targetType;
	}

	/**
	 * 设置属性：
	 *	0标识玩家，1标识怪
	 */
	public void setTargetType(byte targetType){
		this.targetType = targetType;
	}

	/**
	 * 获取属性：
	 *	某个玩家Id或者某个怪的Id
	 */
	public long getTargetId(){
		return targetId;
	}

	/**
	 * 设置属性：
	 *	某个玩家Id或者某个怪的Id
	 */
	public void setTargetId(long targetId){
		this.targetId = targetId;
	}

	/**
	 * 获取属性：
	 *	玩家或者怪身上的Buff
	 */
	public Buff getBuff(){
		return buff;
	}

	/**
	 * 设置属性：
	 *	玩家或者怪身上的Buff
	 */
	public void setBuff(Buff buff){
		this.buff = buff;
	}

}