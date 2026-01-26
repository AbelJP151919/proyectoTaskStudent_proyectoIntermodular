package com.proyecto.taskStudent.www.modelos;


import jakarta.persistence.*;



//Clase Profesores
@Entity
@Table(name = "profesores")
public class profesores extends usuarios {

    private String departamento;

    public profesores() {}

    public profesores(String nombre, String nombreUsuario, String email, String conn, String departamento) {
        super(nombre, nombreUsuario, email, conn);
        this.departamento = departamento;
    }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}
