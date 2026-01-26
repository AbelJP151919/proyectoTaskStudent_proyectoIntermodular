package com.proyecto.taskStudent.www.servicios;

import com.proyecto.taskStudent.www.modelos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;


//Servicio de los contenidos
@Service
public class ContenidoServicio {

    @Autowired
    private contenidoRepo contenidoRepositorio;

    @Autowired
    private temasRepo temaRepositorio;

    //Se obtiene el contenido a partir de la ID
    public contenido obtenerContenido(Long id) {
        return contenidoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));
    }

    //crud del tema, aqui es donde se llaman a las funciones JPA, ya que estas acciones modifican la tabla
    public contenido crearContenido(
            Long idTema,
            String titulo,
            String descripcion,
            java.time.LocalDate fechaEntrega,
            tipoContenido tipo,
            //Es un dato MultiplarFile ya que se puede subir distintos tipos de archivos como pdfs, o cualquier otro tipo de archivos
            org.springframework.web.multipart.MultipartFile archivo
    ) throws IOException {

        temas tema = temaRepositorio.findById(idTema)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        contenido contenido = new contenido(titulo, descripcion, fechaEntrega, tipo, tema);

        // Guardar archivo si nosostros le hemos introducido un archivo
        if (archivo != null && !archivo.isEmpty()) {

            String nombreArchivo = archivo.getOriginalFilename();
            Path ruta = Paths.get("uploads/contenidos/" + nombreArchivo);

            Files.createDirectories(ruta.getParent());
            Files.copy(archivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

            contenido.setNombreArchivo(nombreArchivo);
            contenido.setRutaArchivo(ruta.toString());
        }

        return contenidoRepositorio.save(contenido);
    }

    //funcion para editar el Contenido
    public contenido editarContenido(
            Long id,
            String titulo,
            String descripcion,
            java.time.LocalDate fechaEntrega,
            tipoContenido tipo,
            org.springframework.web.multipart.MultipartFile archivo
    ) throws IOException {

        contenido contenido = obtenerContenido(id);

        contenido.setTitulo(titulo);
        contenido.setDescripcion(descripcion);
        contenido.setFechaEntrega(fechaEntrega);
        contenido.setTipo(tipo);

        if (archivo != null && !archivo.isEmpty()) {

            String nombreArchivo = archivo.getOriginalFilename();
            Path ruta = Paths.get("uploads/contenidos/" + nombreArchivo);

            Files.createDirectories(ruta.getParent());
            Files.copy(archivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

            contenido.setNombreArchivo(nombreArchivo);
            contenido.setRutaArchivo(ruta.toString());
        }

        return contenidoRepositorio.save(contenido);
    }


    public void eliminarContenido(Long id) {
        contenidoRepositorio.deleteById(id);
    }
}
