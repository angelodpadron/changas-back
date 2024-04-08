package com.changas.exceptions;

public class ChangaNotFoundException extends Exception{
    public ChangaNotFoundException(Long changaId) {
        super("No changa found with id " + changaId);
    }
}
