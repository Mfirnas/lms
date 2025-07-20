package com.levein.lms.service;

import com.levein.lms.dto.request.BookRequest;
import com.levein.lms.dto.request.BookUpdateRequest;
import com.levein.lms.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BookService {

    public BookResponse addBook(BookRequest bookRequest);

    public List<BookResponse> getBooks();

    public BookResponse getBook(long id);

    public BookResponse updateBook(long id, BookUpdateRequest bookRequest);

    Page<BookResponse> getAllBooks(String search, Pageable pageable);

    public void deleteBook(long id);

}
