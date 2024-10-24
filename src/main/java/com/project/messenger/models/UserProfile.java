package com.project.messenger.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.messenger.models.enums.ProfileStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Size(min = 8, max = 60)
    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Size(min = 3, max = 60)
    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProfileStatus status;

    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^9\\d{9}$")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Email
    @NotNull
    @Column(name = "email")
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<PrivateChat> privateChats;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<PrivateChatFiles> privateChatFiles;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<PrivateChatMessages> privateChatMessages;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<GroupChatFiles> groupChatFiles;

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<FriendList> userList;

    @OneToMany(mappedBy = "friendId", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<FriendList> friendList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<GroupChatMembers> groupChatMembers;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<GroupChatMessages> senderList;


}
