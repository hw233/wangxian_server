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
 */
public interface CacheListener
{
	// lift time out
	public static final int LIFT_TIMEOUT   = 0;
	// size over flow
	public static final int SIZE_OVERFLOW  = 1;
	// manual remove
	public static final int MANUAL_REMOVE  = 2;
	//sync remove
	public static final int SYNC_REMOVE = 3;
	
	public void remove(int type);
}
