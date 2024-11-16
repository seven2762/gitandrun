package com.sparta.gitandrun.menu.dto;

import com.sparta.gitandrun.menu.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StoreWithMenusDto {

    private String storeName;
    private String menuName;
    private String menuContent;

    public StoreWithMenusDto(String storeName, String menuName, String menuContent) {
        this.storeName = storeName;
        this.menuName = menuName;
        this.menuContent = menuContent;
    }
}
