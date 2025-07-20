package com.levein.lms.book;

import com.levein.lms.constant.MessageConstants;
import com.levein.lms.controller.BookController;
import com.levein.lms.dto.request.BookRequest;
import com.levein.lms.dto.request.BookUpdateRequest;
import com.levein.lms.dto.response.ApiResponse;
import com.levein.lms.dto.response.BookResponse;
import com.levein.lms.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookRequest bookRequest;
    private BookUpdateRequest bookUpdateRequest;
    private BookResponse sampleBook;
    @BeforeEach
    void setUp() {
        sampleBook = BookResponse.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("ISBN123")
                .publishedDate(LocalDate.of(2020, 1, 1))
                .availabilityStatus("AVAILABLE")
                .build();
    }

    @Test
    void testAddBook() {
        ResponseEntity<?> response = bookController.addBook(bookRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetBook() {

        ResponseEntity<?> response = bookController.getBook(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateBook() {

        ResponseEntity<?> response = bookController.updateBook(1L, bookUpdateRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void testGetBooks_ReturnsBooksSuccessfully() {

        int page = 0, size = 10;
        String sortBy = "title", direction = "asc", search = null;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<BookResponse> mockPage = new PageImpl<>(List.of(sampleBook), pageable, 1);

        when(bookService.getAllBooks(eq(search), any(Pageable.class))).thenReturn(mockPage);


        ResponseEntity<ApiResponse<Page<BookResponse>>> response = bookController.getBooks(page, size, sortBy, direction, search);


        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getBody().getStatus());
        assertEquals(MessageConstants.BOOK_RETRIEVED_SUCCESS, response.getBody().getMessage());

        assertNotNull(response.getBody().getData(), "Data should not be null");
        assertTrue(response.getBody().getData() instanceof Page<?>);

        Page<?> pageData = (Page<?>) response.getBody().getData();
        assertEquals(1, pageData.getTotalElements());
        assertEquals("Clean Code", ((BookResponse) pageData.getContent().get(0)).getTitle());
    }

}
