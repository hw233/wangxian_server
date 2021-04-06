package com.xuanzhi.tools.ds;

/**
 * 序列发生器，
 * 根据参数发生序列。
 * 可用于服务器和客户端消息序列的产生。
 * 参数由服务器设置。
 * 
 * 为防止外挂，服务器至少可以设计10个不同的协议，用于修改客户端发生器的参数。
 * 
 * 并且，服务器为每个玩家可以制定不同的参数，而且，可以随机来产生修改参数的指令，
 * 并且在选择协议时，也可以采用随机的办法，以加大外挂的难度。
 * 
 * 
 *
 */
public class SequenceGenerator {

	long a = 0;
	long b = 0;
	long c = 0;
	long d = 0;
	long index = 0;
	long f_n_1,f_n_2,f_n_3,f_n_4;
	
	long _a = 0;
	long _b = 0;
	long _c = 0;
	long _d = 0;
	long _index = -1;
	
	public SequenceGenerator(long a,long b,long c,long d){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		index = 0;
		_index = -1;
	}

	/**
	 * 设置即将变化的参数，当内部计数器达到指定的index时，
	 * 发生器就用这里提供的参数，来发生序列
	 * 
	 * 此设计是方便服务器随时可以要求客户端进行发生器参数的修改
	 * 
	 * @param index
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public void set(long index,long a,long b,long c,long d){
		if(index < this.index){
			throw new java.lang.IllegalArgumentException();
		}
		this._a = a;
		this._b = b;
		this._c = c;
		this._d = d;
		this._index = index;
	}
	
	
	/**
	 * 当前发生器的内部计数器
	 * @return
	 */
	public long getIndex(){
		return index;
	}
	
	/**
	 * 即将修改参数的计数器，当当前计数器达到这个值时，修改参数。
	 * 如果没有设置，默认为-1.
	 * 如果一旦修改参数，此值立即被设置为-1
	 * @return
	 */
	public long get_Index(){
		return _index;
	}
	
	/**
	 * 获取下一个序列，调用一次此方法，内部计数器加1
	 * @return
	 */
	public long next(){
		if(_index >= 0 && index>= _index){
			a = _a;
			b = _b;
			c = _c;
			d = _d;
			_index = -1;
		}
		index++;
		long f_n = 0;
		if(index == 1){
			f_n = a;
		}else if(index == 2){
			f_n = a*b;
		}else if(index == 3){
			f_n = a*b*c;
		}else if(index == 4){
			f_n = a*b*c*d;
		}else{
			f_n = (a & f_n_1) + (b|f_n_2) + (c^f_n_3) + (d*f_n_4);
		}
		f_n_4 = f_n_3;
		f_n_3 = f_n_2;
		f_n_2 = f_n_1;
		f_n_1 = f_n;
		return f_n;
	}
}
