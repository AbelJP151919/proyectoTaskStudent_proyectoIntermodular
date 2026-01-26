package com.proyecto.taskStudent.www.controladores;

import com.proyecto.taskStudent.www.config.EmailService;
import com.proyecto.taskStudent.www.modelos.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


//Controlador de usuarios
@Controller
public class ControladorUsuarios {

    @Autowired
    private EmailService emailService;

    @Autowired
    private usuariosRepo usuarioRepository;
    @Autowired
    private alumnoRepo alumnoRepositorio;

    @Autowired
    private asignaturasRepo asignaturasRepositorio;

    @Autowired
    private entregaRepo entregaRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;



    // Para registrar en la pagina
    @PostMapping("/registrar")
    public String registrarUsuario(
            @RequestParam String nombre,
            @RequestParam String nombreUsuario,
            @RequestParam String email,
            @RequestParam String conn,
            @RequestParam String conn2,
            @RequestParam String tipo,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String departamento,
            Model model){


    // Validaciones
        if (nombre == null || nombre.isEmpty() || nombreUsuario == null || nombreUsuario.isEmpty() || email == null || email.isEmpty() || conn == null || conn.isEmpty()) {
            model.addAttribute("mensajeError", "Rellena todo los campos");
            return "error-registro";


        }
        List<usuarios> usuariosRegistrados = usuarioRepository.findByEmail(email);
        if(!usuariosRegistrados.isEmpty()) {
            model.addAttribute("mensajeError", "Este email ya está registrado");
            return "error-registro";
        }

        List<usuarios> usuariosRegistrados2 = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if(!usuariosRegistrados2.isEmpty()) {
            model.addAttribute("mensajeError", "Este Nombre de usuario ya está registrado");
            return "error-registro";
        }

        if (!email.contains("@")) {
            model.addAttribute("mensajeError", "El email debe contener @");
            return "error-registro";
        }

        if(!conn.equals(conn2) || conn.length() < 8){
            model.addAttribute("mensajeError", "La contraseña no es válida");
            return "error-registro";
        }

        String passwordHasheada = passwordEncoder.encode(conn);

        usuarios usuario = switch (tipo) {
            case "alumno" -> new alumnos(nombre, nombreUsuario, email, passwordHasheada, curso);
            case "profesor" -> new profesores(nombre, nombreUsuario, email, passwordHasheada, departamento);
            case "admin" -> new admin(nombre, nombreUsuario, email, passwordHasheada);
            default -> new usuarios(nombre, nombreUsuario, email, passwordHasheada);
        };


        usuarioRepository.save(usuario);

        emailService.enviarEmail(usuario.getEmail(), usuario.getNombre());

        model.addAttribute("mensaje", "Registro guardado correctamente");
        return "usuario-creado";
    }




    //Para iniciar sesion en la pagina
    @PostMapping("/iniciar")
    public String iniciarSesion(
            @RequestParam String nombreUsuario,
            @RequestParam String conn,
            HttpServletResponse response,
            HttpSession session,
            Model model
    ) {
        List<usuarios> lista = usuarioRepository.findByNombreUsuario(nombreUsuario);

        if (lista.isEmpty()) {
            model.addAttribute("error", "Nombre de usuario o contraseña incorrectos");
            return "error-inicio";
        }
        usuarios usuario = lista.get(0);

        if (!passwordEncoder.matches(conn, usuario.getConn())) {
            model.addAttribute("error", "Nombre de usuario o contraseña incorrectos");
            return "error-inicio";
        }

        session.setAttribute("nombreUsuario", nombreUsuario);
        Cookie cookie = new Cookie("usuario", nombreUsuario);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        response.addCookie(cookie);

        if (usuario instanceof admin) {
            session.setAttribute("tipo", "ADMIN");
            session.setAttribute("admin", usuario);
            return "redirect:/admin";
        }
        if (usuario instanceof profesores) {
            session.setAttribute("tipo", "PROFESOR");
            session.setAttribute("profesor", usuario);
            return "redirect:/mostrarPaginaProfesores";
        }
        if (usuario instanceof alumnos) {
            session.setAttribute("tipo", "ALUMNO");
            session.setAttribute("alumno", usuario);
            return "redirect:/mostrarPaginaAlumno";
        }

        return "usuario-iniciado";
    }
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session, HttpServletResponse response) {

        // Invalidar sesión
        session.invalidate();

        // Borrar cookie
        Cookie cookie = new Cookie("usuario", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }


    //PARTE ADMINISTRADOR
    //Para la gestion de usuarios por parte del admin

    //Listar+Buscar
    @GetMapping("/admin")
    public String listarUsuarios(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String filtro,
            Model model ) {

        List<usuarios> listaUsuarios;

        if (busqueda != null && !busqueda.isBlank()) {
            listaUsuarios = usuarioRepository.findByNombreUsuarioContainingIgnoreCase(busqueda);
        } else {
            listaUsuarios = usuarioRepository.findAll();
        }

        if (filtro != null && !filtro.isBlank()) {
            switch (filtro.toLowerCase()) {
                case "alumnos":
                    listaUsuarios = listaUsuarios.stream()
                            .filter(u -> u instanceof alumnos)
                            .toList();
                    break;

                case "profesores":
                    listaUsuarios = listaUsuarios.stream()
                            .filter(u -> u instanceof profesores)
                            .toList();
                    break;

                case "admins":
                    listaUsuarios = listaUsuarios.stream()
                            .filter(u -> u instanceof admin)
                            .toList();
                    break;
            }
        }

        long totalUsuarios = listaUsuarios.size();
        long totalAlumnos = listaUsuarios.stream().filter(u -> u instanceof alumnos).count();
        long totalProfesores = listaUsuarios.stream().filter(u -> u instanceof profesores).count();
        long activosHoy = 0;

        model.addAttribute("listaUsuarios", listaUsuarios);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalAlumnos", totalAlumnos);
        model.addAttribute("totalProfesores", totalProfesores);
        model.addAttribute("activosHoy", activosHoy);

        model.addAttribute("busqueda", busqueda);
        model.addAttribute("filtro", filtro);

        return "paginaAdmin";
    }

    //Eliminar

    @GetMapping("/admin/confirmarEliminar/{id}")
    public String confirmarEliminarUsuario(@PathVariable Long id, Model model) {

        model.addAttribute("titulo", "¿Eliminar Usuario?");
        model.addAttribute("mensaje", "Esta acción no se puede deshacer. ¿Seguro que deseas eliminar este usuario?");
        model.addAttribute("accion", "/admin/eliminar/" + id);
        model.addAttribute("cancelar", "/admin");

        return "eliminar";
    }


    @PostMapping("/admin/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {

        usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        if (usuario instanceof profesores profesor) {
            List<asignaturas> asignaturasDelProfesor = asignaturasRepositorio.findByProfesor(profesor);
            asignaturasRepositorio.deleteAll(asignaturasDelProfesor);
        }

        if (usuario instanceof alumnos alumno) {
            List<entregas> entregasAlumno = entregaRepositorio.findByAlumno(alumno);
            entregaRepositorio.deleteAll(entregasAlumno);

        }



        usuarioRepository.delete(usuario);
        emailService.enviarEmailEliminar(usuario.getEmail(), usuario.getNombre());

        return "redirect:/admin";
    }



    //Editar
    @GetMapping("/admin/confirmarEditar/{id}")
    public String confirmarUsuario(@PathVariable Long id, Model model) {

        usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        model.addAttribute("usuario", usuario);

        return "editarUsuario";
    }

    @PostMapping("/admin/editar/{id}")
    public String editarUsuario(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String nombreUsuario,
            @RequestParam String conn) {

        usuarios usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        List<String> cambios = new ArrayList<>();

        // Nombre
        if (!nombre.equals(usuario.getNombre())) {
            cambios.add("Nombre: '" + usuario.getNombre() + "' → '" + nombre + "'");
            usuario.setNombre(nombre);
        }

        // Email
        if (!email.equals(usuario.getEmail())) {
            cambios.add("Email: '" + usuario.getEmail() + "' → '" + email + "'");
            usuario.setEmail(email);
        }

        // Nombre de usuario
        if (!nombreUsuario.equals(usuario.getNombreUsuario())) {
            cambios.add("Nombre de usuario: '" + usuario.getNombreUsuario() + "' → '" + nombreUsuario + "'");
            usuario.setNombreUsuario(nombreUsuario);
        }

        // Conn
        if (!conn.isBlank() && !conn.equals(usuario.getConn())) {
            cambios.add("Conexión: '" + usuario.getConn() + "' → '" + conn + "'");
            String passwordHasheada = passwordEncoder.encode(conn);
            usuario.setConn(passwordHasheada);
        }

        usuarioRepository.save(usuario);

        // Enviar email solo si hubo cambios
        if (!cambios.isEmpty()) {
            emailService.enviarEmailEditar(
                    usuario.getEmail(),
                    usuario.getNombre(),
                    cambios
            );
        }

        return "redirect:/admin";
    }




    //NOTA: No he puesto añadir usuarios ya que un administrador no se puede encargar de esa tarea especifica, pero si se puede encargar de modificar a los usuarios y eliminarlos por ende.


}
