package com.changas.exceptions.review;

public class AlreadyReviewedException extends ReviewException{

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     */
    public AlreadyReviewedException() {
        super("Changa already reviewed");
    }
}
