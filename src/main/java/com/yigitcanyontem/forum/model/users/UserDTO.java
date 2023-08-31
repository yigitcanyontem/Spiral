package com.yigitcanyontem.forum.model.users;


import com.yigitcanyontem.forum.entity.Country;

import java.sql.Date;

public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private Date date_of_birth;
    private Country country;
    private String email;
    private String username;

    public UserDTO(Integer id, String firstName, String lastName, Date date_of_birth, Country country, String email,  String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date_of_birth = date_of_birth;
        this.country = country;
        this.email = email;
        this.username = username;
    }


    public UserDTO(String firstName, String lastName, Date date_of_birth, Country country, String email, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date_of_birth = date_of_birth;
        this.country = country;
        this.email = email;
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

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
