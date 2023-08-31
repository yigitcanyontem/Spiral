package com.yigitcanyontem.forum.repository;

import com.yigitcanyontem.forum.entity.enums.Status;
import com.yigitcanyontem.forum.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
    @Query("SELECT max(a.id) FROM Users a")
    int maxUsersId();
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Users getUsersByUsername(String username);
    List<Users> findByUsernameContainingAndStatus(String username, Status status);
}
