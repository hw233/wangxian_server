package com.xuanzhi.tools.simplejpa.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashSet;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;

public class Utils {

	public static void checkArray(Class<?> cl,Field f,Class<?> type) throws Exception{
		Class<?> c = type.getComponentType();
		while(c.isArray()){
			c = c.getComponentType();
		}
		if(isPrimitiveType(c)){
			return;
		}else if(isEntity(c)){
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含其他Entity["+c.getName()+"]]");
			throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]包含其他Entity["+c.getName()+"]]");
		}else if(isEmbeddable(c)){
			checkEmbeddable(cl,f,c,new HashSet<Class>());
		}else if(isCollectionType(c)){
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]为包含集合类的数组，此实现不支持这种类型。集合类为["+c.getName()+"]]");
			throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]为包含集合类的数组，集合类为["+c.getName()+"]]");
		}else{
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含不能识别的类型["+c.getName()+"]]");
			throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]包含不能识别的类型["+c.getName()+"]]");
		}
	}
	
	public static void checkEmbeddable(Class<?> cl,Field f,Class<?> type,HashSet<Class> checkingSet) throws Exception{
		
		try{
			type.getConstructor(new Class[0]);
		}catch(Exception e){
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含的Embeddable["+type.getName()+"]没有公共的无参数的构造函数]");
			throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]包含的Embeddable["+type.getName()+"]没有公共的无参数的构造函数");
		}
		
		if(checkingSet.contains(type)){
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含的Embeddable中有循环引用的情况，循环点为"+type.getName()+"]");
			throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]包含的Embeddable中有循环引用的情况，循环点为"+type.getName()+"]");
		}
		checkingSet.add(type);
		
		ArrayList<Field> al = getPersistentFields(type);
		for(int i = 0 ; i < al.size() ; i++){
			Field subf = al.get(i);
			Class<?> subtype = subf.getType();
			if(Utils.isPrimitiveType(subtype)){
				continue;
			}else if(subtype.isArray() || Utils.isCollectionType(subtype) || Utils.isEmbeddable(subtype)){
				if(subtype.isArray()){
					Utils.checkArray(cl,f,subtype);
				}else if(Utils.isCollectionType(subtype)){
					Utils.checkCollectionType(cl,f,subf.getGenericType());
				}else if(Utils.isEmbeddable(subtype)){
					Utils.checkEmbeddable(cl,f,subtype,checkingSet);
				}
			}else if(Utils.isEntity(subtype)){
				System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含其他Entity["+subtype.getName()+"]]");
				throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]包含其他Entity["+subtype.getName()+"]]");
			}else{
				System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]包含不能识别的类型["+subtype.getName()+"]]");
				throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]包含不能识别的类型["+subtype.getName()+"]]");
			}
		}
	}
	
	public static void checkCollectionType(Class<?> cl,Field f,Type type) throws Exception{
		if(type instanceof ParameterizedType){
			ParameterizedType pt = (ParameterizedType)type;
			Type[] types = pt.getActualTypeArguments();
			for(int i = 0 ; i < types.length ; i++){
				if(types[i] instanceof Class){
					Class clazz = (Class)(types[i]);
					if(Utils.isPrimitiveType(clazz)){
						continue;
					}else if(Utils.isEmbeddable(clazz)){
						Utils.checkEmbeddable(cl,f,clazz,new HashSet<Class>());
					}else if(Utils.isEntity(clazz)){
						System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]为集合类，包含其他Entity["+clazz.getName()+"]]");
						throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]为集合类，包含其他Entity["+clazz.getName()+"]");
					}else if(Utils.isCollectionType(clazz)){
						checkCollectionType(cl,f,clazz);
					}else{
						System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]为集合类，包含不能识别的类型["+clazz.getName()+"]]");
						throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]为集合类，包含不能识别的类型["+clazz.getName()+"]]");
					}
				}else if(types[i] instanceof GenericArrayType) {
					GenericArrayType gt = (GenericArrayType)types[i];
					Type tttt = gt.getGenericComponentType();
					while(tttt instanceof GenericArrayType){
						tttt = ((GenericArrayType)tttt).getGenericComponentType();
					}
					if(tttt instanceof Class){
						Class clazz = (Class)(tttt);
						if(Utils.isPrimitiveType(clazz)){
							continue;
						}else if(Utils.isEmbeddable(clazz)){
							Utils.checkEmbeddable(cl,f,clazz,new HashSet<Class>());
						}else if(Utils.isEntity(clazz)){
							System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]为集合类，包含其他Entity["+clazz.getName()+"]]");
							throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]为集合类，包含其他Entity["+clazz.getName()+"]");
						}else if(Utils.isCollectionType(clazz)){
							checkCollectionType(cl,f,clazz);
						}else{
							System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]为集合类，包含不能识别的类型["+clazz.getName()+"]]");
							throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]为集合类，包含不能识别的类型["+clazz.getName()+"]]");
						}
					}else{
						checkCollectionType(cl,f,tttt);
					}
				}else{
					checkCollectionType(cl,f,types[i]);
				}
			}
			
		}else{
			System.out.println("[SimpleEntityManager] ["+cl.getName()+"] [准备初始化失败] [Entity的某个字段["+f.getName()+"]为集合类，但没有声明具体的类型，需要用泛型来什么集合类里具体的类型]");
			throw new Exception("指定的类["+cl.getName()+"]Entity的某个字段["+f.getName()+"]Entity的某个字段["+f.getName()+"]为集合类，但没有声明具体的类型，需要用泛型来什么集合类里具体的类型]");
		}
		
	}
	
	public static boolean isEntity(Class<?> cl){
		Annotation annotations[] = cl.getAnnotations();
		
		SimpleEntity entity = null;
		for(int i = 0 ; i < annotations.length ; i++){
			Annotation a = annotations[i];
			if(a instanceof SimpleEntity){
				entity = (SimpleEntity)a;
			}
		}
		if(entity != null){
			return true;
		}
		return false;
	}
	
	public static SimpleIndices getDeclaredIndices(Class<?> cl){
		Annotation annotations[] = cl.getDeclaredAnnotations();
		
		SimpleIndices entity = null;
		for(int i = 0 ; i < annotations.length ; i++){
			Annotation a = annotations[i];
			if(a instanceof SimpleIndices){
				entity = (SimpleIndices)a;
			}
		}
		return entity;
	}
	
	public static boolean isEmbeddable(Class<?> cl){
		Annotation annotations[] = cl.getAnnotations();
		
		SimpleEmbeddable entity = null;
		for(int i = 0 ; i < annotations.length ; i++){
			Annotation a = annotations[i];
			if(a instanceof SimpleEmbeddable){
				entity = (SimpleEmbeddable)a;
			}
		}
		if(entity != null){
			return true;
		}
		return false;
	}
	
	public static boolean isCollectionType(Class<?> type){
		if(java.util.Map.class.isAssignableFrom(type)  || 
			java.util.Collection.class.isAssignableFrom(type)
				){
			return true;
		}
		return false;
	}
	
	public static boolean isPrimitiveType(Class<?> type){
		if(type == Boolean.TYPE || type == Boolean.class
				|| type == Byte.TYPE || type == Byte.class
				|| type == Short.TYPE || type == Short.class
				|| type == Character.TYPE || type == Character.class
				|| type == Integer.TYPE || type == Integer.class
				|| type == Long.TYPE || type == Long.class
				|| type == Float.TYPE || type == Float.class
				|| type == Double.TYPE || type == Double.class
				|| type == Long.TYPE || type == Long.class
				|| type == String.class || type == java.util.Date.class
				|| type == java.sql.Date.class){
			return true;
		}
		return false;
	}
	
	public static ArrayList<Field> getPersistentFields(Class<?> cl){
		ArrayList<Field> fieldList = new ArrayList<Field>();
		Class<?> p = cl;
		while(p != null){
			Field fs[] = p.getDeclaredFields();
			for(int i = 0 ; i < fs.length ; i++){
				Field f = fs[i];
				if( (f.getModifiers() & Modifier.FINAL) == 0 
						&& (f.getModifiers() & Modifier.STATIC) == 0
						&& (f.getModifiers() & Modifier.TRANSIENT) == 0	){
					fieldList.add(f);
				}
			}
			p = p.getSuperclass();
		}
		return fieldList;
	}
	
	public static ArrayList<Field> getDeclaredFields(Class<?> cl){
		ArrayList<Field> fieldList = new ArrayList<Field>();
		Class<?> p = cl;
		if(p != null){
			Field fs[] = p.getDeclaredFields();
			for(int i = 0 ; i < fs.length ; i++){
				Field f = fs[i];
				if( (f.getModifiers() & Modifier.FINAL) == 0 
						&& (f.getModifiers() & Modifier.STATIC) == 0
						&& (f.getModifiers() & Modifier.TRANSIENT) == 0	){
					fieldList.add(f);
				}
			}
		}
		return fieldList;
	}
	
	public static SimpleColumn getSimpleColumn(Field f){
		Annotation a[] = f.getAnnotations();
		for(int i = 0 ; i < a.length ; i++){
			if(a[i] instanceof SimpleColumn){
				SimpleColumn c = (SimpleColumn)a[i];
				return c;
			}
		}
		return new SimpleColumn(){

			public String columnDefinition() {
				return "";
			}

			public int length() {
				return 256;
			}

			public String name() {
				return "";
			}
			/**
			 * mysql的列名
			 */
			public String mysqlName() {
				return "";
			}
			
			public boolean nullable() {
				// TODO Auto-generated method stub
				return true;
			}

			public int precision() {
				// TODO Auto-generated method stub
				return 0;
			}

			public int saveInterval() {
				// TODO Auto-generated method stub
				return 0;
			}

			public int scale() {
				// TODO Auto-generated method stub
				return 0;
			}
			

			public Class<? extends Annotation> annotationType() {
				// TODO Auto-generated method stub
				return SimpleColumn.class;
			}

			public boolean urgent() {
				// TODO Auto-generated method stub
				return false;
			}};
	}
	
	public static class SimpleIndexImpl{

		public String name;
		public String members[];
		public int compress;
		public Class<?> cl;
		public SimpleIndexImpl(String name,String m[],int compress,Class<?> cl){
			this.name = name;
			this.members = m;
			this.compress = compress;
			this.cl = cl;
		}
		public int compress() {
			// TODO Auto-generated method stub
			return compress;
		}

		public String[] members() {
			// TODO Auto-generated method stub
			return members;
		}

		public String name() {
			// TODO Auto-generated method stub
			return name;
		}

		public Class<? extends Annotation> annotationType() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static ArrayList<Field> getAllNotFinalFields(Class cl){
		ArrayList<Field> fieldList = new ArrayList<Field>();
		Class<?> p = cl;
		while(p != null){
			Field fs[] = p.getDeclaredFields();
			for(int i = 0 ; i < fs.length ; i++){
				Field f = fs[i];
				if((f.getModifiers() & Modifier.FINAL) == 0){
					fieldList.add(f);
				}
			}
			p = p.getSuperclass();
		}
		return fieldList;
	}

}
