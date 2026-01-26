package com.proyecto.taskStudent.www.controladores;
import com.proyecto.taskStudent.www.modelos.*;
import com.proyecto.taskStudent.www.servicios.TemaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


// Controlador de los temas
@Controller
@RequestMapping("/temas")
public class ControladorTemas {


    // Se llama a los servicios de los temas, para posteriormente llamar a cada funcion
    @Autowired
    private TemaServicio temaService;

    @Autowired
    private asignaturasRepo asignaturasRepositorio;

    @Autowired
    private temasRepo temasRepositorio;

    @GetMapping("/crear/{idAsignatura}")
    public String mostrarFormularioCrear(@PathVariable Long idAsignatura,
                                         Model model) {
        asignaturas asignatura = asignaturasRepositorio.findById(idAsignatura)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));

        model.addAttribute("asignatura", asignatura);
        return "crearTemas";
    }


    @PostMapping("/crear/{idAsignatura}")
    public String crearTema(@PathVariable Long idAsignatura,
                            @RequestParam String nombre,
                            @RequestParam String descripcion,
                            @RequestParam(required = false) Integer orden,
                            Model model) {

        asignaturas asignatura = asignaturasRepositorio.findById(idAsignatura)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));

        //validaciones

        //Campos obligatorios
        if (nombre == null || nombre.isBlank() ||
                descripcion == null || descripcion.isBlank()) {

            model.addAttribute("mensajeError", "Debes rellenar todos los campos obligatorios");
            return "error-Tema";
        }

        //Longitud minima
        if (nombre.length() < 3) {
            model.addAttribute("mensajeError", "El nombre del tema debe tener al menos 3 caracteres");
            return "error-Tema";
        }

        if (descripcion.length() < 10) {
            model.addAttribute("mensajeError", "La descripción debe tener al menos 10 caracteres");
            return "error-Tema";
        }

        //Validar orden
        if (orden != null && orden < 0) {
            model.addAttribute("mensajeError", "El orden no puede ser un número negativo");
            return "error-Tema";
        }

        //Evitar temas duplicados dentro de la misma asignatura
        if (temasRepositorio.existsByNombreAndAsignatura(nombre, asignatura)) {
            model.addAttribute("mensajeError", "Ya existe un tema con ese nombre en esta asignatura");
            return "error-Tema";
        }

        temaService.crearTema(nombre, descripcion, orden, asignatura);

        return "redirect:/asignaturas/" + idAsignatura;
    }




    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                          Model model) {
        temas tema = temaService.obtenerTema(id);

        model.addAttribute("tema", tema);
        model.addAttribute("asignatura", tema.getAsignatura());

        return "editarTema";
    }


    @PostMapping("/editar/{id}")
    public String editarTema(@PathVariable Long id,
                             @RequestParam String nombre,
                             @RequestParam String descripcion,
                             @RequestParam(required = false) Integer orden) {

        temas tema = temaService.editarTema(id, nombre, descripcion, orden);

        return "redirect:/asignaturas/" + tema.getAsignatura().getId();
    }



    @GetMapping("/eliminar/{id}")
    public String mostrarFormularioEliminar(@PathVariable Long id,
                                            Model model) {

        temas tema = temaService.obtenerTema(id);

        model.addAttribute("titulo", "¿Eliminar Tema?");
        model.addAttribute("mensaje", "Esta acción no se puede deshacer. ¿Seguro que deseas eliminar el tema?");
        model.addAttribute("accion", "/temas/eliminar/" + id);
        model.addAttribute("cancelar", "/asignaturas/" + tema.getAsignatura().getId());

        return "eliminarTema";
    }



    @PostMapping("/eliminar/{id}")
    public String eliminarTema(@PathVariable Long id) {

        temas tema = temaService.obtenerTema(id);
        Long idAsignatura = tema.getAsignatura().getId();

        temaService.eliminarTema(id);

        return "redirect:/asignaturas/" + idAsignatura;
    }
}
