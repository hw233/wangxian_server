package com.xuanzhi.tools.statistics;

import java.util.*;

import com.xuanzhi.tools.text.StringUtil;

/**
 * 维度，维度是指一个统计项目中的一个角度，比如用户维度，地区维度，产品维度等。
 * 
 * 一个维度中可以包含多个粒度，粒度是有层次关系的，第一个粒度包含第二个粒度，第二个粒度包含第三个粒度。
 * 同时每个维度可以有一些属性值。属性不用来统计，只是作为冗余的数据存储在数据库中，方便对记录进行过滤。
 * 
 * 比如，地区维度有三个粒度，第一个粒度为国家，第二个粒度为省份，第三个粒度为城市。
 * 国家包含省份，省份包含城市。
 * 
 * 给维度的粒度设置值的时候，遵循如下的规则：
 * 
 * 	   必须从大粒度开始，依次设置粒度值。
 *     
 * 维度的粒度名称对应数据库中的表的字段名，所以粒度必须符合数据库字段名的要求，
 * 否则，将出现不可预料的问题。
 * 
 * 同时，一个维度的粒度信息，必须在其实现类的构造函数中制定，维度本身不提供增加减少粒度的方法。
 * 
 * 维度本身可以作为Map的key，一个维度和另一个维度相等，当且仅当所有的粒度以及粒度的值都相同。
 * 
 * 
 * 注意：所有的粒度都必须是小写的，否则查不出数据。
 * 
 * 		更新，增加了展开的独立标记，这样就可以对范围和多选进行展开了。
 *
 */
public abstract class Dimension {
	
	public final static String LISTMARK = "***";
	
	public final static String NULL = "null";
	
	protected String name;
	
	protected LinkedHashMap<String,String> granulas = new LinkedHashMap<String,String>();
	
	protected LinkedHashMap<String,Object> attachments = new LinkedHashMap<String,Object>();
	
	protected LinkedHashMap<String,Boolean> granulaListMark = new LinkedHashMap<String,Boolean>();
	
	/**
	 * 表明数据库中的字段是char型的还是varchar型的，默认为char型的
	 */
	protected boolean dynamicColumn = false;
	
	/**
	 * 字段的长度，默认为64个字节
	 */
	protected int columnLength = 64;
	
	/**
	 * 是否支持以什么开头的查询，如果支持，对输入串以"*"结尾的，按startWith来查询
	 */
	protected boolean canStartWith = true;
	
	/**
	 * 是否支持多选查询，如果支持多选查询，那么对输入串以","分隔查询，默认不支持
	 */
	protected boolean canMultiSelect = true;
	
	/**
	 * 是否支持范围查询，如果支持，那么输入串的格式为  xxxxx~xxxxx
	 * 左边为小，右边为大，两边都包含
	 */
	protected boolean canRangeSelect = false;
	
	/**
	 * 是否支持展开此维度，对于存在大量可能值得维度，禁止展开，默认可以展开
	 */
	protected boolean canListMark = true;
	
	protected Dimension(String name){
		this.name = name;
	}
	
	protected String uniqueName;
	
	/**
	 * 维度唯一英文名称，此名称将作为数据库表名的一部分，所以必须为英文，且不含特殊字符。
	 * @return
	 */
	public String getDimensionUniqueName(){
		if(uniqueName != null) return uniqueName;
		if(this.granulas.size() > 0){
			String ss[] = getAllGranula();
			uniqueName = ss[0];
			return uniqueName;
		}else{
			if(StringUtil.isValidIdentity(name)) return name;
			return StringUtil.toHex(name.getBytes());
		}
	}
	
	public void setDimensionUniqueName(String uniqueName){
		this.uniqueName = uniqueName;
	}
	
	public int getColumnLength(){
		return this.columnLength;
	}
	/**
	 * 是否支持展开此维度，对于存在大量可能值得维度，禁止展开，默认可以展开
	 */
	public boolean isCanListMark(){
		return canListMark;
	} 
	
	/**
	 * 是否支持多选查询，如果支持多选查询，那么对输入串以","分隔查询
	 */
	public boolean isCanMultiSelect(){
		return canMultiSelect;
	}
	/**
	 * 判断是否能够前缀精确模糊查找
	 * @return
	 */
	public boolean isCanStartWith(){
		return canStartWith;
	}
	
	/**
	 * 是否支持范围查询，如果支持，那么输入串的格式为  xxxxx~xxxxx
	 * 左边为小，右边为大，两边都包含
	 */
	public boolean isCanRangeSelect(){
		return this.canRangeSelect;
	}
	
	/**
	 * 当打印或者显示粒度时，调用此方法。
	 * 子类可以根据需要，改变粒度的显示。
	 * 
	 * @param granula
	 * @return
	 */
	public String getGranulaDisplayName(String granula){
		return granula;
	}
	
	/**
	 * 当打印或者显示粒度的值时，调用此方法。
	 * 子类可以根据需要，改变粒度的值的显示。
	 * 
	 * @param granula
	 * @return
	 */
	public String getDisplayValue(String granula,String value){
		return value;
	}
	
	/**
	 * 维度的名称
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 获得这个维度下有几个粒度 
	 * @return
	 */
	public int getNumOfGranula(){
		return granulas.size();
	}
	
	/**
	 * 获得所有的粒度
	 * @return
	 */
	public String[] getAllGranula(){
		String keys[] = granulas.keySet().toArray(new String[0]);
		return keys;
	}
	
	/**
	 * 获得已经设置值的粒度，这个值包括列表标记
	 * @return
	 */
	public String[] getGranulas(){
		String keys[] = granulas.keySet().toArray(new String[0]);
		ArrayList<String> al = new ArrayList<String>();
		for(int i = 0 ; i < keys.length ; i++){
			String value = granulas.get(keys[i]);
			if(!isNull(value) || isListMark(keys[i])){
				al.add(keys[i]);
			}else{
				break;
			}
		}
		return (String[])al.toArray(new String[0]);
	}
	/**
	 * 得到第几个粒度，如果给定的index超出范围，抛出IllegalArgumentException
	 * @param index
	 * @return
	 */	
	public String getGranula(int index) throws IllegalArgumentException{
		String keys[] = granulas.keySet().toArray(new String[0]);
		if(index >= 0 && index < keys.length)
			return keys[index];
		throw new IllegalArgumentException("index ["+index+"] must between [0,"+keys.length+")");
	}
	
	/**
	 * 判断这个维度是否为更新数据准备好，也就是说，各个粒度都已经设置上了值
	 * @return
	 */
	public boolean isReadyForUpdateData(){
		String keys[] = granulas.keySet().toArray(new String[0]);
		for(int i = 0 ; i < keys.length ; i++){
			String value = granulas.get(keys[i]);
			if(isNull(value) || LISTMARK.equalsIgnoreCase(value))
				return false;
		}
		return true;
	}
	
	/**
	 * 得到某个粒度值，如果没有设置这个粒度的值，返回null
	 * 如果这个粒度设置为列表标记，也返回null
	 * 
	 * @param granula
	 * @return
	 */
	public String getValue(String granula){
		String value = granulas.get(granula);
		if(isNull(value) || LISTMARK.equalsIgnoreCase(value)) return null;
		return value;
	}
	
	/**
	 * 设置某个粒度的值，在设置这个粒度的值的时候，
	 * 这个粒度前面的粒度（粒度更大的粒度）必须已经设置值，否则抛出IllegalArgumentException
	 */
	public void setValue(String granula,String value) throws IllegalArgumentException{
		String keys[] = granulas.keySet().toArray(new String[0]);
		int index = -1;
		for(int i = 0 ; i < keys.length ; i++){
			if(keys[i].equalsIgnoreCase(granula)){
				index = i;
				break;
			}
		}
		if(index == -1){
			throw new IllegalArgumentException("granula ["+granula+"] is not belong to this dimension ["+name+"]");
		}
		
		if(isNull(value)){
			granulas.put(granula,NULL);
			return;
		}

		for(int i = 0 ; i < index ; i++){
			String v = granulas.get(keys[i]);
			if(isNull(v)){
				throw new IllegalArgumentException("must set the bigger granula["+keys[i]+"]'s value before set granula ["+granula+"] value");
			}
			//if(LISTMARK.equalsIgnoreCase(value) == false && isListMark(keys[i]) && v == null){
			//	throw new IllegalArgumentException("the bigger granula ["+keys[i]+"] has been set to listmark");
			//}
		}
		//if(value != null){
		//	byte bytes[] = value.getBytes();
		//	if(bytes.length > columnLength){
		//		value = new String(bytes,0,columnLength-1);
		//	}
		//}
		granulas.put(granula,value);
	}
	
	/**
	 * 设置某个粒度为列表标记，在每次查询时，结果将以这个粒度的对应的所有值，进行列表显示。
	 * 比如，如果这个粒度为接入点，那么查询的结果是列出某个运营商下所有接入点的数据
	 * 
	 * 这个粒度前面的粒度（粒度更大的粒度）必须已经设置值，否则抛出IllegalArgumentException
	 */
	
	public void setListMark(String granula) throws IllegalArgumentException{
		granulaListMark.put(granula, true);
	}
	
	public void clearListMark(String granula) throws IllegalArgumentException{
		granulaListMark.remove(granula);
	}
	
	/**
	 * 判断某个粒度是否设置了列表标记
	 * @param granula
	 * @return
	 */
	public boolean isListMark(String granula){
		Boolean b = granulaListMark.get(granula);
		if(b != null && b.booleanValue() == true){
			return true;
		}
		String value = granulas.get(granula);
		if(value != null && value.equals(LISTMARK)){
			return true;
		}
		return false;
	}
	
	public boolean isNull(String value){
		return (value == null || value.equalsIgnoreCase(NULL));
	}
	
	/**
	 * 设置附加的属性值，附加的属性将作为数据库中的一个字段来表示
	 * @param attachName
	 * @param value
	 */
	public void setAttachment(String attachName,Object value){
		this.attachments.put(attachName,value);
	}
	
	public Object getAttachment(String attachName){
		return this.attachments.get(attachName);
	}
	
	public String [] getAttachmentNames(){
		return this.attachments.keySet().toArray(new String[0]);
	}
	
	public boolean isGranula(String name){
		return granulas.containsKey(name);
	}
	
	public boolean isAttachment(String name){
		return attachments.containsKey(name);
	}
	
	public boolean equals(Object o){
		if(o instanceof Dimension){
			Dimension d = (Dimension)o;
			return granulas.equals(d.granulas);
		}
		return false;
	}
	
	public int hashCode(){
		return granulas.hashCode();
	}
	/**
	 *  将维度的各个粒度值以一个字符串的方式显示，格式如下：
	 *  name{granula1:value1,granula2.value2,...}
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		String keys[] = getAllGranula();
		sb.append(this.name+"{");
		for(int i = 0 ; i < keys.length ;i++){
			String v = granulas.get(keys[i]);
			if(v == null) v = "-";
			sb.append(keys[i]+":"+v);
			if(i < keys.length - 1) sb.append(",");
		}
		sb.append("}");
		return sb.toString();
	}
}
