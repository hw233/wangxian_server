package com.fy.engineserver.core.g2d;

import java.util.ArrayList;

/**
 * 导航图
 * 
 */
public class Navigator {
	private SignPost[] signPosts;
	private Road[] roads;
	private Polygon[] polygons;

	public Navigator(SignPost[] signList, Road[] roadList, Polygon[] polygonList) {
		this.signPosts = signList;
		this.roads = roadList;
		this.polygons = polygonList;
	}

	public Polygon[] getPolygons() {
		return polygons;
	}
	
	public int getPolygonNum() {
		return polygons.length;
	}

	public int getSignPostNum() {
		return signPosts.length;
	}

	public int getRoadNum() {
		return roads.length;
	}

	public SignPost getSignPost(int index) {
		return signPosts[index];
	}

	/**
	 * 找出从点(x,y)能看见的所有的导航点，其中polygons为所有的遮挡物
	 * 
	 * @param polygons
	 * @param x
	 * @param y
	 * @return
	 */
	public SignPost[] findVisiableSignPosts(int x, int y) {
		SignPost[] sps = signPosts;
		ArrayList<SignPost> al = new ArrayList<SignPost>();
		for (int i = 0; i < sps.length; i++) {
			SignPost p = sps[i];
			
			if(isVisiable(x, y, p.x, p.y)){
				al.add(p);
			}
		}
		return al.toArray(new SignPost[0]);
	}

	public Road getRoadBySignPost(int index1, int index2) {
		Road[] roads = signPosts[index1].getRoads();
		for (int i = 0; i < roads.length; i++) {
			Road road = roads[i];
			if (road.p1.equals(signPosts[index2]) || road.p2.equals(signPosts[index2])) {
				return road;
			}
		}
		return null;
	}

	/**
	 * 判断两点之间是否可见
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean isVisiable(int p1x,int p1y,int p2x,int p2y) {
		for (int j = 0; j < polygons.length; j++) {
			for (int k = 0; k <  polygons[j].edges.length; k++) {
				Edge e = polygons[j].edges[k];
				
				int p[] = Graphics2DUtil.intersectLineForPolygon(p1x, p1y, p2x, p2y, e.s.x, e.s.y,e.e.x, e.e.y, 4);
				if(p != null){
					if(Math.abs(p1x-p[0]) + Math.abs(p1y-p[1]) > 4 && Math.abs(p2x-p[0]) + Math.abs(p2y-p[1]) > 4){
						
						//System.out.println("交点=（"+p[0]+","+p[1]+"），isVisiable("+p1x+","+p1y+","+p2x+","+p2y+") = fales, Edge=("+e.s.x+","+e.s.y+")->("+e.e.x+","+e.e.y+")");
						
						return false;
					}else{
//						if(Graphics2DUtil.leftRightMeasure(e.s, e.e, p2x,p2y) > 0){
//							
//							System.out.println("交点=（"+p[0]+","+p[1]+"）左右判断，isVisiable("+p1x+","+p1y+","+p2x+","+p2y+") = fales, Edge=("+e.s.x+","+e.s.y+")->("+e.e.x+","+e.e.y+")");
//							
//							return false;
//						}
					}
				}
				//if( Graphics2DUtil.leftRightMeasure(polygonsX[j][k], polygonsY[j][k],polygonsX[j][k_1], polygonsY[j][k_1], p1x,p1y)
				//	* Graphics2DUtil.leftRightMeasure(polygonsX[j][k], polygonsY[j][k],polygonsX[j][k_1], polygonsY[j][k_1], p2x,p2y) < 0
				//	&& Graphics2DUtil.leftRightMeasure(p1x,p1y,p2x,p2y,polygonsX[j][k], polygonsY[j][k])
				//	* Graphics2DUtil.leftRightMeasure(p1x,p1y,p2x,p2y,polygonsX[j][k_1], polygonsY[j][k_1]) < 0)
			}
		}
		return true;
	}
	
	/**
	 * 判断两点之间是否可见
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean isStrictVisiable(int p1x,int p1y,int p2x,int p2y) {
		for (int j = 0; j < polygons.length; j++) {
			for (int k = 0; k <  polygons[j].edges.length; k++) {
				Edge e = polygons[j].edges[k];
				
				int p[] = Graphics2DUtil.intersectLineForPolygon(p1x, p1y, p2x, p2y, e.s.x, e.s.y,e.e.x, e.e.y, 4);
				if(p != null){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 寻找路径，如果start 能直接到达 end 返回长度为0的数组，如果start不能到达end，返回null
	 * 否则返回从start开始到达end，经过的路标。
	 * 
	 * @param polygons
	 * @param start
	 * @param end
	 * @return
	 */
	public synchronized SignPost[] findPath(Point start, Point end) {
		SignPost sp1 = new SignPost();
		sp1.x = start.x;
		sp1.y = start.y;
		SignPost sp2 = new SignPost();
		sp2.x = end.x;
		sp2.y = end.y;
	

		SignPost startSPS[] = findVisiableSignPosts(start.x, start.y);
		SignPost endSPS[] = findVisiableSignPosts(end.x, end.y);
		if (startSPS.length == 0) {
			return null;
		}

		if (endSPS.length == 0) {
//			SignPost tmp = null;
//			int ming = Integer.MAX_VALUE;
//			for (int i = 0; i < signPosts.length; i++) {
//				SignPost p = signPosts[i];
//				int g = Math.abs(end.x - p.x) + Math.abs(end.y - p.y);
//				if (g < ming) {
//					ming = g;
//					tmp = p;
//				}
//			}
//			end.x = tmp.x;
//			end.y = tmp.y;
//			endSPS = findVisiableSignPosts(end.x, end.y);
			return null;
		}

		for (int i = 0; i < signPosts.length; i++) {
			SignPost p = signPosts[i];
			p.parent = null;
			p.inCLOSE = false;
			p.inOPEN = false;
			p.F = 0;
			p.G = 0;
		}
		return NavigatorAstar.astar(sp1, sp2, startSPS, endSPS);
	}
}
