package com.mx.core.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGeneric<T> {

    private boolean success;
    private String message;
    private T data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public static <T> ResponseGeneric<T> buildSuccess(String message, T data) {
        return new ResponseGeneric<>(true, message, data, LocalDateTime.now());
    }

    public static <T> ResponseGeneric<T> buildSuccess(boolean success, String message, T data) {
        return new ResponseGeneric<>(success, message, data, LocalDateTime.now());
    }

    public static <T> ResponseGeneric<T> buildError(String message) {
        return new ResponseGeneric<>(false, message, null, LocalDateTime.now());
    }
    
}