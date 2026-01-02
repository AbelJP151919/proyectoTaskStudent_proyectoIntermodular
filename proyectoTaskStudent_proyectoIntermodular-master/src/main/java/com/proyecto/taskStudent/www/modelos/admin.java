package com.proyecto.taskStudent.www.modelos;


import jakarta.persistence.*;


@Entity
@Table(name = "admins")
public class admin extends usuarios {

    public admin() {}

    public admin(String nombre, String nombre_usuario, String email, String conn) {
        super(nombre, nombre_usuario, email, conn);
    }
}

