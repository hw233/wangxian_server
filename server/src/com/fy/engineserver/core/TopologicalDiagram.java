package com.fy.engineserver.core;

import java.util.Comparator;
import java.util.Vector;

import com.xuanzhi.tools.ds.Heap;



/**
 * 游戏世界的拓扑结构图，提供一个方法，从一个地图如何到达另外一个地图
 * 
 *
 */
public class TopologicalDiagram {

	Topo topos[];
	
	/**
	 * 
	 * @param mapNames 地图的名字
	 * @param neighborNums 各个地图有几个邻居
	 * @param neighbors 各个地图邻居的编号，用一维表达二维的信息
	 */
	public TopologicalDiagram(String mapNames[],byte[]neighborNums, short[]neighbors){
		topos = new Topo[mapNames.length];
		for(int i = 0 ; i < topos.length ; i++){
			topos[i] = new Topo();
			topos[i].mapName = mapNames[i];
			topos[i].index = i;
		}
		int offset = 0;
		for(int i = 0 ; i < topos.length ; i++){
			topos[i].neighbors = new Topo[neighborNums[i]];
			for(int j = 0 ; j < neighborNums[i] ; j++){
				topos[i].neighbors[j] = topos[neighbors[offset]];
				offset++;
			}
		}
	}
	
	public static class Topo{

		public int F, G;
		public boolean inOPEN;
		public boolean inCLOSE;
		public Topo parent;
		
		public int index;
		public String mapName;
		public Topo neighbors[];
	}
	
	
	public static Heap OPEN = new Heap(new Comparator<Topo>() {
		public int compare(Topo o1, Topo o2) {
			Topo c1 = (Topo) o1;
			Topo c2 = (Topo) o2;
			if (c1 == c2)
				return 0;
			if (c1.F < c2.F)
				return -1;
			if (c1.F > c2.F)
				return 1;
			return 0;
		}
	});
	
	public static Vector<Topo> CLOSE = new Vector<Topo>();
	
	public Topo getTopo(String map){
		for(int i = 0 ; i < topos.length ; i++){
			if(topos[i].mapName.equals(map)){
				return topos[i];
			}
		}
		return null;
	}
	/**
	 * 从开始的地图，到结束的地图，寻找一条路径，
	 * 返回的路径下从开始地图的下一个地图开始，到结束地图。
	 * 
	 * 返回的路径长度为0，标识开始地图和结束地图是一张地图，或者没有路径可以到达目地的
	 * 返回的路径长度为1，标识开始地图可以直接到达结束地图。
	 * 其他值，标识需要先经过一些地图，才能到达目的地
	 * 
	 * @param startMap
	 * @param endMap
	 * @return
	 */
	public String[] findPath(String startMap,String endMap){
		Topo s = getTopo(startMap);
		Topo e = getTopo(endMap);
		if(s == null || e == null) return new String[0];
		if(s == e) return new String[0];
		
		for(int i = 0 ; i < topos.length ; i++){
			topos[i].F = 0;
			topos[i].G = 0;
			topos[i].parent = null;
			topos[i].inCLOSE = false;
			topos[i].inOPEN = false;
		}
		
		OPEN.insert(s);
		s.inOPEN = true;
		
		while(OPEN.size() > 0){
			Topo t = (Topo)OPEN.extract();
			t.inOPEN = false;
			if(t == e){
				break;
			}
			CLOSE.addElement(t);
			t.inCLOSE = true;
			
			for(int i = 0 ; i < t.neighbors.length ; i++){
				Topo neighbor = t.neighbors[i];
				int cost = t.G + 1;
				
				if (neighbor.inOPEN && cost < neighbor.G) {
					OPEN.remove(neighbor);
					neighbor.inOPEN = false;
				}

				if (neighbor.inCLOSE && cost < neighbor.G) {
					CLOSE.removeElement(neighbor);
					neighbor.inCLOSE = false;
				}

				if (neighbor.inOPEN == false && neighbor.inCLOSE == false) {
					neighbor.G = cost;
					neighbor.F = neighbor.G + movecost(neighbor, e);
					neighbor.parent = t;
					OPEN.insert(neighbor);
					neighbor.inOPEN = true;
				}
			}
		}
		
		if(e.parent == null){
			return new String[0];
		}
		
		Vector<Topo> al = new Vector<Topo>();
		Topo p = e;
		while (p != s && p != null) {
			al.addElement(p);
			p = p.parent;
		}

		String ret[] = new String[al.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = ((Topo) al.elementAt(ret.length - 1 - i)).mapName;
		}
		OPEN.clear();
		CLOSE.removeAllElements();
		return ret;

	}
	
	private int movecost(Topo s,Topo e){
		if(s == e) return 0;
		return 1;
	}
}
