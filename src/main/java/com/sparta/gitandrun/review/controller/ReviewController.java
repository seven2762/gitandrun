package com.sparta.gitandrun.review.controller;

import com.sparta.gitandrun.common.entity.ApiResDto;
import com.sparta.gitandrun.review.dto.AdminReviewResponseDto;
import com.sparta.gitandrun.review.dto.ReviewRequestDto;
import com.sparta.gitandrun.review.dto.UserReviewResponseDto;
import com.sparta.gitandrun.review.service.ReviewService;
import com.sparta.gitandrun.user.security.UserDetailsImpl;
import jakarta.validation.Valid;
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
    @Secured("ROLE_CUSTOMER")
    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid ReviewRequestDto requestDto,
            @PathVariable UUID orderId) {
        Long userId = userDetails.getUser().getUserId();
        reviewService.createReview(requestDto, userId, orderId);
        return ResponseEntity.ok().body(new ApiResDto("리뷰 작성 완료", HttpStatus.OK.value()));
    }

    // OWNER: 본인 가게 리뷰 조회
    @Secured("ROLE_OWNER")
    @GetMapping("/myStore/{storeId}")
    public ApiResDto getOwnerReviewsByStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID storeId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Long userId = userDetails.getUser().getUserId();
        Page<UserReviewResponseDto> reviews = reviewService.getOwnerReviewsByStore(userId, storeId, page, size, sortBy);
        return new ApiResDto("리뷰 조회 성공", 200, reviews);
    }

    // 모든 가게 리뷰 조회
    @GetMapping("/store/{storeId}")
    public ApiResDto getReviewsByStore(
            @PathVariable UUID storeId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Page<UserReviewResponseDto> reviews = reviewService.getReviewsByStore(storeId, page, size, sortBy);
        return new ApiResDto("리뷰 조회 성공", 200, reviews);
    }

    // CUSTOEMR - 본인이 작성한 리뷰 조회
    @Secured("ROLE_CUSTOMER")
    @GetMapping("/myReviews")
    public ResponseEntity<ApiResDto> getMyReviewsByUserId(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Long userId = userDetails.getUser().getUserId();
        Page<UserReviewResponseDto> reviews = reviewService.getMyReviewsByUserId(userId, page, size, sortBy);
        return ResponseEntity.ok().body(new ApiResDto("본인 리뷰 조회 성공", HttpStatus.OK.value(), reviews));
    }

    // 관리자 - 모든 리뷰 검색 (reviewContent, userId, reviewId, storeId)
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("/admin")
    public ApiResDto getReviewsByFilters(
            @RequestParam(required = false) String keyword,    // 키워드 검색
            @RequestParam(required = false) Long userId,      // userId로 검색
            @RequestParam(required = false) UUID reviewId,    // reviewId로 검색
            @RequestParam(required = false) UUID storeId,     // storeId로 검색
            @RequestParam(defaultValue = "false") boolean isDeleted, // 삭제 여부
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Page<AdminReviewResponseDto> reviews = reviewService.searchReviewsWithFilters(
                keyword, userId, reviewId, storeId, isDeleted, page, size, sortBy);
        return new ApiResDto("리뷰 조회 성공", 200, reviews);
    }

    //고객 - 리뷰 수정
    @Secured("ROLE_CUSTOMER")
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResDto> updateReviewUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID reviewId,
            @RequestBody @Valid ReviewRequestDto requestDto) {
        reviewService.updateReview(reviewId, userDetails, requestDto);
        return ResponseEntity.ok().body(new ApiResDto("리뷰 수정 완료", HttpStatus.OK.value()));
    }

    //관리자 - 리뷰 수정
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @PatchMapping("/admin/{reviewId}")
    public ResponseEntity<ApiResDto> updateReviewAdmin(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID reviewId,
            @RequestBody @Valid ReviewRequestDto requestDto) {
        reviewService.updateReview(reviewId, userDetails, requestDto);
        return ResponseEntity.ok().body(new ApiResDto("리뷰 수정 완료", HttpStatus.OK.value()));
    }

    //고객 - 리뷰 삭제
    @Secured("ROLE_CUSTOMER")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResDto> deleteReviewUser(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails);
        return ResponseEntity.ok().body(new ApiResDto("리뷰 삭제 완료", HttpStatus.OK.value()));
    }

    //관리자 - 리뷰 삭제
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @DeleteMapping("/admin/{reviewId}")
    public ResponseEntity<ApiResDto> deleteReviewAdmin(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails);
        return ResponseEntity.ok().body(new ApiResDto("리뷰 삭제 완료", HttpStatus.OK.value()));
    }

    //관리자 - 삭제된 리뷰 복구
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @PatchMapping("/admin/restore/{reviewId}")
    public ApiResDto restoreReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.restoreReview(reviewId, userDetails);
        return new ApiResDto("리뷰 복구 성공", 200);
    }
}

