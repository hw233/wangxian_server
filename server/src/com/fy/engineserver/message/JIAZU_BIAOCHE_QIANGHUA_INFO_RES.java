package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 家族镖车强化信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuMoney</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>constructionConsume</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxLevels</td><td>int[]</td><td>maxLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currentLevels</td><td>int[]</td><td>currentLevels.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxProcess.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxProcess</td><td>int[]</td><td>maxProcess.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>process.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>process</td><td>int[]</td><td>process.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costNums</td><td>long[]</td><td>costNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des1.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des1[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des1[0]</td><td>String</td><td>des1[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des1[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des1[1]</td><td>String</td><td>des1[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des1[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des1[2]</td><td>String</td><td>des1[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des2.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des2[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des2[0]</td><td>String</td><td>des2[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des2[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des2[1]</td><td>String</td><td>des2[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>des2[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>des2[2]</td><td>String</td><td>des2[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>jiazuLevels.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiazuLevels</td><td>int[]</td><td>jiazuLevels.length</td><td>*</td></tr>
 * </table>
 */
public class JIAZU_BIAOCHE_QIANGHUA_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int jiazuLevel;
	long jiazuMoney;
	long constructionConsume;
	int[] maxLevels;
	int[] currentLevels;
	int[] maxProcess;
	int[] process;
	long[] costNums;
	String[] des1;
	String[] des2;
	int[] jiazuLevels;

	public JIAZU_BIAOCHE_QIANGHUA_INFO_RES(){
	}

	public JIAZU_BIAOCHE_QIANGHUA_INFO_RES(long seqNum,int jiazuLevel,long jiazuMoney,long constructionConsume,int[] maxLevels,int[] currentLevels,int[] maxProcess,int[] process,long[] costNums,String[] des1,String[] des2,int[] jiazuLevels){
		this.seqNum = seqNum;
		this.jiazuLevel = jiazuLevel;
		this.jiazuMoney = jiazuMoney;
		this.constructionConsume = constructionConsume;
		this.maxLevels = maxLevels;
		this.currentLevels = currentLevels;
		this.maxProcess = maxProcess;
		this.process = process;
		this.costNums = costNums;
		this.des1 = des1;
		this.des2 = des2;
		this.jiazuLevels = jiazuLevels;
	}

	public JIAZU_BIAOCHE_QIANGHUA_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		jiazuLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		jiazuMoney = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		constructionConsume = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		maxLevels = new int[len];
		for(int i = 0 ; i < maxLevels.length ; i++){
			maxLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		currentLevels = new int[len];
		for(int i = 0 ; i < currentLevels.length ; i++){
			currentLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		maxProcess = new int[len];
		for(int i = 0 ; i < maxProcess.length ; i++){
			maxProcess[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		process = new int[len];
		for(int i = 0 ; i < process.length ; i++){
			process[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		costNums = new long[len];
		for(int i = 0 ; i < costNums.length ; i++){
			costNums[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		des1 = new String[len];
		for(int i = 0 ; i < des1.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			des1[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		des2 = new String[len];
		for(int i = 0 ; i < des2.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			des2[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		jiazuLevels = new int[len];
		for(int i = 0 ; i < jiazuLevels.length ; i++){
			jiazuLevels[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FF0017;
	}

	public String getTypeDescription() {
		return "JIAZU_BIAOCHE_QIANGHUA_INFO_RES";
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
		len += 8;
		len += 8;
		len += 4;
		len += maxLevels.length * 4;
		len += 4;
		len += currentLevels.length * 4;
		len += 4;
		len += maxProcess.length * 4;
		len += 4;
		len += process.length * 4;
		len += 4;
		len += costNums.length * 8;
		len += 4;
		for(int i = 0 ; i < des1.length; i++){
			len += 2;
			try{
				len += des1[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		for(int i = 0 ; i < des2.length; i++){
			len += 2;
			try{
				len += des2[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += jiazuLevels.length * 4;
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

			buffer.putInt(jiazuLevel);
			buffer.putLong(jiazuMoney);
			buffer.putLong(constructionConsume);
			buffer.putInt(maxLevels.length);
			for(int i = 0 ; i < maxLevels.length; i++){
				buffer.putInt(maxLevels[i]);
			}
			buffer.putInt(currentLevels.length);
			for(int i = 0 ; i < currentLevels.length; i++){
				buffer.putInt(currentLevels[i]);
			}
			buffer.putInt(maxProcess.length);
			for(int i = 0 ; i < maxProcess.length; i++){
				buffer.putInt(maxProcess[i]);
			}
			buffer.putInt(process.length);
			for(int i = 0 ; i < process.length; i++){
				buffer.putInt(process[i]);
			}
			buffer.putInt(costNums.length);
			for(int i = 0 ; i < costNums.length; i++){
				buffer.putLong(costNums[i]);
			}
			buffer.putInt(des1.length);
			for(int i = 0 ; i < des1.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = des1[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(des2.length);
			for(int i = 0 ; i < des2.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = des2[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(jiazuLevels.length);
			for(int i = 0 ; i < jiazuLevels.length; i++){
				buffer.putInt(jiazuLevels[i]);
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
	 *	家族等级 
	 */
	public int getJiazuLevel(){
		return jiazuLevel;
	}

	/**
	 * 设置属性：
	 *	家族等级 
	 */
	public void setJiazuLevel(int jiazuLevel){
		this.jiazuLevel = jiazuLevel;
	}

	/**
	 * 获取属性：
	 *	家族资金 
	 */
	public long getJiazuMoney(){
		return jiazuMoney;
	}

	/**
	 * 设置属性：
	 *	家族资金 
	 */
	public void setJiazuMoney(long jiazuMoney){
		this.jiazuMoney = jiazuMoney;
	}

	/**
	 * 获取属性：
	 *	家族灵脉值 
	 */
	public long getConstructionConsume(){
		return constructionConsume;
	}

	/**
	 * 设置属性：
	 *	家族灵脉值 
	 */
	public void setConstructionConsume(long constructionConsume){
		this.constructionConsume = constructionConsume;
	}

	/**
	 * 获取属性：
	 *	强化最大等级  [0]血量  [1]双防
	 */
	public int[] getMaxLevels(){
		return maxLevels;
	}

	/**
	 * 设置属性：
	 *	强化最大等级  [0]血量  [1]双防
	 */
	public void setMaxLevels(int[] maxLevels){
		this.maxLevels = maxLevels;
	}

	/**
	 * 获取属性：
	 *	当前强化等级 [0]血量 [1]双防
	 */
	public int[] getCurrentLevels(){
		return currentLevels;
	}

	/**
	 * 设置属性：
	 *	当前强化等级 [0]血量 [1]双防
	 */
	public void setCurrentLevels(int[] currentLevels){
		this.currentLevels = currentLevels;
	}

	/**
	 * 获取属性：
	 *	强化最大进度 [0]血量 [1]双防
	 */
	public int[] getMaxProcess(){
		return maxProcess;
	}

	/**
	 * 设置属性：
	 *	强化最大进度 [0]血量 [1]双防
	 */
	public void setMaxProcess(int[] maxProcess){
		this.maxProcess = maxProcess;
	}

	/**
	 * 获取属性：
	 *	强化当前进度 [0]血量 [1]双防
	 */
	public int[] getProcess(){
		return process;
	}

	/**
	 * 设置属性：
	 *	强化当前进度 [0]血量 [1]双防
	 */
	public void setProcess(int[] process){
		this.process = process;
	}

	/**
	 * 获取属性：
	 *	强化花费[0]血量 [1]双防
	 */
	public long[] getCostNums(){
		return costNums;
	}

	/**
	 * 设置属性：
	 *	强化花费[0]血量 [1]双防
	 */
	public void setCostNums(long[] costNums){
		this.costNums = costNums;
	}

	/**
	 * 获取属性：
	 *	当前等级描述
	 */
	public String[] getDes1(){
		return des1;
	}

	/**
	 * 设置属性：
	 *	当前等级描述
	 */
	public void setDes1(String[] des1){
		this.des1 = des1;
	}

	/**
	 * 获取属性：
	 *	下一级描述
	 */
	public String[] getDes2(){
		return des2;
	}

	/**
	 * 设置属性：
	 *	下一级描述
	 */
	public void setDes2(String[] des2){
		this.des2 = des2;
	}

	/**
	 * 获取属性：
	 *	需要家族等级 [0]血量 [1]双防
	 */
	public int[] getJiazuLevels(){
		return jiazuLevels;
	}

	/**
	 * 设置属性：
	 *	需要家族等级 [0]血量 [1]双防
	 */
	public void setJiazuLevels(int[] jiazuLevels){
		this.jiazuLevels = jiazuLevels;
	}

}