package com.sparta.gitandrun.menu.controller;


import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.menu.dto.MenuResponseDto;
import com.sparta.gitandrun.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    //CREATE
    @PostMapping
    public ResponseEntity<ApiResDto> createMenu(@RequestBody MenuRequestDto requestDto) {
        menuService.createMenu(requestDto);
        return ResponseEntity.ok().body(new ApiResDto("주문 완료", HttpStatus.OK.value()));
    }

    //UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResDto> updateMenu(@RequestBody MenuRequestDto requestDto, @PathVariable("id") Long id) {
        menuService.updateMenu(id, requestDto);
        return ResponseEntity.ok().body(new ApiResDto("수정 완료", HttpStatus.OK.value()));

    }
    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto> deleteMenu(@PathVariable("id") Long id){
        menuService.deleteMenu(id);
        return ResponseEntity.ok().body(new ApiResDto("삭제 완료", HttpStatus.OK.value()));
    }

    //READ
    @GetMapping
    public List<MenuResponseDto> getAllMenus(){
        return menuService.getAllMenus();
    }

    //READ ony One
    @GetMapping("/{id}")
    public MenuResponseDto getOneMenu(@PathVariable("id") Long id){
        return menuService.getOneMenu(id);
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
