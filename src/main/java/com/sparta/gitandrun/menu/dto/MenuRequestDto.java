package com.sparta.gitandrun.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MenuRequestDto {

    private String name;

    private int price;

    private String content;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private boolean isDeleted;

    private LocalDateTime deletedAt;

    private String deletedBy;


}
