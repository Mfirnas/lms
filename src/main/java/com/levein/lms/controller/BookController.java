package com.levein.lms.controller;

import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.request.BookRequest;
import com.levein.lms.dto.request.BookUpdateRequest;
import com.levein.lms.dto.response.ApiResponse;
import com.levein.lms.dto.response.BookResponse;
import com.levein.lms.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(description = "Api for add book")
    public ResponseEntity<ApiResponse<BookResponse>> addBook(@Valid @RequestBody BookRequest bookRequest) {

        BookResponse response = bookService.addBook(bookRequest);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.BOOK_CREATED_SUCCESS, response));
    }


    @GetMapping("/{bookId}")
    @Operation(description = "Api for fetch book")
    public ResponseEntity<ApiResponse<BookResponse>> getBook(@PathVariable long bookId) {

        BookResponse response = bookService.getBook(bookId);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.BOOK_RETRIEVED_SUCCESS, response));
    }

    @PutMapping("/{bookId}")
    @Operation(description = "Api for update book")
    public ResponseEntity<ApiResponse<Object>> updateBook(@PathVariable long bookId, @Valid @RequestBody BookUpdateRequest bookRequest) {

        BookResponse response = bookService.updateBook(bookId, bookRequest);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.BOOK_UPDATED_SUCCESS, response));
    }

    @DeleteMapping("/{bookId}")
    @Operation(description = "Api for delete book")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable long bookId) {

        bookService.deleteBook(bookId);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.BOOK_DELETED_SUCCESS));
    }


    @GetMapping
    @Operation(description = "Api for book list")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BookResponse> response = bookService.getAllBooks(search, pageable);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.BOOK_RETRIEVED_SUCCESS, response));

    }


}
