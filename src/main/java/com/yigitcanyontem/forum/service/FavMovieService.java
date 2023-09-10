package com.yigitcanyontem.forum.service;

import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.entity.FavMovie;
import com.yigitcanyontem.forum.repository.FavMovieRepository;
import com.yigitcanyontem.forum.entity.Users;
import com.yigitcanyontem.forum.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavMovieService {
    private final FavMovieRepository favMovieRepository;
    private final UsersRepository usersRepository;
    public FavMovieService(FavMovieRepository favMovieRepository, UsersRepository usersRepository) {
        this.favMovieRepository = favMovieRepository;
        this.usersRepository = usersRepository;
    }

    public List<FavMovie> findByUserId(Users usersid){
        if (!favMovieRepository.existsByUsersid(usersid)){
            throw new SearchNotFoundException("User Not Found");
        }
        return favMovieRepository.findByUsersid(usersid);
    }

    @Transactional
    public void deleteUserFavMovie(Users usersid){
        if (!favMovieRepository.existsByUsersid(usersid)){
            throw new SearchNotFoundException("User Not Found");
        }
        favMovieRepository.deleteFavMovieByUsersid(usersid);
    }

    @Transactional
    public void deleteUserFavMovieById(Users usersid, Integer movieid){
        if (!favMovieRepository.existsByUsersid(usersid)){
            throw new SearchNotFoundException("User Not Found");
        }
        if (!favMovieRepository.existsFavMovieByUsersidAndMovieid(usersid,movieid)){
            throw new SearchNotFoundException("Movie Not Favorited");
        }
        favMovieRepository.deleteFavMovieByUsersidAndMovieid(usersid,movieid);
    }

    public void saveFavMovie(Users usersid, Integer favmovieid){
        if (!usersRepository.existsById(usersid.getId())){
            throw new SearchNotFoundException("User Not Found");
        }
        if (favMovieRepository.existsFavMovieByUsersidAndMovieid(usersid,favmovieid)){
            throw new SearchNotFoundException("Already Favorited");
        }
        favMovieRepository.save(new FavMovie(usersid,favmovieid));
    }


    public Boolean checkFavoritedMovie(Integer usersid, Integer movieid) {
        return favMovieRepository.existsFavMovieByUsersid_IdAndMovieid(usersid,movieid);
    }
}
