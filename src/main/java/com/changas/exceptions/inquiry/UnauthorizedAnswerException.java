package com.changas.exceptions.inquiry;

public class UnauthorizedAnswerException extends InquiryException {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public UnauthorizedAnswerException() {
        super("Only provider can respond to a question about the service provided");
    }
}
