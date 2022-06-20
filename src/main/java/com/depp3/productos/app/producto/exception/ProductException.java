package com.depp3.productos.app.producto.exception;

import com.depp3.productos.generals.exceptions.GenericException;
import org.springframework.http.HttpStatus;

public class ProductException extends GenericException {

    public ProductException(HttpStatus status, Integer code, String internalMessage, String showableMessage) {
        super(status, code, internalMessage, showableMessage);
    }
}
