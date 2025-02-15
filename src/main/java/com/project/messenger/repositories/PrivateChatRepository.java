package com.project.messenger.repositories;

import com.project.messenger.models.PrivateChat;
import com.project.messenger.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat, Integer> {
    Optional<PrivateChat> findPrivateChatById(int id);
    PrivateChat findPrivateChatBySenderAndReceiver(UserProfile sender, UserProfile receiver);
    List<PrivateChat> findPrivateChatBySender(UserProfile sender);
    List<PrivateChat> findPrivateChatByReceiver(UserProfile receiver);
}
