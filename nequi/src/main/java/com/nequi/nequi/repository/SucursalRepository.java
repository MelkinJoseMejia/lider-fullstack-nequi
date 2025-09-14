package com.nequi.nequi.repository;

import com.nequi.nequi.model.SucursalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<SucursalEntity, Long> {

    @Query("SELECT s FROM SucursalEntity s WHERE s.nombre = ?1")
    SucursalEntity findByName(String nombre);

    @Query("SELECT s FROM SucursalEntity s WHERE s.idFranquicia = ?1")
    List<SucursalEntity> findByFranquicia(int idFranquicia);

}
