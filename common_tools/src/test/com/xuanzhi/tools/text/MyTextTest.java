package com.xuanzhi.tools.text;

import junit.framework.TestCase;

public class MyTextTest extends TestCase{

	public void testA(){
		MyText mt = new MyText();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<f>你好</f><F color='#ffffff'>hehe</F>\n");
		sb.append("<f>北京你好</f>\t<F color='#ffffff'>hehe2</F>\n");
		sb.append("<i id='0'></i>\t<F color='#ffffff'>hehe3</F>\n");
		mt.setText(sb.toString());
		
		assertEquals(11,mt.elements.size());
		MyText.TextItem it = (MyText.TextItem)mt.elements.get(0);
		assertEquals("你好",it.text);
		
		it = (MyText.TextItem)mt.elements.get(3);
		assertEquals("北京你好",it.text);
		
		it = (MyText.TextItem)mt.elements.get(9);
		assertEquals("hehe3",it.text);
		
		
	}
	
	
	public void testB(){
		MyText mt = new MyText();
		
		StringBuffer sb = new StringBuffer();
		sb.append("极动鲨开发了撒旦发了看\n");
		sb.append("<f>北京你好</f>\t大厦法撒旦发建立<F color='#ffffff'>hehe2</F>\n");
		sb.append("<i id='0' width='1234' height   =   '234'></i>当时房间里萨克斯开发来看是否<F color ='#ffff23' size='25'>hehe3</F>\n");
		sb.append("的沙发上啦开发击杀离开对方");
		mt.setText(sb.toString());
		
		assertEquals(12,mt.elements.size());
		MyText.TextItem it = (MyText.TextItem)mt.elements.get(0);
		assertEquals("极动鲨开发了撒旦发了看",it.text);
		
		MyText.TextItem it1 = (MyText.TextItem)mt.elements.get(9);
		assertEquals("hehe3",it1.text);
		assertEquals(0xffff23,it1.color);
		assertEquals(25,it1.size);
		
		MyText.BR b = (MyText.BR)mt.elements.get(3);
		assertEquals(MyText.BR.TAB,b);
		
		it = (MyText.TextItem)mt.elements.get(11);
		assertEquals("的沙发上啦开发击杀离开对方",it.text);
		
		MyText.ImageItem it2 = (MyText.ImageItem)mt.elements.get(7);
		assertEquals(1234,it2.width);
		assertEquals(234,it2.height);
		assertEquals(0,it2.id);
		
		
		
		
		
	}
}
