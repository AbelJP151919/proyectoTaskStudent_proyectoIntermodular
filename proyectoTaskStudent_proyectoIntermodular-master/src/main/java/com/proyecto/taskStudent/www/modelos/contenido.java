package com.proyecto.taskStudent.www.modelos;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class contenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    private LocalDate fechaEntrega;

    private tipoContenido tipo;

    private String rutaArchivo;
    private String nombreArchivo;



    //Relacion con la tabla de temas
    @ManyToOne
    @JoinColumn(name = "tema_id")
    private temas tema;

    public contenido() {}

    public contenido(String titulo, String descripcion, LocalDate fechaEntrega, tipoContenido tipo, temas tema) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaEntrega = fechaEntrega;
        this.tipo = tipo;
        this.tema = tema;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public tipoContenido getTipo() { return tipo; }
    public void setTipo(tipoContenido tipo) { this.tipo = tipo; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public temas getTema() { return tema; }
    public void setTema(temas tema) { this.tema = tema; }
}


