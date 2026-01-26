package com.proyecto.taskStudent.www.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ControladorAdmin {
    @GetMapping("/mostrarPaginaAdmin")
    public String redirect() {
        return "redirect:/admin";
    }


}
