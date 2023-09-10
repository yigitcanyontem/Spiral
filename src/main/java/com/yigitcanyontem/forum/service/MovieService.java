package com.yigitcanyontem.forum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigitcanyontem.forum.entity.FavMovie;
import com.yigitcanyontem.forum.entity.FavShows;
import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.model.entertainment.CrewMember;
import com.yigitcanyontem.forum.model.entertainment.Show;
import com.yigitcanyontem.forum.repository.FavMovieRepository;
import com.yigitcanyontem.forum.model.entertainment.Movie;
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
public class MovieService {
    @Value("${tmdb_api_key}")
    String tmdb_api_key;

    private final FavMovieRepository favMovieRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Movie> getMovieTOByUser(List<FavMovie> favlist) throws JsonProcessingException, ExecutionException, InterruptedException {
        List<CompletableFuture<String>> urlList = new ArrayList<>();
        for (FavMovie movie:favlist){
            String url = "https://api.themoviedb.org/3/movie/"+movie.getMovieid()+"?api_key="+ tmdb_api_key;
            urlList.add(CompletableFuture.supplyAsync(() -> restTemplate.getForObject(url,String.class)));
        }

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(urlList.toArray(new CompletableFuture[0]));
        combinedFuture.get();

        try {
            combinedFuture.get();
        } catch (Exception e) {
            throw e;
        }

        List<Movie> movies = new ArrayList<>();

        for (CompletableFuture<String> future : urlList) {
            try {
                Movie movie = new Movie();
                JsonNode list = objectMapper.readTree(future.get());

                movie.setId(list.get("id").asText());
                movie.setOriginal_title(list.get("original_title").asText());
                movie.setPoster_path("https://image.tmdb.org/t/p/w500"+list.get("poster_path").asText());
                movies.add(movie);
            } catch (Exception e) {
                throw e;
            }
        }
        return movies;
    }


    public Movie getSingleMovieById(Integer movieid) throws JsonProcessingException, ExecutionException, InterruptedException {
        String url = "https://api.themoviedb.org/3/movie/"+movieid+"?api_key="+ tmdb_api_key;
        String credit_url = "https://api.themoviedb.org/3/movie/"+movieid+"/credits?api_key="+ tmdb_api_key;


        CompletableFuture<String> movieInfoFuture = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(url, String.class));
        CompletableFuture<String> creditInfoFuture = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(credit_url, String.class));

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(movieInfoFuture, creditInfoFuture);
        combinedFuture.get();

        String movieJson = movieInfoFuture.get();
        String creditJson = creditInfoFuture.get();

        JsonNode movies = objectMapper.readTree(movieJson);
        JsonNode credits = objectMapper.readTree(creditJson);

        Movie movie = new Movie();

        movie.setId(movies.get("id").asText());
        movie.setAdult(movies.get("adult").asBoolean());
        movie.setBackdrop_path("https://image.tmdb.org/t/p/w500"+movies.get("backdrop_path").asText());
        movie.setOriginal_title(movies.get("original_title").asText());
        movie.setOverview(movies.get("overview").asText());
        movie.setPoster_path("https://image.tmdb.org/t/p/w500"+movies.get("poster_path").asText());
        movie.setRelease_date(movies.get("release_date").asText());
        movie.setLanguage(movies.get("spoken_languages").findValuesAsText("name"));
        movie.setImdb_url("https://www.imdb.com/title/"+movies.get("imdb_id").asText());
        movie.setGenres(movies.get("genres").findValuesAsText("name"));
        movie.setTagline(movies.get("tagline").asText());
        movie.setVote_count(movies.get("vote_count").asInt());
        movie.setFavorite_count(favMovieRepository.countFavMoviesByMovieid(movieid));

        List<CrewMember> directors = new ArrayList<>();
        List<CrewMember> actors = new ArrayList<>();
        List<CrewMember> writers = new ArrayList<>();


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
            if (job.equals("Director")) {
                crewMember.setId(crew.get("id").asInt());
                crewMember.setGender(crew.get("gender").asInt());
                crewMember.setOriginal_name(crew.get("original_name").asText());
                crewMember.setProfile_path("https://image.tmdb.org/t/p/original"+crew.get("profile_path").asText());
                directors.add(crewMember);
            }else if (job.equals("Screenplay")) {
                crewMember.setId(crew.get("id").asInt());
                crewMember.setGender(crew.get("gender").asInt());
                crewMember.setOriginal_name(crew.get("original_name").asText());
                crewMember.setProfile_path("https://image.tmdb.org/t/p/original"+crew.get("profile_path").asText());
                writers.add(crewMember);
            }
        }

        movie.setActors(actors);
        movie.setDirectors(directors);
        movie.setWriters(writers);
        return movie;
    }
    public List<Movie> getMovieSearchResults(String title) throws JsonProcessingException {
        String url = "https://api.themoviedb.org/3/search/movie?api_key="+ tmdb_api_key +"&query="+title;
        String json = restTemplate.getForObject(url,String.class);
        JsonNode list = objectMapper.readTree(json).findValue("results");
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            Movie movie = new Movie();
            if(list.get(i).get("poster_path").asText().equals("null")){
                continue;
            }
            movie.setId(list.get(i).get("id").asText());
            movie.setOriginal_title(list.get(i).get("original_title").asText());
            movie.setPoster_path("https://image.tmdb.org/t/p/w500"+list.get(i).get("poster_path").asText());
            movie.setRelease_date(list.get(i).get("release_date").asText());
            movie.setVote_count(list.get(i).get("vote_count").asInt());
            movies.add(movie);
        }
        movies.sort(Comparator.comparing(Movie::getVote_count).reversed());
        if (movies.size() == 0){
            return List.of();
        }
        return movies;
    }


    public CrewMember getCrewMember(Integer id) throws ExecutionException, InterruptedException, JsonProcessingException {
        String url = "https://api.themoviedb.org/3/person/"+id+"?api_key="+ tmdb_api_key;
        CompletableFuture<String> crewMemberInfoFuture = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(url, String.class));

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(crewMemberInfoFuture);
        combinedFuture.get();

        String result = crewMemberInfoFuture.get();

        JsonNode json = objectMapper.readTree(result);

        CrewMember crewMember = new CrewMember();

        crewMember.setId(json.get("id").asInt());
        crewMember.setGender(json.get("gender").asInt());
        crewMember.setOriginal_name(json.get("name").asText());
        crewMember.setProfile_path("https://image.tmdb.org/t/p/original"+json.get("profile_path").asText());
        crewMember.setPlace_of_birth(json.get("place_of_birth").asText());
        crewMember.setImdb_id(json.get("imdb_id").asText());
        crewMember.setBirthday(json.get("birthday").asText());
        crewMember.setDeathday(json.get("deathday").asText());
        crewMember.setBiography(json.get("biography").asText());
        crewMember.setKnown_for_department(json.get("known_for_department").asText());



        return crewMember;
    }
}
