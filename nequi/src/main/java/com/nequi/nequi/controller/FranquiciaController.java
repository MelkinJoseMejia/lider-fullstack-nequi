package com.nequi.nequi.controller;

import com.nequi.nequi.domain.Franquicia;
import com.nequi.nequi.domain.ProductoFranquiciaResponse;
import com.nequi.nequi.domain.SucursalRequest;
import com.nequi.nequi.exception.DataErrorException;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.service.IFranquiciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/franquicia")
public class FranquiciaController {

    @Autowired
    IFranquiciaService franquiciaService;

    /**
     * Método que permite agregar una franquicia
     * @param franquicia
     * @return franquicia agregada
     */
    @PostMapping("/addFranquicia")
    public Franquicia addFranquicia(@RequestBody Franquicia franquicia) throws DataErrorException, ResourceNotFoundException {
        if(franquicia == null || franquicia.getNombre().isBlank() || franquicia.getId() == 0){
            throw new DataErrorException("Error en parámetros");
        }
        franquiciaService.addFranquicia(franquicia);
        return franquicia;
    }

    /**
     * Método que permite agregar una Sucursal a una Franquicia
     * @param sucursalRequest
     * @return Franquicia con Sucursales
     * @throws ResourceNotFoundException
     */
    @PostMapping("/addSucursal")
    public Franquicia addSucursal(@RequestBody SucursalRequest sucursalRequest) throws ResourceNotFoundException, DataErrorException {
        if(sucursalRequest.getFranquicia().isBlank() || sucursalRequest.getNombre().isBlank()){
            throw new DataErrorException("Error en parámetros");
        }
        return franquiciaService.addSucursal(sucursalRequest);
    }

    /**
     * Método que consulta los productos con más stock de una franquicia
     * @param nombreFranquicia
     * @return Listado de productos con stocke y sucursales
     * @throws ResourceNotFoundException
     */
    @GetMapping("/getProductStock/{nombreFranquicia}")
    public List<ProductoFranquiciaResponse> getProductosFranquicia(@PathVariable(value = "nombreFranquicia") String nombreFranquicia) throws ResourceNotFoundException, DataErrorException {
        if(nombreFranquicia.isBlank()){
            throw new DataErrorException("Error en parámetros");
        }
        return franquiciaService.getProductosFranquicia(nombreFranquicia);
    }

    /**
     * Método que permite actualizar el nombre de una franquicia
     * @param nombreFranquicia
     * @param franquicia
     * @return
     * @throws ResourceNotFoundException
     */
    @PutMapping("/updateFranquicia/{nombreFranquicia}")
    public ResponseEntity<Franquicia> updateFranquicia(
            @PathVariable(value = "nombreFranquicia") String nombreFranquicia, @RequestBody Franquicia franquicia) throws ResourceNotFoundException, DataErrorException {
        if(nombreFranquicia.isBlank() || franquicia == null){
            throw new DataErrorException("Error en parámetros");
        }
        Franquicia updatedFranquicia = franquiciaService.updateFranquicia(nombreFranquicia, franquicia);
        return ResponseEntity.ok(updatedFranquicia);

    }
}
