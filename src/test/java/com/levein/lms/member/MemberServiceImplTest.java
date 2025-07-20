package com.levein.lms.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levein.lms.dto.request.MemberRequest;
import com.levein.lms.dto.response.MemberResponse;
import com.levein.lms.entity.Member;
import com.levein.lms.exceptions.AlreadyExistException;
import com.levein.lms.exceptions.MemberNotFoundException;
import com.levein.lms.repository.MemberRepository;
import com.levein.lms.service.impl.MemberServiceImpl;
import com.levein.lms.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    private MemberRequest memberRequest;
    private Member member;
    private MemberResponse memberResponse;

    @BeforeEach
    void setUp() {
        memberRequest = new MemberRequest();
        memberRequest.setName("Alice");
        memberRequest.setEmail("alice@test.com");

        member = new Member();
        member.setId(1L);
        member.setName("Alice");
        member.setEmail("alice@test.com");

        memberResponse = new MemberResponse();
        memberResponse.setId(1L);
        memberResponse.setName("Alice");
        memberResponse.setEmail("alice@test.com");
    }

    @Test
    void addMember_Success() {
        when(memberRepository.findByEmail(memberRequest.getEmail())).thenReturn(Optional.empty());
        when(mapper.convertValue(memberRequest, Member.class)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(member);

        // Static Mapper method call
        try (MockedStatic<Mapper> mockedMapper = mockStatic(Mapper.class)) {
            mockedMapper.when(() -> Mapper.mapToMemberResponse(member)).thenReturn(memberResponse);

            MemberResponse response = memberService.addMember(memberRequest);

            assertEquals(memberResponse.getEmail(), response.getEmail());
            verify(memberRepository).save(member);
        }
    }

    @Test
    void addMember_AlreadyExistsException() {
        when(memberRepository.findByEmail(memberRequest.getEmail())).thenReturn(Optional.of(member));

        AlreadyExistException ex = assertThrows(AlreadyExistException.class,
                () -> memberService.addMember(memberRequest));
        assertTrue(ex.getMessage().contains("already exists"));
    }


    @Test
    void getMember_Success() {
        when(memberRepository.findMemberWithBorrowedBooksById(1L)).thenReturn(Optional.of(member));

        try (MockedStatic<Mapper> mockedMapper = mockStatic(Mapper.class)) {
            mockedMapper.when(() -> Mapper.mapToMemberResponse(member)).thenReturn(memberResponse);

            MemberResponse response = memberService.getMember(1L);

            assertEquals(memberResponse.getEmail(), response.getEmail());
            assertEquals(memberResponse.getName(), response.getName());
        }
    }


    @Test
    void getMember_NotFoundException() {
        when(memberRepository.findMemberWithBorrowedBooksById(1L)).thenReturn(Optional.empty());

        MemberNotFoundException ex = assertThrows(MemberNotFoundException.class,
                () -> memberService.getMember(1L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void updateMember_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);

        memberRequest.setName("Bob");
        memberRequest.setEmail("bob@test.com");

        try (MockedStatic<Mapper> mockedMapper = mockStatic(Mapper.class)) {
            mockedMapper.when(() -> Mapper.mapToMemberResponse(member)).thenReturn(memberResponse);

            MemberResponse response = memberService.updateMember(1L, memberRequest);

            assertEquals(memberResponse.getEmail(), response.getEmail());
            verify(memberRepository).save(member);
        }
    }

    @Test
    void updateMember_NotFoundException() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        MemberNotFoundException ex = assertThrows(MemberNotFoundException.class,
                () -> memberService.updateMember(1L, memberRequest));
        assertTrue(ex.getMessage().contains("not found"));
    }
}
