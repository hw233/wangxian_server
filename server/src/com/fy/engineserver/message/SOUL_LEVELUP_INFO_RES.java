package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 请求灵髓宝石升级所需经验<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>devourSilver</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>artificeSilver</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needExp.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>needExp</td><td>long[]</td><td>needExp.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>colorRate.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>colorRate</td><td>int[]</td><td>colorRate.length</td><td>*</td></tr>
 * </table>
 */
public class SOUL_LEVELUP_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long devourSilver;
	long artificeSilver;
	long[] needExp;
	int[] colorRate;

	public SOUL_LEVELUP_INFO_RES(){
	}

	public SOUL_LEVELUP_INFO_RES(long seqNum,long devourSilver,long artificeSilver,long[] needExp,int[] colorRate){
		this.seqNum = seqNum;
		this.devourSilver = devourSilver;
		this.artificeSilver = artificeSilver;
		this.needExp = needExp;
		this.colorRate = colorRate;
	}

	public SOUL_LEVELUP_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		devourSilver = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		artificeSilver = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		needExp = new long[len];
		for(int i = 0 ; i < needExp.length ; i++){
			needExp[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		colorRate = new int[len];
		for(int i = 0 ; i < colorRate.length ; i++){
			colorRate[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x70FFF025;
	}

	public String getTypeDescription() {
		return "SOUL_LEVELUP_INFO_RES";
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
		len += 8;
		len += 8;
		len += 4;
		len += needExp.length * 8;
		len += 4;
		len += colorRate.length * 4;
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

			buffer.putLong(devourSilver);
			buffer.putLong(artificeSilver);
			buffer.putInt(needExp.length);
			for(int i = 0 ; i < needExp.length; i++){
				buffer.putLong(needExp[i]);
			}
			buffer.putInt(colorRate.length);
			for(int i = 0 ; i < colorRate.length; i++){
				buffer.putInt(colorRate[i]);
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
	 *	吞噬消耗银两
	 */
	public long getDevourSilver(){
		return devourSilver;
	}

	/**
	 * 设置属性：
	 *	吞噬消耗银两
	 */
	public void setDevourSilver(long devourSilver){
		this.devourSilver = devourSilver;
	}

	/**
	 * 获取属性：
	 *	炼化消耗银两
	 */
	public long getArtificeSilver(){
		return artificeSilver;
	}

	/**
	 * 设置属性：
	 *	炼化消耗银两
	 */
	public void setArtificeSilver(long artificeSilver){
		this.artificeSilver = artificeSilver;
	}

	/**
	 * 获取属性：
	 *	所需经验
	 */
	public long[] getNeedExp(){
		return needExp;
	}

	/**
	 * 设置属性：
	 *	所需经验
	 */
	public void setNeedExp(long[] needExp){
		this.needExp = needExp;
	}

	/**
	 * 获取属性：
	 *	颜色属性比例
	 */
	public int[] getColorRate(){
		return colorRate;
	}

	/**
	 * 设置属性：
	 *	颜色属性比例
	 */
	public void setColorRate(int[] colorRate){
		this.colorRate = colorRate;
	}

}