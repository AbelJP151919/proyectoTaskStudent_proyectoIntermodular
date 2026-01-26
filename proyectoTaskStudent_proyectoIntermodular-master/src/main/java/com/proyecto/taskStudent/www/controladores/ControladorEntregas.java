package com.proyecto.taskStudent.www.controladores;
import com.proyecto.taskStudent.www.config.EmailService;
import com.proyecto.taskStudent.www.modelos.*;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpHeaders;



@RequestMapping("/entregas")
@Controller
public class ControladorEntregas {


    @Autowired
    private contenidoRepo contenidoRepositorio;

    @Autowired
    private entregaRepo entregaRepositorio;

    @Autowired
    private EmailService emailService;

    @GetMapping("/crearEntrega/{idContenido}")
    public String mostrarFormularioEntrega(
            @PathVariable Long idContenido,
            Model model
    ){
        contenido contenido = contenidoRepositorio.findById(idContenido)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));

        model.addAttribute("contenido", contenido);
        return "crearEntregas";
    }

    @PostMapping("/crearEntrega/{idContenido}")
    public String crearEntrega(
            @PathVariable Long idContenido,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(required = false) String comentario,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model
    ) {

        alumnos alumno = (alumnos) session.getAttribute("alumno");

        if (alumno == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para entregar la tarea");
            return "redirect:/login";
        }

        contenido tarea = contenidoRepositorio.findById(idContenido)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));

        // Validaciones
        if (tarea.getFechaEntrega() != null &&
                LocalDate.now().isAfter(tarea.getFechaEntrega())) {
            model.addAttribute("mensajeError", "Ya has enviado esta entrega");
            return "error-Entrega";
        }

        if (entregaRepositorio.findByAlumnoAndTarea(alumno, tarea).isPresent()) {
            model.addAttribute("mensajeError", "Ya has enviado esta entrega");
            return "error-Entrega";
        }

        if (archivo.isEmpty()) {
            model.addAttribute("mensajeError", "Debes adjuntar un archivo");
            return "error-Entrega";
        }

        if (entregaRepositorio.existsByTareaIdAndAlumnoId(tarea.getId(), alumno.getId())) {
            model.addAttribute("mensajeError", "Ya has enviado esta entrega");
            return "error-Entrega";
        }

        try {
            String basePath = System.getProperty("user.dir") + "/uploads/entregas/";
            Files.createDirectories(Paths.get(basePath));

            String nombreArchivo = archivo.getOriginalFilename();
            Path ruta = Paths.get(basePath + nombreArchivo);

            Files.copy(archivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
            entregas entrega = new entregas();
            entrega.setAlumno(alumno);
            entrega.setTarea(tarea);
            entrega.setFechaEntrega(LocalDateTime.now());
            entrega.setArchivoNombre(nombreArchivo);
            entrega.setArchivoRuta(ruta.toString());
            entrega.setComentario(comentario);

            entregaRepositorio.save(entrega);

            redirectAttributes.addFlashAttribute("exito", "Entrega realizada correctamente");
            return "redirect:/contenido/alumno/" + idContenido;

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al subir el archivo");
            return "redirect:/crearEntrega/" + idContenido;
        }
    }

    @GetMapping("/descargarEntrega/{id}")
    public ResponseEntity<Resource> descargarEntrega(@PathVariable Long id) throws Exception {

        entregas entrega = entregaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        if (entrega.getArchivoRuta() == null) {
            throw new RuntimeException("Esta entrega no tiene archivo adjunto");
        }

        Path ruta = Paths.get(entrega.getArchivoRuta());
        UrlResource recurso = new UrlResource(ruta.toUri());

        if (!recurso.exists() || !recurso.isReadable()) {
            throw new RuntimeException("El archivo no existe o no se puede leer");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entrega.getArchivoNombre() + "\"")
                .body(recurso);
    }



    @GetMapping("/lista/{idContenido}")
    public String verEntregasDeTarea(
            @PathVariable Long idContenido,
            Model model
    ) {
        contenido tarea = contenidoRepositorio.findById(idContenido)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));

        // Obtener todas las entregas de esa tarea
        List<entregas> entregas= entregaRepositorio.findByTarea(tarea);

        model.addAttribute("tarea", tarea);
        model.addAttribute("entregas", entregas);

        return "listarEntregas";
    }

    @PostMapping("/calificar/{idEntrega}")
    public String calificarEntrega(
            @PathVariable Long idEntrega,
            @RequestParam Double nota,
            @RequestParam(required = false) String comentarioProfesor,
            RedirectAttributes redirectAttributes
    ) {
        entregas entrega = entregaRepositorio.findById(idEntrega)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        entrega.setNota(nota);
        entrega.setRevisada(true);

        if (comentarioProfesor != null && !comentarioProfesor.isBlank()) {
            entrega.setComentario(entrega.getComentario() + "\n\n[Profesor]: " + comentarioProfesor);
        }

        entregaRepositorio.save(entrega);
        emailService.enviarEmailCorreccion(entrega.getAlumno().getEmail(), entrega.getAlumno().getNombre(), entrega.getTarea().getTitulo(), nota, comentarioProfesor);


        redirectAttributes.addFlashAttribute("exito", "Entrega calificada correctamente");
        return "redirect:/entregas/lista/" + entrega.getTarea().getId();
    }


}

