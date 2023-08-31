package com.yigitcanyontem.forum.model.entertainment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Entertainment{
    String id;
    boolean adult;
    String backdrop_path;
    String original_title;
    String overview;
    String poster_path;
    String release_date;
    List<String> language;
    String imdb_url;
    Integer vote_count;
    Integer favorite_count;
    List<String> genres;
    String tagline;
    List<CrewMember> directors;
    List<CrewMember> actors;
    List<CrewMember> writers;

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public String getImdb_url() {
        return imdb_url;
    }

    public void setImdb_url(String imdb_url) {
        this.imdb_url = imdb_url;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public Integer getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(Integer favorite_count) {
        this.favorite_count = favorite_count;
    }

    public List<CrewMember> getDirectors() {
        return directors;
    }

    public void setDirectors(List<CrewMember> directors) {
        this.directors = directors;
    }

    public List<CrewMember> getActors() {
        return actors;
    }

    public void setActors(List<CrewMember> actors) {
        this.actors = actors;
    }

    public List<CrewMember> getWriters() {
        return writers;
    }

    public void setWriters(List<CrewMember> writers) {
        this.writers = writers;
    }
}

