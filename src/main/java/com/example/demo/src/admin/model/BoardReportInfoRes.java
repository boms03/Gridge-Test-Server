package com.example.demo.src.admin.model;

import com.example.demo.common.Constant;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.report.entity.Report;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import javax.persistence.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Builder
public class BoardReportInfoRes {

    private Long id;

    private Long boardId;

    private Long userId;

    private String reportContent;

    private String state;
}
