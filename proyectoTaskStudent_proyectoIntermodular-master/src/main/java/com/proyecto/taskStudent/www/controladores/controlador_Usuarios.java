package com.proyecto.taskStudent.www.controladores;

import com.proyecto.taskStudent.www.modelos.usuarios;
import com.proyecto.taskStudent.www.modelos.usuariosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class controlador_Usuarios{

    @Autowired
    private usuariosRepo usuarioRepository;

    @GetMapping("/mostrarFormularioRegistro")

    public String mostrarRegistro() {
        return "formularioRegistros";
    }



    @PostMapping("/registrar")
    public String registrarUsuario(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String curso,
            @RequestParam String conn,
            @RequestParam String conn2,
            Model model){



        usuarios usuario = new usuarios(nombre,email,curso,conn,conn2);

        if(usuario.validarCamposVacios()==false) {
            model.addAttribute("mensajeError", "Rellena todo los campos");
            return "error-registro";
        }

        List<usuarios> usuariosRegistrados = usuarioRepository.findByEmail(email);
        if(!usuariosRegistrados.isEmpty()) {
            model.addAttribute("mensajeError", "Este email ya está registrado");
            return "error-registro";
        }

        if(usuario.validarEmail()==false) {
            model.addAttribute("mensajeError", "El email debe contener @");
            return "error-registro";
        }

        if(usuario.validarConn()==false) {
            model.addAttribute("mensajeError", "La contraseña no es válida");
            return "error-registro";
        }


        usuarioRepository.save(usuario);
        return "redirect:/mostrarFormularioRegistro";

    }
    }
