package com.nequi.nequi.service;

import com.nequi.nequi.domain.DeleteProductoRequest;
import com.nequi.nequi.domain.ProductoRequest;
import com.nequi.nequi.domain.Sucursal;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.model.ProductoEntity;
import com.nequi.nequi.model.SucursalEntity;
import com.nequi.nequi.repository.FranquiciaRepository;
import com.nequi.nequi.repository.ProductoRepository;
import com.nequi.nequi.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private SucursalService sucursalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sucursalService = new SucursalService(sucursalRepository);
        sucursalService.productoRepository = productoRepository;
        sucursalService.franquiciaRepository = franquiciaRepository;
    }

    @Test
    void addProductOK() throws ResourceNotFoundException {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto A");
        request.setStock(10);
        request.setSucursal("Sucursal A");

        SucursalEntity sucursalEntity = new SucursalEntity();
        sucursalEntity.setId(1);
        sucursalEntity.setNombre("Sucursal A");

        when(sucursalRepository.findByName("Sucursal A")).thenReturn(sucursalEntity);
        when(productoRepository.findByNombre("Producto A")).thenReturn(null);

        ProductoEntity savedProducto = new ProductoEntity();
        savedProducto.setNombre("Producto A");
        savedProducto.setStock(10);
        savedProducto.setIdSucursal(1);

        when(productoRepository.save(any())).thenReturn(savedProducto);
        when(productoRepository.findBySucursal(1)).thenReturn(List.of(savedProducto));

        Sucursal result = sucursalService.addProduct(request);

        assertNotNull(result);
        assertEquals("Sucursal A", result.getNombre());
        assertEquals(1, result.getProductos().size());
        assertEquals("Producto A", result.getProductos().get(0).getNombre());
    }

    @Test
    void addProductFailed_SucursalNotFound() {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto A");
        request.setSucursal("Sucursal Inexistente");

        when(sucursalRepository.findByName("Sucursal Inexistente")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> sucursalService.addProduct(request));
    }

    @Test
    void addProductFailed_ProductoAlreadyExists() {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto Existente");
        request.setSucursal("Sucursal A");

        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setId(1);

        ProductoEntity productoExistente = new ProductoEntity();

        when(sucursalRepository.findByName("Sucursal A")).thenReturn(sucursal);
        when(productoRepository.findByNombre("Producto Existente")).thenReturn(productoExistente);

        assertThrows(ResourceNotFoundException.class, () -> sucursalService.addProduct(request));
    }

    @Test
    void deleteProductOK() throws ResourceNotFoundException {
        DeleteProductoRequest request = new DeleteProductoRequest();
        request.setNombreSucursal("Sucursal A");
        request.setNombreProducto("Producto A");

        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setId(1);
        sucursal.setNombre("Sucursal A");

        ProductoEntity producto = new ProductoEntity();
        producto.setId(100);
        producto.setNombre("Producto A");
        producto.setIdSucursal(1);

        when(sucursalRepository.findByName("Sucursal A")).thenReturn(sucursal);
        when(productoRepository.findByNombre("Producto A")).thenReturn(producto);

        when(productoRepository.findBySucursal(1)).thenReturn(List.of());

        Sucursal result = sucursalService.deleteProduct(request);

        assertNotNull(result);
        assertEquals("Sucursal A", result.getNombre());
        assertTrue(result.getProductos().isEmpty());
    }

    @Test
    void deleteProductFailed_SucursalOrProductoNotFound() {
        DeleteProductoRequest request = new DeleteProductoRequest();
        request.setNombreSucursal("Sucursal Inexistente");
        request.setNombreProducto("Producto A");

        when(sucursalRepository.findByName("Sucursal Inexistente")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> sucursalService.deleteProduct(request));

        // Otro caso: sucursal existe pero producto no
        SucursalEntity sucursal = new SucursalEntity();
        sucursal.setId(1);
        when(sucursalRepository.findByName("Sucursal A")).thenReturn(sucursal);
        when(productoRepository.findByNombre("Producto Inexistente")).thenReturn(null);

        request.setNombreSucursal("Sucursal A");
        request.setNombreProducto("Producto Inexistente");

        assertThrows(ResourceNotFoundException.class, () -> sucursalService.deleteProduct(request));
    }

    @Test
    void updateSucursalOK() throws ResourceNotFoundException {
        String oldName = "Sucursal A";
        SucursalEntity existing = new SucursalEntity();
        existing.setNombre(oldName);

        Sucursal updated = new Sucursal();
        updated.setNombre("Sucursal Nueva");

        SucursalEntity saved = new SucursalEntity();
        saved.setNombre("Sucursal Nueva");

        when(sucursalRepository.findByName(oldName)).thenReturn(existing);
        when(sucursalRepository.save(existing)).thenReturn(saved);

        Sucursal result = sucursalService.updateSucursal(oldName, updated);

        assertNotNull(result);
        assertEquals("Sucursal Nueva", result.getNombre());
    }

    @Test
    void updateSucursalFailed_NotFound() {
        when(sucursalRepository.findByName("NoExiste")).thenReturn(null);

        Sucursal input = new Sucursal();
        input.setNombre("Nueva");

        assertThrows(ResourceNotFoundException.class, () ->
                sucursalService.updateSucursal("NoExiste", input));
    }
}
