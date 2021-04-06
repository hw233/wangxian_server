package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 1.宠物繁殖必须都是一代非变异宠，否则抛出此异常
 * 2.宠物合体时必须是同代宠物，否则爬出此异常
 * 
 *
 */
public class GenerationErrorException extends Exception {

    private Throwable nestedThrowable = null;

    public GenerationErrorException() {
        super();
    }

    public GenerationErrorException(String msg) {
        super(msg);
    }

    public GenerationErrorException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public GenerationErrorException(String msg, Throwable nestedThrowable) {
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

