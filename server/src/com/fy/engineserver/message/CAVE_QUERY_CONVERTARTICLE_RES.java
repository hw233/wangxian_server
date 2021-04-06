package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.cave.resource.ConvertArticleCfg;
import com.xuanzhi.tools.transport.ResponseMessage;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看可兑换道具的列表<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs.length</td><td>int</td><td>4个字节</td><td>ConvertArticleCfg数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[0].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[0].storeGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[0].groupIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[0].articleColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[0].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[0].articleName</td><td>String</td><td>cfgs[0].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[0].daiylMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[1].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[1].storeGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[1].groupIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[1].articleColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[1].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[1].articleName</td><td>String</td><td>cfgs[1].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[1].daiylMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[2].id</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[2].storeGrade</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[2].groupIndex</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[2].articleColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[2].articleName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>cfgs[2].articleName</td><td>String</td><td>cfgs[2].articleName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>cfgs[2].daiylMaxNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections.length</td><td>int</td><td>4个字节</td><td>ResourceCollection数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[0].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections[0].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[0].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections[1].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[1].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections[1].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[2].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resourceCollections[2].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resourceCollections[2].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CAVE_QUERY_CONVERTARTICLE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ConvertArticleCfg[] cfgs;
	ResourceCollection[] resourceCollections;

	public CAVE_QUERY_CONVERTARTICLE_RES(long seqNum,ConvertArticleCfg[] cfgs,ResourceCollection[] resourceCollections){
		this.seqNum = seqNum;
		this.cfgs = cfgs;
		this.resourceCollections = resourceCollections;
	}

	public CAVE_QUERY_CONVERTARTICLE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		cfgs = new ConvertArticleCfg[len];
		for(int i = 0 ; i < cfgs.length ; i++){
			cfgs[i] = new ConvertArticleCfg();
			cfgs[i].setId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			cfgs[i].setStoreGrade((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			cfgs[i].setGroupIndex((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			cfgs[i].setArticleColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			cfgs[i].setArticleName(new String(content,offset,len,"UTF-8"));
			offset += len;
			cfgs[i].setDaiylMaxNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		resourceCollections = new ResourceCollection[len];
		for(int i = 0 ; i < resourceCollections.length ; i++){
			resourceCollections[i] = new ResourceCollection();
			resourceCollections[i].setFood((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			resourceCollections[i].setWood((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			resourceCollections[i].setStone((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x8F000023;
	}

	public String getTypeDescription() {
		return "CAVE_QUERY_CONVERTARTICLE_RES";
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
		for(int i = 0 ; i < cfgs.length ; i++){
			len += 4;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(cfgs[i].getArticleName() != null){
				try{
				len += cfgs[i].getArticleName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
		}
		len += 4;
		for(int i = 0 ; i < resourceCollections.length ; i++){
			len += 4;
			len += 4;
			len += 4;
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

			buffer.putInt(cfgs.length);
			for(int i = 0 ; i < cfgs.length ; i++){
				buffer.putInt((int)cfgs[i].getId());
				buffer.putInt((int)cfgs[i].getStoreGrade());
				buffer.putInt((int)cfgs[i].getGroupIndex());
				buffer.putInt((int)cfgs[i].getArticleColor());
				byte[] tmpBytes2 = cfgs[i].getArticleName().getBytes("UTF-8");
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)cfgs[i].getDaiylMaxNum());
			}
			buffer.putInt(resourceCollections.length);
			for(int i = 0 ; i < resourceCollections.length ; i++){
				buffer.putInt((int)resourceCollections[i].getFood());
				buffer.putInt((int)resourceCollections[i].getWood());
				buffer.putInt((int)resourceCollections[i].getStone());
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
	 *	可兑换的列表
	 */
	public ConvertArticleCfg[] getCfgs(){
		return cfgs;
	}

	/**
	 * 设置属性：
	 *	可兑换的列表
	 */
	public void setCfgs(ConvertArticleCfg[] cfgs){
		this.cfgs = cfgs;
	}

	/**
	 * 获取属性：
	 *	领取消耗
	 */
	public ResourceCollection[] getResourceCollections(){
		return resourceCollections;
	}

	/**
	 * 设置属性：
	 *	领取消耗
	 */
	public void setResourceCollections(ResourceCollection[] resourceCollections){
		this.resourceCollections = resourceCollections;
	}

}
