package com.sparta.gitandrun.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PageResDto<T> {

    private int code;
    private String message;
    private T data;
}
