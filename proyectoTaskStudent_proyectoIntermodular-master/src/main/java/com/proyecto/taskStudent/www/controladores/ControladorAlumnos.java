package com.proyecto.taskStudent.www.controladores;

import com.proyecto.taskStudent.www.modelos.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


// Controlador de los Alumnos
@Controller
public class ControladorAlumnos {

    @Autowired
    private alumnoRepo alumnosRepositorio;

    @Autowired
    private asignaturasRepo asignaturasRepositorio;

    @Autowired
    private contenidoRepo contenidoRepositorio;

    @Autowired
    private temasRepo temasRepositorio;

    @Autowired
    private entregaRepo entregaRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/mostrarPaginaAlumno")
    public String verAsignaturasAlumno(HttpSession session,
                                       Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");


        alumnos alumno = alumnosRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        List<asignaturas> asignaturas = alumno.getAsignaturas();

        for (asignaturas asig : asignaturas) {
            long totalTemas = temasRepositorio.contarTemasPorAsignatura(asig.getId());
            long tareasPendientes = contenidoRepositorio.contarTareasPorAsignatura(asig.getId());
            long examenesProximos = contenidoRepositorio.contarExamenesPorAsignatura(asig.getId());

            asig.setTotalTemas(totalTemas);
            asig.setTareasPendientes(tareasPendientes);
            asig.setProximosExamenes(examenesProximos);
        }

        model.addAttribute("listaAsignaturas", asignaturas);

        return "paginaAlumno";
    }

    @GetMapping("/mostrarPerfilAlumno")
    public String mostrarPerfil(HttpSession session,
                                HttpServletRequest request,
                                Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        alumnos alumno = alumnosRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);



        model.addAttribute("alumno", alumno);

        return "paginaPerfilAlumno";
    }


    @GetMapping("/alumno/unirseAsignatura")
    public String mostrarFormularioUnirse() {

        return "mostrarUnirseAsignatura";
    }


    @PostMapping("/alumno/unirse")
    public String unirseAsignatura(HttpSession session,
                                   HttpServletRequest request,
                                   Model model,
                                   @RequestParam String codigo) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");


        alumnos alumno = alumnosRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        asignaturas asignatura = asignaturasRepositorio.findByCodigo(codigo)
                .orElse(null);

        if (asignatura == null) {
            model.addAttribute("mensajeError", "No existe ninguna asignatura con ese código");
            return "mostrarUnirseAsignatura";
        }

        if (alumno.getAsignaturas().contains(asignatura)) {
            model.addAttribute("mensajeError", "Ya estás inscrito en esta asignatura");
            return "mostrarUnirseAsignatura";
        }

        alumno.getAsignaturas().add(asignatura);
        alumnosRepositorio.save(alumno);

        return "redirect:/mostrarPaginaAlumno";
    }
    @GetMapping("/alumno/editar")
    public String mostrarFormularioEditarAlumno(HttpSession session, Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        alumnos alumno = alumnosRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        model.addAttribute("alumno", alumno);

        return "editarAlumno";
    }



    @PostMapping("/alumno/editar")
    public String editarPerfilAlumno(
            HttpSession session,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String nombreUsuario,
            @RequestParam(required = false) String conn
    ) {

        String usuarioSesion = (String) session.getAttribute("nombreUsuario");

        alumnos alumno = alumnosRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        // Actualizar datos
        alumno.setNombre(nombre);
        alumno.setEmail(email);
        alumno.setNombreUsuario(nombreUsuario);

        if (conn != null && !conn.isBlank()) {
            String passwordHasheada = passwordEncoder.encode(conn);

            alumno.setConn(passwordHasheada);
        }

        alumnosRepositorio.save(alumno);

        // Actualizar sesion si cambia el nombre de usuario
        session.setAttribute("nombreUsuario", nombreUsuario);

        return "redirect:/mostrarPaginaAlumno";
    }
    @GetMapping("/alumno/confirmarEliminar")
    public String confirmarEliminarAlumno(HttpSession session, Model model) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        alumnos alumno = alumnosRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        model.addAttribute("titulo", "¿Desea eliminar su cuenta?");
        model.addAttribute("mensaje", "Esta acción no se puede deshacer. ¿Seguro que deseas eliminar tu cuenta?");
        model.addAttribute("accion", "/alumno/eliminar");
        model.addAttribute("cancelar", "/mostrarPaginaAlumno");

        return "eliminar";
    }



    @PostMapping("/alumno/eliminar")
    public String eliminarAlumno(HttpSession session) {

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        alumnos alumno = alumnosRepositorio.findByNombreUsuario(nombreUsuario)
                .orElse(null);

        if (alumno == null) {
            return "redirect:/";
        }

        Long idAlumno = alumno.getId();

        entregaRepositorio.deleteByAlumnoId(idAlumno);

        alumnosRepositorio.delete(alumno);

        session.invalidate();

        return "redirect:/";
    }



}
