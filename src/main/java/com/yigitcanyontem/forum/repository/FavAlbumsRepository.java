package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.FavAlbums;
import com.yigitcanyontem.forum.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavAlbumsRepository extends JpaRepository<FavAlbums,String> {
    List<FavAlbums> findByUsersid(Users usersid);
    @Query("SELECT max(a.id) FROM FavAlbums a")
    int maxId();

    int countFavAlbumsByUsersid(Users usersid);
    int countFavAlbumsByAlbumid(String albumid);
    boolean existsFavAlbumsByUsersid(Users usersid);
    boolean existsFavAlbumsByUsersidAndAlbumid(Users usersid, String albumid);
    void deleteFavAlbumsByUsersid(Users usersid);
    void deleteFavAlbumsByUsersidAndAlbumid(Users usersid, String albumid);
}
