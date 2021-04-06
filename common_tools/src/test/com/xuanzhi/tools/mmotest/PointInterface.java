package com.xuanzhi.tools.mmotest;

/**
 * 代表一个点
 * 
 * 有3个分量，x,z,y。
 * x和z，构成3d空间中的地平面，y为高度。符合右手螺旋规则。
 * 
 *     ^Y
 *     .
 *     .                 ^Z
 *     .              .
 *     .           .
 *     .        .
 *     .     .
 *     .  .
 *     ....................>X
 * 
 * @author <a href='myzdf.bj@gmail.com'>yugang.wang</a>
 *
 */
public interface PointInterface {

	public float getX();
	public float getZ();
	public float getY();
	
	public float setX(float x);
	public float setZ(float z);
	public float setY(float y);
	
}
