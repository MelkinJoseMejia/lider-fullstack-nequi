package com.nequi.nequi.service;

import com.nequi.nequi.config.MapperConfig;
import com.nequi.nequi.domain.Producto;
import com.nequi.nequi.domain.ProductoRequest;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.model.ProductoEntity;
import com.nequi.nequi.model.SucursalEntity;
import com.nequi.nequi.repository.ProductoRepository;
import com.nequi.nequi.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService implements IProductoService{

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    private final MapperConfig<ProductoEntity, Producto.ProductoBuilder> mapperConfig;

    @Autowired
    public ProductoService(
            ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
        this.mapperConfig = new MapperConfig<>(ProductoEntity.class, Producto.ProductoBuilder.class);
    }

    @Override
    public List<Producto> getAllProductos() {
        List<ProductoEntity> data = productoRepository.findAll();
        List<Producto> dataE = new ArrayList<>();
        if (data != null) {
            for (ProductoEntity pm : data) {
                dataE.add(
                        mapperConfig.toEntity(pm).build());
            }
        }
        return dataE;
    }

    @Override
    public ProductoEntity addProducto(ProductoRequest productoRequest) throws ResourceNotFoundException {
        SucursalEntity sucursalEntity = sucursalRepository.findByName(productoRequest.getSucursal());
        ProductoEntity productoEntity = productoRepository.findByNombre(productoRequest.getNombre());
        if(sucursalEntity == null || productoEntity != null){
            throw new ResourceNotFoundException("Sucursal No Encontrada o Producto ya Existe");
        }
        ProductoEntity data = new ProductoEntity();
        data.setNombre(productoRequest.getNombre());
        data.setStock(productoRequest.getStock());
        data.setIdSucursal(sucursalEntity.getId());
        return productoRepository.save(data);
    }

    @Override
    public Producto updateProduct(String nombreProducto, Producto producto) throws ResourceNotFoundException {
        Producto updatedProducto = new Producto();
        ProductoEntity productoEntity = productoRepository.findByNombre(nombreProducto);

        if(productoEntity == null ){
            throw new ResourceNotFoundException("Producto No Existe");
        }

        productoEntity.setStock(producto.getStock());
        ProductoEntity res = productoRepository.save(productoEntity);
        updatedProducto = mapperConfig.toEntity(res).build();

        return updatedProducto;
    }

    @Override
    public Producto updateNombreProduct(String nombreProducto, Producto producto) throws ResourceNotFoundException {
        Producto updatedProducto = new Producto();
        ProductoEntity productoEntity = productoRepository.findByNombre(nombreProducto);

        if(productoEntity == null ){
            throw new ResourceNotFoundException("Producto No Existe");
        }

        productoEntity.setNombre(producto.getNombre());
        ProductoEntity res = productoRepository.save(productoEntity);
        updatedProducto = mapperConfig.toEntity(res).build();

        return updatedProducto;
    }
}
