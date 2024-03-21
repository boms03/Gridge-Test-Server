package com.example.demo.src.admin.model;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
public class BoardInfoRes {
    private Long id;

    private String content;

    private String location;

    private String state;

    private String boardState;
}
