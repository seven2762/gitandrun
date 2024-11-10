package com.sparta.gitandrun.Review.service;

import com.sparta.gitandrun.Review.dto.ReviewRequestDto;
import com.sparta.gitandrun.Review.dto.ReviewResponseDto;
import com.sparta.gitandrun.Review.entity.Review;
import com.sparta.gitandrun.Review.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}