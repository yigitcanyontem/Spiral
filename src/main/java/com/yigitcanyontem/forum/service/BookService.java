package com.yigitcanyontem.forum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigitcanyontem.forum.entity.FavAlbums;
import com.yigitcanyontem.forum.entity.FavBooks;
import com.yigitcanyontem.forum.entity.FavGame;
import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.model.entertainment.Album;
import com.yigitcanyontem.forum.model.entertainment.Game;
import com.yigitcanyontem.forum.repository.FavBooksRepository;
import com.yigitcanyontem.forum.model.entertainment.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class BookService {
    private final FavBooksRepository favBooksRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Book> getBookSearchResults(String title) throws JsonProcessingException {
        String url = "https://www.googleapis.com/books/v1/volumes?q="+title;
        String json = restTemplate.getForObject(url,String.class);
        JsonNode list = objectMapper.readTree(json).findValue("items");
        List<Book> books = new ArrayList<>();
        if (list != null){
            for (int i = 0; i < list.size(); i++){
                Book book = new Book();

                try {
                    book.setId(list.get(i).findValue("id").asText());
                    book.setTitle(list.get(i).findValue("title").asText());
                }catch (Exception e){
                    continue;
                }

                book.setCover_url("https://books.google.com/books/publisher/content/images/frontcover/"+book.getId()+"?fife=w400-h600");
                books.add(book);
            }
        }
        if (books.size() == 0){
            throw new SearchNotFoundException("No Book Found");
        }
        return books;
    }

    public List<Book> getGameDTOsByUser(List<FavBooks> favlist) throws ExecutionException, InterruptedException, JsonProcessingException {
        List<CompletableFuture<String>> urlList = new ArrayList<>();
        for (FavBooks book:favlist){
            String url = "https://www.googleapis.com/books/v1/volumes/"+book.getBookid();
            urlList.add(CompletableFuture.supplyAsync(() -> restTemplate.getForObject(url,String.class)));
        }

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(urlList.toArray(new CompletableFuture[0]));
        combinedFuture.get();

        try {
            combinedFuture.get();
        } catch (Exception e) {
            throw e;
        }

        List<Book> books = new ArrayList<>();

        for (CompletableFuture<String> future : urlList) {
            try {
                Book book = new Book();

                JsonNode list = objectMapper.readTree(future.get());
                book.setId(list.findValue("id").asText());
                book.setTitle(list.findValue("title").asText());
                book.setCover_url("https://books.google.com/books/publisher/content/images/frontcover/"+book.getId()+"?fife=w400-h600");
                books.add(book);
            } catch (Exception e) {
                throw e;
            }
        }
        return books;
    }


    public Book getSingleBookById(String bookid) throws JsonProcessingException {
        String url = "https://www.googleapis.com/books/v1/volumes/"+bookid;
        try {
            String json = restTemplate.getForObject(url,String.class);
        }catch (Exception ignored){
            throw new SearchNotFoundException("Book with id " + bookid + " doesn't exist");
        }
        String json = restTemplate.getForObject(url,String.class);

        JsonNode list = objectMapper.readTree(json);

        List<String> authors = new ArrayList<>();


        Book book = new Book();
        book.setId(list.findValue("id").asText());
        book.setTitle(list.findValue("title").asText());

        for (JsonNode authorNode:list.findValue("authors")){
            authors.add(authorNode.asText());

        }
        book.setAuthors(authors);
        book.setDescription(list.findValue("description").asText());
        book.setPageCount(list.findValue("pageCount").asInt());
        book.setCover_url("https://books.google.com/books/publisher/content/images/frontcover/"+book.getId()+"?fife=w400-h600");
        book.setLanguage(list.findValue("language").asText());
        book.setPublishedDate(list.findValue("publishedDate").asText());
        try {
            book.setCategories(Arrays.stream(list.findValue("categories").get(0).asText().split(" / ")).toList());
        }catch (Exception e){
            book.setCategories(null);
        }
        book.setWebReaderLink(list.findValue("webReaderLink").asText());
        book.setFavorite_count(favBooksRepository.countFavBooksByBookid(bookid));
        return book;
    }

}
