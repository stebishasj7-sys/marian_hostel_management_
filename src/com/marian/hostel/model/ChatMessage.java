package com.marian.hostel.model;

import java.util.*;

public class ChatMessage {
    private String id;
    private String studentId;
    private String studentName;
    private String wardenId;
    private String wardenName;
    private String senderId;
    private String senderName;
    private String senderRole; // "STUDENT" or "WARDEN"
    private String message;
    private long timestamp;
    private String timeFormatted;
    private boolean isRead;

    public ChatMessage() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.isRead = false;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("studentId", studentId != null ? studentId : "");
        map.put("studentName", studentName != null ? studentName : "");
        map.put("wardenId", wardenId != null ? wardenId : "");
        map.put("wardenName", wardenName != null ? wardenName : "");
        map.put("senderId", senderId != null ? senderId : "");
        map.put("senderName", senderName != null ? senderName : "");
        map.put("senderRole", senderRole != null ? senderRole : "STUDENT");
        map.put("message", message != null ? message : "");
        map.put("timestamp", timestamp);
        map.put("timeFormatted", timeFormatted != null ? timeFormatted : "");
        map.put("isRead", isRead);
        return map;
    }

    public static ChatMessage fromMap(Map<String, Object> map) {
        ChatMessage m = new ChatMessage();
        if (map.containsKey("id")) m.setId((String) map.get("id"));
        if (map.containsKey("studentId")) m.setStudentId((String) map.get("studentId"));
        if (map.containsKey("studentName")) m.setStudentName((String) map.get("studentName"));
        if (map.containsKey("wardenId")) m.setWardenId((String) map.get("wardenId"));
        if (map.containsKey("wardenName")) m.setWardenName((String) map.get("wardenName"));
        if (map.containsKey("senderId")) m.setSenderId((String) map.get("senderId"));
        if (map.containsKey("senderName")) m.setSenderName((String) map.get("senderName"));
        if (map.containsKey("senderRole")) m.setSenderRole((String) map.get("senderRole"));
        if (map.containsKey("message")) m.setMessage((String) map.get("message"));
        if (map.containsKey("timestamp") && map.get("timestamp") instanceof Number) {
            m.setTimestamp(((Number) map.get("timestamp")).longValue());
        }
        if (map.containsKey("timeFormatted")) m.setTimeFormatted((String) map.get("timeFormatted"));
        if (map.containsKey("isRead")) m.setRead(Boolean.TRUE.equals(map.get("isRead")));
        return m;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getWardenId() { return wardenId; }
    public void setWardenId(String wardenId) { this.wardenId = wardenId; }
    public String getWardenName() { return wardenName; }
    public void setWardenName(String wardenName) { this.wardenName = wardenName; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getSenderRole() { return senderRole; }
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getTimeFormatted() { return timeFormatted; }
    public void setTimeFormatted(String timeFormatted) { this.timeFormatted = timeFormatted; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
