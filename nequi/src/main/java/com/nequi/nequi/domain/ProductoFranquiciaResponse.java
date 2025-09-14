package com.nequi.nequi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductoFranquiciaResponse {
    private String nombreProducto;
    private int stock;
    private String nombreSucursal;
}
