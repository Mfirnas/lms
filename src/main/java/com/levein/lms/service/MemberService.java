package com.levein.lms.service;

import com.levein.lms.dto.request.MemberRequest;

import com.levein.lms.dto.response.MemberResponse;

import java.util.List;


public interface MemberService {

    public MemberResponse addMember(MemberRequest bookRequest);

    public List<MemberResponse> getMembers();

    public MemberResponse getMember(long id);

    public MemberResponse updateMember(long id, MemberRequest request);

    public void deleteMember(long id);

}
