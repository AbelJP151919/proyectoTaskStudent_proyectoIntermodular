package com.proyecto.taskStudent.www.controladores;

import com.proyecto.taskStudent.www.modelos.usuarios;
import com.proyecto.taskStudent.www.modelos.usuariosRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class controlador_Alumnos {

    @GetMapping("/mostrarPaginaAlumno")

    public String mostrarPagina() {
        return "paginaAlumno";
    }
}
