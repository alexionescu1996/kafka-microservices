package com.example.model;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentEvent {
    private String orderId;
    private PaymentStatus status;
    private Date timestamp;

}
