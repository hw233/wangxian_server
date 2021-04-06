package com.xuanzhi.tools.transport;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * <pre>
 * 此xml文件用于定义消息的格式，然后由消息生成器直接生成 
 *	 	其中包的属性分为两种，一种是primitive类型，一种是对象
		primitive类型支持：
			"boolean","char","byte","short","int","long","float","double","String"
			以及他们的一维数组。
			声明的形式为：
				&lt;property name="spriteId" type="int" comment=""/&gt;
				或者
				&lt;property name="data" type="byte[]" max-num="512" comment=""/&gt;
				或者
				&lt;property name="data" type="String" length="23" encoding="utf-8" comment=""/&gt;
		对象类型：
			任何JavaBean对象，支持属性的getter和setter
			以及它们们的一维数组
			任何可序列化的对象。
			
			声明的形式为：
				&lt;object-property name="sprites" type="Sprite" comment="进入窗口精灵"&gt;
					&lt;property name="id" type="int" comment="" /&gt;
					&lt;property name="name" type="String" length="0" comment="" /&gt;
					&lt;property name="x" type="int" comment="" /&gt;
					&lt;property name="y" type="int" comment="" /&gt;
					&lt;property name="y" type="int" comment="" /&gt;
					&lt;property name="data" type="byte[]" comment=""/&gt;
				&lt;/objectarray-property&gt;
			或者
				&lt;object-property name="sprites" type="Sprite[]" max-num="16" comment="进入窗口精灵"&gt;
					&lt;property name="id" type="int" comment="" /&gt;
					&lt;property name="name" type="String" length="16" encoding="utf-8" comment="" /&gt;
					&lt;property name="x" type="int" comment="" /&gt;
					&lt;property name="y" type="int" comment="" /&gt;
					&lt;property name="y" type="int" comment="" /&gt;
					&lt;property name="data" type="byte[]" comment=""/&gt;
				&lt;/objectarray-property&gt;
			
			可序列化对象 
				&lt;object name="sprites" type="Sprite" comment="进入窗口精灵"/&gt;
			
			对象的属性只能是primitive类型，对象不能嵌套对象	
		
		工厂类生成
			&lt;factory name="GameMessageFactory" enable-serialize="true"/&gt;
		
		
		配置
			&lt;init-params stringMaxLength="10240" arrayMaxLength="1024" objectMaxLength="4096"/&gt;
			
		校验算法生成
			&lt;verification name="MD5" a="0x12374834" b="0xfe34b384" c="0x243948e" d="0x45838f4"/&gt;
		
		
		
		典型的例子，其中包含了简单类型，简单类型的一维数组，对象类型，对象类型数组，以及序列号对象	
		
		&lt;?xml version='1.0' encoding='gbk' ?&gt;
		&lt;prototypes version='1.0' author='yugang wang'&gt;
			&lt;factory name='GameMessageFactory' enable-serialize='true'/&gt;
			&lt;verification name='MD5' a='0x12374834' b='0xfe34b384' c='0x243948e' d='0x45838f4'/&gt;
			&lt;prototype req_name='ACTIVE_TEST_REQ' res_name='ACTIVE_TEST_RES' req_type='0x00000002' res_type='0x80000002'&gt;
				&lt;req&gt;
					&lt;property name='spriteId' type='int' comment=''/&gt;
					&lt;property name='spriteName' type='String' encoding="utf-8" comment=''/&gt;
					&lt;property name='spriteDesp' type='String' encoding="utf-8" length='16' comment=''/&gt;
					&lt;property name='x' type='short' comment='坐标x'/&gt;
					&lt;property name='y' type='short' comment='坐标y'/&gt;
					&lt;property name='moveSpriteIds' type='int[]' max-num='16' comment='移动精灵的ID数组'/&gt;
				&lt;/req&gt;
				&lt;res&gt;
					&lt;property name='result' type='boolean' comment='结果标记'/&gt;
					&lt;object-property name='sprite' type='Sprite' comment=''&gt;
						&lt;property name='spriteId' type='int' comment=''/&gt;
						&lt;property name='spriteName' type='String' comment=''/&gt;
						&lt;property name='spriteDesp' type='String' length='16' comment=''/&gt;
						&lt;property name='x' type='short' comment='坐标x'/&gt;
						&lt;property name='y' type='short' comment='坐标y'/&gt;
						&lt;property name='moveSpriteIds' type='int[]' max-num='32' comment='移动精灵的ID数组'/&gt;
					&lt;/object-property&gt;
					&lt;object-property name='sprites' type='Sprite[]' max-num='128' comment=''&gt;
						&lt;property name='spriteId' type='int' comment=''/&gt;
						&lt;property name='spriteName' type='String' comment=''/&gt;
						&lt;property name='spriteDesp' type='String' length='16' comment=''/&gt;
						&lt;property name='x' type='short' comment='坐标x'/&gt;
						&lt;property name='y' type='short' comment='坐标y'/&gt;
						&lt;property name='moveSpriteIds' type='int[]' max-num='16' comment='移动精灵的ID数组'/&gt;
					&lt;/object-property&gt;";
				&lt;/res&gt;
			&lt;/prototype&gt;
		
			&lt;prototype req_name='SENDMTMESSAGE_REQ' res_name='SENDMTMESSAGE_RES' req_type='0x00000003' res_type='0x80000003'&gt;
				&lt;req verification-enable='true' verification-keyword='safsaf'&gt;
					&lt;property name='spriteId' type='int' comment=''/&gt;
					&lt;property name='spriteName' type='String' comment=''/&gt;
					&lt;object name='sprite' type='Sprite' comment=''/&gt;
				&lt;/req&gt;
				&lt;res&gt;
					&lt;property name='result' type='boolean' comment='结果标记'/&gt;
					&lt;object name='sprite1' type='Sprite' comment=''/&gt;
					&lt;object name='sprite2' type='Sprite' comment=''/&gt;
				&lt;/res&gt;
			&lt;/prototype&gt;
		&lt;/prototypes&gt;
		
		典型的用法：
			HashMap&lt;String,String&gt; env = new HashMap&lt;String,String&gt;(); 
			env.put("package", "com.xuanzhi.tools.transport.message");
			env.put("imports", "java.nio.ByteBuffer;com.xuanzhi.tools.transport.*");
			env.put("version", "1.0");
			env.put("classes", "com.xuanzhi.tools.transport.Sprite");
			env.put("output.dir", "./src/test/com/xuanzhi/tools/transport/message");
			
			Document dom = XmlUtil.loadStringWithoutTitle(domContent);
			mc.complie(dom);
	

		数据包头信息：
			在未加此信息头时，包的头信息为k + 8个字节，其中k为包的长度占用的字节数，
			接下来为包的类型，占4个字节，包的序列号占用4个字节。
			加信息头之后，我们可以在头中附带其他信息，为了应用更加扩展。 --- myzdf 2009-8-15
			&lt;header&gt;
					&lt;property name='playerId' type='int' comment=''/&gt;
					&lt;property name='spriteDesp' type='String' encoding="utf-8" length='16' comment=''/&gt;
					&lt;property name='x' type='short' comment='坐标x'/&gt;
					&lt;property name='y' type='short' comment='坐标y'/&gt;
			&lt;/header&gt;
			
			头信息只支持简单的类型，不支持数组。
			头信息的含义，就是所有的消息都含有的属性，并且在构造函数中需要明确传递这些参数。
			
			设计头信息的目的，主要是让所有的消息都有共同的属性，以及这些属性在数据包中的位置都是固定了
			为了将来用四层交换机进行分发。                       --- myzdf 2009-8-15
			
			头信息中，属性值不能为java的关键字，以及len,offset,sn,type这几个名称。
		
		
		增加一些新的功能：
			1）同一个消息ID，只能使用一次，如果使用多次，进行提示。
			
			2）没有修改过的协议包，就不用生成了。
			
			3）没有配置的req_name或者res_name，就不用生成了
			
			4）协议分服务器端发起的，还是客户端发起的，还是服务器客户端都发起的。
			  根据不同的发起端，生成代码的时候，有选择的生成，以减少代码量
			  还未实现 --------2009-10-05
 * </pre>
 * 
 */

public class MessageComplier {

	/**
	 * 参数：
	 * -imports str 值为分号分隔的字符串
	 * -package str 生成的类的包
	 * -output.dir str 输出的目录，如果不设置，输出到标准输出流
	 * -factory.extends str 布尔型，用于指明factory是否继承与AbstractMessageFactory
	 * -version str 版本，用于JavaDoc
	 * -classes str 使用到的Java类，逗号分隔。如果某个消息用到其中的类，将自动增加到import声明中。
	 * -exclude pattern 符合正则表达式的消息将不包含在生成代码的列表中
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{
		HashMap<String,String> env = new HashMap<String,String>();
		ArrayList<String> files = new ArrayList<String>();
		String encoding =null;
		for(int i = 0 ; i < args.length ; i++){
			if(args[i].equals("-exclude") && i < args.length -1){
				env.put("exclude", args[i+1]);
				i++;
			}else
			if(args[i].equals("-imports") && i < args.length -1){
				env.put("imports", args[i+1]);
				i++;
			}else if(args[i].equals("-package") && i < args.length -1){
				env.put("package", args[i+1]);
				i++;
			}else if(args[i].equals("-output.dir") && i < args.length -1){
				env.put("output.dir", args[i+1]);
				i++;
			}else if(args[i].equals("-factory.extends") && i < args.length -1){
				env.put("factory.extends", args[i+1]);
				i++;
			}else if(args[i].equals("-version") && i < args.length -1){
				env.put("version", args[i+1]);
				i++;
			}else if(args[i].equals("-classes") && i < args.length -1){
				String s = env.get("classes");
				if(s == null) s = args[i+1];
				else s = s + "," + args[i+1];
				env.put("classes", s);
				i++;
			}else if(args[i].equals("-file") && i < args.length -1){
				files.add(args[i+1]);
				i++;
			}else if(args[i].equals("-encoding") && i< args.length-1 ){
				encoding = args[i+1];
				i++;
			}else if(args[i].equals("-h")){
				System.out.println("java -cp $cp com.xuanzhi.tools.transport.MessageComplier options <-file file>");
				System.out.println("author: myzdf");
				System.out.println("version: 1.0");
				System.out.println("options:");
				System.out.println("        -imports str 值为分号分隔的字符串");
				System.out.println("        -package str 生成的类的包");
				System.out.println("        -output.dir str 输出的目录，如果不设置，输出到标准输出流");
				System.out.println("        -factory.extends str 布尔型，用于指明factory是否继承与AbstractMessageFactory");
				System.out.println("        -version str 版本，用于JavaDoc");
				System.out.println("        -classes str 使用到的Java类，逗号分隔。如果某个消息用到其中的类，将自动增加到import声明中");
				System.out.println("        -exclude pattern 符合正则表达式的消息将不包含在生成代码的列表中");
				System.out.println("        -file file 配置文件，必须项");
				return;
			}
		}
		if(files.isEmpty() ){
			System.out.println("缺少参数，请用-h查阅帮助！");
			return;
		}

		MessageComplier mc = new MessageComplier(env);
		
		ArrayList<Document> doms = new ArrayList<Document>();
		String factoryName="DefaultMessageFactory";
//		String handlerName="ClientMessageHandler";
//		int maxMessageSize=4096;
		Element init_paramsElement = null;
		Element verificationElement = null;
		boolean enableSerialize = false;
//		int headerLen=12;
		for( String file:files){
			Document dom;
			if( encoding!= null){
				dom = XmlUtil.load(file,encoding);
			}else{
				dom = XmlUtil.load(file);			
			}
			doms.add(dom);
			
			mc.headerElement = XmlUtil.getStrictChildByName(dom.getDocumentElement(), "header");
			init_paramsElement = XmlUtil.getChildByName(dom.getDocumentElement(), "init-params");
			verificationElement = XmlUtil.getChildByName(dom.getDocumentElement(), "verification");
			
			Element ele = XmlUtil.getChildByName(dom.getDocumentElement(), "factory");
			factoryName = XmlUtil.getAttributeAsString(ele, "name", "DefaultMessageFactory",null);
//			maxMessageSize = XmlUtil.getAttributeAsInteger(ele, "max-send-packet-size", 4096);			
//			Element ele2 = XmlUtil.getChildByName(dom.getDocumentElement(), "handler");
//			handlerName = XmlUtil.getAttributeAsString(ele2, "name", "ClientMessageHandler",null);
//			headerLen = XmlUtil.getAttributeAsInteger(ele, "headerLen", 12);
			enableSerialize = XmlUtil.getAttributeAsBoolean(ele, "enable-serialize",false);
		}
		ArrayList<Element> protos = new ArrayList<Element>();
		for( Document dom:doms){
			Element protoTypeEles[] = XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype");
			for( Element e: protoTypeEles){
				protos.add(e);
			}
		}
		if( doms.size()> 0){
			mc.complie(protos.toArray(new Element[0]),init_paramsElement,verificationElement, factoryName,enableSerialize);
		}
//		
//		Document dom;
//		if( encoding!= null){
//			dom = XmlUtil.load(file,encoding);
//		}else{
//			dom = XmlUtil.load(file);			
//		}
//		mc.complie(dom);
	}
	

	public static String java_keywords[] = new String[]{
		"abstract","boolean","break","byte","case",
		"catch","char","class","continue","default",
		"do","double","else","extends","false",
		"final","finally","float","for","if",
		"implements","import","instanceof","int","interface",
		"long","native","new","null","package",
		"private","protected","public","return","short",
		"static","super","switch","synchronized","this",
		"throw","throws","transient","true","try",
		"void","volatile","while","const","goto",
		"offset","seqNum","content","size"
		};

	
	public static boolean isKeywords(String name){
		for(int i = 0 ; i < java_keywords.length ; i++){
			if(name.equals(java_keywords[i])) return true;
		}
		return false;
	}
	
	private static int randomInt = 0;
	
	public static String randomIntegerString(){
		randomInt++;
		String s = "" + randomInt;
		while(s.length() < 4){
			s = "0"+s;
		}
		return s;
	}
	
	HashMap<String,String> env;
	Element headerElement = null;
	
	int stringMaxLength = 16 * 1024;
	int arrayMaxLength = 4 * 1024;
	int objectMaxLength = 8 * 1024;
	/**
	 * 构造函数，env为环境变量，支持如下的环境变量：
	 * imports 值为分号分隔的字符串
	 * package 生成的类的包
	 * output.dir 输出的目录
	 * factory.extends 布尔型，用于指明factory是否继承与AbstractMessageFactory
	 * version 版本，用于JavaDoc
	 * classes 使用到的Java类，逗号分隔。如果某个消息用到其中的类，将自动增加到import声明中。
	 * 
	 * @param env
	 */
	public MessageComplier(HashMap<String,String> env){
		this.env = env;
	}
	
	/**
	 * 根据配置文件生成Java Class
	 * @param dom
	 * @throws Exception
	 */
	public void complie(Element protoTypeEles[],Element init_paramsElement,Element verificationElement,String factoryName,boolean enableSerialize) throws Exception{
		
		stringMaxLength = XmlUtil.getAttributeAsInteger(init_paramsElement, "stringMaxLength",stringMaxLength);
		arrayMaxLength = XmlUtil.getAttributeAsInteger(init_paramsElement, "arrayMaxLength",arrayMaxLength);
		objectMaxLength = XmlUtil.getAttributeAsInteger(init_paramsElement, "objectMaxLength",objectMaxLength);
		
		LinkedHashMap<String,StringBuffer> javaClassMap = new LinkedHashMap<String,StringBuffer>(); 
		StringBuffer sb = generateFactory(protoTypeEles, factoryName,enableSerialize);
		javaClassMap.put(factoryName,sb);
		
		if(verificationElement != null){
			String md5Name = XmlUtil.getAttributeAsString(verificationElement, "name", "MD5",null);
			sb = this.generateMD5(verificationElement);
			javaClassMap.put(md5Name,sb);
			env.put("md5", md5Name);
		}
		
		
		HashMap<Long,String> checkMap = new HashMap<Long,String>();
		StringBuffer errorMessage = new StringBuffer();
		for(int i = 0 ; i < protoTypeEles.length ; i++){
			Element e = protoTypeEles[i];
			String reqName = XmlUtil.getAttributeAsString(e, "req_name",null,null);
			String resName = XmlUtil.getAttributeAsString(e, "res_name",null,null);
			Long reqType = Long.decode(XmlUtil.getAttributeAsString(e, "req_type","0",null));
			Long resType = Long.decode(XmlUtil.getAttributeAsString(e, "res_type","0",null));
			
			if(reqName != null && checkMap.containsKey(reqType)){
				errorMessage.append("\tERROR: more than one message using " + XmlUtil.getAttributeAsString(e, "req_type",null) + "\n");
			}
			
			if(reqName != null)
				checkMap.put(reqType, reqName);
			
			if(resName != null && checkMap.containsKey(resType)){
				errorMessage.append("\tERROR: more than one message using " + XmlUtil.getAttributeAsString(e, "res_type",null) + "\n");
			}
			
			if(resName != null)
				checkMap.put(resType, resName);
		}
		
		if(errorMessage.length() > 0){
			throw new Exception("complie error:\n" + errorMessage.toString());
		}
		
		for(int i = 0 ; i < protoTypeEles.length ; i++){
			Element e = protoTypeEles[i];
			String reqName = XmlUtil.getAttributeAsString(e, "req_name",null,null);
			String resName = XmlUtil.getAttributeAsString(e, "res_name",null,null);
			String exclude = null;
			if(env.get("exclude") != null){
				exclude = env.get("exclude");
			}
			if(reqName != null && (exclude == null || reqName.matches(exclude) == false)){
				sb = generateRequestMessage(factoryName,e);
				javaClassMap.put(reqName,sb);
			}
			if(resName != null && (exclude == null || resName.matches(exclude) == false)){
				sb = generateResponseMessage(factoryName,e);
				javaClassMap.put(resName,sb);
			}
		}
		
		String dir = env.get("output.dir");
		if(dir == null){
			Iterator<Map.Entry<String,StringBuffer>> it = javaClassMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,StringBuffer> me = it.next();
				System.out.println("=====================  "+me.getKey()+".java  ====================");
				System.out.println(me.getValue());
				System.out.println("\n\n\n");
			}
		}else{
			File d = new File(dir);
			if(d.exists() && d.isDirectory()){
				Iterator<Map.Entry<String,StringBuffer>> it = javaClassMap.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String,StringBuffer> me = it.next();
					byte bytes[] = me.getValue().toString().getBytes("utf-8");
					
					File file = new File(d,me.getKey()+".java");
					System.out.print("generate " + file.getAbsolutePath() + " ... ... ");
					boolean sameFile = true;
					if(file.exists()){
						FileInputStream input = new FileInputStream(file);
						byte fileContent[] = new byte[(int)file.length()];
						input.read(fileContent);
						input.close();
						if(fileContent.length == bytes.length){
							for(int i = 0 ; sameFile && i < bytes.length ; i++){
								if(bytes[i] != fileContent[i]){
									sameFile = false;
								}
							}
						}else{
							sameFile = false;
						}
						
					}else{
						sameFile = false;
					}
					if(sameFile){
						System.out.println("success! no change.");
					}else{
						FileOutputStream output = new FileOutputStream(file);
						output.write(bytes);
						output.flush();
						output.close();
						System.out.println("success! write "+bytes.length+" bytes to file.");
					}
				}
			}else{
				throw new Exception("dir ["+dir+"] not exists.");
			}
		}
	}
	
	/**
	 * <factory name="GameMessageFactory"/>
	 * 
	 * @param ele
	 * @return
	 */
	protected StringBuffer generateFactory(Element protoTypeEles[],String factoryName,boolean enableSerialize)  throws Exception{
		String ls = System.getProperty("line.separator");
		
		StringBuffer sb = new StringBuffer();
		boolean extend = true;
		if(env.get("factory.extends") != null){
			if(env.get("factory.extends").equalsIgnoreCase("true")){
				extend = true;
			}else{
				extend = false;
			}
		}
		generatePackageAndImports(sb);
		
		sb.append(ls);
		sb.append("/**"+ls);
		sb.append(" * 消息工厂类，此工厂类是由MessageComplier自动生成，请不要手动修改。<br>"+ls);
		//sb.append(" * 生成时间："+DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss S")+"<br>"+ls);
		sb.append(" * 版本号："+env.get("version")+"<br>"+ls);
		sb.append(" * 各数据包的定义如下：<br><br>"+ls);
		sb.append(" * <table border=\"0\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" bgcolor=\"#000000\" align=\"center\">"+ls);
		sb.append(" * <tr bgcolor=\"#00FFFF\" align=\"center\"><td>请求消息</td><td>类型</td><td>响应消息</td><td>类型</td><td>说明</td></tr>");
		
		for(int i = 0 ; i < protoTypeEles.length ; i++){
			Element e = protoTypeEles[i];
			String reqName = XmlUtil.getAttributeAsString(e, "req_name","-",null);
			String resName = XmlUtil.getAttributeAsString(e, "res_name","-",null);
			String reqType = XmlUtil.getAttributeAsString(e, "req_type","-",null);
			String resType = XmlUtil.getAttributeAsString(e, "res_type","-",null);
			String comment = XmlUtil.getAttributeAsString(e, "comment","无说明",null);
			String color = "#FFFFFF";
			if(i % 2 != 0) color = "#FAFAFA";
			sb.append(" * <tr bgcolor=\""+color+"\" align=\"center\"><td><a href='./"+reqName+".html'>"+reqName+"</a></td><td>"+reqType+"</td><td><a href='./"+resName+".html'>"+resName+"</a></td><td>"+resType+"</td><td>"+comment+"</td></tr>"+ls);
		}
		sb.append(" * </table>"+ls);
		sb.append(" */"+ls);
		
		sb.append("public class " + factoryName);
		if(extend){
			sb.append(" extends AbstractMessageFactory");
		}
		sb.append(" {" + ls);
		sb.append(ls);
		sb.append("\tprivate static long sequnceNum = 0L;"+ls);
		sb.append("\tpublic synchronized static long nextSequnceNum(){"+ls);
		sb.append("\t\tsequnceNum ++;"+ls);
		sb.append("\t\tif(sequnceNum >= 0x7FFFFFFF){"+ls);
		sb.append("\t\t\tsequnceNum = 1L;"+ls);
		sb.append("\t\t}"+ls);
		sb.append("\t\treturn sequnceNum;"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);
		sb.append("\tprotected static "+factoryName+" self;"+ls);
		sb.append(ls);
		sb.append("\tpublic static "+factoryName+" getInstance(){"+ls);
		sb.append("\t\tif(self != null){"+ls);
		sb.append("\t\t\treturn self;"+ls);
		sb.append("\t\t}"+ls);
		sb.append("\t\tsynchronized("+factoryName+".class){"+ls);
		sb.append("\t\t\tif(self != null) return self;"+ls);
		sb.append("\t\t\tself = new "+factoryName+"();"+ls);
		sb.append("\t\t}"+ls);
		sb.append("\t\treturn self;"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);

		if(!extend){
			sb.append("\tpublic int getNumOfByteForMessageLength() {"+ls);
			sb.append("\t\treturn 4;"+ls);
			sb.append("\t}"+ls);
			sb.append(ls);		
			sb.append("\t/**"+ls);
			sb.append("\t * 将网络传输的字节数组转化为整数，遵循高位字节在前的规则。"+ls);
			sb.append("\t * 比如：字节数组为"+ls);
			sb.append("\t *     {0x00,0x7c,0x12,0x34,0xab,0xcd,0x00,0x88}"+ls);
			sb.append("\t * 那么对应的整数为为："+ls);
			sb.append("\t *     0x007c1234abcd0088"+ls);     
			sb.append("\t */"+ls);
			sb.append("\tpublic long byteArrayToNumber(byte[] bytes, int offset,int numOfBytes) {"+ls);
			sb.append("\t\tlong l = 0L;"+ls);
			sb.append("\t\tfor(int i = 0 ; i < numOfBytes ; i++){"+ls);
			sb.append("\t\t\tl = l | ((long)(bytes[offset + i] & 0xFF) << (8 * (numOfBytes - 1 - i)));"+ls);
			sb.append("\t\t}"+ls);
			sb.append("\t\treturn l;"+ls);
			sb.append("\t}"+ls);
			sb.append(ls);
			sb.append("\t/**"+ls);
			sb.append("\t * 将整数转化为网络传输的字节数组，遵循高位字节在前的规则。"+ls);
			sb.append("\t * 比如：	整数为"+ls);
			sb.append("\t *     0x 007c 1234 abcd 0088"+ls);
			sb.append("\t * 那么对应的字节数组为："+ls);
			sb.append("\t *     {0x00,0x7c,0x12,0x34,0xab,0xcd,0x00,0x88}"+ls);     
			sb.append("\t */"+ls);
			sb.append("\tpublic byte[] numberToByteArray(long n, int numOfBytes) {"+ls);
			sb.append("\t\tbyte bytes[] = new byte[numOfBytes];"+ls);
			sb.append("\t\tfor(int i = 0 ; i < numOfBytes ; i++)"+ls);
			sb.append("\t\t\tbytes[i] = (byte)((n >> (8 *(numOfBytes - 1 - i))) & 0xFF);"+ls);
			sb.append("\t\treturn bytes;"+ls);
			sb.append("\t}"+ls);
			sb.append(ls);	
			sb.append("\t/**"+ls);
			sb.append("\t * 将整数转化为网络传输的字节数组，遵循高位字节在前的规则。"+ls);
			sb.append("\t * 比如：	整数为"+ls);
			sb.append("\t *     0x 007c 1234 abcd 0088"+ls);
			sb.append("\t * 那么对应的字节数组为："+ls);
			sb.append("\t *     {0x00,0x7c,0x12,0x34,0xab,0xcd,0x00,0x88}"+ls);     
			sb.append("\t */"+ls);
			sb.append("\tpublic byte[] numberToByteArray(int n, int numOfBytes) {"+ls);
			sb.append("\t\tbyte bytes[] = new byte[numOfBytes];"+ls);
			sb.append("\t\tfor(int i = 0 ; i < numOfBytes ; i++)"+ls);
			sb.append("\t\t\tbytes[i] = (byte)((n >> (8*(numOfBytes - 1 - i))) & 0xFF);"+ls);
			sb.append("\t\treturn bytes;"+ls);
			sb.append("\t}"+ls);
			sb.append(ls);
		}
		
		sb.append("\tpublic Message newMessage(byte[] messageContent,int offset,int size)"+ls);
		sb.append("\t\tthrows MessageFormatErrorException, ConnectionException,Exception {"+ls);
		sb.append("\t\tint len = (int)byteArrayToNumber(messageContent,offset,getNumOfByteForMessageLength());"+ls);
		sb.append("\t\tif(len != size)"+ls);
		sb.append("\t\t\tthrow new MessageFormatErrorException(\"message length not match\");"+ls);
		sb.append("\t\tint end = offset + size;"+ls);
		sb.append("\t\toffset += getNumOfByteForMessageLength();"+ls);
		sb.append("\t\tlong type = byteArrayToNumber(messageContent,offset,4);"+ls);
		sb.append("\t\toffset += 4;"+ls);
		sb.append("\t\tlong sn = byteArrayToNumber(messageContent,offset,4);"+ls);
		sb.append("\t\toffset += 4;"+ls);
		
		ArrayList<String> headerFields = new ArrayList<String>();
		if(this.headerElement != null){
			sb.append("\t\t//自定义头"+ls);
			Element fields[] = XmlUtil.getChildrenByName(headerElement,"property");
			for(int j = 0 ; j < fields.length ; j++){
				String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
				if(isKeywords(fieldName)){
					throw new java.lang.IllegalArgumentException("头信息中的字段["+fieldName+"]为保留字，请更换其他名称");
				}
				String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
				Class cl = getPrimitiveClass(fieldType);
				if(cl.isArray()){
					throw new java.lang.UnsupportedOperationException("头信息中，不支持数组类型");
				}else{
					if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE|| cl == Long.TYPE){
						int length = getPrimitiveSize(cl);
						sb.append("\t\t"+getPrimitiveName(cl)+" "+fieldName+" = ("+getPrimitiveName(cl)+")byteArrayToNumber(messageContent,offset,"+length+");"+ls);
						sb.append("\t\toffset += "+length+";"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\tboolean "+fieldName+" = byteArrayToNumber(messageContent,offset,1) != 0;"+ls);
						sb.append("\t\toffset += 1;"+ls);
					}else if(cl == Float.TYPE){
						int length = getPrimitiveSize(cl);
						sb.append("\t\t"+getPrimitiveName(cl)+" "+fieldName+" = Float.intBitsToFloat(byteArrayToNumber(messageContent,offset,"+length+"));"+ls);
						sb.append("\t\toffset += "+length+";"+ls);
					}else if(cl == String.class){
						String encoding = XmlUtil.getAttributeAsString(fields[j], "encoding","",null);
						int strLen = XmlUtil.getAttributeAsInteger(fields[j], "length",0);
						if(strLen <= 0){
							sb.append("\t\tlen = (int)byteArrayToNumber(messageContent,offset,2);"+ls);
							sb.append("\t\toffset += 2;"+ls);
							sb.append("\t\tif(len < 0 || len > 1024) throw new MessageFormatErrorException(\"string length [\"+len+\"] big than the max length [1024]\");"+ls);
							if(encoding.trim().length()>0){
								sb.append("\t\tString "+fieldName+" = null;"+ls);
//								sb.append("\t\ttry{"+ls);
								sb.append("\t\t\t"+fieldName+" = new String(messageContent,offset,len,\""+encoding+"\");"+ls);
//								sb.append("\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
//								sb.append("\t\t\tthrow new MessageFormatErrorException(\"不支持的字符串编码\");"+ls);
//								sb.append("\t\t}"+ls);
							}
							else
								sb.append("\t\tString "+fieldName+" = new String(messageContent,offset,len);"+ls);
							sb.append("\t\toffset += len;"+ls);
						}else{
							if(encoding.trim().length()>0){
								sb.append("\t\tString "+fieldName+" = null;"+ls);
//								sb.append("\t\ttry{"+ls);
								sb.append("\t\t\t"+fieldName+" = new String(messageContent,offset,"+strLen+",\""+encoding+"\").trim();"+ls);
//								sb.append("\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
//								sb.append("\t\t\tthrow new MessageFormatErrorException(\"不支持的字符串编码\");"+ls);
//								sb.append("\t\t}"+ls);
							}
							else
								sb.append("\t\tString "+fieldName+" = new String(messageContent,offset,"+strLen+").trim();"+ls);
							sb.append("\t\toffset += "+strLen+";"+ls);
						}
					}
				}
				headerFields.add(fieldName);
			}
		}
		String headerStr = "";
		for(int j = 0 ; j < headerFields.size() ; j++){
			headerStr += headerFields.get(j) + ",";
		}
		
		sb.append(ls);
	//	sb.append("\t\ttry{"+ls);
		
//		protoTypeEles = XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype");
		boolean first = true;
		for(int i = 0 ; i < protoTypeEles.length ; i++){
			Element e = protoTypeEles[i];
			String reqName = XmlUtil.getAttributeAsString(e, "req_name",null,null);
			String resName = XmlUtil.getAttributeAsString(e, "res_name",null,null);
			String reqType = XmlUtil.getAttributeAsString(e, "req_type","0",null);
			String resType = XmlUtil.getAttributeAsString(e, "res_type","0",null);
			
 			String exclude = null;
            if(env.get("exclude") != null){
                exclude = env.get("exclude");
            }
            
			if(reqName != null && (exclude == null || reqName.matches(exclude) == false)){
				if(first){
					sb.append("\t\t\tif(type == "+reqType+"L){"+ls);
					first = false;
				}else{
					sb.append("\t\t\t}else if(type == "+reqType+"L){"+ls);
				}
				
//				sb.append("\t\t\t\ttry {"+ls);
				sb.append("\t\t\t\t\treturn new "+reqName+"(sn,"+headerStr+"messageContent,offset,end - offset);"+ls);
//				sb.append("\t\t\t\t} catch (Exception e) {"+ls);
//				sb.append("\t\t\t\t\tthrow new MessageFormatErrorException(\"construct "+reqName+" error:\" + e.getClass() + \":\" + e.getMessage(),e);"+ls);
//				sb.append("\t\t\t\t}"+ls);
				
			}
			
			if(resName != null && (exclude == null || resName.matches(exclude) == false)){
				if(first){
					sb.append("\t\t\tif(type == "+resType+"L){"+ls);
					first = false;
				}else{
					sb.append("\t\t\t}else if(type == "+resType+"L){"+ls);
				}
				
				if(resName != null && (exclude == null || resName.matches(exclude) == false)){
//					sb.append("\t\t\t\ttry {"+ls);
					sb.append("\t\t\t\t\treturn new "+resName+"(sn,"+headerStr+"messageContent,offset,end - offset);"+ls);
//					sb.append("\t\t\t\t} catch (Exception e) {"+ls);
//					sb.append("\t\t\t\t\tthrow new MessageFormatErrorException(\"construct "+resName+" error:\" + e.getClass() + \":\" + e.getMessage(),e);"+ls);
//					sb.append("\t\t\t\t}"+ls);
				}
			}
		}
		
		sb.append("\t\t\t}else{"+ls);
		sb.append("\t\t\t\tthrow new MessageFormatErrorException(\"unknown message type [\"+type+\"]\");"+ls);
		sb.append("\t\t\t}"+ls);
		
//		sb.append("\t\t}catch(IndexOutOfBoundsException e){"+ls);
//		sb.append("\t\t\tthrow new ConnectionException(\"parse message error\",e);"+ls);
//		sb.append("\t\t}"+ls);
		sb.append("\t}"+ls);
		
		if(enableSerialize){
			sb.append("\t/**"+ls);
			sb.append("\t* 将对象转化为数组，可能抛出异常"+ls);
			sb.append("\t*/"+ls);
			sb.append("\tpublic byte[] objectToByteArray(Object obj) {"+ls);
			sb.append("\t\tif(obj == null) return new byte[0];"+ls);
			sb.append("\t\ttry {"+ls);
			sb.append("\t\t\tjava.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();"+ls);
			sb.append("\t\t\tjava.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(out);"+ls);
			sb.append("\t\t\toos.writeObject(obj);"+ls);
			sb.append("\t\t\toos.close();"+ls);
			sb.append("\t\t\treturn out.toByteArray();"+ls);
			sb.append("\t\t} catch(Exception e) {"+ls);
			sb.append("\t\t\te.printStackTrace();"+ls);
			sb.append("\t\t}"+ls);
			sb.append("\t\treturn new byte[0];"+ls);
			sb.append("\t}"+ls);
			
			sb.append("\t/**"+ls);
			sb.append("\t* 将数组转化为对象，可能抛出异常"+ls);
			sb.append("\t*/"+ls);
			sb.append("\tpublic Object byteArrayToObject(byte[] bytes, int offset,int numOfBytes) throws Exception{"+ls);
			sb.append("\t\tif(numOfBytes == 0) return null;"+ls);
			sb.append("\t\tjava.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(bytes,offset,numOfBytes);"+ls);
			sb.append("\t\tjava.io.ObjectInputStream o = new java.io.ObjectInputStream(input);"+ls);
			sb.append("\t\tObject obj = o.readObject();"+ls);
			sb.append("\t\to.close();"+ls);
			sb.append("\t\treturn obj;"+ls);
			sb.append("\t}"+ls);
		}
		
		sb.append("}"+ls);
		
		
		return sb;
	}
	
	protected StringBuffer generateMD5(Element ele)  throws Exception{
		String ls = System.getProperty("line.separator");
		
		String md5Name = XmlUtil.getAttributeAsString(ele, "name", "MD5",null);
		String a = XmlUtil.getAttributeAsString(ele, "a", "0x67452301",null);
		String b = XmlUtil.getAttributeAsString(ele, "b", "0xEFCDAB89",null);
		String c = XmlUtil.getAttributeAsString(ele, "c", "0x98BADCFE",null);
		String d = XmlUtil.getAttributeAsString(ele, "d", "0x10325476",null);
		
		StringBuffer sb = new StringBuffer();
		
		String packageName = env.get("package");
		if(packageName == null) packageName = "";
		sb.append("package " + packageName + ";" + ls);
		sb.append(ls);
		sb.append(ls);
		sb.append("public class "+md5Name+"	{"+ls);
		sb.append(ls);
		sb.append("\tint a = "+a+";"+ls);
		sb.append("\tint b = "+b+";"+ls);
		sb.append("\tint c = "+c+";"+ls);
		sb.append("\tint d = "+d+";"+ls);
		sb.append(ls);  
		sb.append("\tprivate int add(int x, int y) {"+ls);
		sb.append("\t\treturn ((x&0x7FFFFFFF) + (y&0x7FFFFFFF)) ^ (x&0x80000000) ^ (y&0x80000000);"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);  
		sb.append("\tprivate int rol(int num, int cnt) {"+ls);
		sb.append("\t\treturn (num << cnt) | (num >>> (32 - cnt));"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);  
		sb.append("\tprivate int cmn(int q, int a, int b, int x, int s, int t) {"+ls);
		sb.append("\t\treturn add(rol(add(add(a, q), add(x, t)), s), b);"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);
		sb.append("\tprivate int ff(int a, int b, int c, int d, int x, int s, int t) {"+ls);
		sb.append("\t\treturn cmn((b & c) | ((~b) & d), a, b, x, s, t);"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);
		sb.append("\tprivate int gg(int a, int b, int c, int d, int x, int s, int t)  {"+ls);
		sb.append("\t\treturn cmn((b & d) | (c & (~d)), a, b, x, s, t);"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);
		sb.append("\tprivate int hh(int a, int b, int c, int d, int x, int s, int t) {"+ls);
		sb.append("\t\treturn cmn(b ^ c ^ d, a, b, x, s, t);"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);
		sb.append("\tprivate int ii(int a, int b, int c, int d, int x, int s, int t) {"+ls);
		sb.append("\t\treturn cmn(c ^ (b | (~d)), a, b, x, s, t);"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);  
		sb.append("\tpublic void update(byte bytes[],int offset,int len) {"+ls);
		sb.append("\t\tint n = offset + len;"+ls);
		sb.append("\t\tfor(int i = offset; i < n; i ++){"+ls);
		sb.append("\t\t\tint olda = a;"+ls);
		sb.append("\t\t\tint oldb = b;"+ls);
		sb.append("\t\t\tint oldc = c;"+ls);
		sb.append("\t\t\tint oldd = d;"+ls);
		sb.append(ls);  
		      sb.append("\t\t\ta = ff(a, b, c, d, bytes[i], 7 , 0xD76AA478);"+ls);
		      sb.append("\t\t\td = gg(a, b, c, d, bytes[i], 5 , 0xF61E2562);"+ls);
		      sb.append("\t\t\tb = hh(a, b, c, d, bytes[i], 4 , 0xFFFA3942);"+ls);
		      sb.append("\t\t\tc = ii(a, b, c, d, bytes[i], 6 , 0xF4292244);"+ls);
		      
		      sb.append("\t\t\ta = add(a, olda);"+ls);
		      sb.append("\t\t\tb = add(b, oldb);"+ls);
		      sb.append("\t\t\tc = add(c, oldc);"+ls);
		      sb.append("\t\t\td = add(d, oldd);"+ls);
		sb.append("\t\t}"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);
		sb.append("\tpublic byte[] digest(){"+ls);
			 sb.append("\t\tbyte bytes[] = new byte[16];"+ls);
			 sb.append("\t\tbytes[0] = (byte)(a>>24);"+ls);
			 sb.append("\t\tbytes[1] = (byte)(c>>18);"+ls);
			 sb.append("\t\tbytes[2] = (byte)(b>>8);"+ls);
			 sb.append("\t\tbytes[3] = (byte)(d);"+ls);
			 sb.append("\t\tbytes[4] = (byte)(b>>24);"+ls);
			 sb.append("\t\tbytes[5] = (byte)(a>>18);"+ls);
			 sb.append("\t\tbytes[6] = (byte)(d>>8);"+ls);
			 sb.append("\t\tbytes[7] = (byte)(b);"+ls);
			 sb.append("\t\tbytes[8] = (byte)(c>>24);"+ls);
			 sb.append("\t\tbytes[9] = (byte)(b>>18);"+ls);
			 sb.append("\t\tbytes[10] = (byte)(a>>8);"+ls);
			 sb.append("\t\tbytes[11] = (byte)(a);"+ls);
			 sb.append("\t\tbytes[12] = (byte)(d>>24);"+ls);
			 sb.append("\t\tbytes[13] = (byte)(d>>18);"+ls);
			 sb.append("\t\tbytes[14] = (byte)(c>>8);"+ls);
			 sb.append("\t\tbytes[15] = (byte)(c);"+ls);
			
			sb.append("\t\ta = "+a+";"+ls);
			sb.append("\t\tb = "+b+";"+ls);
			sb.append("\t\tc = "+c+";"+ls);
			sb.append("\t\td = "+d+";"+ls);
			  
			 sb.append("\t\treturn bytes;"+ls);
		sb.append("\t}"+ls);
		
		sb.append("\tpublic static boolean equals(byte[] bytes1,byte[] bytes2){"+ls);
		sb.append("\t\tif(bytes1 == bytes2) return true;"+ls);
		sb.append("\t\tif(bytes1.length != bytes2.length) return false;"+ls);
		sb.append("\t\tfor(int i = 0 ; i < bytes1.length ; i++){"+ls);
		sb.append("\t\t\tif(bytes1[i] != bytes2[i]) return false;"+ls);
		sb.append("\t\t}"+ls);
		sb.append("\t\treturn true;"+ls);
		sb.append("\t}"+ls);
		sb.append("}"+ls);
		
		return sb;
	}
	protected StringBuffer generateRequestMessage(String factory,Element ele)  throws Exception{
		randomInt = 0;
		return generateMessage(factory,ele,true);
	}
	
	protected StringBuffer generateResponseMessage(String factory,Element ele) throws Exception{
		randomInt = 0;
		return generateMessage(factory,ele,false);
	}
	
	protected StringBuffer generateMessage(String factory,Element e,boolean request) throws Exception{
		String ls = System.getProperty("line.separator");
		
		StringBuffer importSB = new StringBuffer();
		generatePackageAndImports(importSB);
		
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		
		Element pes[] = null;
		if(request)
			pes = getPropertyElements(XmlUtil.getChildByName(e, "req"));
		else
			pes = getPropertyElements(XmlUtil.getChildByName(e, "res"));
		
		String reqName = null;
		if(request)
			reqName = XmlUtil.getAttributeAsString(e, "req_name",null,null);
		else
			reqName = XmlUtil.getAttributeAsString(e, "res_name",null,null);
		
		boolean verification = false;
		if(request)
			verification = XmlUtil.getAttributeAsBoolean(XmlUtil.getChildByName(e, "req"),"verification-enable",false);
		String keyword = XmlUtil.getAttributeAsString(XmlUtil.getChildByName(e, "req"),"verification-keyword","",null);

		String md5Name = null;
		if(verification){
			md5Name = env.get("md5");
			if(md5Name == null) throw new Exception("message "+reqName+" need verification but cann't find md5 class.");
			
		}
		String reqType = null;
		if(request)
			reqType = XmlUtil.getAttributeAsString(e, "req_type",null);
		else
			reqType = XmlUtil.getAttributeAsString(e, "res_type",null);
		
		//generate comment
		sb2.append("/**"+ls);
		sb2.append(" * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>"+ls);
		//sb2.append(" * 生成时间："+DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss S")+"<br>"+ls);
		sb2.append(" * 版本号："+env.get("version")+"<br>"+ls);
		String comment = XmlUtil.getAttributeAsString(e, "comment","",null);
		sb2.append(" * "+comment+"<br>"+ls);
		sb2.append(" * 数据包的格式如下：<br><br>"+ls);
		sb2.append(" * <table border=\"0\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" bgcolor=\"#000000\" align=\"center\">"+ls);
		sb2.append(" * <tr bgcolor=\"#00FFFF\" align=\"center\"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr>");
		
		
		sb.append(" *  length int getNumOfByteForMessageLength()个字节  包的整体长度，包头的一部分"+ls);
		sb.append(" *  type int 4个字节  包的类型，包头的一部分"+ls);
		sb.append(" *  seqNum int 4个字节  包的序列号，包头的一部分"+ls);
		
		if(this.headerElement != null){
			Element fields[] = XmlUtil.getChildrenByName(headerElement,"property");
			for(int j = 0 ; j < fields.length ; j++){
				String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
				String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
				Class cl = getPrimitiveClass(fieldType);
				if(cl.isArray()){
					throw new java.lang.UnsupportedOperationException("头信息中，不支持数组类型");
				}else{
					if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE||cl == Float.TYPE || cl == Long.TYPE){
						int length = getPrimitiveSize(cl);
						sb.append(" *  "+fieldName+" "+fieldType+" "+length+"个字节  自定义的包头属性，包头的一部分"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append(" *  "+fieldName+" boolean 1个字节  自定义的包头属性，包头的一部分"+ls);
					}else if(cl == String.class){
						String encoding = XmlUtil.getAttributeAsString(fields[j], "encoding","",null);
						int strLen = XmlUtil.getAttributeAsInteger(fields[j], "length",0);
						sb.append(" *  "+fieldName+" String 1个字节  自定义的包头属性，包头的一部分"+ls);
						if(strLen <= 0){
							if(encoding.trim().length()>0)
								sb.append(" *  "+fieldName+" String 变长字符串，编码为"+encoding+"  自定义的包头属性，包头的一部分"+ls);
							else
								sb.append(" *  "+fieldName+" String 变长字符串，编码采用系统编码  自定义的包头属性，包头的一部分"+ls);
							
						}else{
							if(encoding.trim().length()>0)
								sb.append(" *  "+fieldName+" String 固定长字符串，"+strLen+"个字节，编码为"+encoding+"  自定义的包头属性，包头的一部分"+ls);
							else
								sb.append(" *  "+fieldName+" String 变长字符串，"+strLen+"个字节，编码采用系统编码  自定义的包头属性，包头的一部分"+ls);
						}
					}
				}
			}
		}
		
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			
			if(isKeywords(name)){
				throw new Exception("["+name+"] is reserve keyword, please change to other string.");
			}
			
			if(pes[i].getNodeName().equals("object")){
				sb.append(" *  "+name+".serialize.length int 4  序列化的数组长度"+ls);
				sb.append(" *  "+name+" byte[] "+name+".serialize.length  序列化的数组字节数"+ls);
				
				String classesStr = env.get("classes");
				if(classesStr == null) classesStr = "";
				String classNames[] = classesStr.split(",");
				for(int k = 0 ; k < classNames.length ; k++){
					if(classNames[k].lastIndexOf(".")>0){
						if(type.equals(classNames[k].substring(classNames[k].lastIndexOf(".")+1))){
							if(importSB.toString().indexOf(classNames[k]) == -1)
								importSB.append("import "+classNames[k]+";"+ls);
						}
					}
				}
			}else if(pes[i].getNodeName().equals("property")){
				Class cl = getPrimitiveClass(type);
				if(cl.isArray()){
					sb.append(" *  "+name+".length int 4  数组长度"+ls);
					if(cl.getComponentType() == Byte.TYPE ){
						sb.append(" *  "+name+" byte[] "+name+".length  数组实际长度"+ls);
					}else{
						if(cl.getComponentType() == Character.TYPE 
								|| cl.getComponentType() == Short.TYPE
								|| cl.getComponentType() == Integer.TYPE
								|| cl.getComponentType() == Float.TYPE
								|| cl.getComponentType() == Long.TYPE){
							sb.append(" *  "+name+" "+type+" "+name+".length * "+getPrimitiveSize(cl.getComponentType())+"  数组实际长度"+ls);
						}else if(cl.getComponentType() == Boolean.TYPE){
							sb.append(" *  "+name+" "+type+" "+name+".length * 1  数组实际长度，0==false，其他为true"+ls);
						}else if(cl.getComponentType() == String.class){
							sb.append(" *  "+name+"[0].length int 2  字符串实际长度"+ls);
							sb.append(" *  "+name+"[0] String "+name+"[0].length  字符串对应的byte数组"+ls);
							sb.append(" *  "+name+"[1].length int 2  字符串实际长度"+ls);
							sb.append(" *  "+name+"[1] String "+name+"[1].length  字符串对应的byte数组"+ls);
							sb.append(" *  "+name+"[2].length int 2  字符串实际长度"+ls);
							sb.append(" *  "+name+"[2] String "+name+"[2].length  字符串对应的byte数组"+ls);
							sb.append(" *  ......... 重复"+ls);
						}
					}
				}else{
					
					if(cl == Byte.TYPE || cl == Character.TYPE	|| cl == Short.TYPE	|| cl == Integer.TYPE ||cl == Float.TYPE || cl == Long.TYPE){
						int length = getPrimitiveSize(cl);
						sb.append(" *  "+name+" "+type+" "+length+"个字节  配置的长度"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append(" *  "+name+" "+type+" 1个字节  布尔型长度,0==false，其他为true"+ls);
					}else if(cl == String.class){
						int length = XmlUtil.getAttributeAsInteger(pes[i],"length", 0);
						if(length <= 0){
							sb.append(" *  "+name+".length short 2个字节  字符串实际长度"+ls);
							sb.append(" *  "+name+" String "+name+".length  字符串对应的byte数组"+ls);
						}else{
							sb.append(" *  "+name+" String "+length+"  固定长度的字符串，不够长度右边用\0补齐，多余的截取。汉字可能被截成乱码"+ls);
						}
					}
				}
			}else{ //object-property
				if(type.endsWith("[]")){
					type = type.substring(0, type.length()-2);
					sb.append(" *  "+name+".length int 4个字节  "+type+"数组长度"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int k = 0 ; k < 3 ; k++){
						for(int j = 0 ; j < fields.length ; j++){
							String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
							String getter = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
							String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
							Class cl = getPrimitiveClass(fieldType);
							if(cl.isArray()){
								cl = cl.getComponentType();
								sb.append(" *  "+name+"["+k+"]."+fieldName+".length int 4个字节  对象属性为数组，数组的长度"+ls);
								if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE ||cl == Float.TYPE || cl == Long.TYPE){
									sb.append(" *  "+name+"["+k+"]."+fieldName+" "+fieldType+" "+name+"["+k+"]."+fieldName+".length * "+getPrimitiveSize(cl)+"个字节  对象属性为数组，数组实际的数据大小"+ls);
								}else if(cl == Boolean.TYPE){
									sb.append(" *  "+name+"["+k+"]."+fieldName+" "+fieldType+" "+name+"["+k+"]."+fieldName+".length * 1个字节  对象属性为数组，布尔型数组实际的数据大小"+ls);
								}else if(cl == String.class){
									sb.append(" *  "+name+"["+k+"]."+fieldName+"[0].length short 2个字节  字符串实际长度"+ls);
									sb.append(" *  "+name+"["+k+"]."+fieldName+"[0] String "+name+"["+k+"]."+fieldName+"[0].length  字符串对应的byte数组"+ls);
									sb.append(" *  "+name+"["+k+"]."+fieldName+"[1].length short 2个字节  字符串实际长度"+ls);
									sb.append(" *  "+name+"["+k+"]."+fieldName+"[1] String "+name+"["+k+"]."+fieldName+"[1].length  字符串对应的byte数组"+ls);
									sb.append(" *  "+name+"["+k+"]."+fieldName+"[2].length short 2个字节  字符串实际长度"+ls);
									sb.append(" *  "+name+"["+k+"]."+fieldName+"[2] String "+name+"["+k+"]."+fieldName+"[2].length  字符串对应的byte数组"+ls);
									sb.append(" *  ......... 重复"+ls);
								}
							}else{
								if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE ||cl == Float.TYPE || cl == Long.TYPE){
									int length =getPrimitiveSize(cl);
									sb.append(" *  "+name+"["+k+"]."+fieldName+" "+fieldType+" "+length+"个字节  对象属性配置的长度"+ls);
								}else if(cl == Boolean.TYPE){
									sb.append(" *  "+name+"["+k+"]."+fieldName+" "+fieldType+" 1个字节  布尔型长度"+ls);
								}else if(cl == String.class){
									int length = XmlUtil.getAttributeAsInteger(fields[j],"length", 0);
									if(length <= 0){
										sb.append(" *  "+name+"["+k+"]."+fieldName+".length short 2个字节  字符串实际长度"+ls);
										sb.append(" *  "+name+"["+k+"]."+fieldName+" String "+name+"["+k+"]."+fieldName+".length  字符串对应的byte数组"+ls);
									}else{
										sb.append(" *  "+name+"["+k+"]."+fieldName+" String "+length+"个字节  固定长度字符串,不足长度右边用\0补齐"+ls);
									}
								}
							}
						}
					}
					sb.append(" *  ......... 重复"+ls);
					
				}else{
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String getter = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							cl = cl.getComponentType();
							sb.append(" *  "+name+"."+fieldName+".length int 4个字节  对象属性为数组，数组的长度"+ls);
							if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE ||cl == Float.TYPE || cl == Long.TYPE){
								sb.append(" *  "+name+"."+fieldName+" "+fieldType+" "+name+"."+fieldName+".length * "+getPrimitiveSize(cl)+"个字节  对象属性为数组，数组实际的数据大小"+ls);
							}else if(cl == Boolean.TYPE){
								sb.append(" *  "+name+"."+fieldName+" "+fieldType+" "+name+"."+fieldName+".length * 1个字节  对象属性为数组，布尔型数组实际的数据大小"+ls);
							}else if(cl == String.class){
								sb.append(" *  "+name+"."+fieldName+"[0].length short 2个字节  字符串实际长度"+ls);
								sb.append(" *  "+name+"."+fieldName+"[0] String "+name+"."+fieldName+"[0].length  字符串对应的byte数组"+ls);
								sb.append(" *  "+name+"."+fieldName+"[1].length short 2个字节  字符串实际长度"+ls);
								sb.append(" *  "+name+"."+fieldName+"[1] String "+name+"."+fieldName+"[1].length  字符串对应的byte数组"+ls);
								sb.append(" *  "+name+"."+fieldName+"[2].length short 2个字节  字符串实际长度"+ls);
								sb.append(" *  "+name+"."+fieldName+"[2] String "+name+"."+fieldName+"[2].length  字符串对应的byte数组"+ls);
								sb.append(" *  ......... 重复"+ls);
							}
						}else{
							if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE ||cl == Float.TYPE || cl == Long.TYPE){
								int length = getPrimitiveSize(cl);
								sb.append(" *  "+name+"."+fieldName+" "+fieldType+" "+length+"个字节  对象属性配置的长度"+ls);
							}else if(cl == Boolean.TYPE){
								sb.append(" *  "+name+"."+fieldName+" "+fieldType+" 1个字节  布尔型长度"+ls);
							}else if(cl == String.class){
								int length = XmlUtil.getAttributeAsInteger(fields[j],"length", 0);
								if(length <= 0){
									sb.append(" *  "+name+"."+fieldName+".length short 2个字节  字符串实际长度"+ls);
									sb.append(" *  "+name+"."+fieldName+" String "+name+"."+fieldName+".length  字符串对应的byte数组"+ls);
								}else{
									sb.append(" *  "+name+"."+fieldName+" String "+length+"个字节  固定长度字符串,不足长度右边用\0补齐"+ls);
								}
							}
						}
					}
				}
				
				String classesStr = env.get("classes");
				if(classesStr == null) classesStr = "";
				String classNames[] = classesStr.split(",");
				for(int k = 0 ; k < classNames.length ; k++){
					if(classNames[k].lastIndexOf(".")>0){
						if(type.equals(classNames[k].substring(classNames[k].lastIndexOf(".")+1))){
							if(importSB.toString().indexOf(classNames[k]) == -1)
								importSB.append("import "+classNames[k]+";"+ls);
						}
					}
				}
			}
		}
		
		BufferedReader reader = new BufferedReader(new StringReader(sb.toString()));
		String line = null;
		int lineCount = 0;
		while( (line = reader.readLine()) != null){
			String color = "#FFFFFF";
			if(lineCount % 2 != 0) color = "#FAFAFA";
			if(line.indexOf("......... 重复") == -1){
				String column[] = line.trim().split(" +");
				sb2.append(" * <tr bgcolor=\""+color+"\" align=\"center\">");
				sb2.append("<td>"+column[1]+"</td>");
				sb2.append("<td>"+column[2]+"</td>");
				sb2.append("<td>"+column[3]+"</td>");
				sb2.append("<td>"+column[4]+"</td>");
				sb2.append("</tr>"+ls);
			}else{
				sb2.append(" * <tr bgcolor=\""+color+"\" align=\"center\">");
				sb2.append("<td colspan=4>......... 重复</td>");
				sb2.append("</tr>"+ls);
			}
			lineCount++;
		}
		if(verification){
			sb2.append(" * <tr bgcolor=\"#FFFFFF\" align=\"center\">");
			sb2.append("<td>verification code</td>");
			sb2.append("<td>校验码</td>");
			sb2.append("<td>16个字节</td>");
			sb2.append("<td>发送包的校验码，16个字节固定长</td>");
			sb2.append("</tr>"+ls);
		}
		sb2.append(" * </table>"+ls);
		sb2.append(" */"+ls);
		//end getLength
		sb.setLength(0);
		sb.append(importSB);
		sb.append(ls);
		sb.append(ls);
		sb.append(sb2);
		
		
		
		String superclass = "RequestMessage";
		if(request == false)
			superclass = "ResponseMessage";
		sb.append("public class "+reqName+" implements "+superclass+"{"+ls);
		sb.append(ls);
		sb.append("\tstatic "+factory+" mf = "+factory+".getInstance();"+ls);
		if(verification){
			sb.append("\tstatic String verificationKeyword = \""+keyword+"\";"+ls);
			sb.append(ls);
		}
		sb.append(ls);
		sb.append("\tlong seqNum;"+ls);
		
		String params = "";
		
		if(this.headerElement != null){
			sb.append(""+ls);
			sb.append("\t//自定义头"+ls);
			Element fields[] = XmlUtil.getChildrenByName(headerElement,"property");
			for(int j = 0 ; j < fields.length ; j++){
				String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
				String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
				sb.append("\t"+fieldType+" "+fieldName+";"+ls);
				params += "," + fieldType + " " + fieldName;
			}
			sb.append(""+ls);
		}
		
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			sb.append("\t"+type+" "+name+";"+ls);
			params += "," + type + " " + name;
		}
		sb.append(ls);
		
		//加入一个默认构造函数
		sb.append("\tpublic "+reqName+"(){"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);

		sb.append("\tpublic "+reqName+"(long seqNum"+params+"){"+ls);
		sb.append("\t\tthis.seqNum = seqNum;"+ls);
		if(this.headerElement != null){
			Element fields[] = XmlUtil.getChildrenByName(headerElement,"property");
			for(int j = 0 ; j < fields.length ; j++){
				String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
				sb.append("\t\tthis."+fieldName+" = "+fieldName+";"+ls);
			}
		}
		for(int i = 0 ; i < pes.length ; i++){
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null);
			sb.append("\t\tthis."+name+" = "+name+";"+ls);
		}
		sb.append("\t}"+ls);
		sb.append(ls);
		
		params = "";
		
		if(this.headerElement != null){
			Element fields[] = XmlUtil.getChildrenByName(headerElement,"property");
			for(int j = 0 ; j < fields.length ; j++){
				String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
				String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
				
				params += "," + fieldType + " " + fieldName;
			}
		}
		
		sb.append("\tpublic "+reqName+"(long seqNum"+params+",byte[] content,int offset,int size) throws Exception{"+ls);
		sb.append("\t\tthis.seqNum = seqNum;"+ls);
		if(this.headerElement != null){
			Element fields[] = XmlUtil.getChildrenByName(headerElement,"property");
			for(int j = 0 ; j < fields.length ; j++){
				String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
				sb.append("\t\tthis."+fieldName+" = "+fieldName+";"+ls);
			}
		}
		
		if(verification){
			sb.append("\t\tbyte verification[] = new byte[16];"+ls);
			sb.append("\t\tSystem.arraycopy(content, offset+size-16, verification, 0, 16);"+ls);
			sb.append("\t\t"+md5Name+" md5 = new "+md5Name+"();"+ls);
			sb.append("\t\tmd5.update(content,offset,size-16);"+ls);
			sb.append("\t\tmd5.update(verificationKeyword.getBytes(),0,verificationKeyword.getBytes().length);"+ls);
			sb.append("\t\tif(!"+md5Name+".equals(verification,md5.digest())) throw new Exception(\"verification failed!\");"+ls);
		}
		boolean lenDefined = false;
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			if(pes[i].getNodeName().equals("object")){
				if(!lenDefined){
					sb.append("\t\tint len = 0;"+ls);
					lenDefined = true;
				}
				int maxNum = XmlUtil.getAttributeAsInteger(pes[i], "max-num",objectMaxLength);
				sb.append("\t\tlen = (int)mf.byteArrayToNumber(content,offset,4);"+ls);
				sb.append("\t\tif(len < 0 || len > "+maxNum+") throw new Exception(\"object length [\"+len+\"] big than the max length ["+maxNum+"]\");"+ls);
				sb.append("\t\toffset += 4;"+ls);
				sb.append("\t\t"+name+" = ("+type+")mf.byteArrayToObject(content,offset,len);"+ls);
				sb.append("\t\toffset += len;"+ls);
			}else if(pes[i].getNodeName().equals("property")){
				Class cl = getPrimitiveClass(type);
				if(cl.isArray()){
					if(!lenDefined){
						sb.append("\t\tint len = 0;"+ls);
						lenDefined = true;
					}
					sb.append("\t\tlen = (int)mf.byteArrayToNumber(content,offset,4);"+ls);
					sb.append("\t\toffset += 4;"+ls);
					
					int maxNum = XmlUtil.getAttributeAsInteger(pes[i], "max-num",arrayMaxLength);
					sb.append("\t\tif(len < 0 || len > "+maxNum+") throw new Exception(\"array length [\"+len+\"] big than the max length ["+maxNum+"]\");"+ls);
					sb.append("\t\t"+name+" = new "+getPrimitiveName(cl.getComponentType())+"[len];"+ls);
					
					if(cl.getComponentType() == Byte.TYPE){
						sb.append("\t\tSystem.arraycopy(content,offset,"+name+",0,len);"+ls);
						sb.append("\t\toffset += len;"+ls);
					}else{
						sb.append("\t\tfor(int i = 0 ; i < "+name+".length ; i++){"+ls);
						if(cl.getComponentType() == Character.TYPE 
								|| cl.getComponentType() == Short.TYPE
								|| cl.getComponentType() == Integer.TYPE
								|| cl.getComponentType() == Long.TYPE){
							sb.append("\t\t\t"+name+"[i] = ("+getPrimitiveName(cl.getComponentType())+")mf.byteArrayToNumber(content,offset,"+getPrimitiveSize(cl.getComponentType())+");"+ls);
							sb.append("\t\t\toffset += "+getPrimitiveSize(cl.getComponentType())+";"+ls);
						}else if(cl.getComponentType() == Boolean.TYPE){
							sb.append("\t\t\t"+name+"[i] = mf.byteArrayToNumber(content,offset,1) != 0;"+ls);
							sb.append("\t\t\toffset += 1;"+ls);
						}else if(cl == Float.TYPE){
							sb.append("\t\t\t"+name+"[i] = Float.intBitsToFloat(mf.byteArrayToNumber(content,offset,"+getPrimitiveSize(cl.getComponentType())+"));"+ls);
							sb.append("\t\t\toffset += "+getPrimitiveSize(cl.getComponentType())+";"+ls);
						}else if(cl.getComponentType() == String.class){
								sb.append("\t\t\tlen = (int)mf.byteArrayToNumber(content,offset,2);"+ls);
								sb.append("\t\t\toffset += 2;"+ls);
								sb.append("\t\t\tif(len < 0 || len > "+stringMaxLength+") throw new Exception(\"string length [\"+len+\"] big than the max length ["+stringMaxLength+"]\");"+ls);
								String encoding = XmlUtil.getAttributeAsString(pes[i], "encoding","",null);
								if(encoding.trim().length() > 0){
									sb.append("\t\t\t"+name+"[i] = new String(content,offset,len,\""+encoding+"\");"+ls);
								}else{
									sb.append("\t\t\t"+name+"[i] = new String(content,offset,len);"+ls);
								}
								sb.append("\t\toffset += len;"+ls);
						}
						sb.append("\t\t}"+ls);
					}
				}else{
					
					if(cl == Byte.TYPE || cl == Character.TYPE	|| cl == Short.TYPE	|| cl == Integer.TYPE || cl == Long.TYPE){
						int length = getPrimitiveSize(cl);
						sb.append("\t\t"+name+" = ("+getPrimitiveName(cl)+")mf.byteArrayToNumber(content,offset,"+length+");"+ls);
						sb.append("\t\toffset += "+length+";"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\t"+name+" = mf.byteArrayToNumber(content,offset,1) != 0;"+ls);
						sb.append("\t\toffset += 1;"+ls);
					}else if(cl == Float.TYPE ){
						int length = getPrimitiveSize(cl);
						sb.append("\t\t"+name+" = Float.intBitsToFloat(mf.byteArrayToNumber(content,offset,"+length+"));"+ls);
						sb.append("\t\toffset += "+length+";"+ls);						
					}else if(cl == String.class){
						String encoding = XmlUtil.getAttributeAsString(pes[i], "encoding","",null);
						int strLen = XmlUtil.getAttributeAsInteger(pes[i], "length",0);
						if(strLen <= 0){
							if(!lenDefined){
								sb.append("\t\tint len = 0;"+ls);
								lenDefined = true;
							}
							sb.append("\t\tlen = (int)mf.byteArrayToNumber(content,offset,2);"+ls);
							sb.append("\t\toffset += 2;"+ls);
							sb.append("\t\tif(len < 0 || len > "+stringMaxLength+") throw new Exception(\"string length [\"+len+\"] big than the max length ["+stringMaxLength+"]\");"+ls);
							if(encoding.trim().length()>0)
								sb.append("\t\t"+name+" = new String(content,offset,len,\""+encoding+"\");"+ls);
							else
								sb.append("\t\t"+name+" = new String(content,offset,len);"+ls);
							sb.append("\t\toffset += len;"+ls);
						}else{
							if(encoding.trim().length()>0)
								sb.append("\t\t"+name+" = new String(content,offset,"+strLen+",\""+encoding+"\").trim();"+ls);
							else
								sb.append("\t\t"+name+" = new String(content,offset,"+strLen+").trim();"+ls);
							sb.append("\t\toffset += "+strLen+";"+ls);
						}
					}
				}
			}else{ //object-property
				if(type.endsWith("[]")){
					type = type.substring(0, type.length()-2);
					if(!lenDefined){
						sb.append("\t\tint len = 0;"+ls);
						lenDefined = true;
					}
					sb.append("\t\tlen = (int)mf.byteArrayToNumber(content,offset,4);"+ls);
					sb.append("\t\toffset += 4;"+ls);
					
					int maxNum = XmlUtil.getAttributeAsInteger(pes[i], "max-num",objectMaxLength);
					sb.append("\t\tif(len < 0 || len > "+maxNum+") throw new Exception(\"object array length [\"+len+\"] big than the max length ["+maxNum+"]\");"+ls);
					sb.append("\t\t"+name+" = new "+type+"[len];"+ls);
					
					sb.append("\t\tfor(int i = 0 ; i < "+name+".length ; i++){"+ls);
					sb.append("\t\t\t"+name+"[i] = new "+type+"();"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String setter = "set" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							
							sb.append("\t\t\tlen = (int)mf.byteArrayToNumber(content,offset,4);"+ls);
							sb.append("\t\t\toffset += 4;"+ls);
							
							maxNum = XmlUtil.getAttributeAsInteger(fields[j], "max-num",arrayMaxLength);
							sb.append("\t\t\tif(len < 0 || len > "+maxNum+") throw new Exception(\"array length [\"+len+\"] big than the max length ["+maxNum+"]\");"+ls);
							fieldName = fieldName+"_"+randomIntegerString(); 
							sb.append("\t\t\t"+fieldType+" "+fieldName+" = new "+getPrimitiveName(cl.getComponentType())+"[len];"+ls);
							
							if(cl.getComponentType() == Byte.TYPE){
								sb.append("\t\t\tSystem.arraycopy(content,offset,"+fieldName+",0,len);"+ls);
								sb.append("\t\t\toffset += len;"+ls);
							}else{
								sb.append("\t\t\tfor(int j = 0 ; j < "+fieldName+".length ; j++){"+ls);
								if(cl.getComponentType() == Character.TYPE 
										|| cl.getComponentType() == Short.TYPE
										|| cl.getComponentType() == Integer.TYPE
										|| cl.getComponentType() == Long.TYPE){
									sb.append("\t\t\t\t"+fieldName+"[j] = ("+getPrimitiveName(cl.getComponentType())+")mf.byteArrayToNumber(content,offset,"+getPrimitiveSize(cl.getComponentType())+");"+ls);
									sb.append("\t\t\t\toffset += "+getPrimitiveSize(cl.getComponentType())+";"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\t\t\t"+fieldName+"[j] = mf.byteArrayToNumber(content,offset,1) != 0;"+ls);
									sb.append("\t\t\t\toffset += 1;"+ls);
								}else if(cl == Float.TYPE ){
									sb.append("\t\t\t\t"+fieldName+"[j] = Float.intBitsToFloat(mf.byteArrayToNumber(content,offset,"+getPrimitiveSize(cl.getComponentType())+"));"+ls);
									sb.append("\t\t\t\toffset += "+getPrimitiveSize(cl.getComponentType())+";"+ls);														
								}else if(cl.getComponentType() == String.class){
										sb.append("\t\t\t\tlen = (int)mf.byteArrayToNumber(content,offset,2);"+ls);
										sb.append("\t\t\t\toffset += 2;"+ls);
										sb.append("\t\t\t\tif(len < 0 || len > "+stringMaxLength+") throw new Exception(\"string length [\"+len+\"] big than the max length ["+stringMaxLength+"]\");"+ls);
										String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","",null);
										if(encoding.trim().length()>0)
											sb.append("\t\t\t\t"+fieldName+"[j] = new String(content,offset,len,\""+encoding+"\");"+ls);
										else
											sb.append("\t\t\t\t"+fieldName+"[j] = new String(content,offset,len);"+ls);
										sb.append("\t\t\t\toffset += len;"+ls);
								}
								sb.append("\t\t\t}"+ls);
							}
							sb.append("\t\t\t"+name+"[i]."+setter+"("+fieldName+");"+ls);
						}else{
							if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE || cl == Long.TYPE){
								int length = getPrimitiveSize(cl);
								sb.append("\t\t\t"+name+"[i]."+setter+"(("+getPrimitiveName(cl)+")mf.byteArrayToNumber(content,offset,"+length+"));"+ls);
								sb.append("\t\t\toffset += "+length+";"+ls);
							}else if(cl == Boolean.TYPE){
								sb.append("\t\t\t"+name+"[i]."+setter+"(mf.byteArrayToNumber(content,offset,1) != 0);"+ls);
								sb.append("\t\t\toffset += 1;"+ls);
							}else if(cl == Float.TYPE ){
								int length = getPrimitiveSize(cl);
								sb.append("\t\t\t"+name+"[i]."+setter+"(Float.intBitsToFloat(mf.byteArrayToNumber(content,offset,"+length+")));"+ls);
								sb.append("\t\t\toffset += "+length+";"+ls);
							}else if(cl == String.class){
								int strLen = XmlUtil.getAttributeAsInteger(fields[j], "length",0);
								if(strLen <= 0){
									if(!lenDefined){
										sb.append("\t\t\tint len = 0;"+ls);
										lenDefined = true;
									}
									sb.append("\t\t\tlen = (int)mf.byteArrayToNumber(content,offset,2);"+ls);
									sb.append("\t\t\toffset += 2;"+ls);
									sb.append("\t\t\tif(len < 0 || len > "+stringMaxLength+") throw new Exception(\"string length [\"+len+\"] big than the max length ["+stringMaxLength+"]\");"+ls);
									String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","",null);
									if(encoding.trim().length()>0)
										sb.append("\t\t\t"+name+"[i]."+setter+"(new String(content,offset,len,\""+encoding+"\"));"+ls);
									else
										sb.append("\t\t\t"+name+"[i]."+setter+"(new String(content,offset,len));"+ls);
									sb.append("\t\t\toffset += len;"+ls);
								}else{
									String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","",null);
									if(encoding.trim().length()>0)
										sb.append("\t\t\t"+name+"[i]."+setter+"(new String(content,offset,"+strLen+",\""+encoding+"\").trim());"+ls);
									else
										sb.append("\t\t\t"+name+"[i]."+setter+"(new String(content,offset,"+strLen+").trim());"+ls);
									sb.append("\t\t\toffset += "+strLen+";"+ls);
								}
							}
						}
					}
					sb.append("\t\t}"+ls);
					
				}else{
					sb.append("\t\t"+name+" = new "+type+"();"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String setter = "set" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							if(!lenDefined){
								sb.append("\t\tint len = 0;"+ls);
								lenDefined = true;
							}
							sb.append("\t\tlen = (int)mf.byteArrayToNumber(content,offset,4);"+ls);
							sb.append("\t\toffset += 4;"+ls);
							
							int maxNum = XmlUtil.getAttributeAsInteger(fields[j], "max-num",arrayMaxLength);
							sb.append("\t\tif(len < 0 || len > "+maxNum+") throw new Exception(\"array length [\"+len+\"] big than the max length ["+maxNum+"]\");"+ls);
							fieldName = fieldName+"_"+randomIntegerString();
							sb.append("\t\t"+fieldType+" "+fieldName+" = new "+getPrimitiveName(cl.getComponentType())+"[len];"+ls);
							
							if(cl.getComponentType() == Byte.TYPE){
								sb.append("\t\tSystem.arraycopy(content,offset,"+fieldName+",0,len);"+ls);
								sb.append("\t\toffset += len;"+ls);
							}else{
								sb.append("\t\tfor(int j = 0 ; j < "+fieldName+".length ; j++){"+ls);
								if(cl.getComponentType() == Character.TYPE 
										|| cl.getComponentType() == Short.TYPE
										|| cl.getComponentType() == Integer.TYPE
										|| cl.getComponentType() == Long.TYPE){
									sb.append("\t\t\t"+fieldName+"[j] = ("+getPrimitiveName(cl.getComponentType())+")mf.byteArrayToNumber(content,offset,"+getPrimitiveSize(cl.getComponentType())+");"+ls);
									sb.append("\t\t\toffset += "+getPrimitiveSize(cl.getComponentType())+";"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\t\t"+fieldName+"[j] = mf.byteArrayToNumber(content,offset,1) != 0;"+ls);
									sb.append("\t\t\toffset += 1;"+ls);
								}else if(cl == Float.TYPE ){
									sb.append("\t\t\t"+fieldName+"[j] = Float.intBitsToFloat(mf.byteArrayToNumber(content,offset,"+getPrimitiveSize(cl.getComponentType())+"));"+ls);
									sb.append("\t\t\toffset += "+getPrimitiveSize(cl.getComponentType())+";"+ls);									
								}else if(cl.getComponentType() == String.class){
										sb.append("\t\t\tlen = (int)mf.byteArrayToNumber(content,offset,2);"+ls);
										sb.append("\t\t\toffset += 2;"+ls);
										sb.append("\t\t\tif(len < 0 || len > "+stringMaxLength+") throw new Exception(\"string length [\"+len+\"] big than the max length ["+stringMaxLength+"]\");"+ls);
										String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","",null);
										if(encoding.trim().length() > 0)
											sb.append("\t\t\t"+fieldName+"[j] = new String(content,offset,len,\""+encoding+"\");"+ls);
										else
											sb.append("\t\t\t"+fieldName+"[j] = new String(content,offset,len);"+ls);
										sb.append("\t\t\t\toffset += len;"+ls);
								}
								sb.append("\t\t}"+ls);
							}
							sb.append("\t\t"+name+"."+setter+"("+fieldName+");"+ls);
						}else{
							if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE || cl == Long.TYPE){
								int length = getPrimitiveSize(cl);
								sb.append("\t\t"+name+"."+setter+"(("+getPrimitiveName(cl)+")mf.byteArrayToNumber(content,offset,"+length+"));"+ls);
								sb.append("\t\toffset += "+length+";"+ls);
							}else if(cl== Boolean.TYPE){
								sb.append("\t\t"+name+"."+setter+"(mf.byteArrayToNumber(content,offset,1) != 0);"+ls);
								sb.append("\t\toffset += 1;"+ls);
							}else if(cl == Float.TYPE ){								
								int length = getPrimitiveSize(cl);
								sb.append("\t\t"+name+"."+setter+"(Float.intBitsToFloat(mf.byteArrayToNumber(content,offset,"+length+")));"+ls);
								sb.append("\t\toffset += "+length+";"+ls);																
							}else if(cl == String.class){
								int strLen = XmlUtil.getAttributeAsInteger(fields[j], "length",0);
								if(strLen <= 0){
									if(!lenDefined){
										sb.append("\t\tint len = 0;"+ls);
										lenDefined = true;
									}
									sb.append("\t\tlen = (int)mf.byteArrayToNumber(content,offset,2);"+ls);
									sb.append("\t\toffset += 2;"+ls);
									sb.append("\t\tif(len < 0 || len > "+stringMaxLength+") throw new Exception(\"string length [\"+len+\"] big than the max length ["+stringMaxLength+"]\");"+ls);
									String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","",null);
									if(encoding.trim().length() > 0)
										sb.append("\t\t"+name+"."+setter+"(new String(content,offset,len,\""+encoding+"\"));"+ls);
									else
										sb.append("\t\t"+name+"."+setter+"(new String(content,offset,len));"+ls);
									sb.append("\t\toffset += len;"+ls);
								}else{
									String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","",null);
									if(encoding.trim().length() > 0)
										sb.append("\t\t"+name+"."+setter+"(new String(content,offset,"+strLen+",\""+encoding+"\").trim());"+ls);
									else
										sb.append("\t\t"+name+"."+setter+"(new String(content,offset,"+strLen+").trim());"+ls);
									sb.append("\t\toffset += "+strLen+";"+ls);
								}
							}
						}
					}
				}
			}
		}
		sb.append("\t}"+ls); //end constructor
		sb.append(ls);
		sb.append("\tpublic int getType() {"+ls);
		sb.append("\t\treturn "+reqType+";"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);		
		sb.append("\tpublic String getTypeDescription() {"+ls);
		sb.append("\t\treturn \""+reqName+"\";"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);		
		sb.append("\tpublic String getSequenceNumAsString() {"+ls);
		sb.append("\t\treturn String.valueOf(seqNum);"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);		
		sb.append("\tpublic long getSequnceNum(){"+ls);
		sb.append("\t\treturn seqNum;"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);		
	
		if(this.headerElement != null){
			Element fields[] = XmlUtil.getChildrenByName(headerElement,"property");
			for(int j = 0 ; j < fields.length ; j++){
				String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
				String getter = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
				String fieldComment = XmlUtil.getAttributeAsString(fields[j],"comment",null);
				sb.append("\t/**"+ls);
				sb.append("\t * 自定义头："+ ls);
				sb.append("\t *  "+ fieldComment + ls);
				sb.append("\t */"+ls);
				sb.append("\tpublic "+fieldType+" "+getter+"(){"+ls);
				sb.append("\t\treturn "+fieldName+";"+ls);
				sb.append("\t}"+ls);
				sb.append(ls);		
			}
		}
		
		sb.append("\tprivate int packet_length = 0;"+ls);
		sb.append(ls);
		
		//getLength()
		sb.append("\tpublic int getLength() {"+ls);
		sb.append("\t\tif(packet_length > 0) return packet_length;"+ls);
		sb.append("\t\tint len =  mf.getNumOfByteForMessageLength() + 4 + 4;"+ls);
		
		if(this.headerElement != null){
			sb.append("\t\t//自定义头"+ls);
			Element fields[] = XmlUtil.getChildrenByName(headerElement,"property");
			for(int j = 0 ; j < fields.length ; j++){
				String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
				String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
				Class cl = getPrimitiveClass(fieldType);
				if(cl.isArray()){
					throw new java.lang.UnsupportedOperationException("头信息中，不支持数组类型");
				}else{
					if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE ||cl == Float.TYPE || cl == Long.TYPE){
						int length = getPrimitiveSize(cl);
						sb.append("\t\tlen += "+ length + ";" +ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\tlen += 1;" +ls);
					}else if(cl == String.class){
						String encoding = XmlUtil.getAttributeAsString(fields[j], "encoding","",null);
						int strLen = XmlUtil.getAttributeAsInteger(fields[j], "length",0);
						if(strLen <= 0){
							sb.append("\t\tlen += 2;" +ls);
							if(encoding.trim().length()>0){
								sb.append("\t\ttry{"+ls);
								sb.append("\t\t\tlen += "+fieldName+".getBytes(\""+encoding+"\").length;"+ls);
								sb.append("\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
								sb.append("\t\t e.printStackTrace();"+ls);
								sb.append("\t\t\tthrow new RuntimeException(\"不支持的字符串编码["+encoding+"]\",e);"+ls);
								sb.append("\t\t}"+ls);
							}
							else
								sb.append("\t\tlen += "+fieldName+".getBytes().length;"+ls);
							
						}else{
							sb.append("\t\tlen += "+strLen+";" +ls);
						}
					}
				}
			}
		}
		
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			if(pes[i].getNodeName().equals("object")){
				sb.append("\t\tlen += 4;"+ls);
//				sb.append("\t\ttry{"+ls);
				sb.append("\t\t\tlen += mf.objectToByteArray("+name+").length;"+ls);
//				sb.append("\t\t}catch(Exception e){"+ls);
//				sb.append("\t\t e.printStackTrace();"+ls);
//				sb.append("\t\t\tthrow new RuntimeException(\"serialize object ["+type+" "+name+"] error:\" + e,e);"+ls);
//				sb.append("\t\t}"+ls);
			}else if(pes[i].getNodeName().equals("property")){
				Class cl = getPrimitiveClass(type);
				if(cl.isArray()){
					sb.append("\t\tlen += 4;"+ls);
					if(cl.getComponentType() == Byte.TYPE ){
						sb.append("\t\tlen += "+name+".length;"+ls);
					}else{
						if(cl.getComponentType() == Character.TYPE 
								|| cl.getComponentType() == Short.TYPE
								|| cl.getComponentType() == Integer.TYPE
								|| cl.getComponentType() == Float.TYPE
								|| cl.getComponentType() == Long.TYPE){
							sb.append("\t\tlen += "+name+".length * "+getPrimitiveSize(cl.getComponentType())+";"+ls);
						}else if(cl.getComponentType() == Boolean.TYPE){
							sb.append("\t\tlen += "+name+".length;"+ls);
						}else if(cl.getComponentType() == String.class){
							sb.append("\t\tfor(int i = 0 ; i < "+name+".length; i++){"+ls);
							sb.append("\t\t\tlen += 2;"+ls);
							String encoding = XmlUtil.getAttributeAsString(pes[i],"encoding","",null);
							if(encoding.trim().length() > 0){
								sb.append("\t\t\ttry{"+ls);
								sb.append("\t\t\t\tlen += "+name+"[i].getBytes(\""+encoding+"\").length;"+ls);
								sb.append("\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
								sb.append("\t\t e.printStackTrace();"+ls);
								sb.append("\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
								sb.append("\t\t\t}"+ls);
							}
							else{
								sb.append("\t\t\tlen += "+name+"[i].getBytes().length;"+ls);
							}
							sb.append("\t\t}"+ls);
						}
					}
				}else{
					
					if(cl == Byte.TYPE || cl == Character.TYPE	|| cl == Short.TYPE	|| cl == Integer.TYPE ||cl == Float.TYPE|| cl == Long.TYPE){
						sb.append("\t\tlen += "+getPrimitiveSize(cl)+";"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\tlen += 1;"+ls);
					}else if(cl == String.class){
						int length = XmlUtil.getAttributeAsInteger(pes[i],"length",0);
						if(length <= 0){
							sb.append("\t\tlen += 2;"+ls);
							String encoding = XmlUtil.getAttributeAsString(pes[i],"encoding","",null);
							if(encoding.trim().length() > 0){
								sb.append("\t\ttry{"+ls);
								sb.append("\t\t\tlen +="+name+".getBytes(\""+encoding+"\").length;"+ls);
								sb.append("\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
								sb.append("\t\t e.printStackTrace();"+ls);
								sb.append("\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
								sb.append("\t\t}"+ls);
							}
							else{
								sb.append("\t\tlen +="+name+".getBytes().length;"+ls);
							}
							
						}else{
							sb.append("\t\tlen += "+length+";"+ls);
						}
					}
				}
			}else{ //object-property
				if(type.endsWith("[]")){
					type = type.substring(0, type.length()-2);
					sb.append("\t\tlen += 4;"+ls);
					sb.append("\t\tfor(int i = 0 ; i < "+name+".length ; i++){"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String getter = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							sb.append("\t\t\tlen += 4;"+ls);
							if(cl.getComponentType() == Byte.TYPE){
								sb.append("\t\t\tlen += "+name+"[i]."+getter+"().length * "+getPrimitiveSize(cl.getComponentType())+";"+ls);
							}else{
								if(cl.getComponentType() == Character.TYPE 
										|| cl.getComponentType() == Short.TYPE
										|| cl.getComponentType() == Integer.TYPE
										|| cl.getComponentType() == Float.TYPE
										|| cl.getComponentType() == Long.TYPE){
									sb.append("\t\t\tlen += "+name+"[i]."+getter+"().length * "+getPrimitiveSize(cl.getComponentType())+";"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\t\tlen += "+name+"[i]."+getter+"().length * 1;"+ls);
								}else if(cl.getComponentType() == String.class){
									sb.append("\t\t\t"+fieldType+" "+fieldName+" = "+name+"[i]."+getter+"();"+ls);
									sb.append("\t\t\tfor(int j = 0 ; j < "+fieldName+".length; j++){"+ls);
									sb.append("\t\t\t\tlen += 2;"+ls);
									String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null);
									if(encoding.trim().length() > 0){
										sb.append("\t\t\t\ttry{"+ls);
										sb.append("\t\t\t\t\tlen += "+fieldName+"[j].getBytes(\""+encoding+"\").length;"+ls);
										sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t e.printStackTrace();"+ls);
										sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t\t}"+ls);
									}
									else{
										sb.append("\t\t\t\tlen += "+fieldName+"[j].getBytes().length;"+ls);
									}
									sb.append("\t\t\t}"+ls);
								}
							}
						}else{
							if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE ||cl == Float.TYPE || cl == Long.TYPE){
								sb.append("\t\t\tlen += "+getPrimitiveSize(cl)+";"+ls);
							}else if(cl == Boolean.TYPE){
								sb.append("\t\t\tlen += 1;"+ls);
							}else if(cl == String.class){
								int length = XmlUtil.getAttributeAsInteger(fields[j],"length",0);
								if(length <= 0){
									sb.append("\t\t\tlen += 2;"+ls);
									sb.append("\t\t\tif("+name+"[i]."+getter+"() != null){"+ls);
									String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null);
									if(encoding.trim().length() > 0){
										sb.append("\t\t\t\ttry{"+ls);
										sb.append("\t\t\t\tlen += "+name+"[i]."+getter+"().getBytes(\""+encoding+"\").length;"+ls);
										sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t e.printStackTrace();"+ls);
										
										sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t\t}"+ls);
									}
									else{
										sb.append("\t\t\t\tlen += "+name+"[i]."+getter+"().getBytes().length;"+ls);
									}
									
									sb.append("\t\t\t}"+ls);
								}else{
									sb.append("\t\t\tlen += "+length+";"+ls);
								}
							}
						}
					}
					sb.append("\t\t}"+ls);
					
				}else{
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String getter = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							sb.append("\t\tlen += 4;"+ls);
							if(cl.getComponentType() == Byte.TYPE){
								sb.append("\t\tlen += "+name+"."+getter+"().length * "+getPrimitiveSize(cl.getComponentType())+";"+ls);
							}else{
								if(cl.getComponentType() == Character.TYPE 
										|| cl.getComponentType() == Short.TYPE
										|| cl.getComponentType() == Integer.TYPE
										|| cl.getComponentType() == Float.TYPE
										|| cl.getComponentType() == Long.TYPE){
									sb.append("\t\tlen += "+name+"."+getter+"().length * "+getPrimitiveSize(cl.getComponentType())+";"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\tlen += "+name+"."+getter+"().length * 1;"+ls);
								}else if(cl.getComponentType() == String.class){
									sb.append("\t\t"+fieldType+" "+fieldName+" = "+name+"."+getter+"();"+ls);
									sb.append("\t\tfor(int j = 0 ; j < "+fieldName+".length; j++){"+ls);
									sb.append("\t\t\tlen += 2;"+ls);
									String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null);
									if(encoding.trim().length() > 0){
										sb.append("\t\t\ttry{"+ls);
										sb.append("\t\t\t\tlen += "+fieldName+"[j].getBytes(\""+encoding+"\").length;"+ls);
										sb.append("\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t e.printStackTrace();"+ls);
										sb.append("\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t}"+ls);
									}
									else{
										sb.append("\t\t\tlen += "+fieldName+"[j].getBytes().length;"+ls);
									}
									
									
									sb.append("\t\t}"+ls);
								}
							}
						}else{
							if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE ||cl == Float.TYPE || cl == Long.TYPE){
								sb.append("\t\tlen += "+getPrimitiveSize(cl)+";"+ls);
							}else if(cl == Boolean.TYPE){
								sb.append("\t\tlen += 1;"+ls);
							}else if(cl == String.class){
								int length = XmlUtil.getAttributeAsInteger(fields[j],"length",0);
								if(length <= 0){
									sb.append("\t\tlen += 2;"+ls);
									sb.append("\t\tif("+name+"."+getter+"() != null){"+ls);
									String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null);
									if(encoding.trim().length() > 0){
										sb.append("\t\t\ttry{"+ls);
										sb.append("\t\t\tlen += "+name+"."+getter+"().getBytes(\""+encoding+"\").length;"+ls);
										sb.append("\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t e.printStackTrace();"+ls);
										sb.append("\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t}"+ls);
									}
									else{
										sb.append("\t\t\tlen += "+name+"."+getter+"().getBytes().length;"+ls);
									}
									
									sb.append("\t\t}"+ls);

								}else{
									sb.append("\t\tlen += "+length+";"+ls);
								}
							}
						}
					}
				}
			}
		}
		if(verification){
			sb.append("\t\tlen += 16;"+ls);
		}
		sb.append("\t\tpacket_length = len;"+ls);
		sb.append("\t\treturn len;"+ls);
		sb.append("\t}"+ls);
		//end getLength
		sb.append(ls);
		
		boolean tmpBytesDefined1 = false;
		boolean tmpBytesDefined2 = false;
		
		//writeTo
		sb.append("\tpublic int writeTo(ByteBuffer buffer) {"+ls);
		sb.append("\t\tint messageLength = getLength();"+ls);
		sb.append("\t\tif(buffer.remaining() < messageLength) return 0;"+ls);
		sb.append("\t\tint oldPos = buffer.position();"+ls);
		sb.append("\t\tbuffer.mark();"+ls);
		sb.append("\t\ttry{"+ls);
		sb.append("\t\t\tbuffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));"+ls);
		sb.append("\t\t\tbuffer.putInt(getType());"+ls);
		sb.append("\t\t\tbuffer.putInt((int)seqNum);"+ls);
		
		if(this.headerElement != null){
			sb.append("\t\t\t//自定义头"+ls);
			Element fields[] = XmlUtil.getChildrenByName(headerElement,"property");
			for(int j = 0 ; j < fields.length ; j++){
				String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
				String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
				Class cl = getPrimitiveClass(fieldType);
				if(cl.isArray()){
					throw new java.lang.UnsupportedOperationException("头信息中，不支持数组类型");
				}else{
					
					
					if(cl == Byte.TYPE){
						sb.append("\t\t\tbuffer.put("+fieldName+");"+ls);
					}else if(cl == Character.TYPE){
						sb.append("\t\t\tbuffer.putChar("+fieldName+");"+ls);
					}else if(cl == Short.TYPE){
						sb.append("\t\t\tbuffer.putShort("+fieldName+");"+ls);
					}else if(cl == Integer.TYPE){
						sb.append("\t\t\tbuffer.putInt("+fieldName+");"+ls);
					}else if(cl == Float.TYPE){
						sb.append("\t\t\tbuffer.putFloat("+fieldName+");"+ls);
					}else if( cl == Long.TYPE){
						sb.append("\t\t\tbuffer.putLong("+fieldName+");"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\t\tbuffer.put((byte)("+fieldName+"==false?0:1));"+ls);
					}else if(cl == String.class){
						String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null).trim();
						if(tmpBytesDefined1){
							if(encoding.trim().length() > 0){
								sb.append("\t\t\t\ttry{"+ls);
								sb.append("\t\t\ttmpBytes1 = "+fieldName+".getBytes(\""+encoding+"\");"+ls);							
								sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
								sb.append("\t\t\t e.printStackTrace();"+ls);
								sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
								sb.append("\t\t\t\t}"+ls);
								
//								sb.append("\t\t\ttmpBytes1 = "+fieldName+".getBytes(\""+encoding+"\");"+ls);
							}else
								sb.append("\t\t\ttmpBytes1 = "+fieldName+".getBytes();"+ls);
						}else{
							sb.append("\t\t\tbyte[] tmpBytes1;"+ls);
							
							if(encoding.trim().length() > 0){
								sb.append("\t\t\t\ttry{"+ls);
								sb.append("\t\t\ttmpBytes1 = "+fieldName+".getBytes(\""+encoding+"\");"+ls);					
								sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
								sb.append("\t\t\t e.printStackTrace();"+ls);
								sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
								sb.append("\t\t\t\t}"+ls);
								
//								sb.append("\t\t\tbyte[] tmpBytes1 = "+fieldName+".getBytes(\""+encoding+"\");"+ls);
							}else
								sb.append("\t\t\ttmpBytes1 = "+fieldName+".getBytes();"+ls);
							tmpBytesDefined1 = true;
						}
						int length = XmlUtil.getAttributeAsInteger(fields[j],"length",0);
						if(length <= 0){
							sb.append("\t\t\tbuffer.putShort((short)tmpBytes1.length);"+ls);
							sb.append("\t\t\tbuffer.put(tmpBytes1);"+ls);
						}else{
							sb.append("\t\t\tif(tmpBytes1.length >= "+length+"){"+ls);
							sb.append("\t\t\t\tbuffer.put(tmpBytes1,0,"+length+");"+ls);
							sb.append("\t\t\t}else{"+ls);
							sb.append("\t\t\t\tbuffer.put(tmpBytes1);"+ls);
							sb.append("\t\t\t\tbuffer.put(new byte["+length+"-tmpBytes1.length]);"+ls);
							sb.append("\t\t\t}"+ls);
						}
					}
				}
			}
		}
		
		if(verification){
			sb.append("\t\t\tint position = buffer.position();"+ls);
		}
		sb.append(ls);	
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			if(pes[i].getNodeName().equals("object")){
				sb.append("\t\t\ttry{"+ls);
				sb.append("\t\t\t\tbyte[] bytes = mf.objectToByteArray("+name+");"+ls);
				sb.append("\t\t\t\tbuffer.putInt(bytes.length);"+ls);
				sb.append("\t\t\t\tbuffer.put(bytes);"+ls);
				sb.append("\t\t\t}catch(Exception e){"+ls);
				sb.append("\t\t\t\tthrow e;"+ls);
				sb.append("\t\t\t}"+ls);
			}else if(pes[i].getNodeName().equals("property")){
				Class cl = getPrimitiveClass(type);
				if(cl.isArray()){
					sb.append("\t\t\tbuffer.putInt("+name+".length);"+ls);
					if(cl.getComponentType() == Byte.TYPE ){
						sb.append("\t\t\tbuffer.put("+name+");"+ls);
					}else{
						tmpBytesDefined2 = false;
						sb.append("\t\t\tfor(int i = 0 ; i < "+name+".length; i++){"+ls);
						if(cl.getComponentType() == Character.TYPE){
							sb.append("\t\t\t\tbuffer.putChar("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Short.TYPE){
							sb.append("\t\t\t\tbuffer.putShort("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Integer.TYPE){
							sb.append("\t\t\t\tbuffer.putInt("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Float.TYPE){
							sb.append("\t\t\t\tbuffer.putFloat("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Long.TYPE){
							sb.append("\t\t\t\tbuffer.putLong("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Boolean.TYPE){
							sb.append("\t\t\t\tbuffer.put((byte)("+name+"[i]==false?0:1));"+ls);
						}else if(cl.getComponentType() == String.class){
							String encoding = XmlUtil.getAttributeAsString(pes[i],"encoding","",null).trim();
							if(tmpBytesDefined2){
								if(encoding.trim().length() > 0){
									
									sb.append("\t\t\t\ttry{"+ls);
									sb.append("\t\t\t\ttmpBytes2 = "+name+"[i].getBytes(\""+encoding+"\");"+ls);				
									sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
									sb.append("\t\t\t e.printStackTrace();"+ls);
									sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
									sb.append("\t\t\t\t}"+ls);
									
//									sb.append("\t\t\t\ttmpBytes2 = "+name+"[i].getBytes(\""+encoding+"\");"+ls);
								}else{
									sb.append("\t\t\t\ttmpBytes2 = "+name+"[i].getBytes();"+ls);
								}
							}else{
								sb.append("\t\t\t\tbyte[] tmpBytes2 ;"+ls);
								if(encoding.trim().length() > 0){
									sb.append("\t\t\t\ttry{"+ls);
									sb.append("\t\t\t\ttmpBytes2 = "+name+"[i].getBytes(\""+encoding+"\");"+ls);				
									sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
									sb.append("\t\t\t e.printStackTrace();"+ls);
									sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
									sb.append("\t\t\t\t}"+ls);
									
//									sb.append("\t\t\t\tbyte[] tmpBytes2 = "+name+"[i].getBytes(\""+encoding+"\");"+ls);
								}else{
									sb.append("\t\t\t\ttmpBytes2 = "+name+"[i].getBytes();"+ls);
								}
								tmpBytesDefined2 = true;
							}
							sb.append("\t\t\t\tbuffer.putShort((short)tmpBytes2.length);"+ls);
							sb.append("\t\t\t\tbuffer.put(tmpBytes2);"+ls);
						}
						sb.append("\t\t\t}"+ls);
					}
				}else{
					
					if(cl == Byte.TYPE){
						sb.append("\t\t\tbuffer.put("+name+");"+ls);
					}else if(cl == Character.TYPE){
						sb.append("\t\t\tbuffer.putChar("+name+");"+ls);
					}else if(cl == Short.TYPE){
						sb.append("\t\t\tbuffer.putShort("+name+");"+ls);
					}else if(cl == Integer.TYPE){
						sb.append("\t\t\tbuffer.putInt("+name+");"+ls);
					}else if(cl == Float.TYPE){
						sb.append("\t\t\tbuffer.putFloat("+name+");"+ls);
					}else if( cl == Long.TYPE){
						sb.append("\t\t\tbuffer.putLong("+name+");"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\t\tbuffer.put((byte)("+name+"==false?0:1));"+ls);
					}else if(cl == String.class){
						String encoding = XmlUtil.getAttributeAsString(pes[i],"encoding","",null).trim();
						if(tmpBytesDefined1){
							if(encoding.trim().length() > 0){
								sb.append("\t\t\t\ttry{"+ls);
								sb.append("\t\t\ttmpBytes1 = "+name+".getBytes(\""+encoding+"\");"+ls);			
								sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
								sb.append("\t\t\t e.printStackTrace();"+ls);
								sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
								sb.append("\t\t\t\t}"+ls);
								
//								sb.append("\t\t\ttmpBytes1 = "+name+".getBytes(\""+encoding+"\");"+ls);
							}else
								sb.append("\t\t\ttmpBytes1 = "+name+".getBytes();"+ls);
						}else{
							sb.append("\t\t\tbyte[] tmpBytes1;"+ls);
							if(encoding.trim().length() > 0){
								sb.append("\t\t\t\ttry{"+ls);
								sb.append("\t\t\t tmpBytes1 = "+name+".getBytes(\""+encoding+"\");"+ls);		
								sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
								sb.append("\t\t\t e.printStackTrace();"+ls);
								sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
								sb.append("\t\t\t\t}"+ls);
							
//								sb.append("\t\t\tbyte[] tmpBytes1 = "+name+".getBytes(\""+encoding+"\");"+ls);
							}else
								sb.append("\t\t\ttmpBytes1 = "+name+".getBytes();"+ls);
							tmpBytesDefined1 = true;
						}
						int length = XmlUtil.getAttributeAsInteger(pes[i],"length",0);
						if(length <= 0){
							sb.append("\t\t\tbuffer.putShort((short)tmpBytes1.length);"+ls);
							sb.append("\t\t\tbuffer.put(tmpBytes1);"+ls);
						}else{
							sb.append("\t\t\tif(tmpBytes1.length >= "+length+"){"+ls);
							sb.append("\t\t\t\tbuffer.put(tmpBytes1,0,"+length+");"+ls);
							sb.append("\t\t\t}else{"+ls);
							sb.append("\t\t\t\tbuffer.put(tmpBytes1);"+ls);
							sb.append("\t\t\t\tbuffer.put(new byte["+length+"-tmpBytes1.length]);"+ls);
							sb.append("\t\t\t}"+ls);
						}
					}
				}
			}else{ //object-property
				if(type.endsWith("[]")){
					type = type.substring(0, type.length()-2);
					sb.append("\t\t\tbuffer.putInt("+name+".length);"+ls);
					tmpBytesDefined2 = false;
					sb.append("\t\t\tfor(int i = 0 ; i < "+name+".length ; i++){"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String getter = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							sb.append("\t\t\t\tbuffer.putInt("+name+"[i]."+getter+"().length);"+ls);
							
							if(cl.getComponentType() == Byte.TYPE){
								sb.append("\t\t\t\tbuffer.put("+name+"[i]."+getter+"());"+ls);
							}else{
								fieldName = fieldName+"_"+randomIntegerString();
								sb.append("\t\t\t\t"+fieldType+" "+fieldName+" = "+name+"[i]."+getter+"();"+ls);
								sb.append("\t\t\t\tfor(int j = 0 ; j < "+fieldName+".length ; j++){"+ls);
								if(cl.getComponentType() == Character.TYPE){
									sb.append("\t\t\t\t\tbuffer.putChar("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Short.TYPE){
									sb.append("\t\t\t\t\tbuffer.putShort("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Integer.TYPE){
									sb.append("\t\t\t\t\tbuffer.putInt("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Float.TYPE){
									sb.append("\t\t\t\t\tbuffer.putFloat("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Long.TYPE){
									sb.append("\t\t\t\t\tbuffer.putLong("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\t\t\t\tbuffer.put((byte)("+fieldName+"[j]?1:0));"+ls);
								}else if(cl.getComponentType() == String.class){
									String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null).trim();
									if(encoding.length() > 0){
										
										sb.append("\t\t\t\ttry{"+ls);
										sb.append("\t\t\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes(\""+encoding+"\").length);"+ls);
										sb.append("\t\t\t\t\tbuffer.put("+fieldName+"[j].getBytes(\""+encoding+"\"));"+ls);										
										sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t\t e.printStackTrace();"+ls);
										sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t\t}"+ls);
										
//										sb.append("\t\t\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes(\""+encoding+"\").length);"+ls);
//										sb.append("\t\t\t\t\tbuffer.put("+fieldName+"[j].getBytes(\""+encoding+"\"));"+ls);
									}else{
										sb.append("\t\t\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes().length);"+ls);
										sb.append("\t\t\t\t\tbuffer.put("+fieldName+"[j].getBytes());"+ls);
									}
								}
								sb.append("\t\t\t\t}"+ls);
							}
							
						}else{
							if(cl == Byte.TYPE){
								sb.append("\t\t\t\tbuffer.put((byte)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\t\t\tbuffer.putChar((char)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\t\t\tbuffer.putShort((short)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\t\t\tbuffer.putInt((int)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\t\t\tbuffer.putFloat((int)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\t\t\tbuffer.putLong("+name+"[i]."+getter+"());"+ls);
							}else if(cl == Boolean.TYPE){
								String booleanGetter = "is" +  Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
								sb.append("\t\t\t\tbuffer.put((byte)("+name+"[i]."+booleanGetter+"()==false?0:1));"+ls);
							}else if(cl == String.class){
								String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null).trim();
								if(tmpBytesDefined2){
									if(encoding.length() > 0){
										
										sb.append("\t\t\t\ttry{"+ls);
										sb.append("\t\t\t\ttmpBytes2 = "+name+"[i]."+getter+"().getBytes(\""+encoding+"\");"+ls);
										sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t\t e.printStackTrace();"+ls);
										sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t\t}"+ls);
									
									
//										sb.append("\t\t\t\ttmpBytes2 = "+name+"[i]."+getter+"().getBytes(\""+encoding+"\");"+ls);
									}else
										sb.append("\t\t\t\ttmpBytes2 = "+name+"[i]."+getter+"().getBytes();"+ls);
								}else{
									sb.append("\t\t\t\tbyte[] tmpBytes2 ;"+ls);
									if(encoding.length() > 0){
										sb.append("\t\t\t\ttry{"+ls);
										sb.append("\t\t\t\ttmpBytes2 = "+name+"[i]."+getter+"().getBytes(\""+encoding+"\");"+ls);
										sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t\t e.printStackTrace();"+ls);
										sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t\t}"+ls);
									
//										sb.append("\t\t\t\tbyte[] tmpBytes2 = "+name+"[i]."+getter+"().getBytes(\""+encoding+"\");"+ls);
									}else
										sb.append("\t\t\t\ttmpBytes2 = "+name+"[i]."+getter+"().getBytes();"+ls);
									tmpBytesDefined2 = true;
								}
								int length = XmlUtil.getAttributeAsInteger(fields[j],"length",0);
								if(length <= 0){
									sb.append("\t\t\t\tbuffer.putShort((short)tmpBytes2.length);"+ls);
									sb.append("\t\t\t\tbuffer.put(tmpBytes2);"+ls);
								}else{
									sb.append("\t\t\t\tif(tmpBytes2.length >= "+length+"){"+ls);
									sb.append("\t\t\t\t\tbuffer.put(tmpBytes2,0,"+length+");"+ls);
									sb.append("\t\t\t\t}else{"+ls);
									sb.append("\t\t\t\t\tbuffer.put(tmpBytes2);"+ls);
									sb.append("\t\t\t\t\tbuffer.put(new byte["+length+"-tmpBytes2.length]);"+ls);
									sb.append("\t\t\t\t}"+ls);
								}
							}
						}
					}
					sb.append("\t\t\t}"+ls);
					
				}else{
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String getter = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							sb.append("\t\t\tbuffer.putInt("+name+"."+getter+"().length);"+ls);
							
							if(cl.getComponentType() == Byte.TYPE){
								sb.append("\t\t\tbuffer.put("+name+"."+getter+"());"+ls);
							}else{
								fieldName = fieldName+"_"+randomIntegerString();
								sb.append("\t\t\t"+fieldType+" "+fieldName+" = "+name+"."+getter+"();"+ls);
								sb.append("\t\t\tfor(int j = 0 ; j < "+fieldName+".length ; j++){"+ls);
								if(cl.getComponentType() == Character.TYPE){
									sb.append("\t\t\t\tbuffer.putChar("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Short.TYPE){
									sb.append("\t\t\t\tbuffer.putShort("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Integer.TYPE){
									sb.append("\t\t\t\tbuffer.putInt("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Float.TYPE){
									sb.append("\t\t\t\tbuffer.putFloat("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Long.TYPE){
									sb.append("\t\t\t\tbuffer.putLong("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\t\t\tbuffer.put((byte)("+fieldName+"[j]?1:0));"+ls);
								}else if(cl.getComponentType() == String.class){
									String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null).trim();
									if(encoding.length() > 0){
										sb.append("\t\t\t\ttry{"+ls);
										sb.append("\t\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes(\""+encoding+"\").length);"+ls);
										sb.append("\t\t\t\tbuffer.put("+fieldName+"[j].getBytes(\""+encoding+"\"));"+ls);								
										sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t\t e.printStackTrace();"+ls);
										sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t\t}"+ls);
										
//										sb.append("\t\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes(\""+encoding+"\").length);"+ls);
//										sb.append("\t\t\t\tbuffer.put("+fieldName+"[j].getBytes(\""+encoding+"\"));"+ls);
									}else{
										sb.append("\t\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes().length);"+ls);
										sb.append("\t\t\t\tbuffer.put("+fieldName+"[j].getBytes());"+ls);
									}
								}
								sb.append("\t\t\t}"+ls);
							}
						}else{
							if(cl == Byte.TYPE){
								sb.append("\t\t\tbuffer.put((byte)"+name+"."+getter+"());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\t\tbuffer.putChar((char)"+name+"."+getter+"());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\t\tbuffer.putShort((short)"+name+"."+getter+"());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\t\tbuffer.putInt((int)"+name+"."+getter+"());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\t\tbuffer.putFloat((int)"+name+"."+getter+"());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\t\tbuffer.putLong("+name+"."+getter+"());"+ls);
							}else if(cl == Boolean.TYPE){
								String booleanGetter = "is" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
								sb.append("\t\t\tbuffer.put((byte)("+name+"."+booleanGetter+"()==false?0:1));"+ls);
							}else if(cl == String.class){
								String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null).trim();
								if(tmpBytesDefined1){
									if(encoding.length() > 0){
										sb.append("\t\t\t\ttry{"+ls);
										sb.append("\t\t\t\ttmpBytes1 = "+name+"."+getter+"().getBytes(\""+encoding+"\");"+ls);
										sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t\t e.printStackTrace();"+ls);
										sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t\t}"+ls);
										
//										sb.append("\t\t\ttmpBytes1 = "+name+"."+getter+"().getBytes(\""+encoding+"\");"+ls);
									}else
										sb.append("\t\t\ttmpBytes1 = "+name+"."+getter+"().getBytes();"+ls);
								}else{
									sb.append("\t\t\tbyte[] tmpBytes1 ;"+ls);
									if(encoding.length() > 0){
										sb.append("\t\t\t\ttry{"+ls);
										sb.append("\t\t\t\ttmpBytes1 = "+name+"."+getter+"().getBytes(\""+encoding+"\");"+ls);
										sb.append("\t\t\t\t}catch(java.io.UnsupportedEncodingException e){"+ls);
										sb.append("\t\t\t e.printStackTrace();"+ls);
										sb.append("\t\t\t\t\tthrow new RuntimeException(\"unsupported encoding ["+encoding+"]\",e);"+ls);
										sb.append("\t\t\t\t}"+ls);
										
//										sb.append("\t\t\tbyte[] tmpBytes1 = "+name+"."+getter+"().getBytes(\""+encoding+"\");"+ls);
									}else
										sb.append("\t\t\ttmpBytes1 = "+name+"."+getter+"().getBytes();"+ls);
									tmpBytesDefined1 = true;
								}
								int length = XmlUtil.getAttributeAsInteger(fields[j],"length",0);
								if(length <= 0){
									sb.append("\t\t\tbuffer.putShort((short)tmpBytes1.length);"+ls);
									sb.append("\t\t\tbuffer.put(tmpBytes1);"+ls);
								}else{
									sb.append("\t\t\tif(tmpBytes1.length >= "+length+"){"+ls);
									sb.append("\t\t\t\tbuffer.put(tmpBytes1,0,"+length+");"+ls);
									sb.append("\t\t\t}else{"+ls);
									sb.append("\t\t\t\tbuffer.put(tmpBytes1);"+ls);
									sb.append("\t\t\t\tbuffer.put(new byte["+length+"-tmpBytes1.length]);"+ls);
									sb.append("\t\t\t}"+ls);
								}
							}
						}
					}
				}
			}
		}
		if(verification){
			if(tmpBytesDefined1){
				sb.append("\t\t\ttmpBytes1 = new byte[buffer.position() - position];"+ls);
			}else{
				sb.append("\t\t\tbyte[] tmpBytes1 = new byte[buffer.position() - position];"+ls);
			}
			sb.append("\t\t\tbuffer.position(position);"+ls);
			sb.append("\t\t\tbuffer.get(tmpBytes1);"+ls);
			sb.append("\t\t\t"+md5Name+" md5 = new "+md5Name+"();"+ls);
			sb.append("\t\t\tmd5.update(tmpBytes1,0,tmpBytes1.length);"+ls);
			sb.append("\t\t\tmd5.update(verificationKeyword.getBytes(),0,verificationKeyword.getBytes().length);"+ls);
			sb.append("\t\t\tbuffer.put(md5.digest());"+ls);
		}
		sb.append("\t\t}catch(Exception e){"+ls);
		sb.append("\t\t e.printStackTrace();"+ls);
		sb.append("\t\t\tbuffer.reset();"+ls);
		sb.append("\t\t\tthrow new RuntimeException(\"in writeTo method catch exception :\",e);"+ls);
		sb.append("\t\t}"+ls);
		sb.append("\t\tint newPos = buffer.position();"+ls);
		sb.append("\t\tbuffer.position(oldPos);"+ls);
		sb.append("\t\tbuffer.put(mf.numberToByteArray(newPos-oldPos,mf.getNumOfByteForMessageLength()));"+ls);
		sb.append("\t\tbuffer.position(newPos);"+ls);
		sb.append("\t\treturn newPos-oldPos;"+ls);
		sb.append("\t}"+ls);
		//end wirteTo
		sb.append(ls);
		
		//setter and getter
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			comment = XmlUtil.getAttributeAsString(pes[i],"comment","无帮助说明",null);
			
			String Name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
			sb.append("\t/**"+ls);
			sb.append("\t * 获取属性："+ls);
			sb.append("\t *	"+comment+""+ls);
			sb.append("\t */"+ls);
			sb.append("\tpublic "+type+" get"+Name+"(){"+ls);
			sb.append("\t\treturn "+name+";"+ls);
			sb.append("\t}"+ls);
			sb.append(ls);
			sb.append("\t/**"+ls);
			sb.append("\t * 设置属性："+ls);
			sb.append("\t *	"+comment+""+ls);
			sb.append("\t */"+ls);
			sb.append("\tpublic void set"+Name+"("+type+" "+name+"){"+ls);
			sb.append("\t\tthis."+name+" = "+name+";"+ls);
			sb.append("\t}"+ls);
			sb.append(ls);		
		}	

		
		sb.append("}");	
		return sb;
	}
	
	
	
	protected void generatePackageAndImports(StringBuffer sb){
		String packageName = env.get("package");
		if(packageName == null) packageName = "";
		String ls = System.getProperty("line.separator");
		String imports = env.get("imports");
		if(imports == null) imports = "com.xuanzhi.tools.transport.*";
		String importNames[] = imports.split(";");
		
		sb.append("package " + packageName + ";" + ls);
		sb.append(ls);
		for(int i = 0 ; i < importNames.length; i++){
			if(importNames[i].trim().length() > 0)
			sb.append("import " + importNames[i]+";"+ls);
		}
		sb.append(ls);
		
	}
	
	public static Element[] getPropertyElements(Element e) {
      NodeList nl = e.getChildNodes();
      int max = nl.getLength();
      LinkedList<Node> list = new LinkedList<Node>();
      for ( int i = 0; i < max; i++ ) {
         Node n = nl.item( i );
         if ( n.getNodeType() == Node.ELEMENT_NODE &&
               (n.getNodeName().equals("property") || n.getNodeName().equals("object-property") ||
            		   n.getNodeName().equals("object") )   ) {
            list.add( n );
         }
      }
      return ( Element[] )list.toArray( new Element[list.size()] );
    }
	
	public final static String [] primitiveName = {"boolean","char","byte","short","int","long","float","double","void","String"};
	
	public final static int [] primitiveSize = {1,2,1,2,4,8,4,8,1,-1};
	
	private static String [] primitiveArrayName = {"boolean[]", "char[]", "byte[]", "short[]", "int[]", "long[]", "float[]", "double[]", "void[]","String[]"};	

	private static Class [] primitiveClass = {Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE,String.class};
	
	private static Class [] primitiveArrayClass = {boolean[].class, char[].class, byte[].class,short[].class, int[].class, long[].class,float[].class,double[].class,void.class,String[].class};
	
	/**
	 * get the class for given primitive name
	 * @param s,the primitive class name such as int,char,byte and so on.
	 * @return the Class instance of the given primitive name.such as <code>Integer.TYPE</code>,<code>Character.TYPE</code>,<code>Byte.TYPE</code> and so on.
	 * @exception  if the given name is not a primitive name then throw ClassNotFoundException.
	 */
	public static Class getPrimitiveClass(String s) throws ClassNotFoundException 
	{
		for(int i = 0 ; i < primitiveName.length ; i ++){
			if(primitiveName[i].equals(s)) return  primitiveClass[i];
		}
		for(int i = 0 ; i < primitiveArrayName.length ; i ++){
			if(primitiveArrayName[i].equals(s)) return  primitiveArrayClass[i];
		}
		throw new ClassNotFoundException("getPrimitiveClass: class ["+s+"] is not a primitive class."); 
	}
	
	public static String getPrimitiveName(Class s){
		for(int i = 0 ; i < primitiveClass.length ; i ++){
			if(primitiveClass[i].equals(s)) return  primitiveName[i];
		}
		for(int i = 0 ; i < primitiveArrayClass.length ; i ++){
			if(primitiveArrayClass[i].equals(s)) return  primitiveArrayName[i];
		}
		return s.getSimpleName();
	}
	
	public static int getPrimitiveSize(Class cl) throws ClassNotFoundException {
		for(int i = 0 ; i < primitiveClass.length ; i ++){
			if(primitiveClass[i].equals(cl)) return  primitiveSize[i];
		}
		throw new ClassNotFoundException("getPrimitiveClass: class ["+cl+"] is not a primitive class.");
		
	}

}
