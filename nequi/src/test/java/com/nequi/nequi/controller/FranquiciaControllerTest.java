package com.nequi.nequi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nequi.nequi.domain.Franquicia;
import com.nequi.nequi.domain.ProductoFranquiciaResponse;
import com.nequi.nequi.domain.SucursalRequest;
import com.nequi.nequi.service.IFranquiciaService;
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

@WebMvcTest(FranquiciaController.class)
class FranquiciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFranquiciaService franquiciaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Franquicia franquicia;

    @BeforeEach
    void setUp() {
        franquicia = new Franquicia();
        franquicia.setId(1);
        franquicia.setNombre("Franquicia A");
    }

    @Test
    void addFranquicia_ok() throws Exception {
        Mockito.when(franquiciaService.addFranquicia(any(Franquicia.class)))
                .thenReturn(new com.nequi.nequi.model.FranquiciaEntity());

        mockMvc.perform(post("/v1/franquicia/addFranquicia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(franquicia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Franquicia A"));
    }

    @Test
    void addSucursal_ok() throws Exception {
        SucursalRequest request = new SucursalRequest();
        request.setFranquicia("Franquicia A");
        request.setNombre("Sucursal A");

        Mockito.when(franquiciaService.addSucursal(any(SucursalRequest.class)))
                .thenReturn(franquicia);

        mockMvc.perform(post("/v1/franquicia/addSucursal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Franquicia A"));
    }

    @Test
    void getProductosFranquicia_ok() throws Exception {
        ProductoFranquiciaResponse response = new ProductoFranquiciaResponse();
        response.setNombreSucursal("Sucursal A");
        response.setNombreProducto("Combo");
        response.setStock(20);

        Mockito.when(franquiciaService.getProductosFranquicia(eq("Franquicia A")))
                .thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/v1/franquicia/getProductStock/Franquicia A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreSucursal").value("Sucursal A"))
                .andExpect(jsonPath("$[0].nombreProducto").value("Combo"))
                .andExpect(jsonPath("$[0].stock").value(20));
    }

    @Test
    void updateFranquicia_ok() throws Exception {
        Franquicia updated = new Franquicia();
        updated.setId(1);
        updated.setNombre("NuevoNombre");

        Mockito.when(franquiciaService.updateFranquicia(eq("Franquicia A"), any(Franquicia.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/v1/franquicia/updateFranquicia/Franquicia A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("NuevoNombre"));
    }

}
