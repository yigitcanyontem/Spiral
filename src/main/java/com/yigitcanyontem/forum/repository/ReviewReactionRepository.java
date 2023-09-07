package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.FavGame;
import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.entity.ReviewReaction;
import com.yigitcanyontem.forum.entity.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReactionRepository extends JpaRepository<ReviewReaction, Long> {
    boolean existsReviewReactionByUsersid_IdAndReviewid_Id(Integer usersid_id, Long reviewid_id);
    Integer countReviewReactionsByReviewid_IdAndReactionType(Long reviewid_id, ReactionType reactionType);
    ReviewReaction findReviewReactionByUsersid_IdAndReviewid_Id(Integer usersid_id, Long reviewid_id);
    void deleteReviewReactionsByReviewid(Review reviewid);
}
