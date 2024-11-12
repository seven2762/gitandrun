package com.sparta.gitandrun.googleAi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_ai")
@NoArgsConstructor
public class Ai {

    @Id
    @Column(name = "aiId", columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID menuId;

    private String question;

    private String answer;

}
