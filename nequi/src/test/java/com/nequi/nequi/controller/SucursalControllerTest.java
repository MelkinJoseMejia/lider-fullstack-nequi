package com.nequi.nequi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nequi.nequi.domain.DeleteProductoRequest;
import com.nequi.nequi.domain.ProductoRequest;
import com.nequi.nequi.domain.Sucursal;
import com.nequi.nequi.service.IProductoService;
import com.nequi.nequi.service.ISucursalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISucursalService sucursalService;

    @MockBean
    private IProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoRequest productoRequest;
    private DeleteProductoRequest deleteProductoRequest;
    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        productoRequest = new ProductoRequest();
        productoRequest.setNombre("Producto A");
        productoRequest.setSucursal("Sucursal A");
        productoRequest.setStock(10);

        deleteProductoRequest = new DeleteProductoRequest();
        deleteProductoRequest.setNombreProducto("Producto A");
        deleteProductoRequest.setNombreSucursal("Sucursal A");

        sucursal = new Sucursal();
        sucursal.setId(1);
        sucursal.setNombre("Sucursal A");
    }

    @Test
    void addProducto_ok() throws Exception {
        Mockito.when(sucursalService.addProduct(any(ProductoRequest.class)))
                .thenReturn(sucursal);

        mockMvc.perform(post("/v1/sucursal/addProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal A"));
    }

    @Test
    void deleteProducto_ok() throws Exception {
        Mockito.when(sucursalService.deleteProduct(any(DeleteProductoRequest.class)))
                .thenReturn(sucursal);

        mockMvc.perform(delete("/v1/sucursal/deleteProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteProductoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal A"));
    }

    @Test
    void updateSucursal_ok() throws Exception {
        Sucursal actualizado = new Sucursal();
        actualizado.setId(1);
        actualizado.setNombre("Sucursal Actualizada");

        Mockito.when(sucursalService.updateSucursal(eq("Sucursal A"), any(Sucursal.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/v1/sucursal/updateSucursal/Sucursal A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal Actualizada"));
    }

}
