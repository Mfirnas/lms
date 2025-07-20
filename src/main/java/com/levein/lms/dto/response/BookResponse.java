package com.levein.lms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponse {
    private long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publishedDate;
    private String availabilityStatus;
    private MemberDetails borrowedBy;


}
