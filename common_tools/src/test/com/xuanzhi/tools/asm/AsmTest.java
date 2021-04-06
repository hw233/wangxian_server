package com.xuanzhi.tools.asm;

import java.io.*;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class AsmTest {

	public static void main(String args[]) throws Exception{
		
		File dir = new File("D:/TDdownload_chrome/objectdb-2.3.7_12/bin/objectdb/com/objectdb/o");
		File file = new File(dir,"CFG.class");
		
		//File dir = new File("D:/workspace/game/common_tools/bin/com/xuanzhi/tools/asm/");
		//File file = new File(dir,"T.class");
		
		FileInputStream input = new FileInputStream(file);
		
		ClassReader reader = new ClassReader(input);
		ClassWriter cw = new ClassWriter(0);
		ClassVisitor cv = new TraceFieldClassAdapter(cw);
		reader.accept(cv, 0);
		byte b[] = cw.toByteArray();
		input.close();
		
		file = new File(dir,file.getName()+".asm");
		FileOutputStream out = new FileOutputStream(file);
		out.write(b);
		out.close();
		
	}
	
	public static class TraceFieldClassAdapter extends ClassAdapter implements Opcodes{

		private String owner;
		
		public TraceFieldClassAdapter(final ClassVisitor arg0) {
			super(arg0);
		}
		
		 public void visit(
			        final int version,
			        final int access,
			        final String name,
			        final String signature,
			        final String superName,
			        final String[] interfaces)
		{
			  owner = name;
			  super.visit(version, access, name, signature, superName, interfaces);
		}
		 
		public FieldVisitor visitField(
			        final int access,
			        final String name,
			        final String desc,
			        final String signature,
			        final Object value)
			    {
			        FieldVisitor fv = super.visitField(access, name, desc, signature, value);
			        
			        System.out.println("visit field: name="+name+",desc="+desc+",value="+value+"");

			        return fv;
			    }

			    public MethodVisitor visitMethod(
			        final int access,
			        final String name,
			        final String desc,
			        final String signature,
			        final String[] exceptions)
			    {
			        MethodVisitor mv = cv.visitMethod(access,
			                name,
			                desc,
			                signature,
			                exceptions);
			        if(name.equals("M") && desc.equals("()Z")){
			        	
			        	Type t = Type.getType("Z");
			        	
			        	mv.visitInsn(t.getOpcode(ICONST_1));
			        	mv.visitInsn(t.getOpcode(IRETURN));
			        	mv.visitMaxs(1, 1);
			        	mv.visitEnd();
			        	
			        	return null;
//			        	return new TraceFieldCodeAdapter(mv, owner);
			        }else{
			        	return mv;
			        }
			    }
	}
	
	static class TraceFieldCodeAdapter extends MethodAdapter implements Opcodes {

	    private String owner;

	    public TraceFieldCodeAdapter(final MethodVisitor mv, final String owner) {
	        super(mv);
	        this.owner = owner;
	    }

	    public void visitCode() {
	    	super.visitCode();
	    	System.out.println(" ============ start visit H() method code :" + owner);
	    	
	        mv.visitVarInsn(ALOAD, 0);
	        mv.visitVarInsn(ALOAD, 0);
	        mv.visitFieldInsn(GETFIELD, owner, "l", "Z");
	        mv.visitInsn(3);
	        mv.visitInsn(4);
	        mv.visitFieldInsn(PUTFIELD, owner, "l", "Z");
	    }
	    
	    public void visitMaxs(int maxStack,
	               int maxLocals){
	    	super.visitMaxs(maxStack, maxLocals);
	    	
	    	System.out.println(" ============= start visit H90 code : visitMaxs, maxStack = " + maxStack +",maxLocals="+maxLocals);
	    	
	    }
	    public void visitMethodInsn(int opcode,
                String owner,
                String name,
                String desc){
	    	super.visitMethodInsn(opcode, owner, name, desc);
	    	System.out.println(" ============= start visit H90 code : visitMethodInsn, opcode = " + opcode +",owner="+owner+",name="+name+",desc="+desc);
	    	
	    }
	    public void visitLdcInsn(Object o){
	    	super.visitLdcInsn(o);
	    	
	    	System.out.println(" ============= start visit H90 code : visitLdcInsn, cst = " + o);
	    }
	    
	    
	    public void visitTypeInsn(int opcode,
                String type){
	    	super.visitTypeInsn(opcode, type);
	    	
	    	System.out.println(" ============= start visit H90 code : visitTypeInsn, opcode = " + opcode + ", type = "+ type);
	    }
	    
	    public void visitIntInsn(int opcode,
                int operand){
	    	super.visitIntInsn(opcode, operand);
	    	System.out.println(" ============= start visit H90 code : visitIntInsn, opcode = " + opcode + ", operand = "+ operand);
	    }
	    public void visitFieldInsn(
	        final int opcode,
	        final String owner,
	        final String name,
	        final String desc)
	    {
	        super.visitFieldInsn(opcode, owner, name, desc);
	        System.out.println(" ============= start visit H90 code : visitFieldInsn, opcode = " + opcode +",owner="+owner+",name="+name+",desc="+desc);
	    }
	    
	    public void visitVarInsn(int opcode, int var) {
	    	super.visitVarInsn(opcode, var);
	    	System.out.println(" ============= start visit H90 code : visitVarInsn, opcode = " + opcode +", var = " + var );
	    }
	    
	    public void visitInsn(final int opcode){
	    	super.visitInsn(opcode);
	    	System.out.println(" ============= start visit H90 code : visitInsn, opcode = " + opcode );
	    }
	    
	    public void visitEnd() {
	    	super.visitEnd();
	    	System.out.println(" ============ end visit H() method code :" + owner);
	    	
	    }
	}
}
