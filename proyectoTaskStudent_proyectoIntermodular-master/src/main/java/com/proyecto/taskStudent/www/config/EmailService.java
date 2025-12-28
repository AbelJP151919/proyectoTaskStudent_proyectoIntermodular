package com.proyecto.taskStudent.www.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String destinatario, String nombre){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Bienvenido a TaskStudent");
        message.setText("Hola " + nombre + ", tu registro se ha completado con exito" );
        message.setFrom("taskstudentapp@gmail.com");
        emailSender.send(message);


    }


}
