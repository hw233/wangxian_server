package com.fy.engineserver.tool;

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
 * 此实现生成的代码是为了C++服务的，
 * 所以生成的代码非常的节俭。
 * 
 */

public class MessageComplier2For_C {

	/**
	 * 参数：
	 * -imports str 值为分号分隔的字符串
	 * -output.dir str 输出的目录，如果不设置，输出到标准输出流
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
			}else if(args[i].equals("-output.dir") && i < args.length -1){
				env.put("output.dir", args[i+1]);
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
				System.out.println("java -cp $cp com.xuanzhi.tools.transport.MessageComplier2ForC options <-file file>");
				System.out.println("author: wtx");
				System.out.println("version: 1.0");
				System.out.println("options:");
				System.out.println("        -imports str 值为分号分隔的字符串");
				System.out.println("        -output.dir str 输出的目录，如果不设置，输出到标准输出流");
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
		MessageComplier2For_C mc = new MessageComplier2For_C(env);
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
	 * output.dir 输出的目录
	 * version 版本，用于JavaDoc
	 * classes 使用到的Java类，逗号分隔。如果某个消息用到其中的类，将自动增加到import声明中。
	 * 
	 * @param env
	 */
	public MessageComplier2For_C(HashMap<String,String> env){
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
		
		LinkedHashMap<String,StringBuffer> classMap = new LinkedHashMap<String,StringBuffer>(); 
		generateFactory(protoTypeEles,factoryName,handlerName,maxMessageSize,headerLen,singlefunctionMaxLength,classMap);
		
		StringBuffer sb = generateMessageHandler(protoTypeEles,handlerName);
		classMap.put(handlerName+".h",sb);
		
		
		String dir = env.get("output.dir");
		if(dir == null){
			Iterator<Map.Entry<String,StringBuffer>> it = classMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,StringBuffer> me = it.next();
				System.out.println("=====================  "+me.getKey()+"  ===not save=================");
			}
		}else{
			File d = new File(dir);
			if(d.exists() && d.isDirectory()){
				Iterator<Map.Entry<String,StringBuffer>> it = classMap.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String,StringBuffer> me = it.next();
					byte bytes[] = me.getValue().toString().getBytes("utf-8");
					
					File file = new File(d,me.getKey());
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
		
		sb.append("class " + handlerName + "{"+ls);
		sb.append("public: "+ls);
		for(int i = 0 ; i < protoTypeEles.length ; i++){
			Element e = protoTypeEles[i];
			String reqName = XmlUtil.getAttributeAsString(e, "req_name",null,null);
			String resName = XmlUtil.getAttributeAsString(e, "res_name",null,null);

			boolean reqClientReceive = XmlUtil.getAttributeAsBoolean(e, "req_client_receive",false);
			boolean resClientReceive = XmlUtil.getAttributeAsBoolean(e, "res_client_receive",false);
			boolean resClientSend = XmlUtil.getAttributeAsBoolean(e, "res_client_send",false);
			
			if(reqName != null && reqClientReceive){
				
				sb.append("\t/**"+ls);
				sb.append("\t * "+ XmlUtil.getAttributeAsString(e, "comment","",null)+ls);
				
				Element pes[] = getPropertyElements(XmlUtil.getChildByName(e, "req"));
				String params = getParams(pes, resClientSend, referenceTypeSet,sb);				
				
				sb.append("\t */"+ls);
				
				
				sb.append("\tvirtual void handle_"+reqName+"("+params+"){"+ls);
				sb.append("\t\tprintf(\"警告：客户端没有实现协议"+reqName+"处理函数,参数的指针需要你来delete\\n\");"+ls);
				sb.append("\t}"+ls);
				sb.append(""+ls);
			}
			
			if(resName != null && resClientReceive){
				sb.append("\t/**"+ls);
				sb.append("\t * "+ XmlUtil.getAttributeAsString(e, "comment","",null)+ls);
				
				Element pes[] = getPropertyElements(XmlUtil.getChildByName(e, "res"));
				String params = getParams(pes, resClientSend, referenceTypeSet,sb);
				
				sb.append("\t */"+ls);

				sb.append("\tvirtual void handle_"+resName+"("+params+"){"+ls);
				sb.append("\t\tprintf(\"警告：客户端没有实现协议"+resName+"处理函数,参数的指针需要你来delete\\n\");"+ls);
				sb.append("\t}"+ls);
				sb.append(""+ls);
			}
		}
		sb.append("};"+ls);
		
		
		StringBuffer bufferH = new StringBuffer();
		StringBuffer bufferCpp = new StringBuffer();
		bufferH.append("#ifndef GAME_MESSAGE_HANDLER_H"+ls);
		bufferH.append("#define GAME_MESSAGE_HANDLER_H"+ls);
		
		generatePackageAndImports(referenceTypeSet,bufferH,bufferCpp);
		
		bufferH.append(sb.toString());
		
		bufferH.append("#endif");
		
		return bufferH;
		
	}
	
	private String getParams(Element pes[],boolean resClientSend,HashSet<String> referenceTypeSet,StringBuffer comment) throws ClassNotFoundException{
		String params = "";
		String ls = System.getProperty("line.separator");
		if(resClientSend){
			params = "int seqNum,";
		}
		for(int j = 0 ; j < pes.length ; j++){
			String type = XmlUtil.getAttributeAsString(pes[j],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[j],"name",null).trim();
			
			if(pes[j].getNodeName().equals("property")){
				Class cl = getPrimitiveClass(type);
				if(cl.isArray() || cl == String.class ){
					params+=getPrimitiveNameForC(cl)+" &"+name+",";
				}else{
					params+=getPrimitiveNameForC(cl)+" "+name+",";
				}
			}else{
				if(type.endsWith("[]")){
					type = type.substring(0, type.length()-2);
					params+="vector<"+type+"*> &"+name+",";
				}else{
					params+=type+" *"+name+",";
				}
			}
			if( comment!=null){
				comment.append("\t * @param &"+name+" "+XmlUtil.getAttributeAsString(pes[j],"comment","",null)+""+ls);
			}
			if( referenceTypeSet!=null){
				referenceTypeSet.add(type);
			}
		}
		if(params.endsWith(",")){
			params = params.substring(0, params.length() -1 );
		}
		return params;
	}
	/**
	 * <factory name="GameMessageFactory"/>
	 * 
	 * @param ele
	 * @return
	 */
	protected void generateFactory(Element protoTypeEles[],String factoryName,String handlerName,int maxMessageSize,int headerLen,int singlefunctionMaxLength,LinkedHashMap<String,StringBuffer> classMap)  throws Exception{
		String ls = System.getProperty("line.separator");
		
		StringBuffer sbH = new StringBuffer();
		StringBuffer sbCpp = new StringBuffer();
		HashSet<String> referenceTypeSet = new HashSet<String>();
		
		
		sbCpp.append(ls);
		
		
		
		
		
		sbH.append(ls);
		sbH.append("class " + factoryName);
		sbH.append(" {" + ls);
		sbH.append(ls);
		sbH.append("\tprivate:"+ls);		
		sbH.append("\tstatic int sequnceNum;// = 0;"+ls);	
		sbH.append("\tstatic ByteBuffer* output;// = new ByteBuffer("+maxMessageSize+"*8);"+ls);		
		sbH.append(ls);
		
		sbCpp.append("\tint "+factoryName+"::sequnceNum = 0;"+ls);	
		sbCpp.append("\tByteBuffer* "+factoryName+"::output = new ByteBuffer("+maxMessageSize+"*8);"+ls);		
		sbCpp.append(ls);
		
		sbH.append(ls);
		sbH.append("\tpublic:"+ls);
		sbH.append("\tstatic int getNumOfByteForMessageLength() {"+ls);
		sbH.append("\t\treturn 4;"+ls);
		sbH.append("\t}"+ls);
		sbH.append(ls);		
		
		
		
		
		
		int handleCount = 0;		
		int startProto = 0;
		int lastProto = -1;
		while(startProto< protoTypeEles.length){
			handleCount++;
			int lastsblength = sbCpp.length();
			sbH.append("\tstatic boolean handleMessage"+handleCount+"(ByteBuffer* di,Message* protocol_message,"+handlerName+"* handler) ;"+ls);
			sbCpp.append("\t boolean "+factoryName+"::handleMessage"+handleCount+"(ByteBuffer* di,Message* protocol_message,"+handlerName+"* handler) {"+ls);
			sbCpp.append("\t\tint seqNum = protocol_message->seqNum;"+ls);
			sbCpp.append("\t\tswitch(protocol_message->type){"+ls);
			
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
					sbCpp.append("\t\t\tcase "+reqType+":{"+ls);
					generateHandleMessageCode(referenceTypeSet,sbCpp,e,true);
					sbCpp.append("\t\t\t}"+ls);
				}
				
				if(resName != null && resClientReceive){
					sbCpp.append("\t\t\tcase "+resType+":{"+ls);
					generateHandleMessageCode(referenceTypeSet,sbCpp,e,false);
					sbCpp.append("\t\t\t}"+ls);
				}
				//防止单个函数过大
				if( sbCpp.length()-lastsblength> singlefunctionMaxLength){
					startProto++;
					break;					
				}
			}
			sbCpp.append("\t\t\tdefault:{"+ls);
			sbCpp.append("\t\t\treturn false;"+ls);
			sbCpp.append("\t\t\t}"+ls);			
			sbCpp.append("\t\t}"+ls);
			sbCpp.append("\t\treturn true;"+ls);
			sbCpp.append("\t}"+ls);
			sbH.append(ls);
		}
		
		sbH.append("\t/**"+ls);
		sbH.append("\t * 处理消息方法，所有的请求和响应消息都应该调用此方法"+ls);
		sbH.append("\t * 第二个参数为实现处理接口的类实例"+ls);
		sbH.append("\t */"+ls);
		sbH.append("\tstatic void handleMessage(Message* protocol_message,"+handlerName+"* handler) ;"+ls);
		
		sbCpp.append("\t/**"+ls);
		sbCpp.append("\t * 处理消息方法，所有的请求和响应消息都应该调用此方法"+ls);
		sbCpp.append("\t * 第二个参数为实现处理接口的类实例"+ls);
		sbCpp.append("\t */"+ls);
		sbCpp.append("\t void "+factoryName+"::handleMessage(Message* protocol_message,"+handlerName+"* handler) {"+ls);
		sbCpp.append("\t\tByteBuffer* di = new ByteBuffer(protocol_message->data,"+headerLen+",protocol_message->datalength-"+headerLen+");"+ls);
		
		for( int i=1;i<=handleCount;i++){
			sbCpp.append("\t\tif(handleMessage"+i+"(di,protocol_message,handler)){"+ls);
			sbCpp.append("\t\t\tdelete di;"+ls);
			sbCpp.append("\t\t\treturn;"+ls);
			sbCpp.append("\t\t}"+ls);
		}
		sbCpp.append("\t\tdelete di;"+ls);
		sbCpp.append("\t\tprintln(\"协议错误：不能识别的消息类型：0x%x\",protocol_message->type);"+ls);
		
		
		sbCpp.append("\t}"+ls);
		//end handleMessage
		
		sbH.append(ls);
		
		
		for(int i = 0 ; i < protoTypeEles.length ; i++){
			Element e = protoTypeEles[i];
			String reqName = XmlUtil.getAttributeAsString(e, "req_name",null,null);
			String resName = XmlUtil.getAttributeAsString(e, "res_name",null,null);

			boolean reqClientSend = XmlUtil.getAttributeAsBoolean(e, "req_client_send",false);
			boolean resClientSend = XmlUtil.getAttributeAsBoolean(e, "res_client_send",false);
			
			if(reqName != null && reqClientSend){
				sbH.append("\t/**"+ls);
				sbH.append("\t *"+XmlUtil.getAttributeAsString(e, "comment","",null)+ls);
				Element pes[] = getPropertyElements(XmlUtil.getChildByName(e, "req"));
				String params = getParams(pes,false,referenceTypeSet,sbH);
				
				sbH.append("\t */"+ls);
				
				sbH.append("\tstatic Message* construct_"+reqName+"("+params+");"+ls);
				
				sbCpp.append("\t Message* "+factoryName+"::construct_"+reqName+"("+params+"){"+ls);
				generateConstructMessageCode(referenceTypeSet,sbCpp,e,true);
				sbCpp.append("\t}"+ls);
				sbCpp.append(""+ls);
			}
			
			if(resName != null && resClientSend){
				sbH.append("\t/**"+ls);
				sbH.append("\t *"+XmlUtil.getAttributeAsString(e, "comment","",null)+ls);
				
				Element pes[] = getPropertyElements(XmlUtil.getChildByName(e, "res"));
				String params = getParams(pes,true,referenceTypeSet,sbH);
				
				
				sbH.append("\t */"+ls);
				
				sbH.append("\tstatic Message* construct_"+resName+"("+params+");"+ls);
				
				sbCpp.append("\t Message* "+factoryName+"::construct_"+resName+"("+params+"){"+ls);
				generateConstructMessageCode(referenceTypeSet,sbCpp,e,false);
				sbCpp.append("\t}"+ls);
			}
		}
	
		sbH.append("};"+ls);
		
		StringBuffer bufferH = new StringBuffer();
		StringBuffer bufferCpp = new StringBuffer();
		bufferH.append("#ifndef GameMessageFactory_H");
		bufferH.append(ls);
		bufferH.append("#define GameMessageFactory_H");
		bufferH.append(ls);	
		
		bufferH.append("class Message;"+ls);
		bufferH.append("class GameMessageHandler;"+ls);
		
		bufferCpp.append("#include \""+factoryName+".h\""+ls);		
		bufferCpp.append("#include \"message.h\""+ls);
		bufferCpp.append("#include \"GameMessageHandler.h\""+ls);
		
		
		generatePackageAndImports(referenceTypeSet,bufferH,bufferCpp);
		
		bufferH.append(sbH.toString());		
		bufferH.append("#endif");
		
		bufferCpp.append(sbCpp.toString());
		
		classMap.put(factoryName+".h", bufferH);
		classMap.put(factoryName+".cpp", bufferCpp);
	
	}
	
	protected void generateConstructMessageCode(HashSet<String> referenceTypeSet,StringBuffer sb,Element e,boolean request) throws Exception{
		
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

		
		
		sb.append("\t\t"+ls);
		sb.append("\t\tMessage *"+reqName+" = new Message();"+ls);
		
		sb.append("\t\toutput->rewind();"+ls);
		sb.append("\t\toutput->putInt(0);"+ls);
		sb.append("\t\toutput->putInt("+reqType+");"+ls);
		if(request){
			sb.append("\t\t"+reqName+"->seqNum = sequnceNum++;"+ls);
		}else{
			sb.append("\t\t"+reqName+"->seqNum = seqNum;"+ls);
		}
		sb.append("\t\toutput->putInt("+reqName+"->seqNum);"+ls);
		
		sb.append(ls);	
		for(int i = 0 ; i < pes.length ; i++){
			String type = XmlUtil.getAttributeAsString(pes[i],"type",null).trim();
			String name = XmlUtil.getAttributeAsString(pes[i],"name",null).trim();
			if(pes[i].getNodeName().equals("property")){
				Class cl = getPrimitiveClass(type);
				if(cl.isArray()){
					sb.append("\t\toutput->putArray("+name+");"+ls);
				}else{
					if(cl == Byte.TYPE){
						sb.append("\t\toutput->putByte("+name+");"+ls);
					}else if(cl == Character.TYPE){
						sb.append("\t\toutput->putWCHAR("+name+");"+ls);
					}else if(cl == Short.TYPE){
						sb.append("\t\toutput->putShort("+name+");"+ls);
					}else if(cl == Integer.TYPE){
						sb.append("\t\toutput->putInt("+name+");"+ls);
					}else if(cl == Float.TYPE){
						sb.append("\t\toutput->putFloat("+name+");"+ls);
					}else if( cl == Long.TYPE){
						sb.append("\t\toutput->putLong("+name+");"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\toutput->putBoolean("+name+");"+ls);
					}else if(cl == String.class){
						sb.append("\t\toutput->putUTF("+name+");"+ls);
					}
				}
			}else{ //object-property
				if(type.endsWith("[]")){
					type = type.substring(0, type.length()-2);
					
					referenceTypeSet.add(type);
					
					sb.append("\t\toutput->putInt("+name+".size());"+ls);
					sb.append("\t\tfor(int i = 0 ; i < "+name+".size() ; i++){"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String getter = "get" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){							
							sb.append("\t\t\toutput->putArray(*"+name+"[i]->"+getter+"());"+ls);
						}else{
							if(cl == Byte.TYPE){
								sb.append("\t\t\toutput->putByte((byte)"+name+"[i]->"+getter+"());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\t\toutput->putWCHAR((char)"+name+"[i]->"+getter+"());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\t\toutput->putShort((short)"+name+"[i]->"+getter+"());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\t\toutput->putInt((int)"+name+"[i]->"+getter+"());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\t\toutput->putFloat((float)"+name+"[i]->"+getter+"());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\t\toutput->putLong("+name+"[i]->"+getter+"());"+ls);
							}else if(cl == Boolean.TYPE){
								String booleanGetter = "is" +  Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
								sb.append("\t\t\toutput->putBoolean("+name+"[i]->"+booleanGetter+"());"+ls);
							}else if(cl == String.class){
								sb.append("\t\t\toutput->putUTF("+name+"[i]->"+getter+"());"+ls);
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
							sb.append("\t\toutput->putArray(*"+name+"->"+getter+"());"+ls);
						}else{
							if(cl == Byte.TYPE){
								sb.append("\t\toutput->putByte((byte)"+name+"->"+getter+"());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\toutput->putWCHAR((char)"+name+"->"+getter+"());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\toutput->putShort((short)"+name+"->"+getter+"());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\toutput->putInt((int)"+name+"->"+getter+"());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\toutput->putFloat((float)"+name+"->"+getter+"());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\toutput->putLong("+name+"->"+getter+"());"+ls);
							}else if(cl == Boolean.TYPE){
								String booleanGetter = "is" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
								sb.append("\t\toutput->putBoolean("+name+"->"+booleanGetter+"());"+ls);
							}else if(cl == String.class){
								sb.append("\t\toutput->putUTF("+name+"->"+getter+"());"+ls);
							}
						}
					}
				}
			}
		}
		
		sb.append("\t\toutput->setIntAt(output->getPosition(),0);"+ls);
		
		sb.append("\t\t"+reqName+"->type = "+reqType+";"+ls);
		
		sb.append("\t\t"+reqName+"->data = output->toByteArray();"+ls);
		sb.append("\t\t"+reqName+"->datalength = output->getPosition();"+ls);
		
		
		
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
			if(pes[i].getNodeName().equals("property")){
				Class cl = getPrimitiveClass(type);
				
				sb.append("\t\t\t\t"+getPrimitiveNameForC(cl)+" "+name+";"+ls);
				
			}else{
				if(type.endsWith("[]")){
					sb.append("\t\t\t\tvector<"+type.substring(0, type.length()-2)+"*> "+name+";"+ls);
				}else{
					sb.append("\t\t\t\t"+type+" *"+name+";"+ls);
				}
			}			
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
					sb.append("\t\t\t\tdi->getArray("+name+");"+ls);									
				}else{					
					if(cl == Byte.TYPE){
						sb.append("\t\t\t\t"+name+" = di->getByte();"+ls);
					}else if(cl == Character.TYPE){
						sb.append("\t\t\t\t"+name+" = di->getChar();"+ls);
					}else if(cl == Short.TYPE){
						sb.append("\t\t\t\t"+name+" = di->getShort();"+ls);
					}else if(cl == Integer.TYPE){
						sb.append("\t\t\t\t"+name+" = di->getInt();"+ls);
					}else if(cl == Float.TYPE){
						sb.append("\t\t\t\t"+name+" = di->getFloat();"+ls);
					}else if(cl == Long.TYPE){
						sb.append("\t\t\t\t"+name+" = di->getLong();"+ls);
					}else if(cl == Boolean.TYPE){
						sb.append("\t\t\t\t"+name+" = di->getBoolean();"+ls);
					}	
					else if(cl == String.class){
						sb.append("\t\t\t\tdi->getUTF("+name+");"+ls);
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
					sb.append("\t\t\t\tlen = di->getInt();"+ls);
					sb.append("\t\t\t\t"+name+".resize(len);"+ls);
					sb.append("\t\t\t\tfor(int i = 0 ; i < len ; i++){"+ls);
					
					sb.append("\t\t\t\t\t"+name+"[i] = new "+type+"();"+ls);
					
					Element fields[] = XmlUtil.getChildrenByName(pes[i],"property");
					for(int j = 0 ; j < fields.length ; j++){
						String fieldName = XmlUtil.getAttributeAsString(fields[j],"name",null);
						String setter = "set" + Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1); 
						String fieldType = XmlUtil.getAttributeAsString(fields[j],"type",null);
						Class cl = getPrimitiveClass(fieldType);
						if(cl.isArray()){
							fieldName = fieldName+"_"+randomIntegerString();							
							sb.append("\t\t\t\t\t"+getPrimitiveNameForC(cl)+" "+fieldName+";"+ls);
							sb.append("\t\t\t\t\tdi->getArray("+fieldName+");"+ls);
							sb.append("\t\t\t\t\t"+name+"[i]->"+setter+"("+fieldName+");"+ls);
						}else{
							if(cl == Byte.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]->"+setter+"(di->getByte());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]->"+setter+"(di->getChar());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]->"+setter+"(di->getShort());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]->"+setter+"(di->getInt());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]->"+setter+"(di->getFloat());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]->"+setter+"(di->getLong());"+ls);
							}else if(cl == Boolean.TYPE){
								sb.append("\t\t\t\t\t"+name+"[i]->"+setter+"(di->getBoolean());"+ls);
							}
							else if(cl == String.class){								
								sb.append("\t\t\t\t\t"+name+"[i]->"+setter+"(di->getUTF());"+ls);
							}
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
							
							fieldName = fieldName+"_"+randomIntegerString();
							sb.append("\t\t\t\t"+getPrimitiveNameForC(cl)+" "+fieldName+";"+ls);
							sb.append("\t\t\t\tdi->getArray("+fieldName+");"+ls);							
							sb.append("\t\t\t\t"+name+"->"+setter+"("+fieldName+");"+ls);
						}else{
							if(cl == Byte.TYPE){
								sb.append("\t\t\t\t"+name+"->"+setter+"(di->getByte());"+ls);
							}else if(cl == Character.TYPE){
								sb.append("\t\t\t\t"+name+"->"+setter+"(di->getChar());"+ls);
							}else if(cl == Short.TYPE){
								sb.append("\t\t\t\t"+name+"->"+setter+"(di->getShort());"+ls);
							}else if(cl == Integer.TYPE){
								sb.append("\t\t\t\t"+name+"->"+setter+"(di->getInt());"+ls);
							}else if(cl == Float.TYPE){
								sb.append("\t\t\t\t"+name+"->"+setter+"(di->getFloat());"+ls);
							}else if(cl == Long.TYPE){
								sb.append("\t\t\t\t"+name+"->"+setter+"(di->getLong());"+ls);
							}else if(cl == Boolean.TYPE){
								sb.append("\t\t\t\t"+name+"->"+setter+"(di->getBoolean());"+ls);
							}	
							else if(cl == String.class){
								sb.append("\t\t\t\t"+name+"->"+setter+"(di->getUTF());"+ls);
							}
						}
					}
					
				}
				
			}
		}
		
		//end of 解析
		sb.append(ls);
		
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
		
		sb.append("\t\t\t\thandler->handle_"+reqName+"("+params+");"+ls);
		
		
		
		sb.append("\t\t\t\tbreak;"+ls);
	}
	
	
	
	
	protected void generatePackageAndImports(HashSet<String> referenceTypeSet,StringBuffer sbH,StringBuffer sbCpp){
		
		String ls = System.getProperty("line.separator");
		String imports = env.get("imports");
		String importNames[] = imports.split(";");
		
		sbH.append("#include \"Util.h\""+ls);
		
		for(int i = 0 ; i < importNames.length; i++){
			if(importNames[i].trim().length() > 0){
				
				
					sbH.append("class " + importNames[i]+";"+ls);
							
					sbCpp.append("#include \"" + importNames[i]+".h\""+ls);
				
			}
		}
		String classesStr = env.get("classes");
		if(classesStr != null){
			String classNames[] = classesStr.split(",");
			for(int k = 0 ; k < classNames.length ; k++){
				if(classNames[k].lastIndexOf(".")>0){
					String type = classNames[k].substring(classNames[k].lastIndexOf(".")+1);
					if(referenceTypeSet.contains(type)){
						sbH.append("class " + type+";"+ls);
						sbCpp.append("#include \"" + type+".h\""+ls);
					}
				}
			}
		}
		
		
		sbH.append(ls);
		sbCpp.append(ls);
		
		
		
		
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
	public final static String [] primitiveNameForC = {"bool","wchar_t","byte","short","int","long long","float","double","void","std::string"};
	private static Class [] primitiveClass = {Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE,String.class};

	public final static int [] primitiveSize = {1,2,1,2,4,8,8,8,1,-1};
	
	private static String [] primitiveArrayName = {"boolean[]", "char[]", "byte[]", "short[]", "int[]", "long[]", "float[]", "double[]", "void[]","String[]"};	
	private static String [] primitiveArrayNameForC = {"vector<bool>", "vector<wchar_t>", "vector<byte>", "vector<short>", "vector<int>", "vector<long long>", "vector<float>", "vector<double>", "vector<void>","vector<std::string>"};
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
	
	public static String getPrimitiveNameForC(Class s){
		for(int i = 0 ; i < primitiveClass.length ; i ++){
			if(primitiveClass[i].equals(s)) return  primitiveNameForC[i];
		}
		for(int i = 0 ; i < primitiveArrayClass.length ; i ++){
			if(primitiveArrayClass[i].equals(s)) return  primitiveArrayNameForC[i];
		}
		return s.getSimpleName();
	}
	
//	public static int getPrimitiveSize(Class cl) throws ClassNotFoundException {
//		for(int i = 0 ; i < primitiveClass.length ; i ++){
//			if(primitiveClass[i].equals(cl)) return  primitiveSize[i];
//		}
//		throw new ClassNotFoundException("getPrimitiveClass: class ["+cl+"] is not a primitive class.");
//		
//	}

}
