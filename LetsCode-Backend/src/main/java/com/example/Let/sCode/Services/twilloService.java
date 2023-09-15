package com.example.Let.sCode.Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class twilloService {
    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String phoneNumber;

    public String sendMessage(String toPhoneNumber, String messageBody) {
        Twilio.init(accountSid, authToken);
        PhoneNumber from = new PhoneNumber(phoneNumber);
        PhoneNumber to = new PhoneNumber(toPhoneNumber);
        try {
        Message message = Message.creator(to, from, messageBody).create();
        System.out.println("Message SID: " + message.getSid());
            return message.getSid();
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    public void addNumber() {
        String apiUrl = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/OutgoingCallerIds.json";
        String phoneNumber = "+919550811947";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(apiUrl);
            
            String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((accountSid + ":" + authToken).getBytes());
            httpPost.setHeader("Authorization", authHeader);

            // Set the phone number in the request
            StringEntity entity = new StringEntity("PhoneNumber=" + phoneNumber);
            entity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(entity);

            // Make the API request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            // Check the response
            if (response.getStatusLine().getStatusCode() == 201) {
                System.out.println("Caller ID added successfully.");
            } else {
                System.out.println("Failed to add caller ID: " + EntityUtils.toString(responseEntity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}


