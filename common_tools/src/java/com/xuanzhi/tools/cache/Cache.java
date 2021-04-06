package com.xuanzhi.tools.cache ;

/**
 * General purpose cache implementation. It stores objects associated with
 * unique keys in memory for fast access. All objects added to the cache must
 * implement the Cacheable interface, which requires objects to know their
 * size in memory. This restrictions allows the cache to never grow larger
 * than a specified amount.<p>
 *
 * If the cache does grow too large, objects will be removed such that those
 * that are accessed least frequently are removed first. Because expiration
 * happens automatically, the cache makes <b>no</b> gaurantee as to how long
 * an object will remain in cache after it is put in. The cache will return
 * null if the requested object is not found.<p>
 *
 * Optionally, a maximum lifetime for all objects can be specified. In that
 * case, objects will be deleted from cache after that amount of time, even
 * if they are frequently accessed. This feature is useful if objects put in
 * cache represent data that should be periodically refreshed; for example,
 * information from a database.<p>
 *
 * Cache is optimized for fast data access. The getObject() method has 0(n)
 * performance regardless of cache size. The other cache operations also
 * perform quite fast.<p>
 *
 * Cache objects are thread safe.<p>
 *
 * The algorithm for cache is as follows: a HashMap is maintained for fast
 * object lookup. Two linked lists are maintained: one keeps objects in the
 * order they are accessed from cache, the other keeps objects in the order
 * they were originally added to cache. When objects are added to cache, they
 * are first wrapped by a CacheObject which maintains the following pieces
 * of information:<ul>
 *    <li> The size of the object (in bytes).
 *    <li> A pointer to the node in the linked list that maintains accessed
 *         order for the object. Keeping a reference to the node lets us avoid
 *         linear scans of the linked list.
 *    <li> A pointer to the node in the linked list that maintains the age
 *         of the object in cache. Keeping a reference to the node lets us avoid
 *         linear scans of the linked list.</ul>
 *
 * To get an object from cache, a hash lookup is performed to get a reference
 * to the CacheObject that wraps the real object we are looking for.
 * The object is subsequently moved to the front of the accessed linked list
 * and any necessary cache cleanups are performed. Cache deletion and expiration
 * is performed as needed.
 *
 * @see Cacheable
 * 
 */

public interface Cache extends Cacheable 
{
 
    /**
     * Returns the maximum size of the cache in bytes. If the cache grows too
     * large, the least frequently used items will automatically be deleted so
     * that the cache size doesn't exceed the maximum.
     *
     * @return the maximum size of the cache in bytes.
     */
    public int getMaxSize() ;
    
    /**
     * Sets the maximum size of the cache in bytes. If the cache grows too
     * large, the least frequently used items will automatically be deleted so
     * that the cache size doesn't exceed the maximum.
     *
     * @param maxSize the maximum size of the cache in bytes.
     */
    public void setMaxSize(int maxSize) ;

    /**
     * Returns the number of objects in the cache.
     *
     * @return the number of objects in the cache.
     */
    public int getNumElements() ;
  
    /**
     * Adds a new Cacheable object to the cache. The key must be unique.
     *
     * @param key a unique key for the object being put into cache.
     * @param object the  object to put into cache.
     * @return return the old object in cache
     */
    public Cacheable put(Object key, Cacheable object); 
  
    /**
     * Gets an object from cache. This method will return null under two
     * conditions:<ul>
     *    <li>The object referenced by the key was never added to cache.
     *    <li>The object referenced by the key has expired from cache.</ul>
     *
     * @param key the unique key of the object to get.
     * @return the object corresponding to unique key.
     */
    public Cacheable get(Object key) ;
       
    /**
     * Removes an object from cache.
     *
     * @param key the unique key of the object to remove.
     */
    public void remove(Object key) ;
	
    /**
     * Clears the cache of all objects. The size of the cache is reset to 0.
     */
    public void clear() ;
    
    /**
     * Returns a collection view of the values contained in the cache.
     * The Collection is unmodifiable to prevent cache integrity issues.
     *
     * @return a Collection of the cache entries.
     */
    public java.util.Collection values() ;
	
	 /**
     * Returns a set view of the key-values contained in the cache.
     * The Set is unmodifiable to prevent cache integrity issues.
     *
     * @return a Set of the cache key-value entries.
     */
    public java.util.Set entrySet() ;
	
  
    /**
     * Returns the number of cache hits. A cache hit occurs every
     * time the get method is called and the cache contains the requested
     * object.<p>
     *
     * Keeping track of cache hits and misses lets one measure how efficient
     * the cache is; the higher the percentage of hits, the more efficient.
     *
     * @return the number of cache hits.
     */
    public long getCacheHits(); 
 
    /**
     * Returns the number of cache misses. A cache miss occurs every
     * time the get method is called and the cache does not contain the
     * requested object.<p>
     *
     * Keeping track of cache hits and misses lets one measure how efficient
     * the cache is; the higher the percentage of hits, the more efficient.
     *
     * @return the number of cache hits.
     */
    public long getCacheMisses() ;

}
