package com.project.messenger.models;

import com.fasterxml.jackson.annotation.*;
import com.project.messenger.models.enums.FileType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "group_chat_files")
public class GroupChatFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_chat_id", referencedColumnName = "id")
    @JsonProperty("groupChatId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private GroupChat groupChat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @JsonProperty("senderId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private UserProfile sender;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "file_name")
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10)
    private FileType type;

    public GroupChatFiles(GroupChat groupChat, LocalDateTime sentAt, UserProfile sender, String fileName, FileType type) {
        this.groupChat = groupChat;
        this.sentAt = sentAt;
        this.sender = sender;
        this.fileName = fileName;
        this.type = type;
    }
}
