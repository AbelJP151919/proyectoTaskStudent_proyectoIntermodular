package com.proyecto.taskStudent.www.modelos;



//Enum Para enumerar los tipos de contenido, que en este caso pueden ser 4
public enum tipoContenido {
    TAREA("📝"),
    APUNTE("📄"),
    EXAMEN("🧪"),
    RECURSO("📎");

    private final String icono;

    tipoContenido(String icono) {
        this.icono = icono;
    }

    public String getIcono() {
        return icono;
    }
}

