package com.nequi.nequi.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteProductoRequest {
    private String nombreProducto;
    private String nombreSucursal;
}
