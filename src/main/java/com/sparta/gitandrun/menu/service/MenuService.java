package com.sparta.gitandrun.menu.service;

import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.menu.dto.MenuResponseDto;
import com.sparta.gitandrun.menu.entity.Menu;
import com.sparta.gitandrun.menu.repository.MenuRepository;
import com.sparta.gitandrun.store.entity.Store;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

//    public Menu createMenu(MenuRequestDto requestDto) {
//        Store store = storeRepository.findById(requestDto.getStoreId())
//                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
//
//        Menu menu = new Menu();
//        menu.setMenuName(requestDto.getMenuName());
//        menu.setMenuPrice(requestDto.getMenuPrice());
//        menu.setMenuContent(requestDto.getMenuContent());
//        menu.setStore(store); // Store 엔티티 설정
//
//        return menuRepository.save(menu);
//    }

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

    @Transactional // Paging
    public Page<MenuResponseDto> getAllMenusTest( int page, int size, String sortBy) {
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Menu> menuList;

/* 임시 코드, 추후 메뉴 장르별 카테고리 추가시 작성.
        MenuRoleEnum menuRoleEnum 추후 메뉴 category 에서 Role을 받아와야 함.(메뉴별 검색)
        if (menuRoleEnum == MenuRoleEnum.한식) {
            menuList = menuRepository.findAllByCategory(한식, pageable);
        } else if {
            menuList....
        }
*/
        menuList = menuRepository.findAll(pageable);

        return menuList.map(MenuResponseDto::new);
    }
}
