package com.example.demo.src.order.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PurchaseReq {
    private String merchantUid;
    private String customerUid;
    private String buyerName;
    private BigDecimal amount;
    private String payMethod;
    private String phone;
}
