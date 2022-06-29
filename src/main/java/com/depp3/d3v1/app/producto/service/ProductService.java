package com.depp3.d3v1.app.producto.service;

import com.depp3.d3v1.app.producto.data.domain.Product;
import com.depp3.d3v1.app.producto.data.dto.ProductDTO;
import com.depp3.d3v1.app.producto.data.mapper.ProductMapper;
import com.depp3.d3v1.app.producto.data.repository.ProductRepository;
import com.depp3.d3v1.app.producto.type.ProductMessageError;
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
        ProductValidations.getByIdValidation(repository, id);

        return ProductMapper.getInstance().productToProductDTO(repository.getReferenceById(id));
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        ProductValidations.saveValidation(repository, productDTO);

        Product product = ProductMapper.getInstance().productDTOToProduct(productDTO);
        return ProductMapper.getInstance().productToProductDTO(repository.save(product));
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        ProductValidations.modifyValidation(repository, productDTO, id);

        Product modified = repository.getReferenceById(id);
        modified.setDescription(productDTO.getDescription());
        modified.setName(productDTO.getName());
        modified.setPrice(productDTO.getPrice());

        return ProductMapper.getInstance().productToProductDTO(repository.save(modified));
    }

    public String deleteProduct(Long id) {
        ProductValidations.deleteValidation(repository, id);

        String name = repository.findById(id).get().getName();
        repository.deleteById(id);
        return String.format(ProductMessageError.PRODUCT_DELETE_SUCCESS.getDescription(), name);
    }
}
