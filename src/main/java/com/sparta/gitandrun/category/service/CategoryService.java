package com.sparta.gitandrun.category.service;

import com.sparta.gitandrun.category.entity.Category;
import com.sparta.gitandrun.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

//    @PostConstruct
//    @Transactional
//    public void initializeCategories() {
//        if (categoryRepository.count() == 0) {
//            categoryRepository.saveAll(List.of(
//                    new Category(UUID.fromString("11111111-1111-1111-1111-111111111111"), "KOREAN"),
//                    new Category(UUID.fromString("22222222-2222-2222-2222-222222222222"), "CHINESE"),
//                    new Category(UUID.fromString("33333333-3333-3333-3333-333333333333"), "SNACK"),
//                    new Category(UUID.fromString("44444444-4444-4444-4444-444444444444"), "CHICKEN"),
//                    new Category(UUID.fromString("55555555-5555-5555-5555-555555555555"), "PIZZA")
//            ));
//        }
//    }

    @PostConstruct
    @Transactional
    public void initializeCategories() {
        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(List.of(
                    new Category("KOREAN"),
                    new Category("CHINESE"),
                    new Category("SNACK"),
                    new Category("CHICKEN"),
                    new Category("PIZZA")
            ));
        }
    }

    // 새로운 카테고리를 추가하는 메서드
    @Transactional
    public Category addCategory(String name) {
        Category category = new Category(name);
        return categoryRepository.save(category);
    }

    // 카테고리 조회 메서드
    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    // 모든 카테고리를 조회하는 메서드
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 카테고리 이름 수정 메서드
    @Transactional
    public Category updateCategory(UUID id, String newName) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        category.setName(newName);
        return categoryRepository.save(category);
    }

    // 카테고리 삭제 메서드
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        categoryRepository.delete(category);
    }
}
