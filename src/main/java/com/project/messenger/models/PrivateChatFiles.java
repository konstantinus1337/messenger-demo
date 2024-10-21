package com.project.messenger.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "private_chat_files")
public class PrivateChatFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "private_chat_id")
    @JsonBackReference
    private PrivateChat privateChat;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonBackReference
    private UserProfile sender;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "file_url")
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10)
    private FileType type;
}
