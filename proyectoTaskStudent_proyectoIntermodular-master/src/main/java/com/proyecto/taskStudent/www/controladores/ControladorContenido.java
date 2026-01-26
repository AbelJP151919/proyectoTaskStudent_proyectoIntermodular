package com.proyecto.taskStudent.www.controladores;

import com.proyecto.taskStudent.www.modelos.*;
import com.proyecto.taskStudent.www.servicios.ContenidoServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Controller
@RequestMapping("/contenido")
public class ControladorContenido {

    @Autowired
    private ContenidoServicio contenidoService;

    @Autowired
    private temasRepo temaRepositorio;

    @Autowired
    private alumnoRepo alumnoRepositorio;

    @Autowired
    private entregaRepo entregaRepositorio;

    @GetMapping("/crear/{idTema}")
    public String mostrarFormularioCrear(@PathVariable Long idTema,
                                         Model model) {



        temas tema = temaRepositorio.findById(idTema)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        model.addAttribute("tema", tema);
        model.addAttribute("asignatura", tema.getAsignatura());

        return "crearContenido";
    }


    @PostMapping("/crear/{idTema}")
    public String crearContenido(@PathVariable Long idTema,
                                 @RequestParam String titulo,
                                 @RequestParam String descripcion,
                                 @RequestParam(required = false) LocalDate fechaEntrega,
                                 @RequestParam("tipo") tipoContenido tipo,
                                 @RequestParam(value = "archivo", required = false) MultipartFile archivo,
                                 Model model) throws Exception {

        // Obtener tema
        temas tema = temaRepositorio.findById(idTema)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));



        // Campos obligatorios
        if (titulo == null || titulo.isBlank() ||
                descripcion == null || descripcion.isBlank()) {

            model.addAttribute("mensajeError", "Debes rellenar todos los campos obligatorios");
            return "error-Contenido";
        }

        //Longitud minima
        if (titulo.length() < 3) {
            model.addAttribute("mensajeError", "El título debe tener al menos 3 caracteres");
            return "error-Contenido";
        }

        if (descripcion.length() < 10) {
            model.addAttribute("mensajeError", "La descripción debe tener al menos 10 caracteres");
            return "error-Contenido";
        }

        // Validar fecha de entrega (si existe)
        if (fechaEntrega != null && fechaEntrega.isBefore(LocalDate.now())) {
            model.addAttribute("mensajeError", "La fecha de entrega no puede ser anterior a hoy");
            return "error-Contenido";
        }

        contenido contenido = contenidoService.crearContenido(
                idTema, titulo, descripcion, fechaEntrega, tipo, archivo
        );

        Long idAsignatura = contenido.getTema().getAsignatura().getId();
        return "redirect:/asignaturas/" + idAsignatura;
    }



    @GetMapping("/ver/{id}")
    public String mostrarContenido(@PathVariable Long id,
                                   Model model) {

        contenido contenido = contenidoService.obtenerContenido(id);

        model.addAttribute("contenido", contenido);
        model.addAttribute("tema", contenido.getTema());
        model.addAttribute("asignatura", contenido.getTema().getAsignatura());

        return "mostrarContenido";
    }
    @GetMapping("/alumno/{id}")
    public String mostrarContenidoAlumno(@PathVariable Long id,
                                         Model model,
                                         HttpSession session) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        alumnos alumno = alumnoRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        contenido contenido = contenidoService.obtenerContenido(id);

        asignaturas asignatura = contenido.getTema().getAsignatura();

        // Validar que el alumno pertenece a la asignatura
        if (!alumno.getAsignaturas().contains(asignatura)) {
            return "redirect:/alumno/asignaturas";
        }

        // ✔ AÑADIR ESTO
        entregas entrega = entregaRepositorio.findByTarea_IdAndAlumno_Id(id, alumno.getId());
        model.addAttribute("entrega", entrega);

        model.addAttribute("contenido", contenido);
        model.addAttribute("tema", contenido.getTema());
        model.addAttribute("asignatura", asignatura);

        return "mostrarContenidoAlumno";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          Model model) {


        contenido contenido = contenidoService.obtenerContenido(id);

        model.addAttribute("contenido", contenido);
        model.addAttribute("tema", contenido.getTema());

        return "editarContenido";
    }


    @PostMapping("/editar/{id}")
    public String editarContenido(@PathVariable Long id,
                                  @RequestParam String titulo,
                                  @RequestParam String descripcion,
                                  @RequestParam(required = false) LocalDate fechaEntrega,
                                  @RequestParam("tipo") tipoContenido tipo,
                                  @RequestParam(value = "archivo", required = false) MultipartFile archivo
    ) throws Exception {

        contenido contenido = contenidoService.editarContenido(
                id, titulo, descripcion, fechaEntrega, tipo, archivo
        );

        Long idAsignatura = contenido.getTema().getAsignatura().getId();
        return "redirect:/asignaturas/" + idAsignatura;
    }


    @GetMapping("/eliminar/{id}")
    public String confirmarEliminarContenido(@PathVariable Long id, Model model) {

        contenido contenido = contenidoService.obtenerContenido(id);

        model.addAttribute("titulo", "¿Eliminar Contenido?");
        model.addAttribute("mensaje", "Esta acción no se puede deshacer. ¿Seguro que deseas eliminar este contenido?");
        model.addAttribute("accion", "/contenido/eliminar/" + id);
        model.addAttribute("cancelar", "/asignaturas/" + contenido.getTema().getAsignatura().getId());

        return "eliminar";
    }



    @PostMapping("/eliminar/{id}")
    public String eliminarContenido(@PathVariable Long id) {

        contenido contenido = contenidoService.obtenerContenido(id);
        Long idAsignatura = contenido.getTema().getAsignatura().getId();

        contenidoService.eliminarContenido(id);

        return "redirect:/asignaturas/" + idAsignatura;
    }


    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long id) throws Exception {

        contenido contenido = contenidoService.obtenerContenido(id);

        if (contenido.getRutaArchivo() == null) {
            throw new RuntimeException("Este contenido no tiene archivo adjunto");
        }

        Path ruta = Paths.get(contenido.getRutaArchivo());
        UrlResource recurso = new UrlResource(ruta.toUri());

        if (!recurso.exists() || !recurso.isReadable()) {
            throw new RuntimeException("El archivo no existe o no se puede leer");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + contenido.getNombreArchivo() + "\"")
                .body(recurso);
    }
}
