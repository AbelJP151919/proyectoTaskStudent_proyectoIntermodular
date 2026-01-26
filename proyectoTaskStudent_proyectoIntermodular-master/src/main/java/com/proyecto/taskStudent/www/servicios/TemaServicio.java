package com.proyecto.taskStudent.www.servicios;

import com.proyecto.taskStudent.www.modelos.asignaturas;
import com.proyecto.taskStudent.www.modelos.contenidoRepo;
import com.proyecto.taskStudent.www.modelos.temas;
import com.proyecto.taskStudent.www.modelos.temasRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


// servicio del tema
@Service
public class TemaServicio {

    // Se llama a las variables que queremos

    @Autowired
    private temasRepo temaRepositorio;

    @Autowired
    private contenidoRepo contenidoRepositorio;


    //crud del tema, aqui es donde se llaman a las funciones JPA, ya que estas acciones modifican la tabla
    public temas obtenerTema(Long id) {
        return temaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));
    }

    public temas crearTema(String nombre, String descripcion, Integer orden, asignaturas asignatura) {
        temas tema = new temas(nombre, descripcion, orden, asignatura);
        return temaRepositorio.save(tema);
    }

    public temas editarTema(Long id, String nombre, String descripcion, Integer orden) {
        temas tema = obtenerTema(id);

        tema.setNombre(nombre);
        tema.setDescripcion(descripcion);
        tema.setOrden(orden);

        return temaRepositorio.save(tema);
    }


    // Esta funcion es transaccional ya que se tiene que eliminar dos entidades en una misma funcion, evitando una excepcion (LazyInitializationException)
    @Transactional
    public void eliminarTema(Long id) {
        temas tema = obtenerTema(id);

        // borrar contenidos
        contenidoRepositorio.deleteByTema(tema);

        // borrar tema
        temaRepositorio.delete(tema);
    }

    //Funcion para listar los temas

    public List<temas> listarTemasPorAsignatura(asignaturas asignatura) {
        return temaRepositorio.findByAsignaturaOrderByOrdenAsc(asignatura);
    }
}

