package com.levein.lms.service.impl;

import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.response.BookTransactionResponse;
import com.levein.lms.entity.Book;
import com.levein.lms.entity.Member;
import com.levein.lms.exceptions.*;
import com.levein.lms.repository.BookRepository;
import com.levein.lms.repository.MemberRepository;
import com.levein.lms.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public TransactionServiceImpl(BookRepository bookRepository, MemberRepository memberRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }


    /***
     * Borrows a book for a given member.
     * This method updates the availability status of the book to BORROWED
     * and associates the book with the specified member. It performs validation
     * to ensure the book exists, is available for borrowing, and the member exists.
     */

    @Override
    public BookTransactionResponse borrowBook(long id, long memberId) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(MessageConstants.Error.BOOK_NOT_FOUND + id));


        if (book.getAvailabilityStatus() == Book.AvailabilityStatus.BORROWED) {
            throw new BookNotAvailableException(MessageConstants.Error.BOOK_ALREADY_BORROWED+id);
        }
        else if(book.getAvailabilityStatus() == Book.AvailabilityStatus.RESERVED) {
            throw new BookNotAvailableException(MessageConstants.Error.BOOK_ALREADY_RESERVED);
        }


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(MessageConstants.Error.MEMBER_NOT_FOUND + memberId));
        try {

            book.setAvailabilityStatus(Book.AvailabilityStatus.BORROWED);
            book.setBorrowedBy(member);


            bookRepository.save(book);

            return BookTransactionResponse.builder()
                    .title(book.getTitle())
                    .availabilityStatus(book.getAvailabilityStatus().name())
                    .memberId(id)
                    .isbn(book.getIsbn())
                    .bookId(book.getId())
                    .build();

        } catch (CommonServerException exception) {
            throw new CommonServerException(exception.getMessage());
        }
    }

    /***
     * Handles the process of returning a borrowed book by updating its availability status
     * and removing the association with the member who borrowed it.
     */

    @Override
    public BookTransactionResponse returnBook(long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(MessageConstants.Error.BOOK_NOT_FOUND + id));

        if (book.getAvailabilityStatus() != Book.AvailabilityStatus.BORROWED) {
            throw new CommonLmsException(MessageConstants.Error.BOOK_NOT_BORROWED);
        }
        try {
            long memberId = book.getBorrowedBy().getId();
            book.setAvailabilityStatus(Book.AvailabilityStatus.AVAILABLE);
            book.setBorrowedBy(null);

            bookRepository.save(book);

            return BookTransactionResponse.builder()
                    .bookId(book.getId())
                    .title(book.getTitle())
                    .isbn(book.getIsbn())
                    .availabilityStatus(book.getAvailabilityStatus().name())
                    .memberId(memberId)
                    .build();

        } catch (Exception e) {
            throw new CommonServerException(e.getMessage());
        }
    }
}
