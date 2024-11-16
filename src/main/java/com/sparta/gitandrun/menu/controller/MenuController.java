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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    //CREATE
//    @Secured({"ROLE_OWNER","ROLE_MANAGER","ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<ApiResDto> createMenu(@RequestBody MenuRequestDto requestDto) {
        menuService.createMenu(requestDto);
        return ResponseEntity.ok().body(new ApiResDto("메뉴 생성 완료", HttpStatus.OK.value()));
    }

    //UPDATE
//    @Secured({"ROLE_OWNER","ROLE_MANAGER","ROLE_ADMIN"})
    @PatchMapping("/{menuId}")
    public ResponseEntity<ApiResDto> updateMenu(@RequestBody MenuRequestDto requestDto, @PathVariable("menuId") UUID menuId) {
        menuService.updateMenu(requestDto, menuId);
        return ResponseEntity.ok().body(new ApiResDto("메뉴 수정 완료", HttpStatus.OK.value()));

    }
    //DELETE
//    @Secured({"ROLE_OWNER","ROLE_MANAGER","ROLE_ADMIN"})
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResDto> deleteMenu(@PathVariable("menuId") UUID menuId){
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok().body(new ApiResDto("삭제 완료", HttpStatus.OK.value()));
    }

    //READ
    // Menu에 있는 모든 필드 조회(단순 전체 테이블 검색)
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @GetMapping
    public List<MenuResponseDto> getAllMenus(){
        return menuService.getAllMenus();
    }

      //READ
      //StoreId에 해당하는 모든 메뉴에 대한 필드 조회
      @Secured({"ROLE_OWNER","ROLE_MANAGER","ROLE_ADMIN"})
      @GetMapping("/search/{storeId}")
    public List<MenuResponseDto> getDetailMenu(@PathVariable UUID storeId){
        return menuService.getDetailMenu(storeId);
    }

    //READ
    // 가게 이름에 메뉴명이 들어가는 가게 조회 및 가게의 메뉴들의 모든 조회
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
    @Secured({"ROLE_MANAGER","ROLE_ADMIN"})
    public MenuResponseDto getOneMenu(@PathVariable("id") UUID menuId){
        return menuService.getOneMenu(menuId);
    }

    //메뉴 Table의 모든 항목 조회
//    @Secured({"ROLE_MANAGER","ROLE_ADMIN"})
    @GetMapping("/paging")
    public Page<MenuResponseDto> getMenuPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam("sortBy") String sortBy
            ) {
        return menuService.getAllMenusTest(page-1, size, sortBy);
    }

}
