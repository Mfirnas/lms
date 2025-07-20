package com.levein.lms.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookTransactionResponse {
    private long bookId;
    private String title;
    private String isbn;
    private String availabilityStatus;
    private long memberId;
}
