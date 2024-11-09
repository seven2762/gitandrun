package com.sparta.gitandrun.user.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {


    private String address;
    private String addressDetail;
    private String zipcode;



}
