package com.xuanzhi.tools.text;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * 
 *
 */
public class ValueParser {

	public ValueParser() {

	}

	/**
	 * Random access storage for parameter data.
	 */
	protected Hashtable parameters = new Hashtable();

	/**
	 * The character encoding to use when converting to byte arrays
	 */
	private String characterEncoding = "GBK";

	private String paramDelim = "&";

	private String pairDelim = "=";

	public void parse(String str) throws Exception{
		
		parse(str,true);
		
	}
	
	/**
	 * 
	 * @param s
	 * @param urlDecode
	 * @throws Exception
	 */
	public void parse(String str, boolean urlDecode) throws Exception {
		if(str == null){
			return;
		}

		StringTokenizer st = new StringTokenizer(str, paramDelim);

		while (st.hasMoreTokens()) {

			String pair = st.nextToken();
			int pos = pair.indexOf(pairDelim);
			String name = pair.substring(0, pos);
			String value = pair.substring(pos + 1);

			if (urlDecode) {
				name = URLDecoder.decode(name, characterEncoding);
				value = URLDecoder.decode(value, characterEncoding);
			}

			if (name.length() > 0) {
				add(convert(name), value);
			}
		}
	}

	/**
	 * A static version of the convert method, which trims the string data and
	 * applies the conversion specified in the property given by
	 * URL_CASE_FOLDING. It returns a new string so that it does not destroy the
	 * value data.
	 * 
	 * @param value
	 *            A String to be processed.
	 * @return A new String converted to lowercase and trimmed.
	 */
	public String convertAndTrim(String value) {

		String tmp = value.trim().toLowerCase();
		return tmp;
	}

	/**
	 * Constructor that takes a character encoding
	 */
	public ValueParser(String characterEncoding) {
		super();
		this.characterEncoding = characterEncoding;
	}

	/**
	 * Clear all name/value pairs out of this object.
	 */
	public void clear() {
		parameters.clear();
	}

	/**
	 * Set the character encoding that will be used by this ValueParser.
	 */
	public void setCharacterEncoding(String s) {
		characterEncoding = s;
	}

	/**
	 * Get the character encoding that will be used by this ValueParser.
	 */
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	/**
	 * Add a name/value pair into this object.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param value
	 *            A double with the value.
	 */
	public void add(String name, double value) {
		add(name, Double.toString(value));
	}

	/**
	 * Add a name/value pair into this object.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param value
	 *            An int with the value.
	 */
	public void add(String name, int value) {
		add(name, Integer.toString(value));
	}

	/**
	 * Add a name/value pair into this object.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param value
	 *            An Integer with the value.
	 */
	public void add(String name, Integer value) {
		add(name, value.toString());
	}

	/**
	 * Add a name/value pair into this object.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param value
	 *            A long with the value.
	 */
	public void add(String name, long value) {
		add(name, Long.toString(value));
	}

	/**
	 * Add a name/value pair into this object.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param value
	 *            A long with the value.
	 */
	public void add(String name, String value) {
		append(name, value);
	}

	/**
	 * Add a String parameters. If there are any Strings already associated with
	 * the name, append to the array. This is used for handling parameters from
	 * mulitipart POST requests.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param value
	 *            A String with the value.
	 */
	public void append(String name, String value) {
		String[] items = this.getStrings(name);
		if (items == null) {
			items = new String[1];
			items[0] = value;
			parameters.put(convert(name), items);
		} else {
			String[] newItems = new String[items.length + 1];
			System.arraycopy(items, 0, newItems, 0, items.length);
			newItems[items.length] = value;
			parameters.put(convert(name), newItems);
		}
	}

	/**
	 * Removes the named parameter from the contained hashtable. Wraps to the
	 * contained <code>Hashtable.remove()</code>.
	 * 
	 * 
	 * @return The value that was mapped to the key (a <code>String[]</code>)
	 *         or <code>null</code> if the key was not mapped.
	 */
	public Object remove(String name) {
		return parameters.remove(convert(name));
	}

	/**
	 * Trims the string data and applies the conversion specified in the
	 * property given by URL_CASE_FOLDING. It returns a new string so that it
	 * does not destroy the value data.
	 * 
	 * @param value
	 *            A String to be processed.
	 * @return A new String converted to lowercase and trimmed.
	 */
	public String convert(String value) {
		return convertAndTrim(value);
	}

	/**
	 * Determine whether a given key has been inserted. All keys are stored in
	 * lowercase strings, so override method to account for this.
	 * 
	 * @param key
	 *            An Object with the key to search for.
	 * @return True if the object is found.
	 */
	public boolean containsKey(Object key) {
		return parameters.containsKey(convert((String) key));
	}

	/*
	 * Get an enumerator for the parameter keys. Wraps to the contained <code>Hashtable.keys()</code>.
	 * 
	 * @return An <code>enumerator</code> of the keys.
	 */
	public Enumeration keys() {
		return parameters.keys();
	}

	/*
	 * Returns all the available parameter names.
	 * 
	 * @return A object array with the keys.
	 */
	public String[] getKeys() {
		return (String[])parameters.keySet().toArray(new String[0]);
	}

	/**
	 * Return a boolean for the given name. If the name does not exist, return
	 * defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return A boolean.
	 */
	public boolean getBoolean(String name, boolean defaultValue) {
		boolean value = defaultValue;
		Object object = parameters.get(convert(name));
		if (object != null) {
			String tmp = getString(name);
			if (tmp.equalsIgnoreCase("1") || tmp.equalsIgnoreCase("true")
					|| tmp.equalsIgnoreCase("on")) {
				value = true;
			}
			if (tmp.equalsIgnoreCase("0") || tmp.equalsIgnoreCase("false")
					|| tmp.equalsIgnoreCase("off")) {
				value = false;
			}
		}
		return value;
	}

	/**
	 * Return a boolean for the given name. If the name does not exist, return
	 * false.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A boolean.
	 */
	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	/**
	 * Return a Boolean for the given name. If the name does not exist, return
	 * defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return A Boolean.
	 */
	public Boolean getBool(String name, boolean defaultValue) {
		return new Boolean(getBoolean(name, defaultValue));
	}

	/**
	 * Return a Boolean for the given name. If the name does not exist, return
	 * false.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A Boolean.
	 */
	public Boolean getBool(String name) {
		return new Boolean(getBoolean(name, false));
	}

	/**
	 * Return a double for the given name. If the name does not exist, return
	 * defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return A double.
	 */
	public double getDouble(String name, double defaultValue) {
		double value = defaultValue;
		try {
			Object object = parameters.get(convert(name));
			if (object != null) {
				value = Double.valueOf(((String[]) object)[0]).doubleValue();
			}
		} catch (NumberFormatException exception) {
		}
		return value;
	}

	/**
	 * Return a double for the given name. If the name does not exist, return
	 * 0.0.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A double.
	 */
	public double getDouble(String name) {
		return getDouble(name, 0.0);
	}

	/**
	 * Return a float for the given name. If the name does not exist, return
	 * defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return A float.
	 */
	public float getFloat(String name, float defaultValue) {
		float value = defaultValue;
		try {
			Object object = parameters.get(convert(name));
			if (object != null) {
				value = Float.valueOf(((String[]) object)[0]).floatValue();
			}
		} catch (NumberFormatException exception) {
		}
		return value;
	}

	/**
	 * Return a float for the given name. If the name does not exist, return
	 * 0.0.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A float.
	 */
	public float getFloat(String name) {
		return getFloat(name, 0.0f);
	}

	/**
	 * Return a BigDecimal for the given name. If the name does not exist,
	 * return 0.0.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return A BigDecimal.
	 */
	public BigDecimal getBigDecimal(String name, BigDecimal defaultValue) {
		BigDecimal value = defaultValue;
		try {
			Object object = parameters.get(convert(name));
			if (object != null) {
				String temp = ((String[]) object)[0];
				if (temp.length() > 0) {
					value = new BigDecimal(((String[]) object)[0]);
				}
			}
		} catch (NumberFormatException exception) {
		}
		return value;
	}

	/**
	 * Return a BigDecimal for the given name. If the name does not exist,
	 * return 0.0.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A BigDecimal.
	 */
	public BigDecimal getBigDecimal(String name) {
		return getBigDecimal(name, new BigDecimal(0.0));
	}

	/**
	 * Return an array of BigDecimals for the given name. If the name does not
	 * exist, return null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A BigDecimal[].
	 */
	public BigDecimal[] getBigDecimals(String name) {
		BigDecimal[] value = null;
		Object object = getStrings(convert(name));
		if (object != null) {
			String[] temp = (String[]) object;
			value = new BigDecimal[temp.length];
			for (int i = 0; i < temp.length; i++) {
				value[i] = new BigDecimal(temp[i]);
			}
		}
		return value;
	}

	/**
	 * Return an int for the given name. If the name does not exist, return
	 * defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return An int.
	 */
	public int getInt(String name, int defaultValue) {
		int value = defaultValue;
		try {
			Object object = parameters.get(convert(name));
			if (object != null) {
				value = Integer.valueOf(((String[]) object)[0]).intValue();
			}
		} catch (NumberFormatException exception) {
		}
		return value;
	}

	/**
	 * Return an int for the given name. If the name does not exist, return 0.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return An int.
	 */
	public int getInt(String name) {
		return getInt(name, 0);
	}

	/**
	 * Return an Integer for the given name. If the name does not exist, return
	 * defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return An Integer.
	 */
	public Integer getInteger(String name, int defaultValue) {
		return new Integer(getInt(name, defaultValue));
	}

	/**
	 * Return an Integer for the given name. If the name does not exist, return
	 * defaultValue. You cannot pass in a null here for the default value.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return An Integer.
	 */
	public Integer getInteger(String name, Integer def) {
		return new Integer(getInt(name, def.intValue()));
	}

	/**
	 * Return an Integer for the given name. If the name does not exist, return
	 * 0.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return An Integer.
	 */
	public Integer getInteger(String name) {
		return new Integer(getInt(name, 0));
	}

	/**
	 * Return an array of ints for the given name. If the name does not exist,
	 * return null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return An int[].
	 */
	public int[] getInts(String name) {
		int[] value = null;
		Object object = getStrings(convert(name));
		if (object != null) {
			String[] temp = (String[]) object;
			value = new int[temp.length];
			for (int i = 0; i < temp.length; i++) {
				value[i] = Integer.parseInt(temp[i]);
			}
		}
		return value;
	}

	/**
	 * Return an array of Integers for the given name. If the name does not
	 * exist, return null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return An Integer[].
	 */
	public Integer[] getIntegers(String name) {
		Integer[] value = null;
		Object object = getStrings(convert(name));
		if (object != null) {
			String[] temp = (String[]) object;
			value = new Integer[temp.length];
			for (int i = 0; i < temp.length; i++) {
				value[i] = Integer.valueOf(temp[i]);
			}
		}
		return value;
	}

	/**
	 * Return a long for the given name. If the name does not exist, return
	 * defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return A long.
	 */
	public long getLong(String name, long defaultValue) {
		long value = defaultValue;
		try {
			Object object = parameters.get(convert(name));
			if (object != null) {
				value = Long.valueOf(((String[]) object)[0]).longValue();
			}
		} catch (NumberFormatException exception) {
		}
		return value;
	}

	/**
	 * Return a long for the given name. If the name does not exist, return 0.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A long.
	 */
	public long getLong(String name) {
		return getLong(name, 0);
	}

	/**
	 * Return an array of longs for the given name. If the name does not exist,
	 * return null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A long[].
	 */
	public long[] getLongs(String name) {
		long[] value = null;
		Object object = getStrings(convert(name));
		if (object != null) {
			String[] temp = (String[]) object;
			value = new long[temp.length];
			for (int i = 0; i < temp.length; i++) {
				value[i] = Long.parseLong(temp[i]);
			}
		}
		return value;
	}

	/**
	 * Return an array of Longs for the given name. If the name does not exist,
	 * return null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A Long[].
	 */
	public Long[] getLongObjects(String name) {
		Long[] value = null;
		Object object = getStrings(convert(name));
		if (object != null) {
			String[] temp = (String[]) object;
			value = new Long[temp.length];
			for (int i = 0; i < temp.length; i++) {
				value[i] = Long.valueOf(temp[i]);
			}
		}
		return value;
	}

	/**
	 * Return a byte for the given name. If the name does not exist, return
	 * defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return A byte.
	 */
	public byte getByte(String name, byte defaultValue) {
		byte value = defaultValue;
		try {
			Object object = parameters.get(convert(name));
			if (object != null) {
				value = Byte.valueOf(((String[]) object)[0]).byteValue();
			}
		} catch (NumberFormatException exception) {
		}
		return value;
	}

	/**
	 * Return a byte for the given name. If the name does not exist, return 0.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A byte.
	 */
	public byte getByte(String name) {
		return getByte(name, (byte) 0);
	}

	/**
	 * Return an array of bytes for the given name. If the name does not exist,
	 * return null. The array is returned according to the HttpRequest's
	 * character encoding.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A byte[].
	 * @exception UnsupportedEncodingException.
	 */
	public byte[] getBytes(String name) throws UnsupportedEncodingException {
		String tempStr = getString(name);
		if (tempStr != null)
			return tempStr.getBytes(characterEncoding);
		return null;
	}

	/**
	 * Return a String for the given name. If the name does not exist, return
	 * null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A String.
	 */
	public String getString(String name) {
		try {
			String value = null;
			Object object = parameters.get(convert(name));
			if (object != null)
				value = ((String[]) object)[0];
			if (value == null || value.equals("null")) {
				return null;
			}
			return value;
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Return a String for the given name. If the name does not exist, return
	 * null. It is the same as the getString() method however has been added for
	 * simplicity when working with template tools such as Velocity which allow
	 * you to do something like this:
	 * 
	 * <code>$data.Parameters.form_variable_name</code>
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A String.
	 */
	public String get(String name) {
		return getString(name);
	}

	/**
	 * Return a String for the given name. If the name does not exist, return
	 * the defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return A String.
	 */
	public String getString(String name, String defaultValue) {
		String value = getString(name);
		if (value == null || value.length() == 0 || value.equals("null"))
			return defaultValue;
		else
			return value;
	}

	/**
	 * Set a parameter to a specific value.
	 * 
	 * This is useful if you want your action to override the values of the
	 * parameters for the screen to use.
	 * 
	 * @param name
	 *            The name of the parameter.
	 * @param value
	 *            The value to set.
	 */
	public void setString(String name, String value) {
		if (value != null) {
			parameters.put(convert(name), new String[] { value });
		}
	}

	/**
	 * Return an array of Strings for the given name. If the name does not
	 * exist, return null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return A String[].
	 */
	public String[] getStrings(String name) {
		String[] value = null;
		Object object = parameters.get(convert(name));
		if (object != null) {
			value = ((String[]) object);
		}
		return value;
	}

	/**
	 * Return an array of Strings for the given name. If the name does not
	 * exist, return the defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param defaultValue
	 *            The default value.
	 * @return A String[].
	 */
	public String[] getStrings(String name, String[] defaultValue) {
		String[] value = getStrings(name);
		if (value == null || value.length == 0)
			return defaultValue;
		else
			return value;
	}

	/**
	 * Set a parameter to a specific value.
	 * 
	 * This is useful if you want your action to override the values of the
	 * parameters for the screen to use.
	 * 
	 * @param name
	 *            The name of the parameter.
	 * @param values
	 *            The value to set.
	 */
	public void setStrings(String name, String[] values) {
		if (values != null) {
			parameters.put(convert(name), values);
		}
	}

	/**
	 * Return an Object for the given name. If the name does not exist, return
	 * null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return An Object.
	 */
	public Object getObject(String name) {
		try {
			Object value = null;
			Object object = parameters.get(convert(name));
			if (object != null) {
				value = ((Object[]) object)[0];
			}
			return value;
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Return an array of Objects for the given name. If the name does not
	 * exist, return null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @return An Object[].
	 */
	public Object[] getObjects(String name) {
		try {
			return (Object[]) parameters.get(convert(name));
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Returns a {@link java.util.Date} object. String is parsed by supplied
	 * DateFormat. If the name does not exist, return the defaultValue.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param df
	 *            A DateFormat.
	 * @param defaultValue
	 *            The default value.
	 * @return A Date.
	 */
	public Date getDate(String name, DateFormat df, Date defaultValue) {
		Date date = null;

		if (containsKey(name)) {
			try {
				// Reject invalid dates.
				df.setLenient(false);
				date = df.parse(getString(name));
			} catch (ParseException e) {
				// Thrown if couldn't parse date.
				date = defaultValue;
			}
		} else
			date = defaultValue;

		return date;
	}

	/**
	 * Returns a {@link java.util.Date} object. String is parsed by supplied
	 * DateFormat. If the name does not exist, return null.
	 * 
	 * @param name
	 *            A String with the name.
	 * @param df
	 *            A DateFormat.
	 * @return A Date.
	 */
	public Date getDate(String name, DateFormat df) {
		return getDate(name, df, null);
	}

	public Date getDate(String name) {
		DateFormat df = DateFormat.getDateInstance();
		return getDate(name, df, null);
	}

	/**
	 * Simple method that attempts to get a textual representation of this
	 * object's name/value pairs. String[] handling is currently a bit rough.
	 * 
	 * @return A textual representation of the parsed name/value pairs.
	 */
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		for (Enumeration e = parameters.keys(); e.hasMoreElements();) {
			
			String name = (String) e.nextElement();
			try {
				String[] params = this.getStrings(name);
				for (int i = 0; i < params.length; i++) {
					name = URLEncoder.encode(name,characterEncoding);
					String param = URLEncoder.encode(params[i],characterEncoding);
					sb.append(name).append("=").append(param).append("&");
				}
			} catch (Exception ee) {
				
				try {
					sb.append(name);
					sb.append('=');
					sb.append("ERROR\n");
					
				} catch (Exception eee) {
				}
			}
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) throws Exception{
		
		ValueParser svp = new ValueParser();
		
		svp.parse("param3=x3&param2=2&param1=x1&param4=x41&param4=x42&param4=x43&",true);
		String param = "&=DDDD";
		//param = null;
		svp.add("param", param);
		System.out.println(svp.getString("param"));
		System.out.println(svp.getStrings("param")[0]);
		System.out.println(svp.getInt("param2"));
		System.out.println(svp.getString("param3"));
		String[] ss = svp.getStrings("param4");
//		for(int i=0;i<ss.length;i++){
//			System.out.println(ss[i]);
//		}
		
		System.out.println(svp.toString());

	}

}
