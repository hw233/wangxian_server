package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物不存在抛出此异常
 * 
 *
 */
public class PetMatingNotFoundException extends Exception {

    private Throwable nestedThrowable = null;

    public PetMatingNotFoundException() {
        super();
    }

    public PetMatingNotFoundException(String msg) {
        super(msg);
    }

    public PetMatingNotFoundException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public PetMatingNotFoundException(String msg, Throwable nestedThrowable) {
        super(msg);
        this.nestedThrowable = nestedThrowable;
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (nestedThrowable != null) {
            nestedThrowable.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (nestedThrowable != null) {
            nestedThrowable.printStackTrace(ps);
        }
    }

    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (nestedThrowable != null) {
            nestedThrowable.printStackTrace(pw);
        }
    }
}

