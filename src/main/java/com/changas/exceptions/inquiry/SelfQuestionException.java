package com.changas.exceptions.inquiry;

public class SelfQuestionException extends InquiryException {
    public SelfQuestionException() {
        super("Providers cannot ask inquiry on their own listings.");
    }
}
