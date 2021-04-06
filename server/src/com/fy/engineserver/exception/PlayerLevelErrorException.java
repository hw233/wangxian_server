package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 宠物孵化，玩家等级必须大于等于宠物的携带等级，否则抛出此异常
 * 
 *
 */
public class PlayerLevelErrorException extends Exception {

    private Throwable nestedThrowable = null;

    public PlayerLevelErrorException() {
        super();
    }

    public PlayerLevelErrorException(String msg) {
        super(msg);
    }

    public PlayerLevelErrorException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public PlayerLevelErrorException(String msg, Throwable nestedThrowable) {
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

