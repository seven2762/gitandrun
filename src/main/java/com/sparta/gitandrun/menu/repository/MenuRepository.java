package com.sparta.gitandrun.menu.repository;

import com.sparta.gitandrun.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select m from Menu m where m.id in :menuIds and m.isDeleted = false")
    List<Menu> findByIdsAndIsDeletedFalse(@Param("menuIds") List<Long> menuIds);

}
