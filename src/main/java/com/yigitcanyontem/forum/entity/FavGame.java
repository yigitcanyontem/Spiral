package com.yigitcanyontem.forum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "favgames")
@AllArgsConstructor
@NoArgsConstructor
public class FavGame {
    @Id
    @SequenceGenerator(
            name = "favgame_id_seq",
            sequenceName = "favgame_id_seq",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "favgame_id_seq"
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
            name = "gameid",
            nullable = false
    )
    private String gameid;

    @Column(
            name = "gamename",
            nullable = true
    )
    private String gameName;

    @Column(
            name = "gameimage",
            nullable = true
    )
    private String gameImage;


    public FavGame(Users usersid, String gameid) {
        this.usersid = usersid;
        this.gameid = gameid;
    }

    public FavGame(Users usersid, String gameid, String gameName, String gameImage) {
        this.usersid = usersid;
        this.gameid = gameid;
        this.gameName = gameName;
        this.gameImage = gameImage;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameImage() {
        return gameImage;
    }

    public void setGameImage(String gameImage) {
        this.gameImage = gameImage;
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

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    @Override
    public String toString() {
        return "FavGames{" +
                "id=" + id +
                ", usersid=" + usersid +
                ", gameid=" + gameid +
                '}';
    }
}