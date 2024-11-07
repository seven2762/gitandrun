package com.sparta.gitandrun.menu.entity;

import com.sparta.gitandrun.menu.dto.MenuRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "p_menu")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE p_menu set is_deleted = true where id = ?")
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String content;

//    @Column(nullable = false)
//    @CreatedDate
//    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createBy;

//    @Column(nullable = false)
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

    @Column(nullable = false)

    private String updatedBy;

    @Column(nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Column(nullable = true)
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
//            this.createdAt = requestDto.getCreatedAt();
            this.createBy = "test created user";
//            this.updatedAt = requestDto.getUpdatedAt();
            this.updatedBy = "test updated user";
            this.isDeleted = Boolean.FALSE;
            this.deletedAt = requestDto.getDeletedAt();
            this.deletedBy = "test deleted User";
        }

    public void update(MenuRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.content = requestDto.getContent();
        this.updatedAt = requestDto.getUpdatedAt();
    }
}
