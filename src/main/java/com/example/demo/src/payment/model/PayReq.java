package com.example.demo.src.payment.model;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PayReq {
    @NotNull
    private String merchant_uid;
    @NotNull
    private String imp_uid;
    @NotNull
    private String customer_uid;
}
