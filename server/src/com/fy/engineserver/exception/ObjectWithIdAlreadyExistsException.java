package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 物种找不到，抛出此异常
 * 
 *
 */
public class ObjectWithIdAlreadyExistsException extends Exception {

    private Throwable nestedThrowable = null;

    public ObjectWithIdAlreadyExistsException() {
        super();
    }

    public ObjectWithIdAlreadyExistsException(String msg) {
        super(msg);
    }

    public ObjectWithIdAlreadyExistsException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public ObjectWithIdAlreadyExistsException(String msg, Throwable nestedThrowable) {
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

