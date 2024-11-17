package com.sparta.gitandrun.menu.entity;

import com.sparta.gitandrun.common.entity.BaseEntity;
import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import com.sparta.gitandrun.store.entity.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_menu")
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @Column(name = "menuId", columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID menuId;

    @Column(name = "menuName", nullable = false, length = 20)
    private String menuName;

    @Column(name = "menuPrice", nullable = false)
    private int menuPrice;

    @Column(name = "menuContent", nullable = false, length = 100)
    private String menuContent;

    @Column(nullable = false, length = 100)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false, length = 100)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String updatedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(nullable = true, length = 100)
    private LocalDateTime deletedAt;

    @Column(nullable = true, length = 100)
    private String deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Menu(MenuRequestDto requestDto, Store store) {
            this.store = store;
            this.menuName = requestDto.getMenuName();
            this.menuPrice = requestDto.getMenuPrice();
            this.menuContent = requestDto.getMenuContent();
            this.createdAt = requestDto.getCreatedAt();
            this.createdBy = store.getUser().getUsername();
            this.updatedAt = requestDto.getUpdatedAt();
            this.updatedBy = store.getUser().getUsername();
            this.isDeleted = Boolean.FALSE;
        }

    public void update(MenuRequestDto requestDto) {
        this.menuName = requestDto.getMenuName();
        this.menuPrice = requestDto.getMenuPrice();
        this.menuContent = requestDto.getMenuContent();
        this.updatedBy = store.getUser().getUsername();
    }

}
