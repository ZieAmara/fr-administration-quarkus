import java.nio.charset.StandardCharsets;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import dto.MailDto;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class MyMessageConsumer {
    
    @Inject
    private Mailer mailer;

    
    @Incoming("fr_admin_message_queue")
    public void consumeMessage(byte[] rmqMessage) {
        // Logique de traitement du message
        String rmqMessageAsString = new String(rmqMessage, StandardCharsets.UTF_8);
        System.out.println("\n#################################### \n\nReceived message: " + rmqMessageAsString);
        

        JsonObject jsonobject_data = new JsonObject(new String(rmqMessageAsString));
        JsonObject data = jsonobject_data.getJsonObject("data");

        System.out.println(data.toString());

        MailDto mailDto = dataToMailDto(data);
        Mail [] mails = listMails(mailDto);
        
        Runnable ran = ()->{
            try {
                for (Mail mail : mails) {
                    mailer.send(mail);
                }
            } catch (Exception e) {
                System.out.println("echec d'envoie de mail au adresse email : " + mailDto.getMailsDestinataires());
                e.printStackTrace();
            }
        };

        Thread td = new Thread(ran);
        td.start();

    }


    private Mail [] listMails(MailDto mailDto) {

        String from = mailDto.getMailExpeditor();
        String subject = mailDto.getObject();
        String message = mailDto.getContent();

        int length = mailDto.getMailsDestinataires().length;
        Mail[] mails = new Mail[length];
    
        int i = 0;
        for (String email : mailDto.getMailsDestinataires()) {
            System.out.println("Sending email to: " + email);
            String to = email;
            Mail mail = Mail.withText(to, subject, message);
            mail.setFrom(from);
            mails[i] = mail;
            i++;
        }

        return mails;

    }


    public MailDto dataToMailDto(JsonObject data){

        String mailExpeditor = data.getString("mailExpeditor");
        JsonArray mailsDestinataires_JsonArray = data.getJsonArray("mailsDestinataires");
        String[] mailsDestinataires = new String[mailsDestinataires_JsonArray.size()];
        for (int i = 0; i < mailsDestinataires_JsonArray.size(); i++) {
            mailsDestinataires[i] = mailsDestinataires_JsonArray.getString(i);
        }
        String object = data.getString("object");
        String content = data.getString("content");
        
        return new MailDto(mailExpeditor, mailsDestinataires, object, content);

    }

}
