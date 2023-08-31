package com.yigitcanyontem.forum.service;

import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.entity.FavGame;
import com.yigitcanyontem.forum.repository.FavGameRepository;
import com.yigitcanyontem.forum.entity.Users;
import com.yigitcanyontem.forum.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavGameService {
    private final FavGameRepository favGameRepository;
    private final UsersRepository usersRepository;


    public List<FavGame> findByUserId(Users usersid){
        if (!favGameRepository.existsByUsersid(usersid)){
            throw new SearchNotFoundException("User Not Found");
        }
        return favGameRepository.findByUsersid(usersid);
    }

    @Transactional
    public void deleteUserFavGame(Users usersid){
        if (!favGameRepository.existsByUsersid(usersid)){
            throw new SearchNotFoundException("User Not Found");
        }
        favGameRepository.deleteFavGameByUsersid(usersid);
    }

    @Transactional
    public void deleteUserFavGameById(Users usersid, String gameid){
        if (!favGameRepository.existsByUsersid(usersid)){
            throw new SearchNotFoundException("User Not Found");
        }
        if (!favGameRepository.existsFavGameByUsersidAndGameid(usersid,gameid)){
            throw new SearchNotFoundException("Game Not Favorited");
        }
        favGameRepository.deleteFavGameByUsersidAndGameid(usersid,gameid);
    }

    public void saveFavGame(Users usersid, String gameid){
        if (!usersRepository.existsById(usersid.getId())){
            throw new SearchNotFoundException("User Not Found");
        }
        if (favGameRepository.existsFavGameByUsersidAndGameid(usersid,gameid)){
            throw new SearchNotFoundException("Already Favorited");
        }
        favGameRepository.save(new FavGame(usersid,gameid));
    }
}
