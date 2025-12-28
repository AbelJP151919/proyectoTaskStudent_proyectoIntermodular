package com.proyecto.taskStudent.www.modelos;

import jakarta.persistence.*;


@Entity
@Table(name = "usuarios")
public class usuarios {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nombre;
    private String puesto;
    private String curso;
    private String conn;

    @Transient
    private String conn2;


    //Constructor Vacio
    public usuarios() {}

    // Constructor con parámetros
    public usuarios(String nombre, String email,String puesto, String curso , String conn,  String conn2) {
        this.nombre = nombre;
        this.email = email;
        this.puesto = puesto;
        this.curso = curso;
        this.conn = conn;
        this.conn2 = conn2;

    }


    // Getters y Setters


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getConn() { return conn; }
    public void setConn(String conn) { this.conn = conn; }

    public String getConn2() { return conn2; }
    public void setConn2(String conn2) { this.conn2 = conn2; }



    //Validaciones

    public Boolean validarCamposVacios() {
        if (nombre == null || nombre.isEmpty() || email == null || email.isEmpty() || puesto == null || puesto.isEmpty() ||curso == null || curso.isEmpty() || conn == null || conn.isEmpty() || conn2 == null || conn2.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean validarEmail() {
        if (!email.contains("@")) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean validarConn(){
        if(conn.equals(conn2) && conn.length() >= 8){
            return true;
        } else {
            return false;
        }
    }




}