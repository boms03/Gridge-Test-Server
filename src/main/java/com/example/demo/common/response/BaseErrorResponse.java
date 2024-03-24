package com.example.demo.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message",})
public class BaseErrorResponse {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess = false;
    private final String message;
    private final int code;
}
