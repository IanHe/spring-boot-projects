package com.hr.exception;

public class HrException extends RuntimeException {
    private static final long serialVersionUID = 88L;

    public HrException() {
    }

    public HrException(String message) {
        super(message);
    }
}
