package com.sqage.stat.httputil.jsonutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 * 
 * @version 创建时间：Sep 04, 2012 10:41:52 AM
 * 
 */
public class JacksonManager {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private static JacksonManager instance;
	
	public static JacksonManager getInstance() {
		if(instance == null) {
			synchronized(JacksonManager.class) {
				if(instance == null) {
					try {
						instance = (JacksonManager)Class.forName(JacksonManager.class.getName()).newInstance();
						mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return instance;
	}
	
	 /**  
     * 将一个json字串转为list  
     * @param props  
     * @return  
     */  
    public static List jsonToList(String strJson,Class class1){   
        if (strJson == null || strJson.equals(""))   
            return new ArrayList();  
        JSONArray jsonArray = JSONArray.fromObject(strJson);   
        List list = (List) JSONArray.toList(jsonArray,class1);   
        return list;   
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
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String toJsonString(Map map) {
		try {
			return mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String toJsonString(Object obj) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			JsonGenerator gener = mapper.getJsonFactory().createJsonGenerator(out);
			gener.writeObject(obj);
			gener.close();
			return out.toString("UTF-8");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
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
			e.printStackTrace();
		}
		return "";
	}
	
	public Map jsonDecodeMap(String json) {
		JsonParser jsonParser = null;
		try {
			jsonParser = mapper.getJsonFactory().createJsonParser(json);
			jsonParser.nextToken();
			Map map = new HashMap();
			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {   
				jsonParser.nextToken();   
				map.put(jsonParser.getCurrentName(), jsonParser.getText());   
			}
			return map;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				jsonParser.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	 /**  
     * 将一个json字串转为list  
     * @param props  
     * @return  
     */  
  
	
	public List jsonDecodeList(String json) {
		JsonParser jsonParser = null;
		try {
			jsonParser = mapper.getJsonFactory().createJsonParser(json);
			if (jsonParser.nextToken() == JsonToken.START_ARRAY) {
				List list = new ArrayList();
				while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
					System.out.println("list-text"+jsonParser.getText());
					list.add(jsonParser.getText());
				}
				return list;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				jsonParser.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Object jsonDecodeEntity(String json,Class class1) {
		JsonParser jsonParser = null;
		try {
			jsonParser = mapper.getJsonFactory().createJsonParser(json);
			return jsonParser.readValueAs(class1);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				jsonParser.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
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
	
	public static void main(String args[]) throws JsonParseException, IOException {
		JacksonManager jm = JacksonManager.getInstance();
		List glist =new ArrayList<String []>();
		for (int j=0;j<3;j++){
		String [] ls=new String [5];
		for(int i=0;i<5;i++)
		{
			ls[i]=j+""+i;
		}
		glist.add(ls);
		}
		
		System.out.println(jm.toJsonString(glist));

		List list = jm.jsonDecodeList(jm.toJsonString(glist));
		
		//jm.jsonToList(jm.toJsonString(glist),String[]);
		System.out.println("size:" + list.size());
		
	}
}
