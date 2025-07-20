package com.levein.lms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.request.BookRequest;
import com.levein.lms.dto.request.BookUpdateRequest;
import com.levein.lms.dto.response.BookResponse;
import com.levein.lms.entity.Book;
import com.levein.lms.exceptions.AlreadyExistException;
import com.levein.lms.exceptions.BookNotFoundException;
import com.levein.lms.exceptions.CommonLmsException;
import com.levein.lms.exceptions.CommonServerException;
import com.levein.lms.repository.BookRepository;
import com.levein.lms.service.BookService;
import com.levein.lms.utils.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

    public BookServiceImpl(BookRepository bookRepository, ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public BookResponse addBook(BookRequest bookRequest) {

        bookRepository.findByIsbn(bookRequest.getIsbn()).
                ifPresent(book -> {
                    throw new AlreadyExistException(MessageConstants.Error.BOOK_ALREADY_EXISTS + book.getIsbn());
                });

        try {
            Book book = objectMapper.convertValue(bookRequest, Book.class);
            book.setAvailabilityStatus(Book.AvailabilityStatus.AVAILABLE);
            bookRepository.save(book);
            return Mapper.mapToBookResponse(book);
        } catch (Exception e) {
            throw new CommonServerException(e.getMessage());
        }

    }

    @Override
    public List<BookResponse> getBooks() {

        return bookRepository.findAll().stream()
                .map(book -> objectMapper.convertValue(book, BookResponse.class)).toList();
    }

    @Override
    public BookResponse getBook(long id) {

        try {
            return bookRepository.findById(id)
                    .map(Mapper::mapToBookResponse)
                    .orElseThrow(() -> new BookNotFoundException(MessageConstants.Error.BOOK_NOT_FOUND + id));
        } catch (CommonServerException exception) {
            throw new CommonServerException(exception.getMessage());
        }

    }

    @Override
    public BookResponse updateBook(long id, BookUpdateRequest bookRequest) {

        Book book = bookRepository.findById(id)
                .map(bookObj -> {
                    if (bookObj.getAvailabilityStatus() == Book.AvailabilityStatus.BORROWED) {
                        throw new CommonLmsException("Book cannot update without return first" );
                    }
                    return bookObj;
                })
                .orElseThrow(() -> new BookNotFoundException(MessageConstants.Error.BOOK_NOT_FOUND + id));
        try {
            book.setTitle(bookRequest.getTitle());
            book.setAuthor(bookRequest.getAuthor());
            book.setPublishedDate(bookRequest.getPublishedDate());
            book.setAvailabilityStatus(Book.AvailabilityStatus.valueOf(bookRequest.getAvailabilityStatus()));
            bookRepository.save(book);
            return Mapper.mapToBookResponse(book);
        } catch (Exception e) {
            throw new CommonServerException(e.getMessage());
        }

    }

    @Override
    public Page<BookResponse> getAllBooks(String search, Pageable pageable) {

        Page<Book> bookPage;

        if (search != null && !search.trim().isEmpty()) {
            bookPage = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }
        return bookPage.map(book -> objectMapper.convertValue(book, BookResponse.class));

    }

    @Override
    public void deleteBook(long id) {

        try {
            bookRepository.findById(id).orElseThrow(()->new BookNotFoundException(MessageConstants.Error.BOOK_NOT_FOUND+id));
            bookRepository.deleteById(id);
        } catch (CommonServerException exception) {
            throw new CommonServerException(exception.getMessage());
        }
    }


}
