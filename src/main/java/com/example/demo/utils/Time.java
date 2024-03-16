package com.example.demo.utils;

import com.example.demo.src.board.entity.Board;
import com.example.demo.src.comment.entity.Comment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {
    public static String formatPostTime (Object object) {

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime postTime;

        if (object instanceof Board){
            postTime = ((Board)object).getCreatedAt();
        } else {
            postTime = ((Comment)object).getCreatedAt();
        }

        Duration duration = Duration.between(postTime, currentTime);
        long hours = duration.toHours();

        if (hours < 1) {
            long minutes = duration.toMinutes();
            return (minutes < 1) ? "방금" : minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else if (hours < 24 * 30) {
            long days = hours / 24;
            return days + "일 전";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월dd일");
            return postTime.format(formatter);
        }
    }
}
