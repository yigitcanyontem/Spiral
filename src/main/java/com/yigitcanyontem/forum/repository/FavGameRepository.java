package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.FavGame;
import com.yigitcanyontem.forum.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavGameRepository extends JpaRepository<FavGame, String> {
    @Query("SELECT max(a.id) FROM FavGame a")
    int maxId();

    List<FavGame> findByUsersid(Users usersid);
    int countFavGamesByUsersid(Users usersid);
    boolean existsByUsersid(Users usersid);
    int countFavGamesByGameid(String gameid);
    boolean existsFavGameByUsersidAndGameid(Users usersid, String gameid);
    void deleteFavGameByUsersid(Users usersid);
    void deleteFavGameByUsersidAndGameid(Users usersid, String gameid);

}
