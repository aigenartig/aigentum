package com.cloudwebshop.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ErrorDto {
    private UUID errorId;
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
