package com.changas.exceptions.review;

public class ReviewNotFoundException extends ReviewException{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param changaId the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ReviewNotFoundException(Long changaId) {
        super("No review from customer for changa with id: " + changaId);
    }
}
