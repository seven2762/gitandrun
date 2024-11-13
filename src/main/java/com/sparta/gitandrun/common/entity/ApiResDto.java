package com.sparta.gitandrun.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResDto {
    private String msg;
    private Integer statusCode;
    private Object data;  // 응답 데이터 필드 추가

    public ApiResDto(String msg, Integer statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

    public ApiResDto(String msg, Integer statusCode, Object data) {
        this.msg = msg;
        this.statusCode = statusCode;
        this.data = data;
    }
}
