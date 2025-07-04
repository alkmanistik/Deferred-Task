package com.alkmanistik.deferred_thread.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private String field;
    private String message;
    private Object rejectedValue;
}