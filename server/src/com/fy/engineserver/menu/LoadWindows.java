package com.fy.engineserver.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.cave.Option_Cave_CloseDoor;
import com.fy.engineserver.menu.cave.Option_Cave_OptionDoor;
import com.fy.engineserver.newtask.service.TaskManager;

public class LoadWindows {

	public static int windows = 0;
	public static int optionshell = 1;
	public static String CLASSPATH = "com.fy.engineserver.menu.";

	public static int 窗口ids = 0;
	public static int 窗口标题s = 1;
	public static int 窗口描述s = 2;
	public static int 对应npdIds = 3;
	public static int 是否要显示NPC身上的任务s = 4;
	public static int 高度s = 5;
	public static int 宽度s = 6;
	public static int 执行指令是否关闭掉自己s = 7;
	public static int option = 111;
	public static int optionId = 112;

	public static int optionclass = 8;
	public static int text = 9;
	public static int iconId = 10;
	public static int color = 11;
	public static int 参数名 = 12;
	public static int 参数值 = 13;
	public static String startStr = "<f size='40' color='#ffff00'  textUnderLine='true' onclick='true' enaleBgcolor='false'>";
	public static String startContentStr = "<f size='40' enaleBgcolor='false'>";
	public static String compareStr = "<f";
	public static String endStr = "</f>";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String fileName = "C:\\Program Files\\SecureCRT\\download\\windows.xls";
		File file = new File(fileName);
		HSSFWorkbook workbook = null;
		try {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			workbook = new HSSFWorkbook(pss);
			Map<Integer, MenuWindow> map = new HashMap<Integer, MenuWindow>();
			loadWindows(workbook, map);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {

		}
	}

	public static long lastId = 0;

	public static void loadWindows(HSSFWorkbook workbook, Map<Integer, MenuWindow> windowMap) throws Exception {
		HSSFSheet sheet = null;
		for (int j = 0; j < workbook.getNumberOfSheets() - 1; j++) {
			sheet = workbook.getSheetAt(j);
			if (sheet == null) continue;
			int rows = sheet.getPhysicalNumberOfRows();
			MenuWindow mw = null;
			List<Option> optionList = null;
			for (int r = 1; r < rows; r++) {

				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					int 窗口id = 0;
					HSSFCell cell = row.getCell(窗口ids);
					try {
						窗口id = (int) cell.getNumericCellValue();
						mw = windowMap.get(窗口id);
						if (mw != null) {
							lastId = 窗口id;
							String className = null;
							cell = row.getCell(optionclass);

							className = CLASSPATH + cell.getStringCellValue().trim();
							
							Class cl = null;
							Option option = null;
							try {
								cl = Class.forName(className);
								try {
									option = (Option) cl.newInstance();
								} catch (InstantiationException e) {
									throw e;
								} catch (IllegalAccessException e) {
									throw e;
								}
							} catch (ClassNotFoundException e) {
								throw new Exception("解析option出错，没有这个" + className + "option");
							}

							String text = null;
							cell = row.getCell(LoadWindows.text);

							try {
								text = (cell.getStringCellValue());
								if(text!=null && !text.toLowerCase().contains(compareStr)){
									if (!(option instanceof Option_Cave_OptionDoor || option instanceof Option_Cave_CloseDoor)) {
										text = startStr +  text + endStr;
									}
								}
								option.setText(text);
							} catch (Exception e2) {
								System.out.println("loadwindows111>>" + "[" + r + "]" + "  " + className);
							}

							String iconId = "";
							cell = row.getCell(LoadWindows.iconId);
							try {
								iconId = String.valueOf((long) cell.getNumericCellValue());
								option.setIconId(iconId);
							} catch (Exception e) {

							}

							int color = 0xffffff;
							cell = row.getCell(LoadWindows.color);
							try {
								color = (int) cell.getNumericCellValue();
								option.setColor(color);
							} catch (Exception e) {

							}

							String parameterName = null;
							cell = row.getCell(LoadWindows.参数名);

							if (cell != null) {
								parameterName = cell.getStringCellValue();
								if (parameterName.trim().equals("")) {
									parameterName = null;
								}
							}

							String parameterValue = null;
							cell = row.getCell(LoadWindows.参数值);
							try {
								if (cell != null) {
									parameterValue = cell.getStringCellValue();
									if (parameterValue.trim().equals("")) {
										parameterValue = null;
									}
								}
							} catch (IllegalStateException e) {
								try {
									parameterValue = "" + cell.getNumericCellValue();
								} catch (IllegalStateException e1) {
									parameterValue = "" + cell.getBooleanCellValue();
								}
							} catch (Exception e) {
								throw e;
							}
							if (parameterName != null && parameterValue != null) {
								String[] names = parameterName.split("@");
								String[] values = parameterValue.trim().split("@");
								if (names.length == values.length) {
									for (int i = 0; i < names.length; i++) {
										values[i] = (values[i]);
										setObjectValue(cl, option, names[i], values[i]);
									}
								}
							}
							// 如果是需要初始化的option,则调用初始化方法
							if (option instanceof NeedInitialiseOption) {
								((NeedInitialiseOption) option).init();
								TaskManager.logger.warn("[窗口系统初始化] [NeedInitialiseOption] [class:" + option.getClass().toString() + "]");
							}
							Option[] ops = mw.getOptions();
							optionList = new ArrayList<Option>();
							if (ops != null) {
								for (Option o : ops) {
									optionList.add(o);
								}
							}
							optionList.add(option);
							mw.setOptions(optionList.toArray(new Option[0]));

							windowMap.put(mw.getId(), mw);
							continue;
						} else {
							mw = new MenuWindow();
							mw.setId(窗口id);
						}
					} catch (Exception e) {
						System.out.println(e);
						if(Game.logger.isWarnEnabled()){
							Game.logger.warn("[窗口加载异常] [窗口id："+窗口id+"] [页："+j+"] [行:"+r+"] ["+e+"]");
						}
						System.out.println("################################[窗口加载异常] [lastId:" + lastId + "] [页："+j+"] [行:"+r+"]################################");
						throw e;
					}

					String 窗口标题 = "";
					cell = row.getCell(窗口标题s);
					try {
						窗口标题 = cell.getStringCellValue();
						窗口标题 = (窗口标题);
						if (窗口标题 != null) {
							mw.setTitle(窗口标题);
						} else {
							mw.setTitle("");
						}

					} catch (Exception e) {

					}

					String 窗口描述 = "";
					cell = row.getCell(窗口描述s);
					try {
						窗口描述 = cell.getStringCellValue();
						if (窗口描述 != null) {
							mw.setDescriptionInUUB(窗口描述);
						} else {
							mw.setDescriptionInUUB("");
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					int 对应npdId = -1;
					cell = row.getCell(对应npdIds);
					try {
						对应npdId = (int) cell.getNumericCellValue();
						mw.setNpcId(对应npdId);
					} catch (Exception e) {
						mw.setNpcId(对应npdId);
					}

					boolean 是否要显示NPC身上的任务 = true;
					cell = row.getCell(LoadWindows.是否要显示NPC身上的任务s);
					try {
						是否要显示NPC身上的任务 = cell.getBooleanCellValue();
						mw.setShowTask(是否要显示NPC身上的任务);
					} catch (Exception e) {
						mw.setShowTask(是否要显示NPC身上的任务);
					}

					int 高度 = 0;
					cell = row.getCell(LoadWindows.高度s);
					try {
						高度 = (int) cell.getNumericCellValue();
						mw.setHeight(高度);
					} catch (Exception e) {
						mw.setHeight(高度);

					}

					int 宽度 = 0;
					cell = row.getCell(LoadWindows.宽度s);
					try {
						宽度 = (int) cell.getNumericCellValue();
						mw.setWidth(宽度);
					} catch (Exception e) {
						mw.setWidth(宽度);
					}

					boolean 执行指令是否关闭掉自己 = true;
					cell = row.getCell(LoadWindows.执行指令是否关闭掉自己s);
					try {
						执行指令是否关闭掉自己 = cell.getBooleanCellValue();
						mw.setDestroy(执行指令是否关闭掉自己);
					} catch (Exception e) {
						mw.setDestroy(执行指令是否关闭掉自己);
					}

					String className = null;
					cell = row.getCell(optionclass);

					className = CLASSPATH + cell.getStringCellValue();

					Class cl = null;
					Option option = null;
					if (className != null) {
						try {
							cl = Class.forName(className);
							try {
								option = (Option) cl.newInstance();
							} catch (InstantiationException e) {
								throw e;
							} catch (IllegalAccessException e) {
								throw e;
							}
						} catch (ClassNotFoundException e) {
							// e.printStackTrace();
							// continue;
							throw new Exception("解析option出错，没有这个" + className + "option");
						}
					}

					String text = null;
					cell = row.getCell(LoadWindows.text);

					try {
						text = cell.getStringCellValue();
						text = (text);
						option.setText(text);
					} catch (Exception e2) {
						System.out.println("loadwindows>>" + "[" + r + "]" + "  " + className);
					}

					String iconId = "";
					cell = row.getCell(LoadWindows.iconId);
					try {
						iconId = String.valueOf((long) cell.getNumericCellValue());
						option.setIconId(iconId);
					} catch (Exception e) {
						option.setIconId(iconId);
					}

					int color = 0xffffff;
					cell = row.getCell(LoadWindows.color);
					try {
						color = (int) cell.getNumericCellValue();
						option.setColor(color);
					} catch (Exception e) {
						option.setColor(color);
					}

					String parameterName = null;
					cell = row.getCell(LoadWindows.参数名);
					try {
						if (cell != null) parameterName = cell.getStringCellValue();
					} catch (Exception e) {
						e.printStackTrace();
					}

					String parameterValue = null;
					cell = row.getCell(LoadWindows.参数值);
					try {
						if (cell != null) parameterValue = cell.getStringCellValue();
					} catch (IllegalStateException e) {
						try {
							parameterValue = "" + cell.getNumericCellValue();
						} catch (IllegalStateException e1) {
							parameterValue = "" + cell.getBooleanCellValue();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (parameterName != null && parameterValue != null) {
						String[] names = parameterName.split("@");
						String[] values = parameterValue.trim().split("@");
						if (names.length == values.length) {
							for (int i = 0; i < names.length; i++) {
								try {
									values[i] = (values[i]);
									setObjectValue(cl, option, names[i], values[i]);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					if (option instanceof NeedInitialiseOption) {
						((NeedInitialiseOption) option).init();
						TaskManager.logger.warn("[窗口系统初始化] [NeedInitialiseOption] [class:" + option.getClass().toString() + "]");
					}
					mw.setOptions(new Option[] { option });
					if (mw != null) {
						windowMap.put(mw.getId(), mw);
					}
					if(Game.logger.isWarnEnabled()){
						Game.logger.warn("[窗口加载完毕] [窗口id："+窗口id+"] [页："+j+"] [行:"+r+"]");
					}
				}
			}

		}
	}

	private static void setObjectValue(Class<?> cl, Object o, String key, String value) throws Exception {
		if(key.isEmpty() && value.isEmpty()){
			return;
		}
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
				v = Byte.parseByte(value.split("\\.")[0]);
			} else if (t == Short.class || t == Short.TYPE) {
				v = Short.parseShort(value.split("\\.")[0]);
			} else if (t == Integer.class || t == Integer.TYPE) {
				v = Integer.parseInt(value.split("\\.")[0]);
			} else if (t == Long.class || t == Long.TYPE) {
				v = Long.parseLong(value.split("\\.")[0]);
			} else if (t == Boolean.class || t == Boolean.TYPE) {
				v = Boolean.parseBoolean(value);
			} else if (t == String.class) {
				v = value;
			} else if (t.isArray()) {
				if (value.trim().length() == 0) {
					v = Array.newInstance(t, 0);
				} else {
					String ss[] = value.split(":");
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

}
