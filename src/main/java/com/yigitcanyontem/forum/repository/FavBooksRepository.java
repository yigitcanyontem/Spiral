package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.FavBooks;
import com.yigitcanyontem.forum.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavBooksRepository extends JpaRepository<FavBooks,String> {
    List<FavBooks> findByUsersid(Users usersid);
    @Query("SELECT max(a.id) FROM FavBooks a")
    int maxId();
    int countFavBooksByUsersid(Users usersid);
    boolean existsFavBooksByUsersid(Users usersid);
    int countFavBooksByBookid(String bookid);
    boolean existsFavBooksByUsersidAndBookid(Users usersid, String bookid);
    boolean existsFavBooksByUsersid_IdAndBookid(Integer usersid, String bookid);
    void deleteFavBooksByUsersid(Users usersid);
    void deleteFavBooksByUsersidAndBookid(Users usersid, String bookid);

}
