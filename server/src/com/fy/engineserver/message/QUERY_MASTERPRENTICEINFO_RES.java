package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询所有的登记的师徒信息  true 收徒<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ptype</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>beginNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ids.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>ids</td><td>long[]</td><td>ids.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[0]</td><td>String</td><td>names[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[1]</td><td>String</td><td>names[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>names[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>names[2]</td><td>String</td><td>names[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>classLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>classLevels</td><td>short[]</td><td>classLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>careers.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>careers</td><td>byte[]</td><td>careers.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>levels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>levels</td><td>int[]</td><td>levels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>endTimes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>endTimes[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>endTimes[0]</td><td>String</td><td>endTimes[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>endTimes[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>endTimes[1]</td><td>String</td><td>endTimes[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>endTimes[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>endTimes[2]</td><td>String</td><td>endTimes[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_MASTERPRENTICEINFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean ptype;
	int beginNum;
	int maxNum;
	long[] ids;
	String[] names;
	short[] classLevels;
	byte[] careers;
	int[] levels;
	String[] endTimes;

	public QUERY_MASTERPRENTICEINFO_RES(){
	}

	public QUERY_MASTERPRENTICEINFO_RES(long seqNum,boolean ptype,int beginNum,int maxNum,long[] ids,String[] names,short[] classLevels,byte[] careers,int[] levels,String[] endTimes){
		this.seqNum = seqNum;
		this.ptype = ptype;
		this.beginNum = beginNum;
		this.maxNum = maxNum;
		this.ids = ids;
		this.names = names;
		this.classLevels = classLevels;
		this.careers = careers;
		this.levels = levels;
		this.endTimes = endTimes;
	}

	public QUERY_MASTERPRENTICEINFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		ptype = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		beginNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		maxNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		ids = new long[len];
		for(int i = 0 ; i < ids.length ; i++){
			ids[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		names = new String[len];
		for(int i = 0 ; i < names.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			names[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		classLevels = new short[len];
		for(int i = 0 ; i < classLevels.length ; i++){
			classLevels[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		careers = new byte[len];
		System.arraycopy(content,offset,careers,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		levels = new int[len];
		for(int i = 0 ; i < levels.length ; i++){
			levels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		endTimes = new String[len];
		for(int i = 0 ; i < endTimes.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			endTimes[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
	}

	public int getType() {
		return 0x7000F106;
	}

	public String getTypeDescription() {
		return "QUERY_MASTERPRENTICEINFO_RES";
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
		len += 4;
		len += 4;
		len += ids.length * 8;
		len += 4;
		for(int i = 0 ; i < names.length; i++){
			len += 2;
			try{
				len += names[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += classLevels.length * 2;
		len += 4;
		len += careers.length;
		len += 4;
		len += levels.length * 4;
		len += 4;
		for(int i = 0 ; i < endTimes.length; i++){
			len += 2;
			try{
				len += endTimes[i].getBytes("UTF-8").length;
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
		int oldPos = buffer.position();
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.put((byte)(ptype==false?0:1));
			buffer.putInt(beginNum);
			buffer.putInt(maxNum);
			buffer.putInt(ids.length);
			for(int i = 0 ; i < ids.length; i++){
				buffer.putLong(ids[i]);
			}
			buffer.putInt(names.length);
			for(int i = 0 ; i < names.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = names[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(classLevels.length);
			for(int i = 0 ; i < classLevels.length; i++){
				buffer.putShort(classLevels[i]);
			}
			buffer.putInt(careers.length);
			buffer.put(careers);
			buffer.putInt(levels.length);
			for(int i = 0 ; i < levels.length; i++){
				buffer.putInt(levels[i]);
			}
			buffer.putInt(endTimes.length);
			for(int i = 0 ; i < endTimes.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = endTimes[i].getBytes("UTF-8");
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
	 *	查询拜师还是(true)收徒
	 */
	public boolean getPtype(){
		return ptype;
	}

	/**
	 * 设置属性：
	 *	查询拜师还是(true)收徒
	 */
	public void setPtype(boolean ptype){
		this.ptype = ptype;
	}

	/**
	 * 获取属性：
	 *	第几页
	 */
	public int getBeginNum(){
		return beginNum;
	}

	/**
	 * 设置属性：
	 *	第几页
	 */
	public void setBeginNum(int beginNum){
		this.beginNum = beginNum;
	}

	/**
	 * 获取属性：
	 *	总共几页
	 */
	public int getMaxNum(){
		return maxNum;
	}

	/**
	 * 设置属性：
	 *	总共几页
	 */
	public void setMaxNum(int maxNum){
		this.maxNum = maxNum;
	}

	/**
	 * 获取属性：
	 *	玩家的id
	 */
	public long[] getIds(){
		return ids;
	}

	/**
	 * 设置属性：
	 *	玩家的id
	 */
	public void setIds(long[] ids){
		this.ids = ids;
	}

	/**
	 * 获取属性：
	 *	玩家的names
	 */
	public String[] getNames(){
		return names;
	}

	/**
	 * 设置属性：
	 *	玩家的names
	 */
	public void setNames(String[] names){
		this.names = names;
	}

	/**
	 * 获取属性：
	 *	境界
	 */
	public short[] getClassLevels(){
		return classLevels;
	}

	/**
	 * 设置属性：
	 *	境界
	 */
	public void setClassLevels(short[] classLevels){
		this.classLevels = classLevels;
	}

	/**
	 * 获取属性：
	 *	职业s
	 */
	public byte[] getCareers(){
		return careers;
	}

	/**
	 * 设置属性：
	 *	职业s
	 */
	public void setCareers(byte[] careers){
		this.careers = careers;
	}

	/**
	 * 获取属性：
	 *	级别s
	 */
	public int[] getLevels(){
		return levels;
	}

	/**
	 * 设置属性：
	 *	级别s
	 */
	public void setLevels(int[] levels){
		this.levels = levels;
	}

	/**
	 * 获取属性：
	 *	结束时间
	 */
	public String[] getEndTimes(){
		return endTimes;
	}

	/**
	 * 设置属性：
	 *	结束时间
	 */
	public void setEndTimes(String[] endTimes){
		this.endTimes = endTimes;
	}

}