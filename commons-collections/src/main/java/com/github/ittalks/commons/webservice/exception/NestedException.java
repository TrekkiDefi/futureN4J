package com.github.ittalks.commons.webservice.exception;

/**
 * Created by 刘春龙 on 2017/4/29.
 */
public class NestedException extends RuntimeException {

    private static final long serialVersionUID = 5893258079497055346L;

    private Throwable throwable;

    private NestedException(Throwable t) {
        this.throwable = t;
    }

    public static RuntimeException wrap(Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        return new NestedException(t);
    }

    @Override
    public Throwable getCause() {
        return this.throwable;
    }

    @Override
    public void printStackTrace() {
        this.throwable.printStackTrace();
    }
}
