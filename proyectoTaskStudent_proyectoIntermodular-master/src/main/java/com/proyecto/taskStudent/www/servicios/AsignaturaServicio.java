package com.proyecto.taskStudent.www.servicios;

import com.proyecto.taskStudent.www.modelos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AsignaturaServicio {

    @Autowired
    private asignaturasRepo asignaturasRepositorio;

    @Autowired
    private profesorRepo profesorRepositorio;

    @Autowired
    private temasRepo temaRepositorio;

    @Autowired
    private contenidoRepo contenidoRepositorio;


    // Obtener asignatura
    public asignaturas obtenerAsignatura(Long id) {
        return asignaturasRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));
    }


    //crud del tema, aqui es donde se llaman a las funciones JPA, ya que estas acciones modifican la tabla
    public asignaturas crearAsignatura(
            String nombre,
            String descripcion,
            String codigo,
            String aula,
            java.time.LocalDate fechaInicio,
            java.time.LocalDate fechaFin,
            Integer horasTotales,
            String horario,
            String nombreUsuarioProfesor
    ) {

        profesores profesor = profesorRepositorio.findByNombreUsuario(nombreUsuarioProfesor)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        asignaturas asignatura = new asignaturas(
                nombre, descripcion, codigo, aula,
                fechaInicio, fechaFin, horasTotales, horario, profesor
        );

        return asignaturasRepositorio.save(asignatura);
    }


    // Editar asignatura
    public asignaturas editarAsignatura(
            Long id,
            String nombre,
            String descripcion,
            String codigo,
            String aula,
            java.time.LocalDate fechaInicio,
            java.time.LocalDate fechaFin,
            Integer horasTotales,
            String horario
    ) {

        asignaturas asignatura = obtenerAsignatura(id);

        asignatura.setNombre(nombre);
        asignatura.setDescripcion(descripcion);
        asignatura.setCodigo(codigo);
        asignatura.setAula(aula);
        asignatura.setFechaInicio(fechaInicio);
        asignatura.setFechaFin(fechaFin);
        asignatura.setHorasTotales(horasTotales);
        asignatura.setHorario(horario);

        return asignaturasRepositorio.save(asignatura);
    }


    // Eliminar asignatura (incluye temas y contenidos)
    public void eliminarAsignatura(Long id) {

        asignaturas asignatura = obtenerAsignatura(id);

        List<temas> temas = temaRepositorio.findByAsignaturaOrderByOrdenAsc(asignatura);

        for (temas t : temas) {
            contenidoRepositorio.deleteByTema(t);
        }

        temaRepositorio.deleteByAsignatura(asignatura);

        asignaturasRepositorio.delete(asignatura);
    }


    // Listar asignaturas de un profesor
    public List<asignaturas> listarAsignaturasProfesor(String nombreUsuario) {

        Optional <profesores> profesor = profesorRepositorio.findByNombreUsuario(nombreUsuario);

        if (profesor == null) {
            return Collections.emptyList();
        }

        return asignaturasRepositorio.findByProfesor(profesor.get());
    }




}
