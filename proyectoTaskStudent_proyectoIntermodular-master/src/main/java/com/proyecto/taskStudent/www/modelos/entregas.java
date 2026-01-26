package com.proyecto.taskStudent.www.modelos;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "entregas")
public class entregas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Alumno que entrega la tarea
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id")
    private alumnos alumno;

    // Contenido de tipo TAREA
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarea_id")
    private contenido tarea;

    // Fecha en la que el alumno entrega
    private LocalDateTime fechaEntrega;

    // Archivo subido
    private String archivoNombre;

    private String archivoRuta;

    // Comentario opcional del alumno
    @Column(columnDefinition = "TEXT")
    private String comentario;

    // Nota que pone el profesor
    private Double nota;

    // Si el profesor ya la revisó
    private Boolean revisada = false;

    public entregas() {}

    // Getters y setters
    public Long getId() {return id;}

    public alumnos getAlumno() {return alumno;}

    public void setAlumno(alumnos alumno) {this.alumno = alumno;}

    public contenido getTarea() {return tarea;}

    public void setTarea(contenido tarea) {this.tarea = tarea;}

    public LocalDateTime getFechaEntrega() {return fechaEntrega;}

    public void setFechaEntrega(LocalDateTime fechaEntrega) {this.fechaEntrega = fechaEntrega;}

    public String getArchivoNombre() {return archivoNombre;}

    public void setArchivoNombre(String archivoNombre) {this.archivoNombre = archivoNombre;}

    public String getArchivoRuta() {return archivoRuta;}

    public void setArchivoRuta(String archivoRuta) {this.archivoRuta = archivoRuta;}

    public String getComentario() {return comentario;}

    public void setComentario(String comentario) {this.comentario = comentario;}

    public Double getNota() {return nota;}

    public void setNota(Double nota) {this.nota = nota;}

    public Boolean getRevisada() {return revisada;}

    public void setRevisada(Boolean revisada) {this.revisada = revisada;}
}

