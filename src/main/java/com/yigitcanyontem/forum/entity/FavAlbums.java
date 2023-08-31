package com.yigitcanyontem.forum.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "favalbums")
public class FavAlbums {
    @Id
    @SequenceGenerator(
            name = "favalbums_id_seq",
            sequenceName = "favalbums_id_seq",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "favalbums_id_seq"
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
            name = "albumid",
            nullable = false
    )
    private String albumid;

    public FavAlbums(Users usersid, String albumid) {
        this.usersid = usersid;
        this.albumid = albumid;
    }

    public FavAlbums() {

    }

    @Override
    public String toString() {
        return "FavAlbums{" +
                "id=" + id +
                ", usersid=" + usersid +
                ", albumid='" + albumid + '\'' +
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

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }
}