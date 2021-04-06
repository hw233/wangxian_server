package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 合成的根骨丹必须要相同级别，否则抛出此异常
 * 
 *
 */
public class GenguDanLevelErrorException extends Exception {

    private Throwable nestedThrowable = null;

    public GenguDanLevelErrorException() {
        super();
    }

    public GenguDanLevelErrorException(String msg) {
        super(msg);
    }

    public GenguDanLevelErrorException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public GenguDanLevelErrorException(String msg, Throwable nestedThrowable) {
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

