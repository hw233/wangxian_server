package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.guozhan.GuozhanMapPoint;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询国战小地图上几个点的信息，对于玩家，两个点如果都被本方占据，则连接为实线，否则虚线；对于玩家，如果点被本方占据，则显示旗子，否则显示x<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>playerGuozhanType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>canMakeOrder</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints.length</td><td>int</td><td>4个字节</td><td>GuozhanMapPoint数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[0].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].mapName</td><td>String</td><td>mapPoints[0].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].name</td><td>String</td><td>mapPoints[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[0].bigBoss</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].littleBoss</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[0].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[0].ownerType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[0].ownerCountryId</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[1].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].mapName</td><td>String</td><td>mapPoints[1].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].name</td><td>String</td><td>mapPoints[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[1].bigBoss</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].littleBoss</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[1].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[1].ownerType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[1].ownerCountryId</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[2].mapName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].mapName</td><td>String</td><td>mapPoints[2].mapName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].name</td><td>String</td><td>mapPoints[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[2].bigBoss</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].littleBoss</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[2].x</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].y</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapPoints[2].ownerType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapPoints[2].ownerCountryId</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lineAIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lineAIds</td><td>int[]</td><td>lineAIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>lineBIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>lineBIds</td><td>int[]</td><td>lineBIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentAimId</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class GUOZHAN_MAP_POINT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte playerGuozhanType;
	boolean canMakeOrder;
	GuozhanMapPoint[] mapPoints;
	int[] lineAIds;
	int[] lineBIds;
	int currentAimId;

	public GUOZHAN_MAP_POINT_RES(){
	}

	public GUOZHAN_MAP_POINT_RES(long seqNum,byte playerGuozhanType,boolean canMakeOrder,GuozhanMapPoint[] mapPoints,int[] lineAIds,int[] lineBIds,int currentAimId){
		this.seqNum = seqNum;
		this.playerGuozhanType = playerGuozhanType;
		this.canMakeOrder = canMakeOrder;
		this.mapPoints = mapPoints;
		this.lineAIds = lineAIds;
		this.lineBIds = lineBIds;
		this.currentAimId = currentAimId;
	}

	public GUOZHAN_MAP_POINT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		playerGuozhanType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
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
			mapPoints[i].setBigBoss(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			mapPoints[i].setLittleBoss(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			mapPoints[i].setX((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			mapPoints[i].setY((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			mapPoints[i].setOwnerType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			mapPoints[i].setOwnerCountryId((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lineAIds = new int[len];
		for(int i = 0 ; i < lineAIds.length ; i++){
			lineAIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		lineBIds = new int[len];
		for(int i = 0 ; i < lineBIds.length ; i++){
			lineBIds[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		currentAimId = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70EE0015;
	}

	public String getTypeDescription() {
		return "GUOZHAN_MAP_POINT_RES";
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
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += 1;
			len += 1;
		}
		len += 4;
		len += lineAIds.length * 4;
		len += 4;
		len += lineBIds.length * 4;
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

			buffer.put(playerGuozhanType);
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
				buffer.put((byte)(mapPoints[i].isBigBoss()==false?0:1));
				buffer.put((byte)(mapPoints[i].isLittleBoss()==false?0:1));
				buffer.putInt((int)mapPoints[i].getX());
				buffer.putInt((int)mapPoints[i].getY());
				buffer.put((byte)mapPoints[i].getOwnerType());
				buffer.put((byte)mapPoints[i].getOwnerCountryId());
			}
			buffer.putInt(lineAIds.length);
			for(int i = 0 ; i < lineAIds.length; i++){
				buffer.putInt(lineAIds[i]);
			}
			buffer.putInt(lineBIds.length);
			for(int i = 0 ; i < lineBIds.length; i++){
				buffer.putInt(lineBIds[i]);
			}
			buffer.putInt(currentAimId);
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
	 *	国战地图中的点
	 */
	public GuozhanMapPoint[] getMapPoints(){
		return mapPoints;
	}

	/**
	 * 设置属性：
	 *	国战地图中的点
	 */
	public void setMapPoints(GuozhanMapPoint[] mapPoints){
		this.mapPoints = mapPoints;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getLineAIds(){
		return lineAIds;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setLineAIds(int[] lineAIds){
		this.lineAIds = lineAIds;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getLineBIds(){
		return lineBIds;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setLineBIds(int[] lineBIds){
		this.lineBIds = lineBIds;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int getCurrentAimId(){
		return currentAimId;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setCurrentAimId(int currentAimId){
		this.currentAimId = currentAimId;
	}

}