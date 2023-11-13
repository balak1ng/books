package com.balakin.books.web.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class BookDto {

    @Null
    private Long id;

    @NotBlank
    private String author;

    @NotBlank
    private String title;

    @Positive
    private int numPages;

}
