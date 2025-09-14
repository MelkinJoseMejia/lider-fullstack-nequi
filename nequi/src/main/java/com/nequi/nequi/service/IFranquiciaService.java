package com.nequi.nequi.service;

import com.nequi.nequi.domain.Franquicia;
import com.nequi.nequi.domain.ProductoFranquiciaResponse;
import com.nequi.nequi.domain.SucursalRequest;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.model.FranquiciaEntity;

import java.util.List;

public interface IFranquiciaService {

    FranquiciaEntity addFranquicia(Franquicia franquicia) throws ResourceNotFoundException;

    Franquicia addSucursal(SucursalRequest sucursalRequest) throws ResourceNotFoundException;

    List<ProductoFranquiciaResponse> getProductosFranquicia(String nombreFranquicia) throws ResourceNotFoundException;

    Franquicia updateFranquicia(String nombreFranquicia, Franquicia franquicia) throws ResourceNotFoundException;

}
