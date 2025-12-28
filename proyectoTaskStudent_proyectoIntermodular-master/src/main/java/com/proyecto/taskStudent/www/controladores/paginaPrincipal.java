package com.proyecto.taskStudent.www.controladores;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class paginaPrincipal {
    @GetMapping("/")

    public String mostrarPaginaPrincipal() {
        return "paginaPrincipal";
    }

    @GetMapping("/mostrarFormularioRegistro")

    public String mostrarRegistro() {
        return "formularioRegistros";
    }

    @GetMapping("/mostrarFormularioInicio")
    public String mostrarInicio() {
        return "iniciarSesion";
    }


}
