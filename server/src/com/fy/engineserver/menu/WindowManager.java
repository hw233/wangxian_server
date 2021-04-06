package com.fy.engineserver.menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.OPTION_INPUT_REQ;
import com.fy.engineserver.message.OPTION_SELECT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 窗口管理
 * 
 * 
 */
public class WindowManager implements Runnable {

	static WindowManager self;

	public static String fileName = "C:\\Documents and Settings\\Administrator\\桌面\\windows.xls";
	protected File configFile;

	protected int maxWindowId = 0;

	Map<String, Option> optionMap = new HashMap<String, Option>();
	// Map<Integer, MenuWindow> windowMap = new HashMap<Integer, MenuWindow>();
	LinkedHashMap<Integer, MenuWindow> windowMap = new LinkedHashMap<Integer, MenuWindow>();

	Thread thread;
	
	public static int LMLV = 0;

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public static WindowManager getInstance() {
		return self;
	}

	public void init() throws Exception {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		if (configFile == null) {
			System.out.println("【警告】菜单窗口管理系统没有配置任何菜单的信息，请及时配置！");
		} else {
			java.io.FileInputStream input = new java.io.FileInputStream(configFile);
			// loadFrom(input);

			loadExcel(input);
			input.close();

			// com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter adapter = com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter.getInstance();
			// adapter.addListener(configFile, new ConfigFileChangedListener() {
			// public void fileChanged(File file) {
			// try {
			// java.io.FileInputStream input = new java.io.FileInputStream(file);
			// // loadFrom(input);
			// loadExcel(input);
			// input.close();
			// System.out.println("【菜单窗口管理】重新加载配置文件成功");
			// } catch (Exception e) {
			// System.out.println("【菜单窗口管理】重新加载配置文件失败：");
			// e.printStackTrace(System.out);
			// }
			// }
			//
			// });
		}

		MenuWindow mws[] = this.getWindows();
		for (MenuWindow mw : mws) {
			mw.invalidTime = 0;
			if (mw.getId() > maxWindowId) {
				maxWindowId = mw.getId();
			}
		}
		maxWindowId++;

		thread = new Thread(this, "Menu-Window-Check-Thread");
		thread.start();
		self = this;

		ServiceStartRecord.startLog(this);
	}

	public void run() {

		while (Thread.currentThread().isInterrupted() == false) {
			try {
				MenuWindow mws[] = this.getWindows();
				for (MenuWindow mw : mws) {
					if (mw.invalidTime > 0 && mw.invalidTime < com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
						windowMap.remove(mw.getId());
						// Game.logger.warn("[移除窗口] [ID:"+mw.getId()+"]");
						if (Game.logger.isWarnEnabled()) Game.logger.warn("[移除窗口] [ID:{}]", new Object[] { mw.getId() });
					}
				}

				Thread.sleep(1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建一个窗口，并保持一段时间
	 * @param keepTime
	 *            一段时间，以秒为单位
	 * @return
	 */
	public synchronized MenuWindow createTempMenuWindow(int keepTime) {
		MenuWindow mw = new MenuWindow();
		mw.setId(this.maxWindowId++);
		windowMap.put(mw.getId(), mw);
		mw.setTitle(Translate.text_5507);
		mw.setDescriptionInUUB(Translate.text_370);
		mw.invalidTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + keepTime * 1000;
		return mw;
	}

	/**
	 * 配置文件格式如下：
	 * 
	 * <window-manager>
	 * <function-options>
	 * <function-option>
	 * <property name="className" value="com.fy.engineserver.menu.Option_xxxxx"/>
	 * <property name="optionId" value="49923840394"/>
	 * <property name="text" value=Translate.translate.text_5508/>
	 * <property name="xxx" value="xxxx"/>
	 * <property name="xxx" value="xxxx"/>
	 * </function-option>
	 * <function-option>
	 * <property name="className" value="com.fy.engineserver.menu.Option_xxxxx"/>
	 * <property name="optionId" value="49923840394"/>
	 * <property name="text" value=Translate.translate.text_5508/>
	 * <property name="xxx" value="xxxx"/>
	 * <property name="xxx" value="xxxx"/>
	 * </function-option>
	 * <function-option>
	 * <property name="className" value="com.fy.engineserver.menu.Option_xxxxx"/>
	 * <property name="optionId" value="49923840394"/>
	 * <property name="text" value=Translate.translate.text_5508/>
	 * <property name="xxx" value="xxxx"/>
	 * <property name="xxx" value="xxxx"/>
	 * </function-option>
	 * </function-options>
	 * 
	 * <windows>
	 * <window id='1' title='xxxxx' showTask='true'>
	 * <options>
	 * <!-- 其中对于oType==2或者3 optionId无意义 对于oType==1 oId无意义 -->
	 * <option optionId='' oType='' oId='' text='' iconId='' color=''/>
	 * <option optionId='' oType='' oId='' text='' iconId='' color=''/>
	 * <option optionId='' oType='' oId='' text='' iconId='' color=''/>
	 * </options>
	 * <descriptionInUUB>[color=xxxxx]大厦尽快发垃圾分开来看[/color]</descriptionInUUB>
	 * </window>
	 * </windows>
	 * </window-manager>
	 * @param file
	 * @throws Exception
	 */
	public void loadFrom(java.io.InputStream input) throws Exception {
		Document dom = XmlUtil.load(input);
		Element root = dom.getDocumentElement();

		Element eles[] = XmlUtil.getChildrenByName(XmlUtil.getChildByName(root, "function-options"), "function-option");
		ArrayList<Option> al = new ArrayList<Option>();

		for (int i = 0; i < eles.length; i++) {
			Element e = eles[i];
			HashMap<String, String> map = new HashMap<String, String>();
			Element pes[] = XmlUtil.getChildrenByName(e, "property");
			for (int j = 0; j < pes.length; j++) {
				String name = XmlUtil.getAttributeAsString(pes[j], "name", null, TransferLanguage.getMap());
				String value = XmlUtil.getAttributeAsString(pes[j], "value", null, TransferLanguage.getMap());
				if (name != null && value != null) {
					map.put(name, value);
				}
			}

			String className = map.remove("className");
			Class<?> cl = Class.forName(className);
			Option option = (Option) cl.newInstance();
			String keys[] = map.keySet().toArray(new String[0]);
			for (int j = 0; j < keys.length; j++) {
				String value = map.get(keys[j]);

				setObjectValue(cl, option, keys[j], value);
			}

			al.add(option);
		}

		eles = XmlUtil.getChildrenByName(XmlUtil.getChildByName(root, "windows"), "window");

		LinkedHashMap<Integer, MenuWindow> _windowMap = new LinkedHashMap<Integer, MenuWindow>();

		for (int i = 0; i < eles.length; i++) {
			Element e = eles[i];
			MenuWindow mw = new MenuWindow();
			mw.setId(XmlUtil.getAttributeAsInteger(e, "id"));
			mw.setTitle(XmlUtil.getAttributeAsString(e, "title", "NO_TITLE", TransferLanguage.getMap()));
			mw.setShowTask(XmlUtil.getAttributeAsBoolean(e, "showTask", true));
			mw.setDescriptionInUUB(XmlUtil.getChildText(e, "descriptionInUUB", TransferLanguage.getMap()));
			mw.setWidth(XmlUtil.getAttributeAsInteger(e, "width", 0));
			mw.setHeight(XmlUtil.getAttributeAsInteger(e, "height", 0));

			Element pes[] = XmlUtil.getChildrenByName(e, "option");
			ArrayList<Option> oList = new ArrayList<Option>();
			for (int j = 0; j < pes.length; j++) {
				String opiotnId = XmlUtil.getAttributeAsString(pes[j], "optionId", "", TransferLanguage.getMap());
				String text = XmlUtil.getAttributeAsString(pes[j], "text", "", TransferLanguage.getMap());
				int oId = XmlUtil.getAttributeAsInteger(pes[j], "oId", 0);
				int oType = XmlUtil.getAttributeAsInteger(pes[j], "oType");
				String iconId = XmlUtil.getAttributeAsString(pes[j], "iconId", "", null);
				int color = XmlUtil.getAttributeAsInteger(pes[j], "color", 0);

				Option option = null;
				if (oType == Option.OPTION_TYPE_SERVER_FUNCTION) {
					for (int k = 0; k < al.size(); k++) {
						Option op = al.get(k);
						if (op.getOType() == oType && op.optionId.equals(opiotnId)) {
							option = op;
						}
					}
					if (option == null) {
						throw new Exception("菜单窗口[" + mw.getTitle() + "]配置的服务器功能选项[" + opiotnId + "," + text + "]不存在！");
					}
				} else {
					option = new Option();
					option.setOptionId(opiotnId);
					option.setText(text);
					option.setOId(oId);
					option.setOType((byte) oType);
					option.setIconId(iconId);
					option.setColor(color);
				}
				oList.add(option);
			}
			mw.setOptions(oList.toArray(new Option[0]));

			_windowMap.put(mw.getId(), mw);
		}

		windowMap = _windowMap;
	}

	/**
	 * 设置一个对象的属性，需要此对象的属性提供setter和getter方法
	 * @param cl
	 * @param o
	 * @param key
	 * @param value
	 * @throws Exception
	 */

	public static void setObjectValue(Class<?> cl, Object o, String key, String value) throws Exception {
		Field f = null;
		Class<?> clazz = cl;
		while (clazz != null && f == null) {
			try {
				f = clazz.getDeclaredField(key);
			} catch (Exception e) {
				clazz = clazz.getSuperclass();
			}
		}
		if (f == null) {
			throw new Exception("Option类[" + cl.getName() + "]没有属性[" + key + "]，请检查！");
		}

		Class<?> t = f.getType();
		Method m = null;
		try {
			m = cl.getMethod("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1), new Class[] { t });
		} catch (Exception e) {
			throw new Exception("Option类类[" + cl.getName() + "]属性[" + key + "]的设置方法[" + ("set" + java.lang.Character.toUpperCase(key.charAt(0)) + key.substring(1) + "(" + t.getName() + ")") + "]，请检查！");
		}
		Object v = null;
		try {
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
				v = value;
			} else if (t.isArray()) {
				if (value.trim().length() == 0) {
					v = Array.newInstance(t, 0);
				} else {
					String ss[] = value.split(",");
					ArrayList<String> al = new ArrayList<String>();
					for (int i = 0; i < ss.length; i++) {
						if (ss[i].trim().length() > 0) {
							al.add(ss[i].trim());
						}
					}
					ss = al.toArray(new String[0]);
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
							throw new Exception("Option类类[" + cl.getName() + "]属性[" + key + "]为复杂的数据类型【" + t.getName() + "的数组】，暂时不支持！");
						}
						Array.set(v, i, v2);
					}
				}
			} else {
				throw new Exception("Option类类[" + cl.getName() + "]属性[" + key + "]为复杂的数据类型【" + t.getName() + "】，暂时不支持！");
			}
		} catch (NumberFormatException e) {
			throw new Exception("Option类类[" + cl.getName() + "]属性[" + key + "]为数据【" + value + "】解析出错，请检查配置文件！");
		}
		m.invoke(o, new Object[] { v });
	}

	/**
	 * 通过Id获得窗口
	 * @param id
	 * @return
	 */
	public MenuWindow getWindowById(int id) {
		return windowMap.get(id);
	}

	public MenuWindow[] getWindows() {
		return windowMap.values().toArray(new MenuWindow[0]);
	}

	/**
	 * 处理玩家选择某个窗口中的某个选项
	 * 
	 * @param game
	 * @param player
	 * @param req
	 */
	public void doSelect(Game game, Player player, OPTION_INPUT_REQ req) {
		MenuWindow mw = getWindowById(req.getWId());
		if (mw == null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.窗口超时);
			player.addMessageToRightBag(hreq);
		}

		Option options[] = mw.getOptions();
		int index = 0;

		if (index < 0 || index >= options.length) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_5511);
			player.addMessageToRightBag(hreq);
		}
		Option op = options[index];
		if (op.getOType() == Option.OPTION_TYPR_CLIENT_FUNCTION) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_5512);
			player.addMessageToRightBag(hreq);
		}

		if (op.getOType() == Option.OPTION_TYPR_WAITTING_NEXT_WINDOW) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_5513);
			player.addMessageToRightBag(hreq);
		} else if (op.getOType() == Option.OPTION_TYPE_SERVER_FUNCTION) {
			op.doInput(game, player, req.getInputContent());
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_5514 + op.getOType() + Translate.text_5515);
			player.addMessageToRightBag(hreq);
		}
	}

	/**
	 * 处理玩家选择某个窗口中的某个选项
	 * 
	 * @param game
	 * @param player
	 * @param req
	 */
	public void doSelect(Game game, Player player, OPTION_SELECT_REQ req) {
		MenuWindow mw = getWindowById(req.getWId());
		if (mw == null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.窗口超时);
			player.addMessageToRightBag(hreq);
			return;
		}

		Option options[] = mw.getOptions();
		int index = req.getIndex();
		if (index < 0 || index >= options.length) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_5511);
			player.addMessageToRightBag(hreq);
		}
		Option op = options[index];
		if (op.getOType() == Option.OPTION_TYPR_CLIENT_FUNCTION) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_5512);
			player.addMessageToRightBag(hreq);
		}

		if (op.getOType() == Option.OPTION_TYPR_WAITTING_NEXT_WINDOW) {
			MenuWindow mw2 = getWindowById(op.getOId());
			if (mw2 != null) {
				MenuWindow newMW = createTempMenuWindow(600);

				newMW.setShowTask(mw2.isShowTask());
				newMW.setNpcId(req.getNpcId());
				newMW.setTitle(mw2.getTitle());
				newMW.setDescriptionInUUB(mw2.getDescriptionInUUB());

				Option[] newoptions = new Option[mw2.getOptions().length];
				for (int i = 0; i < mw2.getOptions().length; i++) {
					newoptions[i] = mw2.getOptions()[i].copy(game, player);

				}
				newMW.setOptions(newoptions);
				newMW.setWidth(mw2.getWidth());
				newMW.setHeight(mw2.getHeight());

				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(req.getSequnceNum(), newMW, newMW.getOptions());
				player.addMessageToRightBag(res);
			} else {
				mw2 = new MenuWindow();
				mw2.setId(0);
				mw2.setTitle(mw.getTitle());
				mw2.setDescriptionInUUB(Translate.text_5516 + player.getName() + Translate.text_5517 + op.getText() + Translate.text_5518);
				mw2.setNpcId(req.getNpcId());
				mw2.setShowTask(false);

				Option option = new Option();
				option.setColor(0xfffff);
				option.setText(Translate.text_5519);
				option.setOType((byte) Option.OPTION_TYPR_WAITTING_NEXT_WINDOW);
				option.setOId(mw.getId());
				mw2.setOptions(new Option[] { option });

				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(req.getSequnceNum(), mw2, mw2.getOptions());
				player.addMessageToRightBag(res);
			}
		} else if (op.getOType() == Option.OPTION_TYPE_SERVER_FUNCTION) {
			op.doSelect(game, player);
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_5514 + op.getOType() + Translate.text_5515);
			player.addMessageToRightBag(hreq);
		}
	}

	public void loadExcel(InputStream input) throws Exception {

		// File file = new File(fileName);
		HSSFWorkbook workbook = null;
		try {
			// InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(input);
			workbook = new HSSFWorkbook(pss);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// LoadWindows lw = new LoadWindows();
		// lw.loadOptions(workbook,optionMap);
		// lw.loadWindows(workbook,windowMap,optionMap);
		this.windowMap = new LinkedHashMap<Integer, MenuWindow>();
		LoadWindows.loadWindows(workbook, windowMap);
	}

	public LinkedHashMap<Integer, MenuWindow> getWindowMap() {
		return windowMap;
	}

	public void setWindowMap(LinkedHashMap<Integer, MenuWindow> windowMap) {
		this.windowMap = windowMap;
	}

	// public static void main(String[] args) {
	// WindowManager wm = new WindowManager();
	// wm.loadExcel();
	// System.out.println("success");
	// }
}
