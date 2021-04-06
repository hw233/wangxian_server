package com.xuanzhi.tools.ds;

public class SingleLinkedList {
	
	public static class Node{
		protected Object obj;
		protected Node next;
		
		protected Node(Object obj,Node next){
			this.obj = obj;
			this.next = next;
		}
		
		public Object getObject(){
			return obj;
		}
	}
	
	private Node head = new Node(null,null);
	private int size = 0;
	
	public Node insertFirst(Object o){
		Node n = new Node(o,null);
		if(head.next == null){
			head.next = n;
		}else{
			n.next = head.next;
			head.next = n;
		}
		size++;
		return n;
	}
	
	public void removeFirst(){
		if(head.next == null) return;
		head.next = head.next.next;
		size--;
	}
	
	public Node insertAfter(Object o,Node node){
		Node n = new Node(o,null);
		if(node.next == null){
			node.next = n;
		}else{
			n.next = node.next;
			node.next = n;
		}
		size ++;
		return n;
	}
	
	public Node next(Node n){
		return n.next;
	}
	
	public Node getFirst(){
		return head.next;
	}
	
	public Node getLast(){
		Node n = head.next;
		if(n == null) return null;
		while(n.next != null){
			n = n.next;
		}
		return n;
	}
	public Node remove(Object o){
		Node p = head;
		while(p.next != null){
			if(p.next.obj.equals(o)){
				Node n = p.next;
				p.next = n.next;
				size --;
				return n;
			}
		}
		return null;
	}
	
	public int size(){
		return size;
	}
	
	public void clear(){
		head.next = null;
		size = 0;
	}
	
}
