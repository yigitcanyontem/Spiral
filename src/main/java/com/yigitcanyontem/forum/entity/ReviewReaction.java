package com.yigitcanyontem.forum.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yigitcanyontem.forum.entity.enums.ReactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "review_reaction",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "review_reaction_user_unique",
                        columnNames = {"usersid", "reviewid"}
                )
        })
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewReaction {
    @Id
    @SequenceGenerator(
            name = "review_id_seq",
            sequenceName = "review_id_seq",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "review_id_seq"
    )
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(
            name = "usersid",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Users usersid;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(
            name = "reviewid",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Review reviewid;

    @Column(
            name = "reaction_type",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUsersid() {
        return usersid;
    }

    public void setUsersid(Users usersid) {
        this.usersid = usersid;
    }

    public Review getReviewid() {
        return reviewid;
    }

    public void setReviewid(Review reviewid) {
        this.reviewid = reviewid;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}
