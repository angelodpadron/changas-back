package com.changas.exceptions.inquiry;

public class QuestionAlreadyAnsweredException extends InquiryException {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public QuestionAlreadyAnsweredException() {
        super("The question about the service is already answered");
    }
}
