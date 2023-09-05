package com.yigitcanyontem.forum.model.review;

import com.yigitcanyontem.forum.entity.enums.EntertainmentType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDTO {
    private Integer usersid;
    @Enumerated(EnumType.STRING)
    private EntertainmentType entertainmentType;
    private String title;
    private String entertainmentid;
    private String description;
    private Integer rating;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUsersid() {
        return usersid;
    }

    public void setUsersid(Integer usersid) {
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
}
