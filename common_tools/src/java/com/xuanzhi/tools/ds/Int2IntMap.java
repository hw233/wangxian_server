package com.xuanzhi.tools.ds;

import java.util.Arrays;

/**
 * 实现一个Int2Int HashMap
 * 可以节省HashMap产生的大量的小对象
 * 
 * 其内部实现是采用了两个int数组来实现的。
 * 第一个数组为entryArray，存储(key,value)对，长度为2的幂
 * 第二个数组为conflictEntry，存储产生冲突的(key,value)对,长度为实际冲突的个数。
 * 
 * 这个实现采用了int数组，替代了HashMap中Map.Entry数组，
 * 在存储大量int对时，会节省不少内存。统计采用数组实现，也提高了查询效率。
 * 
 * 实际测试中：存储150万个（key,value)对，
 * 此实现需要25M内存，查询效率150万次，200ms左右
 * HashMap的实现，需要100M内存，查询效率150万次，350ms左右。
 * 
 *
 */
public class Int2IntMap implements java.io.Serializable{

	private static final long serialVersionUID = 1546346464356436L;

	private boolean debug = false;
	
	private int reservedKey;
	private int reservedValue;
	private int initialCapacity;

	//i*3+0 key
	//i*3+1 value
	//i*3+2 nextentry
	private int entryArray[];
	private int size;
	
	//i*3+0 key
	//i*3+1 value
	//i*3+2 nextentry
	private int conflictEntry[];
	private int conflictSize;
	
	public Int2IntMap(){
		this(Integer.MIN_VALUE,Integer.MIN_VALUE);
	}
	
	public Int2IntMap(int reservedKey,int reservedValue){
		this(reservedKey,reservedValue,16);
	}
	
	public Int2IntMap(int reservedKey,int reservedValue,int initialCapacity){
		this(reservedKey,reservedValue,initialCapacity,false);
	}
	
	public Int2IntMap(int reservedKey,int reservedValue,int initialCapacity,boolean debug){
		this.reservedKey = reservedKey;
		this.reservedValue = reservedValue;
		this.debug = debug;
		int k = 1;
		while(k<initialCapacity){
			k = k*2;
		}
		this.initialCapacity = k;
		
		clear();
	}
	
	/**
	 * 检查Map的状态是否正常
	 * @return
	 */
	public boolean check(){
		for(int i = 0 ; i < conflictSize ; i++){
			
			int k2 = hash(conflictEntry[3*i]);
			int next2 = -1;
			int curr2 = -1;
			curr2 = entryArray[3*k2+2];
			if(curr2 == -1){
				return false;
			}
			next2 = conflictEntry[3*curr2+2];
			while(curr2 != i){
				curr2 = next2;
				if(curr2 == -1){
					return false;
				}
				next2 = conflictEntry[3*curr2+2];
			}
		}
		return true;
	}
	
	public String dump(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("================entryArray================\n");
		for(int i = 0 ; i < entryArray.length/3 ; i++){
			if(entryArray[3*i] == reservedKey){
				sb.append("entry["+i+"]=-,-,-,"+entryArray[3*i+2]+"\n");
			}else{
				sb.append("entry["+i+"]="+hash(entryArray[3*i])+","+entryArray[3*i]+","+entryArray[3*i+1]+","+entryArray[3*i+2]+"\n");
			}
		}
		
		sb.append("================conflictEntry================\n");
		for(int i = 0 ; i < conflictSize ; i++){
			sb.append("entry["+i+"]="+hash(conflictEntry[3*i])+","+conflictEntry[3*i]+","+conflictEntry[3*i+1]+","+conflictEntry[3*i+2]+"\n");
		}
		
		return sb.toString();
	}
	
	public void clear(){
		conflictSize = 0;
		conflictEntry = new int[(initialCapacity/8<8?8:initialCapacity/8)*3];

		size = 0;
		entryArray = new int[initialCapacity*3];
		int n = entryArray.length/3;
		for(int i = 0 ; i < n ; i++){
			entryArray[3*i] = reservedKey;
			entryArray[3*i+1] = reservedValue;
			entryArray[3*i+2] = -1;
		}
		
		n = conflictEntry.length/3;
		for(int i = 0 ; i < n ; i++){
			conflictEntry[3*i] = reservedKey;
			conflictEntry[3*i+1] = reservedValue;
			conflictEntry[3*i+2] = -1;
		}
	}
	
	public int size(){
		return size + conflictSize;
	}
	
	private void rehash(){
		int []oldEntryArray = entryArray;
		int []oldConflictEntry = conflictEntry;
		int oldConflictSize = conflictSize;
		
		entryArray = new int[(entryArray.length+entryArray.length)];
		int n = entryArray.length/3;
		for(int i = 0 ; i < n; i++){
			entryArray[3*i] = reservedKey;
			entryArray[3*i+1] = reservedValue;
			entryArray[3*i+2] = -1;
		}
		size = 0;
		
		conflictSize = 0;
		conflictEntry = new int[(oldConflictSize<8?8:oldConflictSize)*3];
		n = conflictEntry.length/3;
		for(int i = 0 ; i < n ; i++){
			conflictEntry[3*i] = reservedKey;
			conflictEntry[3*i+1] = reservedValue;
			conflictEntry[3*i+2] = -1;
		}
		
		n = oldEntryArray.length/3;
		for(int i = 0 ; i < n ; i++){
			if(oldEntryArray[3*i] != reservedKey){
				put(oldEntryArray[3*i],oldEntryArray[3*i+1]);
			}
		}
		
		for(int i = 0 ; i < oldConflictSize ; i++){
			put(oldConflictEntry[3*i],oldConflictEntry[3*i+1]);
		}
	}
	
	public void put(int key,int value){
		
		if(size > entryArray.length/3*0.75f || (conflictSize > 16 && conflictSize > entryArray.length/6)){
			if(debug){
				long now = System.currentTimeMillis();
				int s1 = size;
				int s2 = conflictSize;
				int s3 = entryArray.length/3;
				int s4 = conflictEntry.length/3;
				
				long s5 = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
				rehash();
				
				int t1 = size;
				int t2 = conflictSize;
				int t3 = entryArray.length/3;
				int t4 = conflictEntry.length/3;
				long t5 = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
				
				
				System.out.println("[DEBUG] [com.xuanzhi.tools.ds.Int2IntMap] [rehash] [size:"+s1+"->"+t1+"] [conflictSize:"+s2+"->"+t2+"] [entryArray:"+s3+"->"+t3+"] [conflictEntry:"+s4+"->"+t4+"] [mem:"+(s5/1024)+"k->"+(t5/1024)+"k] ["+(System.currentTimeMillis()-now)+"ms]");
				
			}else{
				rehash();
			}
		}
		
		
		
		int k = hash(key);
		
		if(entryArray[3*k] == reservedKey){
			entryArray[3*k] = key;
			entryArray[3*k+1] = value;
			entryArray[3*k+2] = -1;
			size++;
			return;
		}else if(entryArray[3*k] == key ){
			entryArray[3*k+1] = value;
			return;
		}else{
			
			if(conflictSize == conflictEntry.length/3){
				int []oldConflictEntry = conflictEntry;
				conflictEntry = new int[oldConflictEntry.length+oldConflictEntry.length/2];
				System.arraycopy(oldConflictEntry, 0, conflictEntry, 0, oldConflictEntry.length);
			}
			
			int curr = -1;
			int next = entryArray[3*k+2];
			while(next != -1){
				if(conflictEntry[3*next]==key){
					conflictEntry[3*next+1] = value;
					return;
				}
				curr = next;
				next = conflictEntry[3*next+2];
			}
			if(curr == -1){
				conflictEntry[3*conflictSize] = key;
				conflictEntry[3*conflictSize+1] = value;
				conflictEntry[3*conflictSize+2] = -1;
				
				entryArray[3*k+2] = conflictSize;
				conflictSize++;
			}else{
				conflictEntry[3*conflictSize] = key;
				conflictEntry[3*conflictSize+1] = value;
				conflictEntry[3*conflictSize+2] = -1;
				
				conflictEntry[3*curr+2] = conflictSize;
				conflictSize++;
			}
		}
		
	}
	
	public boolean containsKey(int key){

		int k = hash(key);
		
		if(entryArray[3*k] == reservedKey){
			return false;
		}else if(entryArray[3*k] == key ){
			return true;
		}else{
			int next = entryArray[3*k+2];
			while(next != -1){
				if(conflictEntry[3*next]==key){
					return true;
				}
				next = conflictEntry[3*next+2];
			}
			return false;
		}
	}
	
	public int get(int key){
		int k = hash(key);
		
		if(entryArray[3*k] == reservedKey){
			return reservedValue;
		}else if(entryArray[3*k] == key ){
			return entryArray[3*k+1];
		}else{
			int next = entryArray[3*k+2];
			while(next != -1){
				if(conflictEntry[3*next]==key){
					return conflictEntry[3*next+1];
				}
				next = conflictEntry[3*next+2];
			}
			return reservedValue;
		}
	}
	
	public void remove(int key){
		int k = hash(key);
		
		if(entryArray[3*k] == reservedKey){
			return;
		}else if(entryArray[3*k] == key ){
			entryArray[3*k] = reservedKey;
			entryArray[3*k+1] = reservedValue;
			size--;
			
			int curr_prev = -1;
			int curr = -1;
			int next = entryArray[3*k+2];
			while(next != -1){
				curr_prev = curr;
				curr = next;
				next = conflictEntry[3*next+2];
			}
			
			if(curr != -1){
				entryArray[3*k] = conflictEntry[3*curr];
				entryArray[3*k+1] = conflictEntry[3*curr+1];
				size++;
				
				if(curr_prev != -1){
					conflictEntry[3*curr_prev+2] = -1;
				}else{
					entryArray[3*k+2] = -1;
				}
				
				if(curr < conflictSize -1){
					conflictEntry[3*curr] = conflictEntry[3*(conflictSize-1)];
					conflictEntry[3*curr+1] = conflictEntry[3*(conflictSize-1)+1];
					conflictEntry[3*curr+2] = conflictEntry[3*(conflictSize-1)+2];
					
					int k2 = hash(conflictEntry[3*(conflictSize-1)]);
					int prev2 = -1;
					int next2 = -1;
					int curr2 = -1;
					curr2 = entryArray[3*k2+2];
					if(curr2 == -1){
						throw new java.lang.IllegalStateException("Int2IntMap Internal Error, maybe the souce code is not correct!"
								+" the tail of conflictEntry is not a element of some EntryLink."+
								"key="+key+",hash(key)="+k+",entryArray[3*k]="+entryArray[3*k]+",tail="+conflictEntry[3*(conflictSize-1)]+",hash(tail)="+k2);
					}
					next2 = conflictEntry[3*curr2+2];
					while(curr2 != conflictSize-1){
						prev2 = curr2;
						curr2 = next2;
						if(curr2 == -1){
							throw new java.lang.IllegalStateException("Int2IntMap Internal Error, maybe the souce code is not correct!"
									+" the tail of conflictEntry is not a element of some EntryLink."+
									"key="+key+",hash(key)="+k+",tail="+conflictEntry[3*(conflictSize-1)]+",hash(tail)="+k2);
						}
						next2 = conflictEntry[3*curr2+2];
					}
					if(prev2 == -1){
						entryArray[3*k2+2] = curr;
					}else{
						conflictEntry[3*prev2+2] = curr;
					}
					
					conflictSize--;
				}else{
					conflictSize--;
				}
			}
		}else{
		
			int curr_prev = -1;
			int curr = entryArray[3*k+2];
			int next = -1;
			if(curr != -1) next = conflictEntry[3*curr+2];

			while(curr != -1){
				if(conflictEntry[3*curr] == key){
					break;
				}
				
				curr_prev = curr;
				curr = next;
				next = -1;
				if(curr != -1) next = conflictEntry[3*curr+2];
			}
			
			if(curr != -1){
				if(curr_prev != -1){
					conflictEntry[3*curr_prev+2] = next;
				}else{
					entryArray[3*k+2] = next;
				}
				
				if(curr < conflictSize -1){
					conflictEntry[3*curr] = conflictEntry[3*(conflictSize-1)];
					conflictEntry[3*curr+1] = conflictEntry[3*(conflictSize-1)+1];
					conflictEntry[3*curr+2] = conflictEntry[3*(conflictSize-1)+2];
					
					int k2 = hash(conflictEntry[3*(conflictSize-1)]);
					int prev2 = -1;
					int next2 = -1;
					int curr2 = -1;
					curr2 = entryArray[3*k2+2];
					if(curr2 == -1){
						throw new java.lang.IllegalStateException("Int2IntMap Internal Error, maybe the souce code is not correct!"
								+" the tail of conflictEntry is not a element of some EntryLink."+
								"key="+key+",hash(key)="+k+",tail="+conflictEntry[3*(conflictSize-1)]+",hash(tail)="+k2);
					}
					next2 = conflictEntry[3*curr2+2];
					while(curr2 != conflictSize-1){
						prev2 = curr2;
						curr2 = next2;
						if(curr2 == -1){
							throw new java.lang.IllegalStateException("Int2IntMap Internal Error, maybe the souce code is not correct!"
									+" the tail of conflictEntry is not a element of some EntryLink."+
									"key="+key+",hash(key)="+k+",tail="+conflictEntry[3*(conflictSize-1)]+",hash(tail)="+k2);
						}
						next2 = conflictEntry[3*curr2+2];
					}
					if(prev2 == -1){
						entryArray[3*k2+2] = curr;
					}else{
						conflictEntry[3*prev2+2] = curr;
					}
					
					conflictSize--;
				}else{
					conflictSize--;
				}
			}
			
			
		
		}
		
	}
	
	private int hash(int h){
		h ^= (h >>> 20) ^ (h >>> 12);
        h= h ^ (h >>> 7) ^ (h >>> 4);
        
		return h & (entryArray.length/3 -1);
	}
}
