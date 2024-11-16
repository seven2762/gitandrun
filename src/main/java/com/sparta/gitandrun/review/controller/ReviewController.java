package com.sparta.gitandrun.review.controller;

import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.review.dto.ReviewRequestDto;
import com.sparta.gitandrun.review.service.ReviewService;
import com.sparta.gitandrun.user.security.UserDetailsImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    @Secured({"ROLE_CUSTOMER", "ROLE_OWNER"})
    @PostMapping("/write/{orderId}")
    public ResponseEntity<ApiResDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ReviewRequestDto requestDto,
            @PathVariable Long orderId) {
        Long userId = userDetails.getUser().getUserId();
        reviewService.createReview(requestDto, userId, orderId);
        return ResponseEntity.ok().body(new ApiResDto("리뷰 작성 완료", HttpStatus.OK.value()));
    }

    //공통 - 가게별 리뷰 조회
    @GetMapping("/storeId/{storeId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByStore(
            @PathVariable UUID storeId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByStore(storeId, page, size, sortBy);
        return ResponseEntity.ok(reviews);
    }

    // 사용자 - 본인 리뷰 조회
    @GetMapping("/userId/{userId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId, page, size, sortBy);
        return ResponseEntity.ok(reviews);
    }

    // 관리자 - 모든 리뷰 조회
    @GetMapping("/all")
    public ResponseEntity<Page<ReviewResponseDto>> getAllReviews(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Page<ReviewResponseDto> reviews = reviewService.getAllReviews(page, size, sortBy);
        return ResponseEntity.ok(reviews);
    }

    // 관리자 - 리뷰 아이디로 조회
    @GetMapping("/reviewId/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getOneReview(@PathVariable UUID reviewId) {
        ReviewResponseDto review = reviewService.getOneReview(reviewId);
        return ResponseEntity.ok(review);
    }

    // 관리자 - 키워드로 리뷰 검색
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

