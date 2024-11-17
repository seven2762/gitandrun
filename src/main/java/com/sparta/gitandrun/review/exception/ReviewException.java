package com.sparta.gitandrun.review.exception;

import com.sparta.gitandrun.common.entity.ApiResDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReviewException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 오류 메시지 추출
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        // ApiResDto 응답 객체 생성
        ApiResDto response = new ApiResDto("유효성 검사 오류", 400, errors);

        return ResponseEntity.badRequest().body(response);
    }

}
