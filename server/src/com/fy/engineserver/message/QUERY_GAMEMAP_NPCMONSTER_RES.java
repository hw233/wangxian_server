package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询某一地图的NPC，怪物位置数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mapname.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>mapname</td><td>String</td><td>mapname.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcNames[0]</td><td>String</td><td>npcNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcNames[1]</td><td>String</td><td>npcNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcNames[2]</td><td>String</td><td>npcNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>functionTypes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>functionTypes</td><td>int[]</td><td>functionTypes.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcIcons.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcIcons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcIcons[0]</td><td>String</td><td>npcIcons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcIcons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcIcons[1]</td><td>String</td><td>npcIcons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcIcons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcIcons[2]</td><td>String</td><td>npcIcons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcx.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcx</td><td>short[]</td><td>npcx.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>npcy.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>npcy</td><td>short[]</td><td>npcy.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterNames.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterNames[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterNames[0]</td><td>String</td><td>monsterNames[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterNames[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterNames[1]</td><td>String</td><td>monsterNames[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterNames[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterNames[2]</td><td>String</td><td>monsterNames[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterLv.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterLv</td><td>int[]</td><td>monsterLv.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterIcons.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterIcons[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterIcons[0]</td><td>String</td><td>monsterIcons[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterIcons[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterIcons[1]</td><td>String</td><td>monsterIcons[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterIcons[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterIcons[2]</td><td>String</td><td>monsterIcons[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monsterx.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monsterx</td><td>short[]</td><td>monsterx.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monstery.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monstery</td><td>short[]</td><td>monstery.length</td><td>*</td></tr>
 * </table>
 */
public class QUERY_GAMEMAP_NPCMONSTER_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String mapname;
	String[] npcNames;
	int[] functionTypes;
	String[] npcIcons;
	short[] npcx;
	short[] npcy;
	String[] monsterNames;
	int[] monsterLv;
	String[] monsterIcons;
	short[] monsterx;
	short[] monstery;

	public QUERY_GAMEMAP_NPCMONSTER_RES(){
	}

	public QUERY_GAMEMAP_NPCMONSTER_RES(long seqNum,String mapname,String[] npcNames,int[] functionTypes,String[] npcIcons,short[] npcx,short[] npcy,String[] monsterNames,int[] monsterLv,String[] monsterIcons,short[] monsterx,short[] monstery){
		this.seqNum = seqNum;
		this.mapname = mapname;
		this.npcNames = npcNames;
		this.functionTypes = functionTypes;
		this.npcIcons = npcIcons;
		this.npcx = npcx;
		this.npcy = npcy;
		this.monsterNames = monsterNames;
		this.monsterLv = monsterLv;
		this.monsterIcons = monsterIcons;
		this.monsterx = monsterx;
		this.monstery = monstery;
	}

	public QUERY_GAMEMAP_NPCMONSTER_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		mapname = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		npcNames = new String[len];
		for(int i = 0 ; i < npcNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			npcNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		functionTypes = new int[len];
		for(int i = 0 ; i < functionTypes.length ; i++){
			functionTypes[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		npcIcons = new String[len];
		for(int i = 0 ; i < npcIcons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			npcIcons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		npcx = new short[len];
		for(int i = 0 ; i < npcx.length ; i++){
			npcx[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		npcy = new short[len];
		for(int i = 0 ; i < npcy.length ; i++){
			npcy[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		monsterNames = new String[len];
		for(int i = 0 ; i < monsterNames.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monsterNames[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		monsterLv = new int[len];
		for(int i = 0 ; i < monsterLv.length ; i++){
			monsterLv[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		monsterIcons = new String[len];
		for(int i = 0 ; i < monsterIcons.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monsterIcons[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		monsterx = new short[len];
		for(int i = 0 ; i < monsterx.length ; i++){
			monsterx[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		monstery = new short[len];
		for(int i = 0 ; i < monstery.length ; i++){
			monstery[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
	}

	public int getType() {
		return 0x7000F042;
	}

	public String getTypeDescription() {
		return "QUERY_GAMEMAP_NPCMONSTER_RES";
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
		try{
			len +=mapname.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < npcNames.length; i++){
			len += 2;
			try{
				len += npcNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += functionTypes.length * 4;
		len += 4;
		for(int i = 0 ; i < npcIcons.length; i++){
			len += 2;
			try{
				len += npcIcons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += npcx.length * 2;
		len += 4;
		len += npcy.length * 2;
		len += 4;
		for(int i = 0 ; i < monsterNames.length; i++){
			len += 2;
			try{
				len += monsterNames[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += monsterLv.length * 4;
		len += 4;
		for(int i = 0 ; i < monsterIcons.length; i++){
			len += 2;
			try{
				len += monsterIcons[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += monsterx.length * 2;
		len += 4;
		len += monstery.length * 2;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = mapname.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(npcNames.length);
			for(int i = 0 ; i < npcNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = npcNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(functionTypes.length);
			for(int i = 0 ; i < functionTypes.length; i++){
				buffer.putInt(functionTypes[i]);
			}
			buffer.putInt(npcIcons.length);
			for(int i = 0 ; i < npcIcons.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = npcIcons[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(npcx.length);
			for(int i = 0 ; i < npcx.length; i++){
				buffer.putShort(npcx[i]);
			}
			buffer.putInt(npcy.length);
			for(int i = 0 ; i < npcy.length; i++){
				buffer.putShort(npcy[i]);
			}
			buffer.putInt(monsterNames.length);
			for(int i = 0 ; i < monsterNames.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = monsterNames[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(monsterLv.length);
			for(int i = 0 ; i < monsterLv.length; i++){
				buffer.putInt(monsterLv[i]);
			}
			buffer.putInt(monsterIcons.length);
			for(int i = 0 ; i < monsterIcons.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = monsterIcons[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(monsterx.length);
			for(int i = 0 ; i < monsterx.length; i++){
				buffer.putShort(monsterx[i]);
			}
			buffer.putInt(monstery.length);
			for(int i = 0 ; i < monstery.length; i++){
				buffer.putShort(monstery[i]);
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
	 *	地图名
	 */
	public String getMapname(){
		return mapname;
	}

	/**
	 * 设置属性：
	 *	地图名
	 */
	public void setMapname(String mapname){
		this.mapname = mapname;
	}

	/**
	 * 获取属性：
	 *	npc名字
	 */
	public String[] getNpcNames(){
		return npcNames;
	}

	/**
	 * 设置属性：
	 *	npc名字
	 */
	public void setNpcNames(String[] npcNames){
		this.npcNames = npcNames;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getFunctionTypes(){
		return functionTypes;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setFunctionTypes(int[] functionTypes){
		this.functionTypes = functionTypes;
	}

	/**
	 * 获取属性：
	 *	npc图标名
	 */
	public String[] getNpcIcons(){
		return npcIcons;
	}

	/**
	 * 设置属性：
	 *	npc图标名
	 */
	public void setNpcIcons(String[] npcIcons){
		this.npcIcons = npcIcons;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public short[] getNpcx(){
		return npcx;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setNpcx(short[] npcx){
		this.npcx = npcx;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public short[] getNpcy(){
		return npcy;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setNpcy(short[] npcy){
		this.npcy = npcy;
	}

	/**
	 * 获取属性：
	 *	怪名字
	 */
	public String[] getMonsterNames(){
		return monsterNames;
	}

	/**
	 * 设置属性：
	 *	怪名字
	 */
	public void setMonsterNames(String[] monsterNames){
		this.monsterNames = monsterNames;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public int[] getMonsterLv(){
		return monsterLv;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setMonsterLv(int[] monsterLv){
		this.monsterLv = monsterLv;
	}

	/**
	 * 获取属性：
	 *	monster图标名
	 */
	public String[] getMonsterIcons(){
		return monsterIcons;
	}

	/**
	 * 设置属性：
	 *	monster图标名
	 */
	public void setMonsterIcons(String[] monsterIcons){
		this.monsterIcons = monsterIcons;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public short[] getMonsterx(){
		return monsterx;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setMonsterx(short[] monsterx){
		this.monsterx = monsterx;
	}

	/**
	 * 获取属性：
	 *	无帮助说明
	 */
	public short[] getMonstery(){
		return monstery;
	}

	/**
	 * 设置属性：
	 *	无帮助说明
	 */
	public void setMonstery(short[] monstery){
		this.monstery = monstery;
	}

}