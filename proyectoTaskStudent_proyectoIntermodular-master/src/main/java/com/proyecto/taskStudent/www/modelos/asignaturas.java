package com.proyecto.taskStudent.www.modelos;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asignaturas")
public class asignaturas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;


    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String aula;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int horasTotales;
    private String horario;

    @ManyToOne
    private profesores profesor;

    @ManyToMany(mappedBy = "asignaturas")
    private List<alumnos> alumnos = new ArrayList<>();


    //Relacion en cascada con temas
    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.REMOVE)
    private List<temas> temas;


    @Transient private Long totalTemas;
    @Transient private Long tareasPendientes;
    @Transient private Long proximosExamenes;



    public asignaturas(){}

    public asignaturas(String nombre, String descripcion, String codigo, String aula, LocalDate fechaInicio, LocalDate fechaFin,  int horasTotales, String horario, profesores profesor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.aula = aula;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horasTotales = horasTotales;
        this.horario = horario;
        this.profesor = profesor;
    }
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getAula() {
        return aula;
    }
    public void setAula(String aula) {
        this.aula = aula;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    public int getHorasTotales() {
        return horasTotales;
    }
    public void setHorasTotales(int horasTotales) {
        this.horasTotales = horasTotales;
    }
    public String getHorario() {
        return horario;
    }
    public void setHorario(String horario) {
        this.horario = horario;
    }
    public  profesores getProfesor() {return  profesor;}

    public void setProfesor(profesores profesor) {this.profesor = profesor;}


    public Long getTotalTemas() {
        return totalTemas;
    }

    public void setTotalTemas(Long totalTemas) {
        this.totalTemas = totalTemas;
    }

    public Long getTareasPendientes() {
        return tareasPendientes;
    }

    public void setTareasPendientes(Long tareasPendientes) {
        this.tareasPendientes = tareasPendientes;
    }

    public Long getProximosExamenes() {
        return proximosExamenes;
    }

    public void setProximosExamenes(Long proximosExamenes) {
        this.proximosExamenes = proximosExamenes;
    }

}

