package com.yigitcanyontem.forum.service;

import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.entity.ReviewReaction;
import com.yigitcanyontem.forum.entity.Users;
import com.yigitcanyontem.forum.entity.enums.ReactionType;
import com.yigitcanyontem.forum.model.review.ReactionCreateDTO;
import com.yigitcanyontem.forum.repository.ReviewReactionRepository;
import com.yigitcanyontem.forum.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewReactionService {
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;
    private final UsersService usersService;

    public Review addReaction(ReactionCreateDTO reactionCreateDTO) {
        Review review = reviewRepository.findReviewById(reactionCreateDTO.getReviewid());
        if (Objects.equals(review.getUsersid().getId(), reactionCreateDTO.getUsersid())){
            throw new RuntimeException("Can't react to own comment");
        }

        if (reviewReactionRepository.existsReviewReactionByUsersid_IdAndReviewid_Id(reactionCreateDTO.getUsersid(),reactionCreateDTO.getReviewid())){
            updateReaction(reactionCreateDTO);
        }else {
            ReviewReaction reviewReaction = new ReviewReaction();
            reviewReaction.setReviewid(reviewRepository.findReviewById(reactionCreateDTO.getReviewid()));
            reviewReaction.setUsersid(usersService.getUser(reactionCreateDTO.getUsersid()));
            reviewReaction.setReactionType(reactionCreateDTO.getReactionType());
            reviewReactionRepository.save(reviewReaction);
        }

        review.setUpvote(reviewReactionRepository.countReviewReactionsByReviewid_IdAndReactionType(review.getId(),ReactionType.UPVOTE));
        review.setDownvote(reviewReactionRepository.countReviewReactionsByReviewid_IdAndReactionType(review.getId(),ReactionType.DOWNVOTE));
        return reviewRepository.save(review);
    }

    public void updateReaction(ReactionCreateDTO reactionCreateDTO) {
        Long reviewid = reactionCreateDTO.getReviewid();
        ReactionType reactionType = reactionCreateDTO.getReactionType();
        ReviewReaction reviewReaction = reviewReactionRepository.findReviewReactionByUsersid_IdAndReviewid_Id(reactionCreateDTO.getUsersid(),reviewid);
        if (reviewReaction.getReactionType().equals(reactionType)){
            deleteReaction(reviewReaction.getId());
        }else {
            reviewReaction.setReactionType(reactionType);
            reviewReactionRepository.save(reviewReaction);
        }

    }

    public void deleteReactionsOfReview(Review review) {
        reviewReactionRepository.deleteReviewReactionsByReviewid(review);
    }

    @Transactional
    public void deleteReaction(Long id) {
        reviewReactionRepository.deleteById(id);
    }
}
