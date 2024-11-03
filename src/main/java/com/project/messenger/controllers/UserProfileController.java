package com.project.messenger.controllers;

import com.project.messenger.dto.UserProfileDTO;
import com.project.messenger.models.UserProfile;
import com.project.messenger.repositories.UserProfileRepository;
import com.project.messenger.security.JWTUtil;
import com.project.messenger.services.S3Service;
import com.project.messenger.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserProfileRepository userProfileRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable int userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getCurrentUserProfile(@RequestHeader("Authorization") String token) {
        int userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PatchMapping("/update")
    public ResponseEntity<UserProfile> updateUserProfile(@RequestBody UserProfileDTO userProfileDTO,
                                                         @RequestHeader("Authorization") String token) {
        int userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        UserProfile updatedUserProfile = modelMapper.map(userProfileDTO, UserProfile.class);
        userProfileService.updateUserProfile(userId, updatedUserProfile);
        return ResponseEntity.ok(updatedUserProfile);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.FOUND)
    public String deleteUserProfile(@RequestHeader("Authorization") String token) {
        int userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        userProfileService.deleteUserProfile(userId);
        return "redirect:/index";
    }

    @GetMapping("/friendList")
    public ResponseEntity<List<UserProfile>> getFriendList(@RequestHeader("Authorization") String token) {
        int userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        List<UserProfile> friendList = userProfileService.getFriendList(userId);
        return ResponseEntity.ok(friendList);
    }

    @GetMapping("/search") public ResponseEntity<List<UserProfile>> searchUsers(@RequestParam String query) {
        List<UserProfile> searchResults = userProfileService.searchUsers(query);
        return ResponseEntity.ok(searchResults);
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> addUserAvatar(@RequestHeader("Authorization") String token,
                                                     @RequestParam MultipartFile file) {
        try {
            String getExtension = file.getContentType();
            if (getExtension == null || !getExtension.startsWith("image/")) {
                return ResponseEntity.badRequest().body("You can upload only image as avatar");
            }
            int userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
            String avatarURL = userProfileService.uploadAvatar(userId, file);
            return ResponseEntity.ok(avatarURL);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload avatar");
        }
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<?> deleteUserAvatar(@RequestHeader("Authorization") String token) {
        int userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        userProfileService.deleteAvatar(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/avatar")
    public ResponseEntity<?> getUserAvatarLink(@RequestHeader("Authorization") String token) {
        int userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        String avatarFileName = userProfileRepository.findById(userId).get().getAvatar();
        String avatarLink = userProfileService.getAvatarLink(avatarFileName);
        return ResponseEntity.ok(avatarLink);
    }

//    месседж маппинг?
//    добавление в друзья уже на странице другого юзера
    @PostMapping("/addFriend")
    @ResponseStatus(HttpStatus.FOUND)
    public String addFriend (@RequestHeader("Authorization") String token,
                                       @RequestParam int friendId) {
        int userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        userProfileService.addFriend(userId, friendId);
        return "redirect:/friendList";
    }

    //    месседж маппинг?
//    добавление в друзья на странице со списком друзей (отдельная кнопка удалить друга)
    @DeleteMapping("/deleteFriend")
    @ResponseStatus(HttpStatus.FOUND)
    public String deleteFriend (@RequestHeader("Authorization") String token,
                             @RequestParam int friendId) {
        int userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        userProfileService.deleteFriend(userId, friendId);
        return "redirect:/friendList";
    }

}
