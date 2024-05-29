package com.changas.exceptions.review;

public class ReviewUnhiredChangaException extends ReviewException{

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     */
    public ReviewUnhiredChangaException() {
        super("Cannot rate a changa whose transaction has yet to be closed.");
    }
}
