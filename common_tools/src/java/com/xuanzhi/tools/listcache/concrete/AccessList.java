package com.xuanzhi.tools.listcache.concrete;

/**
 * Simple LinkedList implementation. The main feature is that list nodes
 * are public, which allows very fast delete operations (when one has a
 * reference to the node that is to be deleted).<p>
 *
 * The linked list implementation was specifically written for the CoolServlets
 * cache system. While it can be used as a general purpose linked list, for
 * most applications, it is more suitable to use the linked list that is part
 * of the Java Collections package.
 * 
 */
public class AccessList {


	    /**
	     * The root of the list keeps a reference to both the first and last
	     * elements of the list.
	     */
	    private AccessListNode head = new AccessListNode(null, null, null);

	    /**
	     * Creates a new linked list.
	     */
	    public AccessList() {
	        head.next = head.previous = head;
	    }
	     
	    /**
	     * Returns the first linked list node in the list.
	     *
	     * @return the first element of the list.
	     */
	    public AccessListNode getFirst() {
	        AccessListNode node = head.next;
	        if (node == head) {
	            return null;
	        }
	        return node;
	    }
	    
		/**
		 *  Return true if there is at least one object in the linked list.
		 * 
		 * @return true if there is at least one object in the linked list.
		 */
		public boolean isEmpty()
		{
			return ((head.next == head) || (head.previous == head));
		}
	    /**
	     * Returns the last linked list node in the list.
	     *
	     * @return the last element of the list.
	     */
	    public AccessListNode getLast() {
	        AccessListNode node = head.previous;
	        if (node == head) {
	            return null;
	        }
	        return node;
	    }

	    /**
	     * Adds a node to the beginning of the list.
	     *
	     * @param node the node to add to the beginning of the list.
	     */
	    public AccessListNode addFirst(AccessListNode node) {
	        node.next = head.next;
	        node.previous = head;
	        node.previous.next = node;
	        node.next.previous = node;
	        node.timestamp = System.currentTimeMillis();
	        return node;
	    }


	    /**
	     * Erases all elements in the list and re-initializes it.
	     */
	    public void clear() {
	        //Remove all references in the list.
	        AccessListNode node = getLast();
	        while (node != null) {
	            node.remove();
	            node = getLast();
	        }

	        //Re-initialize.
	        head.next = head.previous = head;
	    }

	    /**
	     * Returns a String representation of the linked list with a comma
	     * delimited list of all the elements in the list.
	     *
	     * @return a String representation of the LinkedList.
	     */
	    public String toString() {
	        AccessListNode node = head.next;
	        StringBuffer buf = new StringBuffer();
	        while (node != head) 
	        {
	            buf.append(node.toString()).append(":");
	            buf.append(node.timestamp).append("  ");
	            
	            node = node.next;
	        }
	        return buf.toString();
	    }

}
