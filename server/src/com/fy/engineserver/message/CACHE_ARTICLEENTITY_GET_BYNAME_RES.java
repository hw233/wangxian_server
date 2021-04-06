package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取一个物品<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>className.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>className</td><td>String</td><td>className.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showName</td><td>String</td><td>showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inValidTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>taskArticle</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>level</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>durability</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayEntityIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayEntityIds</td><td>long[]</td><td>inlayEntityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>attackDefenceCount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftUsingTimes</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>drillcount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>appendix.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>appendix</td><td>String</td><td>appendix.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>abandoned</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propertyString.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propertyString</td><td>String</td><td>propertyString.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result</td><td>String</td><td>result.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class CACHE_ARTICLEENTITY_GET_BYNAME_RES implements ResponseMessage{

	static CacheSystemMessageFactory mf = CacheSystemMessageFactory.getInstance();

	long seqNum;
	String className;
	long id;
	String showName;
	long inValidTime;
	boolean taskArticle;
	boolean binded;
	int level;
	int durability;
	long[] inlayEntityIds;
	int attackDefenceCount;
	int leftUsingTimes;
	int drillcount;
	String appendix;
	boolean abandoned;
	String propertyString;
	String result;

	public CACHE_ARTICLEENTITY_GET_BYNAME_RES(long seqNum,String className,long id,String showName,long inValidTime,boolean taskArticle,boolean binded,int level,int durability,long[] inlayEntityIds,int attackDefenceCount,int leftUsingTimes,int drillcount,String appendix,boolean abandoned,String propertyString,String result){
		this.seqNum = seqNum;
		this.className = className;
		this.id = id;
		this.showName = showName;
		this.inValidTime = inValidTime;
		this.taskArticle = taskArticle;
		this.binded = binded;
		this.level = level;
		this.durability = durability;
		this.inlayEntityIds = inlayEntityIds;
		this.attackDefenceCount = attackDefenceCount;
		this.leftUsingTimes = leftUsingTimes;
		this.drillcount = drillcount;
		this.appendix = appendix;
		this.abandoned = abandoned;
		this.propertyString = propertyString;
		this.result = result;
	}

	public CACHE_ARTICLEENTITY_GET_BYNAME_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		className = new String(content,offset,len,"UTF-8");
		offset += len;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		showName = new String(content,offset,len,"UTF-8");
		offset += len;
		inValidTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		taskArticle = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		binded = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		level = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		durability = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 502400) throw new Exception("array length ["+len+"] big than the max length [502400]");
		inlayEntityIds = new long[len];
		for(int i = 0 ; i < inlayEntityIds.length ; i++){
			inlayEntityIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		attackDefenceCount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		leftUsingTimes = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		drillcount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		appendix = new String(content,offset,len,"UTF-8");
		offset += len;
		abandoned = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		propertyString = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		result = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x80000022;
	}

	public String getTypeDescription() {
		return "CACHE_ARTICLEENTITY_GET_BYNAME_RES";
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
			len +=className.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 2;
		try{
			len +=showName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 1;
		len += 1;
		len += 4;
		len += 4;
		len += 4;
		len += inlayEntityIds.length * 8;
		len += 4;
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=appendix.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
		len += 2;
		try{
			len +=propertyString.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=result.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			byte[] tmpBytes1 = className.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(id);
			tmpBytes1 = showName.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(inValidTime);
			buffer.put((byte)(taskArticle==false?0:1));
			buffer.put((byte)(binded==false?0:1));
			buffer.putInt(level);
			buffer.putInt(durability);
			buffer.putInt(inlayEntityIds.length);
			for(int i = 0 ; i < inlayEntityIds.length; i++){
				buffer.putLong(inlayEntityIds[i]);
			}
			buffer.putInt(attackDefenceCount);
			buffer.putInt(leftUsingTimes);
			buffer.putInt(drillcount);
			tmpBytes1 = appendix.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(abandoned==false?0:1));
			tmpBytes1 = propertyString.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			tmpBytes1 = result.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	类型
	 */
	public String getClassName(){
		return className;
	}

	/**
	 * 设置属性：
	 *	类型
	 */
	public void setClassName(String className){
		this.className = className;
	}

	/**
	 * 获取属性：
	 *	id
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	id
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	显示名称
	 */
	public String getShowName(){
		return showName;
	}

	/**
	 * 设置属性：
	 *	显示名称
	 */
	public void setShowName(String showName){
		this.showName = showName;
	}

	/**
	 * 获取属性：
	 *	有效期
	 */
	public long getInValidTime(){
		return inValidTime;
	}

	/**
	 * 设置属性：
	 *	有效期
	 */
	public void setInValidTime(long inValidTime){
		this.inValidTime = inValidTime;
	}

	/**
	 * 获取属性：
	 *	是否任务物品
	 */
	public boolean getTaskArticle(){
		return taskArticle;
	}

	/**
	 * 设置属性：
	 *	是否任务物品
	 */
	public void setTaskArticle(boolean taskArticle){
		this.taskArticle = taskArticle;
	}

	/**
	 * 获取属性：
	 *	是否绑定
	 */
	public boolean getBinded(){
		return binded;
	}

	/**
	 * 设置属性：
	 *	是否绑定
	 */
	public void setBinded(boolean binded){
		this.binded = binded;
	}

	/**
	 * 获取属性：
	 *	等级
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * 设置属性：
	 *	等级
	 */
	public void setLevel(int level){
		this.level = level;
	}

	/**
	 * 获取属性：
	 *	耐久度
	 */
	public int getDurability(){
		return durability;
	}

	/**
	 * 设置属性：
	 *	耐久度
	 */
	public void setDurability(int durability){
		this.durability = durability;
	}

	/**
	 * 获取属性：
	 *	内嵌的物品
	 */
	public long[] getInlayEntityIds(){
		return inlayEntityIds;
	}

	/**
	 * 设置属性：
	 *	内嵌的物品
	 */
	public void setInlayEntityIds(long[] inlayEntityIds){
		this.inlayEntityIds = inlayEntityIds;
	}

	/**
	 * 获取属性：
	 *	攻击或防御的次数
	 */
	public int getAttackDefenceCount(){
		return attackDefenceCount;
	}

	/**
	 * 设置属性：
	 *	攻击或防御的次数
	 */
	public void setAttackDefenceCount(int attackDefenceCount){
		this.attackDefenceCount = attackDefenceCount;
	}

	/**
	 * 获取属性：
	 *	剩余的使用次数
	 */
	public int getLeftUsingTimes(){
		return leftUsingTimes;
	}

	/**
	 * 设置属性：
	 *	剩余的使用次数
	 */
	public void setLeftUsingTimes(int leftUsingTimes){
		this.leftUsingTimes = leftUsingTimes;
	}

	/**
	 * 获取属性：
	 *	钻孔次数
	 */
	public int getDrillcount(){
		return drillcount;
	}

	/**
	 * 设置属性：
	 *	钻孔次数
	 */
	public void setDrillcount(int drillcount){
		this.drillcount = drillcount;
	}

	/**
	 * 获取属性：
	 *	附加属性
	 */
	public String getAppendix(){
		return appendix;
	}

	/**
	 * 设置属性：
	 *	附加属性
	 */
	public void setAppendix(String appendix){
		this.appendix = appendix;
	}

	/**
	 * 获取属性：
	 *	是否遗弃
	 */
	public boolean getAbandoned(){
		return abandoned;
	}

	/**
	 * 设置属性：
	 *	是否遗弃
	 */
	public void setAbandoned(boolean abandoned){
		this.abandoned = abandoned;
	}

	/**
	 * 获取属性：
	 *	属性字符串
	 */
	public String getPropertyString(){
		return propertyString;
	}

	/**
	 * 设置属性：
	 *	属性字符串
	 */
	public void setPropertyString(String propertyString){
		this.propertyString = propertyString;
	}

	/**
	 * 获取属性：
	 *	结果说明
	 */
	public String getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果说明
	 */
	public void setResult(String result){
		this.result = result;
	}

}
