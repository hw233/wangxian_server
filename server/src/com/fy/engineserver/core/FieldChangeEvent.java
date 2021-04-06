package com.fy.engineserver.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.horse.Horse;


/**
 * 表示某个对象的某个属性， 记录此属性的id，属性的描述，属性是否修改过，以及属性的修改是否已经通知所有的客户端
 * 
 */
public class FieldChangeEvent {

	public static final byte BYTE = 0;
	public static final byte SHORT = 1;
	public static final byte INT = 2;
	public static final byte LONG = 3;
	public static final byte CHAR = 4;
	public static final byte STRING = 5;
	public static final byte BOOL = 6;
	public static final byte BYTEARRAY = 7;
	public static final byte STRINGARRAY = 8;

	public static final byte SHORTARRAY = 9;
	public static final byte INTARRAY = 10;
	public static final byte LONGARRAY = 11;
	public static final byte BOOLARRAY = 12;

	protected byte objectType;
	protected long objectId;
	protected short fieldId;

	protected Object value;

	protected byte valueType;
	protected byte[] valueData;

	public FieldChangeEvent() {
		
	}

	@Override
	protected void finalize() throws Throwable {
		
	}

	public FieldChangeEvent(LivingObject lo, short fid, Object value) {
		this();
		this.objectType = lo.getClassType();
		this.objectId = lo.getId();
		this.fieldId = fid;
		this.value = value;
	}
	
	public FieldChangeEvent(Horse horse, short fid, Object value) {
		this();
		this.objectType = (byte)1;
		this.objectId = horse.getHorseId();
		this.fieldId = fid;
		this.value = value;
	}

	public byte getObjectType() {
		return objectType;
	}

	public void setObjectType(byte objectType) {
		this.objectType = objectType;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public short getFieldId() {
		return fieldId;
	}

	public void setFieldId(short fieldId) {
		this.fieldId = fieldId;
	}

	public byte getValueType() {
		if (value == null) {
			return this.valueType;
		} else {
			return getValueType(value);
		}
	}

	public void setValueType(byte b) {
		this.valueType = b;
	}

	public void setValueData(byte[] bytes) {
		valueData = bytes;
	}

	public byte[] getValueData() {
		if (value == null) {
			return this.valueData;
		} else {
			return getValueData(value);
		}
	}

	public Object getValue() {
		if (value != null) return value;
		if (valueData != null) {
			return getValue(valueType, valueData);
		}
		return null;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 返回值的类型
	 * 
	 * @param value
	 * @return
	 */
	public static byte getValueType(Object value) {
		if (value instanceof Integer) {
			return INT;
		} else if (value instanceof Byte) {
			return BYTE;
		} else if (value instanceof Short) {
			return SHORT;
		} else if (value instanceof Character) {
			return CHAR;
		} else if (value instanceof Long) {
			return LONG;
		} else if (value instanceof String) {
			return STRING;
		} else if (value instanceof Boolean) {
			return BOOL;
		} else if (value instanceof byte[]) {
			return BYTEARRAY;
		} else if (value instanceof String[]) {
			return STRINGARRAY;
		} else if (value instanceof short[]) {
			return SHORTARRAY;
		} else if (value instanceof int[]) {
			return INTARRAY;
		} else if (value instanceof long[]) {
			return LONGARRAY;
		} else if (value instanceof boolean[]) {
			return BOOLARRAY;
		} else {
			return -1;
		}
	}

	/**
	 * 把属性域的值用字节数组表达，以便于通过网络传输
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] getValueData(Object value) {
		GameMessageFactory mf = GameMessageFactory.getInstance();
		if (value instanceof Integer) {
			return mf.numberToByteArray(((Integer) value).intValue(), 4);
		} else if (value instanceof Byte) {
			return new byte[] { ((Byte) value).byteValue() };
		} else if (value instanceof Short) {
			return mf.numberToByteArray(((Short) value).shortValue(), 2);
		} else if (value instanceof Character) {
			return mf.numberToByteArray((short) (((Character) value).charValue()), 2);
		} else if (value instanceof Long) {
			return mf.numberToByteArray(((Long) value).longValue(), 8);
		} else if (value instanceof String) {
			byte data[] = new byte[0];

			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(bs);
			try {
				String s = (String) value;
				os.writeUTF(s);
				data = bs.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return data;

		} else if (value instanceof Boolean) {
			if ((Boolean) value) {
				return new byte[] { 1 };
			} else {
				return new byte[] { 0 };
			}
		} else if (value instanceof byte[]) {
			return (byte[]) value;
		} else if (value instanceof String[]) {
			byte data[] = new byte[0];
			String tmp[] = (String[]) value;
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(bs);
			try {
				os.writeInt(tmp.length);
				for (String s : tmp) {
					os.writeUTF(s);
				}
				data = bs.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return data;
		} else if (value instanceof short[]) {
			byte data[] = new byte[0];
			short tmp[] = (short[]) value;
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(bs);
			try {
				os.writeInt(tmp.length);
				for (short s : tmp) {
					os.writeShort(s);
				}
				data = bs.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return data;
		} else if (value instanceof int[]) {
			byte data[] = new byte[0];
			int tmp[] = (int[]) value;
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(bs);
			try {
				os.writeInt(tmp.length);
				for (int s : tmp) {
					os.writeInt(s);
				}
				data = bs.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return data;
		} else if (value instanceof long[]) {
			byte data[] = new byte[0];
			long tmp[] = (long[]) value;
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(bs);
			try {
				os.writeInt(tmp.length);
				for (long s : tmp) {
					os.writeLong(s);
				}
				data = bs.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return data;
		} else if (value instanceof boolean[]) {
			byte data[] = new byte[0];
			boolean tmp[] = (boolean[]) value;
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(bs);
			try {
				os.writeInt(tmp.length);
				for (boolean s : tmp) {
					os.writeBoolean(s);
				}
				data = bs.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					bs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return data;
		} else {
			throw new java.lang.IllegalArgumentException("unsupport type for [" + value.getClass().getSimpleName() + "]");
		}
	}

	/**
	 * 返回值的类型
	 * 
	 * @param value
	 * @return
	 */
	public static Object getValue(byte valueType, byte[] valueData) {
		GameMessageFactory mf = GameMessageFactory.getInstance();

		switch (valueType) {
		case INT:
			return new Integer((int) mf.byteArrayToNumber(valueData, 0, 4));
		case BYTE:
			return new Byte(valueData[0]);
		case SHORT:
			return new Short((short) mf.byteArrayToNumber(valueData, 0, 2));
		case CHAR:
			return new Character((char) mf.byteArrayToNumber(valueData, 0, 2));
		case LONG:
			return new Long(mf.byteArrayToNumber(valueData, 0, 8));
		case STRING: {
			ByteArrayInputStream is = new ByteArrayInputStream(valueData);
			DataInputStream ds = new DataInputStream(is);
			String tmp = "";
			try {
				tmp = ds.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					ds.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return tmp;
		}
		case BOOL:
			return new Boolean(valueData[0] == 1);
		case BYTEARRAY:
			return valueData;
		case STRINGARRAY: {
			ByteArrayInputStream is = new ByteArrayInputStream(valueData);
			DataInputStream ds = new DataInputStream(is);
			String tmp[] = new String[0];
			int num;
			try {
				num = ds.readInt();
				tmp = new String[num];
				for (int i = 0; i < num; i++) {
					tmp[i] = ds.readUTF();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					ds.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return tmp;
		}
		case SHORTARRAY: {
			ByteArrayInputStream is = new ByteArrayInputStream(valueData);
			DataInputStream ds = new DataInputStream(is);
			short tmp[] = new short[0];
			int num;
			try {
				num = ds.readInt();
				tmp = new short[num];
				for (int i = 0; i < num; i++) {
					tmp[i] = ds.readShort();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					ds.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return tmp;
		}
		case INTARRAY: {
			ByteArrayInputStream is = new ByteArrayInputStream(valueData);
			DataInputStream ds = new DataInputStream(is);
			int tmp[] = new int[0];
			int num;
			try {
				num = ds.readInt();
				tmp = new int[num];
				for (int i = 0; i < num; i++) {
					tmp[i] = ds.readInt();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					ds.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return tmp;
		}
		case LONGARRAY: {
			ByteArrayInputStream is = new ByteArrayInputStream(valueData);
			DataInputStream ds = new DataInputStream(is);
			long tmp[] = new long[0];
			int num;
			try {
				num = ds.readInt();
				tmp = new long[num];
				for (int i = 0; i < num; i++) {
					tmp[i] = ds.readLong();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					ds.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return tmp;
		}
		case BOOLARRAY: {
			ByteArrayInputStream is = new ByteArrayInputStream(valueData);
			DataInputStream ds = new DataInputStream(is);
			boolean tmp[] = new boolean[0];
			int num;
			try {
				num = ds.readInt();
				tmp = new boolean[num];
				for (int i = 0; i < num; i++) {
					tmp[i] = ds.readBoolean();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					ds.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return tmp;
		}
		default:
			return null;
		}
	}

}
