package com.xuanzhi.tools.ds ;

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
public class DoubleLinkedList {

public static class Node {

    protected Node previous;
    protected Node next;
    protected Object object;

    /**
     * Constructs a new linked list node.
     *
     * @param object the Object that the node represents.
     * @param next a reference to the next Node in the list.
     * @param previous a reference to the previous Node in the list.
     */
    protected Node(Object object, Node next,
            Node previous)
    {
        this.object = object;
        this.next = next;
        this.previous = previous;
    }

    /**
     * Removes this node from the linked list that it is a part of.
     */
    public void remove() {
        previous.next = next;
        next.previous = previous;
    }
    
    /**
     * Returns a String representation of the linked list node by calling the
     * toString method of the node's object.
     *
     * @return a String representation of the Node.
     */
    public String toString() {
        return object.toString();
    }
    
    public Object getObject(){
    	return object;
    }
    
    public void setObject(Object obj){
    	object = obj;
    }
}

	
    /**
     * The root of the list keeps a reference to both the first and last
     * elements of the list.
     */
    private Node head = new Node("head", null, null);

    /**
     * Creates a new linked list.
     */
    public DoubleLinkedList() {
        head.next = head.previous = head;
    }
     
    /**
     * Returns the first linked list node in the list.
     *
     * @return the first element of the list.
     */
    public Node getFirst() {
        Node node = head.next;
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
    public Node getLast() {
        Node node = head.previous;
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
    public Node addFirst(Node node) {
        node.next = head.next;
        node.previous = head;
        node.previous.next = node;
        node.next.previous = node;
        return node;
    }

    /**
     * Adds an object to the beginning of the list by automatically creating a
     * a new node and adding it to the beginning of the list.
     *
     * @param object the object to add to the beginning of the list.
     * @return the node created to wrap the object.
     */
    public Node addFirst(Object object) {
        Node node = new Node(object, head.next, head);
        node.previous.next = node;
        node.next.previous = node;
        return node;
    }

    /**
     * Adds an object to the end of the list by automatically creating a
     * a new node and adding it to the end of the list.
     *
     * @param object the object to add to the end of the list.
     * @return the node created to wrap the object.
     */
    public Node addLast(Object object) {
        Node node = new Node(object, head, head.previous);
        node.previous.next = node;
        node.next.previous = node;
        return node;
    }
    

    public Node addAfter(Node node,Object object){
    	Node n = new Node(object, node.next, node);
    	n.next.previous = n;
    	n.previous.next = n;
    	return n;
    }
    
    public Node addBefore(Node node,Object object){
    	Node n = new Node(object, node, node.previous);
    	n.next.previous = n;
    	n.previous.next = n;
    	return n;
    }

    public Node next(Node n){
    	if(n.next == head) return null;
    	return n.next;
    }

    public Node previous(Node n){
    	if(n.previous == head) return null;
    	return n.previous;
    }
			
    /**
     * Erases all elements in the list and re-initializes it.
     */
    public void clear() {
        //Remove all references in the list.
        Node node = getLast();
        while (node != null) {
            node.remove();
            node = getLast();
        }

        //Re-initialize.
        head.next = head.previous = head;
    }

}
