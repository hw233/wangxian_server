package com.xuanzhi.tools.statistics;

import java.util.*;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 布尔表达式工具，用于检查布尔表达式的合法性。
 * 
 *
 */
public class BooleanExpressionUtil {

	public static final String[] BOOL_OPERATOR = new String[]{">","<",">=","<=","<>","=","@"};
	
	public static final String[] BOOL_CONNECTOR = new String[]{"and","or","AND","OR"};
	
	public static final char[] SEPERATOR = new char[]{' ','\t','\r','\n'};
	
	public static final char[] OPERATOR = new char[]{'+','-','*','/','%'};
	
	public static boolean isBoolOperator(String s){
		for(int i = 0 ; i < BOOL_OPERATOR.length ; i++){
			if(BOOL_OPERATOR[i].equals(s)) return true;
		}
		return false;
	}
	
	public static boolean isBoolOperator(char s){
		char ch[] = new char[]{s};
		return isBoolOperator(new String(ch));
	}
	
	public static boolean isBoolOperator(char s,char s2){
		char ch[] = new char[]{s,s2};
		return isBoolOperator(new String(ch));
	}
	
	public static boolean isBoolConnector(String s){
		for(int i = 0 ; i < BOOL_CONNECTOR.length ; i++){
			if(BOOL_CONNECTOR[i].equals(s)) return true;
		}
		return false;
	}
	
	public static boolean isRightBracket(String s){
		return s.equals(")");
	}
	
	public static boolean isLeftBracket(String s){
		return s.equals("(");
	}
	
	public static boolean isRightBracket(char s){
		return s == ')';
	}
	
	public static boolean isLeftBracket(char s){
		return s == '(';
	}
	
	public static boolean isBracket(char s){
		return s == ')' || s == '(';
	}
	
	public static boolean isSeperator(char c){
		for(int i = 0 ; i < SEPERATOR.length ; i++){
			if(c == SEPERATOR[i]) return true;
		}
		return false;
	}
	
	public static boolean isOperator(char c){
		for(int i = 0 ; i < OPERATOR.length ; i++){
			if(c == OPERATOR[i]) return true;
		}
		return false;
	}
	
	public static boolean isExpression(String s){
		if(s.length() == 1){
			char c = s.charAt(0);
			if(isOperator(c) || isSeperator(c) || isBracket(c) || isBoolOperator(c) || c == ',') return false;
		}else{
			if(isBoolOperator(s) || isBoolConnector(s) || isFunction(s)) return false;
		}
		return true;
	}
	
	public static boolean isFunction(String s){
		if(!s.startsWith("function:")) return false;
		s = s.substring(9);
		if(s.indexOf("(") == -1) return true;
		return false;
	}
	
	/**
	 * 检查给定的布尔型的表达式是否合法，如果合法，返回布尔表达式中所用到的字段名称，
	 * 如果不合法，抛出异常。
	 * 
	 * 比如：
	 *    (decode((a - b * c)/d) > e * 5 + f) and (a * b > 20 or sign(b * c - 29) = c/d + 4)
	 * 
	 * 那么返回的字段为： a,b,c,d,e,f
	 * 
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public static String [] checkExpression(String expression) throws Exception{
		return checkExpression(expression,false);
	}
	
	private static String escapeExpression(String expression) throws Exception{
		StringBuffer sb = new StringBuffer();
		char chars[] = expression.toCharArray();
		int i = 0;
		while(i < chars.length){
			if(chars[i] == '\''){
				int j = i+1;
				while(j < chars.length && chars[j] != '\''){
					j++;
				}
				if(j == chars.length){
					throw new Exception("表达式单引号不匹配,请检查");
				}
				sb.append(chars[i]);
				sb.append(chars[j]);
				i = j;
			}else{
				sb.append(chars[i]);
			}
			i++;
		}
		
		return sb.toString();
	}
	/**
	 * 检查给定的布尔型的表达式是否合法，如果合法，返回布尔表达式中所用到的字段名称，
	 * 如果不合法，抛出异常。
	 * 
	 * 比如：
	 *    (decode((a - b * c)/d) > e * 5 + f) and (a * b > 20 or sign(b * c - 29) = c/d + 4)
	 * 
	 * 那么返回的字段为： a,b,c,d,e,f
	 * 
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public static String [] checkExpression(String expression,boolean debug) throws Exception{
		HashSet<String> fields = new HashSet<String>();
		Stack<String> stack = new Stack<String>();
		expression = "(" + expression + ") and 1 = 1";
		
		expression = escapeExpression(expression);
		
		int length = expression.length();
		char charArray[] = expression.toCharArray();
		int index = 0;
		while(index < length){
			char ch = charArray[index];
			if(isRightBracket(ch)){
				String subExpression = "" + ch;
				String s = null;
				boolean expectExpression = true;
				boolean containCommas = false;
				while( stack.isEmpty() == false){
					s = stack.pop();
					
					if(isLeftBracket(s)) break;
					if(expectExpression){
						if(isExpression(s) == false && isLeftBracket(s) == false){
							throw new Exception("希望表达式的地方["+(index-1)+"]出现了运算符["+s+"],stack is "+printStack(stack)+ ", subExpression is " + subExpression);
						}
						expectExpression = false;
					}else{
						if(isExpression(s)){
							throw new Exception("希望运算符或者函数的地方["+(index-1)+"]出现了表达式["+s+"] ,stack is "+printStack(stack)+ ", subExpression is " + subExpression);
						}
						if(isFunction(s) == false){
							expectExpression = true;
						}
						if(s.equals(",")){
							containCommas = true;
						}
					}
					subExpression = s + subExpression;
					
				}
				if(s == null){
					throw new Exception("缺少左括号["+subExpression+"],stack is "+printStack(stack));
				}
				if(isLeftBracket(s))
					subExpression = s + subExpression;
				
				if(!stack.isEmpty()){
					String ss = stack.peek();
					if(isFunction(ss)){
						stack.pop();
						stack.push(ss+subExpression);
					}else{
						if(containCommas)
							throw new Exception("逗号出现在非法的位置上,stack is "+printStack(stack));
						stack.push(subExpression);
					}
				}else{
					if(containCommas)
						throw new Exception("逗号出现在非法的位置上,stack is "+printStack(stack));
					stack.push(subExpression);
				}
				index++;
				if(debug)
				System.out.println("stack is " + printStack(stack));
				
			}else if(isBoolOperator(ch)){
				String subExpression = "";
				String s = null;
				boolean expectExpression = true;
				while(stack.isEmpty() == false){
					s = stack.pop();
					if(expectExpression){
						if(isExpression(s) == false){
							throw new Exception("希望表达式的地方["+(index-1)+"]出现了运算符["+s+"] ,stack is "+printStack(stack) + ", subExpression is " + subExpression);
						}
						expectExpression = false;
					}else{
						if(isLeftBracket(s) || isBoolConnector(s)) break;
						
						if(isExpression(s) || s.equals(",")){
							throw new Exception("希望运算符或者函数的地方["+(index-1)+"]出现了表达式["+s+"] ,stack is "+printStack(stack)+ ", subExpression is " + subExpression);
						}
						if(isFunction(s) == false){
							expectExpression = true;
						}
					}
					
					subExpression = s + subExpression;
				}
				if(s != null){
					if(isBoolConnector(s) || isLeftBracket(s))
						stack.push(s);
				}
				
				stack.push(subExpression);
				
				if(isBoolOperator(ch,charArray[index+1])){
					s = new String(new char[]{ch,charArray[index+1]});
					stack.push(s);
					index += 2;
				}else{
					stack.push(""+ch);
					index ++;
				}
				if(debug)
				System.out.println("stack is " + printStack(stack));
				
			}else if(isLeftBracket(ch) || isOperator(ch)){
				stack.push(""+ch);
				index ++;
				if(debug)
				System.out.println("stack is " + printStack(stack));
			}else if(isSeperator(ch)){
				index ++;
			}else if(ch == ','){
				stack.push(""+ch);
				index ++;
				if(debug)
				System.out.println("stack is " + printStack(stack));
			}else{
				int i = index+1;
				for(; i < length ; i++){
					if(charArray[i] == ',' || isBracket(charArray[i]) || isSeperator(charArray[i]) || isOperator(charArray[i]) || isBoolOperator(charArray[i]))
						break;
				}
				String f = new String(charArray,index,i - index);
				
				
				if(isBoolConnector(f)){
					String subExpression = "";
					String s = null;
					boolean expectExpression = true;
					while( stack.isEmpty() == false){
						s = stack.pop();
						if(expectExpression){
							if(isExpression(s) == false){
								throw new Exception("希望表达式的地方["+(index-1)+"]出现了运算符["+s+"] ,stack is "+printStack(stack)+ ", subExpression is " + subExpression);
							}
							expectExpression = false;
						}else{
							if(isBoolOperator(s) || isBoolConnector(s) || isLeftBracket(s)) break;
							
							if(isExpression(s) || s.equals(",")){
								throw new Exception("希望运算符或者函数的地方["+(index-1)+"]出现了表达式["+s+"] ,stack is "+printStack(stack)+ ", subExpression is " + subExpression);
							}
							if(isFunction(s) == false){
								expectExpression = true;
							}
						}
						
						subExpression = s + subExpression;
					}
					
					if(isBoolOperator(s)){
						String m = stack.pop();
						if(m == null){
							throw new Exception("布尔运算符["+s+"]前缺少表达式,stack is "+printStack(stack));
						}
						subExpression = m + s + subExpression;
					}else if(isBoolConnector(s)){
						stack.push(s);
					}else if(isLeftBracket(s)){
						stack.push(s);
					}if(subExpression.length() == 0){
						throw new Exception("连接符["+s+"]前缺少表达式,stack is "+printStack(stack));
					}
					
					stack.push(subExpression);
					
					stack.push(f);
					if(debug)
					System.out.println("stack is " + printStack(stack));
					
					index = i;
					
				}else{
					boolean isFunction = false;
					if(i < length){
						int k = i;
						while(k < length){
							if(isSeperator(charArray[k])){
								k++;
							}else if(isLeftBracket(charArray[k])){
								isFunction = true;
								break;
							}else{
								break;
							}
						}
					}
					
					if(isFunction == false){ //不是函数
						if(StringUtil.isNumberStr(f) == false){
							if(f.startsWith("'") && f.endsWith("'")){
								
							}else {/*else if(StringUtil.isValidIdentity(f)){*/
								fields.add(f);
							}
							/*}else{
								throw new Exception("invalid identity str ["+f+"]");
							}*/
						}
						stack.push(f);
						if(debug)
						System.out.println("stack is " + printStack(stack));
						
						index = i;
					}else{
						stack.push("function:"+f);
						index = i;
						if(debug)
						System.out.println("stack is " + printStack(stack));
					}
				}
			}
		}
		stack.pop();
		stack.pop();
		stack.pop();
		stack.pop();
		String express = "";
		while(stack.isEmpty() == false){
			String s = stack.pop();
			express = s + express;
			if(isExpression(s) == false){
				throw new Exception("表达式不完整，在["+express+"]之前,stack is "+printStack(stack));
			}
		}
		return fields.toArray(new String[0]);
	}
	
	private static String printStack(Stack stack){
		Stack s2 = (Stack)stack.clone();
		String r = "]";
		while(s2.isEmpty() == false){
			String s = (String)s2.pop();
			r = s +" | " + r;
		}
		return "[" + r;
	}
}
