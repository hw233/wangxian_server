package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询游戏的拓扑结构<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapNames[0]</td><td>String</td><td>mapNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapNames[1]</td><td>String</td><td>mapNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapNames[2]</td><td>String</td><td>mapNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>displayMapNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>displayMapNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>displayMapNames[0]</td><td>String</td><td>displayMapNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>displayMapNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>displayMapNames[1]</td><td>String</td><td>displayMapNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>displayMapNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>displayMapNames[2]</td><td>String</td><td>displayMapNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>neighborNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>neighborNums</td><td>byte[]</td><td>neighborNums.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>neighbors.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>neighbors</td><td>short[]</td><td>neighbors.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>neighborTypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>neighborTypes</td><td>short[]</td><td>neighborTypes.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_TOPO_DIAGRAM_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String[] mapNames;
	String[] displayMapNames;
	byte[] neighborNums;
	short[] neighbors;
	short[] neighborTypes;

	public QUERY_TOPO_DIAGRAM_RES(){
	}

	public QUERY_TOPO_DIAGRAM_RES(long seqNum,String[] mapNames,String[] displayMapNames,byte[] neighborNums,short[] neighbors,short[] neighborTypes){
		this.seqNum = seqNum;
		this.mapNames = mapNames;
		this.displayMapNames = displayMapNames;
		this.neighborNums = neighborNums;
		this.neighbors = neighbors;
		this.neighborTypes = neighborTypes;
	}

	public QUERY_TOPO_DIAGRAM_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		mapNames = new String[len];
		for(int i = 0 ; i < mapNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			mapNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		displayMapNames = new String[len];
		for(int i = 0 ; i < displayMapNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			displayMapNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		neighborNums = new byte[len];
		System.arraycopy(content,offset,neighborNums,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 2048) throw new Exception("array length ["+len+"] big than the max length [2048]");
		neighbors = new short[len];
		for(int i = 0 ; i < neighbors.length ; i++){
			neighbors[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 2048) throw new Exception("array length ["+len+"] big than the max length [2048]");
		neighborTypes = new short[len];
		for(int i = 0 ; i < neighborTypes.length ; i++){
			neighborTypes[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
	}

	public int getType() {
		return 0xF0F1EEEE;
	}

	public String getTypeDescription() {
		return "QUERY_TOPO_DIAGRAM_RES";
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
		for(int i = 0 ; i < mapNames.length; i++){
			len += 2;
			try{
				len += mapNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < displayMapNames.length; i++){
			len += 2;
			try{
				len += displayMapNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += neighborNums.length;
		len += 4;
		len += neighbors.length * 2;
		len += 4;
		len += neighborTypes.length * 2;
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

			buffer.putInt(mapNames.length);
			for(int i = 0 ; i < mapNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = mapNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(displayMapNames.length);
			for(int i = 0 ; i < displayMapNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = displayMapNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(neighborNums.length);
			buffer.put(neighborNums);
			buffer.putInt(neighbors.length);
			for(int i = 0 ; i < neighbors.length; i++){
				buffer.putShort(neighbors[i]);
			}
			buffer.putInt(neighborTypes.length);
			for(int i = 0 ; i < neighborTypes.length; i++){
				buffer.putShort(neighborTypes[i]);
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
	 *	所有的游戏英文名称
	 */
	public String[] getMapNames(){
		return mapNames;
	}

	/**
	 * 设置属性：
	 *	所有的游戏英文名称
	 */
	public void setMapNames(String[] mapNames){
		this.mapNames = mapNames;
	}

	/**
	 * 获取属性：
	 *	所有的游戏中文名称
	 */
	public String[] getDisplayMapNames(){
		return displayMapNames;
	}

	/**
	 * 设置属性：
	 *	所有的游戏中文名称
	 */
	public void setDisplayMapNames(String[] displayMapNames){
		this.displayMapNames = displayMapNames;
	}

	/**
	 * 获取属性：
	 *	各个游戏地图的邻居数目
	 */
	public byte[] getNeighborNums(){
		return neighborNums;
	}

	/**
	 * 设置属性：
	 *	各个游戏地图的邻居数目
	 */
	public void setNeighborNums(byte[] neighborNums){
		this.neighborNums = neighborNums;
	}

	/**
	 * 获取属性：
	 *	各个地图邻居的编号，用一维表达二维的信息
	 */
	public short[] getNeighbors(){
		return neighbors;
	}

	/**
	 * 设置属性：
	 *	各个地图邻居的编号，用一维表达二维的信息
	 */
	public void setNeighbors(short[] neighbors){
		this.neighbors = neighbors;
	}

	/**
	 * 获取属性：
	 *	类型
	 */
	public short[] getNeighborTypes(){
		return neighborTypes;
	}

	/**
	 * 设置属性：
	 *	类型
	 */
	public void setNeighborTypes(short[] neighborTypes){
		this.neighborTypes = neighborTypes;
	}

}