package com.yigitcanyontem.forum.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yigitcanyontem.forum.entity.enums.EntertainmentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Date;

@Entity
@Table(
        name = "review",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "review_user_unique",
                        columnNames = {"usersid", "entertainmenttype","entertainmentid"}
                )
        })
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Review {
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

    @Column(
            name = "entertainmenttype",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private EntertainmentType entertainmentType;

    @Column(
            name = "entertainmentid",
            nullable = false
    )
    private String entertainmentid;

    @Column(
            name = "description",
            columnDefinition = "TEXT",
            nullable = true
    )
    private String description;
    @Column(
            name = "title",
            nullable = true
    )
    private String title;

    @Column(
            name = "rating",
            nullable = false
    )
    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(
            name = "upvote",
            nullable = false
    )
    private Integer upvote;

    @Column(
            name = "downvote",
            nullable = false
    )
    private Integer downvote;

    @Column(
            name = "date",
            nullable = false
    )
    private Date date;

    public Review(Users usersid, EntertainmentType entertainmentType, String entertainmentid, String description,String title, Integer rating, Integer upvote, Integer downvote, Date date) {
        this.usersid = usersid;
        this.entertainmentType = entertainmentType;
        this.entertainmentid = entertainmentid;
        this.description = description;
        this.title = title;
        this.rating = rating;
        this.upvote = upvote;
        this.downvote = downvote;
        this.date = date;
    }



    public String getEntertainmentId() {
        return entertainmentid;
    }

    public void setEntertainmentId(String entertainmentId) {
        this.entertainmentid = entertainmentId;
    }

    public String getEntertainmentid() {
        return entertainmentid;
    }

    public void setEntertainmentid(String entertainmentid) {
        this.entertainmentid = entertainmentid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public EntertainmentType getEntertainmentType() {
        return entertainmentType;
    }

    public void setEntertainmentType(EntertainmentType entertainmentType) {
        this.entertainmentType = entertainmentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getUpvote() {
        return upvote;
    }

    public void setUpvote(Integer upvote) {
        this.upvote = upvote;
    }

    public Integer getDownvote() {
        return downvote;
    }

    public void setDownvote(Integer downvote) {
        this.downvote = downvote;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
