package com.depp3.productos.generals.exceptions.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class ErrorMessage {

    private String message;
    private String code;
    private String path;

}
