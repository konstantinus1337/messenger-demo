package com.project.messenger.services;

import com.project.messenger.models.GroupChatMessages;
import com.project.messenger.models.PrivateChatMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupChatMessageService {

    @Autowired
    private RedisTemplate<String, GroupChatMessages> redisTemplate;

    public void saveMessage(GroupChatMessages message) {
        String key = "messages:" + message.getGroupChat();
        redisTemplate.opsForList().rightPush(key, message);
    }

    public List<GroupChatMessages> getMessages(String receiver) {
        String key = "messages:" + receiver;
        return redisTemplate.opsForList().range(key, 0, -1)
                .stream()
                .collect(Collectors.toList());
    }
}