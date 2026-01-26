package com.proyecto.taskStudent.www.modelos;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


//Repositorio donde se guardan las funciones JPA (Java Persistence API). Aqui es donde se encuentra la logica de las bases de datos sin tener que escribir ninguna consulta

@Repository
public interface asignaturasRepo extends JpaRepository<asignaturas, Long> {

    Optional<asignaturas> findByCodigo(String codigo);

    List<asignaturas> findByNombreContainingIgnoreCase(String nombre);

    List<asignaturas> findByProfesor(profesores profesor);

    @Query(" SELECT COUNT(DISTINCT al) FROM asignaturas  a JOIN a.alumnos al WHERE a.profesor.id = :idProfesor")
    Long contarAlumnosDeProfesor(@Param("idProfesor") Long idProfesor);

    boolean existsByCodigo(String codigo);





    Long id(Long id);
}
