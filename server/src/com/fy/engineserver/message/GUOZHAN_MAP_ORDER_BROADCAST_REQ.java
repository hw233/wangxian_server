package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.fy.engineserver.guozhan.GuozhanMapPoint;
import com.xuanzhi.tools.transport.RequestMessage;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 对国战一方发布国战指挥员在国战小地图上的命令<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerGuozhanType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>aimId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>canMakeOrder</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints.length</td><td>int</td><td>4个字节</td><td>GuozhanMapPoint数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[0].mapName</td><td>String</td><td>mapPoints[0].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[0].name</td><td>String</td><td>mapPoints[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].ownerType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[1].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].mapName</td><td>String</td><td>mapPoints[1].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].name</td><td>String</td><td>mapPoints[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[1].ownerType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[2].mapName</td><td>String</td><td>mapPoints[2].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[2].name</td><td>String</td><td>mapPoints[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].ownerType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class GUOZHAN_MAP_ORDER_BROADCAST_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte playerGuozhanType;
	int aimId;
	boolean canMakeOrder;
	GuozhanMapPoint[] mapPoints;

	public GUOZHAN_MAP_ORDER_BROADCAST_REQ(long seqNum,byte playerGuozhanType,int aimId,boolean canMakeOrder,GuozhanMapPoint[] mapPoints){
		this.seqNum = seqNum;
		this.playerGuozhanType = playerGuozhanType;
		this.aimId = aimId;
		this.canMakeOrder = canMakeOrder;
		this.mapPoints = mapPoints;
	}

	public GUOZHAN_MAP_ORDER_BROADCAST_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerGuozhanType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		aimId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		canMakeOrder = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		mapPoints = new GuozhanMapPoint[len];
		for(int i = 0 ; i < mapPoints.length ; i++){
			mapPoints[i] = new GuozhanMapPoint();
			mapPoints[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			mapPoints[i].setMapName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			mapPoints[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			mapPoints[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			mapPoints[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			mapPoints[i].setOwnerType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
	}

	public int getType() {
		return 0x00EE0017;
	}

	public String getTypeDescription() {
		return "GUOZHAN_MAP_ORDER_BROADCAST_REQ";
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
		len += 4;
		len += 1;
		len += 4;
		for(int i = 0 ; i < mapPoints.length ; i++){
			len += 4;
			len += 2;
			if(mapPoints[i].getMapName() != null){
				try{
				len += mapPoints[i].getMapName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(mapPoints[i].getName() != null){
				try{
				len += mapPoints[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			len += 1;
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

			buffer.put(playerGuozhanType);
			buffer.putInt(aimId);
			buffer.put((byte)(canMakeOrder==false?0:1));
			buffer.putInt(mapPoints.length);
			for(int i = 0 ; i < mapPoints.length ; i++){
				buffer.putInt((int)mapPoints[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = mapPoints[i].getMapName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = mapPoints[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)mapPoints[i].getX());
				buffer.putInt((int)mapPoints[i].getY());
				buffer.put((byte)mapPoints[i].getOwnerType());
			}
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public byte getPlayerGuozhanType(){
		return playerGuozhanType;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setPlayerGuozhanType(byte playerGuozhanType){
		this.playerGuozhanType = playerGuozhanType;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getAimId(){
		return aimId;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setAimId(int aimId){
		this.aimId = aimId;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public boolean getCanMakeOrder(){
		return canMakeOrder;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setCanMakeOrder(boolean canMakeOrder){
		this.canMakeOrder = canMakeOrder;
	}

	/**
	 * 获取属性：
	 *	当前国战地图中的点的状态
	 */
	public GuozhanMapPoint[] getMapPoints(){
		return mapPoints;
	}

	/**
	 * 设置属性：
	 *	当前国战地图中的点的状态
	 */
	public void setMapPoints(GuozhanMapPoint[] mapPoints){
		this.mapPoints = mapPoints;
	}

}