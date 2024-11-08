package com.sparta.gitandrun.menu.controller;


import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.menu.dto.MenuResponseDto;
import com.sparta.gitandrun.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuservice;

    //CREATE
    @PostMapping
    public ResponseEntity<ApiResDto> createMenu(@RequestBody MenuRequestDto requestDto) {
        menuservice.createMenu(requestDto); // store 추가되면 store 정보 가져와서
        return ResponseEntity.ok().body(new ApiResDto("주문 완료", HttpStatus.OK.value()));
    }

    //UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResDto> updateMenu(@RequestBody MenuRequestDto requestDto, @PathVariable("id") Long id) {
        menuservice.updateMenu(id, requestDto);
        return ResponseEntity.ok().body(new ApiResDto("수정 완료", HttpStatus.OK.value()));

    }
    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto> deleteMenu(@PathVariable("id") Long id){
        System.out.println("id값은 + " + id);
        menuservice.deleteMenu(id);
        return ResponseEntity.ok().body(new ApiResDto("삭제 완료", HttpStatus.OK.value()));
    }

    //READ
    @GetMapping
    public List<MenuResponseDto> getAllMenus(){
        return menuservice.getAllMenus();
    }

}
