package com.xuanzhi.tools.listcache.concrete;

/**
 * 访问双向列表节点
 *
 */
public class AccessListNode {

	protected AccessListNode previous;
	protected AccessListNode next;
	protected BlockTreeNode block;
	
    /**
     * The creation timestamp is used in the case that the cache has a
     * maximum lifetime set. In that case, when
     * [current time] - [creation time] > [max lifetime], the block will be
     * deleted from cache.
     */
    protected long timestamp;
    
    protected AccessListNode(BlockTreeNode block,AccessListNode previous,AccessListNode next){
    	this.block = block;
    	this.previous = previous;
    	this.next = next;
    	timestamp = System.currentTimeMillis();
    }
    
    /**
     * Removes this node from the linked list that it is a part of.
     */
    protected void remove() {
        previous.next = next;
        next.previous = previous;
    }


}
