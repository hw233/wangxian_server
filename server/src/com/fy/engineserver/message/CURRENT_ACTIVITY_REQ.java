package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.notice.ActivityNotice;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys.length</td><td>int</td><td>4个字节</td><td>ActivityNotice数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].activityId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].activityName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].activityName</td><td>String</td><td>activitys[0].activityName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].iconName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].iconName</td><td>String</td><td>activitys[0].iconName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].needSignup</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[0].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[0].des</td><td>String</td><td>activitys[0].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].activityId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].activityName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].activityName</td><td>String</td><td>activitys[1].activityName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].iconName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].iconName</td><td>String</td><td>activitys[1].iconName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].needSignup</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[1].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[1].des</td><td>String</td><td>activitys[1].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].activityId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].activityName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].activityName</td><td>String</td><td>activitys[2].activityName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].iconName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].iconName</td><td>String</td><td>activitys[2].iconName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].needSignup</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>activitys[2].des.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>activitys[2].des</td><td>String</td><td>activitys[2].des.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CURRENT_ACTIVITY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ActivityNotice[] activitys;

	public CURRENT_ACTIVITY_REQ(){
	}

	public CURRENT_ACTIVITY_REQ(long seqNum,ActivityNotice[] activitys){
		this.seqNum = seqNum;
		this.activitys = activitys;
	}

	public CURRENT_ACTIVITY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		activitys = new ActivityNotice[len];
		for(int i = 0 ; i < activitys.length ; i++){
			activitys[i] = new ActivityNotice();
			activitys[i].setActivityId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activitys[i].setActivityName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activitys[i].setIconName(new String(content,offset,len,"UTF-8"));
			offset += len;
			activitys[i].setNeedSignup(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			activitys[i].setDes(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x0E0EAE01;
	}

	public String getTypeDescription() {
		return "CURRENT_ACTIVITY_REQ";
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
		for(int i = 0 ; i < activitys.length ; i++){
			len += 4;
			len += 2;
			if(activitys[i].getActivityName() != null){
				try{
				len += activitys[i].getActivityName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(activitys[i].getIconName() != null){
				try{
				len += activitys[i].getIconName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			if(activitys[i].getDes() != null){
				try{
				len += activitys[i].getDes().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
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

			buffer.putInt(activitys.length);
			for(int i = 0 ; i < activitys.length ; i++){
				buffer.putInt((int)activitys[i].getActivityId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = activitys[i].getActivityName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = activitys[i].getIconName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(activitys[i].isNeedSignup()==false?0:1));
				try{
				tmpBytes2 = activitys[i].getDes().getBytes("UTF-8");
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
		int newPos = buffer.position();
		buffer.position(oldPos);
		buffer.put(mf.numberToByteArray(newPos-oldPos,mf.getNumOfByteForMessageLength()));
		buffer.position(newPos);
		return newPos-oldPos;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public ActivityNotice[] getActivitys(){
		return activitys;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setActivitys(ActivityNotice[] activitys){
		this.activitys = activitys;
	}

}