package com.depp3.d3v1.app.producto.service;

import com.depp3.d3v1.app.producto.data.domain.Product;
import com.depp3.d3v1.app.producto.data.dto.ProductDTO;
import com.depp3.d3v1.app.producto.data.mapper.ProductMapper;
import com.depp3.d3v1.app.producto.data.repository.ProductRepository;
import com.depp3.d3v1.app.producto.exception.ProductException;
import com.depp3.d3v1.app.producto.type.ProductMessageError;
import com.depp3.d3v1.generals.type.InternalErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
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
        underTest.saveProduct(productDTO);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        // then
        verify(productRepository).save(productArgumentCaptor.capture());

        assertEquals(productDTO.getName(), productArgumentCaptor.getValue().getName());
        assertEquals(productDTO.getDescription(), productArgumentCaptor.getValue().getDescription());
        assertEquals(productDTO.getPrice(), productArgumentCaptor.getValue().getPrice());

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
                1L,
                "Harina",
                "Harina de trigo x 1kg",
                84.2d
        );
        given(productRepository.existsById(anyLong()))
                .willReturn(true);

        given(productRepository.getReferenceById(product.getId()))
                .willReturn(product);

        // when
        ProductDTO productDTO = underTest.getProductById(product.getId());
        ArgumentCaptor<Long> arg = ArgumentCaptor.forClass(Long.class);

        // then
        verify(productRepository).getReferenceById(arg.capture());

        assertEquals(arg.getValue(), productDTO.getId());
        verify(productRepository, atMostOnce()).getReferenceById(any());
    }

    @Test
    @DisplayName("Verifica que se obtengan todos los productos")
    public void shouldReturnListOfProducts() {
        // when
        underTest.findAllProducts();

        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Verifica que se modifique un producto correctamente")
    public void shouldModifyAProduct() {
        // given
        Long id = 1L;

        ProductDTO productDTO = new ProductDTO(
                "Harina",
                "Molienda de trigo x 1kg",
                87.4d
        );

        Product product = new Product(
                1L,
                "Harina",
                "Harina de trigo x 1kg",
                84.2D
        );

        given(productRepository.existsById(id))
                .willReturn(true);

        given(productRepository.getReferenceById(id))
                .willReturn(product);

        given(productRepository.save(any(Product.class)))
                .willReturn(ProductMapper.getInstance().productDTOToProduct(productDTO));

        // when

        underTest.updateProduct(id, productDTO);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        // then
        verify(productRepository).save(productArgumentCaptor.capture());

        assertEquals(productDTO.getName(), productArgumentCaptor.getValue().getName());
        assertEquals(productDTO.getDescription(), productArgumentCaptor.getValue().getDescription());
        assertEquals(productDTO.getPrice(), productArgumentCaptor.getValue().getPrice());

        verify(productRepository, atMostOnce()).existsById(anyLong());
        verify(productRepository, atMostOnce()).getReferenceById(any());
        verify(productRepository, atMostOnce()).save(any(Product.class));
    }

    @Test
    @DisplayName("Retorna ProductException cuando el producto existe y tienen distintos id")
    public void shouldThrowProductExceptionWhenThisNameExistAndIdIsNotEquals() {
        // given
        Long id = 1L;
        Product product = new Product(
                2L,
                "Harina",
                "Harina de trigo x 1kg",
                84.2D
        );

        ProductDTO productDTO = new ProductDTO(
                "Harina",
                "Molienda de trigo x 1kg",
                87.4d
        );

        Optional<Product> productO = Optional.of(product);

        given(productRepository.existsById(anyLong()))
                .willReturn(true);

        given(productRepository.existsByName(anyString()))
                .willReturn(true);

        given(productRepository.findByName(anyString()))
                .willReturn(productO);

        assertNotEquals(id,productO.get().getId());

        // when
        ProductException productException = assertThrows(ProductException.class,
                () -> underTest.updateProduct(id, productDTO));

        assertEquals(ProductMessageError.PRODUCT_ALREADY_EXIST_WITH_THIS_NAME.getDescription(),
                productException.getShowableMessage());

    }

    @Test
    @DisplayName("Retorna mensaje de borrado exitoso")
    public void shouldMessageSuccessDelete() {
        // given
        Long id = 1l;
        Product product = new Product(
                1L,
                "Harina",
                "Harina de trigo x 1kg",
                84.2D
        );
        Optional<Product> productO = Optional.of(product);

        given(productRepository.existsById(id))
                .willReturn(true);

        given(productRepository.findById(id))
                .willReturn(productO);

        // when
        underTest.deleteProduct(id);

        // then
        assertTrue(String.format(ProductMessageError.PRODUCT_DELETE_SUCCESS.getDescription(), product.getName())
                .contains(productO.get().getName()));

        verify(productRepository, atMostOnce()).existsById(anyLong());
        verify(productRepository).deleteById(id);

    }
}