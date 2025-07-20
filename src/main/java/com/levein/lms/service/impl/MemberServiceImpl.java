package com.levein.lms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.request.MemberRequest;
import com.levein.lms.dto.response.MemberResponse;
import com.levein.lms.entity.Member;
import com.levein.lms.exceptions.AlreadyExistException;
import com.levein.lms.exceptions.CommonServerException;
import com.levein.lms.exceptions.MemberNotFoundException;
import com.levein.lms.repository.MemberRepository;
import com.levein.lms.service.MemberService;
import com.levein.lms.utils.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ObjectMapper mapper;

    public MemberServiceImpl(MemberRepository memberRepository, ObjectMapper mapper) {
        this.memberRepository = memberRepository;
        this.mapper = mapper;
    }

    @Override
    public MemberResponse addMember(MemberRequest request) {

        memberRepository.findByEmail(request.getEmail()).ifPresent(member -> {
            throw new AlreadyExistException(MessageConstants.Error.MEMBER_ALREADY_EXISTS + member.getEmail());
        });

        try {
            Member member = mapper.convertValue(request, Member.class);
            memberRepository.save(member);

            return Mapper.mapToMemberResponse(member);

        } catch (Exception e) {
            throw new CommonServerException(e.getMessage());
        }
    }

    @Override
    public List<MemberResponse> getMembers() {
        try {

            return memberRepository.findAll().stream()
                    .map(member->mapper.convertValue(member,MemberResponse.class))
                    .toList();

        } catch (CommonServerException exception) {
            throw new CommonServerException(exception.getMessage());
        }
    }

    @Override
    public MemberResponse getMember(long id) {

        try {
            Member member = memberRepository.findMemberWithBorrowedBooksById(id).orElseThrow(() -> new MemberNotFoundException(MessageConstants.Error.MEMBER_NOT_FOUND + id));

            return Mapper.mapToMemberResponse(member);
        } catch (CommonServerException exception) {
            throw new CommonServerException(exception.getMessage());

        }

    }

    @Override
    public MemberResponse updateMember(long id, MemberRequest request) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(MessageConstants.Error.MEMBER_NOT_FOUND + id));

        try {
            member.setName(request.getName());
            member.setEmail(request.getEmail());
            memberRepository.save(member);

            return Mapper.mapToMemberResponse(member);
        } catch (Exception e) {
            throw new CommonServerException(e.getMessage());
        }

    }

    @Override
    public void deleteMember(long id) {

        try {
            memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(MessageConstants.Error.BOOK_NOT_FOUND + id));

        } catch (CommonServerException exception) {
            throw new CommonServerException(exception.getMessage());
        }
    }
}
