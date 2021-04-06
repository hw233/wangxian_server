package com.fy.engineserver.datasource.buff;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.MultiLanguageTranslateManager;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.text.XmlUtil;

public class BuffTemplateManager {

	private static BuffTemplateManager self;

	public static BuffTemplateManager getInstance() {
		return self;
	}

	protected File configFile;

	public static String encoding = "utf-8";

	protected HashMap<Integer, BuffTemplate> buffTemplates = new HashMap<Integer, BuffTemplate>();
	protected HashMap<String, BuffTemplate> buffTemplateNamesMap = new HashMap<String, BuffTemplate>();
	protected HashMap<Integer, BuffGroup> buffgroups = new HashMap<Integer, BuffGroup>();

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public void init() throws Exception {
		

		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		if (configFile != null) load(configFile);

		self = this;

		ServiceStartRecord.startLog(this);
	}

	/**
	 * <?xml version="1.0" encoding="gbk"?>
	 * <!-- 所有的buff -->
	 * <buff_templates>
	 * 
	 * <!-- 所有的暴击buff -->
	 * <buffs classname="BuffTemplate_BaoJi">
	 * <buff name=Translate.translate.text_3136 timetype="1">
	 * <![CDATA[ json字符串
	 * ]]>
	 * </buff>
	 * <buff name=Translate.translate.text_3137 timetype="2">
	 * <![CDATA[ json字符串
	 * ]]>
	 * </buff>
	 * </buffs>
	 * </buff_templates>
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void load(File file) throws Exception {
		if (file.exists()) {
			InputStream is = new FileInputStream(file);
			loadFromInputStream(is);
			is.close();
		} else {
			throw new Exception("Buff文件不存在");
		}
	}

	public void loadFromInputStream(InputStream is) throws Exception {
		Document dom = XmlUtil.load(is, encoding);
		Element root = dom.getDocumentElement();
		Element bfeles[] = XmlUtil.getChildrenByName(root, "buff");
		HashMap<Integer, BuffTemplate> maps = new HashMap<Integer, BuffTemplate>();
		ArrayList<BuffGroup> buffgroupList = new ArrayList<BuffGroup>();
		HashMap<String, BuffTemplate> namesMap = new HashMap<String, BuffTemplate>();
		HashMap<Integer, BuffGroup> bgs = new HashMap<Integer, BuffGroup>();
		for (int i = 0; i < bfeles.length; i++) {

			HashMap<String, String> map = new HashMap<String, String>();

			Element e = bfeles[i];
			Element eles[] = XmlUtil.getChildrenByName(e, "property");
			for (int j = 0; j < eles.length; j++) {
				String key = XmlUtil.getAttributeAsString(eles[j], "key", null, TransferLanguage.getMap());
				String value = XmlUtil.getAttributeAsString(eles[j], "value", null, TransferLanguage.getMap());
				value = MultiLanguageTranslateManager.languageTranslate(value);
				if (key != null && value != null) {
					if (key.startsWith("expand_")) {
						key = key.substring(7);
					}
					map.put(key, value);
				}
			}

			String className = map.remove("className");
			String categoryName = map.remove("categoryName");
			String groupName = map.remove("groupName");
			int groupId = Integer.parseInt(map.get("groupId"));

			if (buffgroupList != null) {
				boolean exist = false;
				for (BuffGroup bg : buffgroupList) {
					if (bg != null && bg.getGroupId() == groupId) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					BuffGroup bg = new BuffGroup();
					bg.setGroupId(groupId);
					bg.setGroupName(groupName);
					buffgroupList.add(bg);
				}
			}

			Class cl = Class.forName(className);
			Object o = cl.newInstance();
			BuffTemplate bt = (BuffTemplate) o;

			String keys[] = map.keySet().toArray(new String[0]);
			for (int j = 0; j < keys.length; j++) {
				String key = keys[j];
				String value = map.get(keys[j]);
				try {
					setObjectValue(cl, o, key, value);
				} catch (Exception ex) {
					System.out.println("[初始化Buff] [设置属性出错] [" + className + "] [" + categoryName + "] [" + groupName + "] [" + bt.getName() + "] [key:" + key + "] [value:" + value + "] [错误：" + ex.getMessage() + "]");
				}
			}

			if (bt != null) {
				maps.put(bt.getId(), bt);
				namesMap.put(bt.getName(), bt);
				Game.logger.warn("[buff初始化] ["+bt.getId()+"] ["+bt.getIconId()+"]   ["+bt.getName()+"]");
			}
		}
		for (BuffGroup bg : buffgroupList) {
			if (bg != null) {
				bgs.put(bg.getGroupId(), bg);
			}
		}
		buffgroups = bgs;
		buffTemplates = maps;
		buffTemplateNamesMap = namesMap;
	}

	/**
	 * 设置一个对象的属性，需要此对象的属性提供setter和getter方法
	 * @param cl
	 * @param o
	 * @param key
	 * @param value
	 * @throws Exception
	 */

	public static void setObjectValue(Class cl, Object o, String key, String value) throws Exception {
		Field f = null;
		Class clazz = cl;
		while (clazz != null && f == null) {
			try {
				f = clazz.getDeclaredField(key);
			} catch (Exception e) {
				clazz = clazz.getSuperclass();
			}
		}
		if (f == null) {
			throw new Exception("Buff类[" + cl.getName() + "]没有属性[" + key + "]，请检查！");
		}

		Class t = f.getType();
		Method m = null;
		try {
			m = cl.getMethod("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1), new Class[] { t });
		} catch (Exception e) {
			throw new Exception("Buff类[" + cl.getName() + "]属性[" + key + "]的设置方法[" + ("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1) + "(" + t.getName() + ")") + "]，请检查！");
		}
		Object v = null;
		if (t == Byte.class || t == Byte.TYPE) {
			v = Byte.parseByte(value);
		} else if (t == Short.class || t == Short.TYPE) {
			v = Short.parseShort(value);
		} else if (t == Integer.class || t == Integer.TYPE) {
			v = Integer.parseInt(value);
		} else if (t == Long.class || t == Long.TYPE) {
			v = Long.parseLong(value);
		} else if (t == Boolean.class || t == Boolean.TYPE) {
			v = Boolean.parseBoolean(value);
		} else if (t == String.class) {
			value = TransferLanguage.transferString(value);
			v = value;
		} else if (t.isArray()) {
			if (value.trim().length() == 0) {
				v = Array.newInstance(t, 0);
			} else {
				String ss[] = value.split(",");
				t = t.getComponentType();
				v = Array.newInstance(t, ss.length);
				for (int i = 0; i < ss.length; i++) {
					Object v2 = null;
					if (t == Byte.class || t == Byte.TYPE) {
						v2 = Byte.parseByte(ss[i]);
					} else if (t == Short.class || t == Short.TYPE) {
						v2 = Short.parseShort(ss[i]);
					} else if (t == Integer.class || t == Integer.TYPE) {
						v2 = Integer.parseInt(ss[i]);
					} else if (t == Long.class || t == Long.TYPE) {
						v2 = Long.parseLong(ss[i]);
					} else if (t == Boolean.class || t == Boolean.TYPE) {
						v2 = Boolean.parseBoolean(ss[i]);
					} else if (t == String.class) {
						v2 = ss[i];
					} else {
						throw new Exception("Buff类[" + cl.getName() + "]属性[" + key + "]为复杂的数据类型【" + t.getName() + "的数组】，暂时不支持！");
					}
					Array.set(v, i, v2);
				}
			}
		} else {
			throw new Exception("Buff类[" + cl.getName() + "]属性[" + key + "]为复杂的数据类型【" + t.getName() + "】，暂时不支持！");
		}
		m.invoke(o, new Object[] { v });
	}

	/**
	 * 通过Buff的ID得到一个Buff的模板
	 * @return
	 */
	public BuffTemplate getBuffTemplateById(Integer id) {
		return buffTemplates.get(id);
	}

	/**
	 * 通过Buff的名得到一个Buff的模板
	 * @return
	 */
	public BuffTemplate getBuffTemplateByName(String name) {
		return buffTemplateNamesMap.get(name);
	}

	/**
	 * 得到所有buff模板
	 * @return
	 */
	public BuffTemplate[] getAllBuffTemplates() {
		ArrayList<BuffTemplate> btList = new ArrayList<BuffTemplate>();
		if (buffTemplateNamesMap != null && buffTemplateNamesMap.keySet() != null) {
			for (String key : buffTemplateNamesMap.keySet()) {
				if (key != null) {
					btList.add(buffTemplateNamesMap.get(key));
				}
			}
		}
		return btList.toArray(new BuffTemplate[0]);
	}

	/**
	 * 通过BuffGroup的ID得到一个BuffGroup
	 * @return
	 */
	public BuffGroup getBuffGroupById(Integer groupId) {
		return buffgroups.get(groupId);
	}

	public HashMap<Integer, BuffGroup> getGroups() {
		return this.buffgroups;
	}

	public HashMap<Integer, BuffTemplate> getIntKeyTemplates() {
		return this.buffTemplates;
	}

	public HashMap<String, BuffTemplate> getTemplates() {
		return this.buffTemplateNamesMap;
	}
}
