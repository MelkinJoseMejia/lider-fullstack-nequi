package com.nequi.nequi.domain;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {
    private int id;
    private String nombre;
    private List<Producto> productos;
}
