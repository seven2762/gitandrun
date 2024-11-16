package com.sparta.gitandrun.review.dto;

import com.sparta.gitandrun.review.entity.Review;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserReviewResponseDto {
    private String nick_name;
    private String reviewContent;
    private Short reviewRating;
    private LocalDateTime createdAt;

    public UserReviewResponseDto(Review review) {
        this.reviewContent = review.getReviewContent();
        this.reviewRating = review.getReviewRating();
        this.nick_name = review.getUser().getNickName();
        this.createdAt = review.getCreatedAt();
    }
}