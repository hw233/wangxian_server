package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.worldmap.WorldMapArea;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询世界地图地域 <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas.length</td><td>int</td><td>4个字节</td><td>WorldMapArea数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[0].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[0].name</td><td>String</td><td>areas[0].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[0].worldMapAreaX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[0].worldMapAreaX</td><td>short[]</td><td>areas[0].worldMapAreaX.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[0].worldMapAreaY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[0].worldMapAreaY</td><td>short[]</td><td>areas[0].worldMapAreaY.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[0].worldMapAreaWidth.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[0].worldMapAreaWidth</td><td>short[]</td><td>areas[0].worldMapAreaWidth.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[0].worldMapAreaHeight.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[0].worldMapAreaHeight</td><td>short[]</td><td>areas[0].worldMapAreaHeight.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[0].pressPng.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[0].pressPng</td><td>String</td><td>areas[0].pressPng.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[0].pressPngx</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[0].pressPngy</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[1].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[1].name</td><td>String</td><td>areas[1].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[1].worldMapAreaX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[1].worldMapAreaX</td><td>short[]</td><td>areas[1].worldMapAreaX.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[1].worldMapAreaY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[1].worldMapAreaY</td><td>short[]</td><td>areas[1].worldMapAreaY.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[1].worldMapAreaWidth.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[1].worldMapAreaWidth</td><td>short[]</td><td>areas[1].worldMapAreaWidth.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[1].worldMapAreaHeight.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[1].worldMapAreaHeight</td><td>short[]</td><td>areas[1].worldMapAreaHeight.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[1].pressPng.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[1].pressPng</td><td>String</td><td>areas[1].pressPng.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[1].pressPngx</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[1].pressPngy</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[2].name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[2].name</td><td>String</td><td>areas[2].name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[2].worldMapAreaX.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[2].worldMapAreaX</td><td>short[]</td><td>areas[2].worldMapAreaX.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[2].worldMapAreaY.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[2].worldMapAreaY</td><td>short[]</td><td>areas[2].worldMapAreaY.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[2].worldMapAreaWidth.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[2].worldMapAreaWidth</td><td>short[]</td><td>areas[2].worldMapAreaWidth.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[2].worldMapAreaHeight.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[2].worldMapAreaHeight</td><td>short[]</td><td>areas[2].worldMapAreaHeight.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[2].pressPng.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[2].pressPng</td><td>String</td><td>areas[2].pressPng.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>areas[2].pressPngx</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>areas[2].pressPngy</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_WORLD_MAP_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	WorldMapArea[] areas;

	public QUERY_WORLD_MAP_RES(){
	}

	public QUERY_WORLD_MAP_RES(long seqNum,WorldMapArea[] areas){
		this.seqNum = seqNum;
		this.areas = areas;
	}

	public QUERY_WORLD_MAP_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		areas = new WorldMapArea[len];
		for(int i = 0 ; i < areas.length ; i++){
			areas[i] = new WorldMapArea();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			areas[i].setName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] worldMapAreaX_0001 = new short[len];
			for(int j = 0 ; j < worldMapAreaX_0001.length ; j++){
				worldMapAreaX_0001[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			areas[i].setWorldMapAreaX(worldMapAreaX_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] worldMapAreaY_0002 = new short[len];
			for(int j = 0 ; j < worldMapAreaY_0002.length ; j++){
				worldMapAreaY_0002[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			areas[i].setWorldMapAreaY(worldMapAreaY_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] worldMapAreaWidth_0003 = new short[len];
			for(int j = 0 ; j < worldMapAreaWidth_0003.length ; j++){
				worldMapAreaWidth_0003[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			areas[i].setWorldMapAreaWidth(worldMapAreaWidth_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] worldMapAreaHeight_0004 = new short[len];
			for(int j = 0 ; j < worldMapAreaHeight_0004.length ; j++){
				worldMapAreaHeight_0004[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			areas[i].setWorldMapAreaHeight(worldMapAreaHeight_0004);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			areas[i].setPressPng(new String(content,offset,len,"UTF-8"));
			offset += len;
			areas[i].setPressPngx((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			areas[i].setPressPngy((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
		}
	}

	public int getType() {
		return 0x7000F040;
	}

	public String getTypeDescription() {
		return "QUERY_WORLD_MAP_RES";
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
		for(int i = 0 ; i < areas.length ; i++){
			len += 2;
			if(areas[i].getName() != null){
				try{
				len += areas[i].getName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += areas[i].getWorldMapAreaX().length * 2;
			len += 4;
			len += areas[i].getWorldMapAreaY().length * 2;
			len += 4;
			len += areas[i].getWorldMapAreaWidth().length * 2;
			len += 4;
			len += areas[i].getWorldMapAreaHeight().length * 2;
			len += 2;
			if(areas[i].getPressPng() != null){
				try{
				len += areas[i].getPressPng().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 2;
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

			buffer.putInt(areas.length);
			for(int i = 0 ; i < areas.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = areas[i].getName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(areas[i].getWorldMapAreaX().length);
				short[] worldMapAreaX_0005 = areas[i].getWorldMapAreaX();
				for(int j = 0 ; j < worldMapAreaX_0005.length ; j++){
					buffer.putShort(worldMapAreaX_0005[j]);
				}
				buffer.putInt(areas[i].getWorldMapAreaY().length);
				short[] worldMapAreaY_0006 = areas[i].getWorldMapAreaY();
				for(int j = 0 ; j < worldMapAreaY_0006.length ; j++){
					buffer.putShort(worldMapAreaY_0006[j]);
				}
				buffer.putInt(areas[i].getWorldMapAreaWidth().length);
				short[] worldMapAreaWidth_0007 = areas[i].getWorldMapAreaWidth();
				for(int j = 0 ; j < worldMapAreaWidth_0007.length ; j++){
					buffer.putShort(worldMapAreaWidth_0007[j]);
				}
				buffer.putInt(areas[i].getWorldMapAreaHeight().length);
				short[] worldMapAreaHeight_0008 = areas[i].getWorldMapAreaHeight();
				for(int j = 0 ; j < worldMapAreaHeight_0008.length ; j++){
					buffer.putShort(worldMapAreaHeight_0008[j]);
				}
				try{
				tmpBytes2 = areas[i].getPressPng().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)areas[i].getPressPngx());
				buffer.putShort((short)areas[i].getPressPngy());
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
	 *	世界地图地域
	 */
	public WorldMapArea[] getAreas(){
		return areas;
	}

	/**
	 * 设置属性：
	 *	世界地图地域
	 */
	public void setAreas(WorldMapArea[] areas){
		this.areas = areas;
	}

}