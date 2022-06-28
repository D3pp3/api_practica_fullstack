package com.depp3.d3v1.app.producto.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;

    public ProductDTO() {
        clear();
    }

    public ProductDTO(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    private void clear() {
        this.id = 0L;
        this.name = "";
        this.description = "";
        this.price = 0D;
    }
}
