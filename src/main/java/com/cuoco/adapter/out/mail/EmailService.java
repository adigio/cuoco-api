package com.cuoco.adapter.out.mail;


public interface EmailService {
    void sendConfirmationEmail(String to, String confirmationLink);
}
