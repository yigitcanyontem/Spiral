package com.yigitcanyontem.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "description")
@NoArgsConstructor
@ToString
public class Description {
    @Id
    @Column(name = "usersid")
    private Integer usersid;

    @Column(name = "description")
    private String description;

    public Description(Integer usersid, String description) {
        this.usersid = usersid;
        this.description = description;
    }

    public Integer getUsersid() {
        return usersid;
    }

    public void setUsersid(Integer usersid) {
        this.usersid = usersid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
