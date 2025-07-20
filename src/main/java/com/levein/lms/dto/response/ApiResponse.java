package com.levein.lms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{

    private T data;
    private String message;
    private HttpStatus status;
    private int statusCode;



    public ApiResponse(HttpStatus status,String message,T data ) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public ApiResponse(HttpStatus httpStatus, String message) {
        this.message = message;
        this.status = httpStatus;
    }
}
