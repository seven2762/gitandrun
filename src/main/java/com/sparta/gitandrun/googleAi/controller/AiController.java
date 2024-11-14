package com.sparta.gitandrun.googleAi.controller;

import com.sparta.gitandrun.googleAi.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    //Question CREATE
    @PostMapping
    public String createQuestion(@RequestBody String text) { // json 받아오게
        return aiService.createQuestion(text);
    }

}
