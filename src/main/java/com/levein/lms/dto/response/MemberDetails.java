package com.levein.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberDetails {
    private long id;
    private String  name;
    private String email;
}
