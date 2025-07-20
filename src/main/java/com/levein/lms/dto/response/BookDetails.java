package com.levein.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookDetails {
    private long id;
    private String title;
    private String author;
    private String isbn;
}
