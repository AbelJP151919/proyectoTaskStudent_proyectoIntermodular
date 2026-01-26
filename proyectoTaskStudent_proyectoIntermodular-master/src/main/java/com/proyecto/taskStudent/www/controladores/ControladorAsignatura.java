package com.proyecto.taskStudent.www.controladores;

import com.proyecto.taskStudent.www.modelos.*;
import com.proyecto.taskStudent.www.servicios.AsignaturaServicio;
import com.proyecto.taskStudent.www.servicios.TemaServicio;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


// Controlador de las asignaturas
@Controller
@RequestMapping("/asignaturas")
public class ControladorAsignatura {


    @Autowired
    private AsignaturaServicio asignaturaService;


    // Se llama a los servicios de los temas, para posteriormente llamar a cada funcion
    @Autowired
    private TemaServicio temaService;

    // Se llama a los servicios de las asignaturas, para posteriormente llamar a cada funcion
    @Autowired
    private asignaturasRepo asignaturasRepositorio;

    @Autowired
    private alumnoRepo alumnoRepositorio;



    @GetMapping("/profesor")
    public String listarAsignaturasProfesor(HttpSession session,
                                            Model model,
                                            @RequestParam(required = false) String busqueda) {


        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        List<asignaturas> lista;

        if (busqueda != null && !busqueda.isBlank()) {
            lista = asignaturasRepositorio.findByNombreContainingIgnoreCase(busqueda);
        } else {
            lista = asignaturaService.listarAsignaturasProfesor(nombreUsuario);
        }

        if (lista == null) {
            return "redirect:/mostrarFormularioInicio";
        }

        model.addAttribute("listaAsignaturas", lista);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "paginaProfesor";
    }


    @GetMapping("/crear")
    public String mostrarFormularioCrear() {
        return "crearAsignatura";
    }


    @PostMapping("/crear")
    public String crearAsignatura(@RequestParam String nombre,
                                  @RequestParam String descripcion,
                                  @RequestParam String codigo,
                                  @RequestParam String aula,
                                  @RequestParam LocalDate fechaInicio,
                                  @RequestParam LocalDate fechaFin,
                                  @RequestParam Integer horasTotales,
                                  @RequestParam String horario,
                                  HttpSession session,
                                  Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        // Campos obligatorios
        if (nombre == null || nombre.isBlank() ||
                descripcion == null || descripcion.isBlank() ||
                codigo == null || codigo.isBlank() ||
                aula == null || aula.isBlank() ||
                horario == null || horario.isBlank() ||
                fechaInicio == null || fechaFin == null ||
                horasTotales == null) {

            model.addAttribute("mensajeError", "Debes rellenar todos los campos obligatorios");
            return "error-Asignatura";
        }

        // Longitud minima
        if (nombre.length() < 3) {
            model.addAttribute("mensajeError", "El nombre de la asignatura debe tener al menos 3 caracteres");
            return "error-Asignatura";
        }

        if (descripcion.length() < 10) {
            model.addAttribute("mensajeError", "La descripción debe tener al menos 10 caracteres");
            return "error-Asignatura";
        }

        // codigo
        if (codigo.length() < 2 ) {
            model.addAttribute("mensajeError", "El código debe tener al menos 2 caracteres");
            return "error-Asignatura";
        }
        if (codigo.length() < 6 ) {
            model.addAttribute("mensajeError", "El código debe tener 6 caracteres de maximo");
            return "error-Asignatura";
        }

        // Validar fechas
        if (fechaFin.isBefore(fechaInicio)) {
            model.addAttribute("mensajeError", "La fecha de fin no puede ser anterior a la fecha de inicio");
            return "error-Asignatura";
        }

        // Validar horas totales
        if (horasTotales <= 0) {
            model.addAttribute("mensajeError", "Las horas totales deben ser un número positivo");
            return "error-Asignatura";
        }

        // Evitar asignaturas duplicadas por código
        if (asignaturasRepositorio.existsByCodigo(codigo)) {
            model.addAttribute("mensajeError", "Ya existe una asignatura con ese código");
            return "error-Asignatura";
        }

        asignaturaService.crearAsignatura(
                nombre, descripcion, codigo, aula,
                fechaInicio, fechaFin, horasTotales, horario,
                nombreUsuario
        );

        return "redirect:/mostrarPaginaProfesores";
    }



    @GetMapping("/{id}")
    public String verAsignatura(@PathVariable Long id,
                                HttpSession session,
                                Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        asignaturas asignatura = asignaturaService.obtenerAsignatura(id);
        List<temas> listaTemas = temaService.listarTemasPorAsignatura(asignatura);

        model.addAttribute("asignatura", asignatura);
        model.addAttribute("temas", listaTemas);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "panelAsignatura";
    }

    @GetMapping("/alumno/{id}")
    public String verAsignaturaAlumno(@PathVariable Long id,
                                      HttpSession session,
                                      Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        alumnos alumno = alumnoRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);


        asignaturas asignatura = asignaturaService.obtenerAsignatura(id);

        // Validar que el alumno pertenece a la asignatura
        if (!alumno.getAsignaturas().contains(asignatura)) {
            return "redirect:/alumno/asignaturas";
        }

        List<temas> listaTemas = temaService.listarTemasPorAsignatura(asignatura);

        model.addAttribute("asignatura", asignatura);
        model.addAttribute("temas", listaTemas);

        return "panelAsignaturaAlumno";
    }


    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          Model model) {

        asignaturas asignatura = asignaturaService.obtenerAsignatura(id);

        model.addAttribute("asignatura", asignatura);

        return "editarAsignatura";
    }


    @PostMapping("/editar/{id}")
    public String editarAsignatura(@PathVariable Long id,
                                   @RequestParam String nombre,
                                   @RequestParam String descripcion,
                                   @RequestParam String codigo,
                                   @RequestParam String aula,
                                   @RequestParam LocalDate fechaInicio,
                                   @RequestParam LocalDate fechaFin,
                                   @RequestParam Integer horasTotales,
                                   @RequestParam String horario) {

        asignaturaService.editarAsignatura(
                id, nombre, descripcion, codigo, aula,
                fechaInicio, fechaFin, horasTotales, horario
        );

        return "redirect:/asignaturas/" + id;
    }

    @GetMapping("/eliminar/{id}")
    public String confirmarEliminarAsignatura(@PathVariable Long id, Model model) {

        asignaturas asignatura = asignaturaService.obtenerAsignatura(id);

        model.addAttribute("titulo", "¿Eliminar Asignatura?");
        model.addAttribute("mensaje", "Esta acción no se puede deshacer. ¿Seguro que deseas eliminar esta asignatura?");
        model.addAttribute("accion", "/asignaturas/eliminar/" + id);
        model.addAttribute("cancelar", "/asignaturas/profesor");

        return "eliminar";
    }


    @PostMapping("/eliminar/{id}")
    public String eliminarAsignatura(@PathVariable Long id) {

        asignaturaService.eliminarAsignatura(id);

        return "redirect:/asignaturas/profesor";
    }
}

