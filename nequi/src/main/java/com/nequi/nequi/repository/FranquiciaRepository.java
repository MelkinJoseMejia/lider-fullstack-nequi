package com.nequi.nequi.repository;

import com.nequi.nequi.model.FranquiciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FranquiciaRepository extends JpaRepository<FranquiciaEntity, Long> {

    @Query("SELECT f FROM FranquiciaEntity f WHERE f.nombre = ?1")
    FranquiciaEntity findByName(String nombre);

}
