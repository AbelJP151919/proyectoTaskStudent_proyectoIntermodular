package com.proyecto.taskStudent.www.modelos;


import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "alumnos")
public class alumnos extends usuarios {

    private String curso;





    @ManyToMany
    @JoinTable(
            name = "alumnos_asignaturas",
            joinColumns = @JoinColumn(name = "alumno_id"),
            inverseJoinColumns = @JoinColumn(name = "asignatura_id")
    )
    private List<asignaturas> asignaturas;

    public alumnos() {}

    public alumnos(String nombre, String nombre_usuario, String email, String conn, String curso) {
        super(nombre, nombre_usuario, email, conn);
        this.curso = curso;
    }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public List<asignaturas> getAsignaturas() { return asignaturas; }
    public void setAsignaturas(List<asignaturas> asignaturas) {this.asignaturas = asignaturas;}

}
