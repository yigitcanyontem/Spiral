package com.yigitcanyontem.forum.model.entertainment;

public class Album implements Entertainment{
    String mbid;
    String name;
    String artist;
    String image;
    String url;
    Integer favorite_count;

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(Integer favorite_count) {
        this.favorite_count = favorite_count;
    }

    @Override
    public String toString() {
        return "Album{" +
                "mbid='" + mbid + '\'' +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", favorite_count=" + favorite_count +
                '}';
    }
}
