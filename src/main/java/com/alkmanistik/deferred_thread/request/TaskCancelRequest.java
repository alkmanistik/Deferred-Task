package com.alkmanistik.deferred_thread.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на отмену задачи")
public class TaskCancelRequest {

    @NotBlank(message = "Category must not be blank")
    @Size(max = 50, message = "Category must be less than 50 characters")
    @Schema(description = "Категория задачи", example = "email")
    private String category;

}
