package com.levein.lms.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import java.util.List;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse {

    private long id;
    private String name;
    private String email;
    private List<BookDetails> borrowedBooks;

}
