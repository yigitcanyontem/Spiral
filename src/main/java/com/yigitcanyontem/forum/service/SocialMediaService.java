package com.yigitcanyontem.forum.service;

import com.yigitcanyontem.forum.repository.SocialMediaRepository;
import com.yigitcanyontem.forum.entity.Users;
import com.yigitcanyontem.forum.entity.SocialMedia;
import com.yigitcanyontem.forum.model.socialmedia.SocialMediaDTO;
import jakarta.transaction.Transactional;
import org.springframework.aop.AopInvocationException;
import org.springframework.stereotype.Service;

@Service
public class SocialMediaService {
    private final SocialMediaRepository socialMediaRepository;

    public SocialMediaService(SocialMediaRepository socialMediaRepository) {
        this.socialMediaRepository = socialMediaRepository;
    }

    public Integer max(){
        try {
            return socialMediaRepository.maxId();
        }catch (AopInvocationException e){
            return 0;
        }
    }
    public SocialMedia getSocialMedia(Integer id) {
        return socialMediaRepository.findSocialMediaByUsersid(id);

    }

    public SocialMediaDTO getSocialMediaDTO(Integer id) {
        SocialMedia socialMedia = socialMediaRepository.findSocialMediaByUsersid(id);
        return new SocialMediaDTO(
                socialMedia.getUsersid().getId(),
                socialMedia.getInstagram(),
                socialMedia.getLinkedin(),
                socialMedia.getPinterest(),
                socialMedia.getTwitter()
        );
    }

    public SocialMedia getSocialMediaRef(Users usersid) {
        return socialMediaRepository.getReferenceByUsersid(usersid);
    }
    @Transactional
    public void deleteSocialMedia(Users usersid){
        socialMediaRepository.deleteSocialMediaByUsersid(usersid);
    }
    @Transactional
    public void saveSocialMedia(SocialMedia socialMedia){
        socialMediaRepository.save(socialMedia);
    }

}
