package com.sparta.gitandrun.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
public class MenuRequestDto {

    //메뉴 이름
    private String name;

    //음식 가격
    private int price;

    //메뉴 설명
    private String content;

    //baseEntity로 생성해줄 Field 들
    private LocalDateTime createdAt;

    private String createBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    private String deletedBy;


}
