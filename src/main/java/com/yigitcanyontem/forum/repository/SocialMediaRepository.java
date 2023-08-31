package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.Users;
import com.yigitcanyontem.forum.entity.SocialMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Integer> {
    @Query("SELECT max(a.id) FROM SocialMedia a")
    Integer maxId();
    @Query(value = "SELECT * FROM SocialMedia b WHERE b.usersid =?1",nativeQuery = true)
    SocialMedia findSocialMediaByUsersid(Integer usersid);

    SocialMedia getReferenceByUsersid(Users usersid);
    void deleteSocialMediaByUsersid(Users usersid);
}
