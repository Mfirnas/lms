package com.levein.lms.controller;

import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.request.BorrowRequest;
import com.levein.lms.dto.response.ApiResponse;
import com.levein.lms.dto.response.BookTransactionResponse;
import com.levein.lms.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/library/books")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/borrow")
    @Operation(description = "Api for borrow book")
    public ResponseEntity<ApiResponse<Object>> borrowBook(@RequestBody BorrowRequest request) {

        BookTransactionResponse response = transactionService.borrowBook(request.getBookId(), request.getMemberId());

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.BOOK_BORROWED_SUCCESS, response));
    }

    @PutMapping("/return/{bookId}")
    @Operation(description = "Api for return book")
    public ResponseEntity<ApiResponse<BookTransactionResponse>> returnBook(@PathVariable long bookId) {

        BookTransactionResponse response = transactionService.returnBook(bookId);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.BOOK_RETURNED_SUCCESS, response));
    }


}
