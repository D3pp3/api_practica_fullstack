package com.depp3.d3v1.app.producto.exception;

import com.depp3.d3v1.generals.exceptions.GenericException;
import org.springframework.http.HttpStatus;

public class ProductException extends GenericException {

    public ProductException(HttpStatus status, Integer code, String internalMessage, String showableMessage) {
        super(status, code, internalMessage, showableMessage);
    }
}
