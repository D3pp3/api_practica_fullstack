package com.depp3.productos.app.producto.data.mapper;

import com.depp3.productos.app.producto.data.domain.Product;
import com.depp3.productos.app.producto.data.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class ProductMapper {

    public static ProductMapper instance = null;

    private ProductMapper() {}

    public static ProductMapper getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (ProductMapper.class) {
                if (Objects.isNull(instance)) {
                    instance = new ProductMapper();
                }
            }
        }
        return instance;
    }

    public ProductDTO productToProductDTO(Product product) {
        return Optional.ofNullable(product)
                .map(prod -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice()
                ))
                .get();
    }

    public Product productDTOToProduct(ProductDTO productDTO) {
        return Optional.ofNullable(productDTO)
                .map(prodDTO -> new Product(
                        productDTO.getName(),
                        productDTO.getDescription(),
                        productDTO.getPrice()
                ))
                .get();
    }
}
