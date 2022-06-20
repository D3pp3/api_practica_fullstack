package com.depp3.productos.app.producto;

import com.depp3.productos.app.producto.data.dto.ProductDTO;
import com.depp3.productos.app.producto.data.repository.ProductRepository;
import com.depp3.productos.app.producto.exception.ProductException;
import com.depp3.productos.app.producto.type.ProductMessageError;
import com.depp3.productos.generals.Type.InternalErrorMessage;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public class ProductValidations {

    private static final String PRODUCT = "producto";

    public static void getByIdValidation(ProductRepository repository, Long id) {
        existById(repository, id);
    }

    public static void saveValidation(ProductRepository repository, ProductDTO productDTO) {

        nameValidation(productDTO.getName());

        priceValidation(productDTO.getPrice());

        existNameProduct(repository, productDTO.getName());
    }

    public static void modifyValidation(ProductRepository repository, ProductDTO productDTO, Long id) {

        existById(repository, id);

        nameValidation(productDTO.getName());

        priceValidation(productDTO.getPrice());

        existNameAndNotIsThis(repository, productDTO.getName(), id);
    }

    public static void deleteValidation(ProductRepository repository, Long id) {
        existById(repository, id);
    }

    private static void existById(ProductRepository repository, Long id) {
        if (!repository.existsById(id)) {
            throw new ProductException(HttpStatus.BAD_REQUEST,
                    ProductMessageError.PRODUCT_NOT_EXIST.getCode(),
                    ProductMessageError.PRODUCT_GET_BY_ID_METHOD.getDescription(),
                    ProductMessageError.PRODUCT_NOT_EXIST.getDescription());
        }
    }

    private static void nameValidation(String name) {
        if (Objects.isNull(name) || name.equals("")) {
            throw new ProductException(HttpStatus.BAD_REQUEST,
                    InternalErrorMessage.ERROR_NAME_EMPTY.getCode(),
                    ProductMessageError.PRODUCT_SAVE_PRODUCT.getDescription(),
                    String.format(InternalErrorMessage.ERROR_NAME_EMPTY.getDescription(), PRODUCT));
        }
    }

    private static void priceValidation(Double price) {
        if (Objects.isNull(price) || price < 0) {
            throw new ProductException(HttpStatus.BAD_REQUEST,
                    InternalErrorMessage.ERROR_PRICE_NEGATIVE.getCode(),
                    ProductMessageError.PRODUCT_SAVE_PRODUCT.getDescription(),
                    String.format(InternalErrorMessage.ERROR_PRICE_NEGATIVE.getDescription(), PRODUCT));
        }
    }

    private static void existNameProduct(ProductRepository repository, String name) {
        if (repository.existsByName(name)) {
            throw new ProductException(HttpStatus.BAD_REQUEST,
                    ProductMessageError.PRODUCT_ALREADY_EXIST.getCode(),
                    ProductMessageError.PRODUCT_SAVE_PRODUCT.getDescription(),
                    ProductMessageError.PRODUCT_ALREADY_EXIST.getDescription()
            );
        }

    }

    private static void existNameAndNotIsThis(ProductRepository repository, String name, Long id) {
        if (repository.existsByName(name) &&
            repository.findByName(name).get().getId() != id) {
            throw new ProductException(HttpStatus.BAD_REQUEST,
                    ProductMessageError.PRODUCT_ALREADY_EXIST_WITH_THIS_NAME.getCode(),
                    ProductMessageError.PRODUCT_MODIFY.getDescription(),
                    ProductMessageError.PRODUCT_ALREADY_EXIST_WITH_THIS_NAME.getDescription());
        }
    }
}
