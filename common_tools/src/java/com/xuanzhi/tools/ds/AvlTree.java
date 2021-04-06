package com.xuanzhi.tools.ds;


import java.util.Comparator;

/**
 * A height-balanced 1-tree implementation of the AVLTree.
 * this class is not thread safe.
 * 
 * 注意：此实现不能在修改的时候遍历，或者在遍历的时候修改。一定要区分Iterator的用法；
 * 		以下的用法将产生不可预知的行为：
 * 			AvlTree tree = .....;
 * 			TreeNode node = tree.find(o);
 * 			while(node != null){
 * 				TreeNode n = node;
 * 				node = tree.next(node);
 * 				tree.remove(n); // tree.insert(o2);
 * 			}
 * 		导致这个问题的原因是：每次对AvlTree修改时（insert或者remove），Avltree会对树结构进行调整，
 * 		甚至有的调整将某个节点上的数据与别的节点上的数据互换，而节点本身没有改变。
 * 		可以用下面的方法：
 * 			AvlTree tree = ....;
 * 			while(o != null){
 * 				TreeNode node = tree.find(o);
 * 				if(node == null) break;
 * 				TreeNode next = tree.next(node);
 * 				o = next == null?null:next.getObject(); 
 * 				tree.remove(node);
 * 			}
 * 			
 * 		树结构一般情况下都不允许边遍历边修改，容易产生死循环等问题。
 * 
 */

public final class AvlTree
{

	public static class TreeNode{
		
		protected TreeNode left;
		protected TreeNode right;
		protected TreeNode parent;
		protected int balance;
		
		protected Object o;
		
		public TreeNode(Object o,TreeNode left,  TreeNode right ,TreeNode parent)
		{
			this.left = left;
			this.right = right;
			this.parent = parent;
			this.o = o;
			balance = 0;
		}
		
		/**
		 *  left return -1
		 *  right return 1
		 */
		public int side()
		{
			if( parent == null) return 0;
			if( parent.left == this) return -1;
			if( parent.right == this) return 1;
			return 0;
		}
		
		public TreeNode child(int i)
		{
			if(i == -1) return left;
			else if(i == 1) return right;
			else return this;
		}

		public Object getObject(){
			return o;
		}
	}
	
	
	public AvlTree(Comparator comparator){
		this.comparator = comparator;
	}
	
	protected Comparator comparator;
	protected TreeNode root = null;
	protected int treeSize = 0;
	protected String name;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Report the number of elements in the tree.
	 * @return The number of elements
	 **/
	public int size()
	{
		return treeSize;
	}

	/**
	 * Report whether this tree has no elements.
	 * @return true if size() == 0
	 **/

	public boolean isEmpty()
	{
		return ( treeSize == 0 );
	}
	
	public void clear(){
		root = null;
		treeSize = 0;
	}
	
	private void rebalance(TreeNode btnode, int i)
    {
        for(; btnode != null; btnode = btnode.parent)
        {
            if(btnode.balance != i)
                btnode.balance += i;
            else
                btnode = balance(btnode);
            if(btnode.balance == 0)
                break;
            i = btnode.side();
        }

    }

	private TreeNode balance(TreeNode btnode)
    {
        int i = btnode.balance;
        TreeNode btnode1 = btnode.child(i);
        TreeNode btnode2 = btnode1.child(-i);
        if(btnode1.balance == -i)
        {
            rotate(btnode1, i);
            if(btnode2.balance == -i)
            {
                btnode.balance = 0;
                btnode1.balance = i;
            }
            else
            if(btnode2.balance == i)
            {
                btnode.balance = -i;
                btnode1.balance = 0;
            }
            else
            {
                btnode.balance = 0;
                btnode1.balance = 0;
            }
            btnode2.balance = 0;
        }
        else
        if(btnode1.balance == i)
        {
            btnode.balance = 0;
            btnode1.balance = 0;
        }
        else
        if(btnode1.balance == 0)
        {
            btnode.balance = i;
            btnode1.balance = -i;
        }
        btnode = rotate(btnode, -i);
        return btnode;
    }

	private TreeNode rotate(TreeNode btnode,int i)
	{
		TreeNode btnode1 = btnode.child(-i);
		TreeNode btnode2 = btnode1.child(i);
        join(btnode, btnode2, -i);
        join(btnode.parent, btnode1, btnode.side());
        join(btnode1, btnode, i);
        if(btnode == root)
            root = btnode1;
        return btnode1;

	}

	
	private void join(TreeNode btnode, TreeNode btnode1, int i)
    {
        if(btnode1 != null)
            btnode1.parent = btnode;
        if(btnode != null)
        {
            if(i == -1)
            {
                btnode.left = btnode1;
                return;
            }
            if(i == 1)
                btnode.right  = btnode1;
        }
    }
	
	private void debalance(TreeNode btnode, int i)
    {
        for(; btnode != null; btnode = btnode.parent)
        {
            if(btnode.balance != -i)
                btnode.balance -= i;
            else
                btnode = balance(btnode);
            if(btnode.balance != 0)
                break;
            i = btnode.side();
        }
    }

	/**
	 * Maps the specified key to the specified record in this container.
	 * Neither the key nor the record can be null. The record can be retrieved
	 * by calling the find method with a key that is equal to the original key.
	 * The class of the first key that is put in the list is registered. Only
	 * keys of this class may furthermore be inserted in the list. Duplicate keys
	 * are not allowed.
	 * @param key The key used for retrival.
	 * @param record The record.
	 * @exception NullPointerException if the key or record is null
	 * @exception RuntimeException if the key class is different to previously
	 * registered key class or if attempted to put a duplicate key.
	 **/

	public TreeNode insert(Object obj)
	{
		// if tree is empty, insert as root node and return
		TreeNode newNode = new TreeNode(obj,null, null ,null);
		
		if( root == null )
		{
			root = newNode;
			treeSize++;
			return root;
		}

		TreeNode p = root;
		TreeNode node = null;
		int diff = 0;

		
		for( ; p != null && (diff = comparator.compare(obj,p.o)) != 0; p = p.child(diff))
			node = p;
		
		if( p != null)
		{
			return p;
		}
		
		join(node,newNode,diff);
		
		rebalance(node,diff);
		
		treeSize++;
		
		return newNode;
	}

	
	public TreeNode find(Object o)
	{
		if( isEmpty() )
		{
			return null; // tree empty
		}

		//		 Walk down the tree searching for key
		TreeNode n;
		TreeNode p = root;
		int diff = 0;
		while( p != null )
		{
			diff = comparator.compare(o,p.o);
			if(diff == 0){
				return p;
			}
			p = (diff < 0)? p.left : p.right;
		}
		return null;
	}
	
	public TreeNode findNearestBigger(Object o)
	{
		if( isEmpty() )
		{
			return null; // tree empty
		}

		//		 Walk down the tree searching for key
		TreeNode n;
		TreeNode p = root;
		int diff = 0;
		while( p != null )
		{
			diff = comparator.compare(o,p.o);
			if(diff == 0){
				return p;
			}
			
			if(diff < 0 && p.left == null) return p;
			if(diff > 0 && p.right == null){
				return this.next(p);
			}
			p = (diff < 0)? p.left : p.right;
		}
		return null;
	}
	
	public void remove(Object o){
		TreeNode tn = find(o);
		if(tn != null){
			remove(tn);
		}
	}

	/**
	 * Returns the record to which the key is minimum in this container. Only
	 * keys of the class registered on first insert operation may be inserted in
	 * the list.
	 * @param key a key in this container
	 * @return the record to which the key is mapped in this container;
	 * null if the key is not mapped to any value in this container.
	 * @exception RuntimeException if the key class is different to previously
	 * registered key class.
	 **/

	public TreeNode minimum()
	{
		if( isEmpty() )
		{
			return null; // tree empty
		}

		// Walk down the tree searching for key
		TreeNode p = root;
		
		while( p.left  != null )
		{
			p = p.left ;
		}

		return p ;
	}
	
	
	
	/**
	 * Returns the record to which the key is maximum in this container. Only
	 * keys of the class registered on first insert operation may be inserted in
	 * the list.
	 * @param key a key in this container
	 * @return the record to which the key is mapped in this container;
	 * null if the key is not mapped to any value in this container.
	 * @exception RuntimeException if the key class is different to previously
	 * registered key class.
	 **/

	public TreeNode maximum()
	{
		if( isEmpty() )
		{
			return null; // tree empty
		}

		// Walk down the tree searching for key
		TreeNode p = root;
		
		while( p.right  != null )
		{
			p = p.right ;
		}

		return p ;
	}
	
		/**
	 * Returns the record to which the key is previous in this container. Only
	 * keys of the class registered on first insert operation may be inserted in
	 * the list.
	 * @param key a key in this container
	 * @return the record to which the key is mapped in this container;
	 * null if the key is not mapped to any value in this container.
	 * @exception RuntimeException if the key class is different to previously
	 * registered key class.
	 **/

	public TreeNode previous(TreeNode p)	throws RuntimeException
	{
		// Walk down the tree searching for key
		
		if(p.left  != null)
		{
			p = p.left ;
			while(p.right != null)
			{
				p = p.right ;
			}
			return p ;
		}
		else // p.left == null
		{
			int side = p.side() ;
			if(side == 1) return p.parent ; 
			if( side == -1)
			{
				while(p.parent != null)
				{
					p = p.parent ;
					side = p.side();
					if( side == 1) 
							return p.parent ; 
				}
			}
			return null;
					
		}
	}

	
	

	/**
	 * Returns the record to which the key is next in this container. Only
	 * keys of the class registered on first insert operation may be inserted in
	 * the list.
	 * @param key a key in this container
	 * @return the record to which the key is mapped in this container;
	 * null if the key is not mapped to any value in this container.
	 * @exception RuntimeException if the key class is different to previously
	 * registered key class.
	 **/

	public TreeNode next(TreeNode p)
	{
				if(p.right != null)
				{
					p = p.right ; 
					while(p.left != null)
					{
						p = p.left ;
					}
					return p ;
				}
				else 
				{
					int side = p.side() ;
					if(side == -1) return p.parent; 
					if( side == 1)
					{
						while(p.parent != null)
						{
							p = p.parent ;
							side = p.side();
							if( side == -1) 
								return p.parent ; 
						}
					}
					return null;
				}
	}

	

	/**
	 * Removes the key (and its corresponding record) from this container.
	 * This method does nothing if the key is not in this container.
	 * @param key the key that needs to be removed.
	 * @return the record to which the key had been mapped in this container,
	 * or null if the key did not have a mapping.
	 * @exception RuntimeException if the key class is different to previously
	 * registered key class.
	 **/

	public void remove(TreeNode p)
	{
		if( isEmpty() || p == null)
		{
			return ;
		}

		int i = 0;
		TreeNode btnode1 = p;
		TreeNode btnode2 = null;
		
        if(btnode1.left  != null && btnode1.right != null)
        {
        	TreeNode btnode3 = previous(btnode1); 
        	btnode1.o = btnode3.o;
        	
        	btnode1 = btnode3;
        }
		
        btnode2 = btnode1.left == null ? btnode1.right : btnode1.left;
		
        TreeNode btnode = btnode1.parent;
		
        i = btnode1.side();
		
        join(btnode, btnode2, i);
		
        if(root == btnode1)
            root = btnode2;
		
        
        debalance(btnode, i);
        
		treeSize--;
	}
	
	
}
