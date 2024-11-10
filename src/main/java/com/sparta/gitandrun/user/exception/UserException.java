package com.sparta.gitandrun.user.exception;


import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ErrorCode errorCode;

    public UserException(ErrorCode message) {
        super(message.getMessage());
        this.errorCode = message;
    }

}
