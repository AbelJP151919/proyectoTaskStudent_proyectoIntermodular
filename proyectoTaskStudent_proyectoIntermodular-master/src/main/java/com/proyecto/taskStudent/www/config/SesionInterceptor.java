package com.proyecto.taskStudent.www.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class SesionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        String tipo = (String) session.getAttribute("tipo");

        if (nombreUsuario == null && request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("usuario".equals(c.getName())) {
                    nombreUsuario = c.getValue();
                    session.setAttribute("nombreUsuario", nombreUsuario);
                    break;
                }
            }
        }

        if (nombreUsuario == null) {
            response.sendRedirect("/");
            return false;
        }

        String path = request.getRequestURI();

        //RUTAS DEL USUARIO
        if (path.startsWith("/admin") && !"ADMIN".equals(tipo)) {
            response.sendRedirect("/mostrarFormularioInicio");
            return false;
        }


        //RUTAS DEL PROFESOR
        if ((path.startsWith("/profesor") ||
                path.startsWith("/mostrarPaginaProfesores") ||
                path.startsWith("/asignaturas/profesor") ||
                path.startsWith("/contenido/crear") ||
                path.startsWith("/temas/crear") ||
                path.startsWith("/temas/editar") ||
                path.startsWith("/temas/eliminar"))
                && !"PROFESOR".equals(tipo)) {

            response.sendRedirect("/mostrarFormularioInicio");
            return false;
        }

        // RUTAS DEL ALUMNO
        if ((path.startsWith("/alumno") ||
                path.startsWith("/mostrarPaginaAlumno") ||
                path.startsWith("/asignaturas/alumno"))
                && !"ALUMNO".equals(tipo)) {

            response.sendRedirect("/mostrarFormularioInicio");
            return false;
        }

        return true;
    }
}
