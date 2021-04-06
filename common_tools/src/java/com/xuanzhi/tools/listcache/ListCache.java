package com.xuanzhi.tools.listcache;

import java.util.Comparator;

/**
 * 某一种排序下对象列表的缓存，缓存对象列表是以块列表的方式展开。
 * 块中的对象在这种排序下必须是连续的，而块与块之间可以不连续。
 * 
 * <b>重要说明</b>
 * <pre>
 * 请注意在构造ListCache实例的时候，需要提供一个比较器和一个属性名称，这两个参数非常重要，整个ListCache依据这两个参数运行。
 * 其中，比较器是用于比较两个对象的大小，比较器的方法compareTo(Object o1,Object o2)中的
 * 两个参数，第一个就是排序列表中的对象，第二个对象可以是排序列表中的对象，也可以是这个对象的一个包裹OldObject.
 * 所以，在实现这个比较方法的时候，一定要注意这种情况。
 * OldObject代表的是，Object的值已经发生了变化，但是需要用变化前的值来比较。
 * 假设ABC为排序对象的类，排序是根据viewCount属性排序的，典型的实现如下：
 * 
 * 			ListCache lc = new LruListCache(new Comparator(){
 * 				public int compareTo(Object o1,Object o2){
 * 					ABC a1 = (ABC)o1;	
 * 					if(o2 instanceof OldValue){
 * 						return a1.getViewCount() - (int)(((OldValue)o2).oldValue)
 * 					}else{
 * 						ABC a2 = (ABC)o2;
 * 						return a1.getViewCount() - a2.getViewCount();
 * 					}
 * 				}
 * 			},"viewCount","");
 * 
 * 			lc.initialize(128*512,1800000L,1024,2,512);
 * 
 * 另外，在ABC的实现类中，需要通知ListCache，这个对象的属性值发生了变化，以方便ListCache自动调整排序的顺序。
 * 通知ListCache的方法，就是调用PropertyChangeHandler类的静态方法fire(),示意代码如下：
 * 
 * 		public class ABC{
 * 			protected String id;
 * 			protected int viewCount;
 * 
 * 			public void setViewCount(int c){
 * 				int oldC = viewCount;
 * 				if( oldC!= c){
 * 					viewCount = c;
 * 					PropertyChangeHandler.fire(new PropertyChangeEvent(this,"viewCount",oldC,c));	
 * 				}
 * 			}
 * 
 * 			public int hashCode(){
 * 				return this.id.hashCode();
 * 			}
 * 
 * 			public boolean equals(Object o){
 * 				return id.equals(((ABC)o).id);
 * 			}
 * 		}
 * 注意，PropertyChangeEvent构造函数中的Property字符串和ListCache
 * 中的Property字符串必须一致，否则属性的变化通知不到要通知的ListCache。
 * 
 * 同时，一个对象可能存在于成千上万个ListCache中，所以PropertyChangeHandler.fire方法会通知到所有
 * 包含这个对象的ListCache。为了提高这种通知的效率，ABC对象会作为HashMap的key使用，所以请
 * 您重载hashCode()和equals()方法，以保证表现同一份数据的两个不同的对象的hashCode是一致的。
 * 
 * 您也可以将对象的ID放入列表中，而对象本身并不放入列表中，这样在比较器中的两个对象分别是ID或者OldObject。
 * 
 *</pre>
 *
 */

public interface ListCache {

	/**
	 * 初始化ListCache，ListCache必须被初始化后才能使用，否则会抛出RuntimeException。
	 * 
	 * @param maxCachedSize 
	 * @param maxBlockSize
	 * @param blockLifeTime
	 * @param minBlockSize
	 * @param maxBlockNum
	 * @throws Exception
	 */
	public void initailize(int maxCachedSize,long maxBlockLifeTime,int maxBlockSize,int minBlockSize,int maxBlockNum);
	
	/**
	 * 存储介子中所有的对象的个数，这个数不随着cache的数量而变
	 * @return 存储介子中所有的对象的个数
	 */
	public int getTotalSize();
	
	/**
	 * 设置存储介质中所有对象的个数
	 * @param ts
	 */
	public void setTotalSize(int ts);
	
	/**
	 * 已经cache到内存中的对象的个数，这个数一定小于或者等于存储介子中所有的对象的个数
	 * @return
	 */
	public int getCachedSize();
	
	/**
	 * 块的个数
	 * @return
	 */
	public int getBlockNum();
	
	/**
	 * 配置信息，这个Cache最多内cache多少个ID
	 * @return
	 */
	public int getMaxCachedSize();
	
	/**
	 * 配置信息，这个cache中block的大小最大值，
	 * 操作block最大值的要拆分
	 * @return
	 */
	public int getMaxBlockSize();
	
	/**
	 * 每个Block的生命周期，如果固定时间段内没有命中的Block，将会被清除，以节约内存空间
	 * @return
	 */
	public long getMaxBlockLifeTime();

	/**
	 * <pre>
	 * 向ListCache中增加新的列表数据。
	 * 如果加入的这个连续的对象块与ListCache中的多个块有重叠，
	 * 那么这个连续的对象块将被分割成多个小块加入到块列表中。
	 * 并且重叠的部分将进行对象的逐个校验，并记录下相同位置不一致的对象的个数。
	 * 最终将这个数返回。
	 * 不管返回值是什么，新的数据都会覆盖原来的数据。
	 * 图示：
	 * 
	 * 原来的块列表    [@@@@] ---> [@@@@]  ---> [@@@@]  --->  [@@@@]  --->  [@@@@]
	 * 新加入的块              [@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@]   
	 * 分割结果                [@@@]   [@@@@@@@@]   [@@@@@@@@@]
	 * 校验区域                   {     }      {     }       {  } 
	 * 最终列表中应该有8个块
	 * </pre>
	 * @param objs 已经根据指定的排序规则，排好的连续的对象
	 * @param start 已经根据指定的排序规则，第一个对象在完整列表中的位置
	 * @param offset 数组开始的位置
	 * @param size 此次加入对象的个数，一般情况下size等于objs数组的长度，
	 * 如果size大于objs数组的长度，将以数组长度计算。
	 * @return 返回false，表示ListCache中的排序已经与存储介子中的排序不一致，建议清空ListCache
	 */
	public int push(Object objs[],int start,int offset,int size);
	
	/**
	 * 判断目前ListCache是否包含指定范围内的连续对象，如果包含返回true，否则返回false
	 * @param start 范围开始的位置
	 * @param size 范围的大小
	 * @return 如果包含返回true，否则返回false
	 */
	public boolean isCovered(int start,int size);
	
	/**
	 * <pre>
	 * 判断目前ListCache是否包含指定范围内的连续对象,有多少返回多少，并且开始的位置也不确定。
	 * 所以，使用get方法前，因先用isCovered方法来判断。
	 * 具体的用法如下：<code>
	 * 		ListCacheManager lcm = ...;
	 * 		int start = ..;
	 * 		int size = ..;
	 * 
	 * 		Object objIds[] = null; //result
	 * 
	 * 		ListCache lc = lcm.getListCache(Class,String);
	 * 		if(lc != null){
	 * 			if(lc.isCovered(start,size)){
	 * 				 objIds = lc.get(start,size);
	 * 			}else{
	 * 				objIds = ....; // load from db
	 * 				if(!lc.add(objIds,start,size)){
	 * 					lc.clear();
	 * 					lc.add(objIds,start,size);
	 * 				}
	 * 			}
	 * 		}else{
	 * 			objIds = ....; // load from db
	 * 		}
	 * </code></pre>
	 * @param start 范围开始的位置
	 * @param size 范围的大小
	 * @return 返回指定范围内的连续的对象，注意ListCache中有多少返回多少
	 */
	public Object[] get(int start,int size);
	
	/**
	 * 指定的对象被删除，需要ListCache调整对象列表
	 * 此方法是默认给定的对象为实际存储列表中存在的对象，所以此方法一旦调用，
	 * ListCache就会发生调整，无论给定的对象是否合理。 
	 * 
	 * @param obj
	 */
	public boolean remove(Object obj);
	
	/**
	 * 新增一个对象，需要ListCache调整对象列表.
	 * 所谓的的新增，是指原有的存储系统中不存在这个对象。
	 * 比如新的用户注册，新的文件创建等等。
	 * 当一个新的对象产生后，如何知道哪些ListCache需要更新，是一个很难解决的问题。
	 * 目前这个问题没有包含在本实现内，需要使用者自己来解决这个问题。
	 * 
	 * 对于已知数量的ListCache，这个问题根本就不存在。
	 * 
	 * 对于未知数量的ListCache，比如搜索引擎中，每个搜索的Keyword都对应一个ListCache，
	 * 当Spider抓取了新的网页后，我们并不知道那些在ListCache中加入这个网页。
	 * 普遍的做法是，一旦发现ListCache.push产生冲突的时候，清空ListCache，
	 * 或者定时清空ListCache。搜索引擎一般是定期重新生成Index，这时可以清空所有ListCache。
	 * @param obj
	 */
	public void add(Object obj);
	
	/**
	 * 一个列表中的对象的某个属性值发生了变化，需要ListCache调整列表。
	 * 如果ListCache本身和这个属性无关，此方法直接返回。
	 * 否则，此方法相当于先删除这个对象，在新增这个对象。
	 * 
	 * @param obj 列表中的对象
	 * @param property 发生变化的属性，在ListCache实现类构造函数中会指定属性值，这个属性值必须和构造函数中的属性值一致
	 * @param oldValue 属性发生变化前的值
	 */
	public void update(Object obj,String property,Object oldValue);
	
	/**
	 * 清空Cache内的内容
	 */
	public void clear();

}
