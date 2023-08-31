package com.yigitcanyontem.forum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.repository.FavBooksRepository;
import com.yigitcanyontem.forum.model.entertainment.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    public Book getSingleBookById(String bookid) throws JsonProcessingException {
        String url = "https://www.googleapis.com/books/v1/volumes/"+bookid;
        try {
            String json = restTemplate.getForObject(url,String.class);
        }catch (Exception ignored){
            throw new SearchNotFoundException("Book with id " + bookid + " doesn't exist");
        }
        String json = restTemplate.getForObject(url,String.class);

        JsonNode list = objectMapper.readTree(json);

        Book book = new Book();
        book.setId(list.findValue("id").asText());
        book.setTitle(list.findValue("title").asText());
        book.setAuthors(list.findValue("authors").get(0).asText());
        book.setDescription(list.findValue("description").asText());
        book.setPageCount(list.findValue("pageCount").asInt());
        book.setCover_url("https://books.google.com/books/publisher/content/images/frontcover/"+book.getId()+"?fife=w400-h600");
        book.setWebReaderLink(list.findValue("webReaderLink").asText());
        book.setFavorite_count(favBooksRepository.countFavBooksByBookid(bookid));
        return book;
    }

}
