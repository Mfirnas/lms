package com.levein.lms.constant;

public class MessageConstants {

    private MessageConstants() {

    }
    // Book messages
    public static final String BOOK_RETRIEVED_SUCCESS = "Book retrieved successfully.";
    public static final String BOOK_CREATED_SUCCESS = "Book created successfully.";
    public static final String BOOK_UPDATED_SUCCESS = "Book updated successfully.";
    public static final String BOOK_DELETED_SUCCESS = "Book deleted successfully.";


    // Member messages
    public static final String MEMBER_CREATED_SUCCESS = "Member created successfully.";
    public static final String MEMBER_RETRIEVED_SUCCESS = "Member retrieved successfully.";
    public static final String MEMBER_UPDATED_SUCCESS = "Member updated successfully.";
    public static final String MEMBER_DELETED_SUCCESS = "Member deleted successfully.";
    // Borrow/Return messages
    public static final String BOOK_BORROWED_SUCCESS = "Book borrowed successfully.";
    public static final String BOOK_RETURNED_SUCCESS = "Book returned successfully.";



    public static class Error {
        private Error(){

        }
        public static final String BOOK_ALREADY_BORROWED = "This book is already borrowed.";
        public static final String BOOK_NOT_BORROWED = "The book is not currently borrowed";
        public static final String BOOK_NOT_FOUND = "Book not found with the given ID.";
        public static final String BOOK_ALREADY_EXISTS = "A book already exists for this ISBN .";
        public static final String BOOK_ALREADY_RESERVED = "Book already reserved.";

        public static final String MEMBER_NOT_FOUND = "Member not found with the given ID.";
        public static final String MEMBER_LIMIT_EXCEEDED = "Member has reached the borrowing limit.";
        public static final String MEMBER_ALREADY_EXISTS = "A member with this email already exists.";

        public static final String VALIDATION_FAILED = "Validation failed for the request.";
        public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred. Please try again later.";


    }

    public static class Common{
         private Common(){

         }
        public static final String APPLICATION_JSON =  "application/json";
    }
}
