package com.levein.lms.repository;

import com.levein.lms.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = "borrowedBy")
    Optional<Book> findByIsbn(String isbn);

    @EntityGraph(attributePaths = "borrowedBy")
    Optional<Book> findById(long id);


    Page<Book> findAll(Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);

}
