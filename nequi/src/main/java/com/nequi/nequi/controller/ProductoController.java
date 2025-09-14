package com.nequi.nequi.controller;


import com.nequi.nequi.domain.Producto;
import com.nequi.nequi.domain.ProductoRequest;
import com.nequi.nequi.exception.DataErrorException;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.model.ProductoEntity;
import com.nequi.nequi.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/producto")
public class ProductoController {

    @Autowired
    IProductoService productoService;

    /**
     * Métodoq ue permite agrear un Producto
     * @param productoRequest
     * @return
     * @throws ResourceNotFoundException
     */
    @PostMapping("/addProduct")
    public ProductoEntity addProducto(@RequestBody ProductoRequest productoRequest) throws ResourceNotFoundException, DataErrorException {
        if(productoRequest.getNombre().isBlank() || productoRequest.getSucursal().isBlank() || productoRequest.getStock() == 0){
            throw new DataErrorException("Error en parámetros");
        }
        return productoService.addProducto(productoRequest);
    }

    /**
     * Método que permite actualizar el stock de un producto
     * @param nombreProducto
     * @param producto
     * @return
     * @throws ResourceNotFoundException
     */
    @PutMapping("/updateStock/{nombre}")
    public ResponseEntity<Producto> updateProducto(
            @PathVariable(value = "nombre") String nombreProducto, @RequestBody Producto producto) throws ResourceNotFoundException, DataErrorException {
        if(nombreProducto.isBlank() || producto == null){
            throw new DataErrorException("Error en parámetros");
        }
        Producto updatedProduct = productoService.updateProduct(nombreProducto, producto);
            return ResponseEntity.ok(updatedProduct);

    }

    /**
     * Método que permite actualizar el nombre de un Producto
     * @param nombreProducto
     * @param producto
     * @return
     * @throws ResourceNotFoundException
     */
    @PutMapping("/updateNombre/{nombreProducto}")
    public ResponseEntity<Producto> updateNombreProducto(
            @PathVariable(value = "nombreProducto") String nombreProducto, @RequestBody Producto producto) throws ResourceNotFoundException, DataErrorException {
        if(nombreProducto.isBlank() || producto == null){
            throw new DataErrorException("Error en parámetros");
        }
        Producto updatedProduct = productoService.updateNombreProduct(nombreProducto, producto);
        return ResponseEntity.ok(updatedProduct);

    }

    /**
     * Método que consulta los Productos
     * @return
     */
    @GetMapping("/getProducts")
    public List<Producto> getProductos(){
        return productoService.getAllProductos();
    }
}
