package com.xuanzhi.tools.listcache.concrete;


import java.util.Comparator;


/**
 * A height-balanced 1-tree implementation of the AVLTree.
 */

public final class AvlTree
{

	protected BlockTreeNode root = null;
	protected int treeSize = 0;

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
	
	private void rebalance(BlockTreeNode btnode, int i)
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

	private BlockTreeNode balance(BlockTreeNode btnode)
    {
        int i = btnode.balance;
        BlockTreeNode btnode1 = btnode.child(i);
        BlockTreeNode btnode2 = btnode1.child(-i);
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

	private BlockTreeNode rotate(BlockTreeNode btnode,int i)
	{
		BlockTreeNode btnode1 = btnode.child(-i);
		BlockTreeNode btnode2 = btnode1.child(i);
        join(btnode, btnode2, -i);
        join(btnode.parent, btnode1, btnode.side());
        join(btnode1, btnode, i);
        if(btnode == root)
            root = btnode1;
        return btnode1;

	}

	
	private void join(BlockTreeNode btnode, BlockTreeNode btnode1, int i)
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
	
	private void debalance(BlockTreeNode btnode, int i)
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

	public BlockTreeNode insert(Comparator c,Object objs[],int start,int size)
	{
		// if tree is empty, insert as root node and return
		BlockTreeNode newNode = new BlockTreeNode( c,objs,start,size, null, null ,null);
		
		if( root == null )
		{
			root = newNode;
			treeSize++;
			return root;
		}

		BlockTreeNode p = root;
		BlockTreeNode node = null;
		int diff = 0;

		
		for( ; p != null && (diff = newNode.compareNode(p)) != 0; p = p.child(diff))
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

	
	/**
	 * 获取object所在的块对应的节点，分两种情况：
	 * 1. 块包含start位置
	 * 			----------
	 *         |          |
	 *         |          | <-- object
	 *         |          |
	 *          ----------	
	 *    直接返回对应的块
	 *    
	 * 2. 没有块包含start位置
	 * 			----------
	 *         |          |
	 *         |          | 
	 *         |          |
	 *          ----------	
	 *              |
	 *              |      <-- object
	 *              |
	 * 			----------
	 *         |          |
	 *         |          |
	 *         |          |
	 *          ----------	
	 *    这种情况，返回object位置下面的块对应的节点，如果object下面没有块，说明object比最后一个块还靠后。      
	 *          
	 **/
	public BlockTreeNode find(Object o)
	{
		if( isEmpty() )
		{
			return null; // tree empty
		}

		//		 Walk down the tree searching for key
		BlockTreeNode n;
		BlockTreeNode p = root;
		int diff = 0;
		while( p != null )
		{
			diff = p.compareTo(o);
			if(diff == 0){
				return p;
			}else if(diff < 0){
				n = previous(p);
				if(n == null || n.compareTo(o) > 0)
					return p;
			}
			p = (diff < 0)? p.left : p.right;
		}
		return null;
	}

	
	/**
	 * 获取start位置所在的块对应的节点，分两种情况：
	 * 1. 块包含start位置
	 * 			----------
	 *         |          |
	 *         |          | <-- start
	 *         |          |
	 *          ----------	
	 *    直接返回对应的块
	 *    
	 * 2. 没有块包含start位置
	 * 			----------
	 *         |          |
	 *         |          | 
	 *         |          |
	 *          ----------	
	 *              |
	 *              |      <-- start
	 *              |
	 * 			----------
	 *         |          |
	 *         |          |
	 *         |          |
	 *          ----------	
	 *    这种情况，返回start位置上面的块对应的节点，如果start上面没有块，说明start比第一个块还靠前。      
	 *          
	 **/
	public BlockTreeNode find(int start)
	{
		if( isEmpty() )
		{
			return null; // tree empty
		}
		
		//		 Walk down the tree searching for key
		BlockTreeNode n;
		BlockTreeNode p = root;
		int diff = 0;
		while( p != null )
		{
			if(p.start <= start && start < p.start + p.size){
				diff = 0;
			}else if(start < p.start){
				diff = -1;
			}else{
				n = next(p);
				if(n == null || start < n.start){
					return p;
				}
				diff = 1;
			}
			
			if( diff == 0 )
			{
				return p;
			}

			p = (diff < 0)? p.left : p.right;
		}

		return null;
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

	public BlockTreeNode minimum()
	{
		if( isEmpty() )
		{
			return null; // tree empty
		}

		// Walk down the tree searching for key
		BlockTreeNode p = root;
		
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

	public BlockTreeNode maximum()
	{
		if( isEmpty() )
		{
			return null; // tree empty
		}

		// Walk down the tree searching for key
		BlockTreeNode p = root;
		
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

	public BlockTreeNode previous(BlockTreeNode p)	throws RuntimeException
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

	public BlockTreeNode next(BlockTreeNode p)
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

	public void remove(BlockTreeNode p)
	{
		if( isEmpty() || p == null)
		{
			return ;
		}

		int i = 0;
		BlockTreeNode btnode1 = p;
		BlockTreeNode btnode2 = null;
		
        if(btnode1.left  != null && btnode1.right != null)
        {
        	BlockTreeNode btnode3 = previous(btnode1); 
        	
        	BlockTreeNode newNode = new BlockTreeNode(btnode3.comparator,btnode3.ids,btnode3.start,btnode3.size,null,null,null);
        	newNode.left = btnode1.left;
        	newNode.right = btnode1.right;
        	newNode.parent = btnode1.parent;
        	btnode1.left.parent = newNode;
        	btnode1.right.parent = newNode;
        	i = btnode1.side();
        	if(i == -1) btnode1.parent.left = newNode;
        	if(i == 1 ) btnode1.parent.right = newNode;
        	newNode.aln = btnode3.aln;
        	
        	btnode1.left = null;
        	btnode1.right = null;
        	btnode1.parent = null;
        	
			btnode1 = btnode3;
        }
		
        btnode2 = btnode1.left == null ? btnode1.right : btnode1.left;
		
        BlockTreeNode btnode = btnode1.parent;
		
        i = btnode1.side();
		
        join(btnode, btnode2, i);
		
        if(root == btnode1)
            root = btnode2;
		
        
        debalance(btnode, i);
        
		treeSize--;
	}
	
	
}
