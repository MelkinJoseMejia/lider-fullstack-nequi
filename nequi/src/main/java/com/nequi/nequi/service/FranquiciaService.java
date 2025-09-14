package com.nequi.nequi.service;


import com.nequi.nequi.config.MapperConfig;
import com.nequi.nequi.domain.*;
import com.nequi.nequi.exception.ResourceNotFoundException;
import com.nequi.nequi.model.FranquiciaEntity;
import com.nequi.nequi.model.ProductoEntity;
import com.nequi.nequi.model.SucursalEntity;
import com.nequi.nequi.repository.FranquiciaRepository;
import com.nequi.nequi.repository.ProductoRepository;
import com.nequi.nequi.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FranquiciaService implements IFranquiciaService{

    @Autowired
    FranquiciaRepository franquiciaRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    ProductoRepository productoRepository;

    private final MapperConfig<FranquiciaEntity, Franquicia.FranquiciaBuilder> mapperConfig;
    private final MapperConfig<SucursalEntity, Sucursal.SucursalBuilder> mapperSucursal;

    @Autowired
    public FranquiciaService(
            FranquiciaRepository franquiciaRepository){
        this.franquiciaRepository = franquiciaRepository;
        this.mapperConfig = new MapperConfig<>(FranquiciaEntity.class, Franquicia.FranquiciaBuilder.class);
        this.mapperSucursal = new MapperConfig<>(SucursalEntity.class, Sucursal.SucursalBuilder.class);
    }

    @Override
    public FranquiciaEntity addFranquicia(Franquicia franquicia) throws ResourceNotFoundException {
        FranquiciaEntity franquiciaEntity = franquiciaRepository.findByName(franquicia.getNombre());
        if(franquiciaEntity != null){
            throw new ResourceNotFoundException("Franquicia ya Existe");
        }
        FranquiciaEntity data = mapperConfig.toData(franquicia);
        return franquiciaRepository.save(data);
    }

    @Override
    public Franquicia addSucursal(SucursalRequest sucursalRequest) throws ResourceNotFoundException {
        FranquiciaEntity franquiciaEntity = franquiciaRepository.findByName(sucursalRequest.getFranquicia());
        SucursalEntity sucursalEntity = sucursalRepository.findByName(sucursalRequest.getNombre());
        if(franquiciaEntity == null || sucursalEntity != null){
            throw new ResourceNotFoundException("Franquicia No Encontrada o Sucursal ya Existe");
        }
        SucursalEntity data = new SucursalEntity();
        data.setNombre(sucursalRequest.getNombre());
        data.setIdFranquicia(franquiciaEntity.getId());
        SucursalEntity res = sucursalRepository.save(data);

        List<SucursalEntity> sucursals = sucursalRepository.findByFranquicia(franquiciaEntity.getId());
        List<Sucursal> dataE = new ArrayList<>();
        if (sucursals != null) {
            for (SucursalEntity pm : sucursals) {
                dataE.add(
                        mapperSucursal.toEntity(pm).build());
            }
        }
        Franquicia response = mapperConfig.toEntity(franquiciaEntity).build();
        response.setSucursales(dataE);
        return response;
    }

    @Override
    public List<ProductoFranquiciaResponse> getProductosFranquicia(String nombreFranquicia)
            throws ResourceNotFoundException {
        List<ProductoFranquiciaResponse> lista = new ArrayList<>();
        ProductoFranquiciaResponse p = null;
        ProductoEntity p1 = null;
        FranquiciaEntity franquiciaEntity = franquiciaRepository.findByName(nombreFranquicia);
        if(franquiciaEntity == null){
            throw new ResourceNotFoundException("Franquicia No Existe");
        }

        List<SucursalEntity> sucursalEntities = sucursalRepository.findByFranquicia(franquiciaEntity.getId());
        if(sucursalEntities == null){
            throw new ResourceNotFoundException("Sucursal No Existe");
        }

        if (sucursalEntities != null) {
            for (SucursalEntity pm : sucursalEntities) {
                p = new ProductoFranquiciaResponse();
                p.setNombreSucursal(pm.getNombre());
                List<ProductoEntity> list = productoRepository.findBySucursal(pm.getId());
                if(list != null && list.size() > 0){
                    p1 = list.stream().max(Comparator.comparingInt(ProductoEntity::getStock)).get();
                    p.setNombreProducto(p1.getNombre());
                    p.setStock(p1.getStock());
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    @Override
    public Franquicia updateFranquicia(String nombreFranquicia, Franquicia franquicia) throws ResourceNotFoundException {
        Franquicia updatedFranquicia = new Franquicia();
        FranquiciaEntity franquiciaEntity = franquiciaRepository.findByName(nombreFranquicia);

        if(franquiciaEntity == null ){
            throw new ResourceNotFoundException("Franquicia No Existe");
        }

        franquiciaEntity.setNombre(franquicia.getNombre());
        FranquiciaEntity res = franquiciaRepository.save(franquiciaEntity);
        updatedFranquicia = mapperConfig.toEntity(res).build();

        return updatedFranquicia;
    }
}
