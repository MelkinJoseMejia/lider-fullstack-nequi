package com.nequi.nequi.service;

import com.nequi.nequi.domain.Franquicia;
import com.nequi.nequi.domain.ProductoFranquiciaResponse;
import com.nequi.nequi.domain.SucursalRequest;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.model.FranquiciaEntity;
import com.nequi.nequi.model.ProductoEntity;
import com.nequi.nequi.model.SucursalEntity;
import com.nequi.nequi.repository.FranquiciaRepository;
import com.nequi.nequi.repository.ProductoRepository;
import com.nequi.nequi.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private FranquiciaService franquiciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        franquiciaService = new FranquiciaService(franquiciaRepository);
        franquiciaService.sucursalRepository = sucursalRepository;
        franquiciaService.productoRepository = productoRepository;
    }

    @Test
    void addFranquicia() throws ResourceNotFoundException {
        Franquicia franquicia = new Franquicia();
        franquicia.setNombre("Franquicia X");

        FranquiciaEntity entity = new FranquiciaEntity();
        entity.setNombre("Franquicia X");

        when(franquiciaRepository.save(any(FranquiciaEntity.class))).thenReturn(entity);

        FranquiciaEntity result = franquiciaService.addFranquicia(franquicia);

        assertNotNull(result);
        assertEquals("Franquicia X", result.getNombre());
    }

    @Test
    void addFranquicia_existente() {
        Franquicia franquicia = new Franquicia();
        franquicia.setNombre("Franquicia X");

        when(franquiciaRepository.findByName("Franquicia X")).thenReturn(new FranquiciaEntity());

        assertThrows(ResourceNotFoundException.class, () -> franquiciaService.addFranquicia(franquicia));
    }

    @Test
    void addSucursal_franquiciaNotFound() {
        SucursalRequest request = new SucursalRequest();
        request.setFranquicia("Inexistente");
        request.setNombre("Sucursal Nueva");

        when(franquiciaRepository.findByName("Inexistente")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> franquiciaService.addSucursal(request));
    }

    @Test
    void addSucursal_sucursalExists() {
        SucursalRequest request = new SucursalRequest();
        request.setFranquicia("Franquicia A");
        request.setNombre("Sucursal Existente");

        FranquiciaEntity franquiciaEntity = new FranquiciaEntity();
        franquiciaEntity.setId(1);

        SucursalEntity sucursalEntity = new SucursalEntity();

        when(franquiciaRepository.findByName("Franquicia A")).thenReturn(franquiciaEntity);
        when(sucursalRepository.findByName("Sucursal Existente")).thenReturn(sucursalEntity);

        assertThrows(ResourceNotFoundException.class, () -> franquiciaService.addSucursal(request));
    }

    @Test
    void getProductosFranquicia() throws ResourceNotFoundException {
        String nombreFranquicia = "Franquicia A";
        FranquiciaEntity franquiciaEntity = new FranquiciaEntity();
        franquiciaEntity.setId(1);

        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setId(1);
        sucursal.setNombre("Sucursal A");

        ProductoEntity producto = new ProductoEntity();
        producto.setNombre("Producto A");
        producto.setStock(10);

        when(franquiciaRepository.findByName(nombreFranquicia)).thenReturn(franquiciaEntity);
        when(sucursalRepository.findByFranquicia(1)).thenReturn(List.of(sucursal));
        when(productoRepository.findBySucursal(1)).thenReturn(List.of(producto));

        List<ProductoFranquiciaResponse> result = franquiciaService.getProductosFranquicia(nombreFranquicia);

        assertFalse(result.isEmpty());
        assertEquals("Sucursal A", result.get(0).getNombreSucursal());
        assertEquals("Producto A", result.get(0).getNombreProducto());
        assertEquals(10, result.get(0).getStock());
    }

    @Test
    void getProductosFranquicia_FranquiciaNotFound() {
        when(franquiciaRepository.findByName("NoExiste")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                franquiciaService.getProductosFranquicia("NoExiste"));
    }

    @Test
    void updateFranquicia_OK() throws ResourceNotFoundException {
        FranquiciaEntity franquiciaEntity = new FranquiciaEntity();
        franquiciaEntity.setId(1);
        franquiciaEntity.setNombre("Franquicia A");

        Franquicia input = new Franquicia();
        input.setNombre("Franquicia Actualizada");

        FranquiciaEntity savedEntity = new FranquiciaEntity();
        savedEntity.setNombre("Franquicia Actualizada");

        when(franquiciaRepository.findByName("Franquicia A")).thenReturn(franquiciaEntity);
        when(franquiciaRepository.save(any())).thenReturn(savedEntity);

        Franquicia result = franquiciaService.updateFranquicia("Franquicia A", input);

        assertNotNull(result);
        assertEquals("Franquicia Actualizada", result.getNombre());
    }

    @Test
    void updateFranquicia_NotFound() {
        when(franquiciaRepository.findByName("NoExiste")).thenReturn(null);

        Franquicia input = new Franquicia();
        input.setNombre("Nuevo Nombre");

        assertThrows(ResourceNotFoundException.class, () ->
                franquiciaService.updateFranquicia("NoExiste", input));
    }
}
