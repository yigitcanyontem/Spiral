package com.yigitcanyontem.forum.model.auth;

import com.yigitcanyontem.forum.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstName;
  private String lastName;
  private Date date_of_birth;
  private Integer country;
  private String email;
  private String username;
  private String password;

  @Override
  public String toString() {
    return "RegisterRequest{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", date_of_birth=" + date_of_birth +
            ", country=" + country +
            ", email='" + email + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
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

  public Integer getCountry() {
    return country;
  }

  public void setCountry(Integer country) {
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
