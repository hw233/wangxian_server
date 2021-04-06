package com.fy.engineserver.authority;

import java.io.PrintStream;
import java.io.PrintWriter;

public class MaxNumAccessedException extends Exception {

    private Throwable nestedThrowable = null;

    public MaxNumAccessedException() {
        super();
    }

    public MaxNumAccessedException(String msg) {
        super(msg);
    }

    public MaxNumAccessedException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public MaxNumAccessedException(String msg, Throwable nestedThrowable) {
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

