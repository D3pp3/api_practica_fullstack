package com.depp3.productos.app.producto.service;

import com.depp3.productos.app.producto.data.domain.Product;
import com.depp3.productos.app.producto.data.dto.ProductDTO;
import com.depp3.productos.app.producto.data.mapper.ProductMapper;
import com.depp3.productos.app.producto.data.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(final ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductDTO> findAllProducts() {
        return repository.findAll().stream()
                .map(prod -> ProductMapper.getInstance().productToProductDTO(prod))
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        // TODO: crear exception cuando no existe el producto buscado
        return ProductMapper.getInstance().productToProductDTO(repository.getReferenceById(id));
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        // TODO: crear validaciones para guardar un producto correctamente

        Product product = ProductMapper.getInstance().productDTOToProduct(productDTO);
        ProductDTO savedProduct = ProductMapper.getInstance().productToProductDTO(repository.save(product));

        return savedProduct;
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        // TODO: crear validaciones para modificar un producto correctamente

        Product modified = repository.getReferenceById(id);
        modified.setDescription(productDTO.getDescription());
        modified.setName(productDTO.getName());
        modified.setPrice(productDTO.getPrice());
        ProductDTO modifyProduct = ProductMapper.getInstance().productToProductDTO(repository.save(modified));

        return modifyProduct;
    }

    public String deleteProduct(Long id) {
        // TODO: crear validaciones para borrar un producto correctamente

        String name = repository.findById(id).get().getName();
        repository.deleteById(id);
        return name;
    }
}
