package com.levein.lms.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;



@Getter
@Setter
@ToString
public class BookRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Author must not be blank")
    @Size(max = 255, message = "Author must be at most 255 characters")
    private String author;

    @NotBlank(message = "ISBN must not be blank")
    @Size(max = 20, message = "ISBN must be at most 20 characters")
    private String isbn;

    @PastOrPresent(message = "Published date cannot be in the future")
    private LocalDate publishedDate;

}
