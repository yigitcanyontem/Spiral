package com.yigitcanyontem.forum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigitcanyontem.forum.entity.FavAlbums;
import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.repository.FavAlbumsRepository;
import com.yigitcanyontem.forum.model.entertainment.Album;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class AlbumService {
    @Value("${last_fm_api_key}")
    String last_fm_api_key;
    @Value("${last_fm_url}")
    String last_fm_url;

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final UsersService usersService;

    private final FavAlbumsRepository favAlbumsRepository;

    public List<Album> getAlbumSearchResults(String title) throws JsonProcessingException {
        String url = last_fm_url+"search&album="+title+"&api_key="+last_fm_api_key+"&format=json";
        String json = restTemplate.getForObject(url,String.class);

        JsonNode list = objectMapper.readTree(json).findValue("album");
        List<Album> albums = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).get("mbid").asText().equals("")){
                continue;
            }
            Album album = new Album();
            JsonNode images = list.get(i).findValue("image");
            album.setMbid(list.get(i).findValue("mbid").asText());
            album.setName(list.get(i).path("name").asText());
            album.setImage(images.get(3).findValue("#text").asText());
            album.setArtist(list.get(i).findValue("artist").asText());
            albums.add(album);
        }
        if (albums.size() == 0){
            throw new SearchNotFoundException("No Album Found");
        }
        return albums;
    }

    public List<Album> getAlbumDTOsByUser(List<FavAlbums> favlist)  {
        List<UrlCompletableFuture<String>> urlList = new ArrayList<>();
        for (FavAlbums album : favlist) {
            String url = last_fm_url + "getinfo&api_key=" + last_fm_api_key + "&mbid=" + album.getAlbumid() + "&format=json";
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(url, String.class));
            urlList.add(new UrlCompletableFuture<>(future, album.getAlbumid()));
        }

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(
                urlList.stream()
                        .map(UrlCompletableFuture::getFuture)
                        .toArray(CompletableFuture[]::new)
        );

        try {
            combinedFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Album> albums = new ArrayList<>();

        for (UrlCompletableFuture<String> urlCompletableFuture : urlList) {
            try {
                Album album = new Album();
                String response = urlCompletableFuture.getFuture().get();
                JsonNode list = objectMapper.readTree(response).findValue("album");

                JsonNode images = list.findValue("image");
                album.setMbid(urlCompletableFuture.getUrl());
                album.setName(list.path("name").asText());
                album.setImage(images.get(3).findValue("#text").asText());
                albums.add(album);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return albums;
    }

    public Album getSingleAlbumById(String mbid) throws JsonProcessingException {
        String url = last_fm_url+"getinfo&api_key="+last_fm_api_key+"&mbid="+mbid+"&format=json";

        try {
            String json = restTemplate.getForObject(url,String.class);
        }catch (HttpClientErrorException e){
            throw new SearchNotFoundException("Album with id " +mbid+ " doesn't exist");
        }
        String json = restTemplate.getForObject(url,String.class);

        JsonNode list = objectMapper.readTree(json).findPath("album");

        Album album = new Album();
        JsonNode images = list.findValue("image");
        album.setMbid(mbid);
        album.setName(list.path("name").asText());
        album.setArtist(list.findValue("artist").asText());
        album.setUrl("https://www.last.fm/music/"+album.getArtist()+"/"+album.getName());
        album.setImage(images.get(3).findValue("#text").asText());
        album.setFavorite_count(favAlbumsRepository.countFavAlbumsByAlbumid(mbid));
        return album;
    }
}
class UrlCompletableFuture<T> {
    private CompletableFuture<T> future;
    private String url;

    public UrlCompletableFuture(CompletableFuture<T> future, String url) {
        this.future = future;
        this.url = url;
    }

    public CompletableFuture<T> getFuture() {
        return future;
    }

    public String getUrl() {
        return url;
    }
}