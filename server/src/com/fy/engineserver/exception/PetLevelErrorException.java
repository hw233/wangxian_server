package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物孵化，玩家等级必须大于等于宠物的携带等级，否则抛出此异常
 * 
 *
 */
public class PetLevelErrorException extends Exception {

    private Throwable nestedThrowable = null;

    public PetLevelErrorException() {
        super();
    }

    public PetLevelErrorException(String msg) {
        super(msg);
    }

    public PetLevelErrorException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public PetLevelErrorException(String msg, Throwable nestedThrowable) {
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

