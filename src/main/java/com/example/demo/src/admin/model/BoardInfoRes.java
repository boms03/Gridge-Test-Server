package com.example.demo.src.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Builder
public class BoardInfoRes {
    private Long id;

    private String username;

    private String content;

    private String location;

    private String state;

    private String boardState;

    private LocalDateTime createdAt;
}
