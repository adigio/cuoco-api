package com.cuoco.adapter.out.email;


public interface EmailService {
    void sendConfirmationEmail(String to, String confirmationLink);
}
