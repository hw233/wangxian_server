package com.fy.boss.authorize.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class UserNameInValidException extends Exception {

    private Throwable nestedThrowable = null;

    public UserNameInValidException() {
        super();
    }

    public UserNameInValidException(String msg) {
        super(msg);
    }

    public UserNameInValidException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public UserNameInValidException(String msg, Throwable nestedThrowable) {
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

