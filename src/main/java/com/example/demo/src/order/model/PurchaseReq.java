package com.example.demo.src.order.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
public class PurchaseReq {
    @NotNull
    private String merchantUid;
    @NotNull
    private String customerUid;
    @NotNull
    private String buyerName;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String payMethod;
    @NotNull
    private String phone;
    @NotNull
    private boolean success;
}
