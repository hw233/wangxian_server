package com.xuanzhi.tools.transport;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.w3c.dom.Document;

import junit.framework.*;
import com.xuanzhi.tools.text.*;

public class MessageComplierTestCase extends TestCase{

	public MessageComplierTestCase(){
		super();
	}
	
	public void testRequestMessage1() throws Exception{
		HashMap<String,String> env = new HashMap<String,String>(); 
		env.put("package", "com.xuanzhi.tools.transport2");
		env.put("imports", "java.nio.ByteBuffer;com.xuanzhi.tools.transport.*");
		MessageComplier mc = new MessageComplier(env);
		String domContent = "<prototypes version=\"1.0\" author=\"myzdf\">\n";
		
		domContent += "\t<factory name=\"GameMessageFactory\"/>";

		domContent += "\t<prototype req_name=\"CHAT_MESSAGE_TEST_REQ\" res_name=\"CHAT_MESSAGE_TEST_RES\""; 
		domContent += "\treq_type=\"0x00000002\" res_type=\"0x80000002\">";
		domContent += "\t<req>";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" comment=\"\"/>";
		domContent += "\t<property name=\"message\" type=\"String\" comment=\"内容\"/>";
		domContent += "\t</req>";
		domContent += "\t<res>";
		domContent += "\t<property name=\"result\" type=\"boolean\" comment=\"结果\"/>";
		domContent += "\t</res>";
		domContent += "\t</prototype>";
	
		
		domContent += "</prototypes>";
		
		String userDir = System.getProperty("user.dir");
		System.out.println("userDir = " + userDir);
		
		Document dom = XmlUtil.loadStringWithoutTitle(domContent);
		
		StringBuffer sb = mc.generateRequestMessage("GameMessageFactory", 
				XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype")[0]);
		
		System.out.println("==========================================================");
		System.out.println(sb);
		
		File file = new File(userDir + "/common_tools/src/test/com/xuanzhi/tools/transport2/CHAT_MESSAGE_TEST_REQ.java");
		FileOutputStream output = new FileOutputStream(file);
		output.write(sb.toString().getBytes("UTF-8"));
		output.close();
		
		sb = mc.generateResponseMessage("GameMessageFactory", 
				XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype")[0]);
		
		System.out.println("==========================================================");
		System.out.println(sb);
		
		file = new File(userDir + "/common_tools/src/test/com/xuanzhi/tools/transport2/CHAT_MESSAGE_TEST_RES.java");
		output = new FileOutputStream(file);
		output.write(sb.toString().getBytes("UTF-8"));
		output.close();
		
		
		sb = mc.generateFactory(XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype"), "GameMessageFactory", false); 
		
		System.out.println("==========================================================");
		System.out.println(sb);
		
		file = new File(userDir + "/common_tools/src/test/com/xuanzhi/tools/transport2/GameMessageFactory.java");
		output = new FileOutputStream(file);
		output.write(sb.toString().getBytes("UTF-8"));
		output.close();
		
	}
	/*
	public void testRequestMessage1() throws Exception{
		HashMap<String,String> env = new HashMap<String,String>(); 
		env.put("package", "com.xuanzhi.tools.transport");
		env.put("imports", "java.nio.ByteBuffer;com.xuanzhi.tools.transport.*");
		MessageComplier mc = new MessageComplier(env);
		String domContent = "<prototypes version=\"1.0\" author=\"myzdf\">\n";
		domContent += "\t<header>";
		domContent += "\t<property name=\"playerId\" type=\"int\" comment=\"���\"/>";
		domContent += "\t<property name=\"serviceId\" type=\"short\" comment=\"����\"/>";
		domContent += "\t<property name=\"serviceName\" type=\"String\" length=\"33\" encoding=\"UTF-8\" comment=\"�������\"/>";
		domContent += "\t<property name=\"playerName\" type=\"String\" comment=\"�����\"/>";
		domContent += "\t</header>";
		domContent += "\t<factory name=\"GameMessageFactory\"/>";
		domContent += "\t<verification name=\"MD5\" a=\"0x12374834\" b=\"0xfe34b384\" c=\"0x243948e\" d=\"0x45838f4\"/>";
		domContent += "\t<prototype req_name=\"ACTIVE_TEST_REQ\" res_name=\"ACTIVE_TEST_RES\""; 
		domContent += "\treq_type=\"0x00000002\" res_type=\"0x80000002\">";
		domContent += "\t<req>";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" comment=\"\"/>";
		domContent += "\t<property name=\"x\" type=\"short\" comment=\"���x\"/>";
		domContent += "\t<property name=\"y\" type=\"short\" comment=\"���y\"/>";
		domContent += "\t<property name=\"moveSpriteIds\" type=\"int[]\" max-num=\"16\" comment=\"�ƶ������ID����\"/>";
		domContent += "\t</req>";
		domContent += "\t<res>";
		domContent += "\t<property name=\"result\" type=\"boolean\" comment=\"�����\"/>";
		domContent += "\t<object-property name=\"sprite\" type=\"Sprite\" comment=\"\">";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" comment=\"\"/>";
		domContent += "\t<property name=\"x\" type=\"short\" comment=\"���x\"/>";
		domContent += "\t<property name=\"y\" type=\"short\" comment=\"���y\"/>";
		domContent += "\t<property name=\"moveSpriteIds\" type=\"int[]\" max-num=\"32\" comment=\"�ƶ������ID����\"/>";
		domContent += "\t</object-property>";
		domContent += "\t<object-property name=\"sprites\" type=\"Sprite[]\" max-num=\"128\" comment=\"\">";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" comment=\"\"/>";
		domContent += "\t<property name=\"x\" type=\"short\" comment=\"���x\"/>";
		domContent += "\t<property name=\"y\" type=\"short\" comment=\"���y\"/>";
		domContent += "\t<property name=\"moveSpriteIds\" type=\"int[]\" max-num=\"16\" comment=\"�ƶ������ID����\"/>";
		domContent += "\t</object-property>";
		domContent += "\t</res>";
		domContent += "\t</prototype>";
	
		domContent += "</prototypes>";
		
		Document dom = XmlUtil.loadStringWithoutTitle(domContent);
		
		StringBuffer sb = mc.generateRequestMessage("GameMessageFactory", 
				XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype")[0]);
		
		System.out.println(sb);
		
	}
	
	public void testRequestMessage2() throws Exception{
		HashMap<String,String> env = new HashMap<String,String>(); 
		env.put("package", "com.xuanzhi.aaaa");
		env.put("imports", "java.nio.ByteBuffer;com.xuanzhi.tools.transport.*");
		env.put("classes", "com.xuanzhi.gameengine.map.Sprite,com.xuanzhi.gameengine.map.Sprite2");
		MessageComplier mc = new MessageComplier(env);
		String domContent = "<prototypes version=\"1.0\" author=\"myzdf\">\n";
		domContent += "\t<factory name=\"GameMessageFactory\"/>";
		domContent += "\t<verification name=\"MD5\" a=\"0x12374834\" b=\"0xfe34b384\" c=\"0x243948e\" d=\"0x45838f4\"/>";
		domContent += "\t<prototype req_name=\"ACTIVE_TEST_REQ\" res_name=\"ACTIVE_TEST_RES\""; 
		domContent += "\treq_type=\"0x00000002\" res_type=\"0x80000002\">";
		domContent += "\t<req>";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" comment=\"\"/>";
		domContent += "\t<property name=\"x\" type=\"short\" comment=\"���x\"/>";
		domContent += "\t<property name=\"y\" type=\"short\" comment=\"���y\"/>";
		domContent += "\t<property name=\"moveSpriteIds\" type=\"int[]\" max-num=\"16\" comment=\"�ƶ������ID����\"/>";
		domContent += "\t</req>";
		domContent += "\t<res>";
		domContent += "\t<property name=\"result\" type=\"boolean\" comment=\"�����\"/>";
		domContent += "\t<object-property name=\"sprite\" type=\"Sprite\" comment=\"\">";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" comment=\"\"/>";
		domContent += "\t<property name=\"x\" type=\"short\" comment=\"���x\"/>";
		domContent += "\t<property name=\"y\" type=\"short\" comment=\"���y\"/>";
		domContent += "\t<property name=\"moveSpriteIds\" type=\"int[]\" max-num=\"32\" comment=\"�ƶ������ID����\"/>";
		domContent += "\t</object-property>";
		domContent += "\t<object-property name=\"sprites\" type=\"Sprite[]\" max-num=\"128\" comment=\"\">";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" comment=\"\"/>";
		domContent += "\t<property name=\"x\" type=\"short\" comment=\"���x\"/>";
		domContent += "\t<property name=\"y\" type=\"short\" comment=\"���y\"/>";
		domContent += "\t<property name=\"moveSpriteIds\" type=\"int[]\" max-num=\"16\" comment=\"�ƶ������ID����\"/>";
		domContent += "\t</object-property>";
		domContent += "\t</res>";
		domContent += "\t</prototype>";
	
		domContent += "</prototypes>";
		
		Document dom = XmlUtil.loadStringWithoutTitle(domContent);
		StringBuffer sb = mc.generateRequestMessage("GameMessageFactory", 
				XmlUtil.getChildrenByName(dom.getDocumentElement(),"prototype")[0]);
		
		System.out.println(sb);
		
		
		
	}*/
	
	/*
	public void testComplie() throws Exception{
		HashMap<String,String> env = new HashMap<String,String>(); 
		env.put("package", "com.xuanzhi.tools.transport.message");
		env.put("imports", "java.nio.ByteBuffer;com.xuanzhi.tools.transport.*");
		env.put("version", "1.0");
		env.put("classes", "com.xuanzhi.tools.transport.Sprite");
		env.put("output.dir", "./src/test/com/xuanzhi/tools/transport/message");
		
		MessageComplier mc = new MessageComplier(env);
		String domContent = "<prototypes version=\"1.0\" author=\"myzdf\">\n";
		domContent += "\t<header>";
		domContent += "\t<property name=\"playerId\" type=\"int\" comment=\"���\"/>";
		domContent += "\t<property name=\"serviceId\" type=\"short\" comment=\"����\"/>";
		domContent += "\t<property name=\"serviceName\" type=\"String\" length=\"33\" encoding=\"UTF-8\" comment=\"�������\"/>";
		domContent += "\t<property name=\"playerName\" type=\"String\" comment=\"�����\"/>";
		domContent += "\t</header>";
		
		domContent += "\t<factory name=\"GameMessageFactory\" enable-serialize='true' />";
		domContent += "\t<verification name=\"MD5\" a=\"0x12374834\" b=\"0xfe34b384\" c=\"0x243948e\" d=\"0x45838f4\"/>";
		domContent += "\t<prototype req_name=\"ACTIVE_TEST_REQ\" res_name=\"ACTIVE_TEST_RES\""; 
		domContent += "\treq_type=\"0x00000002\" res_type=\"0x80000002\">";
		domContent += "\t<req>";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" encoding=\"UTF-8\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" encoding=\"GBK\" comment=\"\"/>";
		domContent += "\t<property name=\"x\" type=\"short\" comment=\"���x\"/>";
		domContent += "\t<property name=\"y\" type=\"short\" comment=\"���y\"/>";
		domContent += "\t<property name=\"moveSpriteIds\" type=\"int[]\" max-num=\"16\" comment=\"�ƶ������ID����\"/>";
		domContent += "\t</req>";
		domContent += "\t<res>";
		domContent += "\t<property name=\"result\" type=\"boolean\" comment=\"�����\"/>";
		domContent += "\t<object-property name=\"sprite\" type=\"Sprite\" comment=\"\">";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" encoding=\"UTF-8\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" encoding=\"UTF-8\" comment=\"\"/>";
		domContent += "\t<property name=\"x\" type=\"short\" comment=\"���x\"/>";
		domContent += "\t<property name=\"y\" type=\"short\" comment=\"���y\"/>";
		domContent += "\t<property name=\"moveSpriteIds\" type=\"int[]\" max-num=\"32\" comment=\"�ƶ������ID����\"/>";
		domContent += "\t</object-property>";
		domContent += "\t<object-property name=\"sprites\" type=\"Sprite[]\" max-num=\"128\" comment=\"\">";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" encoding=\"UTF-8\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteDesp\" type=\"String\" length=\"16\" comment=\"\"/>";
		domContent += "\t<property name=\"x\" type=\"short\" comment=\"���x\"/>";
		domContent += "\t<property name=\"y\" type=\"short\" comment=\"���y\"/>";
		domContent += "\t<property name=\"moveSpriteIds\" type=\"int[]\" max-num=\"16\" comment=\"�ƶ������ID����\"/>";
		domContent += "\t</object-property>";
		domContent += "\t</res>";
		domContent += "\t</prototype>";
		
		domContent += "\t<prototype req_name=\"SENDMTMESSAGE_REQ\" res_name=\"SENDMTMESSAGE_RES\""; 
		domContent += "\treq_type=\"0x00000003\" res_type=\"0x80000003\">";
		domContent += "\t<req verification-enable='true' verification-keyword='safsaf'>";
		domContent += "\t<property name=\"spriteId\" type=\"int\" comment=\"\"/>";
		domContent += "\t<property name=\"spriteName\" type=\"String\" encoding=\"UTF-8\" comment=\"\"/>";
		domContent += "\t<object name=\"sprite\" type=\"Sprite\" comment=\"\"/>";
		domContent += "\t</req>";
		domContent += "\t<res>";
		domContent += "\t<property name=\"result\" type=\"boolean\" comment=\"�����\"/>";
		domContent += "\t<object name=\"sprite1\" type=\"Sprite\" comment=\"\"/>";
		domContent += "\t<object name=\"sprite2\" type=\"Sprite\" comment=\"\"/>";
		domContent += "\t</res>";
		domContent += "\t</prototype>";
		
		domContent += "</prototypes>";
		
		Document dom = XmlUtil.loadStringWithoutTitle(domContent);
		
		//mc.complie(dom);
	}
	
	
	public void testFactory() throws Exception{
		HashMap<String,String> env = new HashMap<String,String>(); 
		env.put("package", "com.xuanzhi.aaaa");
		env.put("imports", "java.nio.ByteBuffer;com.xuanzhi.tools.transport.*");
		MessageComplier mc = new MessageComplier(env);
		String domContent = "<prototypes version=\"1.0\" author=\"myzdf\">\n";
		domContent += "\t<factory name=\"GameMessageFactory\"/>";
		
		domContent += "\t<prototype req_name=\"ACTIVE_TEST_REQ\" res_name=\"ACTIVE_TEST_RES\""; 
		domContent += "\treq_type=\"0x00000002\" res_type=\"0x80000002\">";
		domContent += "\t<req>";
		domContent += "\t<property name=\"spriteId\" type=\"int\" length=\"4\" comment=\"\"/>";
		domContent += "\t</req>";
		domContent += "\t<res></res>";
		domContent += "\t</prototype>";
	
		domContent += "\t<prototype req_name=\"ACTIVE_TEST_REQ1\" res_name=\"ACTIVE_TEST_RES1\""; 
		domContent += "\treq_type=\"0x00000003\" res_type=\"0x80000003\">";
		domContent += "\t<req>";
		domContent += "\t<property name=\"spriteId\" type=\"int\" length=\"4\" comment=\"\"/>";
		domContent += "\t</req>";
		domContent += "\t<res></res>";
		domContent += "\t</prototype>";
	
		domContent += "</prototypes>";
		
		Document dom = XmlUtil.loadStringWithoutTitle(domContent);
		
		//StringBuffer sb = mc.generateFactory(dom);
		//System.out.println(sb);
		
	}
	
	*/
	
}
