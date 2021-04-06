package com.xuanzhi.tools.text;


import java.util.*;

public class ExpressionUtil {
  
	public static void main( String[ ] args ) {
        String expr = "100 * 100";
        String type = "double";
        if(args.length > 0){
        	expr = args[0];
        }
        if(args.length > 1){
        	type = args[1];
        }
        try {
        	if(type.equals("int"))
        		System.out.println( evaluateAsInteger( expr));
        	else if(type.equals("long"))
        		System.out.println( evaluateAsLong( expr));
        	else if(type.equals("float"))
        		System.out.println( evaluateAsFloat( expr));
        	else 
        		System.out.println( evaluateAsDouble( expr));
        } catch ( Exception e ) {
            System.out.println( "Syntax or numeric range error!" );
            e.printStackTrace();
        }
    }
	
	public static int evaluateAsInteger( String expr ) {
		Number n = evaluate(expr,new Integer(0));
		return n.intValue();
	}

	public static long evaluateAsLong( String expr ) {
		Number n = evaluate(expr,new Long(0));
		return n.longValue();
	}
	
	public static float evaluateAsFloat( String expr ) {
		Number n = evaluate(expr,new Float(0));
		return n.floatValue();
	}
	
	public static double evaluateAsDouble( String expr ) {
		Number n = evaluate(expr,new Double(0));
		return n.doubleValue();
	}
	 
	/**
	 * 计算表达式，如果表达式不正确，抛出异常
	 *@exception NumberFormatException 表达式中包含非数字字符
	 *@exception IllegalArgumentException 表达式中包含不支持的运算符
	 */ 
    public static Number evaluate( String expr , Number n) {
        List<String> tokens = tokenize( expr );
        // Recursively eliminate all operators, in descending order of priority,
        // till a numeric value is obtained from expr or till error is detected.

        // first of all, get rid of all parentheses
        int open  = tokens.indexOf( "(" ),
            close = tokens.lastIndexOf( ")" );
        if ( open != -1 )   // parentheses mismatch will be caught by subList( )
            return evaluate( join( tokens.subList( 0, open ) ) +
                             evaluate( join( tokens.subList( open + 1, close ) ),n) +
                             join( tokens.subList( close + 1, tokens.size( ) ) ) ,n);

        // now, get rid of binaries
        String[ ][ ] binaries = { { "+", "-" }, // priority-0 is the lowest
                                  { "*", "/" },
                                  { "^" } };    // only one R2L (right-to-left)
        List<String> listOfR2L = Arrays.asList( "^" );
        for ( int priority = binaries.length - 1; priority >= 0; priority-- ) {
            List binariesOfCurrentPriority = Arrays.asList( binaries[ priority ] );
            int pos = Algorithms.findFirstOf( binariesOfCurrentPriority, tokens );
            if ( listOfR2L.contains( binariesOfCurrentPriority.get( 0 ) ) )
              pos = Algorithms.findLastOf( binariesOfCurrentPriority, tokens );
            if ( pos != -1 )
                return evaluate( join( tokens.subList( 0, pos - 1 ) ) +
                                 operate( tokens.get( pos ),
                                          tokens.get( pos - 1 ),
                                          tokens.get( pos + 1 ) ,n) +
                                 join( tokens.subList( pos + 2, tokens.size( ) ) ) ,n);
        }

        // at this point, expr should be a numeric value convertible to double
        if(n instanceof Byte || n instanceof Short || n instanceof Integer || n instanceof Long){
        	try{
        		return Long.parseLong(tokens.get( 0 ));
    		}catch(NumberFormatException e){
    		}
        }
        return Double.parseDouble( tokens.get( 0 ) );
    }

    public static Number operate( String operator, String a, String b,Number n ) {
    	if(n instanceof Byte || n instanceof Short || n instanceof Integer || n instanceof Long){
    		long x = 0,y=0;
    		try{
	    		x = Long.parseLong(a);
	    		y = Long.parseLong(b);
	            if ( operator.equals( "+" ) ) return x + y;
	            if ( operator.equals( "-" ) ) return x - y;
	            if ( operator.equals( "*" ) ) return x * y;
	            if ( operator.equals( "/" ) ) return x / y;
	            if ( operator.equals( "^" ) ) return Math.pow( x, y );
	            throw new IllegalArgumentException( "Unknown operator: " + operator );
    		}catch(NumberFormatException e){
    			
    		}
    	}
        double x = Double.parseDouble( a ), y = Double.parseDouble( b );
        if ( operator.equals( "+" ) ) return x + y;
        if ( operator.equals( "-" ) ) return x - y;
        if ( operator.equals( "*" ) ) return x * y;
        if ( operator.equals( "/" ) ) return x / y;
        if ( operator.equals( "^" ) ) return Math.pow( x, y );
        throw new IllegalArgumentException( "Unknown operator: " + operator );
    }

    public static boolean isArithmeticOperator( String s ) {
        return s.length( ) == 1 && "+-*/^".indexOf( s ) != -1;
    }

    public static List<String> tokenize( String expr ) {
        // split by (while "capturing" all) operators by positive look around
        final String pattern = "(?=[-+*/^()])|(?<=[-+*/^()])";
        List<String> tokens =
                new ArrayList<String>( Arrays.asList( expr.split( pattern ) ) );

        // Trim all tokens, discard empty ones, and join sign to number.
        for ( int i = 0; i < tokens.size( ); i++ ) {
            String current  = tokens.get( i ).trim( ),
                   previous = i > 0 ? tokens.get( i - 1 ) : "+";
            if ( current.length( ) == 0 )
                tokens.remove( i-- );   // decrement, or miss the next token
            else
                if ( isArithmeticOperator( current ) &&
                     isArithmeticOperator( previous ) &&
                     i + 1 < tokens.size( ) ) {
                    tokens.set( i + 1, current + tokens.get( i + 1 ).trim( ) );
                    tokens.remove( i ); // no decrement, to skip the next token
                } else
                    tokens.set( i, current );
        }

        return tokens;
    }

    private static String join( List<String> strings ) {
        return strings.toString( ).replaceAll( "[,\\[\\]]", "" );
    }
}

// see C++'s STL algorithm find_first_of( ) and find_last_of( ) for semantics
class Algorithms {
    public static int findFirstOf( List what, List in ) {
        List<Integer> locations = new ArrayList<Integer>( );
        for ( Object o: what )
            if ( in.contains( o ) )
                locations.add( in.indexOf( o ) );
        return locations.isEmpty( ) ? -1 : Collections.min( locations );
    }

    public static int findLastOf( List what, List in ) {
        List<Integer> locations = new ArrayList<Integer>( );
        for ( Object o: what )
            if ( in.contains( o ) )
                locations.add( in.lastIndexOf( o ) );
        return locations.isEmpty( ) ? -1 : Collections.max( locations );
    }
}
  
