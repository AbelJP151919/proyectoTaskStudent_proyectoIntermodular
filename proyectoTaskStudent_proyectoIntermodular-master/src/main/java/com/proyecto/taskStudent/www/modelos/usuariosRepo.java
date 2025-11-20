package com.proyecto.taskStudent.www.modelos;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface usuariosRepo extends JpaRepository<usuarios, Long> {
    List<usuarios> findByEmail(String email);

    //Funciones de CRUD
}