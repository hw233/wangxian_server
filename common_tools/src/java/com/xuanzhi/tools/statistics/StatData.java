package com.xuanzhi.tools.statistics;

import java.util.*;
import com.xuanzhi.tools.cache.Cacheable;
/**
 * 统计数据
 * 
 * 注意：所有的统计项都必须是小写的，否则查不出数据。
 * 
 * @author Administrator
 *
 */
public abstract class StatData implements Cacheable{
	
	protected int autoid;
	
	protected HashMap<String,Number> map = new LinkedHashMap<String,Number>();
	
	protected HashMap<String,Number> expressMap = new LinkedHashMap<String,Number>();
	
	public String getFieldDisplayName(String field){
		return field;
	}
	
	public String[] getAllDataField(){
		return map.keySet().toArray(new String[0]);
	}
	
	public Number getValue(String field){
		return map.get(field);
	}
	
	public boolean isExists(String field){
		return map.containsKey(field);
	}
	
	public void setValue(String field,Number value){
		if(isExists(field)){
			map.put(field,value);
		}
	}
	/**
	 * 增加表达式，在作为查询模板的时候，可以增加表达式查询，
	 * 
	 * 但是在作为数据插入时，表达式不起作用
	 * 
	 * @param expression
	 */
	public void addExpression(String expression,Number type){
		expressMap.put(expression,type);
	}
	
	public void removeExpression(String expression){
		expressMap.remove(expression);
	}
	/**
	 * 获得表达式的值，如果没有设置对应的表达式，返回null
	 * @param expression
	 * @return
	 */
	public Number getValueOfExpression(String expression){
		Number n = expressMap.get(expression);
		return n;
	}
	
	public String[] getAllExpression(){
		return (String[])expressMap.keySet().toArray(new String[0]);
	}
	
	public boolean isExpressionExists(String field){
		return expressMap.containsKey(field);
	}
	
	public void reset(){
		String keys[] = this.getAllDataField();
		for(int i = 0 ; i < keys.length ; i++){
			Number n = map.get(keys[i]);
			if(n instanceof Byte){
				map.put(keys[i],new Byte((byte)0));
			}else if(n instanceof Short){
				map.put(keys[i],new Short((short)0));
			}else if(n instanceof Integer){
				map.put(keys[i],new Integer(0));
			}else if(n instanceof Long){
				map.put(keys[i],new Long(0));
			}else if(n instanceof Float){
				map.put(keys[i],new Float(0));
			}else{
				map.put(keys[i],new Double(0));
			}
		}
	}
	
	public void add(StatData d){
		String keys[] = this.getAllDataField();
		for(int i = 0 ; i < keys.length ; i++){
			Number n1 = this.getValue(keys[i]);
			Number n2 = d.getValue(keys[i]);
			Number n = null;
			if(n1 instanceof Byte){ //Byte, Double, Float, Integer, Long, Short
				n = new Byte((byte)(n1.byteValue() + n2.byteValue()));
			}else if(n1 instanceof Double){
				n = new Double(n1.doubleValue() + n2.doubleValue());
			}else if(n1 instanceof Float){
				n = new Float(n1.floatValue() + n2.floatValue());
			}else if(n1 instanceof Short){
				n = new Short((short)(n1.shortValue() + n2.shortValue()));
			}else if(n1 instanceof Long){
				n = new Long(n1.longValue() + n2.longValue());
			}else{
				n = new Integer(n1.intValue() + n2.intValue());
			}
			map.put(keys[i],n);
		}
	}
	
	public void subtrace(StatData d){
		String keys[] = this.getAllDataField();
		for(int i = 0 ; i < keys.length ; i++){
			Number n1 = this.getValue(keys[i]);
			Number n2 = d.getValue(keys[i]);
			Number n = null;
			if(n1 instanceof Byte){ //Byte, Double, Float, Integer, Long, Short
				n = new Byte((byte)(n1.byteValue() - n2.byteValue()));
			}else if(n1 instanceof Double){
				n = new Double(n1.doubleValue() - n2.doubleValue());
			}else if(n1 instanceof Float){
				n = new Float(n1.floatValue() - n2.floatValue());
			}else if(n1 instanceof Short){
				n = new Short((short)(n1.shortValue() - n2.shortValue()));
			}else if(n1 instanceof Long){
				n = new Long(n1.longValue() - n2.longValue());
			}else{
				n = new Integer(n1.intValue() - n2.intValue());
			}
			map.put(keys[i],n);
		}
	}
	
	public abstract StatData clone();
	
	public int getSize(){
		return 1;
	}
	
	public String toString(){
		return map.toString();
	}
}
