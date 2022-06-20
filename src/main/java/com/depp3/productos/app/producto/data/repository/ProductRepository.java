package com.depp3.productos.app.producto.data.repository;

import com.depp3.productos.app.producto.data.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
