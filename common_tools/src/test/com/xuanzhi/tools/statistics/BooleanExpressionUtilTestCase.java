package com.xuanzhi.tools.statistics;

import java.util.*;

import junit.framework.*;
public class BooleanExpressionUtilTestCase extends TestCase{

	public BooleanExpressionUtilTestCase(){
		super("BooleanExpressionUtilTestCase");
	}
	
	public void testSimple() throws Exception{
		/*
		String expression = "(smc_num*price)/highprice";
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression,true);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
		*/
//		/{{product=abc, channeltype=meida}=7, {product=adb, channeltype=media}=2},userKey={product=abc, channeltype=media}
		Map<Map<String,String>,Integer> aaa = new HashMap<Map<String,String>,Integer>();
		
		Map<String,String> a = new HashMap<String,String>();
		a.put("product","abc");
		a.put("channeltype","meida");
		aaa.put(a,7);
		
		a = new HashMap<String,String>();
		a.put("product","abd");
		a.put("channeltype","meida");
		aaa.put(a,2);
		
		Map<String,String> b = new HashMap<String,String>();
		b.put("channeltype","meida");
		b.put("product","abc");
		
		
		//Assert.assertTrue(b.equals(a));
		
		int n = aaa.get(b);
		
		
	}
	
	public void testA() throws Exception{
		
		String expression = "(operator + node*price)/360 > highprice and abc = 789";
		HashSet<String> set = new HashSet<String>();
		set.add("operator");
		set.add("node");
		set.add("price");
		set.add("highprice");
		set.add("abc");
		
		
		
		String fields[] = BooleanExpressionUtil.checkExpression(expression);
		
		Assert.assertEquals(set.size(),fields.length);
		
		for(int i = 0 ; i < fields.length ; i++){
			Assert.assertTrue(set.contains(fields[i]));
		}
	}
	
	public void testTodate() throws Exception{
		String expression = " lastRequestTime >= to_date('2012-04-04 00:00:00','yyyy-mm-dd hh24:mi:ss') and  lastRequestTime <= to_date('2012-04-05 00:00:00','yyyy-mm-dd hh24:mi:ss')";
		String fields[] = BooleanExpressionUtil.checkExpression(expression);
		for(int i = 0 ; i < fields.length ; i++)
			System.out.println("fields["+i+"]" + fields[i]);

		
		expression = " abc like 'xxx%' AND aacc > 5";
		expression = expression.replaceAll(" like ", " @ ");
		fields = BooleanExpressionUtil.checkExpression(expression);
		for(int i = 0 ; i < fields.length ; i++)
			System.out.println("fields["+i+"]" + fields[i]);
		
		
		expression = " id in(1234,1234,5677,1234)";
		//expression = expression.replaceAll(" in ", " @ ");
		fields = BooleanExpressionUtil.checkExpression(expression);
		for(int i = 0 ; i < fields.length ; i++)
			System.out.println("fields["+i+"]" + fields[i]);
	}
	
	public void testA2() throws Exception{
		
		String expression = "(smc_num*price)/highprice>fee_succ_num  or slient_num>fee_succ_num ";
		HashSet<String> set = new HashSet<String>();
		set.add("smc_num");
		set.add("fee_succ_num");
		set.add("price");
		set.add("highprice");
		set.add("slient_num");
		
		
		
		String fields[] = BooleanExpressionUtil.checkExpression(expression);
		
		Assert.assertEquals(set.size(),fields.length);
		
		for(int i = 0 ; i < fields.length ; i++){
			Assert.assertTrue(set.contains(fields[i]));
		}
	}
	
	public void testA3() throws Exception{
		
		String expression = "(smc_num*price)/highprice>>fee_succ_num  or slient_num>fee_succ_num ";
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testA4() throws Exception{
		
		String expression = "(smc_num*price)/highprice<>fee_succ_num  or slient_num ++ 456 >fee_succ_num ";
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testB() throws Exception{
		
		String expression = "(a+b*c-d/(a-b) > b -c*6 or exy + abc <= a % (b - c))and(a -b>=c or c - ((d*57) % c) <=a)";
		HashSet<String> set = new HashSet<String>();
		set.add("a");
		set.add("b");
		set.add("c");
		set.add("d");
		set.add("exy");
		set.add("abc");
		
		String fields[] = BooleanExpressionUtil.checkExpression(expression);
		
		Assert.assertEquals(set.size(),fields.length);
		
		for(int i = 0 ; i < fields.length ; i++){
			Assert.assertTrue(set.contains(fields[i]));
		}
	}
	
	public void testB2() throws Exception{
		
		String expression = "(a+b*c-d/(a-b) > b -c*6 or exy + abc <= (a % (b - c))and(a -b>=c or c - ((d*57) % c) <=a)";
		
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void testB3() throws Exception{
		
		String expression = "(a+b*c-d/(a-b) > b -c*6 or exy + abc <= a % (b - c)) and or (a -b>=c or c - ((d*57) % c) <=a)";
		
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	public void testC() throws Exception{
		
		String expression = "a+b>ff(a) and ff(a,b)<c";
		HashSet<String> set = new HashSet<String>();
		set.add("a");
		set.add("b");
		set.add("c");
		
		String fields[] = BooleanExpressionUtil.checkExpression(expression);
		
		Assert.assertEquals(set.size(),fields.length);
		
		for(int i = 0 ; i < fields.length ; i++){
			Assert.assertTrue(set.contains(fields[i]));
		}
	}
	
	public void testZ() throws Exception{
		
		String expression = "a + ff(a+b,c+d,a) >= b";
		
		String fields[] = BooleanExpressionUtil.checkExpression(expression);
	}

	public void testZ2() throws Exception{
		
		String expression = "a + ff(a+b,c+d,a,) >= b";
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void testZ3() throws Exception{
		
		String expression = "a + ff(a+b,c+d(a) >= b";
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testZ4() throws Exception{
		
		String expression = "a + ff(a+b,c+d(a) >= b";
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

	public void testY() throws Exception{
		
		String expression = "(ff(a + b*c,e - (aa*bb+2)/cc,(ee-f)*2,gg(a,b*2))*ff(a,b,c) + (d*c*f/g) % 10 > (a+b-c*d)*(a-b) and abc = 789) or (b6+c+d > b*a and (e*f-d)/(a-b)<b-f*d/f(a2-b3))";
		
		String fields[] = BooleanExpressionUtil.checkExpression(expression);
	}

	public void testX() throws Exception{
		
		String expression = "(xxx(a + b*c,e*f,a*2)/360 + (d*c*f/g) % 10 > (a+b-c*d)*(a-b) and abc = 789) or (b6+c+d > b*a and (e*f-d)/(a-b)<b-f*d/f(a2-b3))";
		HashSet<String> set = new HashSet<String>();
		set.add("a");
		set.add("b");
		set.add("c");
		set.add("d");
		set.add("e");
		set.add("f");
		set.add("g");
		set.add("abc");
		set.add("a2");
		set.add("b3");
		set.add("b6");
		
		
		String fields[] = BooleanExpressionUtil.checkExpression(expression);
		
		Assert.assertEquals(set.size(),fields.length);
		
		for(int i = 0 ; i < fields.length ; i++){
			Assert.assertTrue(set.contains(fields[i]));
		}
	}

	public void testZ5() throws Exception{
		
		String expression = "a + ff(a+b,c+d;a) >= b";
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testM() throws Exception{
		
		String expression = "smc_num * 6price > aaaa";
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testM2() throws Exception{
		
		String expression = "smc_num * price > 'aaaa'";
		String fields[] = BooleanExpressionUtil.checkExpression(expression);
		Assert.assertEquals(2,fields.length);
	}
	
	public void testM3() throws Exception{
		
		String expression = "smc_num * $price > aaaa";
		try{
			String fields[] = BooleanExpressionUtil.checkExpression(expression);
			Assert.fail("should throw exception");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testM4() throws Exception{
		
		String expression = "a + ff (a,b) > c";
		String fields[] = BooleanExpressionUtil.checkExpression(expression,true);
	}
	
}
