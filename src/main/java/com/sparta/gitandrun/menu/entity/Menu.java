package com.sparta.gitandrun.menu.entity;

import com.sparta.gitandrun.common.entity.BaseEntity;
import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "p_menu")
@NoArgsConstructor
@SoftDelete(columnName = "is_deleted")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menuName", nullable = false, length = 20)
    private String name;

    @Column(name = "menuPrice", nullable = false)
    private int price;

    @Column(name = "menuContent", nullable = false, length = 100)
    private String content;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false, length = 100)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String updatedBy;

    @Column(name = "is_deleted", nullable = false, insertable = false, updatable = false)
    private boolean isDeleted;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Column(nullable = true, length = 100)
    private String deletedBy;

/*  Store Entity 생성시 ManyToOne관계로 조인하여 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "", nullable = false)
    private Store store;
*/



    public Menu(MenuRequestDto requestDto) {
            this.name = requestDto.getName();
            this.price = requestDto.getPrice();
            this.content = requestDto.getContent();
            this.createdAt = requestDto.getCreatedAt();
            this.createdBy = "test created user";
            this.updatedAt = requestDto.getUpdatedAt();
            this.updatedBy = "test updated user";
            this.isDeleted = Boolean.FALSE;
            this.deletedAt = requestDto.getDeletedAt();
            this.deletedBy = "test deleted User";
        }

    public void update(MenuRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.content = requestDto.getContent();
    }
}
