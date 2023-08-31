package com.yigitcanyontem.forum.model.entertainment;



public class AssignModel {
    private Integer id;
    private String description;
    private String instagramuser;
    private String pinterestuser;
    private String linkedinuser;
    private String twitteruser;

    public AssignModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstagramuser() {
        return instagramuser;
    }

    public void setInstagramuser(String instagramuser) {
        this.instagramuser = instagramuser;
    }

    public String getPinterestuser() {
        return pinterestuser;
    }

    public void setPinterestuser(String pinterestuser) {
        this.pinterestuser = pinterestuser;
    }

    public String getLinkedinuser() {
        return linkedinuser;
    }

    public void setLinkedinuser(String linkedinuser) {
        this.linkedinuser = linkedinuser;
    }

    public String getTwitteruser() {
        return twitteruser;
    }

    public void setTwitteruser(String twitteruser) {
        this.twitteruser = twitteruser;
    }
}
