package com.sparta.gitandrun.googleAi.controller;

import com.sparta.gitandrun.googleAi.dto.AiDto;
import com.sparta.gitandrun.googleAi.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @Secured("ROLE_OWNER")
    @PostMapping
    public String createQuestion(@RequestBody String text) { // json 받아오게
        return aiService.createQuestion(text);
    }

    @GetMapping
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public List<AiDto> ReadQuestions(){
        return aiService.getAllQuestions();
    }

}
