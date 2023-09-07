package com.yigitcanyontem.forum.mapper;

import com.yigitcanyontem.forum.entity.FavGame;
import com.yigitcanyontem.forum.entity.Review;
import com.yigitcanyontem.forum.model.entertainment.FavGameDTO;
import com.yigitcanyontem.forum.model.review.ReviewDTO;
import com.yigitcanyontem.forum.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class FavGameDTOMapper implements Function<FavGame, FavGameDTO> {
    @Override
    public FavGameDTO apply(FavGame favGame) {

        return new FavGameDTO(
                favGame.getId(),
                favGame.getUsersid().getId(),
                favGame.getGameid(),
                favGame.getGameName(),
                favGame.getGameImage()
        );
    }
}
