package com.depp3.productos.generals.exceptions;

import com.depp3.productos.app.producto.exception.ProductException;
import com.depp3.productos.generals.exceptions.data.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionsHandler.class);

    @ExceptionHandler(ProductException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> productException(HttpServletRequest request, GenericException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorMessage(e.getShowableMessage(),
                                            e.getCode().toString(),
                                            request.getRequestURI()),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
