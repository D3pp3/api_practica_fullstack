package com.depp3.productos.app.producto.service;

import com.depp3.productos.app.producto.data.domain.Product;
import com.depp3.productos.app.producto.data.dto.ProductDTO;
import com.depp3.productos.app.producto.data.mapper.ProductMapper;
import com.depp3.productos.app.producto.data.repository.ProductRepository;
import com.depp3.productos.app.producto.exception.ProductException;
import com.depp3.productos.app.producto.type.ProductMessageError;
import com.depp3.productos.generals.type.InternalErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    private ProductService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new ProductService(productRepository);
    }

    @Test
    @DisplayName("Verifica que se arroje productException cuando el nombre del producto es null")
    public void shouldThrowProductExceptionWhenNameIsNull() {
        // given
        ProductDTO productDTO = new ProductDTO(
                null,
                "Harina de trigo x 1kg",
                84.2d
        );

        // when
        ProductException productException = assertThrows(ProductException.class,
                () -> underTest.saveProduct(productDTO));

        // then
        assertEquals(String.format(InternalErrorMessage.ERROR_NAME_EMPTY.getDescription(), "producto"),
                productException.getShowableMessage());

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Verifica que se arroje productException cuando el nombre del producto es vacio")
    public void shouldThrowProductExceptionWhenNameIsEmpty() {
        // given
        ProductDTO productDTO = new ProductDTO(
                "",
                "Harina de trigo x 1kg",
                84.2d
        );

        // when
        ProductException productException = assertThrows(ProductException.class,
                () -> underTest.saveProduct(productDTO));

        // then
        assertEquals(String.format(InternalErrorMessage.ERROR_NAME_EMPTY.getDescription(), ProductValidations.PRODUCT),
                productException.getShowableMessage());

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Verifica que se arroje productException cuando el precio del producto es negativo")
    public void shouldThrowProductExceptionWhenPriceIsNegative() {
        // given
        ProductDTO productDTO = new ProductDTO(
                "Harina",
                "Harina de trigo x 1kg",
                -84.2d
        );

        // when
        ProductException productException = assertThrows(ProductException.class,
                () -> underTest.saveProduct(productDTO));

        // then
        assertEquals(String.format(InternalErrorMessage.ERROR_PRICE_NEGATIVE.getDescription(), ProductValidations.PRODUCT),
                productException.getShowableMessage());

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Verifica que se arroje productException cuando el nombre del producto existe")
    public void shouldThrowProductExceptionWhenNameProductIsTaken() {
        // given
        ProductDTO productDTO = new ProductDTO(
                "Harina",
                "Harina de trigo x 1kg",
                84.2d
        );

        given(productRepository.existsByName(anyString()))
                .willReturn(true);

        // when
        ProductException productException = assertThrows(ProductException.class,
                () -> underTest.saveProduct(productDTO));

        // then
        assertEquals(ProductMessageError.PRODUCT_ALREADY_EXIST.getDescription(),
                productException.getShowableMessage());

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Verifica que se guarde correctamente el producto")
    public void shouldSaveProduct() {
        // given
        ProductDTO productDTO = new ProductDTO(
                "Harina",
                "Harina de trigo x 1kg",
                84.2d
        );
        given(productRepository.save(any(Product.class)))
                .willReturn(ProductMapper.getInstance().productDTOToProduct(productDTO));

        // when
        ProductDTO save = underTest.saveProduct(productDTO);

        // then

        assertEquals(productDTO.getName(), save.getName());
        assertEquals(productDTO.getDescription(), save.getDescription());
        assertEquals(productDTO.getPrice(), save.getPrice());

        verify(productRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Verifica que cuando no exista el producto x id arroje ProductException")
    public void shouldThrowProductExceptionWhenIdDontExist() {
        // given
        given(productRepository.existsById(anyLong()))
                .willReturn(false);

        // when
        ProductException productException = assertThrows(ProductException.class,
                () -> underTest.getProductById(anyLong()));

        // then
        assertEquals(ProductMessageError.PRODUCT_NOT_EXIST.getDescription(),
                productException.getShowableMessage());

        verify(productRepository, never()).getReferenceById(any());
    }

    @Test
    @DisplayName("Verifica que cuando exista el producto x id retorne un productoDTO")
    public void shouldReturnProductDTO() {
        // given
        Product product = new Product(
                1l,
                "Harina",
                "Harina de trigo x 1kg",
                84.2d
        );
        given(productRepository.existsById(anyLong()))
                .willReturn(true);

        given(productRepository.getReferenceById(1l))
                .willReturn(product);

        // when
        ProductDTO productDTO = underTest.getProductById(1l);

        // then
        verify(productRepository, atMostOnce()).getReferenceById(any());
    }
}