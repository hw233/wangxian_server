package com.fy.engineserver.datasource.article.data.props;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;

/**
 * 修改的项目
 * 
 *
 */
public class IntProperty implements Cloneable,Serializable {
	
	public static final long serialVersionUID = 65539123123234343L;

	//要修改的属性名
	String fieldName;
	
	//要增加的值
	int fieldValue;
	
	//描述
	String comment;

	public IntProperty(){
		
	}
	public IntProperty(String fieldName,String comment){
		this.fieldName = fieldName;
		this.comment = comment;
	}
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(int fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * 将此属性修改作用到玩家身上
	 * @param p
	 */
	public void add(Player p){
		String fn = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		Class cl = p.getClass();
		Method ms[] = cl.getMethods();
		Method getter = null;
		Method setter = null;
		for(int i = 0 ; i < ms.length ; i++){
			if(ms[i].getName().equals("get"+fn) && ms[i].getParameterTypes().length == 0){
				getter = ms[i];
			}else if(ms[i].getName().equals("set"+fn) && ms[i].getParameterTypes().length == 1){
				setter = ms[i];
			}
		}
		if(getter != null && setter != null){
			try {
				Object o = getter.invoke(p, new Object[]{});
				if(o instanceof Byte){
					setter.invoke(p, new Object[]{(byte)(fieldValue+((Byte)o).byteValue())});
				}else if(o instanceof Short){
					setter.invoke(p, new Object[]{(short)(fieldValue+((Short)o).shortValue())});
				}else if(o instanceof Integer){
					setter.invoke(p, new Object[]{(int)(fieldValue+((Integer)o).intValue())});
				}else if(o instanceof Long){
					setter.invoke(p, new Object[]{(long)(fieldValue+((Long)o).longValue())});
				}else{
//					ArticleManager.logger.warn("[int_property] [add] [error] [unsupport_type:"+o.getClass()+"] [field:"+fieldName+"]");
					if(ArticleManager.logger.isWarnEnabled())
						ArticleManager.logger.warn("[int_property] [add] [error] [unsupport_type:{}] [field:{}]", new Object[]{o.getClass(),fieldName});
				}
			} catch (Exception e) {
				if(ArticleManager.logger.isWarnEnabled())
					ArticleManager.logger.warn("[int_property] [add] [error] [catch_exception] [field:"+fieldName+"]",e);
			}
		}else{
//			ArticleManager.logger.warn("[int_property] [add] [error] [getter_or_setter_not_exists] [field:"+fieldName+"]");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[int_property] [add] [error] [getter_or_setter_not_exists] [field:{}]", new Object[]{fieldName});
		}
	}
	
	/**
	 * 将此属性修改作用从玩家身上去除
	 * @param p
	 */
	public void subtract(Player p){
		String fn = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		Class cl = p.getClass();
		Method ms[] = cl.getMethods();
		Method getter = null;
		Method setter = null;
		for(int i = 0 ; i < ms.length ; i++){
			if(ms[i].getName().equals("get"+fn) && ms[i].getParameterTypes().length == 0){
				getter = ms[i];
			}else if(ms[i].getName().equals("set"+fn) && ms[i].getParameterTypes().length == 1){
				setter = ms[i];
			}
		}
		if(getter != null && setter != null){
			try {
				Object o = getter.invoke(p, new Object[]{});
				if(o instanceof Byte){
					setter.invoke(p, new Object[]{(byte)(-fieldValue+((Byte)o).byteValue())});
				}else if(o instanceof Short){
					setter.invoke(p, new Object[]{(short)(-fieldValue+((Short)o).shortValue())});
				}else if(o instanceof Integer){
					setter.invoke(p, new Object[]{(int)(-fieldValue+((Integer)o).intValue())});
				}else if(o instanceof Long){
					setter.invoke(p, new Object[]{(long)(-fieldValue+((Long)o).longValue())});
				}else{
//					ArticleManager.logger.warn("[int_property] [subtract] [error] [unsupport_type:"+o.getClass()+"] [field:"+fieldName+"]");
					if(ArticleManager.logger.isWarnEnabled())
						ArticleManager.logger.warn("[int_property] [subtract] [error] [unsupport_type:{}] [field:{}]", new Object[]{o.getClass(),fieldName});
				}
			} catch (Exception e) {
				if(ArticleManager.logger.isWarnEnabled())
					ArticleManager.logger.warn("[int_property] [subtract] [error] [catch_exception] [field:"+fieldName+"]",e);
			}
		}else{
//			ArticleManager.logger.warn("[int_property] [subtract] [error] [getter_or_setter_not_exists] [field:"+fieldName+"]");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[int_property] [subtract] [error] [getter_or_setter_not_exists] [field:{}]", new Object[]{fieldName});
		}
	}
	
	public String getStringForSave() {
		return fieldName==null?"":fieldName+"#@#"+fieldValue+"#@#"+(comment==null?"":comment);
	}
	
	public static IntProperty getFromString(String str) {
		String arr[] = str.split("#@#");
		if(arr.length == 2) {
			IntProperty ip = new IntProperty();
			ip.setFieldName(arr[0]);
			ip.setFieldValue(Integer.parseInt(arr[1]));
			return ip;
		}
		if(arr.length >= 3) {
			IntProperty ip = new IntProperty();
			ip.setFieldName(arr[0]);
			ip.setFieldValue(Integer.parseInt(arr[1]));
			ip.setComment(arr[2]);
			return ip;
		}
		return null;
	}
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
