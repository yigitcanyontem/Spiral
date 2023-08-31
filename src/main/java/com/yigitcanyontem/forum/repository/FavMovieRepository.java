package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.FavMovie;
import com.yigitcanyontem.forum.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavMovieRepository extends JpaRepository<FavMovie,Users> {
    List<FavMovie> findByUsersid(Users usersid);
    @Query("SELECT max(a.id) FROM FavMovie a")
    int maxId();
    int countFavMoviesByUsersid(Users usersid);
    boolean existsByUsersid(Users usersid);
    int countFavMoviesByMovieid(Integer movieid);
    boolean existsFavMovieByUsersidAndMovieid(Users usersid, Integer movieid);
    void deleteFavMovieByUsersid(Users usersid);
    void deleteFavMovieByUsersidAndMovieid(Users usersid, Integer movieid);
}
