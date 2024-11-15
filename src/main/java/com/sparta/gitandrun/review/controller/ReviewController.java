package com.sparta.gitandrun.review.controller;

import com.sparta.gitandrun.review.dto.ReviewRequestDto;
import com.sparta.gitandrun.review.dto.ReviewResponseDto;
import com.sparta.gitandrun.review.entity.Review;
import com.sparta.gitandrun.review.service.ReviewService;
import com.sparta.gitandrun.user.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 작성
    @PostMapping("/write")
    public ResponseEntity<ReviewResponseDto> createReview(
            @RequestBody ReviewRequestDto requestDto,
            @RequestParam Long userId,
            @RequestParam Long orderId) {
        Review review = reviewService.createReview(requestDto, userId, orderId);
        return ResponseEntity.ok(new ReviewResponseDto(review));
    }

    //리뷰 전체 조회
    @GetMapping("/all")
    public ResponseEntity<Page<ReviewResponseDto>> getAllReviews(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Page<ReviewResponseDto> reviews = reviewService.getAllReviews(page, size, sortBy);
        return ResponseEntity.ok(reviews);
    }

    //리뷰 단일 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getOneReview(@PathVariable UUID reviewId) {
        ReviewResponseDto review = reviewService.getOneReview(reviewId);
        return ResponseEntity.ok(review);
    }

    //현재 로그인한 사용자가 쓴 리뷰 조회
    @GetMapping("/my-reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByCurrentUser(
            @AuthenticationPrincipal User user,  // 로그인한 사용자 정보를 가져옴
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        // 로그인한 사용자의 ID를 이용해 해당 사용자의 리뷰를 조회
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByUser(user.getUserId(), page, size, sortBy);
        return ResponseEntity.ok(reviews);
    }

    //storeId로 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByStore(
            @PathVariable UUID storeId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByStore(storeId, page, size, sortBy);
        return ResponseEntity.ok(reviews);
    }

    //reviewContent에서 키워드로 검색
    @GetMapping("/search")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByKeyword(keyword, page, size, sortBy);
        return ResponseEntity.ok(reviews);
    }

    //리뷰 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable UUID reviewId,
                                               @RequestBody ReviewRequestDto requestDto) {
        reviewService.updateReview(reviewId, requestDto);
        return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
    }

    //리뷰 삭제
    @DeleteMapping("{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable UUID reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }
}

