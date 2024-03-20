package com.example.demo.src.payment.model;

import lombok.Getter;

@Getter
public class PayReq {
    private String merchant_uid;
    private String imp_uid;
    private String customer_uid;
}
