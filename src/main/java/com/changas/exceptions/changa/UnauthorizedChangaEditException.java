package com.changas.exceptions.changa;

public class UnauthorizedChangaEditException extends Exception{
    public UnauthorizedChangaEditException() {
        super("Cannot edit a service of another customer");
    }
}
