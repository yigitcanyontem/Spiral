package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.enums.EntertainmentType;
import com.yigitcanyontem.forum.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findReviewsByUsersid_IdAndRating(Integer usersid_id, @Min(1) @Max(5) Integer rating, Pageable pageable);
    Page<Review> findReviewsByUsersid_Id(Integer usersid_id, Pageable pageable);
    Page<Review> findReviewsByEntertainmentTypeAndEntertainmentidAndRating(EntertainmentType entertainmentType, String entertainmentid, @Min(1) @Max(5) Integer rating, Pageable pageable);
    Page<Review> findReviewsByEntertainmentTypeAndEntertainmentid(EntertainmentType entertainmentType, String entertainmentId, Pageable pageable);

    Review findReviewById(Long id);
    List<Review> findReviewsByEntertainmentTypeAndEntertainmentid(EntertainmentType entertainmentType, String entertainmentId);
}
