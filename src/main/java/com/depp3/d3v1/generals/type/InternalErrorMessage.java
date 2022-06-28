package com.depp3.d3v1.generals.type;

public enum InternalErrorMessage {

    ERROR_NAME_EMPTY(900, "El nombre del %s no puede quedar vacio"),
    ERROR_PRICE_NEGATIVE(901, "El precio del %s debe contener un valor valido");

    private final Integer code;
    private final String description;

    InternalErrorMessage(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() { return code; }

    public String getDescription() { return description; }
}
