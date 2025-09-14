package com.nequi.nequi.service;

import com.nequi.nequi.domain.Producto;
import com.nequi.nequi.domain.ProductoRequest;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.model.ProductoEntity;

import java.util.List;

public interface IProductoService {

    List<Producto> getAllProductos();

    ProductoEntity addProducto(ProductoRequest productoRequest) throws ResourceNotFoundException;

    Producto updateProduct(String nombreProducto, Producto producto) throws ResourceNotFoundException;

    Producto updateNombreProduct(String nombreProducto, Producto producto) throws ResourceNotFoundException;
}
