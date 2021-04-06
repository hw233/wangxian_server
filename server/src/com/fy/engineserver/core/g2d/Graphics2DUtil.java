package com.fy.engineserver.core.g2d;


public class Graphics2DUtil {
	/**
	 * 判断两个矩形在同一个坐标系下是否相交
	 * 
	 * @param r1x1
	 *            矩形r1的左边缘
	 * @param r1y1
	 *            矩形r1的上边缘
	 * @param r1x2
	 *            矩形r1的右边缘
	 * @param r1y2
	 *            矩形r1的下边缘
	 * @param r2x1
	 *            矩形r2的左边缘
	 * @param r2y1
	 *            矩形r2的上边缘
	 * @param r2x2
	 *            矩形r2的右边缘
	 * @param r2y2
	 *            矩形r2的下边缘
	 * @return
	 */
	public static boolean intersectRect(int r1x1, int r1y1, int r1x2, int r1y2,
			int r2x1, int r2y1, int r2x2, int r2y2) {
		return !(r2x1 >= r1x2 || r2y1 >= r1y2 || r2x2 <= r1x1 || r2y2 <= r1y1);
	}

	/**
	 * 判断两条线段是否相交，重叠不算相交
	 * 
	 * @param p1
	 * @param p2
	 * @param q1
	 * @param q2
	 * @return true —— 相交
	 */
	public static boolean intersectLine(Point p1, Point p2, Point q1, Point q2) {
		return getPointOfIntersection(p1, p2, q1, q2) != null;
	}

	/**
	 * 判断两条线段是否相交，并返回交点。重叠不算相交
	 * 
	 * @param p1
	 * @param p2
	 * @param q1
	 * @param q2
	 * @return 返回交点
	 */
	public static Point getPointOfIntersection(Point p1, Point p2, Point q1,
			Point q2) {
		if (q1.x < q2.x && p1.x < q1.x && p2.x < q1.x)
			return null;
		if (q1.x < q2.x && p1.x > q2.x && p2.x > q2.x)
			return null;
		if (q1.x > q2.x && p1.x > q1.x && p2.x > q1.x)
			return null;
		if (q1.x > q2.x && p1.x < q2.x && p2.x < q2.x)
			return null;

		if (q1.y < q2.y && p1.y < q1.y && p2.y < q1.y)
			return null;
		if (q1.y < q2.y && p1.y > q2.y && p2.y > q2.y)
			return null;
		if (q1.y > q2.y && p1.y > q1.y && p2.y > q1.y)
			return null;
		if (q1.y > q2.y && p1.y < q2.y && p2.y < q2.y)
			return null;

		int a1 = p2.y - p1.y;
		int b1 = p2.x - p1.x;
		int a2 = q2.y - q1.y;
		int b2 = q2.x - q1.x;
		int k = a1 * b2 - a2 * b1;
		if (k == 0) {
			// 平行
		} else {

			int x = (int) (1L * a1 * b2 * p1.x - 1L * a2 * b1 * q1.x - 1L * b1 * b2 * p1.y + 1L * b1 * b2 * q1.y) / k;
			int y = (int) (1L * b1 * a2 * p1.y - 1L * b2 * a1 * q1.y + 1L * a1 * a2 * q1.x - 1L * a1 * a2 * p1.x) / (-1 * k);
			if (1L * (x - p1.x) * (x - p2.x) > 0 || 1L * (x - q1.x) * (x - q2.x) > 0) {

			} else {
				if (p1.x == p2.x || q1.x == q2.x) {
					if (p1.x == p2.x) {
						if (1L * (y - p1.y) * (y - p2.y) <= 0) {
							Point p = new Point(x, y);
							// System.out.println("(x,y)=("+x+","+y+"), p1=("+p1.x+","+p1.y+"),p2=("+p2.x+","+p2.y+"),q1=("+q1.x+","+q1.y+"),q2=("+q2.x+","+q2.y+")");
							return p;
						}
					} else {
						if (1L * (y - q1.y) * (y - q2.y) <= 0) {
							Point p = new Point(x, y);
							// System.out.println("(x,y)=("+x+","+y+"), p1=("+p1.x+","+p1.y+"),p2=("+p2.x+","+p2.y+"),q1=("+q1.x+","+q1.y+"),q2=("+q2.x+","+q2.y+")");
							return p;
						}
					}
				} else {
					Point p = new Point(x, y);
					// System.out.println("(x,y)=("+x+","+y+"), p1=("+p1.x+","+p1.y+"),p2=("+p2.x+","+p2.y+"),q1=("+q1.x+","+q1.y+"),q2=("+q2.x+","+q2.y+")");
					return p;
				}
			}
		}

		return null;
	}

	/**
	 * 检测点(x,y)在向量 p1 --> p2 的左边，右边，还是在向量所在的直线上 返回 -1 表示在左边 返回 0 表示在向量所在的直线上 返回
	 * 1 表示在右边
	 */
	public static int leftRightMeasure(Point p1, Point p2, int x, int y) {
		int f = (y - p1.y) * (p2.x - p1.x) - (p2.y - p1.y) * (x - p1.x);
		if (f < 0)
			return -1;
		if (f == 0)
			return 0;
		return 1;
	}
	
	/**
	 * 计算点到某个线段的距离，如果点的投影不在线段上
	 * 距离为到端点的最小距离
	 * @param x
	 * @param y
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static int distanceOfPointToSegmentLine(int x,int y,Point p1,Point p2){
		if( 1L * ((y - p2.y) *(p2.y - p1.y) + (x - p2.x)*(p2.x - p1.x)) *
				((y - p1.y) *(p2.y - p1.y) + (x - p1.x)*(p2.x - p1.x)) <= 0){
			//表示点到线段的垂直线夹在点到两端的直线中
			int d = (p1.y - p2.y) *(p1.y - p2.y) + (p1.x - p2.x)*(p1.x - p2.x);
			if(d == 0){
				int d1 = (y - p2.y) *(y - p2.y) + (x - p2.x)*(x - p2.x);
				return (int)Math.sqrt(d1);
			}else{
				return (int)Math.abs(((y-p1.y)*(p2.x-p1.x) -(x-p1.x)*(p2.y-p1.y))/Math.sqrt(d));
			}
		}else{
			int d1 = (y - p2.y) *(y - p2.y) + (x - p2.x)*(x - p2.x);
			int d2 = (y - p1.y) *(y - p1.y) + (x - p1.x)*(x - p1.x);
			if(d1 < d2){
				return (int)Math.sqrt(d1);
			}else{
				return (int)Math.sqrt(d2);
			}
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

		int[]p = intersectLineForPolygon((int)p1.x,(int)p1.y,(int)p2.x,(int)p2.y,
				(int)q1.x,(int)q1.y,(int)q2.x,(int)q2.y,(int)precision);
		if(p == null) return null;
		return new Point(p[0],p[1]);
		
//		int a1 = p2.y - p1.y;
//		int b1 = p2.x - p1.x;
//		int a2 = q2.y - q1.y;
//		int b2 = q2.x - q1.x;
//		int k = a1 * b2 - a2 * b1;
//		int l = a1 * a2 + b1 * b2;
//		if(k == 0 || Math.abs(k) * 100  <  Math.abs(l)){ // 夹角小于2度
//			//平行
//		}else{
//			
//			long x = (1L * a1 * b2 * p1.x - 1L * a2 * b1 * q1.x - 1L * b1 * b2 *p1.y + 1L * b1 * b2 * q1.y)/k;
//			long y = (1L *b1 * a2 * p1.y - 1L *b2 * a1 * q1.y + 1L *a1 * a2 * q1.x - 1L * a1 * a2 * p1.x)/(-1 * k);
//			long t1 = 1L * (x - p1.x) * (x - p2.x);
//			long t2 = 1L * (x - q1.x) * (x - q2.x);
//			if( t1 > 0 || t2 > 0){
//				if(Math.abs(x-q1.x) + Math.abs(y-q1.y) <= precision
//						||  Math.abs(x-q2.x) + Math.abs(y-q2.y) <= precision){
//					if(t1 < 0){ 
//						return new Point((int)x,(int)y);
//					}else if(t1 == 0 && 1L * (y - p1.y) * (y - p2.y) <= 0) {
//						return new Point((int)x,(int)y);
//					}
//					
//				}
//				if(Math.abs(x-p1.x) + Math.abs(y-p1.y) <= precision 
//						||  Math.abs(x-p2.x) + Math.abs(y-p2.y)<= precision ){
//					if(t2 < 0){
//						return new Point((int)x,(int)y);
//					}
//					else if(t2 == 0 && 1L * (y - q1.y) * (y - q2.y) <= 0){
//						return new Point((int)x,(int)y);
//					}
//				}
//			}else{
//				if(t1 == 0 && t2 < 0){
//					if( 1L * (y - p1.y) * (y - p2.y) <= 0 
//							|| Math.abs(y-p1.y) <= precision ||  Math.abs(y-p2.y) <= precision){
//						return new Point((int)x,(int)y);
//					}
//				}else if(t1 < 0 && t2 == 0){
//					if( 1L * (y - q1.y) * (y - q2.y) <= 0
//							|| Math.abs(y-q1.y) <= precision ||  Math.abs(y-q2.y) <= precision){
//						return new Point((int)x,(int)y);
//					}
//				}else if(t1 == 0 && t2 == 0){
//					if( (1L * (y - p1.y) * (y - p2.y) <= 0 
//							|| Math.abs(y-p1.y) <= precision ||  Math.abs(y-p2.y)  <= precision)
//							&& ( 1L * (y - q1.y) * (y - q2.y) <= 0
//									|| Math.abs(y-q1.y)  <= precision ||  Math.abs(y-q2.y)  <= precision)){
//						return new Point((int)x,(int)y);
//					}
//				}else{
//					return new Point((int)x,(int)y);
//				}
//			}
//		}
//		
//		return null;
	}
	
	
	
	/**
	 * 判断两条线段是否相交，重叠不算相交
	 * 
	 * @param p1
	 * @param p2
	 * @param q1
	 * @param q2
	 * @param precision
	 *            精度，标识交点与端点的距离，如果小于等于此距离，表明相交，这里的距离是用x方向差别和y方向差别只和表示的。
	 * @return
	 */
	public static int[] intersectLineForPolygon(int p1x, int p1y,
			int p2x,int p2y,
			int q1x,int q1y, 
			int q2x,int q2y,
			int precision) {
		
		if(p1x <= p2x && q1x < p1x - precision && q2x < p1x - precision) return null;
		if(p1x <= p2x && q1x > p2x + precision && q2x > p2x + precision) return null;
		if(p1x >= p2x && q1x > p1x + precision && q2x > p1x + precision) return null;
		if(p1x >= p2x && q1x < p2x - precision && q2x < p2x - precision) return null;
		if(p1y <= p2y && q1y < p1y - precision && q2y < p1y - precision) return null;
		if(p1y <= p2y && q1y > p2y + precision && q2y > p2y + precision) return null;
		if(p1y >= p2y && q1y > p1y + precision && q2y > p1y + precision) return null;
		if(p1y >= p2y && q1y < p2y - precision && q2y < p2y - precision) return null;
		
		int a1 = p2y - p1y;
		int b1 = p2x - p1x;
		int a2 = q2y - q1y;
		int b2 = q2x - q1x;
		int k = a1 * b2 - a2 * b1;
		int l = a1 * a2 + b1 * b2;
		if (k == 0 || Math.abs(k) * 100 < Math.abs(l)) { // 夹角小于2度
			// 平行
		} else {
			long x = (1L * a1 * b2 * p1x - 1L * a2 * b1 * q1x - 1L * b1 * b2 * p1y + 1L * b1 * b2 * q1y) / k;
			long y = (1L * b1 * a2 * p1y - 1L * b2 * a1 * q1y + 1L * a1 * a2 * q1x - 1L * a1 * a2 * p1x) / (-1 * k);
			long t1 = 1L * (x - p1x) * (x - p2x);
			long t2 = 1L * (x - q1x) * (x - q2x);
			
			//if( Math.abs(x - 795) < 10 && Math.abs(y - 1191) < 10)
			//	System.out.println("("+p1x+","+p1y+")->("+p2x+","+p2y+") X ("+q1x+","+q1y+")->("+q2x+","+q2y+") == ("+x+","+y+") t1 = "+t1+" t2 = " + t2);
			
			if (t1 > 0 || t2 > 0) {
				if (Math.abs(x - q1x) + Math.abs(y - q1y) <= precision
						|| Math.abs(x - q2x) + Math.abs(y - q2y) <= precision) {
					if (t1 < 0) {
						return new int[]{(int) x, (int) y};
					} else if (t1 == 0 && 1L * (y - p1y) * (y - p2y) <= 0) {
						return new int[]{(int) x, (int) y};
					}
				}
				if (Math.abs(x - p1x) + Math.abs(y - p1y) <= precision
						|| Math.abs(x - p2x) + Math.abs(y - p2y) <= precision) {
					if (t2 < 0) {
						return new int[]{(int) x, (int) y};
					} else if (t2 == 0 && 1L * (y - q1y) * (y - q2y) <= 0) {
						return new int[]{(int) x, (int) y};
					}
				}
			} else {
				if (t1 == 0 && t2 < 0) {
					if (1L * (y - p1y) * (y - p2y) <= 0 || Math.abs(y - p1y) <= precision
							|| Math.abs(y - p2y) <= precision) {
						return new int[]{(int) x, (int) y};
					}
				} else if (t1 < 0 && t2 == 0) {
					if (1L * (y - q1y) * (y - q2y) <= 0 || Math.abs(y - q1y) <= precision
							|| Math.abs(y - q2y) <= precision) {
						return new int[]{(int) x, (int) y};
					}
				} else if (t1 == 0 && t2 == 0) {
					if ((1L * (y - p1y) * (y - p2y) <= 0 || Math.abs(y - p1y) <= precision || Math.abs(y - p2y) <= precision)
							&& (1L * (y - q1y) * (y - q2y) <= 0 || Math.abs(y - q1y) <= precision || Math.abs(y
									- q2y) <= precision)) {
						return new int[]{(int) x, (int) y};
					}
				} else {
					return new int[]{(int) x, (int) y};
				}
			}
		}

		return null;
	}
	
	/**
	 * 判断一个点是否在一个凸多边形内。
	 * 
	 * @param poly
	 * @param x
	 * @param y
	 * @param includingEdge true表示在边上也算在多边形内
	 * @return
	 */
	public static boolean isPointInConvexPolygon(Polygon poly,int x,int y,boolean includingEdge){
		int k = 1;
		if(includingEdge) k = 0;
		for(int i = 0 ; i < poly.edges.length ; i++){
			if(leftRightMeasure(poly.edges[i].s,poly.edges[i].e,x,y) < k){
				return false;
			}
		}
		return true;
	}

	public static int distance(Point p1, Point p2) {
		int dx = p1.x - p2.x;
		int dy = p1.y - p2.y;
		if (dx == 0) {
			return dy > 0 ? dy : -dy;
		}
		if (dy == 0) {
			return dx > 0 ? dx : -dx;
		}
		return (int) sqrt(dx * dx + dy * dy);
	}

	public static long sqrt(long x) {
		long y = 0;
		long b = (~Long.MAX_VALUE) >>> 1;
		while (b > 0) {
			if (x >= y + b) {
				x -= y + b;
				y >>= 1;
				y += b;
			} else {
				y >>= 1;
			}
			b >>= 2;
		}
		return y;
	}
	
	
}
