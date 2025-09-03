package com.bus.reservation.service;

import com.bus.reservation.entity.Payment;
import com.bus.reservation.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment save(Payment payment) { return paymentRepository.save(payment); }
    public Optional<Payment> findById(Long id) { return paymentRepository.findById(id); }
    public List<Payment> findAll() { return paymentRepository.findAll(); }
    public void deleteById(Long id) { paymentRepository.deleteById(id); }
    public Payment update(Long id, Payment updated) { updated.setId(id); return paymentRepository.save(updated); }
}

