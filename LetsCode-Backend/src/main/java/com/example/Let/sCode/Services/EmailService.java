package com.example.Let.sCode.Services;

import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private SendGrid sendGrid;

    @Value("${spring.sendgrid.from-email}")
    private String fromEmail;

    public Boolean sendEmail(String toEmail, String subject, String content) {
        Email from = new Email(fromEmail,"Let's Code");
        Email to = new Email(toEmail);
        Content body = new Content("text/plain", content);
        if(subject.equals("Password Reset")||subject.equals("Getting Started with Let's Code Platform")){
             body = new Content("text/html", content);
        }
        Mail mail = new Mail(from, subject, to, body);
        
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sendGrid.api(request);
            System.out.println("Sent");
            return true;
        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public void fun() throws IOException {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("/api_keys");
            request.setBody("{\"name\": \"My API Key\", \"scopes\": [\"mail.send\", \"alerts.create\", \"alerts.read\"]}");
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) { throw ex; }
    }
}
