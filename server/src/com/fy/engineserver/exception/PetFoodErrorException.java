package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物不存在抛出此异常
 * 
 *
 */
public class PetFoodErrorException extends Exception {

    private Throwable nestedThrowable = null;

    public PetFoodErrorException() {
        super();
    }

    public PetFoodErrorException(String msg) {
        super(msg);
    }

    public PetFoodErrorException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public PetFoodErrorException(String msg, Throwable nestedThrowable) {
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

