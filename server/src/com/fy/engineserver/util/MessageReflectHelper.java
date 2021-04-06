package com.fy.engineserver.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.message.JIAZU_APPLY_RES;
import com.xuanzhi.tools.transport.RequestMessage;

class TestMessage implements RequestMessage {
	long seqNum;

	byte by;
	boolean b;
	char c;
	short s;
	int i;
	long l;
	float f;
	double d;
	/**
	 * 字符数组
	 */
	byte aby[];
	boolean ab[];
	char ac[];
	short as[];
	int ai[];
	long al[];
	float af[];
	double ad[];

	String key;
	String[] st;

	int length;

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("byte=" + this.by);
		buffer.append("\nboolean=" + this.b);
		buffer.append("\nchar=" + this.c);
		buffer.append("\nint=" + this.i);
		buffer.append("\nshort=" + this.s);
		buffer.append("\nlong=" + this.l);
		buffer.append("\nfloat=" + this.f);
		buffer.append("\ndouble=" + this.d);
		buffer.append("\nString=" + this.key);
		buffer.append("\nbyte array[");
		for (byte b : this.aby) {
			buffer.append("\t").append(b);
		}
		buffer.append("]\n");

		buffer.append("boolean array[");
		for (boolean b : this.ab) {
			buffer.append("\t").append(b);
		}
		buffer.append("]\n");
		buffer.append("short array[");
		for (short b : this.as) {
			buffer.append("\t").append(b);
		}
		buffer.append("]\n");
		buffer.append("int array[");
		for (int b : this.ai) {
			buffer.append("\t").append(b);
		}
		buffer.append("]\n");
		buffer.append("long array[");
		for (long b : this.al) {
			buffer.append("\t").append(b);
		}
		buffer.append("]\n");
		buffer.append("float array[");
		for (float b : this.af) {
			buffer.append("\t").append(b);
		}
		buffer.append("]\n");
		buffer.append("double array[");
		for (double b : this.ad) {
			buffer.append("\t").append(b);
		}
		buffer.append("]\n");
		buffer.append("String array[");
		for (String b : this.st) {
			buffer.append("\t").append(b);
		}
		buffer.append("]\n");

		return buffer.toString();
	}

	public TestMessage(long seqNum, byte[] a, int i, int j) {

	}

	/*
	 * public TestMessage(int t) { this.length = t; }
	 */
	public TestMessage(byte a, short b, char c, int d, float e, double f, long g, boolean h, String i, byte[] j, short k[], char l[], int m[], float n[], double o[], long p[], String q[],
			boolean s[], Integer t[]) {
		// System.arraycopy(p, 0, t, 0, p.length);

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return this.length;
	}

	@Override
	public String getSequenceNumAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getSequnceNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int writeTo(ByteBuffer arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

}

public class MessageReflectHelper {
	public synchronized static Object createNewInstance(String className, Map<String, String[]> map) {
		try {
			Class cls = Class.forName(className);
			Field field[] = cls.getDeclaredFields();
			// Message message=(Message)cls.newInstance();

			Class<?> arg[] = { long.class, byte[].class, int.class, int.class };
			Constructor cm = null;
			Constructor cs = cls.getConstructor(arg);
			for (Constructor con : cls.getConstructors()) {
				if (cs.equals(con)) {
					continue;
				}
				cm = con;
				break;
			}

			Type p[] = cm.getGenericParameterTypes();
			Object ss = Array.newInstance(Object.class, p.length);
			int m = 0;
			for (int i = 0; i < p.length; i++) {
				String type = p[i].toString();
				if (type.equals("int") || type.equals("double") || type.equals("float") || type.equals("long")) {
					((Object[]) ss)[i] = 0;
					m++;
				} else if (type.equals("char")) {
					((Object[]) ss)[i] = '0';
					m++;
				} else if (p[i].toString().equals("boolean")) {
					((Object[]) ss)[i] = false;
					m++;
				} else if (type.equals("short")) {
					short s = 0;
					((Object[]) ss)[i] = s;
					m++;
				} else if (type.equals("byte")) {
					byte s = 0;
					((Object[]) ss)[i] = s;
					m++;
				}

			}

			/*
			 * System.out.println("m=" + m); for (Type p1 : p) {
			 * System.out.println(p1.toString()); }
			 */
			Object t = cm.newInstance((Object[]) ss);
			int sum = 0;

			for (Field f : field) {
				int d = f.getModifiers();
				if (Modifier.isStatic(d) || f.getName().equals("seqNum")) {
					continue;
				} else {

					sum++;
					if (map.get(f.getName()) != null) {
						f.setAccessible(true);

						// 如果数组
						String type = f.getType().toString();
						String values[] = map.get(f.getName());
						if (type.contains("[")) {
							if (type.equals("class [I")) {
								int ivalue[] = new int[values.length];
								for (int i = 0; i < ivalue.length; i++) {
									ivalue[i] = Integer.parseInt(values[i]);
								}
								f.set(t, ivalue);
							} else if (type.equals("class [J")) {
								long lvalue[] = new long[values.length];
								for (int i = 0; i < lvalue.length; i++) {
									lvalue[i] = Long.parseLong(values[i]);
								}
								f.set(t, lvalue);
							} else if (type.equals("class [F")) {
								float fvalue[] = new float[values.length];
								for (int i = 0; i < fvalue.length; i++) {
									fvalue[i] = Float.parseFloat(values[i]);
								}
								f.set(t, fvalue);
							} else if (type.equals("class [C")) {
								char dvalue[] = new char[values.length];
								for (int i = 0; i < dvalue.length; i++) {
									dvalue[i] = values[i].toCharArray()[0];
								}

								f.set(t, dvalue);
							} else if (type.equals("class [D")) {
								double dvalue[] = new double[values.length];
								for (int i = 0; i < dvalue.length; i++) {
									dvalue[i] = Double.parseDouble(values[i]);
								}
								f.set(t, dvalue);
							} else if (type.equals("class [Ljava.lang.String;")) {
								f.set(t, values);
							} else if (type.equals("class [B")) {
								byte bvalue[] = new byte[values.length];
								for (int i = 0; i < bvalue.length; i++) {
									bvalue[i] = Byte.parseByte(values[i]);
								}
								f.set(t, bvalue);
							} else if (type.equals("class [S")) {
								short bvalue[] = new short[values.length];
								for (int i = 0; i < bvalue.length; i++) {
									bvalue[i] = Short.parseShort(values[i]);
								}
								f.set(t, bvalue);
							} else if (type.equals("class [Z")) {
								boolean bvalue[] = new boolean[values.length];
								for (int i = 0; i < bvalue.length; i++) {
									bvalue[i] = Boolean.parseBoolean(values[i]);
								}
								f.set(t, bvalue);
							}
						} else {
							if (type.equals("int") || type.equals("class java.lang.Integer")) {
								f.set(t, Integer.parseInt(values[0]));
							} else if (type.equals("long") || type.equals("class java.lang.Long")) {
								f.set(t, Long.parseLong(values[0]));
							} else if (type.equals("float") || type.equals("class java.lang.Float")) {
								f.set(t, Float.parseFloat(values[0]));
							} else if (type.equals("double") || type.equals("class java.lang.Double")) {
								f.set(t, Double.parseDouble(values[0]));
							} else if (type.equals("boolean") || type.equals("class java.lang.Boolean")) {
								f.set(t, Boolean.parseBoolean(values[0]));
							} else if (type.equals("byte") || type.equals("class java.lang.Byte")) {
								f.set(t, Byte.parseByte(values[0]));
							} else if (type.equals("char") || type.equals("class java.lang.Character")) {
								f.set(t, values[0].toCharArray()[0]);
							} else if (type.equals("short") || type.equals("class java.lang.Short")) {
								f.set(t, Short.parseShort(values[0]));
							} else if (type.equals("class java.lang.String")) {
								f.set(t, values[0]);
							}

						}

					}
				}
			}

			return t;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*
		 * catch (NoSuchFieldException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		return null;
	}

	public synchronized static String getMessageInfo(Object t) {
		StringBuffer buffer = new StringBuffer();
		try {
			Class cls = t.getClass();
			Field field[] = cls.getDeclaredFields();

			int sum = 0;
			buffer.append("<table border='1' align=\"center\" ><tr><td>属性名</td><td>类型</td><td>值</td></tr>");
			for (Field f : field) {
				int d = f.getModifiers();
				if (Modifier.isStatic(d) || f.getName().equals("seqNum")) {
					continue;
				} else {
					sum++;
					
					f.setAccessible(true);
					
					// 如果数组
					String type = f.getType().toString();
					buffer.append("<tr><td>").append(f.getName()).append("</td><td>").append(type).append("</td><td>");
					if (type.contains("[")) {
						if (type.equals("class [I")) {
							int values[] = (int[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");
						} else if (type.equals("class [J")) {
							long values[] = (long[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");

						} else if (type.equals("class [F")) {

							float values[] = (float[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");

						} else if (type.equals("class [C")) {

							char values[] = (char[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");

						} else if (type.equals("class [D")) {
							double values[] = (double[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");
						} else if (type.equals("class [Ljava.lang.String;")) {
							String values[] = (String[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");
						} else if (type.equals("class [B")) {
							byte values[] = (byte[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");
						} else if (type.equals("class [S")) {
							short values[] = (short[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");
						} else if (type.equals("class [Z")) {
							boolean values[] = (boolean[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");
						} else {
							Object values[] = (Object[]) (f.get(t));
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									buffer.append(values[i]);
								} else
									buffer.append("," + values[i]);
							}
							buffer.append("</td></tr>");
						}
					} else {
						if (type.equals("int") || type.equals("class java.lang.Integer")) {
							buffer.append(f.getInt(t)).append("</td></tr>");
						} else if (type.equals("long") || type.equals("class java.lang.Long")) {
							buffer.append(f.getLong(t)).append("</td></tr>");
						} else if (type.equals("float") || type.equals("class java.lang.Float")) {
							buffer.append(f.getFloat(t)).append("</td></tr>");
						} else if (type.equals("double") || type.equals("class java.lang.Double")) {
							buffer.append(f.getDouble(t)).append("</td></tr>");
						} else if (type.equals("boolean") || type.equals("class java.lang.Boolean")) {
							buffer.append(f.getBoolean(t)).append("</td></tr>");
						} else if (type.equals("byte") || type.equals("class java.lang.Byte")) {
							buffer.append(f.getByte(t)).append("</td></tr>");
						} else if (type.equals("char") || type.equals("class java.lang.Character")) {
							buffer.append(f.getChar(t)).append("</td></tr>");
						} else if (type.equals("short") || type.equals("class java.lang.Short")) {
							buffer.append(f.getShort(t)).append("</td></tr>");
						} else if (type.equals("class java.lang.String")) {
							buffer.append((String) (f.get(t))).append("</td></tr>");
						} else {
							buffer.append((f.get(t).toString())).append("</td></tr>");
						}

					}

				}
			}
			buffer.append("</table>");

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*
		 * catch (NoSuchFieldException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		return buffer.toString();
	}

	public synchronized static boolean isContainMenuWindow(Object t) {
		Class tcls = t.getClass();
		Field field[] = tcls.getDeclaredFields();
		boolean conWindow = false;

		for (Field f : field) {
			int d = f.getModifiers();
			if (Modifier.isStatic(d))
				continue;
			f.setAccessible(true);
			try {
				if (f.get(t) instanceof MenuWindow) {
					conWindow = true;
				}

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return conWindow;
	}

	public synchronized static MenuWindow getMenuWindow(Object t) {
		Class tcls = t.getClass();
		Field field[] = tcls.getDeclaredFields();
		for (Field f : field) {
			int d = f.getModifiers();
			if (Modifier.isStatic(d))
				continue;
			f.setAccessible(true);
			try {
				if (f.get(t) instanceof MenuWindow) {
					MenuWindow m = (MenuWindow) f.get(t);
					return m;
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String args[]) {
		Map<String, String[]> amap = new HashMap<String, String[]>();
		//
		// name=by byte
		// name=b boolean
		// name=c char
		// name=s short
		// name=i int
		// name=l long
		// name=f float
		// name=aby class [B
		// name=ab class [Z
		// name=ac class [C
		// name=as class [S
		// name=ai class [I
		// name=al class [J
		// name=af class [F
		// name=ad class [D
		// name=key class java.lang.String
		// name=st class [Ljava.lang.String;

		amap.put("by", new String[] { "1" });
		amap.put("b", new String[] { "true" });
		amap.put("c", new String[] { "d" });
		amap.put("s", new String[] { "2" });
		amap.put("i", new String[] { "3" });

		amap.put("l", new String[] { "4" });
		amap.put("f", new String[] { "5" });
		amap.put("d", new String[] { "5.3" });

		amap.put("aby", new String[] { "3", "5" });
		amap.put("ab", new String[] { "true", "true" });
		amap.put("ac", new String[] { "d", "f" });
		amap.put("as", new String[] { "33", "38" });
		amap.put("ai", new String[] { "332", "323" });

		amap.put("al", new String[] { "3323", "3233" });

		amap.put("af", new String[] { "333232.1", "33323.1" });

		amap.put("ad", new String[] { "323.3", "323.3112" });
		amap.put("st", new String[] { "33afafa2", "3adaf23" });
		amap.put("key", new String[] { "asdfa", "3adaf23" });
		TestMessage t = (TestMessage) MessageReflectHelper.createNewInstance("com.fy.engineserver.util.TestMessage", amap);
		// System.out.println((t == null));
		if (t != null) {
//			System.out.println(t.toString());
//			System.out.println("--------------------------");
//			System.out.println(MessageReflectHelper.getMessageInfo(t));

			// MenuWindow mw =
			// WindowManager.getInstance().createTempMenuWindow(36000);
			// QUERY_WINDOW_RES res = new
			// QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw,
			// mw.getOptions());
			
			JIAZU_APPLY_RES res = new JIAZU_APPLY_RES(0, (byte) 0, Translate.text_6100  + Translate.text_6101);
			
//			System.out.println(JiazuManager.class.getName());
			if (res.getClass().getName().matches("(?i).*jiazu.*"))
			{
//				System.out.println("true");
				
			}
//			System.out.println(MessageReflectHelper.isContainMenuWindow(t));
		}
	}
}
