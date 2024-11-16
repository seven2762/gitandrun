package com.sparta.gitandrun.category.controller;

import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.category.dto.CategoryRequestDto;
import com.sparta.gitandrun.category.entity.Category;
import com.sparta.gitandrun.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 모든 카테고리 조회
    @GetMapping
    public ResponseEntity<ApiResDto> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        ApiResDto response = new ApiResDto("카테고리 조회 성공", 200, categories);
        return ResponseEntity.ok(response);
    }

    // 새로운 카테고리 추가
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @PostMapping
    public ResponseEntity<ApiResDto> createCategory(@RequestBody CategoryRequestDto categoryRequest) {
        try {
            Category newCategory = categoryService.addCategory(categoryRequest.getName());
            ApiResDto response = new ApiResDto("카테고리 추가 성공", 201, newCategory);
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            ApiResDto errorResponse = new ApiResDto("카테고리 추가 실패: " + e.getMessage(), 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 카테고리 수정
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @PatchMapping("/{categoryId}")
    public ResponseEntity<ApiResDto> updateCategory(@PathVariable UUID categoryId, @RequestBody CategoryRequestDto categoryRequest) {
        try {
            Category updatedCategory = categoryService.updateCategory(categoryId, categoryRequest.getName());
            ApiResDto response = new ApiResDto("카테고리 이름이 성공적으로 수정되었습니다.", 200, updatedCategory);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResDto errorResponse = new ApiResDto("카테고리 수정 실패: " + e.getMessage(), 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 카테고리 삭제
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResDto> deleteCategory(@PathVariable UUID categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            ApiResDto response = new ApiResDto("카테고리 삭제 성공", 200);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResDto errorResponse = new ApiResDto("카테고리 삭제 실패: " + e.getMessage(), 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
