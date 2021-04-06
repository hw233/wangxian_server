package com.xuanzhi.tools.cache.lateral;


import com.xuanzhi.tools.cache.CacheManager;

/**
 * <pre>
 * 分布式Cache管理类的实现
 * 
 * 具体的使用方法：
 * 		DefaultDistributeCacheManager ddcm = DefaultDistributeCacheManager();
 * 		try{
 *			Element configuration = XmlUtil.load("/usr/local/AProject/conf/ddc.xml").getDocumentElement();
 * 			ddcm.configure(configuration);
 * 		}catch(Exception e){
 * 			System.out.println("分布式Cache配置有问题："+e);
 * 			System.exit(1);
 * 		}
 * 		...
 * 
 * 		DistributeCache dc = ddcm.getDistributeCache("some-name");
 * 		dc.get(...);
 * 		dc.put(...,...);
 * </pre>
 * 
 *
 */
public interface LateralCacheManager extends CacheManager {

	public LateralCache getCache(String name);
}
