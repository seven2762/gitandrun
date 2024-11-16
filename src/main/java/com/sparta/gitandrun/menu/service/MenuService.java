package com.sparta.gitandrun.menu.service;

import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.menu.dto.MenuResponseDto;
import com.sparta.gitandrun.menu.dto.StoreWithMenusDto;
import com.sparta.gitandrun.menu.entity.Menu;
import com.sparta.gitandrun.menu.repository.MenuRepository;
import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.store.repository.StoreRepository;
import com.sparta.gitandrun.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    //CREATE
    public MenuResponseDto createMenu(MenuRequestDto requestDto) {

        //Store ID 확인
        Store store = findStoreId(requestDto.getStoreId());

        //메뉴에 저장
        Menu menu = menuRepository.save(new Menu(requestDto, store));
        return new MenuResponseDto(menu);
    }

    //UPDATE
    @Transactional
    public MenuResponseDto updateMenu(MenuRequestDto requestDto, UUID menuId) {
        findMenuId(menuId);
        Menu menu = menuRepository.findById(menuId).get();
        menu.update(requestDto);
        return new MenuResponseDto(menu);
    }

    //DELETE
    @Transactional
    public void deleteMenu(UUID menuId) {
        findMenuId(menuId);
        Menu menu = menuRepository.findById(menuId).get();

        menu.setDeletedBy(menu.getStore().getUser().getUsername());
        menu.setDeletedAt(LocalDateTime.now());
        menu.setDeleted(true);
        menuRepository.save(menu);
    }

    //READ ( 단 건 조회 )
    public MenuResponseDto getOneMenu(UUID menuId) {
        findMenuId(menuId);
        Menu menu = menuRepository.findById(menuId).get();
        MenuResponseDto responseDto = new MenuResponseDto(menu);
        return responseDto;
    }

    //READ ( 전체 조회 )
    @Transactional(readOnly = true)
    public Page<MenuResponseDto> getAllMenus(String sortBy, int page, int size) {
        int realSize = ConfirmPageSize(size);
        Pageable pageable = PageRequest.of(page, realSize, Sort.by(sortBy).ascending());
        Page<Menu> menuList = menuRepository.findAll(pageable);
        return menuList.map(menu -> new MenuResponseDto(menu));
    }

    //하나의 가게에 있 모든 메뉴 검색
    @Transactional(readOnly = true)
    public Page<MenuResponseDto> getDetailMenu(UUID storeId, String sortBy, int page, int size) {
        int realSize = ConfirmPageSize(size);
        findStoreId(storeId);
        Pageable pageable = PageRequest.of(page, realSize, Sort.by(sortBy).ascending());
        Page<Menu> menuList = menuRepository.findAllByStoreId(storeId, pageable);

        return menuList.map(menu -> new MenuResponseDto(menu));
    }

    //메뉴를 검색시 가게와 가게의 메뉴이름, 메뉴내용 검색
    @Transactional(readOnly = true)
    public Page<StoreWithMenusDto> getStoreAndMenus(String storeName, String sortBy, int page, int size) {
        int realSize = ConfirmPageSize(size);
        Pageable pageable = PageRequest.of(page, realSize, Sort.by(sortBy).ascending());

        Page<Menu> menuList = menuRepository.findMenusByStoreName(storeName, pageable);

        return menuList.map(menu -> new StoreWithMenusDto(
                menu.getStore().getStoreName(),
                menu.getMenuName(),
                menu.getMenuContent(),
                menu.getMenuPrice()
        ));
    }


    private int ConfirmPageSize(int size) {
        if ( size != 10 && size != 30 && size != 50){
            return 10;
        } else return size;
    }

    private void findMenuId(UUID id){
        menuRepository.findById(id).orElseThrow(
            () -> new NullPointerException("해당 Id가 존재하지 않습니다.")
        );
    }

    private Store findStoreId(UUID storeId){
        Store whatStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
        return whatStore;
    }

}
