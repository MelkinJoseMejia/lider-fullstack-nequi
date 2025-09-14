package com.nequi.nequi.controller;

import com.nequi.nequi.domain.*;
import com.nequi.nequi.exception.DataErrorException;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.service.IProductoService;
import com.nequi.nequi.service.ISucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sucursal")
public class SucursalController {

    @Autowired
    ISucursalService sucursalService;

    @Autowired
    IProductoService productoService;

    /**
     * Método que permite agregar un Producto a una Sucursal
     * @param productoRequest
     * @return
     * @throws ResourceNotFoundException
     */
    @PostMapping("/addProduct")
    public Sucursal addProducto(@RequestBody ProductoRequest productoRequest) throws ResourceNotFoundException, DataErrorException {
        if(productoRequest.getNombre().isBlank() || productoRequest.getSucursal() == null || productoRequest.getStock() == 0){
            throw new DataErrorException("Error en parámetros");
        }
        return sucursalService.addProduct(productoRequest);
    }

    /**
     * Método que permite borrar un Producto de una Sucursal
     * @param deleteProductoRequest
     * @return
     * @throws ResourceNotFoundException
     */
    @DeleteMapping("/deleteProduct")
    public Sucursal deleteProducto(@RequestBody DeleteProductoRequest deleteProductoRequest) throws ResourceNotFoundException, DataErrorException {
        if(deleteProductoRequest.getNombreProducto().isBlank() || deleteProductoRequest.getNombreSucursal().isBlank()){
            throw new DataErrorException("Error en parámetros");
        }
        return sucursalService.deleteProduct(deleteProductoRequest);
    }

    /**
     * Método que permite actualizar el nombre de una Sucursal
     * @param nombreSucursal
     * @param sucursal
     * @return
     * @throws ResourceNotFoundException
     */
    @PutMapping("/updateSucursal/{nombreSucursal}")
    public ResponseEntity<Sucursal> updateFranquicia(
            @PathVariable(value = "nombreSucursal") String nombreSucursal, @RequestBody Sucursal sucursal) throws ResourceNotFoundException, DataErrorException {
        if(nombreSucursal.isBlank() || sucursal == null){
            throw new DataErrorException("Error en parámetros");
        }
        Sucursal updatedSucursal = sucursalService.updateSucursal(nombreSucursal, sucursal);
        return ResponseEntity.ok(updatedSucursal);

    }
}
