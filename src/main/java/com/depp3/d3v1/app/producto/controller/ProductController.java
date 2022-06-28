package com.depp3.d3v1.app.producto.controller;

import com.depp3.d3v1.app.producto.data.dto.ProductDTO;
import com.depp3.d3v1.app.producto.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService service;

    public ProductController(final ProductService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtuvo todos los productos",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Error al obtener los productos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "[]",
                    content = @Content)
    })
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(service.findAllProducts());
    }

    @Operation(summary = "Obtener producto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtuvo producto por id",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Error al obtener los producto",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "El producto no existe",
                    content = @Content)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @Operation(summary = "Crear un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Creo un producto con exito",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Error al crear el producto",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "El al crear el producto",
                    content = @Content)
    })
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<ProductDTO> saveProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(service.saveProduct(productDTO));
    }

    @Operation(summary = "Modificar un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto modificado con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Request invalida",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No pudo actualizar el producto",
                    content = @Content)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(service.updateProduct(id, productDTO));
    }

    @Operation(summary = "Eliminar un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Request invalida",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No pudo eliminar el producto",
                    content = @Content)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.deleteProduct(id));
    }
}

