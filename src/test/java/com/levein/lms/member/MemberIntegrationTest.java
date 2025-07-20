package com.levein.lms.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.request.MemberRequest;
import com.levein.lms.entity.Member;
import com.levein.lms.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
 class MemberIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper
            objectMapper;
    private static final String BASE_URL = "/api/v1/member";
    @BeforeEach
    void cleanDB() {
        memberRepository.deleteAll();
    }

    @Test
    void testAddMember_Success() throws Exception {
        MemberRequest request = new MemberRequest("Alice", "alice@test.com");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(MessageConstants.MEMBER_CREATED_SUCCESS));
    }
    @Test
    void testAddMember_MissingName() throws Exception {
        MemberRequest request = new MemberRequest();
        request.setEmail("alice@test.com");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetMembers_EmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetMemberById_Success() throws Exception {
        Member member = new Member();
        member.setName("Bob");
        member.setEmail("bob@test.com");

        Member saved = memberRepository.save(member);

        mockMvc.perform(get(BASE_URL+"/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Bob"));
    }

    @Test
    void testUpdateMember_Success() throws Exception {
        Member member = new Member();
        member.setName("Old");
        member.setEmail("old@test.com");
        Member saved = memberRepository.save(member);

        MemberRequest update = new MemberRequest("New", "new@test.com");

        mockMvc.perform(put(BASE_URL+"/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("New"));
    }
}
