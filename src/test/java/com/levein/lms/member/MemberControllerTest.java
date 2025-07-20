package com.levein.lms.member;

import com.levein.lms.controller.MemberController;
import com.levein.lms.dto.request.MemberRequest;
import com.levein.lms.dto.response.ApiResponse;
import com.levein.lms.dto.response.MemberResponse;
import com.levein.lms.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


import java.util.List;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private MemberRequest memberRequest;
    private MemberResponse memberResponse;


    @BeforeEach
    void setUp() {
        memberRequest = new MemberRequest();
        memberRequest.setName("Alice");
        memberRequest.setEmail("alice@test.com");

        memberResponse = new MemberResponse();
        memberResponse.setId(1L);
        memberResponse.setName("Alice");
        memberResponse.setEmail("alice@test.com");
    }

    @Test
    void addMember_ReturnsApiResponse() {
        when(memberService.addMember(memberRequest)).thenReturn(memberResponse);

        ResponseEntity<ApiResponse<MemberResponse>> response = memberController.addMember(memberRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(memberResponse, response.getBody().getData());
        assertEquals("Member created successfully.", response.getBody().getMessage());
    }

    @Test
    void getMembers_ReturnsList() {
        MemberResponse memberListResponse = new MemberResponse(); // or use builder/factory
        memberListResponse.setId(1L);
        memberListResponse.setName("John");
        memberListResponse.setEmail("john@example.com");

        List<MemberResponse> list = List.of(memberListResponse);

        when(memberService.getMembers()).thenReturn(list);

        ResponseEntity<Object> response = memberController.getMembers();

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(list, apiResponse.getData());
    }



    @Test
    void getMember_ReturnsMemberResponse() {
        when(memberService.getMember(1L)).thenReturn(memberResponse);

        ResponseEntity<Object> response = memberController.getMember(1L);

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(memberResponse, apiResponse.getData());
    }

    @Test
    void updateMember_ReturnsUpdatedMember() {
        when(memberService.updateMember(eq(1L), any(MemberRequest.class))).thenReturn(memberResponse);


        ResponseEntity<Object> response = memberController.updateMember(1L, memberRequest);

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(memberResponse, apiResponse.getData());
    }
}
