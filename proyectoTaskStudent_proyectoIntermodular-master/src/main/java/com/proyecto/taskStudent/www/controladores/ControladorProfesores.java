package com.proyecto.taskStudent.www.controladores;

import com.proyecto.taskStudent.www.modelos.*;
import com.proyecto.taskStudent.www.servicios.AsignaturaServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;



@Controller
public class ControladorProfesores {

    @Autowired
    private profesorRepo profesorRepositorio;

    // Se llama a los servicios de las asignaturas, para posteriormente llamar a cada funcion

    @Autowired
    private AsignaturaServicio asignaturaService;

    @Autowired
    private contenidoRepo contenidoRepositorio;

    @Autowired
    private asignaturasRepo asignaturasRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @GetMapping("/mostrarPerfilProfesor")
    public String mostrarPerfil(HttpSession session, Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        profesores profesor = profesorRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        model.addAttribute("profesor", profesor);

        return "paginaPerfilProfesor";
    }



    @GetMapping("/mostrarPaginaProfesores")
    public String mostrarPanelProfesor(HttpSession session, Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        profesores profesor = profesorRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        // Asignaturas del profesor
        model.addAttribute("listaAsignaturas",
                asignaturaService.listarAsignaturasProfesor(nombreUsuario));

        // Estadisticas de la pagina web
        long totalTareas = contenidoRepositorio.contarPorTipo(tipoContenido.TAREA, profesor);
        model.addAttribute("totalTareas", totalTareas);

        long totalAlumnos = asignaturasRepositorio.contarAlumnosDeProfesor(profesor.getId());
        model.addAttribute("totalAlumnos", totalAlumnos);


        List<contenido> tareasRecientes =
                contenidoRepositorio.findTop5ByTipoAndTema_Asignatura_ProfesorOrderByIdDesc(tipoContenido.TAREA, profesor);
        model.addAttribute("tareasRecientes", tareasRecientes);

        List<contenido> examenesProximos =
                contenidoRepositorio.findTop5ByTipoAndTema_Asignatura_ProfesorOrderByFechaEntregaAsc(tipoContenido.EXAMEN, profesor);
        model.addAttribute("examenesProximos", examenesProximos);

        model.addAttribute("nombreUsuario", nombreUsuario);

        return "paginaProfesor";
    }


    @GetMapping("/profesor/editar")
    public String mostrarFormularioEditarProfesor(HttpSession session, Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        profesores profesor = profesorRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        model.addAttribute("profesor", profesor);

        return "editarProfesor";
    }



    @PostMapping("/profesor/editar")
    public String editarPerfilProfesor(
            HttpSession session,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String nombreUsuario,
            @RequestParam(required = false) String conn
    ) {

        String usuarioSesion = (String) session.getAttribute("nombreUsuario");

        profesores profesor = profesorRepositorio.findByNombreUsuario(usuarioSesion)
                .orElse(null);

        // Actualizar datos
        profesor.setNombre(nombre);
        profesor.setEmail(email);
        profesor.setNombreUsuario(nombreUsuario);

        if (conn != null && !conn.isBlank()) {
            String passwordHasheada = passwordEncoder.encode(conn);
            profesor.setConn(passwordHasheada);
        }

        profesorRepositorio.save(profesor);

        // Actualizar sesion si cambia el nombre de usuario
        session.setAttribute("nombreUsuario", nombreUsuario);

        return "redirect:/mostrarPaginaProfesores";
    }



    @GetMapping("/profesor/confirmarEliminar")
    public String confirmarEliminarProfesor(HttpSession session, Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        profesores profesor = profesorRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        model.addAttribute("titulo", "¿Desea eliminar su cuenta?");
        model.addAttribute("mensaje", "Esta acción no se puede deshacer. ¿Seguro que deseas eliminar tu cuenta?");
        model.addAttribute("accion", "/profesor/eliminar");
        model.addAttribute("cancelar", "/mostrarPaginaProfesores");

        return "eliminar";
    }



    @PostMapping("/profesor/eliminar")
    public String eliminarProfesor(HttpSession session) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        profesores profesor = profesorRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        // Eliminar asignaturas del profesor
        List<asignaturas> asignaturasDelProfesor = asignaturasRepositorio.findByProfesor(profesor);
        asignaturasRepositorio.deleteAll(asignaturasDelProfesor);

        // Eliminar profesor
        profesorRepositorio.delete(profesor);

        session.invalidate();
        return "redirect:/";
    }

}


