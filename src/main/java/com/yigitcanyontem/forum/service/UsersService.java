package com.yigitcanyontem.forum.service;

import com.yigitcanyontem.forum.entity.SocialMedia;
import com.yigitcanyontem.forum.entity.Users;
import com.yigitcanyontem.forum.entity.enums.Status;
import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.model.entertainment.AssignModel;
import com.yigitcanyontem.forum.model.users.UserDTO;
import com.yigitcanyontem.forum.model.users.UserSearchDTO;
import com.yigitcanyontem.forum.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final DescriptionService descriptionService;
    private final SocialMediaService socialMediaService;

    public Users getUser(Integer id){
        return usersRepository.findById(id).orElseThrow(
                () -> new SearchNotFoundException(
                        "User with id " + id + " not found"
                )
        );
    }
    public UserDTO getUserModel(Integer id){
        Users users = usersRepository.findById(id).orElseThrow(
                () -> new SearchNotFoundException(
                        "User with id " + id + " not found"
                )
        );
        return new UserDTO(
                users.getId(),
                users.getFirstName(),
                users.getLastName(),
                users.getDate_of_birth(),
                users.getCountry(),
                users.getEmail(),
                users.getUsername()
        );
    }
    public Integer getUserByUsername(String username){
        return usersRepository.getUsersByUsername(username).getId();
    }

    public Users getUsersObjectByUsername(String username){
        return usersRepository.getUsersByUsername(username);
    }

    public List<UserSearchDTO> usersList(String username){
        List<UserSearchDTO> list = usersRepository.findByUsernameContainingAndStatus(username, Status.ACTIVE).stream().map(users -> new UserSearchDTO(
                users.getId(),
                users.getFirstName(),
                users.getLastName(),
                users.getUsername()
        )).toList();
        if (list.isEmpty()){
            return List.of();
        }
        return list;
    }


    public List<String> updateCustomer(AssignModel assignModel) {
        try {
            int id = assignModel.getId();
        }catch (NullPointerException e){
            throw new SearchNotFoundException("User with id " + assignModel.getId() + " not found");
        }

        int id = assignModel.getId();
        String description = assignModel.getDescription();
        String instagramuser = assignModel.getInstagramuser();
        String pinterestuser = assignModel.getPinterestuser();
        String linkedinuser = assignModel.getPinterestuser();
        String twitteruser = assignModel.getTwitteruser();
        SocialMedia socialMedia = socialMediaService.getSocialMedia(id);
        if (!Objects.equals(assignModel.getDescription(), "")){
            descriptionService.saveDescription(id,description);
        }
        if (!Objects.equals(instagramuser, "") && instagramuser != null){
            socialMedia.setInstagram(instagramuser);
        }
        if (!Objects.equals(pinterestuser, "") && pinterestuser != null){
            socialMedia.setPinterest(pinterestuser);
        }
        if (!Objects.equals(linkedinuser, "") && linkedinuser != null){
            socialMedia.setLinkedin(linkedinuser);
        }
        if (!Objects.equals(twitteruser, "") && twitteruser != null){
            socialMedia.setTwitter(twitteruser);
        }
        socialMediaService.saveSocialMedia(socialMedia);
        return List.of("Success");
    }
    @Transactional
    public void deleteCustomer(Integer usersid) {
        if (!usersRepository.existsById(usersid)){
            throw new SearchNotFoundException("User with id "+ usersid + " not found");
        }
        Users users = getUser(usersid);
        users.setStatus(Status.INACTIVE);
    }

     public List<String> uploadPicture(MultipartFile file, Integer id){
        if (file.isEmpty()) {
            return List.of("No file selected");
        }
        try {
            byte[] bytes = file.getBytes();

            String filePath = "photos/" + id+".jpg";
            File serverFile = new File(filePath);

            FileUtils.writeByteArrayToFile(serverFile, bytes);

            return List.of("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return List.of("Error uploading file");
        }
    }

    public Resource getImage(Integer imageName) {
        String imagePath = "photos/" + imageName + ".jpg";
        File imageFile = new File(imagePath);
        Resource resource = new FileSystemResource(imageFile);

        if (!resource.exists()) {
            imagePath = "photos/default.jpg";
            imageFile = new File(imagePath);
            resource = new FileSystemResource(imageFile);
        }
        return resource;
    }
}
