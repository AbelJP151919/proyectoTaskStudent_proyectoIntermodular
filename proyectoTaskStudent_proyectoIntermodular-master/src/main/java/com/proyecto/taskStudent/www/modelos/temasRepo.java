package com.proyecto.taskStudent.www.modelos;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


//Repositorio donde se guardan las funciones JPA (Java Persistence API). Aqui es donde se encuentra la logica de las bases de datos sin tener que escribir ninguna consulta
@Repository
public interface temasRepo extends JpaRepository<temas, Long> {

    List<temas> findByAsignaturaOrderByOrdenAsc(asignaturas asignatura);

    @Query("SELECT COUNT(t) FROM temas t WHERE t.asignatura.id = :idAsignatura")
    Long contarTemasPorAsignatura(Long idAsignatura);



    boolean existsByNombreAndAsignatura(String nombre, asignaturas asignatura);

    void deleteByAsignatura(asignaturas asignatura);
}

