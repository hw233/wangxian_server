package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * <br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>name.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>name</td><td>String</td><td>name.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>step.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>step</td><td>String</td><td>step.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>desc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>desc</td><td>String</td><td>desc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>stepDesc.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>stepDesc[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>stepDesc[0]</td><td>String</td><td>stepDesc[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>stepDesc[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>stepDesc[1]</td><td>String</td><td>stepDesc[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>stepDesc[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>stepDesc[2]</td><td>String</td><td>stepDesc[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>openDesc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>openDesc</td><td>String</td><td>openDesc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>layerDesc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>layerDesc</td><td>String</td><td>layerDesc.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>needP.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>needP</td><td>String</td><td>needP.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>nextLayerDesc.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>nextLayerDesc</td><td>String</td><td>nextLayerDesc.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class SkEnh_Detail_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int index;
	int level;
	String name;
	String step;
	String desc;
	String[] stepDesc;
	String openDesc;
	String layerDesc;
	String needP;
	String nextLayerDesc;

	public SkEnh_Detail_RES(){
	}

	public SkEnh_Detail_RES(long seqNum,int index,int level,String name,String step,String desc,String[] stepDesc,String openDesc,String layerDesc,String needP,String nextLayerDesc){
		this.seqNum = seqNum;
		this.index = index;
		this.level = level;
		this.name = name;
		this.step = step;
		this.desc = desc;
		this.stepDesc = stepDesc;
		this.openDesc = openDesc;
		this.layerDesc = layerDesc;
		this.needP = needP;
		this.nextLayerDesc = nextLayerDesc;
	}

	public SkEnh_Detail_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		name = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		step = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		desc = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		stepDesc = new String[len];
		for(int i = 0 ; i < stepDesc.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			stepDesc[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		openDesc = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		layerDesc = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		needP = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		nextLayerDesc = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x8E0EAA7A;
	}

	public String getTypeDescription() {
		return "SkEnh_Detail_RES";
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
		len += 2;
		try{
			len +=name.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=step.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=desc.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < stepDesc.length; i++){
			len += 2;
			try{
				len += stepDesc[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 2;
		try{
			len +=openDesc.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=layerDesc.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=needP.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=nextLayerDesc.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			buffer.putInt(index);
			buffer.putInt(level);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = name.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = step.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = desc.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(stepDesc.length);
			for(int i = 0 ; i < stepDesc.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = stepDesc[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
				try{
			tmpBytes1 = openDesc.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = layerDesc.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = needP.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = nextLayerDesc.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	技能下标
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	技能下标
	 */
	public void setIndex(int index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	当前等级
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	当前等级
	 */
	public void setLevel(int level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	名字
	 */
	public String getName(){
		return name;
	}

	/**
	 * 设置属性：
	 *	名字
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * 获取属性：
	 *	当前X阶X重
	 */
	public String getStep(){
		return step;
	}

	/**
	 * 设置属性：
	 *	当前X阶X重
	 */
	public void setStep(String step){
		this.step = step;
	}

	/**
	 * 获取属性：
	 *	描述
	 */
	public String getDesc(){
		return desc;
	}

	/**
	 * 设置属性：
	 *	描述
	 */
	public void setDesc(String desc){
		this.desc = desc;
	}

	/**
	 * 获取属性：
	 *	三大阶段的描述
	 */
	public String[] getStepDesc(){
		return stepDesc;
	}

	/**
	 * 设置属性：
	 *	三大阶段的描述
	 */
	public void setStepDesc(String[] stepDesc){
		this.stepDesc = stepDesc;
	}

	/**
	 * 获取属性：
	 *	开启条件
	 */
	public String getOpenDesc(){
		return openDesc;
	}

	/**
	 * 设置属性：
	 *	开启条件
	 */
	public void setOpenDesc(String openDesc){
		this.openDesc = openDesc;
	}

	/**
	 * 获取属性：
	 *	当前层描述
	 */
	public String getLayerDesc(){
		return layerDesc;
	}

	/**
	 * 设置属性：
	 *	当前层描述
	 */
	public void setLayerDesc(String layerDesc){
		this.layerDesc = layerDesc;
	}

	/**
	 * 获取属性：
	 *	升级消耗
	 */
	public String getNeedP(){
		return needP;
	}

	/**
	 * 设置属性：
	 *	升级消耗
	 */
	public void setNeedP(String needP){
		this.needP = needP;
	}

	/**
	 * 获取属性：
	 *	下一层描述
	 */
	public String getNextLayerDesc(){
		return nextLayerDesc;
	}

	/**
	 * 设置属性：
	 *	下一层描述
	 */
	public void setNextLayerDesc(String nextLayerDesc){
		this.nextLayerDesc = nextLayerDesc;
	}

}