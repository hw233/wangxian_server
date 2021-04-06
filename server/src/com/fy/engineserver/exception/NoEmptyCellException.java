package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物状态不能为出战状态，否则抛出此异常
 * 
 *
 */
public class NoEmptyCellException extends Exception {

    private Throwable nestedThrowable = null;

    public NoEmptyCellException() {
        super();
    }

    public NoEmptyCellException(String msg) {
        super(msg);
    }

    public NoEmptyCellException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public NoEmptyCellException(String msg, Throwable nestedThrowable) {
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

