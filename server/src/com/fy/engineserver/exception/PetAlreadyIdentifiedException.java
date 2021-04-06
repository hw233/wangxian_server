package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物已经鉴定过了，否则抛出此异常
 * 
 *
 */
public class PetAlreadyIdentifiedException extends Exception {

    private Throwable nestedThrowable = null;

    public PetAlreadyIdentifiedException() {
        super();
    }

    public PetAlreadyIdentifiedException(String msg) {
        super(msg);
    }

    public PetAlreadyIdentifiedException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public PetAlreadyIdentifiedException(String msg, Throwable nestedThrowable) {
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

