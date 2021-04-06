package com.xuanzhi.tools.gamestat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class AnalyseTool {

	Class<?> dataClass;
	
	//
	public ArrayList<String> headers = new ArrayList<String>();
	public ArrayList<String> errors = new ArrayList<String>();
	
	public AnalyseTool(Class<?> dataClass){
		this.dataClass = dataClass;
	}
	
	/**
	 * 获得可以隐藏的头
	 * @return
	 */
	public String[] getHiddenableHeaders(){
		
		ArrayList<String> al = new ArrayList<String>();
		Field fields[] = dataClass.getDeclaredFields();
		for(int i = 0 ; i < fields.length ; i++){
			Field f = fields[i];
			if(f.getType().getEnclosingClass() == dataClass){
				al.add(f.getName());
			}
		}
		return al.toArray(new String[0]);
	}
	
	/**
	 * 获得可以合并的头
	 * @return
	 */
	public String[] getMergeableHeaders(){
		
		ArrayList<String> al = new ArrayList<String>();
		Field fields[] = dataClass.getDeclaredFields();
		for(int i = 0 ; i < fields.length ; i++){
			Field f = fields[i];
			if(f.getType().getEnclosingClass() == dataClass){
				Mergeable m = f.getType().getAnnotation(Mergeable.class);
				if(m != null){
					al.add(f.getName());
				}else{
					//
					Field fields2[] = f.getType().getDeclaredFields();
					for(int j = 0 ; j < fields2.length ; j++){
						Field f2 = fields2[j];
						if(f2.getType().getEnclosingClass() == dataClass){
							Mergeable m2 = f2.getType().getAnnotation(Mergeable.class);
							if(m2 != null){
								al.add(f.getName()+"."+f2.getName());
							}
						}
					}
				}
			}
		}
		return al.toArray(new String[0]);
	}
	
	public int getMaxDeepth(){
		int maxDeepth = 0;
		for(int i = 0 ; i < headers.size() ; i++){
			int count = headers.get(i).split("\\.").length;
			if(count > maxDeepth){
				maxDeepth = count;
			}
		}
		return maxDeepth;
	}
	/**
	 * 寻找分母，寻找的顺序是
	 * 1. 从本身定义的class中找
	 * 2. 如果找不到就像上级定义中找
	 * 
	 * @param parents
	 * @param f
	 * @param denominator
	 * @return
	 */
	public boolean find_denominator(ArrayList<Field> parents,Field f, String denominator,String keyword){
		
		boolean found_denominator = false;
		try {
			Field denoF = f.getDeclaringClass().getDeclaredField(denominator);
			if(denoF != null){
				if(denoF.getType().isPrimitive()){
					found_denominator = true;
				}else{
					Distribute d = denoF.getAnnotation(Distribute.class);
					if(d != null){
						String mm[] = d.members();
						for(int i = 0 ; i < mm.length ; i++){
							if(mm[i].equals(keyword)){
								found_denominator = true;
							}
						}
					}
				}
			}
			
		} catch (NoSuchFieldException e) {
			//error info	
			for(Field pf : parents){
				try {
					Field denoF = pf.getType().getDeclaredField(denominator);
					if(denoF != null){
						if(denoF.getType().isPrimitive()){
							found_denominator = true;
						}else{
							Distribute d = denoF.getAnnotation(Distribute.class);
							if(d != null){
								String mm[] = d.members();
								for(int i = 0 ; i < mm.length ; i++){
									if(mm[i].equals(keyword)){
										found_denominator = true;
									}
								}
							}
						}
					}
				}catch (NoSuchFieldException ex) {}
			}
			try {
				Field denoF = dataClass.getDeclaredField(denominator);
				if(denoF != null){
					if(denoF.getType().isPrimitive()){
						found_denominator = true;
					}else{
						Distribute d = denoF.getAnnotation(Distribute.class);
						if(d != null){
							String mm[] = d.members();
							for(int i = 0 ; i < mm.length ; i++){
								if(mm[i].equals(keyword)){
									found_denominator = true;
								}
							}
						}
					}
				}
			}catch (NoSuchFieldException ex) {}
		}
		return found_denominator;
	}
	
	public void constructHeader(ArrayList<Field> parents,Field f){
		
		String name = f.getName();
		for(int i = parents.size() -1 ; i >= 0 ; i--){
			name = parents.get(i).getName()+"."+name;
		}
		
		if(f.getType().isPrimitive()){
			
			
			headers.add(name);
			
			Percent a = f.getAnnotation(Percent.class);
			if(a != null){
				if(find_denominator(parents,f,a.denominator(),a.keyword()) == false){
					// error info
					errors.add(new String("[error] can't find denominator '"+a.denominator()+"' for {"+name+"}"));
				}
			}
			
			Average b = f.getAnnotation(Average.class);
			if(b != null){
				if(find_denominator(parents,f,b.denominator(),b.keyword()) == false){
					// error info
					errors.add(new String("[error] can't find denominator '"+b.denominator()+"' for {"+name+"}"));
				}
			}
		
		}else if(java.util.Map.class.isAssignableFrom(f.getType())){
			Distribute a = f.getAnnotation(Distribute.class);
			if(a == null){
				errors.add(new String("[error] type {"+f.getType()+"} for field {"+name+"} not supported because not define by @Distribute!"));
			}else{
				String members[] = a.members();
				
				for(int i = 0 ; i < members.length ; i++){
					headers.add(name+"."+members[i]);
				}
				
				if(find_denominator(parents,f,a.denominator(),"") == false){
					// error info
					errors.add(new String("[error] can't find denominator '"+a.denominator()+"' for {"+name+"}"));
				}
			}
		}else if(f.getType().getEnclosingClass() == dataClass){
			
			ArrayList<Field> newParents = new ArrayList<Field>();
			newParents.addAll(parents);
			newParents.add(f);
			
			Field fields[] = f.getType().getDeclaredFields();
			for(int i = 0 ; i < fields.length ; i++){
				Field ff = fields[i];
				if(java.lang.reflect.Modifier.isStatic(ff.getModifiers()) || java.lang.reflect.Modifier.isTransient(ff.getModifiers())) continue;
				
				constructHeader(newParents,ff);
			}
		}else{
			errors.add(new String("[error] type {"+f.getType()+"} for field {"+name+"} not supported!"));
		}
	}
	
	public void analyse(){
		
		Field fields[] = dataClass.getDeclaredFields();
		for(int i = 0 ; i < fields.length ; i++){
			Field f = fields[i];
			
			if(java.lang.reflect.Modifier.isStatic(f.getModifiers()) || java.lang.reflect.Modifier.isTransient(f.getModifiers())) continue;
			
			constructHeader(new ArrayList<Field>(),f);
		}
	}
	
	/**
	 * 根据单条用户的记录，按索引合并数据
	 * 所有索引为指定值的
	 * @param records
	 * @return
	 */
	public Object mergeByIndex(Object[] records,long index) throws Exception{
		Field fields[] = dataClass.getDeclaredFields();
		Field indexF = null;
		for(int i = 0 ; i < fields.length ; i++){
			Field f = fields[i];
			Index a = f.getAnnotation(Index.class);
			if(a != null){
				indexF = f;
			}
		}
		if(indexF == null){
			throw new java.lang.IllegalArgumentException("can't find @Index field!");
		}
		indexF.setAccessible(true);
		
		ArrayList<Object> al = new ArrayList<Object>(); 
		for(int i = 0 ; i < records.length ; i++){
			Long id = 0L;
			try {
				id = (Long)indexF.get(records[i]);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(id.longValue() == index){
				al.add(records[i]);
			}
		}
		
		Object result = dataClass.newInstance();
		indexF.set(result, index);
		for(int i = 0 ; i < al.size() ; i++){
			mergeTowObject(result,al.get(i));
		}
		return result;
	}
	
	/**
	 * 将parentDataClassList[n-1]和parentDataList[n-1]对应的对象中的某个属性，合并到指定的结果中。
	 * 此结果的类型和此属性是一致的都是内部类。
	 * 
	 * @param parentDataList
	 * @param parentDataClassList
	 * @param mergeClass
	 * @param mergeResult
	 * @param field
	 * @return
	 */
	void mergeTowObject(Object result,Object obj){
		
		 
		
		Field dfs[] = result.getClass().getDeclaredFields();
		
		for(int i = 0 ; i < dfs.length ; i++){
			Field f = dfs[i];
			
			if(java.lang.reflect.Modifier.isStatic(f.getModifiers()) || java.lang.reflect.Modifier.isTransient(f.getModifiers())) continue;
			
			f.setAccessible(true);
			
			if(f.getType().isPrimitive()){
				Index ia = f.getAnnotation(Index.class);
				if(ia != null){
					continue;
				}
				Object value1 = null;
				Object value2 = null;
				try {
					value1 = f.get(result);
					value2 = f.get(obj);
				} catch (Exception e) {
					e.printStackTrace(System.out);
				} 
				try {
					f.set(result, WebTool.add(value1,value2));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(java.util.Map.class.isAssignableFrom(f.getType())){
				Distribute a = f.getAnnotation(Distribute.class);
				if(a != null){
					Map map1 = null;
					Map map2 = null;
					try {
						map1 = (Map)f.get(result);
						map2 = (Map)f.get(obj);
					} catch (Exception e) {
						e.printStackTrace(System.out);
					}
					String mm [] = a.members();
					for(String key : mm){
						Object value1 = map1.get(key);
						Object value2 = map2.get(key);
						if(value1 == null){
							value1 = value2;
							if(value1 == null) value1 = 0;
							map1.put(key, value1);
						}else if(value2 != null){
							map1.put(key, WebTool.add(value1,value2));
						}
					}
				}
			}else if(f.getType().getEnclosingClass() == dataClass){
				
				//递归
				try {
					mergeTowObject(f.get(result),f.get(obj));
				} catch (Exception e) {
					e.printStackTrace(System.out);
				} 
			}
		}
		
	}
}
