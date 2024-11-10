package com.sparta.gitandrun.Review.dto;

import com.sparta.gitandrun.Review.entity.Review;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private UUID reviewId;
    private String username; //임시 추가
    private String reviewContent;
    private Short reviewRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.username = review.getUsername();
        this.reviewContent = review.getReviewContent();
        this.reviewRating = review.getReviewRating();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
