package com.levein.lms.transaction;

import com.levein.lms.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.response.BookTransactionResponse;
import com.levein.lms.entity.Book;
import com.levein.lms.entity.Member;
import com.levein.lms.exceptions.BookNotFoundException;
import com.levein.lms.repository.BookRepository;
import com.levein.lms.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionTest {

    private BookRepository bookRepository;
    private MemberRepository memberRepository;
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        memberRepository = mock(MemberRepository.class);
        transactionService = new TransactionServiceImpl(bookRepository, memberRepository);
    }

    @Test
    void testBorrowBook_Success() {
        Book book = new Book();
        book.setId(1L);
        book.setIsbn("ISBN123");
        book.setTitle("Book Title");
        book.setAvailabilityStatus(Book.AvailabilityStatus.AVAILABLE);

        Member member = new Member();
        member.setId(100L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(100L)).thenReturn(Optional.of(member));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookTransactionResponse response = transactionService.borrowBook(1L, 100L);

        assertEquals("BORROWED", response.getAvailabilityStatus());
        assertEquals("Book Title", response.getTitle());
    }

    @Test
    void testBorrowBook_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookNotFoundException.class, () ->
                transactionService.borrowBook(1L, 100L));

        assertTrue(exception.getMessage().contains(MessageConstants.Error.BOOK_NOT_FOUND));
    }

    @Test
    void testReturnBook_Success() {
        Book book = new Book();
        book.setId(1L);
        book.setIsbn("ISBN123");
        book.setTitle("Book Title");
        book.setAvailabilityStatus(Book.AvailabilityStatus.BORROWED);

        Member member = new Member();
        member.setId(100L);
        book.setBorrowedBy(member);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookTransactionResponse response = transactionService.returnBook(1L);

        assertEquals("AVAILABLE", response.getAvailabilityStatus());
    }
}
