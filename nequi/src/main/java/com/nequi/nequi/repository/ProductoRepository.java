package com.nequi.nequi.repository;

import com.nequi.nequi.model.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {

    @Query("SELECT p FROM ProductoEntity p WHERE p.nombre = ?1")
    ProductoEntity findByNombre(String nombre);

    @Query("SELECT p FROM ProductoEntity p WHERE p.idSucursal = ?1")
    List<ProductoEntity> findBySucursal(int idSucursal);

}
