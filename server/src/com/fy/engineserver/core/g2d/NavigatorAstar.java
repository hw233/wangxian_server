package com.fy.engineserver.core.g2d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import com.xuanzhi.tools.ds.Heap;

public class NavigatorAstar {
	
	
	/**
	 * A×算法，选择最短路径
	 * @param start
	 * @param end
	 * @param startSPS
	 * @param endSPS
	 * @return
	 */
	public static SignPost[] astar(SignPost start,SignPost end,SignPost startSPS[],SignPost endSPS[]){
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		Heap OPEN = new Heap(new Comparator<SignPost>(){

			public int compare(SignPost c1, SignPost c2) {
				if(c1 == c2) return 0;
				if(c1.F < c2.F) return -1;
				if(c1.F > c2.F) return 1;
			
				return 0;
			}
			
		});
		HashSet<SignPost> CLOSE = new HashSet<SignPost>();
		
		OPEN.insert(start);
		start.inOPEN = true;
		int step = 0;
		while(OPEN.size() > 0){
			step ++;
			SignPost p = (SignPost)OPEN.extract();
			p.inOPEN = false;
			for(int i = 0 ; i < endSPS.length ; i++){
				if(p.equals(endSPS[i])){
					end.parent = endSPS[i];
					break;
				}
			}
			if(end.parent != null) break;
			
			CLOSE.add(p);
			p.inCLOSE = true;
			
			if(p == start){
				for(int i = 0 ; i < startSPS.length ; i++){
					startSPS[i].G = p.G + movecost(p,startSPS[i]);
					startSPS[i].F = startSPS[i].G + movecost(startSPS[i],end);
					startSPS[i].parent = p;
					OPEN.insert(startSPS[i]);
					startSPS[i].inOPEN = true;
				}
			}else{
				Road rs[] = p.getRoads();
				for(int i = 0 ; i < rs.length ; i++){
					SignPost neighbor = null;
					if(rs[i].p1 == p) neighbor = rs[i].p2;
					if(rs[i].p2 == p) neighbor = rs[i].p1;
					
					int cost = p.G + rs[i].len;
					
					if(neighbor.inOPEN  && cost < neighbor.G){
						OPEN.remove(neighbor);
						neighbor.inOPEN = false;
					}
					
					if( neighbor.inCLOSE && cost < neighbor.G){
						CLOSE.remove(neighbor);
						neighbor.inCLOSE = false;
					}
					
					if(neighbor.inOPEN == false && neighbor.inCLOSE == false){
						neighbor.G = cost;
						neighbor.F = neighbor.G + movecost(neighbor,end);
						neighbor.parent = p;
						OPEN.insert(neighbor);
						neighbor.inOPEN = true;
					}
				}
			}
		}
		
		if(end.parent == null){
			//System.out.println("基于导航的寻路算法失败：耗时"+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-startTime)+"ms，A*算法检查步数:" + step);
			return new SignPost[0];
		}
		
		
		
		ArrayList<SignPost> al = new ArrayList<SignPost>();
		SignPost p = end.parent;
		while(p != start && p != null){
			al.add(p);
			p = p.parent;
		}
		
		SignPost ret[] = new SignPost[al.size()];
		for(int i = 0 ; i < ret.length ; i++){
			ret[i] = al.get(ret.length-1-i);
		}
		
		//System.out.println("基于导航的寻路算法成功，路径长度为：" + ret.length+"耗时"+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-startTime)+"ms，A*算法检查步数:" + step);
		
		return ret;
	}
	
	public static int movecost(SignPost p,SignPost q){
		return Math.abs(q.y - p.y) + Math.abs(q.x - p.x); 
	}
}
