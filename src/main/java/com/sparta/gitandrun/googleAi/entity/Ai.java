package com.sparta.gitandrun.googleAi.entity;

import com.sparta.gitandrun.googleAi.dto.AiDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private UUID Id;

    private String question;

    private String answer;

    private LocalDateTime createdAt;

    public Ai(AiDto aiDto){
        this.question = aiDto.getQuestion();
        this.answer = aiDto.getAnswer();
        this.createdAt = LocalDateTime.now();
    }
}
