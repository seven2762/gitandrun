package com.sparta.gitandrun.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MenuRequestDto {

    private String menuName;

    private int menuPrice;

    private String menuContent;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private boolean isDeleted;

    private LocalDateTime deletedAt;

    private String deletedBy;

    private UUID storeId;

}
