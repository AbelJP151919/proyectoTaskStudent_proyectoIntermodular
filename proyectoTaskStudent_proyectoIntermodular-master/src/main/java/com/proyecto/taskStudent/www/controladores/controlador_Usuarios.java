package com.proyecto.taskStudent.www.controladores;

import com.proyecto.taskStudent.www.config.EmailService;
import com.proyecto.taskStudent.www.modelos.usuarios;
import com.proyecto.taskStudent.www.modelos.usuariosRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.proyecto.taskStudent.www.modelos.admin;
import com.proyecto.taskStudent.www.modelos.alumnos;
import com.proyecto.taskStudent.www.modelos.profesores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;




@Controller
public class controlador_Usuarios{

    @Autowired
    private EmailService emailService;

    @Autowired
    private usuariosRepo usuarioRepository;





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
        usuarios usuario;

        switch (tipo) {
            case "alumno": usuario = new alumnos(nombre, nombreUsuario, email, conn, curso); break;
            case "profesor": usuario = new profesores(nombre, nombreUsuario, email, conn, departamento); break;
            default: usuario = new usuarios(nombre, nombreUsuario, email, conn); break;
        }


        usuarioRepository.save(usuario);

        emailService.sendEmail(usuario.getEmail(), usuario.getNombre());

        model.addAttribute("mensaje", "Registro guardado correctamente");
        return "usuario-creado";
    }

    @PostMapping("/iniciar")
    public String iniciarSesion(
            @RequestParam String nombreUsuario,
            @RequestParam String conn,
            HttpSession session,
            Model model
            ){
        usuarios usuario = usuarioRepository.findByNombreUsuarioAndConn(nombreUsuario,conn);



        if (usuario == null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
            return "error-registro";
        }
        session.setAttribute("usuario", usuario);


        if (usuario instanceof admin) {
            return "redirect:/admin";
        } if (usuario instanceof profesores) {
            return "redirect:/mostrarPaginaProfesores";
        } if (usuario instanceof alumnos) {
            return "redirect:/mostrarPaginaAlumno";
        }

        model.addAttribute("mensaje", "Registro guardado correctamente");
        return "usuario-creado";
    }

    @GetMapping("/admin")
    public String listarUsuarios(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String filtro,
            Model model ) {

        System.out.println(">>> ENTRANDO A /admin");

        List<usuarios> listaUsuarios = usuarioRepository.findAll();

        System.out.println(">>> Usuarios encontrados: " + listaUsuarios.size());

        long totalUsuarios = listaUsuarios.size();
        long totalAlumnos = listaUsuarios.stream().filter(u -> u instanceof alumnos).count();
        long totalProfesores = listaUsuarios.stream().filter(u -> u instanceof profesores).count();
        long activosHoy = 0;

        model.addAttribute("listaUsuarios", listaUsuarios);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalAlumnos", totalAlumnos);
        model.addAttribute("totalProfesores", totalProfesores);
        model.addAttribute("activosHoy", activosHoy);

        return "paginaAdmin";
    }


}
