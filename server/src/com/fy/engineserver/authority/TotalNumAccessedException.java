package com.fy.engineserver.authority;

import java.io.PrintStream;
import java.io.PrintWriter;

public class TotalNumAccessedException extends Exception {

    private Throwable nestedThrowable = null;

    public TotalNumAccessedException() {
        super();
    }

    public TotalNumAccessedException(String msg) {
        super(msg);
    }

    public TotalNumAccessedException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public TotalNumAccessedException(String msg, Throwable nestedThrowable) {
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

