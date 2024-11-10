package com.sparta.gitandrun.menu.controller;


import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.menu.dto.MenuResponseDto;
import com.sparta.gitandrun.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @PostMapping("/new")
    public MenuResponseDto createMenu(@RequestBody MenuRequestDto requestDto) {
        return menuservice.createMenu(requestDto); // store 추가되면 store 정보 가져와서
    }

    //UPDATE
    @PutMapping("/update/{id}")
    public MenuResponseDto updateMenu(@RequestBody MenuRequestDto requestDto, @PathVariable Long id) {

        return menuservice.updateMenu(id, requestDto);
    }
    //DELETE
    @DeleteMapping("/delete/{id}")
    public void deleteMenu(@PathVariable Long id){
        menuservice.deleteMenu(id);
    }

    //READ
    @GetMapping("/list")
    public List<MenuResponseDto> getAllMunus(){
        return menuservice.getAllMenus();
    }

}
