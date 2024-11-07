package com.sparta.gitandrun.menu.service;

import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.menu.dto.MenuResponseDto;
import com.sparta.gitandrun.menu.entity.Menu;
import com.sparta.gitandrun.menu.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

//    public MenuService(MenuRepository menuRepository) {
//        this.menuRepository = menuRepository;
//    }

    //CREATE
    public MenuResponseDto createMenu(MenuRequestDto requestDto) {

        Menu menu = menuRepository.save(new Menu(requestDto));
        return new MenuResponseDto(menu);

    }

    //UPDATE
    @Transactional
    public MenuResponseDto updateMenu(Long id, MenuRequestDto requestDto) {


        Menu menu = menuRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 Id가 존재하지 않습니다.")
        );
        menu.update(requestDto);
        return new MenuResponseDto(menu);
    }

    public void deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 Id가 존재하지 않습니다.")
        );
        menuRepository.deleteById(id);
    }

    public List<MenuResponseDto> getAllMenus() {
        List<Menu> menuList = menuRepository.findAll();

        List<MenuResponseDto> responseDtoList = new ArrayList<>();
        for (Menu menu : menuList) {
            responseDtoList.add(new MenuResponseDto(menu));
        }

        return responseDtoList;
    }
}

    //DELETE
//    @Transactional
//    public void deleteMenu(Long id) {
//        return  menuRepository.deleteById(id);
//
//    }

    //READ
//    @Transactional //product에서의 @ManyToOne의 지연로딩기능을 이용하기 위함
//    public List<MenuResponseDto> getMenus(Menu menu) {
//
//        List<Menu> menuList = menuRepository.findAllByMenu(menu); // var 명령어로 생성
//
//        List<MenuResponseDto> responseDtoList = new ArrayList<>();
//
//        for (Menu menu : menuList) { // iter(향상된 for문) 명령어로 생성
//            responseDtoList.add(new MenuResponseDto(menu));
//        }
//
//        return menuList.map(menuResponseDto::new); // map 메서드를 사용하여 Page타입에 들어있는 Product를 하나씩 돌리면서 ProductResponseDto가 생성된다.
//
//    }
