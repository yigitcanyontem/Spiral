package com.yigitcanyontem.forum.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "favshows")
@NoArgsConstructor
public class FavShows {
    @Id
    @SequenceGenerator(
            name = "favshows_id_seq",
            sequenceName = "favshows_id_seq",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "favshows_id_seq"
    )
    @Column(name = "id")
    private Integer id;

    @ManyToOne()
    @JoinColumn(
            name = "usersid",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Users usersid;

    @Column(
            name = "showid",
            nullable = false
    )
    private Integer showid;

    public FavShows(Users usersid, Integer showid) {
        this.usersid = usersid;
        this.showid = showid;
    }

    public FavShows(Integer id, Users usersid, Integer showid) {
        this.id = id;
        this.usersid = usersid;
        this.showid = showid;
    }

    @Override
    public String toString() {
        return "FavShows{" +
                "id=" + id +
                ", usersid=" + usersid +
                ", showid=" + showid +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUsersid() {
        return usersid;
    }

    public void setUsersid(Users usersid) {
        this.usersid = usersid;
    }

    public Integer getShowid() {
        return showid;
    }

    public void setShowid(Integer showid) {
        this.showid = showid;
    }
}