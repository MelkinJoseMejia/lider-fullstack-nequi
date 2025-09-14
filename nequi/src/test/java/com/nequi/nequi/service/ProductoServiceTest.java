package com.nequi.nequi.service;

import com.nequi.nequi.domain.Producto;
import com.nequi.nequi.domain.ProductoRequest;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.model.ProductoEntity;
import com.nequi.nequi.model.SucursalEntity;
import com.nequi.nequi.repository.ProductoRepository;
import com.nequi.nequi.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productoService = new ProductoService(productoRepository);
        productoService.sucursalRepository = sucursalRepository;
    }

    @Test
    void getAllProductos() {
        ProductoEntity entity = new ProductoEntity();
        entity.setNombre("Producto A");
        entity.setStock(100);

        when(productoRepository.findAll()).thenReturn(List.of(entity));

        List<Producto> productos = productoService.getAllProductos();

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Producto A", productos.get(0).getNombre());
    }

    @Test
    void addProductoOK() throws ResourceNotFoundException {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Nuevo Producto");
        request.setStock(50);
        request.setSucursal("Sucursal A");

        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setId(1);

        when(sucursalRepository.findByName("Sucursal A")).thenReturn(sucursal);
        when(productoRepository.findByNombre("Nuevo Producto")).thenReturn(null);

        ProductoEntity productoEntity = new ProductoEntity();
        productoEntity.setNombre("Nuevo Producto");
        productoEntity.setStock(50);
        productoEntity.setIdSucursal(1);

        when(productoRepository.save(any())).thenReturn(productoEntity);

        ProductoEntity saved = productoService.addProducto(request);

        assertNotNull(saved);
        assertEquals("Nuevo Producto", saved.getNombre());
        assertEquals(50, saved.getStock());
    }

    @Test
    void addProductoFailed_sucursalNotFound() {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Nuevo Producto");
        request.setSucursal("Sucursal Inexistente");

        when(sucursalRepository.findByName("Sucursal Inexistente")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> productoService.addProducto(request));
    }

    @Test
    void addProductoFailed_ProductoExists() {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto Existente");
        request.setSucursal("Sucursal A");

        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setId(1);

        ProductoEntity existing = new ProductoEntity();
        existing.setNombre("Producto Existente");

        when(sucursalRepository.findByName("Sucursal A")).thenReturn(sucursal);
        when(productoRepository.findByNombre("Producto Existente")).thenReturn(existing);

        assertThrows(ResourceNotFoundException.class, () -> productoService.addProducto(request));
    }

    @Test
    void updateProductOK() throws ResourceNotFoundException {
        ProductoEntity existing = new ProductoEntity();
        existing.setNombre("Producto A");
        existing.setStock(10);

        Producto updated = new Producto();
        updated.setStock(20);

        when(productoRepository.findByNombre("Producto A")).thenReturn(existing);
        when(productoRepository.save(any())).thenReturn(existing);

        Producto result = productoService.updateProduct("Producto A", updated);

        assertNotNull(result);
        assertEquals(20, result.getStock());
    }

    @Test
    void updateProduct_NotFound() {
        when(productoRepository.findByNombre("No Existe")).thenReturn(null);

        Producto updated = new Producto();
        updated.setStock(20);

        assertThrows(ResourceNotFoundException.class, () ->
                productoService.updateProduct("No Existe", updated));
    }

    @Test
    void UpdateNombreProductOK() throws ResourceNotFoundException {
        ProductoEntity existing = new ProductoEntity();
        existing.setNombre("Producto A");

        Producto updated = new Producto();
        updated.setNombre("Nuevo Nombre");

        when(productoRepository.findByNombre("Producto A")).thenReturn(existing);
        when(productoRepository.save(any())).thenReturn(existing);

        Producto result = productoService.updateNombreProduct("Producto A", updated);

        assertNotNull(result);
        assertEquals("Nuevo Nombre", result.getNombre());
    }

    @Test
    void updateNombreProduct_NotFound() {
        when(productoRepository.findByNombre("No Existe")).thenReturn(null);

        Producto updated = new Producto();
        updated.setNombre("Nuevo Nombre");

        assertThrows(ResourceNotFoundException.class, () ->
                productoService.updateNombreProduct("No Existe", updated));
    }
}
