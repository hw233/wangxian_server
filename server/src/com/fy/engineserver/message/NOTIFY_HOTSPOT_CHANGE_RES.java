package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.hotspot.HotspotClientInfo;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知客户端热点数据变化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>allLen</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos.length</td><td>int</td><td>4个字节</td><td>HotspotClientInfo数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[0].hotspotID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[0].name</td><td>String</td><td>hotspotClientInfos[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[0].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[0].icon</td><td>String</td><td>hotspotClientInfos[0].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[0].isSee</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[0].isOver</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[1].hotspotID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[1].name</td><td>String</td><td>hotspotClientInfos[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[1].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[1].icon</td><td>String</td><td>hotspotClientInfos[1].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[1].isSee</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[1].isOver</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[2].hotspotID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[2].name</td><td>String</td><td>hotspotClientInfos[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[2].icon.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[2].icon</td><td>String</td><td>hotspotClientInfos[2].icon.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hotspotClientInfos[2].isSee</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hotspotClientInfos[2].isOver</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NOTIFY_HOTSPOT_CHANGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int allLen;
	HotspotClientInfo[] hotspotClientInfos;

	public NOTIFY_HOTSPOT_CHANGE_RES(){
	}

	public NOTIFY_HOTSPOT_CHANGE_RES(long seqNum,int allLen,HotspotClientInfo[] hotspotClientInfos){
		this.seqNum = seqNum;
		this.allLen = allLen;
		this.hotspotClientInfos = hotspotClientInfos;
	}

	public NOTIFY_HOTSPOT_CHANGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		allLen = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		hotspotClientInfos = new HotspotClientInfo[len];
		for(int i = 0 ; i < hotspotClientInfos.length ; i++){
			hotspotClientInfos[i] = new HotspotClientInfo();
			hotspotClientInfos[i].setHotspotID((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			hotspotClientInfos[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			hotspotClientInfos[i].setIcon(new String(content,offset,len,"UTF-8"));
			offset += len;
			hotspotClientInfos[i].setIsSee(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			hotspotClientInfos[i].setIsOver(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
		}
	}

	public int getType() {
		return 0x0F400001;
	}

	public String getTypeDescription() {
		return "NOTIFY_HOTSPOT_CHANGE_RES";
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
		for(int i = 0 ; i < hotspotClientInfos.length ; i++){
			len += 4;
			len += 2;
			if(hotspotClientInfos[i].getName() != null){
				try{
				len += hotspotClientInfos[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(hotspotClientInfos[i].getIcon() != null){
				try{
				len += hotspotClientInfos[i].getIcon().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
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

			buffer.putInt(allLen);
			buffer.putInt(hotspotClientInfos.length);
			for(int i = 0 ; i < hotspotClientInfos.length ; i++){
				buffer.putInt((int)hotspotClientInfos[i].getHotspotID());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = hotspotClientInfos[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = hotspotClientInfos[i].getIcon().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(hotspotClientInfos[i].isIsSee()==false?0:1));
				buffer.put((byte)(hotspotClientInfos[i].isIsOver()==false?0:1));
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
	 *	热点总个数
	 */
	public int getAllLen(){
		return allLen;
	}

	/**
	 * 设置属性：
	 *	热点总个数
	 */
	public void setAllLen(int allLen){
		this.allLen = allLen;
	}

	/**
	 * 获取属性：
	 *	以开启的热点，客户端需要的热点数据
	 */
	public HotspotClientInfo[] getHotspotClientInfos(){
		return hotspotClientInfos;
	}

	/**
	 * 设置属性：
	 *	以开启的热点，客户端需要的热点数据
	 */
	public void setHotspotClientInfos(HotspotClientInfo[] hotspotClientInfos){
		this.hotspotClientInfos = hotspotClientInfos;
	}

}