package com.project.messenger.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "private_chat")
@EqualsAndHashCode
public class PrivateChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @JsonBackReference
    private UserProfile sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    @JsonBackReference
    private UserProfile receiver;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "privateChat", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<PrivateChatFiles> privateChatFiles;

    @OneToMany(mappedBy = "privateChat", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<PrivateChatMessage> privateChatMessages;
}
