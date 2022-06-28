package com.depp3.d3v1.generals.exceptions;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException{

    private HttpStatus status;
    private Integer code;
    private String showableMessage;

    public GenericException(HttpStatus status, Integer code, String internalMessage, String showableMessage) {
        super(internalMessage);
        this.status = status;
        this.code = code;
        this.showableMessage = showableMessage;
    }

    public HttpStatus getStatus() { return status; }

    public Integer getCode() { return code; }

    public String getShowableMessage() { return showableMessage; }
}
