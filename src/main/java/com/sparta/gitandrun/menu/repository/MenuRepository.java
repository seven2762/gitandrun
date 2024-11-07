package com.sparta.gitandrun.menu.repository;

import com.sparta.gitandrun.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select m from Menu m where m.id = :menuId and m.isDeleted = false ")
    Optional<Menu> findByIdAndIsDeletedFalse(@Param("menuId") Long menuId);


//    List<Menu> findAllBy(Menu menu);

}
