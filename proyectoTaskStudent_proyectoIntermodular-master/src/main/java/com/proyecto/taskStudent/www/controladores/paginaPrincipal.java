package com.proyecto.taskStudent.www.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class paginaPrincipal {

    @GetMapping("/")
    public String index() {
        return "paginaPrincipal";
    }
}