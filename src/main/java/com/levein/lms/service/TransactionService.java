package com.levein.lms.service;

import com.levein.lms.dto.response.BookTransactionResponse;


public interface TransactionService {

    public BookTransactionResponse borrowBook(long id, long memberId);

    public BookTransactionResponse returnBook(long id);
}
