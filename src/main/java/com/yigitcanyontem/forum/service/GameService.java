package com.yigitcanyontem.forum.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.model.entertainment.Character;
import com.yigitcanyontem.forum.model.entertainment.CrewMember;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class GameService {
    @Value("${giantbomb_api_key}")
    String giantbomb_api_key;

    String singleurlstart = "https://www.giantbomb.com/api/game/";
    String singleurlend = "/?api_key=giantbomb_api_key&format=json";
    String searchurlstart = "https://www.giantbomb.com/api/search/?api_key=giantbomb_api_key&format=json&query=";
    String searchurlend = "&resources=game";
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    private final FavGameRepository favGameRepository;

    private CloseableHttpClient httpClient = HttpClients.custom()
            .setUserAgent("Mozilla/5.0 Firefox/26.0")
            .build();


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

    public Game getSingleGameDTOById(String gameid) throws IOException {
        singleurlend = singleurlend.replaceAll("giantbomb_api_key",giantbomb_api_key);
        String url = singleurlstart+gameid+singleurlend;

        final HttpGet request = new HttpGet(url);

        String executed = httpClient.execute(request, new BasicHttpClientResponseHandler());

        JsonNode response = objectMapper.readTree(executed).findValue("results");
        String id = response.get("guid").asText();
        Game game = new Game();
        game.setId(id);
        game.setName(response.get("name").asText());
        game.setOriginal_url(response.get("image").get("original_url").asText());
        return game;
    }


    /*public List<Game> getGameDTOsByUser(List<FavGame> favlist) throws ExecutionException, InterruptedException, JsonProcessingException {
        List<CompletableFuture<String>> urlList = new ArrayList<>();
        for (FavGame game:favlist){
            urlList.add(CompletableFuture.supplyAsync(() -> restTemplate.getForObject(singleurlstart+game.getGameid()+singleurlend,String.class)));
        }

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(urlList.toArray(new CompletableFuture[0]));
        combinedFuture.get();

        try {
            combinedFuture.get();
        } catch (Exception e) {
            throw e;
        }

        List<Game> games = new ArrayList<>();

        for (CompletableFuture<String> future : urlList) {
            try {
                Game game = new Game();
                JsonNode list = objectMapper.readTree(future.get()).findValue("results");
                game.setId(list.get("guid").asText());
                game.setName(list.get("name").asText());
                game.setOriginal_url(list.get("image").get("original_url").asText());
                games.add(game);
            } catch (Exception e) {
                throw e;
            }
        }
        return games;
    }*/

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

        JsonNode screenshots = response.get("images");
        game.setScreenshots(screenshots.findValuesAsText("original"));

        List<Character> characters = new ArrayList<>();
        JsonNode characterNode = response.get("characters");
        for (int i = 0; i < Math.min(characterNode.size(), 5); i++) {
            Character character = new Character();
            JsonNode value = characterNode.get(i);

            String charurl = value.get("api_detail_url").asText()+"?api_key="+giantbomb_api_key+"&format=json";

            final HttpGet request1 = new HttpGet(charurl);

            String executed1 = httpClient.execute(request1, new BasicHttpClientResponseHandler());

            JsonNode response1 = objectMapper.readTree(executed1).findValue("results");

            character.setId(response1.get("id").asText());
            character.setName(response1.get("name").asText());

            JsonNode images = response1.get("image");

            character.setMedium_url(images.get("original_url").asText());
            character.setOriginal_url(images.get("medium_url").asText());

            characters.add(character);
        }
        game.setCharacters(characters);
        JsonNode similar = response.get("similar_games");
        game.setSimilar_games(similar.findValuesAsText("name"));
        return game;
    }

    /*
    * public Game getSingleGameById(String gameid) throws IOException, ExecutionException, InterruptedException {
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

        List<Character> characters = new ArrayList<>();

        JsonNode characterNode = response.get("characters");
        List<String> urls = response.get("characters").findValuesAsText("api_detail_url").subList(0, 10);

        CompletableFuture<String>[] completableFutures = new CompletableFuture[urls.size()];

        for (int i = 0; i < urls.size(); i++) {
            final int index = i;
            completableFutures[i] = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(urls.get(index), String.class));
        }

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(completableFutures);
        combinedFuture.get();

        JsonNode[] jsonNodes = new JsonNode[completableFutures.length];

        for (int i = 0; i < completableFutures.length; i++) {
            jsonNodes[i] = objectMapper.readTree(completableFutures[i].get());
            Character character = new Character();

            character.setName(jsonNodes[i].get("id").asText());

            JsonNode images = jsonNodes[i].get("image");

            character.setMedium_url(images.get("original_url").asText());
            character.setOriginal_url(images.get("medium_url").asText());

            characters.add(character);
        }

        game.setCharacters(characters);
        JsonNode similar = response.get("similar_games");
        game.setSimilar_games(similar.findValuesAsText("name"));

        return game;
    }*/

}
