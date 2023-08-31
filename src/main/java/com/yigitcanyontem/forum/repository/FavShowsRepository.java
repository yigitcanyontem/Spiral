package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.FavShows;
import com.yigitcanyontem.forum.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavShowsRepository extends JpaRepository<FavShows,Integer> {
    List<FavShows> findByUsersid(Users usersid);
    @Query("SELECT max(a.id) FROM FavShows a")
    int maxId();

    int countFavShowsByShowid(Integer showid);
    int countFavShowsByUsersid(Users usersid);
    boolean existsByUsersid(Users usersid);
    boolean existsByUsersidAndShowid(Users usersid, Integer showid);
    void deleteFavShowsByUsersid(Users usersid);
    void deleteFavShowsByUsersidAndShowid(Users usersid, Integer showid);
}
