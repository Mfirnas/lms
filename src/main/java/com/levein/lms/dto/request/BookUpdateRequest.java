package com.levein.lms.dto.request;

import com.levein.lms.dto.validation.ValidateAvailabilityType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter
@Setter
@ToString
public class BookUpdateRequest {

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

    @ValidateAvailabilityType
    @Nullable
    private String availabilityStatus;


    //cannot manually update as borrowed
    public enum AvailabilityStatus {
        AVAILABLE,
        RESERVED
    }
}
