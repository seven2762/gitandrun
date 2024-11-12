package com.sparta.gitandrun.googleAi.controller;

import com.sparta.gitandrun.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Ai")
@RequiredArgsConstructor
public class AiController {

    private final static String SECRET_KEY = "AIzaSyBok8WRU8OgJUvFnUFvD5DTEdzuuHlVTCU";

    private final MenuService menuService;

}
