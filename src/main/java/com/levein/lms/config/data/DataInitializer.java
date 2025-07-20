package com.levein.lms.config.data;

import com.levein.lms.entity.Book;
import com.levein.lms.entity.Member;
import com.levein.lms.repository.BookRepository;
import com.levein.lms.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        addSampleBooks();
        addSampleMembers();
    }

    private void addSampleBooks() {
        List<Book> sampleBooks = List.of(
                createBook("The Jungle Book", "Rudyard Kipling", "ISBN001"),
                createBook("1984", "George Orwell", "ISBN002"),
                createBook("To Kill a Mockingbird", "Harper Lee", "ISBN003"),
                createBook("The Great Gatsby", "F. Scott Fitzgerald", "ISBN004"),
                createBook("Moby Dick", "Herman Melville", "ISBN005"),
                createBook("Pride and Prejudice", "Jane Austen", "ISBN006"),
                createBook("War and Peace", "Leo Tolstoy", "ISBN007"),
                createBook("The Catcher in the Rye", "J.D. Salinger", "ISBN008"),
                createBook("The Hobbit", "J.R.R. Tolkien", "ISBN009"),
                createBook("Hamlet", "William Shakespeare", "ISBN010")
        );

        for (Book book : sampleBooks) {
            if (bookRepository.findByIsbn(book.getIsbn()).isEmpty()) {
                bookRepository.save(book);
            }
        }
    }

    private void addSampleMembers() {
        List<Member> sampleMembers = List.of(
                createMember("Alice", "alice@example.com"),
                createMember("Bob", "bob@example.com"),
                createMember("Charlie", "charlie@example.com"),
                createMember("David", "david@example.com"),
                createMember("Eve", "eve@example.com")
        );

        for (Member member : sampleMembers) {
            if (memberRepository.findByEmail(member.getEmail()).isEmpty()) {
                memberRepository.save(member);
            }
        }
    }

    private Book createBook(String title, String author, String isbn) {
        return Book.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .publishedDate(LocalDate.now())
                .availabilityStatus(Book.AvailabilityStatus.AVAILABLE)
                .build();
    }

    private Member createMember(String name, String email) {
        return Member.builder()
                .name(name)
                .email(email)
                .build();
    }
}
