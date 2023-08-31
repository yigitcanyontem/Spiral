package com.yigitcanyontem.forum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.model.entertainment.CrewMember;
import com.yigitcanyontem.forum.model.entertainment.Movie;
import com.yigitcanyontem.forum.repository.FavShowsRepository;
import com.yigitcanyontem.forum.model.entertainment.Show;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ShowService {
    @Value("${tmdb_api_key}")
    String tmdb_api_key;

    private final FavShowsRepository favShowsRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    public Show getSingleShowDTOById(Integer showid) throws JsonProcessingException, ExecutionException, InterruptedException {
        String url = "https://api.themoviedb.org/3/tv/"+showid+"?api_key="+ tmdb_api_key;
        CompletableFuture<String> showInfoFuture = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(url, String.class));

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(showInfoFuture);
        combinedFuture.get();

        String showJson = showInfoFuture.get();

        JsonNode shows = objectMapper.readTree(showJson);

        Show show = new Show();

        show.setId(shows.get("id").asText());
        show.setPoster_path("https://image.tmdb.org/t/p/w500"+shows.get("poster_path").asText());
        return show;
    }

    public Show getSingleShowById(Integer showid) throws JsonProcessingException, ExecutionException, InterruptedException {
        String url = "https://api.themoviedb.org/3/tv/"+showid+"?api_key="+ tmdb_api_key;
        String credit_url = "https://api.themoviedb.org/3/tv/"+showid+"/credits?api_key="+ tmdb_api_key;


        CompletableFuture<String> showInfoFuture = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(url, String.class));
        CompletableFuture<String> creditInfoFuture = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(credit_url, String.class));

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(showInfoFuture, creditInfoFuture);
        combinedFuture.get();

        String showJson = showInfoFuture.get();
        String creditJson = creditInfoFuture.get();

        JsonNode shows = objectMapper.readTree(showJson);
        JsonNode credits = objectMapper.readTree(creditJson);

        Show show = new Show();

        show.setId(shows.get("id").asText());
        show.setAdult(shows.get("adult").asBoolean());
        show.setBackdrop_path("https://image.tmdb.org/t/p/w500"+shows.get("backdrop_path").asText());
        show.setOriginal_title(shows.get("original_name").asText());
        show.setOverview(shows.get("overview").asText());
        show.setPoster_path("https://image.tmdb.org/t/p/w500"+shows.get("poster_path").asText());
        show.setFirst_air_date(shows.get("first_air_date").asText());
        show.setLast_air_date(shows.get("last_air_date").asText());
        show.setOriginal_language(shows.get("original_language").asText());
        show.setStatus(shows.get("status").asText());
        show.setVote_count(shows.get("vote_count").asInt());
        show.setFavorite_count(favShowsRepository.countFavShowsByShowid(showid));

        List<CrewMember> producers = new ArrayList<>();
        List<CrewMember> actors = new ArrayList<>();


        JsonNode castArray = credits.get("cast");
        for (int i = 0; i < Math.min(castArray.size(), 10); i++) {
            CrewMember crewMember = new CrewMember();

            JsonNode cast = castArray.get(i);
            crewMember.setId(cast.get("id").asInt());
            crewMember.setGender(cast.get("gender").asInt());
            crewMember.setOriginal_name(cast.get("original_name").asText());
            crewMember.setCharacter(cast.get("character").asText());
            crewMember.setProfile_path("https://image.tmdb.org/t/p/original"+cast.get("profile_path").asText());
            actors.add(crewMember);
        }
        JsonNode crewArray = credits.get("crew");
        for (JsonNode crew : crewArray) {
            CrewMember crewMember = new CrewMember();
            String job = crew.get("job").asText();
            if (job.equals("Executive Producer")) {
                crewMember.setId(crew.get("id").asInt());
                crewMember.setGender(crew.get("gender").asInt());
                crewMember.setOriginal_name(crew.get("original_name").asText());
                crewMember.setProfile_path("https://image.tmdb.org/t/p/original"+crew.get("profile_path").asText());
                producers.add(crewMember);
            }
        }

        show.setActors(actors);
        show.setProducers(producers);
        return show;
    }
    public List<Show> getShowSearchResults(String title) throws JsonProcessingException {
        String url = "https://api.themoviedb.org/3/search/tv?api_key="+ tmdb_api_key +"&query="+title;

        String json = restTemplate.getForObject(url,String.class);

        JsonNode list = objectMapper.readTree(json).findValue("results");
        List<Show> shows = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).get("poster_path").asText().equals("null")){
                continue;
            }
            Show show = new Show();
            show.setId(list.get(i).get("id").asText());
            show.setOriginal_title(list.get(i).get("original_name").asText());
            show.setPoster_path("https://image.tmdb.org/t/p/w500"+list.get(i).get("poster_path").asText());
            show.setFirst_air_date(list.get(i).get("first_air_date").asText());
            show.setVote_count(list.get(i).get("vote_count").asInt());
            shows.add(show);
        }
        shows.sort(Comparator.comparing(Show::getVote_count).reversed());
        if (shows.size() == 0){
            throw new SearchNotFoundException("No Show Found");
        }
        return shows;
    }
}
