package com.proyecto.taskStudent.www.modelos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//Repositorio donde se guardan las funciones JPA (Java Persistence API). Aqui es donde se encuentra la logica de las bases de datos sin tener que escribir ninguna consulta
@Repository
public interface contenidoRepo extends JpaRepository<contenido, Long> {

    Long id(Long id);

    //Esta consulta cuenta el contenido tipo TAREA que tiene un profesor, en este caso el que ha creado dicha tarea.
    @Query("SELECT COUNT(c) FROM contenido c WHERE c.tipo = :tipo AND c.tema.asignatura.profesor = :profesor")
    long contarPorTipo(@Param("tipo") tipoContenido tipo, @Param("profesor") profesores profesor);
    void deleteByTema(temas tema);

    List<contenido> findTop5ByTipoAndTema_Asignatura_ProfesorOrderByIdDesc( tipoContenido tipo, profesores profesor);
    List<contenido> findTop5ByTipoAndTema_Asignatura_ProfesorOrderByFechaEntregaAsc( tipoContenido tipo, profesores profesor);



    //Esta consulta cuenta el contenido tipo TAREA que tiene un profesor, en este caso el que ha creado dicha tarea.
    @Query("SELECT COUNT(c) FROM contenido c WHERE c.tipo = com.proyecto.taskStudent.www.modelos.tipoContenido.TAREA AND c.tema.asignatura.id = :idAsignatura")
    Long contarTareasPorAsignatura(Long idAsignatura);



    //Esta consulta cuenta el contenido tipo TAREA que tiene un profesor, en este caso el que ha creado dicha tarea.
    @Query("SELECT COUNT(c) FROM contenido c WHERE c.tipo = com.proyecto.taskStudent.www.modelos.tipoContenido.EXAMEN AND c.tema.asignatura.id = :idAsignatura")
    Long contarExamenesPorAsignatura(Long idAsignatura);






}
