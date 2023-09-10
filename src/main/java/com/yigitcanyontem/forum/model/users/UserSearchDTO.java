package com.yigitcanyontem.forum.model.users;

import com.yigitcanyontem.forum.entity.Country;
import org.springframework.core.io.Resource;

import java.sql.Date;

public class UserSearchDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;

    public UserSearchDTO(Integer id, String firstName, String lastName, String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
