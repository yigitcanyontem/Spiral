package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends JpaRepository<Description,Integer> {

    void deleteDescriptionByUsersid(Integer usersid);
}
