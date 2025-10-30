package com.milano.milanopastas.service;

import com.milano.milanopastas.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    // 👇 dirección válida de la fábrica o del administrador
    private static final String FACTORY_EMAIL = "matiascamacho454@gmail.com";

    public void notifyNewOrder(Order order) {
        // correo a cliente
        sendMail(order.getBuyerEmail(),
                "Confirmación de tu pedido #" + order.getId(),
                "Hola " + order.getBuyerName() + ",\n\n" +
                        "Recibimos tu pedido correctamente. Te avisaremos cuando esté listo.\n\n" +
                        "Número de pedido: " + order.getId() + "\nTotal: $" + order.getTotal() + "\n\n" +
                        "Gracias por comprar en Milano Pastas 🍝");

        // correo a la fábrica
        sendMail(FACTORY_EMAIL,
                "Nuevo pedido #" + order.getId(),
                "Nuevo pedido recibido de " + order.getBuyerName() +
                        " (" + order.getBuyerEmail() + ")\n\nTotal: $" + order.getTotal());
    }

    private void sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("📧 Enviado a " + to);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo: " + e.getMessage());
        }
    }
}
