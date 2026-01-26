package com.proyecto.taskStudent.www.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void enviarEmail(String destinatario, String nombre){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Bienvenido a TaskStudent");
        message.setText("Hola " + nombre + ", tu registro se ha completado con exito" );
        message.setFrom("taskstudentapp@gmail.com");
        emailSender.send(message);
    }

    public void enviarEmailEliminar(String destinatario, String nombre){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Cuenta Eliminada");
        message.setText("Hola " + nombre + ", tu cuenta de TaskStudent ha sido eliminada. Si deseas saber más info+, contáctanos" );
        message.setFrom("taskstudentapp@gmail.com");
        emailSender.send(message);
    }

    public void enviarEmailEditar(String destinatario, String nombre, List cambios){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Cuenta Editada");
        message.setText("Hola " + nombre + ", tu cuenta de TaskStudent ha sido Editada. Los campos editados son los siguientes: "+ cambios +" deseas saber más info+, contáctanos" );
        message.setFrom("taskstudentapp@gmail.com");
        emailSender.send(message);
    }
    public void enviarEmailCorreccion(String destinatario, String nombre,String nombreTarea, double nota, String comentarioProfesor){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Correccion de la tarea");
        message.setText("Hola" + nombre + ", la nota de la tarea " + nombreTarea + " es:" + nota + ". El profesor ha puesto el siguiente comentario: " +  comentarioProfesor);
        message.setFrom("taskstudentapp@gmail.com");
        emailSender.send(message);
    }



}
