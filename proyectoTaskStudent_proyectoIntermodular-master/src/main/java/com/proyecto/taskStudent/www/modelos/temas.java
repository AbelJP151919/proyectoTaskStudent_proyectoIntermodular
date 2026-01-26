package com.proyecto.taskStudent.www.modelos;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "temas")
public class temas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private int orden; // Para ordenar los temas dentro de la asignatura


    //Relacion con la tabla Asignaturas
    @ManyToOne
    @JoinColumn(name = "asignatura_id")
    private asignaturas asignatura;


    //Relacion en cascada con la tabla contenidos
    @OneToMany(mappedBy = "tema", cascade = CascadeType.REMOVE)
    private List<contenido> contenidos = new ArrayList<>();


    public temas() {}

    public temas(String nombre, String descripcion, int orden, asignaturas asignatura) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.orden = orden;
        this.asignatura = asignatura;
    }
    public Long getId() {return id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public int getOrden() {return orden;}
    public void setOrden(int orden) {this.orden = orden;}
    public asignaturas getAsignatura() {return asignatura;}
    public List<contenido> getContenidos() {return contenidos;}

    // Getters y setters
}
