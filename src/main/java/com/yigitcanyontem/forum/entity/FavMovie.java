package com.yigitcanyontem.forum.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "favmovie")
public class FavMovie {
    @Id
    @SequenceGenerator(
            name = "favmovie_id_seq",
            sequenceName = "favmovie_id_seq",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "favmovie_id_seq"
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
            name = "movieid",
            nullable = false
    )
    private Integer movieid;

    public FavMovie(Users usersid, Integer movieid) {
        this.usersid = usersid;
        this.movieid = movieid;
    }

    public FavMovie() {

    }

    @Override
    public String toString() {
        return "FavMovie{" +
                "id=" + id +
                ", usersid=" + usersid +
                ", movieid=" + movieid +
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

    public Integer getMovieid() {
        return movieid;
    }

    public void setMovieid(Integer movieid) {
        this.movieid = movieid;
    }


}