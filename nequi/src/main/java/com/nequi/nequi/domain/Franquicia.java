package com.nequi.nequi.domain;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Franquicia {
    private int id;
    private String nombre;
    private List<Sucursal> sucursales;
}
