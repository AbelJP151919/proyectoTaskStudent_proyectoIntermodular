package com.proyecto.taskStudent.www.modelos;


import jakarta.persistence.*;


@Entity
@Table(name = "alumnos")
public class alumnos extends usuarios {

    private String curso;

    public alumnos() {}

    public alumnos(String nombre, String nombre_usuario, String email, String conn, String curso) {
        super(nombre, nombre_usuario, email, conn);
        this.curso = curso;
    }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
}
