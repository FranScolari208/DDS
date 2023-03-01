package dds2022.grupo1.HuellaDeCarbono.entidades.Medicion;
import dds2022.grupo1.HuellaDeCarbono.Interfaces.MedioDeNotificacion;
import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.Agente;
import dds2022.grupo1.HuellaDeCarbono.exceptions.NoImplementadoException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mailer implements MedioDeNotificacion {

        public static void enviar(String asunto, Agente agente) throws NoImplementadoException {

           final String contacto= String.valueOf(agente.getEmail());
           final String body = String.valueOf(agente.getSector().calcularHC());
            final String username = "ceciliarocca2@gmail.com";
            final String password = "kosezjvwolklkvdn";

            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(prop,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("ceciliarocca2@gmail.com"));//este seria el mail del sistema
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(contacto)
                );
                //Asunto
                message.setSubject(asunto);
                //Body
                message.setText(body);

                Transport.send(message);

                System.out.println("Done");

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }



}

