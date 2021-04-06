package com.xuanzhi.tools.text;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;



/**
 * 文本显示
 * 
 * 输入为一个自定义好的字符串
 * 格式如下：
 * 
 * 		<F color='' size=''>........</F>
 * 		<I width='' height='' id=''></I>
 * 
 * 例子：
 *       <F color='#45FF9F' size='40'>兽王琳固化</F>
 *       <I>1234.png</I><F>威力值：</F><F color='#FF0000'>570</F>
 *       <F>品质：普通</F>
 *       <F>装备位置：头部</F>
 *       <F>耐久度：300/300</F>
 *       <F>等级：26</F>
 *       
 * @author <a href='myzdf.bj@gmail.com'>yugang.wang</a>
 *
 */
public class MyText  {

	public static class TextItem{
		public CharSequence text;
		public int color = 0xffffff;
		public int size = 25;
	}
	
	public static class ImageItem{
		public int width = 42;
		public int height = 42;
		public int id;
		
		private float x;
		private float y;
	}
	
	public static class BR{
		public static final BR NEWLINE = new BR();
		public static final BR TAB = new BR();
		public static final BR SPACE = new BR();
	}
	
	ArrayList elements = new ArrayList();
	
	//各个字符对应纹理上的坐标位置
	private FloatBuffer texCoords;
	
	//场景中面的坐标位置
	private FloatBuffer vertexeCoords;
	
	//4个点的颜色信息
	private FloatBuffer colorCoords;
	
	private ShortBuffer indexCoords;
	
	
	
	//场景中面的坐标位置
	private FloatBuffer backgroundVertexeCoords;
	//4个点的颜色信息
	private FloatBuffer backgroundColorCoords;
	
	private ShortBuffer backgroundIndexCoords;
	
	
	private int hGap_ = 0;
	int capacity_  = 0;
	
	
	public MyText(){
		
	}
	
public void setText(CharSequence text){
		
		elements.clear();
		
		int i = 0;
		int n = text.length();
		while(i < n){
			char ch = text.charAt(i);
			if(ch == '\t'){
				this.appendBR(BR.TAB);
				i++;
			}else if(ch == ' '){
				this.appendBR(BR.SPACE);
				i++;
			}else if(ch == '\r'){
				i++;
			}else if(ch == '\n'){
				this.appendBR(BR.NEWLINE);
				i++;
			}else{
				//
				//
				if(ch == '<' && i < n - 2 
						&& (text.charAt(i+1) == 'F' || text.charAt(i+1) == 'f' 
							|| text.charAt(i+1) == 'I' || text.charAt(i+1) == 'i') 
						&& (text.charAt(i+2) == '>' || text.charAt(i+2) == ' ') ){
					int j = i+2;
					while(j < n){
						char xx = text.charAt(j);
						if(xx == '>'){
							break;
						}
						j++;
					}
					//没有找到'>'
					if(j == n){
						this.appendText(text.subSequence(i, j));
						i = n;
					}else{
						int k = j+1;
						while(k < n - 3){
							if(text.charAt(k)=='<' && text.charAt(k+1)=='/' &&
									text.charAt(k+2) == text.charAt(i+1) && text.charAt(k+3)=='>'){
								break;
							}
							k++;
						}
						//没有找到</F>
						if(k>=n-3){
							this.appendText(text.subSequence(i, n));
							i = n;
						}else{
							if(text.charAt(i+1) == 'F' || text.charAt(i+1) == 'f'){
								//
								int size = 25;
								int color = 0xffffff;
								try{
									String s = getAttribute(text,i+2,j,"size");
									if(s != null){
										size = Integer.parseInt(s);
									}
									s = getAttribute(text,i+2,j,"color");
									if(s != null){
										color = Integer.decode(s);
									}
								}catch(Exception e){
									e.printStackTrace();
								}
								
								this.appendText(text.subSequence(j+1, k),color,size);
								i = k + 4;
							}else if(text.charAt(i+1) == 'I' || text.charAt(i+1) == 'i'){
								int width = 42;
								int height = 42;
								int id = 0;
								try{
									String s = getAttribute(text,i+2,j,"width");
									if(s != null){
										width = Integer.parseInt(s);
									}
									s = getAttribute(text,i+2,j,"height");
									if(s != null){
										height = Integer.parseInt(s);
									}
									s = getAttribute(text,i+2,j,"id");
									if(s != null){
										id = Integer.parseInt(s);
									}
								}catch(Exception e){
									e.printStackTrace();
								}
								this.appendImage(id, width, height);
								i = k + 4;
							}
						}
					}
				}else{ //不是以 "<F"或者"<I"开头的
					int j = i+1;
					while(j < n){
						
						char xx = text.charAt(j);
						if(xx == '\t'){
							this.appendText(text.subSequence(i, j),0xffffff,25);
							this.appendBR(BR.TAB);
							i = j+1;
							break;
						}else if(xx == ' '){
							this.appendText(text.subSequence(i, j),0xffffff,25);
							this.appendBR(BR.SPACE);
							i = j+1;
							break;
						}else if(xx == '\r'){
							this.appendText(text.subSequence(i, j),0xffffff,25);
							i = j+1;
							break;
						}else if(xx == '\n'){
							this.appendText(text.subSequence(i, j),0xffffff,25);
							this.appendBR(BR.NEWLINE);
							i = j+1;
							break;
						}else{
							if(j < n - 2 && text.charAt(j)=='<' && 
									(text.charAt(j+1) == 'F' || text.charAt(j+1) == 'f' 
									|| text.charAt(j+1) == 'I' || text.charAt(j+1) == 'i') &&
									(text.charAt(j+2) == '>' || text.charAt(j+2)==' ') ){
								this.appendText(text.subSequence(i, j),0xffffff,25);
								i = j;
								break;
							}else{
								j++;
							}
						}
						
					}
					if(j == n){
						this.appendText(text.subSequence(i, j),0xffffff,25);
						i = n;
					}
				}
			}
		}
		this.appendFinish();
	}

private String getAttribute(CharSequence text,int start,int end,CharSequence key){
	
	for(int i = start; i < end - key.length() ; i++){
		boolean b = true;
		for(int j = 0 ; j < key.length(); j++){
			if(text.charAt(i+j) != key.charAt(j)){
				b = false;
				break;
			}
		}
		if(b == true){
			i+=key.length();
			while(i < end && text.charAt(i) == ' '){
				i++;
			}
			if(i >= end || text.charAt(i) != '='){
				return null;
			}
			i++;
			while(i < end && text.charAt(i) == ' '){
				i++;
			}
			if(i >= end || text.charAt(i) != '\''){
				return null;
			}
			i++;
			int j = i;
			while(i < end && text.charAt(i) != '\''){
				i++;
			}
			if(i >= end){
				return null;
			}
			if(text.charAt(i) == '\''){
				return text.subSequence(j, i).toString();
			}
		}
	}
	return null;
}
	
	public MyText appendText(CharSequence text){
		return appendText(text,0xffffff,25);
	}
	
	public MyText appendText(CharSequence text,int color,int size){
		TextItem t = new TextItem();
		t.text = text;
		t.color = color;
		t.size = size;
		elements.add(t);
		return this;
	}
	
	public MyText appendImage(int id,int width,int height){
		ImageItem t = new ImageItem();
		t.width = width;
		t.height = height;
		t.id = id;
		elements.add(t);
		return this;
	}
	
	public MyText appendBR(BR b){
		elements.add(b);
		return this;
	}
	
	public void appendFinish(){
		//construct();
	}
	
	/**
	 * 创建所有的用于贴图的面
	 * 
	 * 坐标原点位于左上角
	 */
	
	

}
