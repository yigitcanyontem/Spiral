package com.yigitcanyontem.forum.model.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yigitcanyontem.forum.entity.Users;
import com.yigitcanyontem.forum.entity.enums.EntertainmentType;
import com.yigitcanyontem.forum.model.entertainment.Entertainment;
import com.yigitcanyontem.forum.model.users.UserDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private UserDTO usersid;
    private EntertainmentType entertainmentType;
    private String entertainmentid;
    private String entertainmentTitle;
    private String description;
    private String title;
    private Integer rating;
    private Integer upvote;
    private Integer downvote;
    private Date date;
    private String image;

    public String getEntertainmentTitle() {
        return entertainmentTitle;
    }

    public void setEntertainmentTitle(String entertainmentTitle) {
        this.entertainmentTitle = entertainmentTitle;
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

    public UserDTO getUsersid() {
        return usersid;
    }

    public void setUsersid(UserDTO usersid) {
        this.usersid = usersid;
    }

    public EntertainmentType getEntertainmentType() {
        return entertainmentType;
    }

    public void setEntertainmentType(EntertainmentType entertainmentType) {
        this.entertainmentType = entertainmentType;
    }

    public String getEntertainmentid() {
        return entertainmentid;
    }

    public void setEntertainmentid(String entertainmentid) {
        this.entertainmentid = entertainmentid;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
