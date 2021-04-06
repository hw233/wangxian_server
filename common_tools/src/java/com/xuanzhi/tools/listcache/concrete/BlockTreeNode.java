package com.xuanzhi.tools.listcache.concrete;

import java.util.Comparator;

public class BlockTreeNode {

	/**
	 * 块的第一个ID在整个排序列表中的位置
	 */
	protected int start;
	
	/**
	 * 块包含的ID的个数
	 */
	protected int size;

	protected Object ids[];
	
    protected AccessListNode aln;
    protected Comparator comparator;

	protected BlockTreeNode left;
	protected BlockTreeNode right;
	protected BlockTreeNode parent;
	protected int balance;

	public BlockTreeNode(Comparator c,Object [] ids,int start,int size,BlockTreeNode left,  BlockTreeNode right ,BlockTreeNode parent)
	{
		this.comparator = c;
		this.ids = ids;
		this.start = start;
		this.size = size;
		this.left = left;
		this.right = right;
		this.parent = parent;
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
	
	public BlockTreeNode child(int i)
	{
		if(i == -1) return left;
		else if(i == 1) return right;
		else return this;
	}
	
    public int getStartNumber(){
    	return start;
    }
    
    public int getSizeNumber(){
    	return size;
    }
    
    public Object[] getIds(){
    	return ids;
    }
    
    public long getLeftLifeTime(long maxBlockLifeTime){
    	if(aln == null) return maxBlockLifeTime;
    	return  (maxBlockLifeTime + aln.timestamp - System.currentTimeMillis()); 
    }
    /**
     * 判断这个块是否应该包含给定的值对应的ID，
     * 所谓的包含必须是夹在第一个ID和最后一个ID之间。
     * 如果ID应该包含在块中，则返回0，如果ID应该在块前，返回-1,如果ID应该在块后，返回1。
     * 如图所示：
     * 
     *                           <- ID  返回-1
     *         |-----------|     <- ID  返回0    //等于第一个ID也返回-1
     *         |           | 
     *         |           |
     *         |           |     <- ID  返回0
     *         |           |
     *         |           |
     *         |-----------|     <- ID  返回0
     *                           <- ID  返回1
     *         
     * @param value
     * @return
     */
    protected int compareTo(Object id){
	
    	int a1 = compare(ids[0],id);
    	int a2 = compare(ids[size-1],id);
    	
    	if(a1 > 0) return -1;
    	if(a2 < 0) return 1;
    	return 0;
    }
    
    protected int compareNode(BlockTreeNode p){
    	if(start < p.start) return -1;
    	
    	if(start > p.start) return 1;
    	
    	return 0;
    }
    
    /**
     * 插入给定的ID。
     * 如果块中已经包含这个ID或者ID不属于块的范围，插入失败，返回false
     * 否则，插入成功，返回true
     * @param value
     * @return
     */
    protected boolean insert(Object id){
    	int pos[] = binarySearch(id,0,size);
    	if(pos[0] != pos[1]){
    		Object newIds[] = new Object[size+1];
	    	for(int i = 0 ; i < pos[1] ; i++){
	    		newIds[i] = ids[i];
	    	}
	    	newIds[pos[1]] = id;
	    	for(int i = pos[1]; i < size; i++){
	    		newIds[i+1] = ids[i];
	    	}
	    	size += 1;
	    	ids = newIds;
	    	
	    	return true;
    	}
    	return false;
    }
    
    protected void insertFirst(Object id){
    	Object newIds[] = new Object[size+1];
	    newIds[0] = id;
	    for(int i = 0; i < size; i++){
	    	newIds[i+1] = ids[i];
	    }
	    size += 1;
	    ids = newIds;
    }
    
    protected void insertEnd(Object id){
    	Object newIds[] = new Object[size+1];
	    for(int i = 0; i < size; i++){
	    	newIds[i] = ids[i];
	    }
	    newIds[size] = id;
	    size += 1;
	    ids = newIds;
    }
    
    /**
     * 从块中删除指定的ID，如果块包含ID，则删除成功，返回true。反之，返回false
     * @param id
     * @param value
     * @return
     */
    protected boolean delete(Object id){
    	int pos[] = binarySearch(id,0,size);
    	if(pos[0] == pos[1] && pos[0] >= 0){
    		for(int i = pos[0] ; i < size - 1; i++){
    			ids[i] = ids[i+1];
    		}
    		size = size - 1;
    		return true;
    	}
    	return false;
    }
    
    protected boolean isContains(Object id ){
    	int pos[] = binarySearch(id,0,size);
    	if(pos[0] == pos[1] && pos[0] >= 0){
    		return true;
    	}else
    		return false;
    }
    
    /**
     * <pre>
     * 二分查找，返回给定的值所在的位置。
     * 如果块中包含给定的ID，则返回这个ID对应的位置，即：
     *      int a = binarySearch(id，..);
     * 		a[0]=a[1]=某个位置
     * 		ids[a[0]]与id相同
     * 如果给定ID在块的首，尾之间，则返回这个ID应该所在位置的前继和后续，即
     *      int a = binarySearch(id，value,..);
     * 		a[1]=某个位置
     * 		a[0]=a[1]-1
     * 		ids[a[0]].value <= value <= ids[a[1]].value 或者 ids[a[0]].value >= value >= ids[a[1]].value
     * 对于值相同，ID不同的情况，又分两种情况：
     *      排序是从小到大的情况：返回位置应该在相同值列表的最前面的位置，即
     *      	对于值为{2 2 2 3 3 4 5 5 5}块，二分查找不同ID，值为2的情况，返回{-1,0}数组
     *      	对于值为{2 2 2 3 3 4 5 5 5}块，二分查找不同ID，值为5的情况，返回{5,6}数组
     *      排序是从大到小的情况：返回位置应该在相同值列表的最后面的位置，即
     *      对于值为{5 5 5 4 3 3 2 2 2}块，二分查找不同ID，值为2的情况，返回{8,9}数组
     *      对于值为{5 5 5 4 3 3 2 2 2}块，二分查找不同ID，值为5的情况，返回{2,3}数组
     * </pre>     
     * @param id
     * @param value
     * @param s 开始的位置
     * @param n 个数
     * @return
     */
    protected int[] binarySearch(Object id,int s,int n){
    	if(n == 1){
     		int a = compare(ids[s],id);
    		if(a != 0){
    			int [] ret = new int[2];
    			ret[0] = -1; ret[1] = -1;
    			return ret;
    		}else{
    			if(ids[s].equals(id) || id.equals(ids[s])){
    				int [] ret = new int[2];
        			ret[0] = s; ret[1] = s;
        			return ret;
    			}else{
    				int [] ret = new int[2];
 					ret[1] = s;
    				ret[0] = ret[1]-1;
    				return ret;
    			}
    		}
    	}else if(n == 2){
    		int a = compare(ids[s],id);
    		int b = compare(ids[s+1],id);
    		if(a * b < 0){ 
    			int [] ret = new int[2];
    			ret[0] = s; ret[1] = s+1;
    			return ret;
    		}else if( a * b > 0){
    			int [] ret = new int[2];
    			ret[0] = -1; ret[1] = -1;
    			return ret;
    		}else if(a == 0){
    			if(ids[s].equals(id)|| id.equals(ids[s])){
    				int [] ret = new int[2];
        			ret[0] = s; ret[1] = s;
        			return ret;
    			}else{
    				int [] ret = new int[2];
    				ret[1] = s;
    				ret[0] = ret[1]-1;
    				return ret;
    			}
    		}else{
    			if(ids[s+1].equals(id) || id.equals(ids[s+1])){
    				int [] ret = new int[2];
        			ret[0] = s+1; ret[1] = s+1;
        			return ret;
    			}else{
    				int [] ret = new int[2];
    				ret[1] = s+1;
    				ret[0] = ret[1]-1;
    				return ret;
    			}
    		}
    	}else{
  		
    		int a = compare(ids[s + (n+1)/2-1],id);
    		
    		//System.out.println("s="+s+",n="+n+",ids["+(s + (n+1)/2-1)+"]=" + ids[s + (n+1)/2-1] + ",value=" + v1);
    		
    		if(a < 0 ){
    			return binarySearch(id,s+(n+1)/2-1,n-(n+1)/2+1);
    		}else if(a > 0){
    			return binarySearch(id,s,(n+1)/2);
    		}else{  
    			//a==0 找出上下所有相等的ID，判断是否与id有一样的
    			//如果有，表明id已经在列表中，返回-2
    			//如果没有找到相等集合的边缘，继续二分查找
    			int x = s + (n+1)/2 -2 ;
    			for(; x >= s; x--){
    				if(compare(ids[x],id) != 0) break;
    			}
    			x++;
    			
    			int y = s + (n+1)/2;
    			for(; y <= s+n-1; y++){
    				if(compare(ids[y],id) != 0) break;
    			}
    			y--;
    			
    			for(int i = x ; i <= y ; i++){
    				if(ids[i].equals(id) || id.equals(ids[i])){
    					int [] ret = new int[2];
    	    			ret[0] = i; ret[1] = i;
    	    			return ret;
    				}
    			}
    			
   				int [] ret = new int[2];
       			ret[0] = x-1; ret[1] = x;
       			return ret;
    		}
    	}
    }
    
    
    protected int compare(Object v1,Object v2){
    	return this.comparator.compare(v1,v2);
	}

}
