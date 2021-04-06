package com.fy.engineserver.guozhan.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 物种找不到，抛出此异常
 * 
 *
 */
public class CountryCanNotBeDeclareWarException extends Exception {

    private Throwable nestedThrowable = null;

    public CountryCanNotBeDeclareWarException() {
        super();
    }

    public CountryCanNotBeDeclareWarException(String msg) {
        super(msg);
    }

    public CountryCanNotBeDeclareWarException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public CountryCanNotBeDeclareWarException(String msg, Throwable nestedThrowable) {
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

