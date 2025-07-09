package com.cuoco.adapter.in.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentResultViewController {

    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("title", "¡Pago exitoso!");
        model.addAttribute("message", message != null ? message : "¡Felicitaciones! Tu pago fue procesado exitosamente.");
        model.addAttribute("color", "#FF6F61");
        return "payment-result";
    }

    @GetMapping("/payment/failure")
    public String paymentFailure(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("title", "Pago fallido");
        model.addAttribute("message", message != null ? message : "Hubo un problema procesando tu pago.");
        model.addAttribute("color", "#B22222");
        return "payment-result";
    }

    @GetMapping("/payment/pending")
    public String paymentPending(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("title", "Pago pendiente");
        model.addAttribute("message", message != null ? message : "Tu pago está siendo procesado.");
        model.addAttribute("color", "#FFA500");
        return "payment-result";
    }
}
