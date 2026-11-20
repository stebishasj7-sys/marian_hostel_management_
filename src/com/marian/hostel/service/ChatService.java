package com.marian.hostel.service;

import com.marian.hostel.model.*;
import com.marian.hostel.repository.DataStore;

import java.text.SimpleDateFormat;
import java.util.*;

public class ChatService {
    private final DataStore dataStore = DataStore.getInstance();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    public Map<String, Object> getChat(String activeUserId, String targetStudentId) {
        Map<String, Object> res = new LinkedHashMap<>();
        User user = dataStore.getUserById(activeUserId);
        if (user == null) {
            res.put("success", false);
            res.put("message", "User not found.");
            return res;
        }

        boolean isWarden = "WARDEN".equalsIgnoreCase(user.getRole());
        String chatStudentId = isWarden ? (targetStudentId != null ? targetStudentId : "") : user.getId();

        List<Map<String, Object>> messages = new ArrayList<>();
        if (!chatStudentId.isEmpty()) {
            for (ChatMessage m : dataStore.getAllChatMessages()) {
                if (m.getStudentId().equals(chatStudentId)) {
                    messages.add(m.toMap());
                    if (!m.getSenderId().equals(activeUserId)) {
                        m.setRead(true);
                    }
                }
            }
        }

        List<Map<String, Object>> studentConversations = new ArrayList<>();
        if (isWarden) {
            // Provide warden with list of all students
            for (User u : dataStore.getAllUsers()) {
                if ("STUDENT".equalsIgnoreCase(u.getRole())) {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("studentId", u.getId());
                    item.put("studentName", u.getName());
                    item.put("roomNo", u.getRoomNo());
                    item.put("department", u.getDepartment());
                    item.put("photoUrl", u.getPhotoUrl());

                    // Find last message
                    String lastMsg = "No messages yet";
                    String lastTime = "";
                    for (int i = dataStore.getAllChatMessages().size() - 1; i >= 0; i--) {
                        ChatMessage cm = dataStore.getAllChatMessages().get(i);
                        if (cm.getStudentId().equals(u.getId())) {
                            lastMsg = cm.getMessage();
                            lastTime = cm.getTimeFormatted();
                            break;
                        }
                    }
                    item.put("lastMessage", lastMsg);
                    item.put("lastTime", lastTime);
                    studentConversations.add(item);
                }
            }
        }

        res.put("success", true);
        res.put("isWarden", isWarden);
        res.put("activeChatStudentId", chatStudentId);
        res.put("messages", messages);
        res.put("conversations", studentConversations);
        return res;
    }

    public Map<String, Object> sendMessage(String senderId, String targetStudentId, String messageText) {
        Map<String, Object> res = new LinkedHashMap<>();
        User sender = dataStore.getUserById(senderId);
        if (sender == null) {
            res.put("success", false);
            res.put("message", "Sender not found.");
            return res;
        }

        if (messageText == null || messageText.trim().isEmpty()) {
            res.put("success", false);
            res.put("message", "Message text cannot be empty.");
            return res;
        }

        boolean isWarden = "WARDEN".equalsIgnoreCase(sender.getRole());
        String studentId;
        String studentName;
        String wardenId;
        String wardenName;

        if (isWarden) {
            studentId = targetStudentId;
            User st = dataStore.getUserById(studentId);
            studentName = st != null ? st.getName() : "Student";
            wardenId = sender.getId();
            wardenName = sender.getName();
        } else {
            studentId = sender.getId();
            studentName = sender.getName();
            // Default to first warden
            wardenId = "warden-01";
            wardenName = "Hostel Warden";
            for (User u : dataStore.getAllUsers()) {
                if ("WARDEN".equalsIgnoreCase(u.getRole())) {
                    wardenId = u.getId();
                    wardenName = u.getName();
                    break;
                }
            }
        }

        ChatMessage cm = new ChatMessage();
        cm.setStudentId(studentId);
        cm.setStudentName(studentName);
        cm.setWardenId(wardenId);
        cm.setWardenName(wardenName);
        cm.setSenderId(sender.getId());
        cm.setSenderName(sender.getName());
        cm.setSenderRole(sender.getRole());
        cm.setMessage(messageText.trim());
        cm.setTimeFormatted(timeFormat.format(new Date()));
        cm.setRead(false);

        dataStore.addChatMessage(cm);
        dataStore.saveToFile();

        res.put("success", true);
        res.put("message", "Message sent securely.");
        res.put("chatMessage", cm.toMap());
        return res;
    }
}
