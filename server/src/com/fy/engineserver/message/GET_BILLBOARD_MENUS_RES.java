package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.newBillboard.BillboardMenu;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 打开排行榜<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus.length</td><td>int</td><td>4个字节</td><td>BillboardMenu数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[0].menuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[0].menuName</td><td>String</td><td>billboardMenus[0].menuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[0].subMenus.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[0].subMenus[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[0].subMenus[0]</td><td>String</td><td>billboardMenus[0].subMenus[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[0].subMenus[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[0].subMenus[1]</td><td>String</td><td>billboardMenus[0].subMenus[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[0].subMenus[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[0].subMenus[2]</td><td>String</td><td>billboardMenus[0].subMenus[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[0].menuMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[0].subMenuMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[1].menuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[1].menuName</td><td>String</td><td>billboardMenus[1].menuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[1].subMenus.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[1].subMenus[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[1].subMenus[0]</td><td>String</td><td>billboardMenus[1].subMenus[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[1].subMenus[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[1].subMenus[1]</td><td>String</td><td>billboardMenus[1].subMenus[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[1].subMenus[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[1].subMenus[2]</td><td>String</td><td>billboardMenus[1].subMenus[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[1].menuMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[1].subMenuMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[2].menuName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[2].menuName</td><td>String</td><td>billboardMenus[2].menuName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[2].subMenus.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[2].subMenus[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[2].subMenus[0]</td><td>String</td><td>billboardMenus[2].subMenus[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[2].subMenus[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[2].subMenus[1]</td><td>String</td><td>billboardMenus[2].subMenus[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[2].subMenus[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[2].subMenus[2]</td><td>String</td><td>billboardMenus[2].subMenus[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>billboardMenus[2].menuMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>billboardMenus[2].subMenuMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class GET_BILLBOARD_MENUS_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	BillboardMenu[] billboardMenus;

	public GET_BILLBOARD_MENUS_RES(){
	}

	public GET_BILLBOARD_MENUS_RES(long seqNum,BillboardMenu[] billboardMenus){
		this.seqNum = seqNum;
		this.billboardMenus = billboardMenus;
	}

	public GET_BILLBOARD_MENUS_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		billboardMenus = new BillboardMenu[len];
		for(int i = 0 ; i < billboardMenus.length ; i++){
			billboardMenus[i] = new BillboardMenu();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			billboardMenus[i].setMenuName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] subMenus_0001 = new String[len];
			for(int j = 0 ; j < subMenus_0001.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				subMenus_0001[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			billboardMenus[i].setSubMenus(subMenus_0001);
			billboardMenus[i].setMenuMaxNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			billboardMenus[i].setSubMenuMaxNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x0F70001A;
	}

	public String getTypeDescription() {
		return "GET_BILLBOARD_MENUS_RES";
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
		for(int i = 0 ; i < billboardMenus.length ; i++){
			len += 2;
			if(billboardMenus[i].getMenuName() != null){
				try{
				len += billboardMenus[i].getMenuName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			String[] subMenus = billboardMenus[i].getSubMenus();
			for(int j = 0 ; j < subMenus.length; j++){
				len += 2;
				try{
					len += subMenus[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
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

			buffer.putInt(billboardMenus.length);
			for(int i = 0 ; i < billboardMenus.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = billboardMenus[i].getMenuName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(billboardMenus[i].getSubMenus().length);
				String[] subMenus_0002 = billboardMenus[i].getSubMenus();
				for(int j = 0 ; j < subMenus_0002.length ; j++){
				try{
					buffer.putShort((short)subMenus_0002[j].getBytes("UTF-8").length);
					buffer.put(subMenus_0002[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
				buffer.putInt((int)billboardMenus[i].getMenuMaxNum());
				buffer.putInt((int)billboardMenus[i].getSubMenuMaxNum());
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
	 *	排行榜菜单
	 */
	public BillboardMenu[] getBillboardMenus(){
		return billboardMenus;
	}

	/**
	 * 设置属性：
	 *	排行榜菜单
	 */
	public void setBillboardMenus(BillboardMenu[] billboardMenus){
		this.billboardMenus = billboardMenus;
	}

}