package com.nequi.nequi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nequi.nequi.domain.Producto;
import com.nequi.nequi.domain.ProductoRequest;
import com.nequi.nequi.model.ProductoEntity;
import com.nequi.nequi.service.IProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoRequest productoRequest;
    private ProductoEntity productoEntity;
    private Producto producto;

    @BeforeEach
    void setUp() {
        productoRequest = new ProductoRequest();
        productoRequest.setNombre("Producto A");
        productoRequest.setSucursal("Sucursal A");
        productoRequest.setStock(50);

        productoEntity = new ProductoEntity();
        productoEntity.setId(1);
        productoEntity.setNombre("Producto A");
        productoEntity.setStock(50);

        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Producto A");
        producto.setStock(50);
    }

    @Test
    void addProducto_ok() throws Exception {
        Mockito.when(productoService.addProducto(any(ProductoRequest.class)))
                .thenReturn(productoEntity);

        mockMvc.perform(post("/v1/producto/addProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto A"))
                .andExpect(jsonPath("$.stock").value(50));
    }

    @Test
    void updateProducto_ok() throws Exception {
        Producto actualizado = new Producto();
        actualizado.setId(1);
        actualizado.setNombre("Producto A");
        actualizado.setStock(100);

        Mockito.when(productoService.updateProduct(eq("Producto A"), any(Producto.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/v1/producto/updateStock/Producto A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    void updateNombreProducto_ok() throws Exception {
        Producto actualizado = new Producto();
        actualizado.setId(1);
        actualizado.setNombre("NuevoNombre");
        actualizado.setStock(50);

        Mockito.when(productoService.updateNombreProduct(eq("Producto A"), any(Producto.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/v1/producto/updateNombre/Producto A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("NuevoNombre"));
    }

    @Test
    void getProductos_ok() throws Exception {
        Mockito.when(productoService.getAllProductos())
                .thenReturn(Collections.singletonList(producto));

        mockMvc.perform(get("/v1/producto/getProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Producto A"))
                .andExpect(jsonPath("$[0].stock").value(50));
    }
}
