package com.alkmanistik.deferred_thread.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCancelRequest {

    @NotBlank(message = "Category must not be blank")
    @Size(max = 50, message = "Category must be less than 50 characters")
    private String category;

}
