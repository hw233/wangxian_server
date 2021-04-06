package com.fy.engineserver.core.g2d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * 简单的多边形，点为顺时针方向
 * 
 */
public class Polygon {

	public Point ps[] = new Point[0];

	public Edge edges[] = new Edge[0];

	public float minX,minY,maxX,maxY;
	
	public Polygon(Point points[]) {
		if (points.length < 3)
			throw new java.lang.IllegalArgumentException("多边形的点至少为3个");
		// 判断极点（本方法判断最右边点）
		int maxRightPointIndex = 0, beforeMaxRightPointIndex = 0, afterMaxRightPointIndex = 0;
		float tempX = points[0].x;
		for (int i = 0; i < points.length; i++) {
			if (tempX < points[i].x) {
				maxRightPointIndex = i;
				tempX = points[i].x;
			}
		}
		if (maxRightPointIndex == 0) {
			beforeMaxRightPointIndex = points.length - 1;
			afterMaxRightPointIndex = maxRightPointIndex + 1;
		} else if (maxRightPointIndex == points.length - 1) {
			beforeMaxRightPointIndex = maxRightPointIndex - 1;
			afterMaxRightPointIndex = 0;
		} else {
			beforeMaxRightPointIndex = maxRightPointIndex - 1;
			afterMaxRightPointIndex = maxRightPointIndex + 1;
		}
		// int f = Graphics2DUtil.leftRightMeasure(points[0], points[1],
		// points[2].x, points[2].y);
		int f = Graphics2DUtil.leftRightMeasure(
				points[beforeMaxRightPointIndex], points[maxRightPointIndex],
				points[afterMaxRightPointIndex].x,
				points[afterMaxRightPointIndex].y);
		if (f == -1) {
			Point points2[] = new Point[points.length];
			for (int i = 0; i < points.length; i++) {
				points2[i] = points[points.length - 1 - i];
			}
			points = points2;
		}

		ps = new Point[points.length];
		edges = new Edge[points.length];
		for (int i = 0; i < ps.length; i++) {
			ps[i] = new Point();
			ps[i].x = points[i].x;
			ps[i].y = points[i].y;

			if (i > 0) {
				edges[i - 1] = new Edge();
				edges[i - 1].s = ps[i - 1];
				edges[i - 1].e = ps[i];
				edges[i - 1].polygon = this;
			}
			if (i == ps.length - 1) {
				edges[i] = new Edge();
				edges[i].s = ps[i];
				edges[i].e = ps[0];
				edges[i].polygon = this;
			}
		}
		
		minX=minY=Integer.MAX_VALUE;
		maxX=maxY=-Integer.MAX_VALUE;
		for(int i = 0 ; i < ps.length ; i++){
			if(minX > ps[i].x) minX = ps[i].x;
			if(maxX < ps[i].x) maxX = ps[i].x;
			if(minY > ps[i].y) minY = ps[i].y;
			if(maxY < ps[i].y) maxY = ps[i].y;
		}
	}

	/**
	 * 判断两条线段是否相交，重叠不算相交
	 * @param p1
	 * @param p2
	 * @param q1
	 * @param q2
	 * @param precision 精度，标识交点与端点的距离，如果小于等于此距离，表明相交，这里的距离是用x方向差别和y方向差别只和表示的。
	 * @return
	 */
	public static Point intersectLineForPolygon(Point p1,Point p2,Point q1,Point q2,int precision){

		int a1 = p2.y - p1.y;
		int b1 = p2.x - p1.x;
		int a2 = q2.y - q1.y;
		int b2 = q2.x - q1.x;
		int k = a1 * b2 - a2 * b1;
		int l = a1 * a2 + b1 * b2;
		if(k == 0 || Math.abs(k) * 50  <  Math.abs(l)){ // 夹角小于2度
			//平行
		}else{
			
			long x = (1L * a1 * b2 * p1.x - 1L * a2 * b1 * q1.x - 1L * b1 * b2 *p1.y + 1L * b1 * b2 * q1.y)/k;
			long y = (1L *b1 * a2 * p1.y - 1L *b2 * a1 * q1.y + 1L *a1 * a2 * q1.x - 1L * a1 * a2 * p1.x)/(-1 * k);
			long t1 = 1L * (x - p1.x) * (x - p2.x);
			long t2 = 1L * (x - q1.x) * (x - q2.x);
			if( t1 > 0 || t2 > 0){
				if(Math.abs(x-q1.x) + Math.abs(y-q1.y) <= precision
						||  Math.abs(x-q2.x) + Math.abs(y-q2.y) <= precision){
					if(t1 < 0){ 
						return new Point((int)x,(int)y);
					}else if(t1 == 0 && 1L * (y - p1.y) * (y - p2.y) <= 0) {
						return new Point((int)x,(int)y);
					}
					
				}
				if(Math.abs(x-p1.x) + Math.abs(y-p1.y) <= precision 
						||  Math.abs(x-p2.x) + Math.abs(y-p2.y)<= precision ){
					if(t2 < 0){
						return new Point((int)x,(int)y);
					}
					else if(t2 == 0 && 1L * (y - q1.y) * (y - q2.y) <= 0){
						return new Point((int)x,(int)y);
					}
				}
			}else{
				if(t1 == 0 && t2 < 0){
					if( 1L * (y - p1.y) * (y - p2.y) <= 0 
							|| Math.abs(y-p1.y) <= precision ||  Math.abs(y-p2.y) <= precision){
						return new Point((int)x,(int)y);
					}
				}else if(t1 < 0 && t2 == 0){
					if( 1L * (y - q1.y) * (y - q2.y) <= 0
							|| Math.abs(y-q1.y) <= precision ||  Math.abs(y-q2.y) <= precision){
						return new Point((int)x,(int)y);
					}
				}else if(t1 == 0 && t2 == 0){
					if( (1L * (y - p1.y) * (y - p2.y) <= 0 
							|| Math.abs(y-p1.y) <= precision ||  Math.abs(y-p2.y)  <= precision)
							&& ( 1L * (y - q1.y) * (y - q2.y) <= 0
									|| Math.abs(y-q1.y)  <= precision ||  Math.abs(y-q2.y)  <= precision)){
						return new Point((int)x,(int)y);
					}
				}else{
					return new Point((int)x,(int)y);
				}
			}
		}
		
		return null;
	}
	
	
	/**
	 * 功能：判断点是否在多边形内 方法：求解通过该点的水平线与多边形各边的交点 结论：单边交点为奇数，成立!
	 * 
	 * @param point
	 *            指定的某个点
	 * @param APoints
	 *            多边形的各个顶点坐标（首末点可以不一致）
	 * @return
	 */
	public boolean PtInPolygon(Point point, Point APoints[]) {
		int nCross = 0;
		for (int i = 0; i < APoints.length; i++) {
			Point p1 = APoints[i];
			Point p2 = APoints[(i + 1) % APoints.length];
			// 求解 y=p.y 与 p1p2 的交点
			if (p1.getY() == p2.getY()) // p1p2 与 y=p0.y平行
				continue;
			if (point.getY() < Math.min(p1.getY(), p2.getY())) // 交点在p1p2延长线上
				continue;
			if (point.getY() >= Math.max(p1.getY(), p2.getY())) // 交点在p1p2延长线上
				continue;
			// 求交点的 X 坐标
			// --------------------------------------------------------------
			double x = (double) (point.getY() - p1.getY())
					* (double) (p2.getX() - p1.getX())
					/ (double) (p2.getY() - p1.getY()) + p1.getX();
			if (x > point.getX())
				nCross++; // 只统计单边交点
		}
		// 单边交点为偶数，点在多边形之外 ---
		return (nCross % 2 == 1);
	}
	
	/**
	 * 判断一个点是否在多边形内部或者边上
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isPointInside(int x, int y) {
		if( x< minX || x> maxX || y< minY || y> maxY) return false;
		Point p1 = new Point(x, y);
		return PtInPolygon(p1,ps);
		
//		Point p1 = new Point(-100000, y);
//		Point p2 = new Point(100000, y);
//
//		ArrayList<Point> al = new ArrayList<Point>();
//		for (int i = 0; i < edges.length; i++) {
//			Point p = intersectLineForPolygon(p1, p2, edges[i].s,
//					edges[i].e,1);
//			if (p != null) {
//				p.tmp_edge = edges[i];
//				al.add(p);
//			}
//		}
//		if (al.size() < 2) {
//			return false;
//		}
//		Point points[] = al.toArray(new Point[0]);
//		java.util.Arrays.sort(points, new Comparator<Point>() {
//
//			public int compare(Point o1, Point o2) {
//				if (o1.x < o2.x)
//					return -1;
//				if (o1.x > o2.x)
//					return 1;
//				return 0;
//			}
//		});
//		int k = -1;
//		for (int i = 0; i < points.length - 1; i++) {
//			if (points[i].x <= x && points[i + 1].x >= x) {
//				k = i;
//				break;
//			}
//		}
//		if (k == -1)
//			return false;
//
//		if (points[k].x == x || points[k + 1].x == x)
//			return true; // 在边上
//
//		int f = Graphics2DUtil.leftRightMeasure(points[k].tmp_edge.s,
//				points[k].tmp_edge.e, x, y);
//		if (f == -1)
//			return false;
//		return true;
	}
}
