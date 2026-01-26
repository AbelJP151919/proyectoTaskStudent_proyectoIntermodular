package com.proyecto.taskStudent.www.modelos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


//Repositorio de la entrega
public interface entregaRepo extends JpaRepository<entregas, Long> {

    Optional<entregas> findByAlumnoAndTarea(alumnos alumno, contenido tarea);

    List<entregas> findByTarea(contenido tareas);

    boolean existsByTareaIdAndAlumnoId(Long tareaId, Long alumnoId);

    entregas findByTarea_IdAndAlumno_Id(Long tareaId, Long alumnoId);

    List<entregas> findByAlumno(alumnos alumno);


    void deleteByAlumnoId(Long alumnoId);


}
