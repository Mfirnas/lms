package com.levein.lms.utils;

import com.levein.lms.dto.response.BookDetails;
import com.levein.lms.dto.response.BookResponse;
import com.levein.lms.dto.response.MemberDetails;
import com.levein.lms.dto.response.MemberResponse;
import com.levein.lms.entity.Book;
import com.levein.lms.entity.Member;

import java.util.List;

public class Mapper {

    public static MemberResponse mapToMemberResponse(Member member) {
        if (member == null) {
            return null;
        }

        List<BookDetails> borrowedBooks = member.getBorrowedBooks() != null
                ? member.getBorrowedBooks().stream()
                .map(book -> new BookDetails(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn()
                ))
                .toList()
                : List.of();

        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .borrowedBooks(borrowedBooks)
                .build();
    }


    public static BookResponse mapToBookResponse(Book book) {
        if (book == null) {
            return null;
        }

        MemberDetails memberDetails = null;
        if (book.getBorrowedBy() != null) {
            memberDetails = new MemberDetails(
                    book.getBorrowedBy().getId(),
                    book.getBorrowedBy().getName(),
                    book.getBorrowedBy().getEmail()
            );
        }

        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setPublishedDate(book.getPublishedDate());
        response.setAvailabilityStatus(
                book.getAvailabilityStatus() != null ? book.getAvailabilityStatus().toString() : null
        );
        response.setBorrowedBy(memberDetails);

        return response;
    }

    private Mapper(){

    }
}
