package com.sparta.gitandrun.store.dto;

import com.sparta.gitandrun.store.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequestDto {

    @NotBlank(message = "가게 이름은 필수 입력 항목입니다.")
    private String storeName;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    private String phone;

    @NotBlank(message = "카테고리 이름은 필수 입력 항목입니다.")
    private String categoryName;  // 카테고리 이름을 String으로 받기

    @NotNull(message = "지역 번호는 필수 입력 항목입니다.")
    private Long regionId;

    @NotNull(message = "주소는 필수 입력 항목입니다.")
    private Address address;  // Address 임베디드 클래스를 사용
}
