package com.sparta.gitandrun.Review.service;

import com.sparta.gitandrun.Review.dto.ReviewRequestDto;
import com.sparta.gitandrun.Review.dto.ReviewResponseDto;
import com.sparta.gitandrun.Review.entity.Review;
import com.sparta.gitandrun.Review.repository.ReviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    //리뷰 작성
    public Review createReview(ReviewRequestDto requestDto, String username) {
        Review review = new Review(requestDto, username);
        return reviewRepository.save(review);
    }

    //리뷰 전체 조회
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    //리뷰 단일 조회
    public ReviewResponseDto getOneReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        return new ReviewResponseDto(review);
    }

    //리뷰 수정
    @Transactional
    public void updateReview(UUID reviewId, ReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        if (requestDto.getReviewContent() != null && !requestDto.getReviewContent().isEmpty()) {
            review.setReviewContent(requestDto.getReviewContent());
        }

        if (requestDto.getReviewRating() != null) {
            review.setReviewRating(requestDto.getReviewRating());
        }

        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }
}