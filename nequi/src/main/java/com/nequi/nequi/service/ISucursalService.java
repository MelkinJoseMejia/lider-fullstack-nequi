package com.nequi.nequi.service;

import com.nequi.nequi.domain.*;
import com.nequi.nequi.exception.ResourceNotFoundException;

public interface ISucursalService {


    Sucursal addProduct(ProductoRequest productoRequest) throws ResourceNotFoundException;

    Sucursal deleteProduct(DeleteProductoRequest deleteProductoRequest) throws ResourceNotFoundException;

    Sucursal updateSucursal(String nombreSucursal, Sucursal sucursal) throws ResourceNotFoundException;

}
