package com.sparta.gitandrun.menu.controller;


import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.menu.dto.MenuResponseDto;
import com.sparta.gitandrun.menu.dto.StoreWithMenusDto;
import com.sparta.gitandrun.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    //CREATE
    @PostMapping
    public ResponseEntity<ApiResDto> createMenu(@RequestBody MenuRequestDto requestDto) {
        menuService.createMenu(requestDto);
        return ResponseEntity.ok().body(new ApiResDto("메뉴 생성 완료", HttpStatus.OK.value()));
    }

    //UPDATE
    @PatchMapping("/{menuId}")
    public ResponseEntity<ApiResDto> updateMenu(@RequestBody MenuRequestDto requestDto, @PathVariable("menuId") UUID menuId) {
        menuService.updateMenu(requestDto, menuId);
        return ResponseEntity.ok().body(new ApiResDto("메뉴 수정 완료", HttpStatus.OK.value()));

    }
    //DELETE
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResDto> deleteMenu(@PathVariable("menuId") UUID menuId){
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok().body(new ApiResDto("삭제 완료", HttpStatus.OK.value()));
    }

    //READ 모든 필드 조회(SECURED ADMIN, MANAGER)
    @GetMapping
    public List<MenuResponseDto> getAllMenus(){
        return menuService.getAllMenus();
    }

      //READ 가게의 모든 메뉴에 대한 이름, 내용, 가격들 조회
    @GetMapping("/search/{storeId}")
    public List<MenuResponseDto> getDetailMenu(@PathVariable UUID storeId){
        return menuService.getDetailMenu(storeId);
    }

    //READ 가게 이름에 메뉴명이 들어가는 가게 조회 및 가게의 메뉴들 조회
    @GetMapping("/search/store")
    public ResponseEntity<ApiResDto> getStoreAndMenus(
            @RequestParam String storeName,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<StoreWithMenusDto> StoreAndMenu = menuService.getStoreAndMenus(storeName, sortBy, page, size);
        return ResponseEntity.ok().body(new ApiResDto("메뉴명 기반 가게 검색 완료", HttpStatus.OK.value(), StoreAndMenu));
    }


        //READ ony One
    @GetMapping("/{id}")
    public MenuResponseDto getOneMenu(@PathVariable("id") UUID menuId){
        return menuService.getOneMenu(menuId);
    }

    //메뉴 페이징
    @GetMapping("/paging")
    public Page<MenuResponseDto> getMenuPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam("sortBy") String sortBy // 생성일, 수정일 기준
            ) {
        return menuService.getAllMenusTest(page-1, size, sortBy);
    }

}
