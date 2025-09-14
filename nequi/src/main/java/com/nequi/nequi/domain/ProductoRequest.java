package com.nequi.nequi.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRequest {
    private String nombre;
    private int stock;
    private String sucursal;
}
