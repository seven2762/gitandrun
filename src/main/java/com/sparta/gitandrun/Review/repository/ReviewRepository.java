package com.sparta.gitandrun.Review.repository;

import com.sparta.gitandrun.Review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
