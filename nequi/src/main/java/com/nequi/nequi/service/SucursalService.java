package com.nequi.nequi.service;

import com.nequi.nequi.config.MapperConfig;
import com.nequi.nequi.domain.*;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.model.ProductoEntity;
import com.nequi.nequi.model.SucursalEntity;
import com.nequi.nequi.repository.FranquiciaRepository;
import com.nequi.nequi.repository.ProductoRepository;
import com.nequi.nequi.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SucursalService implements ISucursalService{

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    FranquiciaRepository franquiciaRepository;

    @Autowired
    ProductoRepository productoRepository;

    private final MapperConfig<SucursalEntity, Sucursal.SucursalBuilder> mapperConfig;
    private final MapperConfig<ProductoEntity, Producto.ProductoBuilder> mapperProduct;

    @Autowired
    public SucursalService(
            SucursalRepository sucursalRepository){
        this.sucursalRepository = sucursalRepository;
        this.mapperConfig = new MapperConfig<>(SucursalEntity.class, Sucursal.SucursalBuilder.class);
        this.mapperProduct = new MapperConfig<>(ProductoEntity.class, Producto.ProductoBuilder.class);
    }

    @Override
    public Sucursal addProduct(ProductoRequest productoRequest) throws ResourceNotFoundException {
        SucursalEntity sucursalEntity = sucursalRepository.findByName(productoRequest.getSucursal());
        ProductoEntity productoEntity = productoRepository.findByNombre(productoRequest.getNombre());
        if(sucursalEntity == null || productoEntity != null){
            throw new ResourceNotFoundException("Sucursal No Encontrada o Producto ya Existe");
        }

        ProductoEntity data = new ProductoEntity();
        data.setNombre(productoRequest.getNombre());
        data.setStock(productoRequest.getStock());
        data.setIdSucursal(sucursalEntity.getId());
        ProductoEntity res = productoRepository.save(data);

        List<ProductoEntity> productoEntityList = productoRepository.findBySucursal(sucursalEntity.getId());

        List<Producto> dataE = new ArrayList<>();
        if (productoEntityList != null) {
            for (ProductoEntity pm : productoEntityList) {
                dataE.add(
                        mapperProduct.toEntity(pm).build());
            }
        }
        Sucursal response = mapperConfig.toEntity(sucursalEntity).build();
        response.setProductos(dataE);
        return response;
    }

    @Override
    public Sucursal deleteProduct(DeleteProductoRequest deleteProductoRequest) throws ResourceNotFoundException {
        SucursalEntity sucursalEntity = sucursalRepository.findByName(deleteProductoRequest.getNombreSucursal());
        ProductoEntity productoEntity = productoRepository.findByNombre(deleteProductoRequest.getNombreProducto());
        if(sucursalEntity == null || productoEntity == null){
            throw new ResourceNotFoundException("Sucursal o Producto No Existe");
        }

        productoRepository.deleteById(productoEntity.getId());
        List<ProductoEntity> productoEntityList = productoRepository.findBySucursal(sucursalEntity.getId());

        List<Producto> dataE = new ArrayList<>();
        if (productoEntityList != null) {
            for (ProductoEntity pm : productoEntityList) {
                dataE.add(
                        mapperProduct.toEntity(pm).build());
            }
        }
        Sucursal response = mapperConfig.toEntity(sucursalEntity).build();
        response.setProductos(dataE);
        return response;
    }

    @Override
    public Sucursal updateSucursal(String nombreSucursal, Sucursal sucursal) throws ResourceNotFoundException {
        Sucursal updatedSucursal = new Sucursal();
        SucursalEntity sucursalEntity = sucursalRepository.findByName(nombreSucursal);

        if(sucursalEntity == null ){
            throw new ResourceNotFoundException("Sucursal No Existe");
        }

        sucursalEntity.setNombre(sucursal.getNombre());
        SucursalEntity res = sucursalRepository.save(sucursalEntity);
        updatedSucursal = mapperConfig.toEntity(res).build();

        return updatedSucursal;
    }
}
