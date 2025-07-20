package com.levein.lms.controller;

import com.levein.lms.constant.MessageConstants;
import com.levein.lms.dto.request.MemberRequest;
import com.levein.lms.dto.response.ApiResponse;
import com.levein.lms.dto.response.MemberResponse;
import com.levein.lms.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @Operation(description = "Api for add member")
    public ResponseEntity<ApiResponse<MemberResponse>> addMember(@Valid @RequestBody MemberRequest request) {

        MemberResponse response = memberService.addMember(request);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.MEMBER_CREATED_SUCCESS, response));
    }

    @GetMapping
    @Operation(description = "Api for fetch member list")
    public ResponseEntity<Object> getMembers() {

        List<MemberResponse> response = memberService.getMembers();

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.MEMBER_RETRIEVED_SUCCESS, response));
    }

    @GetMapping("/{memberId}")
    @Operation(description = "Api for fetch member")
    public ResponseEntity<Object> getMember(@PathVariable long memberId) {

        MemberResponse response = memberService.getMember(memberId);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.MEMBER_RETRIEVED_SUCCESS, response));
    }

    @PutMapping("/{memberId}")
    @Operation(description = "Api for update member")
    public ResponseEntity<Object> updateMember(@PathVariable long memberId, @Valid @RequestBody MemberRequest request) {

        MemberResponse response = memberService.updateMember(memberId, request);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.MEMBER_UPDATED_SUCCESS, response));
    }

    @DeleteMapping("/{memberId}")
    @Operation(description = "Api for delete member")
    public ResponseEntity<ApiResponse<String>> deleteMember(@PathVariable long memberId) {

        memberService.deleteMember(memberId);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, MessageConstants.MEMBER_DELETED_SUCCESS));
    }
}
