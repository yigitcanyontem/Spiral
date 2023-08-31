package com.yigitcanyontem.forum.model.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateDTO {
    private Long id;
    private Integer usersid;
    private String description;
    @Min(1)
    @Max(5)
    private Integer rating;

    public ReviewUpdateDTO(Long id,Integer rating) {
        this.id = id;
        this.rating = rating;
    }

    public ReviewUpdateDTO(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
