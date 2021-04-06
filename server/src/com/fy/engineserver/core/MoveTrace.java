package com.fy.engineserver.core;

import java.util.ArrayList;
import java.util.Collection;

import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.gametime.SystemTime;

public class MoveTrace {
	private boolean finish;
	private short[] roadLen;
	private Point[] roadPoints;
	private LivingObject livingObject;
	private int totalLength;
	private int currentRoad;

	// 服务器设置路径开始的时间
	private long startTimestamp;

	// 客户端设置的路径结束的时间
	private long destineTimestamp;

	// 用于检测加速外挂
	public boolean isClientMoveTrace = false;// 是否是客户端发出的移动路径
	public long clientGameTimestamp;// 客户端发出移动路径时的游戏时间

	public int clientSpeed;// 客户端玩家移动速度

	public long receiveTimestamp;

	// 客户端的产生路径时的速度
	public int pathSpeed;

	// 服务器计算出来的路径实际的速度
	// 计算的标准时：下一条路径的起点与这条路径的起点之间的距离/下一条路径发出时间与此条路径发出时间之差
	public int realSpeed;

	public long realEndTime;
	public int realEndX;
	public int realEndY;

	// 计算路径的速度，此方法在接收到这条路径时调用
	public void calculatePathSpeed(long now) {
		receiveTimestamp = now;
		long t = destineTimestamp - clientGameTimestamp;
		if (t <= 0) {
			pathSpeed = 9999;
		} else {
			pathSpeed = (int) (totalLength * 1000 / t);
		}
	}

	// 计算客户端真实移动的速度
	// nextPathSentTime 表示下一条路径发出的时间
	// x 下一条路径的开始位置
	// y 下一条路径的开始位置
	public void calculateRealSpeed(long nextPathClientGameTimestamp, int x, int y) {
		realEndTime = nextPathClientGameTimestamp;
		realEndX = x;
		realEndY = y;
		int d = (realEndX - roadPoints[0].x) * (realEndX - roadPoints[0].x) + (realEndY - roadPoints[0].y) * (realEndY - roadPoints[0].y);
		long t = nextPathClientGameTimestamp - clientGameTimestamp;
		if (d > 0 && t > 10) {
			realSpeed = (int) (Math.sqrt(d) * 1000 / t);
		}
	}

	//private Collection<LivingObject> livingNotifySet = new HashSet<LivingObject>();

	private ArrayList<LivingObject> livingNotifySet = null; //new ArrayList<LivingObject>(5);
	
	public MoveTrace(short[] roadLen, Point[] roadPoints, long time, long clientGameTimestamp, int clientSpeed) {
		this.startTimestamp = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.destineTimestamp = time;
		this.roadLen = roadLen;
		this.roadPoints = roadPoints;
		
		init();

		isClientMoveTrace = true;
		this.clientGameTimestamp = clientGameTimestamp;
		this.clientSpeed = clientSpeed;
	}

	public MoveTrace(short[] roadLen, Point[] roadPoints, long time) {
		this.startTimestamp = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.destineTimestamp = time;
		this.roadLen = roadLen;
		this.roadPoints = roadPoints;
		
		init();
	}

	public MoveTrace(MoveTrace4Client trace) {
		this.startTimestamp = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		short[] xs = trace.getPointsX();
		short[] ys = trace.getPointsY();
		roadPoints = new Point[xs.length];
		
		if(roadPoints.length < 2){
			throw new IllegalArgumentException("路径点不能小于2");
		}
		
		roadLen = new short[roadPoints.length-1];
		
		for (int i = 0; i < roadPoints.length; i++) {
			roadPoints[i] = new Point(xs[i], ys[i]);
			if(i > 0){
				roadLen[i-1] = (short)Math.sqrt((roadPoints[i].y - roadPoints[i-1].y)*(roadPoints[i].y - roadPoints[i-1].y) + (roadPoints[i].x - roadPoints[i-1].x)*(roadPoints[i].x - roadPoints[i-1].x));
			}
		}
		
		destineTimestamp = trace.getDestineTimestamp();
		
		init();

		isClientMoveTrace = true;
		this.clientGameTimestamp = trace.getStartTimestamp();
		this.clientSpeed = trace.getSpeed();
	}

	private void init() {
		totalLength = 0;
		for (int i = 0; i < roadLen.length; i++) {
			totalLength += roadLen[i];
		}
		currentRoad = 0;
	}

	public long getDestineTimestamp() {
		return destineTimestamp;
	}

	public void setDestineTimestamp(long destineTimestamp) {
		this.destineTimestamp = destineTimestamp;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	/**
	 * 沿着路径向前移动，速度根据目的时间来定
	 */
	public void moveFollowPath(long currentTime) {
		if (finish) {
			livingObject.removeMoveTrace();
		} else {
			if (currentTime >= destineTimestamp) {
				livingObject.setX(getEndPointX());
				livingObject.setY(getEndPointY());
				finish = true;
			} else if (currentTime <= this.startTimestamp) {
				livingObject.setX(getStartPointX());
				livingObject.setY(getStartPointY());
			} else {
				int currentLength = getCurrentLength(currentTime);
				int roadIndex = 0;
				while (roadIndex < roadLen.length && currentLength > 0 && currentLength >= roadLen[roadIndex]) {
					currentLength -= roadLen[roadIndex];
					++roadIndex;
				}
				currentRoad = roadIndex;
				Point start = roadPoints[roadIndex];
				Point end = roadPoints[roadIndex + 1];
				int cx, cy;
				if (currentLength == 0) {
					cx = start.x;
					cy = start.y;
					if (roadIndex == roadLen.length) {
						finish = true;
					}
				} else {
					int L = roadLen[roadIndex];
					int L2 = roadLen[roadIndex] - currentLength;
					int dx = L2 * start.x + currentLength * end.x;
					cx = dx / L;
					// 对计算结果四舍五入，以保证更加精确
					if (dx % L > L >> 1) {
						++cx;
					}
					int dy = L2 * start.y + currentLength * end.y;
					cy = dy / L;
					// 对计算结果四舍五入，以保证更加精确
					if (dy % L > L >> 1) {
						++cy;
					}
				}
				livingObject.setX(cx);
				livingObject.setY(cy);
				livingObject.face(start.x, start.y, end.x, end.y);
			}
		}
	}

	public void setLivingObject(LivingObject living) {
		livingObject = living;
	}

	public int getStartPointX() {
		return roadPoints[0].x;
	}

	public int getStartPointY() {
		return roadPoints[0].y;
	}

	private int getEndPointX() {
		return roadPoints[roadPoints.length - 1].x;
	}

	private int getEndPointY() {
		return roadPoints[roadPoints.length - 1].y;
	}

	public int getCurrentLength(long time) {
		if (destineTimestamp - startTimestamp == 0) return 0;
		return (int) (totalLength * (time - startTimestamp) / (destineTimestamp - startTimestamp));
	}

	public void speedChanged(int speed, long delay) {
		if(speed <= 0) speed = 1;
		if (!finish) {
			int leftRoadNum = roadLen.length - currentRoad;

			Point[] points = new Point[leftRoadNum + 1];
			points[0] = new Point(livingObject.getX(), livingObject.getY());
			System.arraycopy(roadPoints, currentRoad + 1, points, 1, leftRoadNum);
			roadPoints = points;
			short[] leftRoads = new short[leftRoadNum];
			leftRoads[0] = (short) Graphics2DUtil.distance(points[0], points[1]);
			System.arraycopy(roadLen, currentRoad + 1, leftRoads, 1, leftRoadNum - 1);
			roadLen = leftRoads;

			init();
			startTimestamp = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			// destineTimestamp = startTimestamp + totalLength / speed * 1000;
			destineTimestamp = startTimestamp + totalLength * 1000 / speed + delay;
		}
	}

	public boolean hasNotify(LivingObject living) {
		if(livingNotifySet == null) return false;
		return livingNotifySet.contains(living);
	}

	public void setNotifyMark(LivingObject living) {
		if(livingNotifySet == null)
			livingNotifySet = new ArrayList<LivingObject>(4);
		livingNotifySet.add(living);
	}

//	public void removeNotifyMark(LivingObject living) {
//		livingNotifySet.remove(living);
//	}


	public Collection<LivingObject> getLivingNotifySet() {
		return livingNotifySet;
	}


	public MoveTrace4Client getLeftPath() {
		short[] leftRoads = new short[roadLen.length - currentRoad];
		short[] pointX = new short[leftRoads.length + 1];
		short[] pointY = new short[leftRoads.length + 1];

		Point startPoint = new Point(livingObject.getX(), livingObject.getY());
		Point secondPoint = roadPoints[currentRoad + 1];

		leftRoads[0] = (short) Graphics2DUtil.distance(startPoint, secondPoint);
		System.arraycopy(roadLen, currentRoad + 1, leftRoads, 1, leftRoads.length - 1);

		pointX[0] = (short) startPoint.x;
		pointY[0] = (short) startPoint.y;
		for (int i = 1; i < pointX.length; i++) {
			pointX[i] = (short) roadPoints[currentRoad + i].x;
			pointY[i] = (short) roadPoints[currentRoad + i].y;
		}

		return new MoveTrace4Client(livingObject.getClassType(), livingObject.getId(), destineTimestamp, leftRoads, pointX, pointY);
	}

	public short[] getRoadLen() {
		return roadLen;
	}

	public void setRoadLen(short[] roadLen) {
		this.roadLen = roadLen;
	}

	public Point[] getRoadPoints() {
		return roadPoints;
	}

	public void setRoadPoints(Point[] roadPoints) {
		this.roadPoints = roadPoints;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

	public int getCurrentRoad() {
		return currentRoad;
	}

	public void setCurrentRoad(int currentRoad) {
		this.currentRoad = currentRoad;
	}

	public LivingObject getLivingObject() {
		return livingObject;
	}

	public double alreadyMoved() {
		return ((double) getCurrentLength(SystemTime.currentTimeMillis())) / totalLength;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < roadPoints.length; i++) {
			sb.append("(" + roadPoints[i].x + "," + roadPoints[i].y + ")");
			if (i < roadPoints.length - 1) {
				sb.append("->");
			}
		}
		return sb.toString();
	}
}
