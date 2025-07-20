package com.levein.lms.repository;

import com.levein.lms.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @EntityGraph(attributePaths = "borrowedBooks")
    Optional<Member> findMemberWithBorrowedBooksById(long id);

}
