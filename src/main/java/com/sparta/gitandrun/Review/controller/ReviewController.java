package com.sparta.gitandrun.Review.controller;

import com.sparta.gitandrun.Review.dto.ReviewRequestDto;
import com.sparta.gitandrun.Review.dto.ReviewResponseDto;
import com.sparta.gitandrun.Review.entity.Review;
import com.sparta.gitandrun.Review.service.ReviewService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 작성
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequestDto requestDto) {
        String username = "testUser"; // 임시로 설정한 사용자 이름
        Review review = reviewService.createReview(requestDto, username);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        List<ReviewResponseDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    //리뷰 단일 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getOneReview(@PathVariable UUID reviewId) {
        ReviewResponseDto review = reviewService.getOneReview(reviewId);
        return ResponseEntity.ok(review);
    }
}

