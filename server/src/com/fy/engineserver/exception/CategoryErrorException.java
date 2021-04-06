package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物繁殖必须是同一物种和种类，否则抛出此异常
 * 
 *
 */
public class CategoryErrorException extends Exception {

    private Throwable nestedThrowable = null;

    public CategoryErrorException() {
        super();
    }

    public CategoryErrorException(String msg) {
        super(msg);
    }

    public CategoryErrorException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public CategoryErrorException(String msg, Throwable nestedThrowable) {
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

