package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物不存在抛出此异常
 * 
 *
 */
public class PetNotFoundException extends Exception {

    private Throwable nestedThrowable = null;

    public PetNotFoundException() {
        super();
    }

    public PetNotFoundException(String msg) {
        super(msg);
    }

    public PetNotFoundException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public PetNotFoundException(String msg, Throwable nestedThrowable) {
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

