package com.sparta.gitandrun.menu.service;

import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.menu.dto.MenuResponseDto;
import com.sparta.gitandrun.menu.entity.Menu;
import com.sparta.gitandrun.menu.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    //CREATE
    public MenuResponseDto createMenu(MenuRequestDto requestDto) {
        Menu menu = menuRepository.save(new Menu(requestDto));
        return new MenuResponseDto(menu);
    }

    //UPDATE
    @Transactional
    public MenuResponseDto updateMenu(Long id, MenuRequestDto requestDto) {
        findId(id);
        Menu menu = menuRepository.findById(id).get();
        menu.update(requestDto);
        return new MenuResponseDto(menu);
    }

    //DELETE
    public void deleteMenu(Long id) {
        findId(id);
        menuRepository.deleteById(id);
    }

    //READ ( 전체 조회 )
    public List<MenuResponseDto> getAllMenus() {
        List<Menu> menuList = menuRepository.findAll();
        List<MenuResponseDto> responseDtoList = new ArrayList<>();
        for (Menu menu : menuList) {
            responseDtoList.add(new MenuResponseDto(menu));
        }
        return responseDtoList;
    }

    //READ ( 단 건 조회 )
    public MenuResponseDto getOneMenu(Long id) {
        findId(id);
        Menu menu = menuRepository.findById(id).get();
        MenuResponseDto responseDto = new MenuResponseDto(menu);
        return responseDto;
    }

    private void findId(Long id){
        menuRepository.findById(id).orElseThrow(
            () -> new NullPointerException("해당 Id가 존재하지 않습니다.")
        );
    }
}
