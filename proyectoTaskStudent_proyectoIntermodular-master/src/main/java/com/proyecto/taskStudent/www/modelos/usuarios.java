package com.proyecto.taskStudent.www.modelos;

import jakarta.persistence.*;


@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class usuarios {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    @Column(nullable = false)
    private String nombre;
    private String conn;

    @Transient
    private String conn2;


    //Constructor Vacio
    public usuarios() {}

    // Constructor con parámetros
    public usuarios(String nombre, String nombreUsuario,String email, String conn) {
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.conn = conn;
    }
    // Getters y Setters


    public Long getId() { return id; }

    @Transient
    public String getTipo() {
        return this.getClass().getSimpleName();
    }


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getConn() { return conn; }
    public void setConn(String conn) { this.conn = conn; }
}