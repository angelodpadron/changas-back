package com.changas.exceptions;

public class HiringOwnChangaException extends Exception {
    public HiringOwnChangaException() {
        super("Cannot hire an own service");
    }
}
