package com.sparta.gitandrun.menu.dto;

import com.sparta.gitandrun.menu.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MenuResponseDto {

    private Long id;
    private String name;
    private int price;
    private String content;
    private LocalDateTime createdAt;
    private String createBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    private String deletedBy;

    LocalDateTime localDateTime = LocalDateTime.now();

    private List<MenuResponseDto> menuList = new ArrayList<>();

    public MenuResponseDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.content = menu.getContent();
        this.createdAt = menu.getCreatedAt();
        this.createBy = "test create user";
        this.updatedAt = menu.getUpdatedAt();
        this.updatedBy = "test update user";
//        this.isDeleted = menu.getIsDeleted();
//        this.deletedAt = menu.getDeletedAt();
//        this.deletedBy = menu.getDeletedBy();
    }

}
