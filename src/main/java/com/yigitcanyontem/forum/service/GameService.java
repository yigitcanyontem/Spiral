package com.yigitcanyontem.forum.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.repository.FavGameRepository;
import com.yigitcanyontem.forum.model.entertainment.Game;
import com.yigitcanyontem.forum.model.entertainment.GameSearchDTO;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    @Value("${giantbomb_api_key}")
    String giantbomb_api_key;

    String singleurlstart = "https://www.giantbomb.com/api/game/";
    String singleurlend = "/?api_key=giantbomb_api_key&format=json";
    String searchurlstart = "https://www.giantbomb.com/api/search/?api_key=giantbomb_api_key&format=json&query=";
    String searchurlend = "&resources=game";

    private final FavGameRepository favGameRepository;

    private CloseableHttpClient httpClient = HttpClients.custom()
            .setUserAgent("Mozilla/5.0 Firefox/26.0")
            .build();

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<GameSearchDTO> getGameSearchResults(String title) throws IOException {
        title = title.replaceAll(" ","%20");
        searchurlstart = searchurlstart.replaceAll("giantbomb_api_key",giantbomb_api_key);
        String url = searchurlstart+title+searchurlend;

        final HttpGet request = new HttpGet(url);

        String response = httpClient.execute(request, new BasicHttpClientResponseHandler());

        JsonNode list = objectMapper.readTree(response).findValue("results");

        List<GameSearchDTO> games = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            GameSearchDTO game = new GameSearchDTO();
            game.setId(list.get(i).get("guid").asText());
            game.setName(list.get(i).get("name").asText());
            game.setOriginal_url(list.get(i).get("image").get("original_url").asText());
            games.add(game);
        }

        if (games.isEmpty()){
            throw new SearchNotFoundException("No Games Found");
        }
        return games;
    }

    public Game getSingleGameById(String gameid) throws IOException {
        singleurlend = singleurlend.replaceAll("giantbomb_api_key",giantbomb_api_key);
        String url = singleurlstart+gameid+singleurlend;

        final HttpGet request = new HttpGet(url);

        String executed = httpClient.execute(request, new BasicHttpClientResponseHandler());

        JsonNode response = objectMapper.readTree(executed).findValue("results");
        String id = response.get("guid").asText();
        Game game = new Game();
        game.setId(id);
        game.setName(response.get("name").asText());
        game.setReleaseDate(response.get("original_release_date").asText());
        game.setOriginal_url(response.get("image").get("original_url").asText());
        game.setIcon_url(response.get("image").get("icon_url").asText());
        game.setScreen_large_url(response.get("image").get("screen_large_url").asText());
        game.setDeck(response.get("deck").asText());
        game.setFavorite_count(favGameRepository.countFavGamesByGameid(id));

        JsonNode platforms = response.get("platforms");
        game.setPlatforms(platforms.findValuesAsText("name"));

        JsonNode franchises = response.get("franchises");
        game.setFranchises(franchises.findValuesAsText("name"));

        JsonNode genres = response.get("genres");
        game.setGenres(genres.findValuesAsText("name"));

        JsonNode developers = response.get("developers");
        game.setDevelopers(developers.findValuesAsText("name"));

        JsonNode publishers = response.get("publishers");
        game.setPublishers(publishers.findValuesAsText("name"));

        JsonNode characters = response.get("characters");
        game.setCharacters(characters.findValuesAsText("name"));

        JsonNode similar = response.get("similar_games");
        game.setSimilar_games(similar.findValuesAsText("name"));

        return game;
    }

}
