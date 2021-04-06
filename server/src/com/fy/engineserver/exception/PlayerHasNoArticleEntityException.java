package com.fy.engineserver.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 背包必须是有物品的，否则抛出此异常
 * 
 *
 */
public class PlayerHasNoArticleEntityException extends Exception {

    private Throwable nestedThrowable = null;

    public PlayerHasNoArticleEntityException() {
        super();
    }

    public PlayerHasNoArticleEntityException(String msg) {
        super(msg);
    }

    public PlayerHasNoArticleEntityException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public PlayerHasNoArticleEntityException(String msg, Throwable nestedThrowable) {
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

