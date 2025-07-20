package com.levein.lms.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.request.BorrowRequest;
import com.levein.lms.entity.Book;
import com.levein.lms.entity.Member;
import com.levein.lms.repository.BookRepository;
import com.levein.lms.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private Member member;
    private static final String BASE_URL = "/api/v1/library/books";


@BeforeEach
void setup() {
    member = new Member();
    member.setName("Test Member");
    member.setEmail("test@example.com");
    member = memberRepository.save(member);

    book = new Book();
    book.setTitle("Test Book");
    book.setAuthor("Test Author");
    book.setIsbn("ISBN99999");
    book.setAvailabilityStatus(Book.AvailabilityStatus.AVAILABLE);
    book = bookRepository.save(book);
}


    @Test
    void testBorrowBook_Success() throws Exception {
        BorrowRequest request = new BorrowRequest();
        request.setBookId(book.getId());
        request.setMemberId(member.getId());

        mockMvc.perform(post(BASE_URL+"/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(MessageConstants.BOOK_BORROWED_SUCCESS)))
                .andExpect(jsonPath("$.data.title", is(book.getTitle())))
                .andExpect(jsonPath("$.data.availabilityStatus", is("BORROWED")));
    }

    @Test
    void testBorrowBook_AlreadyBorrowed() throws Exception {
        // Assign persisted member and set book to BORROWED
        book.setAvailabilityStatus(Book.AvailabilityStatus.BORROWED);
        book.setBorrowedBy(member);
        bookRepository.save(book); // Ensure this save happens after member is saved

        BorrowRequest request = new BorrowRequest();
        request.setBookId(book.getId());
        request.setMemberId(member.getId());

        mockMvc.perform(post(BASE_URL+"/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString(MessageConstants.Error.BOOK_ALREADY_BORROWED)));
    }


    @Test
    void testReturnBook_Success() throws Exception {
        book.setAvailabilityStatus(Book.AvailabilityStatus.BORROWED);
        book.setBorrowedBy(member);
        bookRepository.save(book);

        mockMvc.perform(put(BASE_URL+"/return/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(MessageConstants.BOOK_RETURNED_SUCCESS)))
                .andExpect(jsonPath("$.data.availabilityStatus", is("AVAILABLE")));
    }

    @Test
    void testReturnBook_NotBorrowed() throws Exception {
        mockMvc.perform(put(BASE_URL+"/return/" + book.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(MessageConstants.Error.BOOK_NOT_BORROWED)));
    }

    @AfterEach
    void cleanUp() {
        bookRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
