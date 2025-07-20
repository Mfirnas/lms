package com.levein.lms.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.request.BookRequest;
import com.levein.lms.dto.request.BookUpdateRequest;
import com.levein.lms.entity.Book;
import com.levein.lms.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    private Book savedBook;

    private static final String BASE_URL = "/api/v1/books";


    @BeforeEach
    void setUp() {
        Book book = new Book();
        book.setTitle("Java Basics");
        book.setAuthor("James");
        book.setIsbn("ISBN123456");
        book.setAvailabilityStatus(Book.AvailabilityStatus.AVAILABLE);

        savedBook = bookRepository.save(book);
    }


    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }
    @Test
    void testAddBook_Success() throws Exception {
        BookRequest request = new BookRequest();
        request.setTitle("New Book");
        request.setAuthor("New Author");
        request.setIsbn("NEW123");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("New Book")))
                .andExpect(jsonPath("$.message", is(MessageConstants.BOOK_CREATED_SUCCESS)));
    }


    @Test
    void testUpdateBook_Success() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setTitle("Updated Title");
        request.setAuthor("Updated Author");
        request.setIsbn(savedBook.getIsbn());
        request.setAvailabilityStatus("AVAILABLE");

        mockMvc.perform(put(BASE_URL+"/" + savedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Updated Title")));
    }

    @Test
    void testGetBooks_Success() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.status", is("OK")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteBook_Success() throws Exception {
        Book book = bookRepository.findAll().get(0);

        mockMvc.perform(delete(BASE_URL+"/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value(MessageConstants.BOOK_DELETED_SUCCESS));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteBook_NotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL+"/9999"))
                .andExpect(status().isNotFound());
    }
}
