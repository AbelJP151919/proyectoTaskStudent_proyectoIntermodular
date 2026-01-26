package com.proyecto.taskStudent.www.modelos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


//Repositorio donde se guardan las funciones JPA (Java Persistence API). Aqui es donde se encuentra la logica de las bases de datos sin tener que escribir ninguna consulta

public interface alumnoRepo extends JpaRepository<alumnos, Long> {
    Optional<alumnos> findByCurso(String curso);

    Optional<alumnos> findByNombreUsuario(String nombreUsuario);

    List<alumnos> findByAsignaturas(asignaturas asignaturas);

}