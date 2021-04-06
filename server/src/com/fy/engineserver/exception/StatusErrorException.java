package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物状态不能为出战状态，否则抛出此异常
 * 
 *
 */
public class StatusErrorException extends Exception {

    private Throwable nestedThrowable = null;

    public StatusErrorException() {
        super();
    }

    public StatusErrorException(String msg) {
        super(msg);
    }

    public StatusErrorException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public StatusErrorException(String msg, Throwable nestedThrowable) {
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

