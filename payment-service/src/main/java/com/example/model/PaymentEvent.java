package com.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
public class PaymentEvent {
    private String orderId;
    private PaymentStatus status;
    private Date timestamp;
}
