package com.proyecto.taskStudent.www.modelos;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Repositorio donde se guardan las funciones JPA (Java Persistence API). Aqui es donde se encuentra la logica de las bases de datos sin tener que escribir ninguna consulta
@Repository
public interface usuariosRepo extends JpaRepository<usuarios, Long> {
    List<usuarios> findByEmail(String email);
    List<usuarios> findByNombreUsuario(String nombreUsuario);
    List<usuarios> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);









}