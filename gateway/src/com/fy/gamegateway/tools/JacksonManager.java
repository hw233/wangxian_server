package com.fy.gamegateway.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class JacksonManager {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private static JacksonManager instance;
	
	public static JacksonManager getInstance() {
		if(instance == null) {
			synchronized(JacksonManager.class) {
				if(instance == null) {
					try {
						instance = (JacksonManager)Class.forName(JacksonManager.class.getName()).newInstance();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return instance;
	}
	
	/**
	 * 创建一个ObjectNode
	 * @return
	 */
	public ObjectNode createObjectNode() {
		return mapper.createObjectNode();
	}
	
	/**
	 * 创建一个arrayNode
	 * @return
	 */
	public ArrayNode createArrayNode() {
		return mapper.createArrayNode();
	}
	
	public String toJsonString(Collection collection) {
		try {
			return mapper.writeValueAsString(collection);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public String toJsonString(Map map) {
		try {
			return mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) {
//		JacksonManager jm = new JacksonManager();
//		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
//		params.put("user_id","111");
//		params.put("token","222");
//		System.out.println(jm.toJsonString(params));
		long qian = (long)Double.parseDouble("12.00") * 100;
		System.out.println(qian);
	}
	public void addToArray(ArrayNode array, Object object) {
		JsonNode node = mapper.valueToTree(object);
		array.add(node);
	}
	
	/**
	 * 得到json串的的字节数据
	 * @param node
	 * @return
	 */
	public byte[] toJsonBytes(ObjectNode node) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			JsonGenerator gener = mapper.getJsonFactory().createJsonGenerator(out);
			gener.writeTree(node);
			gener.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
	/**
	 * 得到json字符串
	 * @param node
	 * @return
	 */
	public String toJson(ObjectNode node) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			JsonGenerator gener = mapper.getJsonFactory().createJsonGenerator(out);
			gener.writeTree(node);
			gener.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			return out.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public Object jsonDecodeObject(String json) throws JsonParseException, IOException {
		JsonParser jp = mapper.getJsonFactory().createJsonParser(json);
		Object ob =  mapStreamToObject(jp, jp.nextToken());
		return ob;
	}
	
	private Object mapStreamToObject(JsonParser jp, JsonToken t) throws IOException {
		switch (t) {
		case VALUE_FALSE:
			return Boolean.FALSE;
		case VALUE_TRUE:
			return Boolean.TRUE;
		case VALUE_STRING:
			return jp.getText();
		case VALUE_NUMBER_INT:
		case VALUE_NUMBER_FLOAT:
			return jp.getNumberValue();
		case VALUE_EMBEDDED_OBJECT:
			return jp.getEmbeddedObject();
		case START_ARRAY: {
			ArrayList<Object> kids = new ArrayList<Object>(4);
			while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
				kids.add(mapStreamToObject(jp, t));
			}
			return kids;
		}
		case START_OBJECT: {
			// HashMap<String, Object> kids = new LinkedHashMap<String, Object>();
			HashMap<String, Object> kids = new HashMap<String, Object>(8);
			while ((t = jp.nextToken()) != JsonToken.END_OBJECT) {
				kids.put(jp.getCurrentName(), mapStreamToObject(jp, jp.nextToken()));
			}
			return kids;
		}
		case VALUE_NULL : {
			return null;
		}
		default:
			throw new RuntimeException("Unexpected token: " + t);
		}
	}
	
	
}
