package com.fy.engineserver.util;

import java.util.Arrays;

/**
 * 复合返回类型
 * 
 * 
 */
public class CompoundReturn {

	private int intValue;
	private long longValue;
	private String stringValue;
	private boolean booleanValue;
	private byte byteValue;
	private Object objValue;
	private double doubleValue;
	
	private int[] intValues;
	private long[] longValues;
	private String[] stringValues;
	private boolean[] booleanValues;
	private byte[] byteValues;
	private Object[] objVlues;

	public long getLongValue() {
		return longValue;
	}

	public CompoundReturn setLongValue(long longValue) {
		this.longValue = longValue;
		return this;
	}

	public long[] getLongValues() {
		return longValues;
	}

	public CompoundReturn setLongValues(long[] longValues) {
		this.longValues = longValues;
		return this;
	}

	public int getIntValue() {
		return intValue;
	}

	public CompoundReturn setIntValue(int intValue) {
		this.intValue = intValue;
		return this;
	}

	public String getStringValue() {
		return stringValue;
	}

	public CompoundReturn setStringValue(String stringValue) {
		this.stringValue = stringValue;
		return this;
	}

	public boolean getBooleanValue() {
		return booleanValue;
	}

	public CompoundReturn setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
		return this;
	}

	public byte getByeValue() {
		return byteValue;
	}

	public CompoundReturn setByeValue(byte byteValue) {
		this.byteValue = byteValue;
		return this;
	}

	public Object getObjValue() {
		return objValue;
	}

	public CompoundReturn setObjValue(Object objValue) {
		this.objValue = objValue;
		return this;
	}

	public Object[] getObjVlues() {
		return objVlues;
	}

	public CompoundReturn setObjVlues(Object[] objVlues) {
		this.objVlues = objVlues;
		return this;
	}

	public byte getByteValue() {
		return byteValue;
	}

	public CompoundReturn setByteValue(byte byteValue) {
		this.byteValue = byteValue;
		return this;
	}

	public int[] getIntValues() {
		return intValues;
	}

	public CompoundReturn setIntValues(int[] intValues) {
		this.intValues = intValues;
		return this;
	}

	public String[] getStringValues() {
		return stringValues;
	}

	public CompoundReturn setStringValues(String[] stringValues) {
		this.stringValues = stringValues;
		return this;
	}

	public boolean[] getBooleanValues() {
		return booleanValues;
	}

	public CompoundReturn setBooleanValues(boolean[] booleanValues) {
		this.booleanValues = booleanValues;
		return this;
	}

	public byte[] getByteValues() {
		return byteValues;
	}

	public CompoundReturn setByteValues(byte[] byteValues) {
		this.byteValues = byteValues;
		return this;
	}

	public static CompoundReturn createCompoundReturn() {
		return new CompoundReturn();
	}
	public static CompoundReturn create() {
		return new CompoundReturn();
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public CompoundReturn setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
		return this;
	}

	@Override
	public String toString() {
		return "CompoundReturn [intValue=" + intValue + ", longValue=" + longValue + ", stringValue=" + stringValue + ", booleanValue=" + booleanValue + ", byteValue=" + byteValue + ", objValue=" + objValue + ", intValues=" + Arrays.toString(intValues) + ", longValues=" + Arrays.toString(longValues) + ", stringValues=" + Arrays.toString(stringValues) + ", booleanValues=" + Arrays.toString(booleanValues) + ", byteValues=" + Arrays.toString(byteValues) + ", objVlues=" + Arrays.toString(objVlues) + "]";
	}
}
