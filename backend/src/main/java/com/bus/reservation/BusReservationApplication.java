package com.bus.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bus.reservation")
public class BusReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusReservationApplication.class, args);
    }
}
