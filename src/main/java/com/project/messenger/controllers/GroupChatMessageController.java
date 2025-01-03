package com.project.messenger.controllers;

import com.project.messenger.dto.GroupChatMessageDTO;
import com.project.messenger.models.GroupChat;
import com.project.messenger.models.GroupChatMessage;
import com.project.messenger.security.JWTUtil;
import com.project.messenger.services.GroupChatMessageService;
import com.project.messenger.services.GroupChatService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RequestMapping("api/group-message")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class GroupChatMessageController {

    private final GroupChatMessageService groupChatMessageService;
    private final GroupChatService groupChatService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    @GetMapping("/{groupChatId}")
    public ResponseEntity<List<GroupChatMessageDTO>> getGroupChatMessages(
            @PathVariable int groupChatId) {
        List<GroupChatMessageDTO> messages = groupChatMessageService.getGroupChatMessages(groupChatId);
        return ResponseEntity.ok(messages);
    }

    @MessageMapping("/group.send")
    public void handleGroupMessage(@Payload Map<String, Object> payload,
                                   SimpMessageHeaderAccessor headerAccessor) {
        String token = headerAccessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            int senderId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
            int chatId = (Integer) payload.get("chatId");
            String message = (String) payload.get("message");

            try {
                groupChatMessageService.sendMessage(senderId, chatId, message);
            } catch (Exception e) {
                throw new RuntimeException("Failed to send message: " + e.getMessage());
            }
        }
    }

    @MessageMapping("/group.delete")
    public void handleDeleteMessage(@Payload Map<String, Object> payload,
                                    SimpMessageHeaderAccessor headerAccessor) {
        String token = headerAccessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            int messageId = (Integer) payload.get("messageId");
            try {
                groupChatMessageService.deleteGroupMessage(messageId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete message: " + e.getMessage());
            }
        }
    }

    @MessageMapping("/group.edit")
    public void handleEditMessage(@Payload Map<String, Object> payload,
                                  SimpMessageHeaderAccessor headerAccessor) {
        String token = headerAccessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            int messageId = (Integer) payload.get("messageId");
            String editedMessage = (String) payload.get("editedMessage");
            try {
                groupChatMessageService.editGroupMessage(messageId, editedMessage);
            } catch (Exception e) {
                throw new RuntimeException("Failed to edit message: " + e.getMessage());
            }
        }
    }



}
//    @PostMapping("/{groupChatId}")
//    public ResponseEntity<GroupChatMessageDTO> sendMessage(
//            @RequestHeader("Authorization") String token,
//            @PathVariable int groupChatId,
//            @RequestParam String message) throws AccessDeniedException {
//        int memberId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
//        GroupChat groupChat = modelMapper.map(groupChatService.getGroupChat(groupChatId, memberId), GroupChat.class);
//        if (groupChat == null) {
//            throw new AccessDeniedException("Access denied");
//        }
//        GroupChatMessageDTO groupChatMessage = groupChatMessageService.sendMessage(memberId, groupChatId, message);
//        return ResponseEntity.ok(groupChatMessage);
//    }
//    @DeleteMapping("/{MessageId}")
//    public ResponseEntity<String> deleteMessage(
//            @PathVariable int MessageId) {
//        groupChatMessageService.deleteGroupMessage(MessageId);
//        return ResponseEntity.ok("Message was deleted");
//    }
//
//    @PatchMapping("/{messageId}")
//    public ResponseEntity<GroupChatMessage> editMessage(
//            @PathVariable int messageId,
//            @RequestParam String editedTextMessage) {
//        GroupChatMessage updatedMessage = groupChatMessageService.editGroupMessage(messageId, editedTextMessage);
//        return ResponseEntity.ok(updatedMessage);
//    }
