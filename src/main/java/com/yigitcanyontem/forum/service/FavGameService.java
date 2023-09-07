package com.yigitcanyontem.forum.service;

import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.entity.FavGame;
import com.yigitcanyontem.forum.mapper.FavGameDTOMapper;
import com.yigitcanyontem.forum.model.entertainment.FavGameCreateDTO;
import com.yigitcanyontem.forum.model.entertainment.FavGameDTO;
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
    private final FavGameDTOMapper favGameDTOMapper;

    public List<FavGameDTO> findByUserId(Integer usersid){
        if (!favGameRepository.existsByUsersid_Id(usersid)){
            throw new SearchNotFoundException("User Not Found");
        }
        return favGameRepository.findByUsersid_Id(usersid).stream().map(favGameDTOMapper).toList();
    }

    @Transactional
    public void deleteUserFavGame(Users usersid){
        if (!favGameRepository.existsByUsersid_Id(usersid.getId())){
            throw new SearchNotFoundException("User Not Found");
        }
        favGameRepository.deleteFavGameByUsersid(usersid);
    }

    @Transactional
    public void deleteUserFavGameById(Users usersid, String gameid){
        if (!favGameRepository.existsByUsersid_Id(usersid.getId())){
            throw new SearchNotFoundException("User Not Found");
        }
        if (!favGameRepository.existsFavGameByUsersidAndGameid(usersid,gameid)){
            throw new SearchNotFoundException("Game Not Favorited");
        }
        favGameRepository.deleteFavGameByUsersidAndGameid(usersid,gameid);
    }

    public void saveFavGame(FavGameCreateDTO favGameCreateDTO){
        Users user = usersRepository.findById(favGameCreateDTO.getUsersid()).orElseThrow(() -> new SearchNotFoundException("User Not Found"));
        String gameid = favGameCreateDTO.getGameid();

        if (favGameRepository.existsFavGameByUsersidAndGameid(user,gameid)){
            throw new SearchNotFoundException("Already Favorited");
        }

        favGameRepository.save(new FavGame(
                user,
                gameid,
                favGameCreateDTO.getGameName(),
                favGameCreateDTO.getGameImage()
        ));
    }
}
