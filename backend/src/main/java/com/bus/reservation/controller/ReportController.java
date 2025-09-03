package com.bus.reservation.controller;

import com.bus.reservation.entity.Payment;
import com.bus.reservation.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final PaymentService paymentService;

    public ReportController(PaymentService paymentService) { this.paymentService = paymentService; }

    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> sales() {
        List<Payment> payments = paymentService.findAll();
        double total = payments.stream()
                .filter(p -> p.getAmount() != null && "SUCCESS".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();
        Map<String, Object> res = new HashMap<>();
        res.put("totalSales", total);
        res.put("successfulPayments", payments.stream().filter(p -> "SUCCESS".equalsIgnoreCase(p.getStatus())).count());
        res.put("allPayments", payments.size());
        return ResponseEntity.ok(res);
    }
}
