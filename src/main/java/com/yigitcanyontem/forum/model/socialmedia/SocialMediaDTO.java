package com.yigitcanyontem.forum.model.socialmedia;


public class SocialMediaDTO {
    private Integer usersid;
    private String instagram;
    private String pinterest;
    private String linkedin;
    private String twitter;

    public SocialMediaDTO(Integer usersid, String instagram, String pinterest, String linkedin, String twitter) {
        this.usersid = usersid;
        this.instagram = instagram;
        this.pinterest = pinterest;
        this.linkedin = linkedin;
        this.twitter = twitter;
    }

    public Integer getUsersid() {
        return usersid;
    }

    public void setUsersid(Integer usersid) {
        this.usersid = usersid;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getPinterest() {
        return pinterest;
    }

    public void setPinterest(String pinterest) {
        this.pinterest = pinterest;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
