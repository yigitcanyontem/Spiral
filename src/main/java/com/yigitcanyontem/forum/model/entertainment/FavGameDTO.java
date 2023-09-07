package com.yigitcanyontem.forum.model.entertainment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FavGameDTO {
    private Integer id;
    private Integer usersid;
    private String gameid;
    private String gameName;
    private String gameImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsersid() {
        return usersid;
    }

    public void setUsersid(Integer usersid) {
        this.usersid = usersid;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
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
}
