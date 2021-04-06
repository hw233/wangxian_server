package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物需具有繁殖次数，每30级可进行一次繁殖，繁殖后繁殖次数-1，繁殖次数可进行累计，等于0则不可以在繁殖
 * 
 *
 */
public class BreedTimesErrorException extends Exception {

    private Throwable nestedThrowable = null;

    public BreedTimesErrorException() {
        super();
    }

    public BreedTimesErrorException(String msg) {
        super(msg);
    }

    public BreedTimesErrorException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public BreedTimesErrorException(String msg, Throwable nestedThrowable) {
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

