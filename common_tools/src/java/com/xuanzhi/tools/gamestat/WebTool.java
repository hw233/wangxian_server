package com.xuanzhi.tools.gamestat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * 用于页面的显示
 * 
 * 可以针对某些属性进行省略显示，或者合并显示。
 * 
 * 
 *
 */
public class WebTool {

	Class<?> dataClass;
	
	Object[] datas;
	
	public WebTool(Class<?> dataClass,Object[] datas){
		this.dataClass = dataClass;
		this.datas = datas;
	}
	
	
	private void filterHeader(ArrayList<String> headers,String hiddenHeaders[],String mergeHeaders[]){
		//隐藏
		Iterator<String> it = headers.iterator();
		while(it.hasNext()){
			String s = it.next();
			boolean removed = false;
			for(int i = 0 ; i < hiddenHeaders.length ; i++){
				if(s.startsWith(hiddenHeaders[i])){
					removed = true;
				}
			}
			if(removed){
				it.remove();
			}
		}
		
		//合并
		//将被合并的项，隐藏
		//合并的项，用#连接
		for(int i = 0 ; i < mergeHeaders.length ; i++){
			try{
				String names[] = mergeHeaders[i].split("\\.");
				Class<?> parentClass = dataClass;
				Field field = null;
				int index = 0;
				while(index < names.length){
					
					field = parentClass.getDeclaredField(names[index]);
				
					index++;
					parentClass = field.getType();
				}
				//记录要被merge的属性，大于1才有意义
				ArrayList<Field> mergedField = new ArrayList<Field>();
				
				Field dfs[] = parentClass.getDeclaredFields();
				for(int j = 0 ; j < dfs.length ; j++){
					if(dfs[j].getType().getEnclosingClass() == dataClass){
						if(mergedField.size() == 0){
							mergedField.add(dfs[j]);
						}else if(dfs[j].getType() == mergedField.get(mergedField.size()-1).getType()){
							mergedField.add(dfs[j]);
						}
					}
				}
				if(mergedField.size() > 1){
					String suffix = "";
					for(int j = 1; j < mergedField.size() ;j++){
						suffix += "#"+mergedField.get(j).getName();
					}
					//隐藏合并的项
					for(int j = 1; j < mergedField.size() ;j++){
						String prefix = mergeHeaders[i]+"."+mergedField.get(j).getName()+".";
						it = headers.iterator();
						while(it.hasNext()){
							String s = it.next();
							if(s.startsWith(prefix)){
								it.remove();
							}
						}
					}
					String prefix = mergeHeaders[i]+"."+mergedField.get(0).getName()+".";
					for(int j = 0 ; j < headers.size() ; j++){
						String s = headers.get(j);
						if(s.startsWith(prefix)){
							String sss = s.substring(prefix.length());
							headers.set(j, mergeHeaders[i]+"."+mergedField.get(0).getName()+suffix+"."+sss);
						}
					}
				}
				
			}catch(Exception e){
				e.printStackTrace(System.out);
			}
			
		}
		
	}
	
	public String constructHTMLHeader(String hiddenHeaders[],String mergeHeaders[]){
		AnalyseTool at = new AnalyseTool(dataClass);
		at.analyse();
		int maxDeepth = at.getMaxDeepth();
		
		ArrayList<String> headers = at.headers;
		
		filterHeader(headers,hiddenHeaders,mergeHeaders);
		
		StringBuffer headerSb = new StringBuffer();
		byte[][] flags = new byte[maxDeepth][at.headers.size()];
		for(int i = 0 ; i < maxDeepth ; i++){
			StringBuffer sb = new StringBuffer();
			sb.append("<tr bgcolor='#DDDDDD'>");
			for(int j = 0 ; j < headers.size() ; j++){
				//第i行，第j列
				String header = headers.get(j);
				String names[] = header.split("\\.");
				if(i < names.length && flags[i][j] == 0){
					if(i == names.length - 1 && i < maxDeepth - 1 ){
						sb.append("<td align='center' rowspan='"+(maxDeepth-i)+"'>"+names[i]+"</td>");
						for(int k = i ; k < maxDeepth ; k++){
							flags[k][j] = 1;
						}
					}else{
						//不需要跨行
						String prefix = "";
						for(int k = 0 ; k <= i ; k++){
							prefix += names[k]+".";
						}
						int ccc = 0;
						for(int k = j ; k < headers.size() ; k++){
							String s = headers.get(k);
							if(s.startsWith(prefix)){
								ccc++;
							}
						}
						//需要跨多少列
						sb.append("<td align='center' colspan='"+(ccc)+"'>"+names[i]+"</td>");
						for(int k = 0 ; k < ccc ; k++){
							flags[i][j+k] = 1;
						}
					}
				}
				
			}
			sb.append("</tr>");
			headerSb.append(sb.toString());
			headerSb.append("\n");
		}
		
		return headerSb.toString();
		
	}
	
	public String constructHTMLContent(String hiddenHeaders[],String mergeHeaders[],int start,int end,int step){
		if(step < 1) step = 1;
		AnalyseTool at = new AnalyseTool(dataClass);
		at.analyse();
		
		ArrayList<String> headers = at.headers;
		
		filterHeader(headers,hiddenHeaders,mergeHeaders);
		
		StringBuffer headerSb = new StringBuffer();
		int count = 0;
		for(int i = start ; i < end ; ){
			
			StringBuffer sb = new StringBuffer();
			if(count % 2 == 0)
				sb.append("<tr bgcolor='#FFFFFF'>");
			else 
				sb.append("<tr bgcolor='#FFEFEE'>");
			count++;
			
			RV row[] = new RV[headers.size()];
			
			for(int j = 0 ; j < step && i+j < datas.length ; j++){
				Object rd = datas[i+j];
				RV rvs[] = new RV[headers.size()];
				int index = 0;
				for(String header : headers){
					String names[] = header.split("\\.");
					
					ArrayList<Object> parentDataList = new ArrayList<Object>();
					ArrayList<Class<?>> parentDataClassList = new ArrayList<Class<?>>();
					parentDataList.add(rd);
					parentDataClassList.add(dataClass);
					
					rvs[index] = calculateValue(parentDataList,parentDataClassList,names,0);
					index++;
				}
				
				for(int k = 0 ; k < row.length ; k++){
					if(row[k] == null){
						row[k] = rvs[k];
					}else{
						row[k].add(rvs[k]);
					}
				}
			}
			for(int k = 0 ; k < row.length ; k++){
				if(k == 0){
					sb.append("<td nowrap>"+row[k].str+"</td>");
				}else{
					sb.append("<td>"+row[k].str+"</td>");
				}
			}
			sb.append("</tr>");
			headerSb.append(sb);
			headerSb.append("\n");
			i += step;
		}
		return headerSb.toString();
	}
	
	
	public static class RV{
		public static int COUNT = 0;
		public static int PERCENT = 1;
		public static int AVERAGE = 2;
		public static int DISTRIBUTE = 3;
		public static int INDEX = 4;
		
		public RV(Object v,String s,int t,Object numerator,Object denominator){
			this.value = v;
			this.str = s;
			this.type = t;
			this.denominator = denominator;
			this.numerator = numerator;
		}
		
		public void add(RV rv){
			this.numerator = WebTool.add(this.numerator,rv.numerator);
			this.denominator = WebTool.add(this.denominator,rv.denominator);
			if(type == COUNT){
				this.value = WebTool.add(this.value,rv.value);
				this.str = this.value.toString();
			}else if(type == PERCENT){
				if(((Number)(denominator)).doubleValue() == 0){
					this.value = 0;
					this.str = "0%";
				}else{
					this.value =(float) (((Number)(numerator)).doubleValue()*100/((Number)(denominator)).doubleValue());
					this.str = String.format("%.1f%%", this.value);
				}
			}else if(type == AVERAGE){
				if(((Number)(denominator)).doubleValue() == 0){
					this.value = 0;
					this.str = "0";
				}else{
					this.value = ((Number)(numerator)).doubleValue()/((Number)(denominator)).doubleValue();
					this.str = String.format("%.1f", this.value);
				}
			}else if(type == DISTRIBUTE){
				if(((Number)(denominator)).doubleValue() == 0){
					this.value = 0;
					this.str = "0%";
				}else{
					this.value = ((Number)(numerator)).doubleValue()*100/((Number)(denominator)).doubleValue();
					this.str = String.format("%.1f%%", this.value);
				}
			}else if(type == INDEX){
				int v1 = ((Number)(this.value)).intValue();
				int v2 = ((Number)(rv.value)).intValue();
				if(str.indexOf("-")>0){
					int k = str.indexOf("-");
					int k1 = Integer.parseInt(str.substring(0,k));
					int k2 = Integer.parseInt(str.substring(k+1));
					this.value = Math.max(k2, v2);
					this.str = Math.min(k1, v2)+"-"+Math.max(k2, v2);
				}else{
					this.value = Math.max(v1, v2);
					this.str = Math.min(v1, v2)+"-"+Math.max(v1, v2);
				}
			}
		}
		
		Object value;
		String str;
		int type;
		Object numerator;
		Object denominator;
	}
	/**
	 * 得到某一列属性的值。
	 * names是这一列的定义，从上至下，比如 本尊装备.武器.颜色.白
	 * 此方法的设计是为了方便递归。
	 * 
	 * 如果存在合并的情况，names的定义会是这样：  本尊装备.武器#手套#头部.颜色.白
	 * 这样通过"武器#手套#头部"是无法找到对应
	 * 
	 * @param parentDataList
	 * @param parentDataClassList
	 * @param names
	 * @param index
	 * @return
	 */
	
	public RV calculateValue(ArrayList<Object> parentDataList,ArrayList<Class<?>> parentDataClassList,String names[],int index){
		
		Field f = null;
		
		try {
			//合并情况
			if(names[index].indexOf("#")>0){
				f = parentDataClassList.get(index).getDeclaredField(names[index].substring(0,names[index].indexOf("#")));
			}else{
				f = parentDataClassList.get(index).getDeclaredField(names[index]);
			}
		} catch (NoSuchFieldException e) {
			System.out.println("can't find field for {"+names[index]+"} in class {"+parentDataClassList.get(index).getName()+"}");
			e.printStackTrace(System.out);
			return new RV(0,"-",RV.COUNT,0,0);
		}
		f.setAccessible(true);
		if(f.getType().isPrimitive()){
			
			Object value = null;
			
			try {
				value = f.get(parentDataList.get(index));
			} catch (Exception e) {
				System.out.println("can't get field value for {"+names[index]+"} in class {"+parentDataClassList.get(index).getName()+"}");
				e.printStackTrace(System.out);
				return new RV(0,"-",RV.COUNT,0,0);
			} 
			
			Percent a = f.getAnnotation(Percent.class);
			if(a != null){
				Object denominator = null;
				try {
					denominator = calculateDenominatorValue(parentDataList,parentDataClassList,names,index,a.denominator(),a.keyword());
				} catch (Exception e) {
					System.out.println("can't get field Denominator{"+a.denominator()+","+a.keyword()+"} value for {"+names[index]+"} in class {"+parentDataClassList.get(index).getName()+"}");
					e.printStackTrace(System.out);
				}
				if(denominator == null){
					return new RV(Float.NaN,"N/A",RV.PERCENT,0,0);
				}else{
					if(((Number)denominator).intValue() == 0){
						if(((Number)value).floatValue() == 0){
							return new RV(0,"0%",RV.PERCENT,0,0);
						}else{
							return new RV(Float.NaN,"N/A",RV.PERCENT,0,0);
						}
					}else{
						double d = ((Number)value).doubleValue()*100/((Number)denominator).doubleValue();
						return new RV(d,String.format("%.1f%%", d),RV.PERCENT,value,denominator); 
					}
				}
			}
			
			Average b = f.getAnnotation(Average.class);
			if(b != null){
				Object denominator = null;
				try {
					denominator = calculateDenominatorValue(parentDataList,parentDataClassList,names,index,b.denominator(),b.keyword());
				} catch (Exception e) {
					System.out.println("can't get field Denominator{"+b.denominator()+","+b.keyword()+"} value for {"+names[index]+"} in class {"+parentDataClassList.get(index).getName()+"}");
					e.printStackTrace(System.out);
				}
				if(denominator == null){
					return new RV(Float.NaN,"N/A",RV.AVERAGE,0,0);
				}else{
					if(((Number)denominator).intValue() == 0){
						if(((Number)value).floatValue() == 0){
							return new RV(0,"0",RV.AVERAGE,0,0);
						}else{
							return new RV(Float.NaN,"N/A",RV.AVERAGE,0,0);
						}
					}else{
						double d = ((Number)value).doubleValue()/((Number)denominator).doubleValue();
						return new RV(d,String.format("%.1f", d),RV.AVERAGE,value,denominator); 
					}
				}
			}
			Count c = f.getAnnotation(Count.class);
			if(c != null){
				return new RV(value,value.toString(),RV.COUNT,value,1);
			}
			 
			Index d = f.getAnnotation(Index.class);
			if(d != null){
				return new RV(value,value.toString(),RV.INDEX,value,1);
			}
			
			return new RV(value,value.toString(),RV.COUNT,value,1);
		}else if(java.util.Map.class.isAssignableFrom(f.getType())){
			Distribute a = f.getAnnotation(Distribute.class);
			if(a != null){
				Map map = null;
				
				try {
					map = (Map)f.get(parentDataList.get(index));
				} catch (Exception e) {
					System.out.println("can't get field value for {"+names[index]+"} in class {"+parentDataClassList.get(index).getName()+"}");
					e.printStackTrace();
					return new RV(0,"-",RV.DISTRIBUTE,0,0);
				}
				
				Object value = map.get(names[index+1]);
				
				if(value == null){
					value = 0;
				}
				
				Object denominator = null;
				try {
					denominator = calculateDenominatorValue(parentDataList,parentDataClassList,names,index,a.denominator(),"");
				} catch (Exception e) {
					System.out.println("can't get field Denominator{"+a.denominator()+"} value for {"+names[index]+"} in class {"+parentDataClassList.get(index).getName()+"}");
					e.printStackTrace(System.out);
				}
				
				if(denominator == null){
					return new RV(Float.NaN,"N/A",RV.DISTRIBUTE,0,0);
				}else{
					if(((Number)denominator).intValue() == 0){
						if(((Number)value).floatValue() == 0){
							return new RV(0,"0",RV.DISTRIBUTE,0,0);
						}else{
							return new RV(Float.NaN,"N/A",RV.DISTRIBUTE,0,0);
						}
					}else{
						double d = ((Number)value).doubleValue() * 100 /((Number)denominator).doubleValue();
						return new RV(d,String.format("%.1f%%", d),RV.DISTRIBUTE,value,denominator); 
					}
				}
			}else{
				return new RV(0,"-",RV.DISTRIBUTE,0,0);
			}
		}else if(f.getType().getEnclosingClass() == dataClass){
			
			Object value = null;
			
			try {
				if(names[index].indexOf("#")>0){
					String mergeNames[] = names[index].split("#");
					Object mergeResult = f.getType().newInstance();
					for(int i = 0 ; i < mergeNames.length ; i++){
						Field  f2 = parentDataClassList.get(index).getDeclaredField(mergeNames[i]);
						try {
							mergeTowObject(parentDataList,parentDataClassList,f.getType(),mergeResult,f2);
						}catch(Exception e){
							System.out.println("[error] merge two object error, class {"+f2.getType()+"} field {"+f2.getName()+"}");
							e.printStackTrace(System.out);
						}
					}
					value = mergeResult;
				}else{
					value = f.get(parentDataList.get(index));
				}
			} catch (Exception e) {
				System.out.println("can't get field value for {"+names[index]+"} in class {"+parentDataClassList.get(index).getName()+"}");
				e.printStackTrace(System.out);
				return new RV(0,"-",RV.COUNT,0,0);
			} 
			parentDataList.add(value);
			parentDataClassList.add(f.getType());
	
			return calculateValue(parentDataList,parentDataClassList,names,index+1);
		}
		return new RV(0,"-",RV.COUNT,0,0);
	}
	/**
	 * 寻找分母
	 * @param parentDataList
	 * @param parentDataClassList
	 * @param names
	 * @param index
	 * @return
	 */
	public Object calculateDenominatorValue(ArrayList<Object> parentDataList,
			ArrayList<Class<?>> parentDataClassList,String names[],int index,
			String denominator,String keyword) throws Exception{
		
		try {
			Field denoF = parentDataClassList.get(index).getDeclaredField(denominator);
			if(denoF != null){
				if(denoF.getType().isPrimitive()){
					denoF.setAccessible(true);
					return denoF.get(parentDataList.get(index));
				}else{
					Distribute d = denoF.getAnnotation(Distribute.class);
					if(d != null){
						denoF.setAccessible(true);
						Map map = (Map)denoF.get(parentDataList.get(index));
						return map.get(keyword);
					}
				}
			}
			
		} catch (NoSuchFieldException e) {
		}

		if(index == 0) return null;
		
		return calculateDenominatorValue(parentDataList,parentDataClassList,names,index-1,denominator,keyword);
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
	public void mergeTowObject(ArrayList<Object> parentDataList,
			ArrayList<Class<?>> parentDataClassList,Class<?> mergeClass,Object mergeResult,Field field){
		
		Class<?> parentClass = parentDataClassList.get(parentDataClassList.size()-1);
		Object parentObject = parentDataList.get(parentDataList.size()-1);
		
		field.setAccessible(true);
		Object mergeObject = null;
		try {
			mergeObject = field.get(parentObject);
		} catch (Exception e) {
			System.out.println("can't get field value for {"+field.getName()+"} in class {"+parentClass.getName()+"}");
			e.printStackTrace(System.out);
			return;
		} 
		
		Field dfs[] = mergeClass.getDeclaredFields();
		
		for(int i = 0 ; i < dfs.length ; i++){
			Field f = dfs[i];
			
			if(java.lang.reflect.Modifier.isStatic(f.getModifiers()) || java.lang.reflect.Modifier.isTransient(f.getModifiers())) continue;
			
			f.setAccessible(true);
			if(f.getType().isPrimitive()){
				
				Object value1 = null;
				Object value2 = null;
				try {
					value1 = f.get(mergeResult);
					value2 = f.get(mergeObject);
				} catch (Exception e) {
					System.out.println("can't get field value for {"+f.getName()+"} in class {"+mergeClass.getName()+"}");
					e.printStackTrace(System.out);
				} 
				double d = ((Number)value1).doubleValue()+((Number)value2).doubleValue();
				try {
					f.set(mergeResult, add(value1,value2));
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
						map1 = (Map)f.get(mergeResult);
						map2 = (Map)f.get(mergeObject);
					} catch (Exception e) {
						System.out.println("can't get field value for {"+f.getName()+"} in class {"+mergeClass.getName()+"}");
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
							map1.put(key, add(value1,value2));
						}
					}
				}
			}else if(f.getType().getEnclosingClass() == dataClass){
				
				//递归
				
				//ArrayList<Object> parentDataList,
				//ArrayList<Class<?>> parentDataClassList,Class<?> mergeClass,Object mergeResult,Field field
				
				ArrayList<Object> newParentDataList = new ArrayList<Object>();
				ArrayList<Class<?>> newParentDataClassList = new ArrayList<Class<?>>();
				newParentDataList.addAll(parentDataList);
				newParentDataClassList.addAll(parentDataClassList);
				newParentDataList.add(mergeObject);
				newParentDataClassList.add(mergeClass);
				Class<?> newMergeClass = f.getType();
				Object newMergeResult = null;
				

				try {
					newMergeResult = f.get(mergeResult);
				} catch (Exception e) {
					System.out.println("can't get field value for {"+f.getName()+"} in class {"+mergeClass.getName()+"}");
					e.printStackTrace(System.out);
				} 
		
				mergeTowObject(newParentDataList,newParentDataClassList,newMergeClass,newMergeResult,f);
			}
		}
		
	}
	
	public static Object add(Object v1,Object v2){
		Class<?> cl = v1.getClass();
		if(cl == Long.class || cl == Long.TYPE || v2.getClass() == Long.class || v2.getClass() == Long.TYPE){
			long v = ((Number)v1).longValue() + ((Number)v2).longValue();
			return v;
		}else if(cl == Double.class || cl == Double.TYPE || v2.getClass() == Double.class || v2.getClass() == Double.TYPE){
			double v = (double)(((Number)v1).doubleValue() + ((Number)v2).doubleValue());
			return v;
		}else if(cl == Float.class || cl == Float.TYPE){
			float v = (float)(((Number)v1).floatValue() + ((Number)v2).floatValue());
			return v;
		}
		else if(cl == Integer.class || cl == Integer.TYPE){
			int v = ((Number)v1).intValue() + ((Number)v2).intValue();
			return v;
		}else if(cl == Short.class || cl == Short.TYPE){
			short v = (short)(((Number)v1).intValue() + ((Number)v2).intValue());
			return v;
		}else if(cl == Byte.class || cl == Byte.TYPE){
			byte v = (byte)(((Number)v1).intValue() + ((Number)v2).intValue());
			return v;
		}else{
			throw new java.lang.IllegalArgumentException("不支持此类型["+cl.getName()+"]加法");
		}
		
		
	}
}
