package com.depp3.productos.app.producto.type;

public enum ProductMessageError {

    PRODUCT_GET_BY_ID_METHOD(1000, "Error en el metodo getById(Long id)"),
    PRODUCT_SAVE_PRODUCT(1001, "Error en el metodo saveProduct(ProductDTO productDTO)"),
    PRODUCT_MODIFY(1002, "Error en el metodo updateProduct(ProductDTO productDTO)"),
    PRODUCT_NOT_EXIST(1010, "El producto no existe"),
    PRODUCT_ALREADY_EXIST(1011, "El producto ya existe"),
    PRODUCT_ALREADY_EXIST_WITH_THIS_NAME(1012, "Existe otro producto con este nombre"),
    PRODUCT_DELETE_SUCCESS(1013, "El producto %s se elimino con exito");

    private final Integer code;
    private final String description;

    ProductMessageError(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() { return code; }

    public String getDescription() { return description; }
}
