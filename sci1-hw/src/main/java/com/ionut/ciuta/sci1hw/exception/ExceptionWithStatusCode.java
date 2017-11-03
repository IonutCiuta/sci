package com.ionut.ciuta.sci1hw.exception;

import org.springframework.http.HttpStatus;

public abstract class ExceptionWithStatusCode extends RuntimeException {
    public HttpStatus status;
    public String text;

    public ExceptionWithStatusCode(HttpStatus status, String text) {
        this.status = status;
        this.text = text;
    }
}
