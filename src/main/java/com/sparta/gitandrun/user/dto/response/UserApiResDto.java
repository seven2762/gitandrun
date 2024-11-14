package com.sparta.gitandrun.user.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserApiResDto {

    private int statusCode;
    private String message;
    private Object data;

    public UserApiResDto(String message, int statusCode, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

}
