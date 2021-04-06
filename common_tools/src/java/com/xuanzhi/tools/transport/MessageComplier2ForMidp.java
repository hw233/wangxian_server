package com.xuanzhi.tools.transport;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import com.xuanzhi.tools.text.XmlUtil;

/**
 * 此实现生成的代码是为了MIDP服务的，
 * 所以生成的代码非常的节俭。
 * 
 */

public class MessageComplier2ForMidp {

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
				files.add( args[i+1]);
				i++;
			}else if(args[i].equals("-encoding") && i< args.length-1 ){
				encoding = args[i+1];
				i++;
			}
			else if(args[i].equals("-h")){
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
		MessageComplier2ForMidp mc = new MessageComplier2ForMidp(env);
		ArrayList<Document> doms = new ArrayList<Document>();
		String factoryName="DefaultMessageFactory";
		String handlerName="ClientMessageHandler";
		int maxMessageSize=4096;
		int headerLen=12;
		int singlefunctionMaxLength = Integer.MAX_VALUE;
		for( String file:files){
			Document dom;
			if( encoding!= null){
				dom = XmlUtil.load(file,encoding);
			}else{
				dom = XmlUtil.load(file);			
			}
			doms.add(dom);
			
			
			Element ele = XmlUtil.getChildByName(dom.getDocumentElement(), "factory");
			factoryName = XmlUtil.getAttributeAsString(ele, "name", "DefaultMessageFactory",null);
			maxMessageSize = XmlUtil.getAttributeAsInteger(ele, "max-send-packet-size", 4096);
			headerLen = XmlUtil.getAttributeAsInteger(ele, "headerLen", 12);
			int singlefunctionLength = XmlUtil.getAttributeAsInteger(ele, "singlefunctionMaxLength", 0);
			if(singlefunctionLength> 0 ){
				if( singlefunctionMaxLength> singlefunctionLength){
					singlefunctionMaxLength = singlefunctionLength;
				}
			}
			
			
			Element ele2 = XmlUtil.getChildByName(dom.getDocumentElement(), "handler");
			handlerName = XmlUtil.getAttributeAsString(ele2, "name", "ClientMessageHandler",null);
			
		}
		ArrayList<Element> protos = new ArrayList<Element>();
		for( Document dom:doms){
			Element protoTypeEles[] = XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype");
			for( Element e: protoTypeEles){
				protos.add(e);
			}
		}
		if( doms.size()> 0){
			mc.complie(protos.toArray(new Element[0]),factoryName,handlerName,maxMessageSize,headerLen,singlefunctionMaxLength);
		}
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
		"offset","seqNum","content","size","protocol_message","buffer"
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
	public MessageComplier2ForMidp(HashMap<String,String> env){
		this.env = env;
	}
	
	/**
	 * 根据配置文件生成Java Class
	 * @param dom
	 * @throws Exception
	 */
	public void complie(Element protoTypeEles[],String factoryName,String handlerName,
			int maxMessageSize,int headerLen,int singlefunctionMaxLength) throws Exception{
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
			if(resName != null && checkMap.containsKey(resType)){
				errorMessage.append("\tERROR: more than one message using " + XmlUtil.getAttributeAsString(e, "res_type",null) + "\n");
			}
			if(reqName != null)
				checkMap.put(reqType, reqName);
			if(resName != null)
				checkMap.put(resType, resName);
		}
		
		if(errorMessage.length() > 0){
			throw new Exception("complie error:\n" + errorMessage.toString());
		}
		
//		Element ele = XmlUtil.getChildByName(dom.getDocumentElement(), "factory");
//		String factoryName = XmlUtil.getAttributeAsString(ele, "name", "DefaultMessageFactory",null);
//		Element ele2 = XmlUtil.getChildByName(dom.getDocumentElement(), "handler");
//		String handlerName = XmlUtil.getAttributeAsString(ele2, "name", "ClientMessageHandler",null);
		
		LinkedHashMap<String,StringBuffer> javaClassMap = new LinkedHashMap<String,StringBuffer>(); 
		StringBuffer sb = generateFactory(protoTypeEles,factoryName,handlerName,maxMessageSize,headerLen,singlefunctionMaxLength);
		javaClassMap.put(factoryName,sb);
		
		sb = generateMessageHandler(protoTypeEles,handlerName);
		javaClassMap.put(handlerName,sb);
		
		
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
	
	protected StringBuffer generateMessageHandler(Element protoTypeEles[],String handlerName) throws Exception{
		String ls = System.getProperty("line.separator");
		StringBuffer sb = new StringBuffer();
//		Element ele2 = XmlUtil.getChildByName(dom.getDocumentElement(), "handler");
//		String handlerName = XmlUtil.getAttributeAsString(ele2, "name", "ClientMessageHandler",null);
		
		
		
//		Element protoTypeEles[] = XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype");
		
		HashSet<String> referenceTypeSet = new HashSet<String>();
		
		sb.append("public abstract class " + handlerName + "{"+ls);
		for(int i = 0 ; i < protoTypeEles.length ; i++){
			Element e = protoTypeEles[i];
			String reqName = XmlUtil.getAttributeAsString(e, "req_name",null,null);
			String resName = XmlUtil.getAttributeAsString(e, "res_name",null,null);

			boolean reqClientReceive = XmlUtil.getAttributeAsBoolean(e, "req_client_receive",false);
			boolean resClientReceive = XmlUtil.getAttributeAsBoolean(e, "res_client_receive",false);
			boolean resClientSend = XmlUtil.getAttributeAsBoolean(e, "res_client_send",false);
			
			if(reqName != null && reqClientReceive){
				String params = "";
				if(resClientSend){
					params = "int seqNum,";
				}
				Element pes[] = getPropertyElements(XmlUtil.getChildByName(e, "req"));
				sb.append("\t/**"+ls);
				sb.append("\t * "+ XmlUtil.getAttributeAsString(e, "comment","",null)+ls);
				for(int j = 0 ; j < pes.length ; j++){
					String type = XmlUtil.getAttributeAsString(pes[j],"type",null).trim();
					String name = XmlUtil.getAttributeAsString(pes[j],"name",null).trim();
					params += type+" " +name+",";
					sb.append("\t * @param "+name+" "+XmlUtil.getAttributeAsString(pes[j],"comment","",null)+""+ls);
					
					if(pes[j].getNodeName().equals("object-property")){
						if(type.trim().endsWith("[]")){
							type = type.substring(0,type.length()-2);
						}
						referenceTypeSet.add(type);
					}
				}
				sb.append("\t */"+ls);
				if(params.endsWith(",")){
					params = params.substring(0, params.length() -1 );
				}
				sb.append("\tpublic void handle_"+reqName+"("+params+"){"+ls);
				sb.append("\t\tSystem.out.println(\"警告：客户端没有实现协议"+reqName+"处理函数\");"+ls);
				sb.append("\t}"+ls);
				sb.append(""+ls);
			}
			
			if(resName != null && resClientReceive){
				Element pes[] = getPropertyElements(XmlUtil.getChildByName(e, "res"));
				String params = "";
				sb.append("\t/**"+ls);
				sb.append("\t * "+ XmlUtil.getAttributeAsString(e, "comment","",null)+ls);
				for(int j = 0 ; j < pes.length ; j++){
					String type = XmlUtil.getAttributeAsString(pes[j],"type",null).trim();
					String name = XmlUtil.getAttributeAsString(pes[j],"name",null).trim();
					params += type+" " +name+",";
					sb.append("\t * @param "+name+" "+XmlUtil.getAttributeAsString(pes[j],"comment","",null)+""+ls);
					
					if(pes[j].getNodeName().equals("object-property")){
						if(type.trim().endsWith("[]")){
							type = type.substring(0,type.length()-2);
						}
						referenceTypeSet.add(type);
					}
				}
				sb.append("\t */"+ls);
				if(params.endsWith(",")){
					params = params.substring(0, params.length() -1 );
				}
				sb.append("\tpublic void handle_"+resName+"("+params+"){"+ls);
				sb.append("\t\tSystem.out.println(\"警告：客户端没有实现协议"+resName+"处理函数\");"+ls);
				sb.append("\t}"+ls);
				sb.append(""+ls);
			}
		}
		sb.append("}"+ls);
		
		
		StringBuffer buffer = new StringBuffer();
		generatePackageAndImports(buffer);
		buffer.append(ls);
		
		String classesStr = env.get("classes");
		if(classesStr == null) classesStr = "";
		String classNames[] = classesStr.split(",");
		for(int k = 0 ; k < classNames.length ; k++){
			if(classNames[k].lastIndexOf(".")>0){
				String type = classNames[k].substring(classNames[k].lastIndexOf(".")+1);
				if(referenceTypeSet.contains(type)){
					buffer.append("import " + classNames[k]+";"+ls);
				}
			}
		}
		buffer.append(ls);
		buffer.append(sb.toString());
		
		return buffer;
		
	}
	
	/**
	 * <factory name="GameMessageFactory"/>
	 * 
	 * @param ele
	 * @return
	 */
	protected StringBuffer generateFactory(Element protoTypeEles[],String factoryName,String handlerName,int maxMessageSize,int headerLen,int singlefunctionMaxLength)  throws Exception{
		String ls = System.getProperty("line.separator");
		
		StringBuffer sb = new StringBuffer();
//		Element ele = XmlUtil.getChildByName(dom.getDocumentElement(), "factory");
//		String factoryName = XmlUtil.getAttributeAsString(ele, "name", "DefaultMessageFactory",null);
//		int maxMessageSize = XmlUtil.getAttributeAsInteger(ele, "max-send-packet-size", 4096);
		
//		Element ele2 = XmlUtil.getChildByName(dom.getDocumentElement(), "handler");
//		String handlerName = XmlUtil.getAttributeAsString(ele2, "name", "ClientMessageHandler",null);
		
		
		
//		int headerLen = XmlUtil.getAttributeAsInteger(ele, "headerLen", 12);
		//generatePackageAndImports(sb);
		
		HashSet<String> referenceTypeSet = new HashSet<String>();
		
		sb.append(ls);
		
//		Element protoTypeEles[] = XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype");
		
		sb.append("public class " + factoryName);
		sb.append(" {" + ls);
		sb.append(ls);
		sb.append("\tprivate static int sequnceNum = 0;"+ls);
		sb.append(ls);

		sb.append("\tpublic static int getNumOfByteForMessageLength() {"+ls);
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
		sb.append("\tpublic static long byteArrayToNumber(byte[] bytes, int offset,int numOfBytes) {"+ls);
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
		sb.append("\tpublic static byte[] numberToByteArray(long n, int numOfBytes) {"+ls);
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
		sb.append("\tpublic static byte[] numberToByteArray(int n, int numOfBytes) {"+ls);
		sb.append("\t\tbyte bytes[] = new byte[numOfBytes];"+ls);
		sb.append("\t\tfor(int i = 0 ; i < numOfBytes ; i++)"+ls);
		sb.append("\t\t\tbytes[i] = (byte)((n >> (8*(numOfBytes - 1 - i))) & 0xFF);"+ls);
		sb.append("\t\treturn bytes;"+ls);
		sb.append("\t}"+ls);
		sb.append(ls);

		sb.append("\t/**"+ls);
		sb.append("\t * 处理消息方法，所有的请求和响应消息都应该调用此方法"+ls);
		sb.append("\t * 第二个参数为实现处理接口的类实例"+ls);
		sb.append("\t */"+ls);
		
		int handleCount = 0;		
		int startProto = 0;
		int lastProto = -1;
		while(startProto< protoTypeEles.length){
			handleCount++;
			int lastsblength = sb.length();
			System.out.println("private static boolean handleMessage"+handleCount);
			sb.append("\tprivate static boolean handleMessage"+handleCount+"(DataInputStream di,Message protocol_message,"+handlerName+" handler) throws Exception{"+ls);
			sb.append("\t\tint seqNum = protocol_message.seqNum;"+ls);
			sb.append("\t\tswitch(protocol_message.type){"+ls);
			
			for(; startProto < protoTypeEles.length ; startProto++){
				if( startProto == lastProto) throw new Exception("handleMessage error :------");
				if( lastProto>= 0 && lastProto+1 != startProto) throw new Exception("handleMessage error 222:------");
				lastProto = startProto;
				Element e = protoTypeEles[startProto];
				String reqName = XmlUtil.getAttributeAsString(e, "req_name",null,null);
				String resName = XmlUtil.getAttributeAsString(e, "res_name",null,null);
				String reqType = XmlUtil.getAttributeAsString(e, "req_type","0",null);
				String resType = XmlUtil.getAttributeAsString(e, "res_type","0",null);
				boolean reqClientReceive = XmlUtil.getAttributeAsBoolean(e, "req_client_receive",false);
				boolean resClientReceive = XmlUtil.getAttributeAsBoolean(e, "res_client_receive",false);

				
				if(reqName != null && reqClientReceive){
					sb.append("\t\t\tcase "+reqType+":{"+ls);
					generateHandleMessageCode(referenceTypeSet,sb,e,true);
					sb.append("\t\t\t}"+ls);
				}
				
				if(resName != null && resClientReceive){
					sb.append("\t\t\tcase "+resType+":{"+ls);
					generateHandleMessageCode(referenceTypeSet,sb,e,false);
					sb.append("\t\t\t}"+ls);
				}
				//防止单个函数过大
				if( sb.length()-lastsblength> singlefunctionMaxLength){
					startProto++;
					break;					
				}
			}
			sb.append("\t\t\tdefault:{"+ls);
//			sb.append("\t\t\t\tSystem.out.println(\"协议错误：不能识别的消息类型：\"+protocol_message.type+\"\");"+ls);
			sb.append("\t\t\treturn false;"+ls);
			sb.append("\t\t\t}"+ls);			
			sb.append("\t\t}"+ls);
			sb.append("\t\treturn true;"+ls);
			sb.append("\t}"+ls);
		}
		
		sb.append("\tpublic static void handleMessage(Message protocol_message,"+handlerName+" handler) throws Exception{"+ls);
		sb.append("\t\tDataInputStream di = new DataInputStream(new ByteArrayInputStream(protocol_message.data,"+headerLen+",protocol_message.data.length-"+headerLen+"));"+ls);
		
		for( int i=1;i<=handleCount;i++){
			sb.append("\t\tif(handleMessage"+i+"(di,protocol_message,handler)){"+ls);
			sb.append("\t\t\treturn;"+ls);
			sb.append("\t\t}"+ls);
		}
		sb.append("\t\tSystem.out.println(\"协议错误：不能识别的消息类型：\"+protocol_message.type+\"\");"+ls);
		
		
		sb.append("\t}"+ls);
		//end handleMessage
		
		sb.append(""+ls);
		//sb.append("\tprivate static ByteBuffer buffer = ByteBuffer.allocate("+maxMessageSize+");"+ls);
		sb.append("\tprivate static ByteArrayOutputStream output = new ByteArrayOutputStream("+maxMessageSize+");"+ls);
		
		sb.append(""+ls);
		
		for(int i = 0 ; i < protoTypeEles.length ; i++){
			Element e = protoTypeEles[i];
			String reqName = XmlUtil.getAttributeAsString(e, "req_name",null,null);
			String resName = XmlUtil.getAttributeAsString(e, "res_name",null,null);

			boolean reqClientSend = XmlUtil.getAttributeAsBoolean(e, "req_client_send",false);
			boolean resClientSend = XmlUtil.getAttributeAsBoolean(e, "res_client_send",false);
			
			if(reqName != null && reqClientSend){
				Element pes[] = getPropertyElements(XmlUtil.getChildByName(e, "req"));
				String params = "";
				for(int j = 0 ; j < pes.length ; j++){
					String type = XmlUtil.getAttributeAsString(pes[j],"type",null).trim();
					String name = XmlUtil.getAttributeAsString(pes[j],"name",null).trim();
					params += type+" " +name+",";
				}
				if(params.endsWith(",")){
					params = params.substring(0, params.length() -1 );
				}
				
				sb.append("\t/**"+ls);
				sb.append("\t *"+XmlUtil.getAttributeAsString(e, "comment","",null)+ls);
				for(int j = 0 ; j < pes.length ; j++){
					String name = XmlUtil.getAttributeAsString(pes[j],"name",null).trim();
					sb.append("\t *@param "+name+" "+XmlUtil.getAttributeAsString(pes[j],"comment","",null)+ls);
				}
				sb.append("\t */"+ls);
				
				sb.append("\tpublic synchronized static Message construct_"+reqName+"("+params+"){"+ls);
				generateConstructMessageCode(referenceTypeSet,sb,e,true);
				sb.append("\t}"+ls);
				sb.append(""+ls);
			}
			
			if(resName != null && resClientSend){
				Element pes[] = getPropertyElements(XmlUtil.getChildByName(e, "res"));
				String params = ",";
				for(int j = 0 ; j < pes.length ; j++){
					String type = XmlUtil.getAttributeAsString(pes[j],"type",null).trim();
					String name = XmlUtil.getAttributeAsString(pes[j],"name",null).trim();
					params += type+" " +name+",";
				}
				if(params.endsWith(",")){
					params = params.substring(0, params.length() -1 );
				}
				
				sb.append("\t/**"+ls);
				sb.append("\t *"+XmlUtil.getAttributeAsString(e, "comment","",null)+ls);
				for(int j = 0 ; j < pes.length ; j++){
					String name = XmlUtil.getAttributeAsString(pes[j],"name",null).trim();
					sb.append("\t *@param "+name+" "+XmlUtil.getAttributeAsString(pes[j],"comment","",null)+ls);
				}
				sb.append("\t */"+ls);
				
				sb.append("\tpublic synchronized static Message construct_"+resName+"(int seqNum"+params+"){"+ls);
				generateConstructMessageCode(referenceTypeSet,sb,e,false);
				sb.append("\t}"+ls);
			}
		}
	
		sb.append("}"+ls);
		
		StringBuffer buffer = new StringBuffer();
		generatePackageAndImports(buffer);
		buffer.append("import java.io.ByteArrayOutputStream;"+ls);
		buffer.append("import java.io.ByteArrayInputStream;"+ls);
		buffer.append("import java.io.DataOutputStream;"+ls);
		buffer.append("import java.io.DataInputStream;"+ls);
		buffer.append(ls);
		
		String classesStr = env.get("classes");
		if(classesStr == null) classesStr = "";
		String classNames[] = classesStr.split(",");
		for(int k = 0 ; k < classNames.length ; k++){
			if(classNames[k].lastIndexOf(".")>0){
				String type = classNames[k].substring(classNames[k].lastIndexOf(".")+1).trim();
				if(referenceTypeSet.contains(type)){
					buffer.append("import " + classNames[k]+";"+ls);
				}
			}
		}
		buffer.append(ls);
		buffer.append(sb.toString());
		
		return buffer;

	}
	
	protected void generateConstructMessageCode(HashSet<String> referenceTypeSet,StringBuffer sb,Element e,boolean request) throws Exception{
		
		String ls = System.getProperty("line.separator");
		boolean tmpBytesDefined1 = false;
		boolean tmpBytesDefined2 = false;
		
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
		
		String reqType = null;
		if(request)
			reqType = XmlUtil.getAttributeAsString(e, "req_type",null,null);
		else
			reqType = XmlUtil.getAttributeAsString(e, "res_type",null,null);

		
		sb.append("\t\t"+ls);
		sb.append("\t\tMessage "+reqName+" = new Message();"+ls);
		//sb.append("\t\tbuffer.clear();"+ls);
		sb.append("\t\ttry{"+ls);
		sb.append("\t\toutput.reset();"+ls);
		sb.append("\t\tDataOutputStream out = new DataOutputStream(output);"+ls);
		
		//sb.append("\t\tbuffer.putInt(0);"+ls);
		//sb.append("\t\tbuffer.putInt("+reqType+");"+ls);
		sb.append("\t\tout.writeInt(0);"+ls);
		sb.append("\t\tout.writeInt("+reqType+");"+ls);
		if(request){
			sb.append("\t\t"+reqName+".seqNum = sequnceNum++;"+ls);
		}else{
			sb.append("\t\t"+reqName+".seqNum = seqNum;"+ls);
		}
		//sb.append("\t\tbuffer.putInt("+reqName+".seqNum);"+ls);
		sb.append("\t\tout.writeInt("+reqName+".seqNum);"+ls);
		
		sb.append(ls);	
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			if(pes[i].getNodeName().equals("property")){
				Class cl = getPrimitiveClass(type);
				if(cl.isArray()){
					sb.append("\t\tout.writeInt("+name+".length);"+ls);
					if(cl.getComponentType() == Byte.TYPE ){
						sb.append("\t\tout.write("+name+");"+ls);
					}else{
						tmpBytesDefined2 = false;
						sb.append("\t\tfor(int i = 0 ; i < "+name+".length; i++){"+ls);
						if(cl.getComponentType() == Character.TYPE){
							sb.append("\t\t\tout.writeChar("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Short.TYPE){
							sb.append("\t\t\tout.writeShort("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Integer.TYPE){
							sb.append("\t\t\tout.writeInt("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Float.TYPE){
							sb.append("\t\t\tout.writeFloat("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Long.TYPE){
							sb.append("\t\t\tout.writeLong("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == Boolean.TYPE){
							sb.append("\t\t\tout.writeBoolean("+name+"[i]);"+ls);
						}else if(cl.getComponentType() == String.class){
							
//							String encoding = XmlUtil.getAttributeAsString(pes[i],"encoding","").trim();
//							if(tmpBytesDefined2){
//								if(encoding.trim().length() > 0){
//									sb.append("\t\t\ttry{"+ls);
//									sb.append("\t\t\t\ttmpBytes2 = "+name+"[i].getBytes(\""+encoding+"\");"+ls);
//									sb.append("\t\t\t}catch(Exception e){ e.printStackTrace();}"+ls);
//								}else{
//									sb.append("\t\t\ttmpBytes2 = "+name+"[i].getBytes();"+ls);
//								}
//							}else{
//								if(encoding.trim().length() > 0){
//									sb.append("\t\t\tbyte[] tmpBytes2 = null;"+ls);
//									sb.append("\t\t\ttry{"+ls);
//									sb.append("\t\t\t\ttmpBytes2 = "+name+"[i].getBytes(\""+encoding+"\");"+ls);
//									sb.append("\t\t\t}catch(Exception e){ e.printStackTrace();}"+ls);
//								}else{
//									sb.append("\t\t\tbyte[] tmpBytes2 = "+name+"[i].getBytes();"+ls);
//								}
//								tmpBytesDefined2 = true;
//							}
//							sb.append("\t\t\tbuffer.putShort((short)tmpBytes2.length);"+ls);
//							sb.append("\t\t\tbuffer.put(tmpBytes2);"+ls);
							sb.append("\t\t\tout.writeUTF("+name+"[i]);"+ls);
						}
						sb.append("\t\t}"+ls);
					}
				}else{
					
					if(cl == Byte.TYPE){
						sb.append("\t\tout.writeByte("+name+");"+ls);
					}else if(cl == Character.TYPE){
						sb.append("\t\tout.writeChar("+name+");"+ls);
					}else if(cl == Short.TYPE){
						sb.append("\t\tout.writeShort("+name+");"+ls);
					}else if(cl == Integer.TYPE){
						sb.append("\t\tout.writeInt("+name+");"+ls);
					}else if(cl == Float.TYPE){
						sb.append("\t\tout.writeFloat("+name+");"+ls);
					}else if( cl == Long.TYPE){
						sb.append("\t\tout.writeLong("+name+");"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\tout.writeBoolean("+name+");"+ls);
					}else if(cl == String.class){
						String encoding = XmlUtil.getAttributeAsString(pes[i],"encoding","",null).trim();
						
						int length = XmlUtil.getAttributeAsInteger(pes[i],"length",0);
						if(length <= 0){
//							sb.append("\t\tbuffer.putShort((short)tmpBytes1.length);"+ls);
//							sb.append("\t\tbuffer.put(tmpBytes1);"+ls);
							sb.append("\t\tout.writeUTF("+name+");"+ls);
						}else{
							if(tmpBytesDefined1){
								if(encoding.trim().length() > 0){
									sb.append("\t\ttry{"+ls);
									sb.append("\t\t\ttmpBytes1 = "+name+".getBytes(\""+encoding+"\");"+ls);
									sb.append("\t\t}catch(Exception e){ e.printStackTrace();}"+ls);
								}
								else
									sb.append("\t\ttmpBytes1 = "+name+".getBytes();"+ls);
							}else{
								if(encoding.trim().length() > 0){
									sb.append("\t\tbyte[] tmpBytes1 = null;"+ls);
									sb.append("\t\ttry{"+ls);
									sb.append("\t\t\ttmpBytes1 = "+name+".getBytes(\""+encoding+"\");"+ls);
									sb.append("\t\t}catch(Exception e){ e.printStackTrace();}"+ls);
								}
								else
									sb.append("\t\tbyte[] tmpBytes1 = "+name+".getBytes();"+ls);
								tmpBytesDefined1 = true;
							}
							sb.append("\t\tif(tmpBytes1.length >= "+length+"){"+ls);
							sb.append("\t\t\tout.write(tmpBytes1,0,"+length+");"+ls);
							sb.append("\t\t}else{"+ls);
							sb.append("\t\t\tout.write(tmpBytes1);"+ls);
							sb.append("\t\t\tout.write(new byte["+length+"-tmpBytes1.length]);"+ls);
							sb.append("\t\t}"+ls);
						}
					}
				}
			}else{ //object-property
				if(type.endsWith("[]")){
					type = type.substring(0, type.length()-2);
					
					referenceTypeSet.add(type);
					
					sb.append("\t\tout.writeInt("+name+".length);"+ls);
					tmpBytesDefined2 = false;
					sb.append("\t\tfor(int i = 0 ; i < "+name+".length ; i++){"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String getter = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							sb.append("\t\t\tout.writeInt("+name+"[i]."+getter+"().length);"+ls);
							
							if(cl.getComponentType() == Byte.TYPE){
								sb.append("\t\t\tout.write("+name+"[i]."+getter+"());"+ls);
							}else{
								fieldName = fieldName+"_"+randomIntegerString();
								sb.append("\t\t\t"+fieldType+" "+fieldName+" = "+name+"[i]."+getter+"();"+ls);
								sb.append("\t\t\tfor(int j = 0 ; j < "+fieldName+".length ; j++){"+ls);
								if(cl.getComponentType() == Character.TYPE){
									sb.append("\t\t\t\tout.writeChar("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Short.TYPE){
									sb.append("\t\t\t\tout.writeShort("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Integer.TYPE){
									sb.append("\t\t\t\tout.writeInt("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Float.TYPE){
									sb.append("\t\t\t\tout.writeFloat("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Long.TYPE){
									sb.append("\t\t\t\tout.writeLong("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\t\t\tout.writeBoolean("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == String.class){
//									String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","").trim();
//									if(encoding.length() > 0){
//										sb.append("\t\t\t\ttry{"+ls);
//										sb.append("\t\t\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes(\""+encoding+"\").length);"+ls);
//										sb.append("\t\t\t\t\tbuffer.put("+fieldName+"[j].getBytes(\""+encoding+"\"));"+ls);
//										sb.append("\t\t\t\t}catch(Exception e){e.printStackTrace();}"+ls);
//									}else{
//										sb.append("\t\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes().length);"+ls);
//										sb.append("\t\t\t\tbuffer.put("+fieldName+"[j].getBytes());"+ls);
//									}
									sb.append("\t\t\t\tout.writeUTF("+fieldName+"[j]);"+ls);
								}
								sb.append("\t\t\t}"+ls);
							}
							
						}else{
							if(cl == Byte.TYPE){
								sb.append("\t\t\tout.writeByte((byte)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\t\tout.writeChar((char)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\t\tout.writeShort((short)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\t\tout.writeInt((int)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\t\tout.writeFloat((float)"+name+"[i]."+getter+"());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\t\tout.writeLong("+name+"[i]."+getter+"());"+ls);
							}else if(cl == Boolean.TYPE){
								String booleanGetter = "is" +  Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
								sb.append("\t\t\tout.writeBoolean("+name+"[i]."+booleanGetter+"());"+ls);
							}else if(cl == String.class){
								String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null).trim();
								
								int length = XmlUtil.getAttributeAsInteger(fields[j],"length",0);
								if(length <= 0){
									//sb.append("\t\t\tbuffer.putShort((short)tmpBytes2.length);"+ls);
									//sb.append("\t\t\tbuffer.put(tmpBytes2);"+ls);
									sb.append("\t\t\tout.writeUTF("+name+"[i]."+getter+"());"+ls);
								}else{
									if(tmpBytesDefined2){
										if(encoding.length() > 0){
											sb.append("\t\t\ttry{"+ls);
											sb.append("\t\t\t\ttmpBytes2 = "+name+"[i]."+getter+"().getBytes(\""+encoding+"\");"+ls);
											sb.append("\t\t\t}catch(Exception e){e.printStackTrace();}"+ls);
										}
										else
											sb.append("\t\t\ttmpBytes2 = "+name+"[i]."+getter+"().getBytes();"+ls);
									}else{
										if(encoding.length() > 0){
											sb.append("\t\t\tbyte[] tmpBytes2 = null;"+ls);
											sb.append("\t\t\ttry{"+ls);
											sb.append("\t\t\t\ttmpBytes2 = "+name+"[i]."+getter+"().getBytes(\""+encoding+"\");"+ls);
											sb.append("\t\t\t}catch(Exception e){e.printStackTrace();}"+ls);
										}
										else
											sb.append("\t\t\tbyte[] tmpBytes2 = "+name+"[i]."+getter+"().getBytes();"+ls);
										tmpBytesDefined2 = true;
									}
									
									sb.append("\t\t\tif(tmpBytes2.length >= "+length+"){"+ls);
									sb.append("\t\t\t\tout.write(tmpBytes2,0,"+length+");"+ls);
									sb.append("\t\t\t}else{"+ls);
									sb.append("\t\t\t\tout.write(tmpBytes2);"+ls);
									sb.append("\t\t\t\tout.write(new byte["+length+"-tmpBytes2.length]);"+ls);
									sb.append("\t\t\t}"+ls);
								}
							}
						}
					}
					sb.append("\t\t}"+ls);
					
				}else{
					
					referenceTypeSet.add(type);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String getter = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							sb.append("\t\tout.writeInt("+name+"."+getter+"().length);"+ls);
							
							if(cl.getComponentType() == Byte.TYPE){
								sb.append("\t\tout.write("+name+"."+getter+"());"+ls);
							}else{
								fieldName = fieldName+"_"+randomIntegerString();
								sb.append("\t\t"+fieldType+" "+fieldName+" = "+name+"."+getter+"();"+ls);
								sb.append("\t\tfor(int j = 0 ; j < "+fieldName+".length ; j++){"+ls);
								if(cl.getComponentType() == Character.TYPE){
									sb.append("\t\t\tout.writeChar("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Short.TYPE){
									sb.append("\t\t\tout.writeShort("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Integer.TYPE){
									sb.append("\t\t\tout.writeInt("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Float.TYPE){
									sb.append("\t\t\tout.writeFloat("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Long.TYPE){
									sb.append("\t\t\tout.writeLong("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\t\tout.writeBoolean("+fieldName+"[j]);"+ls);
								}else if(cl.getComponentType() == String.class){
//									String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","").trim();
//									if(encoding.length() > 0){
//										sb.append("\t\t\ttry{"+ls);
//										sb.append("\t\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes(\""+encoding+"\").length);"+ls);
//										sb.append("\t\t\t\tbuffer.put("+fieldName+"[j].getBytes(\""+encoding+"\"));"+ls);
//										sb.append("\t\t\t}catch(Exception e){e.printStackTrace();}"+ls);
//									}else{
//										sb.append("\t\t\tbuffer.putShort((short)"+fieldName+"[j].getBytes().length);"+ls);
//										sb.append("\t\t\tbuffer.put("+fieldName+"[j].getBytes());"+ls);
//									}
									sb.append("\t\t\tout.writeUTF("+fieldName+"[j]);"+ls);
								}
								sb.append("\t\t}"+ls);
							}
						}else{
							if(cl == Byte.TYPE){
								sb.append("\t\tout.writeByte((byte)"+name+"."+getter+"());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\tout.writeChar((char)"+name+"."+getter+"());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\tout.writeShort((short)"+name+"."+getter+"());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\tout.writeInt((int)"+name+"."+getter+"());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\tout.writeFloat((float)"+name+"."+getter+"());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\tout.writeLong("+name+"."+getter+"());"+ls);
							}else if(cl == Boolean.TYPE){
								String booleanGetter = "is" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
								sb.append("\t\tout.writeBoolean("+name+"."+booleanGetter+"());"+ls);
							}else if(cl == String.class){
								String encoding = XmlUtil.getAttributeAsString(fields[j],"encoding","",null).trim();
								
								int length = XmlUtil.getAttributeAsInteger(fields[j],"length",0);
								if(length <= 0){
									//sb.append("\t\tbuffer.putShort((short)tmpBytes1.length);"+ls);
									//sb.append("\t\tbuffer.put(tmpBytes1);"+ls);
									sb.append("\t\tout.writeUTF("+name+"."+getter+"());"+ls);
									
								}else{
									if(tmpBytesDefined1){
										if(encoding.length() > 0){
											sb.append("\t\ttry{"+ls);
											sb.append("\t\t\ttmpBytes1 = "+name+"."+getter+"().getBytes(\""+encoding+"\");"+ls);
											sb.append("\t\t}catch(Exception e){e.printStackTrace();}"+ls);
										}
										else
											sb.append("\t\ttmpBytes1 = "+name+"."+getter+"().getBytes();"+ls);
									}else{
										if(encoding.length() > 0){
											sb.append("\t\tbyte[] tmpBytes1 = null;"+ls);
											sb.append("\t\ttry{"+ls);
											sb.append("\t\t\ttmpBytes1 = "+name+"."+getter+"().getBytes(\""+encoding+"\");"+ls);
											sb.append("\t\t}catch(Exception e){e.printStackTrace();}"+ls);
										}
										else
											sb.append("\t\tbyte[] tmpBytes1 = "+name+"."+getter+"().getBytes();"+ls);
										tmpBytesDefined1 = true;
									}
									sb.append("\t\tif(tmpBytes1.length >= "+length+"){"+ls);
									sb.append("\t\t\tout.write(tmpBytes1,0,"+length+");"+ls);
									sb.append("\t\t}else{"+ls);
									sb.append("\t\t\tout.write(tmpBytes1);"+ls);
									sb.append("\t\t\tout.write(new byte["+length+"-tmpBytes1.length]);"+ls);
									sb.append("\t\t}"+ls);
								}
							}
						}
					}
				}
			}
		}
		
		//sb.append("\t\tbuffer.flip();"+ls);
		sb.append("\t\tbyte content[] = output.toByteArray();"+ls);
		//sb.append("\t\tbuffer.get(content);"+ls);
		sb.append("\t\tSystem.arraycopy(numberToByteArray(content.length,4), 0, content, 0, 4);"+ls);
		sb.append("\t\t"+reqName+".type = "+reqType+";"+ls);
		sb.append("\t\t"+reqName+".data = content;"+ls);
		sb.append("\t\t}catch(Exception e){e.printStackTrace();}"+ls);
		sb.append("\t\treturn "+reqName+";"+ls);
	}
	
	protected void generateHandleMessageCode(HashSet<String> referenceTypeSet,StringBuffer sb,Element e,boolean request) throws Exception{
		randomInt = 0;
		String ls = System.getProperty("line.separator");
		
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
		
		String reqType = null;
		if(request)
			reqType = XmlUtil.getAttributeAsString(e, "req_type",null,null);
		else
			reqType = XmlUtil.getAttributeAsString(e, "res_type",null,null);
		
		
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			sb.append("\t\t\t\t"+type+" "+name+";"+ls);
		}
		sb.append(ls);

		boolean lenDefined = false;
		boolean tmpBytesDefined = false;
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			if(pes[i].getNodeName().equals("property")){
				Class cl = getPrimitiveClass(type);
				if(cl.isArray()){
					if(!lenDefined){
						sb.append("\t\t\t\tint len = 0;"+ls);
						lenDefined = true;
					}
					sb.append("\t\t\t\tlen = di.readInt();"+ls);
					
					//sb.append("\t\t\t\tlen = (int)byteArrayToNumber(content,offset,4);"+ls);
					//sb.append("\t\t\t\toffset += 4;"+ls);
					
					sb.append("\t\t\t\t"+name+" = new "+getPrimitiveName(cl.getComponentType())+"[len];"+ls);
					
					if(cl.getComponentType() == Byte.TYPE){
						
						sb.append("\t\t\t\tdi.read("+name+");"+ls);
						
						//sb.append("\t\t\t\tSystem.arraycopy(content,offset,"+name+",0,len);"+ls);
						//sb.append("\t\t\t\toffset += len;"+ls);
					}else{
						sb.append("\t\t\t\tfor(int i = 0 ; i < "+name+".length ; i++){"+ls);
						if(cl.getComponentType() == Character.TYPE){
							sb.append("\t\t\t\t\t"+name+"[i] = di.readChar();"+ls);
						}else if(cl.getComponentType() == Short.TYPE){
							sb.append("\t\t\t\t\t"+name+"[i] = di.readShort();"+ls);
						}else if(cl.getComponentType() == Integer.TYPE){
							sb.append("\t\t\t\t\t"+name+"[i] = di.readInt();"+ls);
						}else if(cl.getComponentType() == Float.TYPE){
							sb.append("\t\t\t\t\t"+name+"[i] = di.readFloat();"+ls);
						}else if(cl.getComponentType() == Long.TYPE){
							sb.append("\t\t\t\t\t"+name+"[i] = di.readLong();"+ls);
						}else if(cl.getComponentType() == Boolean.TYPE){
							sb.append("\t\t\t\t\t"+name+"[i] = di.readBoolean();"+ls);
							//sb.append("\t\t\t\t\t"+name+"[i] = byteArrayToNumber(content,offset,1) != 0;"+ls);
							//sb.append("\t\t\t\t\toffset += 1;"+ls);
						}else if(cl.getComponentType() == String.class){
							sb.append("\t\t\t\t\t"+name+"[i] = di.readUTF();"+ls);
//								sb.append("\t\t\t\t\tlen = (int)byteArrayToNumber(content,offset,2);"+ls);
//								sb.append("\t\t\t\t\toffset += 2;"+ls);
//								String encoding = XmlUtil.getAttributeAsString(pes[i], "encoding","");
//								if(encoding.trim().length() > 0){
//									sb.append("\t\t\t\t\t"+name+"[i] = new String(content,offset,len,\""+encoding+"\");"+ls);
//								}else{
//									sb.append("\t\t\t\t\t"+name+"[i] = new String(content,offset,len);"+ls);
//								}
//								sb.append("\t\toffset += len;"+ls);
						}
						sb.append("\t\t\t\t}"+ls);
					}
				}else{
					
					if(cl == Byte.TYPE){
						sb.append("\t\t\t\t"+name+" = di.readByte();"+ls);
					}else if(cl == Character.TYPE){
						sb.append("\t\t\t\t"+name+" = di.readChar();"+ls);
					}else if(cl == Short.TYPE){
						sb.append("\t\t\t\t"+name+" = di.readShort();"+ls);
					}else if(cl == Integer.TYPE){
						sb.append("\t\t\t\t"+name+" = di.readInt();"+ls);
					}else if(cl == Float.TYPE){
						sb.append("\t\t\t\t"+name+" = di.readFloat();"+ls);
					}else if(cl == Long.TYPE){
						sb.append("\t\t\t\t"+name+" = di.readLong();"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\t\t\t"+name+" = di.readBoolean();"+ls);
					}	
					else if(cl == String.class){
						String encoding = XmlUtil.getAttributeAsString(pes[i], "encoding","",null);
						int strLen = XmlUtil.getAttributeAsInteger(pes[i], "length",0);
						if(strLen <= 0){
//							if(!lenDefined){
//								sb.append("\t\t\t\tint len = 0;"+ls);
//								lenDefined = true;
//							}
//							sb.append("\t\t\t\tlen = (int)byteArrayToNumber(content,offset,2);"+ls);
//							sb.append("\t\t\t\toffset += 2;"+ls);
//							if(encoding.trim().length()>0)
//								sb.append("\t\t\t\t"+name+" = new String(content,offset,len,\""+encoding+"\");"+ls);
//							else
//								sb.append("\t\t\t\t"+name+" = new String(content,offset,len);"+ls);
//							sb.append("\t\t\t\toffset += len;"+ls);
							sb.append("\t\t\t\t"+name+" = di.readUTF();"+ls);
						}else{
							if(!tmpBytesDefined){
								sb.append("\t\t\t\tbyte tempBytes[] = null;" + ls);
								tmpBytesDefined = true;
							}
							sb.append("\t\t\t\ttempBytes =new byte["+strLen+"];"+ls);
							sb.append("\t\t\t\tdi.read(tempBytes);"+ls);
							if(encoding.trim().length()>0){
								sb.append("\t\t\t\t"+name+" = new String(tempBytes,\""+encoding+"\").trim();"+ls);
								//sb.append("\t\t\t\t"+name+" = new String(content,offset,"+strLen+",\""+encoding+"\").trim();"+ls);
							}
							else{
								sb.append("\t\t\t\t"+name+" = new String(tempBytes).trim();"+ls);
								//sb.append("\t\t\t\t"+name+" = new String(content,offset,"+strLen+").trim();"+ls);
								
							}
							//sb.append("\t\t\t\toffset += "+strLen+";"+ls);
						}
					}
				}
			}else{ //object-property
				if(type.endsWith("[]")){
					type = type.substring(0, type.length()-2);
					
					referenceTypeSet.add(type);
					
					if(!lenDefined){
						sb.append("\t\t\t\tint len = 0;"+ls);
						lenDefined = true;
					}
					sb.append("\t\t\t\tlen = di.readInt();"+ls);
					//sb.append("\t\t\t\tlen = (int)byteArrayToNumber(content,offset,4);"+ls);
					//sb.append("\t\t\t\toffset += 4;"+ls);
					
					
					sb.append("\t\t\t\t"+name+" = new "+type+"[len];"+ls);
					
					sb.append("\t\t\t\tfor(int i = 0 ; i < "+name+".length ; i++){"+ls);
					sb.append("\t\t\t\t\t"+name+"[i] = new "+type+"();"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String setter = "set" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							
							//sb.append("\t\t\t\t\tlen = (int)byteArrayToNumber(content,offset,4);"+ls);
							//sb.append("\t\t\t\t\toffset += 4;"+ls);
							
							sb.append("\t\t\t\t\tlen = di.readInt();"+ls);
							
							fieldName = fieldName+"_"+randomIntegerString(); 
							sb.append("\t\t\t\t\t"+fieldType+" "+fieldName+" = new "+getPrimitiveName(cl.getComponentType())+"[len];"+ls);
							
							if(cl.getComponentType() == Byte.TYPE){
								//sb.append("\t\t\t\t\tSystem.arraycopy(content,offset,"+fieldName+",0,len);"+ls);
								//sb.append("\t\t\t\t\toffset += len;"+ls);
								sb.append("\t\t\t\t\tdi.read("+fieldName+");"+ls);
							}else{
								sb.append("\t\t\t\t\tfor(int j = 0 ; j < "+fieldName+".length ; j++){"+ls);
								
								if(cl.getComponentType() == Character.TYPE){
									sb.append("\t\t\t\t\t\t"+fieldName+"[j] = di.readChar();"+ls);
								}else if(cl.getComponentType() == Short.TYPE){
									sb.append("\t\t\t\t\t\t"+fieldName+"[j] = di.readShort();"+ls);
								}else if(cl.getComponentType() == Integer.TYPE){
									sb.append("\t\t\t\t\t\t"+fieldName+"[j] = di.readInt();"+ls);
								}else if(cl.getComponentType() == Float.TYPE){
									sb.append("\t\t\t\t\t\t"+fieldName+"[j] = di.readFloat();"+ls);
								}else if(cl.getComponentType() == Long.TYPE){
									sb.append("\t\t\t\t\t\t"+fieldName+"[j] = di.readLong();"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\t\t\t\t\t"+fieldName+"[j] = di.readBoolean();"+ls);
									//sb.append("\t\t\t\t\t"+name+"[i] = byteArrayToNumber(content,offset,1) != 0;"+ls);
									//sb.append("\t\t\t\t\toffset += 1;"+ls);
								}else if(cl.getComponentType() == String.class){
									sb.append("\t\t\t\t\t\t"+fieldName+"[j] = di.readUTF();"+ls);
//										sb.append("\t\t\t\t\tlen = (int)byteArrayToNumber(content,offset,2);"+ls);
//										sb.append("\t\t\t\t\toffset += 2;"+ls);
//										String encoding = XmlUtil.getAttributeAsString(pes[i], "encoding","");
//										if(encoding.trim().length() > 0){
//											sb.append("\t\t\t\t\t"+name+"[i] = new String(content,offset,len,\""+encoding+"\");"+ls);
//										}else{
//											sb.append("\t\t\t\t\t"+name+"[i] = new String(content,offset,len);"+ls);
//										}
//										sb.append("\t\toffset += len;"+ls);
								}
								
//								if(cl.getComponentType() == Character.TYPE 
//										|| cl.getComponentType() == Short.TYPE
//										|| cl.getComponentType() == Integer.TYPE
//										|| cl.getComponentType() == Long.TYPE){
//									sb.append("\t\t\t\t\t\t"+fieldName+"[j] = ("+getPrimitiveName(cl.getComponentType())+")byteArrayToNumber(content,offset,"+getPrimitiveSize(cl.getComponentType())+");"+ls);
//									sb.append("\t\t\t\t\t\toffset += "+getPrimitiveSize(cl.getComponentType())+";"+ls);
//								}else if(cl.getComponentType() == Boolean.TYPE){
//									sb.append("\t\t\t\t\t\t"+fieldName+"[j] = byteArrayToNumber(content,offset,1) != 0;"+ls);
//									sb.append("\t\t\t\t\t\toffset += 1;"+ls);
//								}else if(cl.getComponentType() == String.class){
//										sb.append("\t\t\t\t\t\tlen = (int)byteArrayToNumber(content,offset,2);"+ls);
//										sb.append("\t\t\t\t\t\toffset += 2;"+ls);
//										String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","");
//										if(encoding.trim().length()>0)
//											sb.append("\t\t\t\t\t\t"+fieldName+"[j] = new String(content,offset,len,\""+encoding+"\");"+ls);
//										else
//											sb.append("\t\t\t\t\t\t"+fieldName+"[j] = new String(content,offset,len);"+ls);
//										sb.append("\t\t\t\t\t\toffset += len;"+ls);
//								}
//								
								sb.append("\t\t\t\t\t}"+ls);
							}
							
							//TODO: 是否要修改成直接用变量名字赋值？
							sb.append("\t\t\t\t\t"+name+"[i]."+setter+"("+fieldName+");"+ls);
							
						}else{
							
							
							if(cl == Byte.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(di.readByte());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(di.readChar());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(di.readShort());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(di.readInt());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(di.readFloat());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(di.readLong());"+ls);
							}else if(cl == Boolean.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(di.readBoolean());"+ls);
							}	
							else if(cl == String.class){
								String encoding = XmlUtil.getAttributeAsString(pes[i], "encoding","",null);
								int strLen = XmlUtil.getAttributeAsInteger(pes[i], "length",0);
								if(strLen <= 0){
//									if(!lenDefined){
//										sb.append("\t\t\t\tint len = 0;"+ls);
//										lenDefined = true;
//									}
//									sb.append("\t\t\t\tlen = (int)byteArrayToNumber(content,offset,2);"+ls);
//									sb.append("\t\t\t\toffset += 2;"+ls);
//									if(encoding.trim().length()>0)
//										sb.append("\t\t\t\t"+name+" = new String(content,offset,len,\""+encoding+"\");"+ls);
//									else
//										sb.append("\t\t\t\t"+name+" = new String(content,offset,len);"+ls);
//									sb.append("\t\t\t\toffset += len;"+ls);
									sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(di.readUTF());"+ls);
								}else{
									if(!tmpBytesDefined){
										sb.append("\t\t\t\t\tbyte tempBytes[] = null;" + ls);
										tmpBytesDefined = true;
									}
									sb.append("\t\t\t\t\ttempBytes =new byte["+strLen+"];"+ls);
									sb.append("\t\t\t\t\tdi.read(tempBytes);"+ls);
									if(encoding.trim().length()>0){
										sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(new String(tempBytes,\""+encoding+"\").trim());"+ls);
										//sb.append("\t\t\t\t"+name+" = new String(content,offset,"+strLen+",\""+encoding+"\").trim();"+ls);
									}
									else{
										sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(new String(tempBytes).trim());"+ls);
										//sb.append("\t\t\t\t"+name+" = new String(content,offset,"+strLen+").trim();"+ls);
										
									}
									//sb.append("\t\t\t\toffset += "+strLen+";"+ls);
								}
							}
							
//							if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE || cl == Long.TYPE){
//								int length = getPrimitiveSize(cl);
//								sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(("+getPrimitiveName(cl)+")byteArrayToNumber(content,offset,"+length+"));"+ls);
//								sb.append("\t\t\t\t\toffset += "+length+";"+ls);
//							}else if(cl == Boolean.TYPE){
//								sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(byteArrayToNumber(content,offset,1) != 0);"+ls);
//								sb.append("\t\t\t\t\toffset += 1;"+ls);
//							}else if(cl == String.class){
//								int strLen = XmlUtil.getAttributeAsInteger(fields[j], "length",0);
//								if(strLen <= 0){
//									if(!lenDefined){
//										sb.append("\t\t\t\t\tint len = 0;"+ls);
//										lenDefined = true;
//									}
//									sb.append("\t\t\t\t\tlen = (int)byteArrayToNumber(content,offset,2);"+ls);
//									sb.append("\t\t\t\t\toffset += 2;"+ls);
//									String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","");
//									if(encoding.trim().length()>0)
//										sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(new String(content,offset,len,\""+encoding+"\"));"+ls);
//									else
//										sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(new String(content,offset,len));"+ls);
//									sb.append("\t\t\t\t\toffset += len;"+ls);
//								}else{
//									String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","");
//									if(encoding.trim().length()>0)
//										sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(new String(content,offset,"+strLen+",\""+encoding+"\").trim());"+ls);
//									else
//										sb.append("\t\t\t\t\t"+name+"[i]."+setter+"(new String(content,offset,"+strLen+").trim());"+ls);
//									sb.append("\t\t\t\t\toffset += "+strLen+";"+ls);
//								}
//							}
							
						}
					}
					sb.append("\t\t\t\t}"+ls);
					
				}else{
					
					referenceTypeSet.add(type);
					
					sb.append("\t\t\t\t"+name+" = new "+type+"();"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String setter = "set" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							if(!lenDefined){
								sb.append("\t\t\t\tint len = 0;"+ls);
								lenDefined = true;
							}
							//sb.append("\t\t\t\tlen = (int)byteArrayToNumber(content,offset,4);"+ls);
							//sb.append("\t\t\t\toffset += 4;"+ls);
							sb.append("\t\t\t\tlen = di.readInt();"+ls);
							
							fieldName = fieldName+"_"+randomIntegerString();
							sb.append("\t\t\t\t"+fieldType+" "+fieldName+" = new "+getPrimitiveName(cl.getComponentType())+"[len];"+ls);
							
							if(cl.getComponentType() == Byte.TYPE){
								//sb.append("\t\t\t\tSystem.arraycopy(content,offset,"+fieldName+",0,len);"+ls);
								//sb.append("\t\t\t\toffset += len;"+ls);
								sb.append("\t\t\t\tdi.read("+fieldName+");"+ls);
							}else{
								sb.append("\t\t\t\tfor(int j = 0 ; j < "+fieldName+".length ; j++){"+ls);
								
								if(cl.getComponentType() == Character.TYPE){
									sb.append("\t\t\t\t\t"+fieldName+"[j] = di.readChar();"+ls);
								}else if(cl.getComponentType() == Short.TYPE){
									sb.append("\t\t\t\t\t"+fieldName+"[j] = di.readShort();"+ls);
								}else if(cl.getComponentType() == Integer.TYPE){
									sb.append("\t\t\t\t\t"+fieldName+"[j] = di.readInt();"+ls);
								}else if(cl.getComponentType() == Float.TYPE){
									sb.append("\t\t\t\t\t"+fieldName+"[j] = di.readFloat();"+ls);
								}else if(cl.getComponentType() == Long.TYPE){
									sb.append("\t\t\t\t\t"+fieldName+"[j] = di.readLong();"+ls);
								}else if(cl.getComponentType() == Boolean.TYPE){
									sb.append("\t\t\t\t\t"+fieldName+"[j] = di.readBoolean();"+ls);
									//sb.append("\t\t\t\t\t"+name+"[i] = byteArrayToNumber(content,offset,1) != 0;"+ls);
									//sb.append("\t\t\t\t\toffset += 1;"+ls);
								}else if(cl.getComponentType() == String.class){
									sb.append("\t\t\t\t\t"+fieldName+"[j] = di.readUTF();"+ls);
//										sb.append("\t\t\t\t\tlen = (int)byteArrayToNumber(content,offset,2);"+ls);
//										sb.append("\t\t\t\t\toffset += 2;"+ls);
//										String encoding = XmlUtil.getAttributeAsString(pes[i], "encoding","");
//										if(encoding.trim().length() > 0){
//											sb.append("\t\t\t\t\t"+name+"[i] = new String(content,offset,len,\""+encoding+"\");"+ls);
//										}else{
//											sb.append("\t\t\t\t\t"+name+"[i] = new String(content,offset,len);"+ls);
//										}
//										sb.append("\t\toffset += len;"+ls);
								}
								
//								if(cl.getComponentType() == Character.TYPE 
//										|| cl.getComponentType() == Short.TYPE
//										|| cl.getComponentType() == Integer.TYPE
//										|| cl.getComponentType() == Long.TYPE){
//									sb.append("\t\t\t\t\t"+fieldName+"[j] = ("+getPrimitiveName(cl.getComponentType())+")byteArrayToNumber(content,offset,"+getPrimitiveSize(cl.getComponentType())+");"+ls);
//									sb.append("\t\t\t\t\toffset += "+getPrimitiveSize(cl.getComponentType())+";"+ls);
//								}else if(cl.getComponentType() == Boolean.TYPE){
//									sb.append("\t\t\t\t\t"+fieldName+"[j] = byteArrayToNumber(content,offset,1) != 0;"+ls);
//									sb.append("\t\t\t\t\toffset += 1;"+ls);
//								}else if(cl.getComponentType() == String.class){
//										sb.append("\t\t\t\t\tlen = (int)byteArrayToNumber(content,offset,2);"+ls);
//										sb.append("\t\t\t\t\toffset += 2;"+ls);
//										String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","");
//										if(encoding.trim().length() > 0)
//											sb.append("\t\t\t\t\t"+fieldName+"[j] = new String(content,offset,len,\""+encoding+"\");"+ls);
//										else
//											sb.append("\t\t\t\t\t"+fieldName+"[j] = new String(content,offset,len);"+ls);
//										sb.append("\t\t\t\t\t\toffset += len;"+ls);
//								}
//								
								sb.append("\t\t\t\t}"+ls);
							}
							sb.append("\t\t\t\t"+name+"."+setter+"("+fieldName+");"+ls);
						}else{
							
							

							if(cl == Byte.TYPE){
								sb.append("\t\t\t\t"+name+"."+setter+"(di.readByte());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\t\t\t"+name+"."+setter+"(di.readChar());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\t\t\t"+name+"."+setter+"(di.readShort());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\t\t\t"+name+"."+setter+"(di.readInt());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\t\t\t"+name+"."+setter+"(di.readFloat());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\t\t\t"+name+"."+setter+"(di.readLong());"+ls);
							}else if(cl == Boolean.TYPE){
								sb.append("\t\t\t\t"+name+"."+setter+"(di.readBoolean());"+ls);
							}	
							else if(cl == String.class){
								String encoding = XmlUtil.getAttributeAsString(pes[i], "encoding","",null);
								int strLen = XmlUtil.getAttributeAsInteger(pes[i], "length",0);
								if(strLen <= 0){
									sb.append("\t\t\t\t"+name+"."+setter+"(di.readUTF());"+ls);
								}else{
									if(!tmpBytesDefined){
										sb.append("\t\t\t\tbyte tempBytes[] = null;" + ls);
										tmpBytesDefined = true;
									}
									sb.append("\t\t\t\ttempBytes =new byte["+strLen+"];"+ls);
									sb.append("\t\t\t\tdi.read(tempBytes);"+ls);
									if(encoding.trim().length()>0){
										sb.append("\t\t\t\t"+name+"."+setter+"(new String(tempBytes,\""+encoding+"\").trim());"+ls);
										//sb.append("\t\t\t\t"+name+" = new String(content,offset,"+strLen+",\""+encoding+"\").trim();"+ls);
									}
									else{
										sb.append("\t\t\t\t"+name+"."+setter+"(new String(tempBytes).trim());"+ls);
										//sb.append("\t\t\t\t"+name+" = new String(content,offset,"+strLen+").trim();"+ls);
										
									}
									//sb.append("\t\t\t\toffset += "+strLen+";"+ls);
								}
							}
							
							
//							if(cl == Byte.TYPE || cl == Character.TYPE || cl == Short.TYPE || cl == Integer.TYPE || cl == Long.TYPE){
//								int length = getPrimitiveSize(cl);
//								sb.append("\t\t\t\t"+name+"."+setter+"(("+getPrimitiveName(cl)+")byteArrayToNumber(content,offset,"+length+"));"+ls);
//								sb.append("\t\t\t\toffset += "+length+";"+ls);
//							}else if(cl== Boolean.TYPE){
//								sb.append("\t\t\t\t"+name+"."+setter+"(byteArrayToNumber(content,offset,1) != 0);"+ls);
//								sb.append("\t\t\t\toffset += 1;"+ls);
//							}else if(cl == String.class){
//								int strLen = XmlUtil.getAttributeAsInteger(fields[j], "length",0);
//								if(strLen <= 0){
//									if(!lenDefined){
//										sb.append("\t\t\t\tint len = 0;"+ls);
//										lenDefined = true;
//									}
//									sb.append("\t\t\t\tlen = (int)byteArrayToNumber(content,offset,2);"+ls);
//									sb.append("\t\t\t\toffset += 2;"+ls);
//									String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","");
//									if(encoding.trim().length() > 0)
//										sb.append("\t\t\t\t"+name+"."+setter+"(new String(content,offset,len,\""+encoding+"\"));"+ls);
//									else
//										sb.append("\t\t\t\t"+name+"."+setter+"(new String(content,offset,len));"+ls);
//									sb.append("\t\t\t\toffset += len;"+ls);
//								}else{
//									String encoding =  XmlUtil.getAttributeAsString(fields[j], "encoding","");
//									if(encoding.trim().length() > 0)
//										sb.append("\t\t\t\t"+name+"."+setter+"(new String(content,offset,"+strLen+",\""+encoding+"\").trim());"+ls);
//									else
//										sb.append("\t\t\t\t"+name+"."+setter+"(new String(content,offset,"+strLen+").trim());"+ls);
//									sb.append("\t\t\t\toffset += "+strLen+";"+ls);
//								}
//							}
						}
					}
				}
				
			}
		}
		
		//end of 解析
		sb.append(ls);
		
		
		if(request){
			boolean resClientSend = XmlUtil.getAttributeAsBoolean(e, "res_client_send",false);
			String params = "";
			if(resClientSend){
				params = "seqNum,";
			}
			
			for(int i = 0 ; i < pes.length ; i++){
				String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
				params += name + ",";
			}
			if(params.endsWith(",")){
				params = params.substring(0,params.length()-1);
			}
			
			sb.append("\t\t\t\thandler.handle_"+reqName+"("+params+");"+ls);
		}else{
			String params = "";
			
			for(int i = 0 ; i < pes.length ; i++){
				String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
				params += name + ",";
			}
			if(params.endsWith(",")){
				params = params.substring(0,params.length()-1);
			}
			
			sb.append("\t\t\t\thandler.handle_"+reqName+"("+params+");"+ls);
		}
		
		sb.append("\t\t\t\tbreak;"+ls);
	}
	
	
	
	
	protected void generatePackageAndImports(StringBuffer sb){
		String packageName = env.get("package");
		if(packageName == null) packageName = "";
		String ls = System.getProperty("line.separator");
		String imports = env.get("imports");
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
	
	public final static int [] primitiveSize = {1,2,1,2,4,8,8,8,1,-1};
	
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
