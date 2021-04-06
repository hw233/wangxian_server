package com.fy.gamegateway.message;

import com.fy.gamegateway.menu.Option;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询某个NPC身上的窗口<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>title.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>title</td><td>String</td><td>title.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>descriptionInUUB.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>descriptionInUUB</td><td>String</td><td>descriptionInUUB.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inputTitle.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inputTitle[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inputTitle[0]</td><td>String</td><td>inputTitle[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inputTitle[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inputTitle[1]</td><td>String</td><td>inputTitle[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inputTitle[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inputTitle[2]</td><td>String</td><td>inputTitle[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inputType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inputType</td><td>byte[]</td><td>inputType.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxSize.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxSize</td><td>byte[]</td><td>maxSize.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defaultContent.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defaultContent[0].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defaultContent[0]</td><td>String</td><td>defaultContent[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defaultContent[1].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defaultContent[1]</td><td>String</td><td>defaultContent[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>defaultContent[2].length</td><td>int</td><td>2</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>defaultContent[2]</td><td>String</td><td>defaultContent[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>png.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>png</td><td>byte[]</td><td>png.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>options.length</td><td>int</td><td>4个字节</td><td>Option数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>options[0].text.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>options[0].text</td><td>String</td><td>options[0].text.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>options[0].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>options[0].oType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>options[0].oId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>options[1].text.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>options[1].text</td><td>String</td><td>options[1].text.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>options[1].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>options[1].oType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>options[1].oId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>options[2].text.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>options[2].text</td><td>String</td><td>options[2].text.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>options[2].color</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>options[2].oType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>options[2].oId</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class NEW_QUERY_WINDOW_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int id;
	String title;
	String descriptionInUUB;
	String[] inputTitle;
	byte[] inputType;
	byte[] maxSize;
	String[] defaultContent;
	byte[] png;
	Option[] options;

	public NEW_QUERY_WINDOW_REQ(){
	}

	public NEW_QUERY_WINDOW_REQ(long seqNum,int id,String title,String descriptionInUUB,String[] inputTitle,byte[] inputType,byte[] maxSize,String[] defaultContent,byte[] png,Option[] options){
		this.seqNum = seqNum;
		this.id = id;
		this.title = title;
		this.descriptionInUUB = descriptionInUUB;
		this.inputTitle = inputTitle;
		this.inputType = inputType;
		this.maxSize = maxSize;
		this.defaultContent = defaultContent;
		this.png = png;
		this.options = options;
	}

	public NEW_QUERY_WINDOW_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		title = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		descriptionInUUB = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		inputTitle = new String[len];
		for(int i = 0 ; i < inputTitle.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			inputTitle[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		inputType = new byte[len];
		System.arraycopy(content,offset,inputType,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		maxSize = new byte[len];
		System.arraycopy(content,offset,maxSize,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		defaultContent = new String[len];
		for(int i = 0 ; i < defaultContent.length ; i++){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			defaultContent[i] = new String(content,offset,len,"UTF-8");
		offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		png = new byte[len];
		System.arraycopy(content,offset,png,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		options = new Option[len];
		for(int i = 0 ; i < options.length ; i++){
			options[i] = new Option();
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			options[i].setText(new String(content,offset,len,"UTF-8"));
			offset += len;
			options[i].setColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			options[i].setOType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			options[i].setOId((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x002EE106;
	}

	public String getTypeDescription() {
		return "NEW_QUERY_WINDOW_REQ";
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
		len += 2;
		try{
			len +=title.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=descriptionInUUB.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < inputTitle.length; i++){
			len += 2;
			try{
				len += inputTitle[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += inputType.length;
		len += 4;
		len += maxSize.length;
		len += 4;
		for(int i = 0 ; i < defaultContent.length; i++){
			len += 2;
			try{
				len += defaultContent[i].getBytes("UTF-8").length;
			}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
				throw new RuntimeException("unsupported encoding [UTF-8]",e);
			}
		}
		len += 4;
		len += png.length;
		len += 4;
		for(int i = 0 ; i < options.length ; i++){
			len += 2;
			if(options[i].getText() != null){
				try{
				len += options[i].getText().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 1;
			len += 4;
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

			buffer.putInt(id);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = title.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = descriptionInUUB.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(inputTitle.length);
			for(int i = 0 ; i < inputTitle.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = inputTitle[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(inputType.length);
			buffer.put(inputType);
			buffer.putInt(maxSize.length);
			buffer.put(maxSize);
			buffer.putInt(defaultContent.length);
			for(int i = 0 ; i < defaultContent.length; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = defaultContent[i].getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(png.length);
			buffer.put(png);
			buffer.putInt(options.length);
			for(int i = 0 ; i < options.length ; i++){
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = options[i].getText().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)options[i].getColor());
				buffer.put((byte)options[i].getOType());
				buffer.putInt((int)options[i].getOId());
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
	 *	窗口的Id
	 */
	public int getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	窗口的Id
	 */
	public void setId(int id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	窗口的标题
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * 设置属性：
	 *	窗口的标题
	 */
	public void setTitle(String title){
		this.title = title;
	}

	/**
	 * 获取属性：
	 *	窗口的描述，UUB格式
	 */
	public String getDescriptionInUUB(){
		return descriptionInUUB;
	}

	/**
	 * 设置属性：
	 *	窗口的描述，UUB格式
	 */
	public void setDescriptionInUUB(String descriptionInUUB){
		this.descriptionInUUB = descriptionInUUB;
	}

	/**
	 * 获取属性：
	 *	input标题
	 */
	public String[] getInputTitle(){
		return inputTitle;
	}

	/**
	 * 设置属性：
	 *	input标题
	 */
	public void setInputTitle(String[] inputTitle){
		this.inputTitle = inputTitle;
	}

	/**
	 * 获取属性：
	 *	输入内容的类型，0标识输入数字，1随意,长度为0就没有输入框
	 */
	public byte[] getInputType(){
		return inputType;
	}

	/**
	 * 设置属性：
	 *	输入内容的类型，0标识输入数字，1随意,长度为0就没有输入框
	 */
	public void setInputType(byte[] inputType){
		this.inputType = inputType;
	}

	/**
	 * 获取属性：
	 *	输入内容的最大长度，1个汉字长度为2
	 */
	public byte[] getMaxSize(){
		return maxSize;
	}

	/**
	 * 设置属性：
	 *	输入内容的最大长度，1个汉字长度为2
	 */
	public void setMaxSize(byte[] maxSize){
		this.maxSize = maxSize;
	}

	/**
	 * 获取属性：
	 *	默认内容
	 */
	public String[] getDefaultContent(){
		return defaultContent;
	}

	/**
	 * 设置属性：
	 *	默认内容
	 */
	public void setDefaultContent(String[] defaultContent){
		this.defaultContent = defaultContent;
	}

	/**
	 * 获取属性：
	 *	窗口的PNG图片
	 */
	public byte[] getPng(){
		return png;
	}

	/**
	 * 设置属性：
	 *	窗口的PNG图片
	 */
	public void setPng(byte[] png){
		this.png = png;
	}

	/**
	 * 获取属性：
	 *	Window上的选项
	 */
	public Option[] getOptions(){
		return options;
	}

	/**
	 * 设置属性：
	 *	Window上的选项
	 */
	public void setOptions(Option[] options){
		this.options = options;
	}

}