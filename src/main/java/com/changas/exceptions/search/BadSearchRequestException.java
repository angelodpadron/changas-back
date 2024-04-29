package com.changas.exceptions.search;

public class BadSearchRequestException extends Exception{
    public BadSearchRequestException() {
        super("Provide valid search criteria");
    }
}
