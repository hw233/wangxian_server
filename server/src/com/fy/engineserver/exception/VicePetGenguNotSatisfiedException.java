package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物合体时副宠根骨必须大于等于主宠的品阶，否则抛出此异常
 * 
 *
 */
public class VicePetGenguNotSatisfiedException extends Exception {

    private Throwable nestedThrowable = null;

    public VicePetGenguNotSatisfiedException() {
        super();
    }

    public VicePetGenguNotSatisfiedException(String msg) {
        super(msg);
    }

    public VicePetGenguNotSatisfiedException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public VicePetGenguNotSatisfiedException(String msg, Throwable nestedThrowable) {
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

